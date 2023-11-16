package com.example.myBlog.service;

import com.example.myBlog.config.DataNotFoundException;
import com.example.myBlog.dto.MemberResponse;
import com.example.myBlog.entity.MemberEntity;
import com.example.myBlog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder; //passwordEncoder 주입

    public void create(String nickname, String password, String name){



        MemberEntity memberEntity = MemberEntity.builder()

                .nickname(nickname)
                .password(passwordEncoder.encode(password))
                .name(name)
                .build();
        memberRepository.save(memberEntity);

        //return  memberEntity.toDto();
    }

    public MemberResponse getMember(String nickname){
        Optional<MemberEntity> memberEntity = memberRepository.findBynickname(nickname);
        if(memberEntity.isPresent()){
            return memberEntity.get().toDto();
        }else{
            throw new DataNotFoundException("MemberEntity not found");
        }
    }
}
