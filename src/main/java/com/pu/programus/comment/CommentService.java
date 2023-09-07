package com.pu.programus.comment;

import com.pu.programus.exception.AuthorityException;
import com.pu.programus.member.Member;
import com.pu.programus.member.MemberRepository;
import com.pu.programus.project.Project;
import com.pu.programus.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;

    public void create(String uid, CommentRequestDTO commentRequestDTO){

        Comment comment = buildComment(commentRequestDTO);

        setCommentOwner(uid, comment);

        setCommentProject(commentRequestDTO, comment);

        commentRepository.save(comment);
    }

    private void setCommentProject(CommentRequestDTO commentRequestDTO, Comment comment) {
        Project project = projectRepository.findById(commentRequestDTO.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로젝트입니다."));
        comment.setProject(project);
    }

    private void setCommentOwner(String uid, Comment comment) {
        Member member = memberRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("uid가 존재하지 않습니다."));
        comment.setMember(member);
    }

    public static Comment buildComment(CommentRequestDTO commentRequestDTO){
        return Comment.builder()
                .comment(commentRequestDTO.getComment())
                .build();
    }

    public void update(String uid, Long commentId, CommentRequestDTO commentRequestDTO) throws AuthorityException{

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        checkOwner(comment, uid);

        updateComment(comment, commentRequestDTO);
    }

    private void updateComment(Comment comment, CommentRequestDTO commentRequestDTO){
        comment.setComment(commentRequestDTO.getComment());
    }

    private void checkOwner(Comment comment, String checkUid) throws AuthorityException {

        String ownerUid = comment.getMember().getUid();

        if(!ownerUid.equals(checkUid))
            throw new AuthorityException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
    }

    public void delete(String uid, Long commentId) throws AuthorityException {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        checkOwner(comment, uid);

        commentRepository.delete(comment);
    }

//    public List<Comment> getCommentsByProjectId(Long projectId){
//
//    }
//
//    public List<Comment> getCommentsByUserId(Long uid){
//
//    }
}
