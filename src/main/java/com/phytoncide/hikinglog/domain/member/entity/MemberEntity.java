package com.phytoncide.hikinglog.domain.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uid")
    private Integer uid;

    @Column(name = "email", nullable = false, length = 30)
    private String email;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "name", nullable = false, length = 10)
    private String name;

    @Column(name = "birth", nullable = false)
    private Date birth;

    @Column(name = "sex", nullable = false, length = 50)
    private String sex;

    @Column(name = "phone", nullable = false, length = 50)
    private String phone;

    @Column(name = "image", nullable = false, columnDefinition = "text")
    private String image;

//    @JsonIgnore
//    @Column(name = "activated")
//    private boolean activated;
//
//    @ManyToMany
//    @JoinTable(
//            name = "user_authority",
//            joinColumns = {@JoinColumn(name = "uid", referencedColumnName = "uid")},
//            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
//    private Set<Authority> authorities;

}
