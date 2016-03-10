package com.test.mail.main;

import com.test.mail.execute.MailExecutor;

/**
 * Created by IntelliJ IDEA. User: ${zm} Date: ${date} Time: ${time} To change this template use File | Settings | File
 * Templates.
 */
public class MailServer {
    public static void main(String args[]) {
        MailExecutor executor = new MailExecutor();
        executor.sendEmail();
    }
}
