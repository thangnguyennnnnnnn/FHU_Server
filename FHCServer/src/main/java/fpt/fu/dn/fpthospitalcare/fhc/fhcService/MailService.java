package fpt.fu.dn.fpthospitalcare.fhc.fhcService;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailService {
	final String EMAIL = "fpthospitalcare@gmail.com";
    final String PASSWORD = "lwjfbjjykyjznqxl";
    
    public void sendMail(String reciverEmail,String subject, String content, MimeMultipart type) {
    	Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL, PASSWORD);
            }
        });
        
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(reciverEmail));
            message.setSubject(subject);
            message.setText(content);
            message.setContent(type);
            
            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    
}
