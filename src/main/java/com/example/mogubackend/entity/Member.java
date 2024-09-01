package com.example.mogubackend.entity;

import com.example.mogubackend.common.MemberType;
import com.example.mogubackend.dto.member.request.MemberUpdateRequest;
import com.example.mogubackend.dto.sign_up.request.SignUpRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter //추가
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

//    @Column(nullable = false, scale = 20, unique = true)
//    private String account;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(unique = true)
    private String phone;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private MemberType type;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.REMOVE)
    private List<Product> sellingProducts;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Participation> participation;

//    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Token> tokens;




    public static Member from(SignUpRequest request, PasswordEncoder encoder) {	// 파라미터에 PasswordEncoder 추가
        return Member.builder()
                .email(request.email())
                .nickname(request.nickname())
                .phone(request.phone())
                .password(encoder.encode(request.password()))	// 수정
                .type(MemberType.USER)
                .build();
    }

    @Builder
    private Member(UUID id, String email, String nickname, String phone, String password, MemberType type) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.phone = phone;
        this.password = password;
        this.type = type;
    }

    public void update(MemberUpdateRequest newMember, PasswordEncoder encoder) {	// 파라미터에 PasswordEncoder 추가
        this.password = newMember.newPassword() == null || newMember.newPassword().isBlank()
                ? this.password : encoder.encode(newMember.newPassword());	// 수정
        this.nickname = newMember.nickname();
        this.phone = newMember.phone();
    }

    /** 비밀번호 변경 메서드 **/
    public void updatePassword(String password){
        this.password = password;
    }
}
