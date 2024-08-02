package carreiras.com.github.java_spring_boot_library.services;

import java.util.List;

public interface EmailService {

    void sendEmails(String message, List<String> mailsList);
}
