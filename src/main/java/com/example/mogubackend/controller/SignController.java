package com.example.mogubackend.controller;

import com.example.mogubackend.dto.ApiResponse;
import com.example.mogubackend.dto.sign_in.request.SignInRequest;
import com.example.mogubackend.dto.sign_up.request.SignUpRequest;

import com.example.mogubackend.dto.verification_code.request.VerificationCodeRequest;
import com.example.mogubackend.entity.MailVo;
import com.example.mogubackend.service.MailService;
import com.example.mogubackend.service.MemberService;
import com.example.mogubackend.service.SignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원 가입 및 로그인")
@RequiredArgsConstructor
@RestController
@RequestMapping
public class SignController {
    private final SignService signService;
    private final MemberService memberService;

    private final MailService mailService;


    @Operation(summary = "회원 가입")
    @PostMapping("/sign-up")
    public ApiResponse signUp(@RequestBody SignUpRequest request) {
        return ApiResponse.success(signService.registerMember(request));
    }

    @Operation(summary = "로그인")
    @PostMapping("/sign-in")
    public ApiResponse signIn(@RequestBody SignInRequest request) {
        return ApiResponse.success(signService.signIn(request));
    }

    @Operation(summary = "이메일 DB 존재 유무 조회")
    @GetMapping("/check-email")
    public boolean checkEmail(@RequestParam("memberEmail") String memberEmail){
        return memberService.checkEmail(memberEmail);
    }

    @Operation(summary = "메일 인증코드 발급(회원가입 시)")
    @PostMapping("/sendVerificationCode")
    public ApiResponse sendVerificationCode(@RequestParam("memberEmail") String memberEmail) {

        //인증 코드 생성
        String code = memberService.getVerifyCode();

        //인증 코드 저장
        memberService.saveVerificationCode(code, memberEmail);

        //메일 생성 & 전송
        MailVo mail = mailService.createMail(code, memberEmail, "verification");
        mailService.sendMail(mail);
//        log.info("인증 코드 전송 완료");
        return ApiResponse.success("인증 코드가 성공적으로 전송되었습니다.");
    }

    @Operation(summary = "메일 인증코드 확인(회원가입 시)")
    @PostMapping("/verifyCode")
    public ApiResponse verifyCode(@RequestBody VerificationCodeRequest request) {
        boolean isVerified = memberService.verifyCode(request.email(), request.code());
        if (isVerified) {
            return ApiResponse.success("인증이 완료되었습니다.");
        } else {
            return ApiResponse.error("인증 코드가 유효하지 않거나 만료되었습니다.");
        }
    }

    @Operation(summary = "임시 비밀번호 발급")
    @PostMapping("/sendPwd")
    public ApiResponse sendPwdEmail(@RequestParam("memberEmail") String memberEmail) {

        // 임시 비밀번호 생성
        String tmpPassword = memberService.getVerifyCode();

        // 임시 비밀번호 저장
        memberService.updateTmpPassword(tmpPassword, memberEmail);

        // 메일 생성 & 전송
        MailVo mail = mailService.createMail(tmpPassword, memberEmail, "tmpPwd");
        mailService.sendMail(mail);
//        log.info("임시 비밀번호 전송 완료");
        return ApiResponse.success("임시 비밀번호가 성공적으로 발급되었습니다.");
    }

    @Operation(summary = "회원 닉네임 중복 체크")
    @GetMapping("/nicknames/{nickname}")
    public ResponseEntity<String> checkNicknameAvailability(@PathVariable String nickname) {
        boolean isAvailable = memberService.checkNickname(nickname);
        return ResponseEntity.status(HttpStatus.OK).body(isAvailable ? "이미 사용 중인 닉네임입니다." : "사용 가능한 닉네임입니다." );
    }

    @Operation(summary = "회원 전화번호 중복 체크")
    @GetMapping("/phone/{phone}")
    public ResponseEntity<String> checkPhoneAvailability(@PathVariable String phone) {
        boolean isAvailable = memberService.checkPhone(phone);
        return ResponseEntity.status(HttpStatus.OK).body(isAvailable ? "이미 사용 중인 번호입니다." : "사용 가능한 번호입니다." );
    }

    //필요하면 아래 주석 풀어서 쓸 것... 근데 필요 없는거 같아서..
//    @Operation(summary = "이메일 사용 가능 체크(중복 체크)")
//    @GetMapping("/emails/{email}")
//    public ResponseEntity<String> checkEmailAvailability(@PathVariable String email) {
//        boolean isAvailable = memberService.checkEmail(email);
//        return ResponseEntity.status(HttpStatus.OK).body(isAvailable ? "이미 사용 중인 이메일입니다." : "사용 가능한 이메일입니다." );
//    }
//
//    @Operation(summary = "닉네임 사용 가능 체크(중복 체크)")
//    @GetMapping("/nicknames/{nickname}")
//    public ResponseEntity<String> checkNicknameAvailability(@PathVariable String nickname) {
//        boolean isAvailable = memberService.checkNickname(nickname);
//        return ResponseEntity.status(HttpStatus.OK).body(isAvailable ? "이미 사용 중인 닉네임입니다." : "사용 가능한 닉네임입니다." );
//    }
}
