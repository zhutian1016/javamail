package com.test.mail.execute;

import com.test.mail.util.Constants;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.util.Date;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA. User: ${} Date: ${date} Time: ${time} To change this template use File | Settings | File
 * Templates.
 */
public class MailExecutor {
    public boolean sendEmail() {
        // 创建Properties 对象
        Properties props = System.getProperties();
        // 设置邮件发送方的主机地址如果是163邮箱，则为smtp.163.com
        props.put("mail.smtp.host", Constants.HOST); // 全局变量
        // 设置发送邮件端口号
        props.put("mail.smtp.port", "25");
        // 设置邮件发送需要认证
        props.put("mail.smtp.auth", "true");

        // 创建邮件会话
        Session session = Session.getDefaultInstance(props,
        new Authenticator() { // 验账账户
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                // 重写验证方法，填写用户名，密码
                return new PasswordAuthentication(Constants.USERNAME,
                                                  Constants.PASSWORD);
            }
        });

        try {
            // 定义邮件信息
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Constants.FROM));
            message.addRecipient(
                Message.RecipientType.TO,
                new InternetAddress(
                    // 这里可以添加多个目的用户
                        Constants.EMAIL_TO
                    //data.get(Constants.EMAIL_TO)
                )
            );
            // 添加邮件发送时间（不知道体现在哪儿）
            message.setSentDate(new Date());
            // 要编码，否则中文会出乱码，貌似这个方法是对数据进行了
            //("=?GB2312?B?"+enc.encode(subject.getBytes())+"?=")形势的包装
            message.setSubject(MimeUtility.encodeText(Constants.EMAIL_SUBJECT, "gbk", "B"));

            MimeMultipart mmp = new MimeMultipart();
            MimeBodyPart mbp_text = new MimeBodyPart();
            // "text/plain"是文本型，没有样式，
            //"text/html"是html样式，可以解析html标签
            mbp_text.setContent(Constants.EMAIL_TEXT,
                                "text/html;charset=gbk");
            mmp.addBodyPart(mbp_text); // 加入邮件正文

            // 处理附件，可以添加多个附件
            if (!"".equals(Constants.EMAIL_ATTACHMENT.trim())) {
                String file = Constants.EMAIL_ATTACHMENT;
                    //for (String file : files) {
                        MimeBodyPart mbp_file = new MimeBodyPart();
                        FileDataSource fds = new FileDataSource(file);
                        mbp_file.setDataHandler(new DataHandler(fds));
                        mbp_file.setFileName(MimeUtility.encodeText(fds.getName(), "gbk", "B"));
                        mmp.addBodyPart(mbp_file);
                    //}
            }
            message.setContent(mmp);
            // message.setText(data.get(Constants.EMAIL_TEXT));

            // 发送消息
            // session.getTransport("smtp").send(message); //也可以这样创建Transport对象
            Transport.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
