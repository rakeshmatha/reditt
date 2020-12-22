//package service;
//
//import lombok.AllArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//@AllArgsConstructor
//public class MailContentBuilder {
//
//
//    private TemplateEngine templateEngine;
//
//    String build(String message){
//        Context context = new Context();
//        context.setVariable("message", message);
//        return templateEngine.process("mailTemplate", context);
//    }
//}
