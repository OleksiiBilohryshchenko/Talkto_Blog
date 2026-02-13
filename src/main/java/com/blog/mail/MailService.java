package com.blog.mail;

public interface MailService {

    void sendPasswordResetEmail(String to, String resetLink);

}