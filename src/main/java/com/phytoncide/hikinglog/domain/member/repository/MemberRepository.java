package com.phytoncide.hikinglog.domain.member.repository;

import com.phytoncide.hikinglog.domain.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {

    MemberEntity findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<MemberEntity> findByPhone(String phone);
}
