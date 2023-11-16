package com.example.myBlog.entity;

import com.example.myBlog.common.BaseTimeEntity;
import com.example.myBlog.common.MemberRole;
import com.example.myBlog.dto.MemberResponse;
import lombok.*;
import org.springframework.stereotype.Component;


import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Setter
public class MemberEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nickname;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole = MemberRole.USER;

    @Builder
    public MemberEntity(Long id , String nickname, String password, String name){
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.name = name;
    }

    public MemberEntity(MemberResponse memberResponse) {
    }



    public MemberResponse toDto(){
        return MemberResponse.builder()
                .memberEntity(this)
                .build();

    }
}
