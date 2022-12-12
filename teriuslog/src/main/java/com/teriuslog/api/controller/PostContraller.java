package com.teriuslog.api.controller;

import com.teriuslog.api.domain.Post;
import com.teriuslog.api.request.PostCreate;
import com.teriuslog.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostContraller {

    private final PostService postService;

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request){
        postService.write(request);
    }

    @GetMapping("/posts/{postId}")
    public Post searchOnePost(@PathVariable(name = "postId") Long id){
        return postService.getOnePost(id);
    }
}
