package com.pu.programus.notification.fcm;

import com.pu.programus.notification.bridge.NoticeGroupFcmToken;
import com.pu.programus.member.Member;
import lombok.*;
import org.checkerframework.common.aliasing.qual.Unique;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FcmToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Unique
    private String value;

    @NotNull
    @OneToOne
    private Member member;

    @OneToMany(mappedBy = "fcmToken", cascade = CascadeType.ALL)
    private List<NoticeGroupFcmToken> noticeGroupFcmTokenList = new ArrayList<>();

    public FcmToken(String value, Member member) {
        this.value = value;
        this.member = member;
    }
}
