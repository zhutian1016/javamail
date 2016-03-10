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
        // ����Properties ����
        Properties props = System.getProperties();
        // �����ʼ����ͷ���������ַ�����163���䣬��Ϊsmtp.163.com
        props.put("mail.smtp.host", Constants.HOST); // ȫ�ֱ���
        // ���÷����ʼ��˿ں�
        props.put("mail.smtp.port", "25");
        // �����ʼ�������Ҫ��֤
        props.put("mail.smtp.auth", "true");

        // �����ʼ��Ự
        Session session = Session.getDefaultInstance(props,
        new Authenticator() { // �����˻�
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                // ��д��֤��������д�û���������
                return new PasswordAuthentication(Constants.USERNAME,
                                                  Constants.PASSWORD);
            }
        });

        try {
            // �����ʼ���Ϣ
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Constants.FROM));
            message.addRecipient(
                Message.RecipientType.TO,
                new InternetAddress(
                    // ���������Ӷ��Ŀ���û�
                        Constants.EMAIL_TO
                    //data.get(Constants.EMAIL_TO)
                )
            );
            // ����ʼ�����ʱ�䣨��֪���������Ķ���
            message.setSentDate(new Date());
            // Ҫ���룬�������Ļ�����룬ò����������Ƕ����ݽ�����
            //("=?GB2312?B?"+enc.encode(subject.getBytes())+"?=")���Ƶİ�װ
            message.setSubject(MimeUtility.encodeText(Constants.EMAIL_SUBJECT, "gbk", "B"));

            MimeMultipart mmp = new MimeMultipart();
            MimeBodyPart mbp_text = new MimeBodyPart();
            // "text/plain"���ı��ͣ�û����ʽ��
            //"text/html"��html��ʽ�����Խ���html��ǩ
            mbp_text.setContent(Constants.EMAIL_TEXT,
                                "text/html;charset=gbk");
            mmp.addBodyPart(mbp_text); // �����ʼ�����

            // ��������������Ӷ������
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

            // ������Ϣ
            // session.getTransport("smtp").send(message); //Ҳ������������Transport����
            Transport.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
