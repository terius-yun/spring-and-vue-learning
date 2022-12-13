package com.teriuslog.api.service;

import com.teriuslog.api.domain.Post;
import com.teriuslog.api.repository.PostRepository;
import com.teriuslog.api.request.PostCreate;
import com.teriuslog.api.request.PostEdit;
import com.teriuslog.api.request.PostSearch;
import com.teriuslog.api.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    void savePostServiceTest() {
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
    void searchOnePostTest() {
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
    void getPostsTest() {
        //give
        List<Post> requestPosts = IntStream.range(0,20)
                        .mapToObj(i -> Post.builder()
                                .title("terius title "+i)
                                .content("content "+i)
                                .build()).collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .build();
        //when
        List<PostResponse> posts = postService.getPostList(postSearch);

        //then
        assertEquals(10L, posts.size());
        assertEquals("terius title 19", posts.get(0).getTitle());
        assertEquals("terius title 15", posts.get(4).getTitle());
    }

    @Test
    @DisplayName("글 제목 수정")
    void editPostTitleTest() {
        //give
        Post post = Post.builder()
                .title("terius title")
                .content("terius content")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("terius edit title")
                .content("terius content")
                .build();

        //when
        postService.edit(post.getId(),postEdit);

        //then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지않습니다. id= " + post.getId()));
        assertEquals("terius edit title", changedPost.getTitle());
        assertEquals("terius content", changedPost.getContent());
    }

    @Test
    @DisplayName("글 내용 수정")
    void editPostContentTest() {
        //give
        Post post = Post.builder()
                .title("terius title")
                .content("terius content")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("terius title")
                .content("terius edit content")
                .build();

        //when
        postService.edit(post.getId(),postEdit);

        //then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지않습니다. id= " + post.getId()));
        assertEquals("terius title", changedPost.getTitle());
        assertEquals("terius edit content", changedPost.getContent());
    }
}