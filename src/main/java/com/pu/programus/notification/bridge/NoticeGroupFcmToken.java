package com.pu.programus.notification.bridge;

import com.pu.programus.notification.fcm.FcmToken;
import com.pu.programus.notification.noticegroup.NoticeGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class NoticeGroupFcmToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private NoticeGroup noticeGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    private FcmToken fcmToken;

    public NoticeGroupFcmToken(NoticeGroup noticeGroup, FcmToken fcmToken) {
        this.noticeGroup = noticeGroup;
        this.fcmToken = fcmToken;
    }
}
