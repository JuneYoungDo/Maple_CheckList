package com.maple.checklist.global.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailUtilsService {

    @Value("${custom.server}")
    private String SERVER_VERSION;

    private final JavaMailSender javaMailSender;
    private static final String FROM_ADDRESS = "QuestBookService@gmail.com";   // 변경 필요

    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String ALL_CHARACTERS = UPPERCASE + LOWERCASE + DIGITS;

    private static final SecureRandom RANDOM = new SecureRandom();

    public void sendLogMail() throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // 현재 날짜와 시간을 포함한 제목과 내용 생성
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = dateFormat.format(new Date());

        String subject = "Quest Book Today Log ! _ " + currentDate + " _ " + SERVER_VERSION;
        helper.setSubject(subject);
        String content = "오늘의 로그 파일을 첨부합니다.\n\n날짜 및 시간: " + currentDate;
        helper.setText(content);

        String logDir = "logs";
        String dailyErrorLogPath = logDir + "/daily-error-log.log";
        String dailyLogPath = logDir + "/daily-log.log";

        FileSystemResource file1 = new FileSystemResource(new File(dailyErrorLogPath));
        FileSystemResource file2 = new FileSystemResource(new File(dailyLogPath));
        helper.addAttachment(currentDate + "_daily-error-log.log", file1);
        helper.addAttachment(currentDate + "_daily-log.log", file2);

        helper.setFrom(FROM_ADDRESS);
        helper.setTo(FROM_ADDRESS);
        javaMailSender.send(message);
        log.info("SUCCESS for message sending");
    }

    public String sendAuthMail(String email) {
        String code = generateRandomNumber();
        String title = "QuestBook 서비스에 함께해 주셔서 감사합니다.";
        String emailContent = "안녕하세요, " + email + "님.\n\n" +
            "QuestBook에 함께해 주셔서 감사합니다.\n\n" +
            "회원가입을 완료하려면 이메일 주소를 인증해야 합니다. 아래 6자리 숫자 코드를 서비스 상에 입력해주세요.\n\n" +
            "코드 : " + code + " \n\n" +
            "이 코드는 10분 동안만 유효하므로 가능한 빨리 입력해 주시길 바랍니다.\n\n" +
            "만약 이 이메일 주소로 계정을 생성하지 않으셨다면, 이 이메일을 무시하시거나 저희 서비스팀에 연락해 주세요.\n\n" +
            "QuestBook에 가입해 주셔서 감사합니다.\n\n" +
            "감사합니다,\n" +
            "QuestBook 팀\n\n" +
            "---\n\n" +
            "지원:\n" +
            "질문이 있거나 도움이 필요하시면, 이 이메일에 회신하시거나 QuestBookService@gmail.com으로 지원팀에 연락해 주세요.\n\n" +
            "개인정보 보호:\n" +
            "저희는 사용자의 개인정보를 소중히 여기며, 절대로 타사와 공유하지 않습니다.\n\n" +
            "[QuestBook]\n";
        sendMail(email, title, emailContent);
        return code;
    }

    public String sendResetMail(String email) {
        String code = generatePassword();
        String title = "[QuestBook] 비밀번호 변경 안내";
        String emailContent = "안녕하세요, " + email + "님.\n\n" +
            "QuestBook에서 사용자의 요청에 의해 임시 비밀번호로 비밀번호가 변경되었음을 알려드립니다.\n\n" +
            "변경된 비밀번호는 아래 8자리 문자 및 숫자이며 외부의 유츨에 유의하시기 바랍니다.\n\n" +
            "비밀번호 : " + code + " \n\n" +
            "해당 비밀번호는 임시 비밀번호 임으로 확인 후 빠른 시일내에 변경을 권유드립니다.\n\n" +
            "QuestBook을 이용해 주셔서 감사합니다.\n\n" +
            "감사합니다,\n" +
            "QuestBook 팀\n\n" +
            "---\n\n" +
            "지원:\n" +
            "질문이 있거나 도움이 필요하시면, 이 이메일에 회신하시거나 QuestBookService@gmail.com으로 지원팀에 연락해 주세요.\n\n" +
            "개인정보 보호:\n" +
            "저희는 사용자의 개인정보를 소중히 여기며, 절대로 타사와 공유하지 않습니다.\n\n" +
            "[QuestBook]\n";
        sendMail(email, title, emailContent);
        return code;
    }

    public String generatePassword() {
        StringBuilder password = new StringBuilder(8);

        password.append(UPPERCASE.charAt(RANDOM.nextInt(UPPERCASE.length())));
        password.append(LOWERCASE.charAt(RANDOM.nextInt(LOWERCASE.length())));
        password.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));

        for (int i = 3; i < 8; i++) {
            password.append(ALL_CHARACTERS.charAt(RANDOM.nextInt(ALL_CHARACTERS.length())));
        }

        return shuffleString(password.toString());
    }

    private static String shuffleString(String input) {
        char[] characters = input.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            int randomIndex = RANDOM.nextInt(characters.length);
            char temp = characters[i];
            characters[i] = characters[randomIndex];
            characters[randomIndex] = temp;
        }
        return new String(characters);
    }

    private String generateRandomNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int randomNumber = random.nextInt(10); // 0부터 9까지의 숫자 생성
            sb.append(randomNumber);
        }

        return sb.toString();
    }

    private void sendMail(String to, String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(title);
        message.setText(text);
        message.setFrom(FROM_ADDRESS);
        javaMailSender.send(message);
    }
}
