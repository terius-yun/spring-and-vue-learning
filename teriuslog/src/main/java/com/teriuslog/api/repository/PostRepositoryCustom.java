package com.teriuslog.api.repository;

import com.teriuslog.api.domain.Post;
import com.teriuslog.api.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> getList(PostSearch postSearch);
}
