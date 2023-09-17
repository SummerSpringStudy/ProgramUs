package com.pu.programus.notification.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.messaging.*;
import com.pu.programus.notification.bridge.NoticeGroupFcmToken;
import com.pu.programus.notification.bridge.NoticeGroupFcmTokenRepository;
import com.pu.programus.member.Member;
import com.pu.programus.member.MemberRepository;
import com.pu.programus.notification.noticegroup.NoticeGroup;
import com.pu.programus.notification.noticegroup.NoticeGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FcmService {

    /**
     * 전송 요령
     * https://firebase.google.com/docs/cloud-messaging/send-message?hl=ko&authuser=0
     */

    private final MemberRepository memberRepository;
    private final FcmTokenRepository fcmTokenRepository;
    private final NoticeGroupRepository noticeGroupRepository;
    private final NoticeGroupFcmTokenRepository noticeGroupFcmTokenRepository;

    @Value("${firebase.admin.sdk.key}")
    String firebaseSdkKeyPath;

    @PostConstruct
    public void init() throws IOException {
        initFirebaseApp();
    }
    public void sendMessageToGroup(String groupName, Map<String, String> data) throws FirebaseMessagingException {
        List<String> getGroupTokenList = getGroupTokenList(groupName);

        MulticastMessage message = makeMulticastMessage(data, getGroupTokenList);

        BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
        checkFailedTokens(getGroupTokenList, response);
    }

    public void enableFcmToken(String uid, String token) throws FirebaseAuthException {
        Member member = getMember(uid);

        FirebaseAuth.getInstance().verifyIdToken(token);
        FcmToken fcmToken = new FcmToken(token, member);
        member.setFcmToken(fcmToken);
        fcmTokenRepository.save(fcmToken);
    }

    public void disableFcmToken(String uid) {
        Member member = getMember(uid);

        FcmToken fcmToken = member.getFcmToken();
        if (fcmToken == null)
            return;

        member.setFcmToken(null);
        fcmTokenRepository.delete(fcmToken);
    }

    // Todo: 관리자 권한으로 설정할 수 있게끔, 또는 채팅방 생성 등등
    public void createNoticeGroup(String groupName) {
        noticeGroupRepository.findNoticeGroupByValue(groupName)
                .ifPresent(m -> { throw new IllegalArgumentException("이미 있는 groupId 입니다."); });

        NoticeGroup noticeGroup = new NoticeGroup(groupName);
        noticeGroupRepository.save(noticeGroup);
    }

    // Todo: 관리자 권한으로 설정할 수 있게끔, 또는 채팅방 생성 등등
    public void deleteNoticeGroup(String groupName) {
        NoticeGroup noticeGroup = getNoticeGroup(groupName);

        noticeGroupRepository.delete(noticeGroup);
    }

    public void joinNoticeGroup(String uid, String groupName) {
        Member member = getMember(uid);
        FcmToken fcmToken = getFcmToken(member);

        NoticeGroup noticeGroup = getNoticeGroup(groupName);

        NoticeGroupFcmToken noticeGroupFcmToken = new NoticeGroupFcmToken(noticeGroup, fcmToken);
        noticeGroupFcmTokenRepository.save(noticeGroupFcmToken);
    }

    public void outNoticeGroup(String uid, String groupName) {
        NoticeGroupFcmToken noticeGroupFcmToken = getNoticeGroupFcmToken(uid, groupName);

        noticeGroupFcmTokenRepository.delete(noticeGroupFcmToken);
    }

    private NoticeGroupFcmToken getNoticeGroupFcmToken(String uid, String groupName) {
        Member member = getMember(uid);
        FcmToken fcmToken = getFcmToken(member);
        NoticeGroup noticeGroup = getNoticeGroup(groupName);

        NoticeGroupFcmToken noticeGroupFcmToken = noticeGroupFcmTokenRepository.findNoticeGroupFcmTokenByNoticeGroupAndFcmToken(noticeGroup, fcmToken)
                .orElseThrow(() -> new IllegalArgumentException(groupName + " 그룹에 가입되지 않은 uid 입니다."));
        return noticeGroupFcmToken;
    }

    private NoticeGroup getNoticeGroup(String groupName) {
        return noticeGroupRepository.findNoticeGroupByValue(groupName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 그룹입니다."));
    }

    private FcmToken getFcmTokenByUid(String uid) {
        Member member = getMember(uid);

        return getFcmToken(member);
    }

    private FcmToken getFcmToken(Member member) {
        FcmToken token = member.getFcmToken();
        if (token == null)
            throw new IllegalArgumentException("해당 uid에 토큰이 등록되지 않았습니다.");

        return token;
    }

    private Member getMember(String uid) {
        return memberRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 uid입니다."));
    }

    /**
     * Todo: Exception 바꾸기??
     * HTTP v1 전송 요청 승인
     * Firebase Admin SDK 사용자 인증
     */
    private void initFirebaseApp() throws IOException {
        try {
            InputStream serviceAccount = new ClassPathResource(firebaseSdkKeyPath).getInputStream();
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (FileNotFoundException e) {
            throw new IOException("No firebaseSdkKey in path");
        } catch (IOException e) {
            throw new IOException("Credential cannot be created from stream");
        }
        log.info("initFirebaseApp Complete");
    }

    private MulticastMessage makeMulticastMessage(Map<String, String> data, List<String> getGroupTokenList) {
        return MulticastMessage.builder()
                .putAllData(data)
                .addAllTokens(getGroupTokenList)
                .build();
    }

    private List<String> getGroupTokenList(String groupName) {
        List<String> groupList = new ArrayList<>();
        NoticeGroup noticeGroup = getNoticeGroup(groupName);
        for (NoticeGroupFcmToken noticeGroupFcmToken : noticeGroup.getNoticeGroupFcmTokenList()) {
            String token = noticeGroupFcmToken.getFcmToken().getValue();
            groupList.add(token);
        }
        return groupList;
    }

    private void checkFailedTokens(List<String> groupList, BatchResponse response) {
        if (response.getFailureCount() > 0) {
            List<SendResponse> responses = response.getResponses();
            List<String> failedTokens = new ArrayList<>();
            for (int i = 0; i < responses.size(); i++) {
                if (!responses.get(i).isSuccessful()) {
                    // The order of responses corresponds to the order of the registration tokens.
                    failedTokens.add(groupList.get(i));
                }
            }

            log.warn("List of tokens that caused failures: " + failedTokens);
        }
    }
}
