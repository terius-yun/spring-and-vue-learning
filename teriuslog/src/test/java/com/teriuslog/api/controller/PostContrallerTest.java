package com.teriuslog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teriuslog.api.domain.Post;
import com.teriuslog.api.repository.PostRepository;
import com.teriuslog.api.request.PostCreate;
import com.teriuslog.api.request.PostEdit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean(){
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청시 hello world 출력")
    void test() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        String requestJson = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청시 필수값 누락에 대한 Exception.")
    void validateErrorTest() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .content("내용입니다.")
                .build();
        String requestJson = objectMapper.writeValueAsString(request);


        //expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청시 값이 저장.")
    void postSaveTest() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        String requestJson = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson)
                )
                .andExpect(status().isOk())
                .andDo(print());

        //then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.",post.getTitle());
        assertEquals("내용입니다.",post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void requestOnePostTest() throws Exception {
        //given
        Post request = Post.builder()
                .title("123456789012345")
                .content("bar")
                .build();
        postRepository.save(request);

        //expected
        mockMvc.perform(get("/posts/{postId}", request.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(request.getId()))
                .andExpect(jsonPath("$.title").value("123456789012345"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());
        //then
    }

    @Test
    @DisplayName("글 여러개 조회")
    void getPostsTest() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(1,31)
                .mapToObj(i -> Post.builder()
                        .title("terius title "+i)
                        .content("content "+i)
                        .build()).collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        //expected
        mockMvc.perform(get("/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title", is("terius title 30")))
                .andExpect(jsonPath("$[0].content", is("content 30")))
                .andDo(print());
        //then
    }


    @Test
    @DisplayName("글 0번페이지 조회")
    void getPostsTest2() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(1,31)
                .mapToObj(i -> Post.builder()
                        .title("terius title "+i)
                        .content("content "+i)
                        .build()).collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        //expected
        mockMvc.perform(get("/posts?page=0&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title", is("terius title 30")))
                .andExpect(jsonPath("$[0].content", is("content 30")))
                .andDo(print());
        //then
    }

    @Test
    @DisplayName("글 제목 수정")
    void editPostTitleTest() throws Exception {
        //given
        Post post = Post.builder()
                .title("terius title")
                .content("terius content")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("terius edit title")
                .content("terius content")
                .build();

        //expected
        mockMvc.perform(patch("/posts/{postId}",post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title", is(postEdit.getTitle())))
//                .andExpect(jsonPath("$.content", is(post.getContent())))
                .andDo(print());
        //then
    }

    @Test
    @DisplayName("글 내용 수정")
    void editPostContentTest() throws Exception {
        //given
        Post post = Post.builder()
                .title("terius title")
                .content("terius content")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("terius title")
                .content("terius edit content")
                .build();

        //expected
        mockMvc.perform(patch("/posts/{postId}",post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title", is(post.getTitle())))
//                .andExpect(jsonPath("$.content", is(postEdit.getContent())))
                .andDo(print());
        //then
    }

    @Test
    @DisplayName("게시글 삭제 요청")
    void deletePostControllerTest() throws Exception {
        //given
        Post post = Post.builder()
                .title("terius title")
                .content("terius content")
                .build();

        postRepository.save(post);

        //expected
        mockMvc.perform(delete("/posts/{postId}",post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}