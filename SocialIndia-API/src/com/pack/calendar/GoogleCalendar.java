package com.pack.calendar;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.WebContent;
import com.google.gdata.data.extensions.Recurrence;
import com.google.gdata.data.extensions.When;
import com.google.gdata.data.extensions.Where;
import com.google.gdata.util.ServiceException;

/**
 *
 * @author Administrator
 */
public class GoogleCalendar {

    private static final String METAFEED_URL_BASE = "http://www.google.com/calendar/feeds/";
    private static final String EVENT_FEED_URL_SUFFIX = "/private/full";
    private static URL metafeedUrl = null;
    private static URL eventFeedUrl = null;

    public CalendarEventEntry createEvent(
            CalendarService service, String eventTitle, String eventContent,
            String recurData, boolean isQuickAdd, WebContent wc, String location)
            throws ServiceException, IOException {
        CalendarEventEntry myEntry = new CalendarEventEntry();
        myEntry.setTitle(new PlainTextConstruct(eventTitle));
        myEntry.setContent(new PlainTextConstruct(eventContent));
        myEntry.setQuickAdd(isQuickAdd);
        myEntry.setWebContent(wc);
        myEntry.addLocation(new Where("", "", location));

        if (recurData == null) {
            Calendar calendar = new GregorianCalendar();
            DateTime startTime = new DateTime(calendar.getTime(), TimeZone.getDefault());
            calendar.add(Calendar.MINUTE, 30);
            DateTime endTime = new DateTime(calendar.getTime(), TimeZone.getDefault());
            When eventTimes = new When();
            eventTimes.setStartTime(startTime);
            eventTimes.setEndTime(endTime);
            myEntry.addTime(eventTimes);
        } else {
            Recurrence recur = new Recurrence();
            recur.setValue(recurData);
            myEntry.setRecurrence(recur);
        }
        return service.insert(eventFeedUrl, myEntry);
    }

    public CalendarEventEntry createSingleEvent(CalendarService service,
            String eventTitle, String eventContent, String location) throws ServiceException,
            IOException {
        return createEvent(service, eventTitle, eventContent, null, false, null, location);
    }

    public String toSetReminder(String emaildd, String pswd, String title, String sub, String location) {
        try {
//            String nextUrl = "http://192.168.1.44:8084/rooster/NewServlet";
//            String scope = "https://www.google.com/calendar/feeds/";
//            boolean secure = false;  // set secure=true to request secure AuthSub tokens
//            boolean session = true;
//            String authSubUrl = com.google.gdata.client.http.AuthSubUtil.getRequestUrl(nextUrl, scope, secure, session);
////            String authSubUrl ="1/m-Pd5fASB7f2MThDOc3a5jWS9ao6k1JYoYblAym4S6g";
//            System.out.println("authSubUrl=="+authSubUrl);

            CalendarService myService;
            myService = new CalendarService("exampleCo-exampleApp-1");
//            myService.setAuthSubToken(authSubUrl, null);


            myService.setUserCredentials(emaildd, pswd);

            CalendarEventEntry singleEvent = createSingleEvent(myService, title, sub, location);
            System.out.println("Successfully created event : " + singleEvent.getTitle().getPlainText());

        } catch (Exception e) {
            System.out.println("Exception Found in GoogleCalendar.class [toSetReminder] Method : " + e);
        }
        return "";
    }

//    public static void main(String[] args) {
//        String userName = "peninlogphp@gmail.com";
//        String userPassword = "peninlog123";
//        String location = "tamilnadu, tuticorin";
//        String title = "Meeting991";
//        String sub = "fgfgf  dfg dgdfgfdg dfgd.";
//
//        new GoogleCalendar().toSetReminder(userName, userPassword, title, sub, location);
//    }
    public static void main(String[] args) {
        try {
            while (true) {
                BufferedReader br;
                br = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("If You Need to Stop Enter - 1 , Countinue 2.");
                String cntr = br.readLine();
                if (cntr.equalsIgnoreCase("1")) {
                    break;
                } else {

                    System.out.println("Enter your Email :");
                    String userName = br.readLine();

                    System.out.println("Enter your Passowrd :");
                    String userPassword = br.readLine();
                    System.out.println("Waiting... ");
                    // String userName = "peninlogphp@gmail.com";
                    //String userPassword = "peninlog123";
                    String location = "TamilNadu, Chennai, Ashoknagar";
                    String title = "Meeting";
                    String sub = "Meet for a quick lesson.";
                    String toSetReminder = new GoogleCalendar().toSetReminder(userName, userPassword, title, sub, location);
                    if (toSetReminder != "") {
                        System.out.println(toSetReminder);
                    } else {
                        System.out.println("----");
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(GoogleCalendar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}



// https://www.google.com/accounts/AuthSubRequest?next=http%3A%2F%2Flocalhost:8084/rooster%2FNewServlet&scope=http%3A%2F%2Fwww.google.com%2Fcalendar%2Ffeeds%2F&session=0&secure=0