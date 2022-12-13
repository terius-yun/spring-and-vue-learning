package com.teriuslog.api.service;

import com.teriuslog.api.domain.Post;
import com.teriuslog.api.repository.PostRepository;
import com.teriuslog.api.request.PostCreate;
import com.teriuslog.api.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void savePostService() {
        //given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        //when
        postService.write(postCreate);

        //then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void searchOnePost() {
        //give
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);

        //when
        PostResponse responsePost = postService.getOnePost(requestPost.getId());

        //then
        assertNotNull(responsePost);
        assertEquals("foo", requestPost.getTitle());
        assertEquals("bar", requestPost.getContent());
    }

    @Test
    @DisplayName("글 여러개 조회")
    void getPosts() {
        //give
        postRepository.saveAll(List.of(
                Post.builder()
                        .title("title1")
                        .content("content1")
                        .build(),
                Post.builder()
                        .title("title2")
                        .content("content2")
                        .build()
        ));

        //when
        List<PostResponse> posts = postService.getPosts();

        //then
        assertEquals(2L, posts.size());
        assertEquals("title2",posts.get(1).getTitle());
        assertEquals("content2",posts.get(1).getContent());
    }
}