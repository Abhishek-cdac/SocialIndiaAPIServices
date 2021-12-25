/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pack.calendar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author Administrator
 */
public class OutlookCalendar {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            String to = "prabhakarr@peninlog.com";
            OutlookCalendar.setOutlookCallicsRemainder(to);
        } catch (Exception ex) {
            Logger.getLogger(OutlookCalendar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setOutlookCallicsRemainder(String to) throws Exception {
        boolean sessionDebug = true;
        SimpleDateFormat smtt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            String host = "smtp.gmail.com";
            final String from = "peninlogjava@gmail.com";
            final String pas = "java!@PENIN";
            //String to = "peninlogphp@gmail.com";
// Get system properties
            Properties props = System.getProperties();
// Setup mail server         
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.port", "25");
            props.put("mail.smtp.user", from);
            props.put("mail.smtp.password", pas);
            props.put("mail.smtp.auth", "true"); // create some properties and get the default Session  
// Get session
            // Session session = Session.getInstance(props, null);
            Session mailSession = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {

                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(from, pas);
                        }
                    });
            mailSession.setDebug(sessionDebug);
                
                MailcapCommandMap mailcap = (MailcapCommandMap) MailcapCommandMap.getDefaultCommandMap();
                mailcap.addMailcap("text/calendar;; x-java-content-handler=com.sun.mail.handlers.text_plain");

        // Define message
            MimeMessage message = new MimeMessage(mailSession);
            message.addHeaderLine("method=REQUEST");
            message.addHeaderLine("charset=UTF-8");
            message.addHeaderLine("component=vevent");
            message.setFrom(new InternetAddress(from));
          //  message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Outlook Meeting Request Using JavaMail");

            String buffer = OutLookParameter.toGetReminderDetail(from, smtt.format(new Date()), smtt.format(new Date()), "SOCIAL INDIA", "SOCIAL INDIA Optionl data", "Meeting Information", "Today meeting in Ashoknagar. So please Come.", "Meeting Fixed", "Ashok Nagar", "Reminder - Meeting");
// Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            messageBodyPart.setHeader("Content-Class", "urn:content-classes:calendarmessage");
            messageBodyPart.setHeader("Content-ID", "calendar_message");
            messageBodyPart.setContent(buffer, "text/calendar");
// Fill the message
            messageBodyPart.setText("You are requested to participlate in the review meeting.");

// Create a Multipart
            Multipart multipart = new MimeMultipart();


            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();
            String filename = "Meeting.ics";
            messageBodyPart.setFileName(filename);
            messageBodyPart.setContent(buffer, "text/plain");

// Add part two
            multipart.addBodyPart(messageBodyPart);

// Put parts in message
            message.setContent(multipart);

// send message
//            Transport.send(message);

            Transport transport = mailSession.getTransport("smtp");
            transport.connect(host, from, pas);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        } catch (MessagingException me) {
            me.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
