package com.pu.programus.notification.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
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

    //Todo: 메시지 전송 로직
    /**
     * 특정 기기에 메시지 전송
     * @param registrationToken 사용자가 fcm에 등록한 토큰
     * @return 응답 토큰 ex)"name":"projects/myproject-b5ae1/messages/0:1500415314455276%31bd1c9631bd1c96"
     */
//    public String sendMessageByToken(String registrationToken) {
//        Message message = Message.builder()
//                .set
//                .setToken(registrationToken)
//                .build();
//    }

    public void enableFcmToken(String uid, String token) {
        Member member = getMember(uid);

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
}
