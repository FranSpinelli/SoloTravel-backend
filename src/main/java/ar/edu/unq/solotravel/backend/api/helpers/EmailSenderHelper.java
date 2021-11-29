package ar.edu.unq.solotravel.backend.api.helpers;

import ar.edu.unq.solotravel.backend.api.dtos.MailDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSenderHelper {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ObjectMapper objectMapper;


    @Value("${spring.mail.username}")
    private String emailFrom;


    @RabbitListener(queues = "mailQueue")
    public void sendEmail(String jsonMailDto) throws JsonProcessingException {
        MailDto mailDto = objectMapper.readValue(jsonMailDto, MailDto.class);

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(emailFrom);
        mailMessage.setTo(mailDto.getTo());
        mailMessage.setSubject(mailDto.getSubject());
        mailMessage.setText(mailDto.getBody());

        javaMailSender.send(mailMessage);
    }
}
