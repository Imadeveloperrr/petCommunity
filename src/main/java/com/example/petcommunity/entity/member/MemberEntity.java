package com.example.petcommunity.entity.member;

import com.example.petcommunity.dto.member.MemberDTO;
import com.example.petcommunity.role.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "user")
/*
    1. Entity 는 일종의 테이블 역할(DB의 테이블을 일종의 자바 객체처럼 사용할 수 있다.)
*/
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_no")
    private Long userNo;

    @Column(name = "user_id", unique = true, nullable = false)
    private String userId;

    @Column(name = "user_pwd", nullable = false)
    private String userPwd;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_nickname", nullable = false)
    private String userNickname;

    @Column(name = "user_age", nullable = false)
    private int userAge;

    @Column(name = "user_gender", nullable = false)
    private String userGender;

    @Column(name = "user_tel", nullable = false)
    private String userTel;

    @Column(name = "user_addr", nullable = false)
    private String userAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRole userRole;

    @CreationTimestamp
    @Column(name = "user_created", updatable = false)
    private LocalDateTime userCreated;

    @UpdateTimestamp
    @Column(name = "user_updated")
    private LocalDateTime userUpdated;

    public static MemberEntity toMemberEntity(MemberDTO memberDTO) {
        return MemberEntity.builder()
                .userId(memberDTO.getUserId())
                .userPwd(memberDTO.getUserPwd())
                .userName(memberDTO.getUserName())
                .userNickname(memberDTO.getUserNickname())
                .userAge(memberDTO.getUserAge())
                .userGender(memberDTO.getUserGender())
                .userTel(memberDTO.getUserTel())
                .userAddress(memberDTO.getUserAddress())
                .build();
    }
}
