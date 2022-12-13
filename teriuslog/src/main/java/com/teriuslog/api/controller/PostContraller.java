package com.teriuslog.api.controller;

import com.teriuslog.api.domain.Post;
import com.teriuslog.api.request.PostCreate;
import com.teriuslog.api.response.PostResponse;
import com.teriuslog.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public PostResponse searchOnePost(@PathVariable(name = "postId") Long id) {
        return postService.getOnePost(id);
    }
}
