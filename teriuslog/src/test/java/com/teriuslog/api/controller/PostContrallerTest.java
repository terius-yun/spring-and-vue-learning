package com.teriuslog.api.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class PostContrallerTest {

    @Autowired
    public MockMvc mockMvc;

//    @Test
//    @DisplayName("/posts 요청시 hello world 출력")
//    void test() throws Exception {
//        //글 제목
//        //글 내용
//
//        //expected
//        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"title\": \"글 제목\", \"content\": \"글 내용\"}")
//                )
//                .andExpect(status().isOk())
//                .andExpect(content().string("{}"))
//                .andDo(MockMvcResultHandlers.print());
//    }

    @Test
    @DisplayName("/posts 요청시 title 값은 필수다.")
    void validateErrorTest() throws Exception {
        //글 제목
        //글 내용

        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"\", \"content\": \"\"}")
                )
//                .andExpect(status().isOk())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(MockMvcResultHandlers.print());
    }
}