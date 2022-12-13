package com.teriuslog.api.service;

import com.teriuslog.api.domain.Post;
import com.teriuslog.api.repository.PostRepository;
import com.teriuslog.api.request.PostCreate;
import com.teriuslog.api.request.PostSearch;
import com.teriuslog.api.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate){
        postRepository.save(postCreate.toEntity());
    }

    public PostResponse getOnePost(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        return new PostResponse(post);
    }

    public List<PostResponse> getPostList(PostSearch postSearch) {
        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }
}
