// package mail;

import org.apache.commons.mail.*;
import java.io.File;

public class MailSender {
    private static final String HOST = "smtp.gmail.com";
    private static final int PORT = 465;
    private static final boolean SSL_FLAG = true;

    public static void main(String[] args){
        MailSender sender = new MailSender();
        sender.sendEmail();
    }

    private void sendEmail(){

        String userName = "testmailjavaapache@gmail.com";
        String password = "Apachemail1234";

        String fromAddress = "testmailjavaapache@gmail.com";
        //String toAddress =  "azatates4977@gmail.com";
        String toAddress = "hikmetarasdk@gmail.com";
        String subject = "Test Mail";
        String message = "Test from Apache Mail";

        try {
            HtmlEmail email = new HtmlEmail();
            email.setHostName(HOST);
            email.setSmtpPort(PORT);
            email.setAuthenticator(new DefaultAuthenticator(userName, password));
            email.setSSLOnConnect(SSL_FLAG);
            email.setFrom(fromAddress);
            email.setSubject(subject);
            email.setMsg(message);
            email.addTo(toAddress);

            String filename = "src/main/resources/static/img/try.jpg";
            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("File Not Founded");
            }

            email.attach(file);
            email.send();
            System.out.println("File Sended Succesfully");
        }
        
        catch(Exception ex){
            System.out.println("An Error Detected : "+ex.getMessage());
        }
        
    }
}

/*
        Library Details
            https://mvnrepository.com/artifact/org.apache.commons/commons-email/1.5
 */
