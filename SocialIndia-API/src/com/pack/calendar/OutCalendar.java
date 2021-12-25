/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pack.calendar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Message;
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
public class OutCalendar {

    public static void main(String[] args) {
        try {
            while (true) {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("If You Need to Stop Enter - 1 , Countinue 2.");
                String cntr = br.readLine();
                if (cntr.equalsIgnoreCase("1")) {
                    break;
                } else {
                    System.out.println("Enter Email :");
                    String mailto = br.readLine();
                    //String to = "peninlogphp@gmail.com";
                    System.out.println("Waiting...");
                    //String send = OutCalendar.send(mailto);
                    
                    
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat simdat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    Date stdate = simdat.parse(simdat.format(new Date()));
                    cal.setTime(stdate);
                    cal.add(Calendar.MINUTE, 2);
                    //cal.add(Calendar.DAY_OF_MONTH, 1);
                    Date start = cal.getTime();

                    Calendar cal_enddt = Calendar.getInstance();
                    Date endddate = simdat.parse(simdat.format(new Date()));
                    cal_enddt.setTime(endddate);
                   // cal_enddt.add(Calendar.DAY_OF_MONTH, 1);
                    cal_enddt.add(Calendar.MINUTE, 10);
                    Date end = cal_enddt.getTime();
                    
                    
                    System.out.println("Start Date Time : " + simdat.format(start));
                    System.out.println("End Date Time : " + simdat.format(end));
                    
                    String usesubject="My Son Birth day invite";
                    String Optsubject="My Son Birth day invite [Special For you]";//subject
                    String category="Remainder - Category";//Category
                   
                    String bdyy ="birthday celebration in my house plaease come and attend.";
                    
                    String lvrSummary="Summary :  Birthday party";//Category
                    
                    String alrDesc ="Alram Description by prabhu";
                    
                    String locat ="Nungambakkam, Valuvarkottm, Chennai";
                    
                    String send_1 = OutCalendar.setOutlookCalDirectRemainder(mailto,  simdat.format(start), simdat.format(end),usesubject,Optsubject, category,bdyy,lvrSummary, alrDesc, locat);
                    
                    String send_2 = setOutlookCallicsRemainder(mailto,  simdat.format(start), simdat.format(end),usesubject,Optsubject, category,bdyy,lvrSummary, alrDesc, locat);
                    
                    System.out.println("\n\n Send 1 :" + send_1);
                    
                    System.out.println("\n\n Send 2 :" + send_2);
                }
                // TODO code application logic here

            }
        } catch (Exception ex) {
            System.out.println("Exception found in OutCalendar.java : " + ex);
        }
    }

    public static String setOutlookCalDirectRemainder(String to, String stdate, String enddate, String usesubject, String optionsubject, String pCategeory, String bdyy, String pSummary, String alrDesc, String locat) {
        try {
            boolean sessionDebug = true;
            //String host = "smtpout.secureserver.net";
            // final String from = "noreply@giggzo.com";
            //final String pswd = "gigging01##";
            
            String host = "smtp.gmail.com";
            final String from = "peninlogjava@gmail.com";
            final String pswd = "java!@PENIN";
            
           //String host = "smtpout.secureserver.net";
           //final String from = "prabhakarr@peninlog.com";
           //final String pswd = "pkaranbsc1985";
            
            //String subject = "Outlook Meeting Request Using JavaMail";
            
          /*Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, 1);
            Date start = cal.getTime();
            //cal.add(Calendar.DAY_OF_MONTH, 3);
            Calendar cal2 = Calendar.getInstance();
            cal2.add(Calendar.DAY_OF_MONTH, 1);
            
            Date end = cal2.getTime();
            */
            
// Setup mail server 
            BodyPart calendarPart = OutLookParameter.toGetReminderDetailBdypart_2(from, stdate, enddate, usesubject, optionsubject, pCategeory, bdyy, pSummary, locat, alrDesc); 
                        
            Properties props = System.getProperties();  
            if(props!=null){
            	props.put("mail.smtp.host", host);
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.port", "25");
                props.put("mail.smtp.user", from);
                props.put("mail.smtp.password", pswd);
                props.put("mail.smtp.auth", "true"); // create some properties and get the default Session  
            }else{
            	
            }
            
            Session mailSession = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {

                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(from, pswd);
                        }
                    });
            mailSession.setDebug(sessionDebug);

            //MimetypesFileTypeMap mimetypes = (MimetypesFileTypeMap) MimetypesFileTypeMap.getDefaultFileTypeMap();
            //mimetypes.addMimeTypes("text/calendar ics ICS");

            MailcapCommandMap mailcap = (MailcapCommandMap) MailcapCommandMap.getDefaultCommandMap();
            mailcap.addMailcap("text/calendar;; x-java-content-handler=com.sun.mail.handlers.text_plain");

            MimeMessage message_ext = new MimeMessage(mailSession);
            message_ext.setFrom(new InternetAddress(from));
            message_ext.setSubject(usesubject);
            message_ext.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        
                                   
            Multipart multipart = new MimeMultipart("alternative");
            multipart.addBodyPart(calendarPart);
            
            message_ext.setContent(multipart);

            // send the message
            Transport transport_ext = mailSession.getTransport("smtp");
            transport_ext.connect();
            transport_ext.sendMessage(message_ext, message_ext.getAllRecipients());
            transport_ext.close();
            return "Succesfully Send Reminder to Outlook Calendar.";
        } catch (Exception e) {
            System.out.println("Exception found in OutCalendar.java : " + e);
            return "Not Set Reminder to Outlook Calendar.";
        }
    }

    public static String setOutlookCallicsRemainder(String to,String stdate, String enddate, String usesubject,String optionsubject,String pCategeory, String bdyy, String pSummary, String alrDesc, String locat) throws Exception {
        
    	boolean sessionDebug = true;
       // SimpleDateFormat smtt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            String host = "smtp.gmail.com";
            final String from = "peninlogjava@gmail.com";
            final String pas = "java!@PENIN";
            //String to = "peninlogphp@gmail.com";
// Get system properties
            Properties props = System.getProperties();
// Setup mail server   
            if(props!=null){
            	props.put("mail.smtp.host", host);
            	props.put("mail.smtp.starttls.enable", "true");
            	props.put("mail.smtp.port", "25");
            	props.put("mail.smtp.user", from);
            	props.put("mail.smtp.password", pas);
            	props.put("mail.smtp.auth", "true"); // create some properties and get the default Session  
            }
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

            String buffer = OutLookParameter.toGetReminderDetail(from, stdate, enddate, usesubject, optionsubject, pCategeory, bdyy, pSummary, locat, alrDesc);
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

        } catch (Exception ex) {
            ex.printStackTrace();
        }finally{
        	
        }
        return "SUCCESS - ics Mode";
    }
    
    
public static String sendOutlookMail(List<String> toList, String subject,String body) throws Exception {
        
    	boolean sessionDebug = true;
       // SimpleDateFormat smtt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            String host = "smtp.gmail.com";
            final String from = "peninlogjava@gmail.com";
            final String pas = "java!@PENIN";
            Properties props = System.getProperties();
            if(props!=null){
            	props.put("mail.smtp.host", host);
            	props.put("mail.smtp.starttls.enable", "true");
            	props.put("mail.smtp.port", "25");
            	props.put("mail.smtp.user", from);
            	props.put("mail.smtp.password", pas);
            	props.put("mail.smtp.auth", "true"); // create some properties and get the default Session  
            }
// Get session
            Session mailSession = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {

                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(from, pas);
                        }
                    });
            mailSession.setDebug(sessionDebug);
                
                MailcapCommandMap mailcap = (MailcapCommandMap) MailcapCommandMap.getDefaultCommandMap();
//                mailcap.addMailcap("text/calendar;; x-java-content-handler=com.sun.mail.handlers.text_plain");

        // Define message
            MimeMessage message = new MimeMessage(mailSession);
            message.addHeaderLine("method=REQUEST");
            message.addHeaderLine("charset=UTF-8");
            message.addHeaderLine("component=vevent");
            message.setFrom(new InternetAddress(from));
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            InternetAddress[] mailAddress_TO = new InternetAddress [toList.size()] ;
            for(int i=0;i<toList.size();i++){
                mailAddress_TO[i] = new InternetAddress(toList.get(i));
            }
            message.addRecipients(Message.RecipientType.TO, mailAddress_TO);
            message.setSubject(subject);

// Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

// Fill the message
            messageBodyPart.setText(body);

// Create a Multipart
            Multipart multipart = new MimeMultipart();


            multipart.addBodyPart(messageBodyPart);

// Put parts in message
            message.setContent(multipart);

// send message
//            Transport.send(message);

            Transport transport = mailSession.getTransport("smtp");
            transport.connect(host, from, pas);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }finally{
        	
        }
        return "SUCCESS";
    }
    
    
    
    public static String send(String to) throws Exception {
        boolean sessionDebug = true;
        //String host = "smtpout.secureserver.net";
        //final String from = "noreply@giggzo.com";
        //final String pswd = "gigging01##";
        
        String host = "smtpout.secureserver.net";
        final String from = "prabhakarr@peninlog.com";
        final String pswd = "pkaranbsc1985";
        
        String subject = "Outlook Meeting Request Using JavaMail";
        Properties props = System.getProperties();
// Setup mail server         
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "25");
        props.put("mail.smtp.user", "prabhakarr@peninlog.com");
        props.put("mail.smtp.password", "pkaranbsc1985");
        props.put("mail.smtp.auth", "true"); // create some properties and get the default Session  
        Session mailSession = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {

                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, pswd);
                    }
                });
        mailSession.setDebug(sessionDebug);

//        MimetypesFileTypeMap mimetypes = (MimetypesFileTypeMap) MimetypesFileTypeMap.getDefaultFileTypeMap();
//        mimetypes.addMimeTypes("text/calendar ics ICS");


        MailcapCommandMap mailcap = (MailcapCommandMap) MailcapCommandMap.getDefaultCommandMap();
        mailcap.addMailcap("text/calendar;; x-java-content-handler=com.sun.mail.handlers.text_plain");


        MimeMessage message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress(from));
        message.setSubject(subject);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

        Multipart multipart = new MimeMultipart("alternative");

//        BodyPart calendarPart = buildCalendarPart();
        SimpleDateFormat simdat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date start = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 3);
        Date end = cal.getTime();
        BodyPart calendarPart = OutLookParameter.toGetReminderDetailBdypart_2(from, simdat.format(start), simdat.format(end), "Rooster", "Rooster",
                "Meeting Information", "Today meeting in Ashoknagar. So please Come.", "Meeting Fixed", "", "Reminder - Meeting");
        
         
        
        multipart.addBodyPart(calendarPart);
        message.setContent(multipart);

        // send the message
        Transport transport = mailSession.getTransport("smtp");
        transport.connect();
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
        return "Succesfully Send Reminder to Outlook Calendar.";
    }
    private static SimpleDateFormat iCalendarDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmm'00'");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmm'00'");
    SimpleDateFormat dateParser = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    private static BodyPart buildCalendarPart() throws Exception {

        BodyPart calendarPart = new MimeBodyPart();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date start = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 3);
        Date end = cal.getTime();

        //check the icalendar spec in order to build a more complicated meeting request
        String calendarContent =
                "BEGIN:VCALENDAR\n"
                + "METHOD:REQUEST\n"
                + "PRODID: BCP - Meeting\n"
                + "VERSION:2.0\n"
                + "BEGIN:VEVENT\n"
                + "DTSTAMP:" + iCalendarDateFormat.format(start) + "\n"
                + "DTSTART:" + iCalendarDateFormat.format(start) + "\n"
                + "DTEND:" + iCalendarDateFormat.format(end) + "\n"
                + "SUMMARY:test request\n"
                + "UID:324\n"
                + "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE:MAILTO:organizer@yahoo.com\n"
                + "ORGANIZER:MAILTO:organizer@yahoo.com\n"
                + "LOCATION:on the net\n"
                + "DESCRIPTION:learn some stuff\n"
                + "SEQUENCE:0\n"
                + "PRIORITY:5\n"
                + "CLASS:PUBLIC\n"
                + "STATUS:CONFIRMED\n"
                + "TRANSP:OPAQUE\n"
                + "BEGIN:VALARM\n"
                + "ACTION:DISPLAY\n"
                + "DESCRIPTION:REMINDER\n"
                + "TRIGGER;RELATED=START:-PT00H15M00S\n"
                + "END:VALARM\n"
                + "END:VEVENT\n"
                + "END:VCALENDAR";
        calendarPart.addHeader("Content-Class", "urn:content-classes:calendarmessage");
        calendarPart.setContent(calendarContent, "text/calendar;method=CANCEL");

        return calendarPart;
    }
}
