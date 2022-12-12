package com.teriuslog.api.request;

import com.teriuslog.api.domain.Post;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해주세요.")
    private String title;

    @NotBlank(message = "콘텐츠를 입력해주세요.")
    private String content;

    public Post toEntity(){
        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }
}
