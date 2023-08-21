package com.pu.programus.member;

import com.pu.programus.bridge.MemberProject;
import com.pu.programus.location.LocationController;
import com.pu.programus.project.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LocationController.class)
class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService service;

    @MockBean
    private MemberRepository repository;

    @Test
    void getProfileByIdShouldReturnMemberInfoAndProjectInfo() throws Exception {
        // given
        Member member = Member.builder().uid("tom123").build();
        Project project = Project.builder().title("Project1").build();
        MemberProject memberProject = new MemberProject();
        memberProject.setProject(project);
        member.addMemberProject(memberProject);

        repository.save(member);
        // when
        when(service.getProfile("tom123")).thenReturn(member);

        // then
        this.mockMvc.perform(get("/location/tom123"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[\"temp string\"]"));
    }

}