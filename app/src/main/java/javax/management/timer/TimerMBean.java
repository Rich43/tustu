package javax.management.timer;

import java.util.Date;
import java.util.Vector;
import javax.management.InstanceNotFoundException;

/* loaded from: rt.jar:javax/management/timer/TimerMBean.class */
public interface TimerMBean {
    void start();

    void stop();

    Integer addNotification(String str, String str2, Object obj, Date date, long j2, long j3, boolean z2) throws IllegalArgumentException;

    Integer addNotification(String str, String str2, Object obj, Date date, long j2, long j3) throws IllegalArgumentException;

    Integer addNotification(String str, String str2, Object obj, Date date, long j2) throws IllegalArgumentException;

    Integer addNotification(String str, String str2, Object obj, Date date) throws IllegalArgumentException;

    void removeNotification(Integer num) throws InstanceNotFoundException;

    void removeNotifications(String str) throws InstanceNotFoundException;

    void removeAllNotifications();

    int getNbNotifications();

    Vector<Integer> getAllNotificationIDs();

    Vector<Integer> getNotificationIDs(String str);

    String getNotificationType(Integer num);

    String getNotificationMessage(Integer num);

    Object getNotificationUserData(Integer num);

    Date getDate(Integer num);

    Long getPeriod(Integer num);

    Long getNbOccurences(Integer num);

    Boolean getFixedRate(Integer num);

    boolean getSendPastNotifications();

    void setSendPastNotifications(boolean z2);

    boolean isActive();

    boolean isEmpty();
}
