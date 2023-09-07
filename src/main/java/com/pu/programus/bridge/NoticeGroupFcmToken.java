package com.pu.programus.bridge;

import com.pu.programus.fcm.FcmToken;
import com.pu.programus.noticegroup.NoticeGroup;
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
}
