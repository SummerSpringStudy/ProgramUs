package com.pu.programus.comment;

import com.pu.programus.annotation.PUTokenApiImplicitParams;
import com.pu.programus.config.security.SecurityConfiguration;
import com.pu.programus.exception.AuthorityException;
import com.pu.programus.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final JwtTokenProvider jwtTokenProvider;

    @PUTokenApiImplicitParams
    @PostMapping
    public void createComment(@RequestHeader(SecurityConfiguration.TOKEN_HEADER) String token, @RequestBody CommentRequestDTO commentRequestDTO) {
        String uid = jwtTokenProvider.getUid(token);
        commentService.create(uid, commentRequestDTO);
    }

    @PUTokenApiImplicitParams
    @PutMapping
    public void updateComment(@RequestHeader(SecurityConfiguration.TOKEN_HEADER) String token, @RequestParam Long commentId, @RequestBody CommentRequestDTO commentRequestDTO) throws AuthorityException {
        String uid = jwtTokenProvider.getUid(token);
        commentService.update(uid, commentId, commentRequestDTO);
    }

    @PUTokenApiImplicitParams
    @DeleteMapping
    public void deleteComment(@RequestHeader(SecurityConfiguration.TOKEN_HEADER) String token, @RequestParam Long commentId) throws AuthorityException{
        String uid = jwtTokenProvider.getUid(token);
        commentService.delete(uid, commentId);

    }
}
