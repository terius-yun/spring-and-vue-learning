package com.teriuslog.api.request;

import com.teriuslog.api.domain.Post;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해주세요.")
    private final String title;

    @NotBlank(message = "콘텐츠를 입력해주세요.")
    private final String content;

    public Post toEntity(){
        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }

    @Builder
    public PostCreate(String title, String content){
        this.title = title;
        this.content = content;
    }
}
