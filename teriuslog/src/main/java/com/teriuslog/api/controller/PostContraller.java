package com.teriuslog.api.controller;

import com.teriuslog.api.domain.Post;
import com.teriuslog.api.request.PostCreate;
import com.teriuslog.api.response.PostResponse;
import com.teriuslog.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostContraller {

    private final PostService postService;

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {
        postService.write(request);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse getOnePost(@PathVariable(name = "postId") Long postId) {
        return postService.getOnePost(postId);
    }

    @GetMapping("/posts")
    public List<PostResponse> getPosts(){
        return postService.getPosts();
    }
}
