package com.example.mogubackend.service;

import com.example.mogubackend.dto.member.response.MemberDeleteResponse;
import com.example.mogubackend.dto.member.response.MemberInfoResponse;
import com.example.mogubackend.dto.member.response.MemberUpdateResponse;
import com.example.mogubackend.entity.Member;
import com.example.mogubackend.entity.VerificationCode;
import com.example.mogubackend.repository.MemberRepository;
import com.example.mogubackend.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;	// 추가
    private final VerificationCodeRepository verificationCodeRepository;

    /** 이메일이 존재하는지 확인 **/
    public boolean checkEmail(String memberEmail) {
        /* 이메일이 존재하면 true, 이메일이 없으면 false  */
        return memberRepository.existsByEmail(memberEmail);
    }

    /** 회원 정보 가져오기 **/
    @Transactional(readOnly = true)
    public MemberInfoResponse getMemberInfo(UUID id) {
        return memberRepository.findById(id)
                .map(MemberInfoResponse::from)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));
    }

    /** 회원 탈퇴 처리하기 **/
    @Transactional
    public MemberDeleteResponse deleteMember(UUID id) {
        if (!memberRepository.existsById(id)) return new MemberDeleteResponse(false);
        memberRepository.deleteById(id);
        return new MemberDeleteResponse(true);
    }

    /** 닉네임이 중복되는지 확인 **/
    public boolean checkNickname(String nickname) {
        /* 닉네임이 존재하면 true, 닉네임이 없으면 false */
        return memberRepository.existsByNickname(nickname);
    }

    /** 회원 닉네임 업데이트 **/
    @Transactional
    public MemberUpdateResponse updateNickname(UUID id, String nickname) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다."));
        member.setNickname(nickname);
        return MemberUpdateResponse.of(true, member);
    }

    /** 핸드폰번호가 중복되는지 확인 **/
    public boolean checkPhone(String phone) {
        /* 전화번호가 존재하면 true, 아니면 false */
        return memberRepository.existsByPhone(phone);
    }

    /** 회원 전화번호 업데이트 **/
    @Transactional
    public MemberUpdateResponse updatePhone(UUID id, String phone) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다."));
        member.setPhone(phone);
        return MemberUpdateResponse.of(true, member);
    }


    /** 회원 기존 비밀번호 체크 **/
    @Transactional
    public MemberUpdateResponse checkCurrentPassword(UUID id, String currentPassword) {
        Member member = memberRepository.findById(id)
              .orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다."));
        if (!encoder.matches(currentPassword, member.getPassword())) {
         throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }
        return MemberUpdateResponse.of(true, member);
}

    /** 회원 비밀번호 업데이트 **/
    @Transactional
    public MemberUpdateResponse updatePassword(UUID id, String newPassword) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("회원을 찾을 수 없습니다."));
        member.setPassword(encoder.encode(newPassword));
        return MemberUpdateResponse.of(true, member);
    }





    /** 인증 코드 & 임시 비밀번호 만들기 **/
    public String getVerifyCode() {
        char[] charSet = new char[]{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
                'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        String verifyCode = "";

        /* 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 조합 */
        int idx = 0;
        for(int i = 0; i < 10; i++){
            idx = (int) (charSet.length * Math.random());
            verifyCode += charSet[idx];
        }
//        log.info("인증코드 생성");
        return verifyCode;
    }

    /** 임시 비밀번호 업데이트 **/
    public void updateTmpPassword(String tmpPassword, String memberEmail) {
        String encryptPassword = encoder.encode(tmpPassword);
        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        member.updatePassword(encryptPassword);
//        log.info("임시 비밀번호 업데이트");
    }

    /** 인증 코드 저장 **/
    public void saveVerificationCode(String code, String email) {
//        String code = getVerifyCode();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setCode(code);
        verificationCode.setExpirationTime(LocalDateTime.now().plusMinutes(5));
        verificationCodeRepository.save(verificationCode);
//        MailVo mail = mailService.createMail(code, email);
//        mailService.sendMail(mail);
    }

    /** 인증 코드 확인 및 삭제 **/
    public boolean verifyCode(String email, String code) {
        Optional<VerificationCode> verificationCode = verificationCodeRepository.findByEmailAndCode(email, code);
        if (verificationCode.isPresent() && verificationCode.get().getExpirationTime().isAfter(LocalDateTime.now())) {
            verificationCodeRepository.delete(verificationCode.get());
            return true;
        }
        return false;
    }


//    public Member findMemberById(UUID id) {
//        return memberRepository.findById(id)
//                .orElse(null); // 찾지 못할 경우 null 반환
//    }
//
//    public Member findByNickname(String nickname) {
//        return memberRepository.findByNickname(nickname)
//                .orElse(null); // 찾지 못할 경우 null 반환
//    }
}
