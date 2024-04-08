package com.example.myapplication;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender extends AsyncTask<Void, Void, Void> {
//tạo luồng xử lí
    private final String email;
    private final String subject;
    private final String message;

    public EmailSender(String email, String subject, String message) {
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String host = "smtp.gmail.com";//local host

        int port = 587;//cong hd 587,149
        final String username = "dangduylinh021103@gmail.com";
        final String password = "lwfh nnbu dsdz wpiy";

        // Cấu hình các thuộc tính
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");//máy khách cần phải xác thực máy chủ trước khi gửi mail
        props.put("mail.smtp.starttls.enable", "true");//TLS cung cấp mã hóa và đảm bảo rằng giao tiếp giữa máy khách và máy chủ được an toàn.
        props.put("mail.smtp.host", host);//Thuộc tính này chỉ định tên máy chủ hoặc địa chỉ IP của máy chủ SMTP mà ứng dụng email sẽ kết nối để gửi email.
        props.put("mail.smtp.port", port);//Thuộc tính này chỉ định số cổng mà máy chủ SMTP đang lắng nghe các kết nối đến.
        // Cổng tiêu chuẩn cho SMTP là 25, nhưng nó có thể thay đổi tùy theo cấu hình máy chủ.
        // Điều quan trọng là đặt thuộc tính này thành số cổng chính xác để đảm bảo liên lạc thành công với máy chủ SMTP.

        //Tạo thêm luồng hoạt động mới truy cap den email chu
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            // Tạo tin nhắn email
            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(username));
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);

            // Gửi email
            Transport.send(mimeMessage);
//
            Log.d("emailSender", "Email sent successfully.");

        } catch (MessagingException e) {
            Log.d("emailSender", "Email sent fail." + e.toString());
            e.printStackTrace();
        }
        return null;
    }
}
