package service;


import exception.SpringRedittException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.NotificationEmail;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;
//    private final MailContentBuilder mailContentBuilder;

    //using lamda expressions we will send an email to user
    public void sendEmail(NotificationEmail notificationEmail){
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("matharakesh46@gmail.com");
            messageHelper.setFrom(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
//            messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
        };
        try {
            mailSender.send(messagePreparator);
            log.info("Activation email sent");
        }
        catch (MailException e){
        throw new SpringRedittException("Exception occured while sending an activation link");
        }

    }

}
