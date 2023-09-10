package com.pu.programus.notification.noticegroup;

import com.pu.programus.notification.bridge.NoticeGroupFcmToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class NoticeGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Todo: 1대1메시지 같은 경우 특수기호를 사용해 표시하고, 이외에는 값지정 하지 못하게 하기 -> 세터로
    @NotNull
    @Unique
    private String value;

    @OneToMany(mappedBy = "noticeGroup", cascade = CascadeType.ALL)
    private List<NoticeGroupFcmToken> noticeGroupFcmTokenList = new ArrayList<>();

    public NoticeGroup(String value) {
        setValue(value);
    }

    public void setValue(String value) {
        //Todo: 1대1메시지 같은 경우 특수기호를 사용해 표시하고, 이외에는 값지정 하지 못하게 하기
        this.value = value;
    }
}
