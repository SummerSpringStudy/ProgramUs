package com.pu.programus.notification.bridge;

import com.pu.programus.notification.fcm.FcmToken;
import com.pu.programus.notification.noticegroup.NoticeGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeGroupFcmTokenRepository extends JpaRepository<NoticeGroupFcmToken, Long> {
    Optional<NoticeGroupFcmToken> findNoticeGroupFcmTokenByNoticeGroupAndFcmToken(NoticeGroup noticeGroup, FcmToken fcmToken);
}
