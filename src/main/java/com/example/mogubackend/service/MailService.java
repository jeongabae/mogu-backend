package com.example.mogubackend.service;

import com.example.mogubackend.entity.MailVo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Transactional
@RequiredArgsConstructor
@Slf4j
@Service
public class MailService {

    private final JavaMailSender mailSender;
    private static final String fromAddress = "moguyonsei@gmail.com";

    /** 이메일 생성 **/
    public MailVo createMail(String s, String memberEmail, String type) {
        String title = "";
        String message = "";
        if ("verification".equals(type)) {
            title = "Mogu 이메일 인증 코드 안내";
            message = "안녕하세요. Mogu 이메일 인증 코드 안내 메일입니다.\n"
                    + "회원님의 인증 코드는 아래와 같습니다.\n";
        } else if ("tmpPwd".equals(type)) {
            title = "Mogu 임시 비밀번호 안내 이메일입니다.";
            message = "안녕하세요. Mogu 임시 비밀번호 안내 메일입니다.\n"
                    + "회원님의 임시 비밀번호는 아래와 같습니다. 로그인 후 반드시 비밀번호를 변경해주세요.\n";
        }
        MailVo mailVo = MailVo.builder()
                .toAddress(memberEmail)
                .title(title)
                .message(message + s)
                .fromAddress(fromAddress)
                .build();

//        log.info("메일 생성 완료");
        return mailVo;
    }



    /** 이메일 전송 **/
    public void sendMail(MailVo mailVo) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mailVo.getToAddress());
        mailMessage.setSubject(mailVo.getTitle());
        mailMessage.setText(mailVo.getMessage());
        mailMessage.setFrom(mailVo.getFromAddress());
        mailMessage.setReplyTo(mailVo.getFromAddress());

        mailSender.send(mailMessage);

//        log.info("메일 전송 완료");
    }
}
