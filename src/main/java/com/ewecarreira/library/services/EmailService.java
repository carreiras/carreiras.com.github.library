package com.ewecarreira.library.services;

import java.util.List;

public interface EmailService {

    void sendEmails(String message, List<String> mailsList);
}
