package com.example.myBlog.service;

import com.example.myBlog.entity.MemberEntity;
import com.example.myBlog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class MemberSecurityService implements UserDetailsService {

    private final MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String nickname) {
        Optional<MemberEntity> _member = memberRepository.findBynickname(nickname);
        if(_member.isEmpty()){
            return null;
        }
        MemberEntity memberEntity = _member.get();

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("USER"));

        return new org.springframework.security.core.userdetails
                .User(memberEntity.getNickname(), memberEntity.getPassword(), authorities );
    }
}
