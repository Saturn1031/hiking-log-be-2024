package com.phytoncide.hikinglog.domain.member.Service;

import com.phytoncide.hikinglog.domain.member.dto.EmailMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(EmailMessage emailMessage) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            // 이메일 수신자
            mimeMessageHelper.setTo(emailMessage.getTo());
            // 이메일 제목
            mimeMessageHelper.setSubject(emailMessage.getSubject());
            // 본문 내용
            mimeMessageHelper.setText(emailMessage.getMessage(), false);
            //이메일 발신자 설정
            mimeMessageHelper.setFrom(new InternetAddress(from + "@naver.com"));

            //이메일 발송
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
