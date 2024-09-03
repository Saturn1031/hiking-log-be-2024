package com.phytoncide.hikinglog.domain.member.config;

import com.phytoncide.hikinglog.domain.member.entity.MemberEntity;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class AuthDetails implements UserDetails {

    private MemberEntity memberEntity;

    public AuthDetails(MemberEntity memberEntity) {
        this.memberEntity = memberEntity;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        memberEntity.getUid();
        return List.of();
    }

    @Override
    public String getPassword() { return  memberEntity.getPassword(); }

    @Override
    public String getUsername() {
        return memberEntity.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
