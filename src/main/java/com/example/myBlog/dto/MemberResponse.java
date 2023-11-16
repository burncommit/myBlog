package com.example.myBlog.dto;

import com.example.myBlog.entity.MemberEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberResponse {
    private Long id;
    private String nickname;
    private String password;
    private String name;

    @Builder
    public MemberResponse(MemberEntity memberEntity){
        this.id = memberEntity.getId();
        this.nickname = memberEntity.getNickname();
        this.password = memberEntity.getPassword();
        this.name = memberEntity.getName();
    }

    public MemberEntity toEntity(){
        return MemberEntity.builder()
                .id(id)
                .nickname(nickname)
                .password(password)
                .name(name)
                .build();
    }

}
