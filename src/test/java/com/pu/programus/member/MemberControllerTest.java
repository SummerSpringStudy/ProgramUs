package com.pu.programus.member;

import com.pu.programus.bridge.MemberProject;
import com.pu.programus.bridge.MemberProjectRepository;
import com.pu.programus.location.LocationController;
import com.pu.programus.project.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private MemberService service;

    @Autowired
    private MemberRepository repository;

    @Autowired
    private MemberProjectRepository memberProjectRepository;

    @Test
    @Rollback(value = false)
    void getProfileByIdShouldReturnMemberInfoAndProjectInfo() throws Exception {
        // given
        Member member = Member.builder().uid("tom123").password("pw1234").userName("HongGilDong").build();
        Project project = Project.builder().title("Project1").build();
        MemberProject memberProject = new MemberProject();
        memberProject.setProject(project);
        memberProject.setMember(member);
        member.addMemberProject(memberProject);

        repository.save(member);
        memberProjectRepository.save(memberProject);
        // when
//        service.getProfile("tom123");
        // then
        this.mockMvc.perform(get("/member/tom123"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[\"temp string\"]"));
    }

}