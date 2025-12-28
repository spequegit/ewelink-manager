package com.looxon;

import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static com.looxon.EwelinkRestController.log;

@Component
public class Scheduler {

    public static final String SOFTENER_EXECUTION =     "0 30 0 5,20 * ?";
    public static final String EVERY_5_SECONDS =    "*/5 * * * * ?";
    public static final String EVERY_MINUTE =       "* * * * * ?";
    public static final String EVERY_2_MINUTES =   "0 */2 * * * ?";
    public static final String EVERY_30_MINUTES =   "* */30 * * * ?";
    public static final String SOFTENER_EXECUTION_INFO = "0 0/10 * * * ?";
    @Autowired
    private EwelinkRestController controller;
    @Autowired
    private ChristmasLightsController christmasLightsController;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Scheduled(cron = "0 0/15 16-22 * * ?")
    public void randomizeChristmasLights() throws Exception {
        christmasLightsController.randomize();
    }

    @Scheduled(cron = "0 0 23 * * ?")
    public void offChristmasLights() throws Exception {
        christmasLightsController.turnOffAllLights();
    }

    @Scheduled(cron = SOFTENER_EXECUTION_INFO)
    public void showRemainingTimeForSoftener() throws ParseException {
        log.info("softener process begin: " + showNextExecutionTime(SOFTENER_EXECUTION));
    }

    @Scheduled(cron = SOFTENER_EXECUTION)
    public void runWaterSoftenerProcess() throws Exception {
        controller.runWaterSoftenerProcess();
    }

    private String showNextExecutionTime(String expression) throws ParseException {
        Date fromDate = new Date();
        TimeZone timeZone = TimeZone.getDefault();
        CronExpression cron = new CronExpression(expression);
        cron.setTimeZone(timeZone);
        Date nextFireTime = cron.getNextValidTimeAfter(fromDate);

        long milliseconds = nextFireTime.getTime() - fromDate.getTime();
        long days = TimeUnit.MILLISECONDS.toDays(milliseconds);
        milliseconds -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
        milliseconds -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);

        return createMessage(days, hours, minutes) + " " + nextFireTime;
    }

    private String createMessage(long days, long hours, long minutes) {
        StringBuilder builder = new StringBuilder();
        if (days > 0) {
            builder.append(days).append(" days ");
        }
        if (hours > 0 || days > 0) {
            builder.append(hours).append(" hours ");
        }
        builder.append(minutes).append(" minutes");
        return builder.toString();
    }
}

//      sekundy /minuty /godziny /dzień-miesiąca /miesiąc /dzień-tygodnia
//      * : every value
//      ? : no specific value (used in day-of-month or day-of-week when the other is specified)
//      / : increment (e.g., 0/15 = every 15 starting at 0)
//      - : range (e.g., 1-5)
//      , : list (e.g., MON,WED,FRI)
//    Schedule,Expression (Spring/Quartz),Explanation
//    Every minute,                         0 * * * * ?             ,At second 0 of every minute
//    Every day at 1:01 AM,                 0 1 1 * * ?             ,1:01:00 AM daily
//    Every hour,                           0 0 * * * ?             ,At the start of every hour
//    Every 5 minutes,                      0 0/5 * * * ?           ,"Starts at 0, then every 5 minutes"
//    Weekdays at 10:15 AM,                 0 15 10 ? * MON-FRI     ,Monday to Friday
//    First day of every month at noon,     0 0 12 1 * ?            ,Day 1 of month
//    Last day of every month at 10 AM,     0 0 10 L * ? (Quartz)   ,"""L"" means last day"