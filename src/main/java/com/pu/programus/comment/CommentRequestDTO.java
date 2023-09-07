package com.pu.programus.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDTO {
//    private String uid;
    private Long projectId;
    private String comment;
}
