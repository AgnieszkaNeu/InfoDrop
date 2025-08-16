package com.example.demo.mailConfig;

import com.example.demo.User.AppUser;
import com.example.demo.User.AppUserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MailSenderConfig {

    private final AppUserRepository appUserRepository;

    private final CustomMailSender customMailSender;

    public MailSenderConfig(AppUserRepository appUserRepository, CustomMailSender newsSender) {
        this.appUserRepository = appUserRepository;
        this.customMailSender = newsSender;
    }

    @Scheduled(cron = "0 0 7 * * ?")
    public void sendEmailsToAllUsers() {
        List<AppUser> users = appUserRepository.findAll();
        for (AppUser user : users) {
            customMailSender.sendEmail(user.getEmail());
        }
    }

}
