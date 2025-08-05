package sun.util.calendar;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/util/calendar/CalendarSystem.class */
public abstract class CalendarSystem {
    private static ConcurrentMap<String, String> names;
    private static ConcurrentMap<String, CalendarSystem> calendars;
    private static final String PACKAGE_NAME = "sun.util.calendar.";
    private static volatile boolean initialized = false;
    private static final String[] namePairs = {"gregorian", "Gregorian", "japanese", "LocalGregorianCalendar", "julian", "JulianCalendar"};
    private static final Gregorian GREGORIAN_INSTANCE = new Gregorian();

    public abstract String getName();

    public abstract CalendarDate getCalendarDate();

    public abstract CalendarDate getCalendarDate(long j2);

    public abstract CalendarDate getCalendarDate(long j2, CalendarDate calendarDate);

    public abstract CalendarDate getCalendarDate(long j2, TimeZone timeZone);

    public abstract CalendarDate newCalendarDate();

    public abstract CalendarDate newCalendarDate(TimeZone timeZone);

    public abstract long getTime(CalendarDate calendarDate);

    public abstract int getYearLength(CalendarDate calendarDate);

    public abstract int getYearLengthInMonths(CalendarDate calendarDate);

    public abstract int getMonthLength(CalendarDate calendarDate);

    public abstract int getWeekLength();

    public abstract Era getEra(String str);

    public abstract Era[] getEras();

    public abstract void setEra(CalendarDate calendarDate, String str);

    public abstract CalendarDate getNthDayOfWeek(int i2, int i3, CalendarDate calendarDate);

    public abstract CalendarDate setTimeOfDay(CalendarDate calendarDate, int i2);

    public abstract boolean validate(CalendarDate calendarDate);

    public abstract boolean normalize(CalendarDate calendarDate);

    private static void initNames() {
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < namePairs.length; i2 += 2) {
            sb.setLength(0);
            concurrentHashMap.put(namePairs[i2], sb.append(PACKAGE_NAME).append(namePairs[i2 + 1]).toString());
        }
        synchronized (CalendarSystem.class) {
            if (!initialized) {
                names = concurrentHashMap;
                calendars = new ConcurrentHashMap();
                initialized = true;
            }
        }
    }

    public static Gregorian getGregorianCalendar() {
        return GREGORIAN_INSTANCE;
    }

    public static CalendarSystem forName(String str) {
        CalendarSystem localGregorianCalendar;
        if ("gregorian".equals(str)) {
            return GREGORIAN_INSTANCE;
        }
        if (!initialized) {
            initNames();
        }
        CalendarSystem calendarSystem = calendars.get(str);
        if (calendarSystem != null) {
            return calendarSystem;
        }
        String str2 = names.get(str);
        if (str2 == null) {
            return null;
        }
        if (str2.endsWith("LocalGregorianCalendar")) {
            localGregorianCalendar = LocalGregorianCalendar.getLocalGregorianCalendar(str);
        } else {
            try {
                localGregorianCalendar = (CalendarSystem) Class.forName(str2).newInstance();
            } catch (Exception e2) {
                throw new InternalError(e2);
            }
        }
        if (localGregorianCalendar == null) {
            return null;
        }
        CalendarSystem calendarSystemPutIfAbsent = calendars.putIfAbsent(str, localGregorianCalendar);
        return calendarSystemPutIfAbsent == null ? localGregorianCalendar : calendarSystemPutIfAbsent;
    }

    public static Properties getCalendarProperties() throws IOException {
        try {
            final String str = ((String) AccessController.doPrivileged(new GetPropertyAction("java.home"))) + File.separator + "lib" + File.separator + "calendars.properties";
            return (Properties) AccessController.doPrivileged(new PrivilegedExceptionAction<Properties>() { // from class: sun.util.calendar.CalendarSystem.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Properties run() throws IOException {
                    Properties properties = new Properties();
                    FileInputStream fileInputStream = new FileInputStream(str);
                    Throwable th = null;
                    try {
                        try {
                            properties.load(fileInputStream);
                            if (fileInputStream != null) {
                                if (0 != 0) {
                                    try {
                                        fileInputStream.close();
                                    } catch (Throwable th2) {
                                        th.addSuppressed(th2);
                                    }
                                } else {
                                    fileInputStream.close();
                                }
                            }
                            return properties;
                        } finally {
                        }
                    } catch (Throwable th3) {
                        if (fileInputStream != null) {
                            if (th != null) {
                                try {
                                    fileInputStream.close();
                                } catch (Throwable th4) {
                                    th.addSuppressed(th4);
                                }
                            } else {
                                fileInputStream.close();
                            }
                        }
                        throw th3;
                    }
                }
            });
        } catch (PrivilegedActionException e2) {
            Throwable cause = e2.getCause();
            if (cause instanceof IOException) {
                throw ((IOException) cause);
            }
            if (cause instanceof IllegalArgumentException) {
                throw ((IllegalArgumentException) cause);
            }
            throw new InternalError(cause);
        }
    }
}
