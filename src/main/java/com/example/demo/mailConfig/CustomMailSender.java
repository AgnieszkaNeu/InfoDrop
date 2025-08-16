package com.example.demo.mailConfig;

import com.example.demo.exceptions.EmailSendException;
import com.example.demo.news.Article;
import com.example.demo.news.NewsFilter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

@Service
public class CustomMailSender {

    private final JavaMailSender mailSender;

    private final NewsFilter newsFilter;

    private final TemplateEngine templateEngine;

    public CustomMailSender(JavaMailSender mailSender, NewsFilter newsFilter, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.newsFilter = newsFilter;
        this.templateEngine = templateEngine;
    }

    public void sendEmail(String mail) {
        try{
            MimeMessage message = mailSender.createMimeMessage();

            List<Article> articles = newsFilter.fetchDailyNewsForUser(mail);

            Context ctx = new Context(Locale.getDefault());
            ctx.setVariable("articles", articles);

            String html = templateEngine.process("mailBody", ctx);

            MimeMessageHelper helper = new MimeMessageHelper(message, true, String.valueOf(StandardCharsets.UTF_8));
            helper.setTo(mail);
            helper.setSubject("A portion of news from around the world, specially for you. \uD83D\uDCF0");
            helper.setText(html,true);

            mailSender.send(message);
        } catch (MessagingException ex) {
            throw new EmailSendException(ex.getMessage());
        }
    }
}
