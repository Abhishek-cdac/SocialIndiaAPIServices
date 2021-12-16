/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pack.calendar;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.mail.BodyPart;
import javax.mail.internet.MimeBodyPart;

/**
 *
 * @author Administrator
 */
public class OutLookParameter {
     public static String toGetReminderDetail(String fromMailid, String strtDate, String endDate, String nameRq, String nameOpt, String Category, String desc, String summry, String location, String alrDesc) {
        //06-12-2012 16:32 --- StartDate
        //06-12-2012 17:00  ----EndDate
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmm00");
            SimpleDateFormat dateParser = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
            Date stdate = dateParser.parse(strtDate);
            Date enddate = dateParser.parse(endDate);
            StringBuffer sb = new StringBuffer();
            StringBuffer buffer = sb.append("BEGIN:VCALENDAR\n"
                    + "PRODID:"+Category+"\n"
                    + "VERSION:2.0\n"
                    + "METHOD:REQUEST\n"
                    + "BEGIN:VTIMEZONE\n"//
                    + "TZID:India\n"
                    + "BEGIN:STANDARD\n"
                    + "DTSTART:" + dateFormat.format(stdate) + "\n"
                    + "RRULE:FREQ=YEARLY;BYDAY=1SU;BYMONTH=4\n"
                    + "TZOFFSETFROM:+0530\n"
                    + "TZOFFSETTO:+0530\n"
                    + "TZNAME:Standard Time\n"
                    + "END:STANDARD\n"
                    + "BEGIN:DAYLIGHT\n"
                    + "DTSTART:" + dateFormat.format(stdate) + "\n"
                    + "RRULE:FREQ=YEARLY;BYDAY=1SU;BYMONTH=10\n"
                    + "TZOFFSETFROM:+0530\n"
                    + "TZOFFSETTO:+0530\n"
                    + "TZNAME:Daylight Saving Time\n"
                    + "END:DAYLIGHT\n"
                    + "END:VTIMEZONE\n"
                    + "BEGIN:VEVENT\n"
                    + "ATTENDEE;CN=\"" + nameRq + "\";ROLE=REQ-PARTICIPANT;MAILTO:" + fromMailid + "\n"
                    + "ATTENDEE;CN=\"" + nameOpt + "\";ROLE=OPT-PARTICIPANT;MAILTO:" + fromMailid + "\n"
                    + "ORGANIZER:MAILTO:" + fromMailid + "\n"
                    + "DTSTART;TZID=\"India\":" + dateFormat.format(stdate) + "\n"
                    + "DTEND;TZID=\"India\":" + dateFormat.format(enddate) + "\n"
                    + "LOCATION:" + location + "\n"
                    + "TRANSP:OPAQUE\n"
                    + "SEQUENCE:1\n"
                    + "UID:040000008200E00074C5B7101A82E00800000000A0A742E5073AC5010000000000000000100\n"
                    + " 0000029606C073D82204AB6C77ACE6BC2FBE2\n"
                    + "DTSTAMP:" + dateFormat.format(stdate) + "\n"
                    + "CATEGORIES:" + Category + "\n"
                    + "DESCRIPTION:" + desc + "\n\n"
                    + "SUMMARY:" + summry + "\n"
                    + "PRIORITY:5\n"
                    + "X-MICROSOFT-CDO-IMPORTANCE:0\n"
                    + "CLASS:PUBLIC\n"
                    + "BEGIN:VALARM\n"
                    + "TRIGGER:PT1440M\n" //PT15M   PT1440                  
                    + "ACTION:DISPLAY\n"
                    + "DESCRIPTION:" + alrDesc + "\n"
                    + "END:VALARM\n"
                    + "END:VEVENT\n"
                    + "END:VCALENDAR\n");
            return buffer.toString();
        } catch (Exception ex) {
            System.out.println("Exception Found in toGetReminderInfo() Method : " + ex);
            return "Exception!_!" + ex;
        }

    }

    public static BodyPart toGetReminderDetailBdypart(String fromMailid, String strtDate, String endDate, String nameRq, String nameOpt, String Category, String desc, String summry, String location, String alrDesc) {
        //06-12-2012 16:32 --- StartDate
        //06-12-2012 17:00  ----EndDate
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmm00");
            SimpleDateFormat dateParser = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            Date stdate = dateParser.parse(strtDate);
            Date enddate = dateParser.parse(endDate);
            StringBuffer sb = new StringBuffer();
            StringBuffer buffer = sb.append("BEGIN:VCALENDAR\n"
                    + "METHOD:REQUEST\n"
                    + "PRODID:Rooster - Meeting\n"
                    + "VERSION:2.0\n"
                    //                    + "BEGIN:VEVENT\n"
                    //                    + "DTSTAMP:" + dateFormat.format(stdate) + "\n"
                    //                    + "DTSTART:" + dateFormat.format(stdate) + "\n"
                    //                    + "DTEND:" + dateFormat.format(endDate) + "\n"
                    //                    + "SUMMARY:" + summry + "\n"
                    //                    + "UID:324\n"
                    //                    + "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE:MAILTO:" + fromMailid + "\n"
                    //                    + "ORGANIZER:MAILTO:"+ fromMailid + "\n"

                    + "BEGIN:VTIMEZONE\n"//
                    + "TZID:India\n"
                    + "BEGIN:STANDARD\n"
                    + "DTSTART:" + dateFormat.format(stdate) + "\n"
                    + "RRULE:FREQ=YEARLY;BYDAY=1SU;BYMONTH=4\n"
                    + "TZOFFSETFROM:+0530\n"
                    + "TZOFFSETTO:+0530\n"
                    + "TZNAME:Standard Time\n"
                    + "END:STANDARD\n"
                    + "BEGIN:DAYLIGHT\n"
                    + "DTSTART:" + dateFormat.format(stdate) + "\n"
                    + "RRULE:FREQ=YEARLY;BYDAY=1SU;BYMONTH=10\n"
                    + "TZOFFSETFROM:+0530\n"
                    + "TZOFFSETTO:+0530\n"
                    + "TZNAME:Daylight Saving Time\n"
                    + "END:DAYLIGHT\n"
                    + "END:VTIMEZONE\n"
                    + "BEGIN:VEVENT\n"
                    + "ATTENDEE;CN=\"" + nameRq + "\";ROLE=REQ-PARTICIPANT;MAILTO:" + fromMailid + "\n"
                    + "ATTENDEE;CN=\"" + nameOpt + "\";ROLE=OPT-PARTICIPANT;MAILTO:" + fromMailid + "\n"
                    + "ORGANIZER:MAILTO:" + fromMailid + "\n"
                    + "DTSTART;TZID=\"India\":" + dateFormat.format(stdate) + "\n"
                    + "DTEND;TZID=\"India\":" + dateFormat.format(enddate) + "\n"
                    + "LOCATION:" + location + "\n"
                    + "TRANSP:OPAQUE\n"
                    + "SEQUENCE:1\n"
                    + "UID:040000008200E00074C5B7101A82E00800000000A0A742E5073AC5010000000000000000100\n"
                    + " 0000029606C073D82204AB6C77ACE6BC2FBE2\n"
                    + "DTSTAMP:" + dateFormat.format(stdate) + "\n"
                    + "CATEGORIES:" + Category + "\n"
                    + "DESCRIPTION:" + desc + "\n\n"
                    + "SUMMARY:" + summry + "\n"
                    + "PRIORITY:5\n"
                    + "X-MICROSOFT-CDO-IMPORTANCE:1\n"
                    + "CLASS:PUBLIC\n"
                    + "BEGIN:VALARM\n"
                    + "TRIGGER:-PT15MM\n" //PT15M   PT1440                  
                    + "ACTION:DISPLAY\n"
                    + "DESCRIPTION:" + alrDesc + "\n"
                    + "END:VALARM\n"
                    + "END:VEVENT\n"
                    + "END:VCALENDAR\n");
            BodyPart calendarPart = new MimeBodyPart();
            calendarPart.addHeader("Content-Class", "urn:content-classes:calendarmessage");
            calendarPart.setContent(buffer.toString(), "text/calendar;method=CANCEL");

            return calendarPart;
        } catch (Exception ex) {
            System.out.println("Exception Found in toGetReminderInfo() Method : " + ex);
            return null;
}

    }

    public static BodyPart toGetReminderDetailBdypart_2(String fromMailid, String strtDate, String endDate, String nameRq, String nameOpt, String Category, String desc, String summry, String location, String alrDesc) {
        //06-12-2012 16:32 --- StartDate
        //06-12-2012 17:00  ----EndDate
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmm00");
            SimpleDateFormat dateParser = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            Date stdate = dateParser.parse(strtDate);
            Date enddate = dateParser.parse(endDate);
            
            
            StringBuffer sb = new StringBuffer();
            StringBuffer buffer = sb.append("BEGIN:VCALENDAR\n"
                    + "METHOD:REQUEST\n"
                    + "PRODID:" + Category + "\n"
                    + "VERSION:2.0\n"
                    + "BEGIN:VEVENT\n"
                    + "DTSTAMP:" + dateFormat.format(stdate) + "\n"
                    + "DTSTART:" + dateFormat.format(stdate) + "\n"
                    + "DTEND:" + dateFormat.format(enddate) + "\n"
                    + "SUMMARY:" + summry + "\n"
                    + "UID:324\n"
                    + "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE:MAILTO:" + fromMailid + "\n"
                    + "ORGANIZER:MAILTO:" + fromMailid + "\n"
                    + "LOCATION:" + location + "\n"
                    + "CATEGORIES:" + Category + "\n"
                    + "DESCRIPTION:" + desc + "\n\n"
                    + "SEQUENCE:0\n"
                    + "PRIORITY:5\n"
                    + "CLASS:PUBLIC\n"
                    + "STATUS:CONFIRMED\n"
                    + "TRANSP:OPAQUE\n"
                    + "BEGIN:VALARM\n"
                    + "ACTION:DISPLAY\n"
                    + "TRIGGER;RELATED=START:-PT00H15M00S\n"
                    + "END:VALARM\n"
                    + "END:VEVENT\n"
                    + "END:VCALENDAR");
            BodyPart calendarPart = new MimeBodyPart();
            calendarPart.addHeader("Content-Class", "urn:content-classes:calendarmessage");
            calendarPart.setContent(buffer.toString(), "text/calendar;method=CANCEL");

            return calendarPart;
        } catch (Exception ex) {
            System.out.println("Exception Found in toGetReminderInfo() Method : " + ex);
            return null;
        }

    }
}
