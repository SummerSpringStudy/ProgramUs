package com.pu.programus.comment;

import com.pu.programus.annotation.PUTokenApiImplicitParams;
import com.pu.programus.comment.DTO.CommentRequestDTO;
import com.pu.programus.comment.DTO.CommentResponseDTO;
import com.pu.programus.config.security.SecurityConfiguration;
import com.pu.programus.exception.AuthorityException;
import com.pu.programus.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final JwtTokenProvider jwtTokenProvider;

    @PUTokenApiImplicitParams
    @PostMapping("/comment")
    public void createComment(@RequestHeader(SecurityConfiguration.TOKEN_HEADER) String token, @RequestBody CommentRequestDTO commentRequestDTO) {
        String uid = jwtTokenProvider.getUid(token);
        commentService.create(uid, commentRequestDTO);
    }

    @PUTokenApiImplicitParams
    @PutMapping("/comment/{commentId}")
    public void updateComment(@RequestHeader(SecurityConfiguration.TOKEN_HEADER) String token, @PathVariable Long commentId, @RequestBody CommentRequestDTO commentRequestDTO) throws AuthorityException {
        String uid = jwtTokenProvider.getUid(token);
        commentService.update(uid, commentId, commentRequestDTO);
    }

    @PUTokenApiImplicitParams
    @DeleteMapping("/comment/{commentId}")
    public void deleteComment(@RequestHeader(SecurityConfiguration.TOKEN_HEADER) String token, @PathVariable Long commentId) throws AuthorityException{
        String uid = jwtTokenProvider.getUid(token);
        commentService.delete(uid, commentId);

    }

    @GetMapping("/project/{projectId}/comments")
    public List<CommentResponseDTO> getProjectComments(@PathVariable Long projectId){
        return commentService.getCommentsByProjectId(projectId);
    }

    @GetMapping("/user/{memberId}/comments")
    public List<CommentResponseDTO> getUserComments(@PathVariable Long memberId){
        return commentService.getCommentsByUserId(memberId);
    }
}
