package javax.management.timer;

import com.sun.jmx.defaults.JmxProperties;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;
import java.util.logging.Level;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;

/* loaded from: rt.jar:javax/management/timer/Timer.class */
public class Timer extends NotificationBroadcasterSupport implements TimerMBean, MBeanRegistration {
    public static final long ONE_SECOND = 1000;
    public static final long ONE_MINUTE = 60000;
    public static final long ONE_HOUR = 3600000;
    public static final long ONE_DAY = 86400000;
    public static final long ONE_WEEK = 604800000;
    private static final int TIMER_NOTIF_INDEX = 0;
    private static final int TIMER_DATE_INDEX = 1;
    private static final int TIMER_PERIOD_INDEX = 2;
    private static final int TIMER_NB_OCCUR_INDEX = 3;
    private static final int ALARM_CLOCK_INDEX = 4;
    private static final int FIXED_RATE_INDEX = 5;
    private java.util.Timer timer;
    private final Map<Integer, Object[]> timerTable = new HashMap();
    private boolean sendPastNotifications = false;
    private transient boolean isActive = false;
    private transient long sequenceNumber = 0;
    private volatile int counterID = 0;

    @Override // javax.management.MBeanRegistration
    public ObjectName preRegister(MBeanServer mBeanServer, ObjectName objectName) throws Exception {
        return objectName;
    }

    @Override // javax.management.MBeanRegistration
    public void postRegister(Boolean bool) {
    }

    @Override // javax.management.MBeanRegistration
    public void preDeregister() throws Exception {
        JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "preDeregister", "stop the timer");
        stop();
    }

    @Override // javax.management.MBeanRegistration
    public void postDeregister() {
    }

    @Override // javax.management.NotificationBroadcasterSupport, javax.management.NotificationBroadcaster
    public synchronized MBeanNotificationInfo[] getNotificationInfo() {
        TreeSet treeSet = new TreeSet();
        Iterator<Object[]> it = this.timerTable.values().iterator();
        while (it.hasNext()) {
            treeSet.add(((TimerNotification) it.next()[0]).getType());
        }
        return new MBeanNotificationInfo[]{new MBeanNotificationInfo((String[]) treeSet.toArray(new String[0]), TimerNotification.class.getName(), "Notification sent by Timer MBean")};
    }

    @Override // javax.management.timer.TimerMBean
    public synchronized void start() {
        JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "start", "starting the timer");
        if (!this.isActive) {
            this.timer = new java.util.Timer();
            Date date = new Date();
            sendPastNotifications(date, this.sendPastNotifications);
            for (Object[] objArr : this.timerTable.values()) {
                Date date2 = (Date) objArr[1];
                if (((Boolean) objArr[5]).booleanValue()) {
                    TimerAlarmClock timerAlarmClock = new TimerAlarmClock(this, date2);
                    objArr[4] = timerAlarmClock;
                    this.timer.schedule(timerAlarmClock, timerAlarmClock.next);
                } else {
                    TimerAlarmClock timerAlarmClock2 = new TimerAlarmClock(this, date2.getTime() - date.getTime());
                    objArr[4] = timerAlarmClock2;
                    this.timer.schedule(timerAlarmClock2, timerAlarmClock2.timeout);
                }
            }
            this.isActive = true;
            JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "start", "timer started");
            return;
        }
        JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "start", "the timer is already activated");
    }

    @Override // javax.management.timer.TimerMBean
    public synchronized void stop() {
        JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "stop", "stopping the timer");
        if (this.isActive) {
            Iterator<Object[]> it = this.timerTable.values().iterator();
            while (it.hasNext()) {
                TimerAlarmClock timerAlarmClock = (TimerAlarmClock) it.next()[4];
                if (timerAlarmClock != null) {
                    timerAlarmClock.cancel();
                }
            }
            this.timer.cancel();
            this.isActive = false;
            JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "stop", "timer stopped");
            return;
        }
        JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "stop", "the timer is already deactivated");
    }

    @Override // javax.management.timer.TimerMBean
    public synchronized Integer addNotification(String str, String str2, Object obj, Date date, long j2, long j3, boolean z2) throws IllegalArgumentException {
        TimerAlarmClock timerAlarmClock;
        if (date == null) {
            throw new IllegalArgumentException("Timer notification date cannot be null.");
        }
        if (j2 < 0 || j3 < 0) {
            throw new IllegalArgumentException("Negative values for the periodicity");
        }
        Date date2 = new Date();
        if (date2.after(date)) {
            date.setTime(date2.getTime());
            if (JmxProperties.TIMER_LOGGER.isLoggable(Level.FINER)) {
                JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "addNotification", "update timer notification to add with:\n\tNotification date = " + ((Object) date));
            }
        }
        int i2 = this.counterID + 1;
        this.counterID = i2;
        Integer numValueOf = Integer.valueOf(i2);
        TimerNotification timerNotification = new TimerNotification(str, this, 0L, 0L, str2, numValueOf);
        timerNotification.setUserData(obj);
        Object[] objArr = new Object[6];
        if (z2) {
            timerAlarmClock = new TimerAlarmClock(this, date);
        } else {
            timerAlarmClock = new TimerAlarmClock(this, date.getTime() - date2.getTime());
        }
        Date date3 = new Date(date.getTime());
        objArr[0] = timerNotification;
        objArr[1] = date3;
        objArr[2] = Long.valueOf(j2);
        objArr[3] = Long.valueOf(j3);
        objArr[4] = timerAlarmClock;
        objArr[5] = Boolean.valueOf(z2);
        if (JmxProperties.TIMER_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "addNotification", "adding timer notification:\n\tNotification source = " + timerNotification.getSource() + "\n\tNotification type = " + timerNotification.getType() + "\n\tNotification ID = " + ((Object) numValueOf) + "\n\tNotification date = " + ((Object) date3) + "\n\tNotification period = " + j2 + "\n\tNotification nb of occurrences = " + j3 + "\n\tNotification executes at fixed rate = " + z2);
        }
        this.timerTable.put(numValueOf, objArr);
        if (this.isActive) {
            if (z2) {
                this.timer.schedule(timerAlarmClock, timerAlarmClock.next);
            } else {
                this.timer.schedule(timerAlarmClock, timerAlarmClock.timeout);
            }
        }
        JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "addNotification", "timer notification added");
        return numValueOf;
    }

    @Override // javax.management.timer.TimerMBean
    public synchronized Integer addNotification(String str, String str2, Object obj, Date date, long j2, long j3) throws IllegalArgumentException {
        return addNotification(str, str2, obj, date, j2, j3, false);
    }

    @Override // javax.management.timer.TimerMBean
    public synchronized Integer addNotification(String str, String str2, Object obj, Date date, long j2) throws IllegalArgumentException {
        return addNotification(str, str2, obj, date, j2, 0L);
    }

    @Override // javax.management.timer.TimerMBean
    public synchronized Integer addNotification(String str, String str2, Object obj, Date date) throws IllegalArgumentException {
        return addNotification(str, str2, obj, date, 0L, 0L);
    }

    @Override // javax.management.timer.TimerMBean
    public synchronized void removeNotification(Integer num) throws InstanceNotFoundException {
        if (!this.timerTable.containsKey(num)) {
            throw new InstanceNotFoundException("Timer notification to remove not in the list of notifications");
        }
        Object[] objArr = this.timerTable.get(num);
        TimerAlarmClock timerAlarmClock = (TimerAlarmClock) objArr[4];
        if (timerAlarmClock != null) {
            timerAlarmClock.cancel();
        }
        if (JmxProperties.TIMER_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "removeNotification", "removing timer notification:\n\tNotification source = " + ((TimerNotification) objArr[0]).getSource() + "\n\tNotification type = " + ((TimerNotification) objArr[0]).getType() + "\n\tNotification ID = " + ((Object) ((TimerNotification) objArr[0]).getNotificationID()) + "\n\tNotification date = " + objArr[1] + "\n\tNotification period = " + objArr[2] + "\n\tNotification nb of occurrences = " + objArr[3] + "\n\tNotification executes at fixed rate = " + objArr[5]);
        }
        this.timerTable.remove(num);
        JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "removeNotification", "timer notification removed");
    }

    @Override // javax.management.timer.TimerMBean
    public synchronized void removeNotifications(String str) throws InstanceNotFoundException {
        Vector<Integer> notificationIDs = getNotificationIDs(str);
        if (notificationIDs.isEmpty()) {
            throw new InstanceNotFoundException("Timer notifications to remove not in the list of notifications");
        }
        Iterator<Integer> it = notificationIDs.iterator();
        while (it.hasNext()) {
            removeNotification(it.next());
        }
    }

    @Override // javax.management.timer.TimerMBean
    public synchronized void removeAllNotifications() {
        Iterator<Object[]> it = this.timerTable.values().iterator();
        while (it.hasNext()) {
            ((TimerAlarmClock) it.next()[4]).cancel();
        }
        JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "removeAllNotifications", "removing all timer notifications");
        this.timerTable.clear();
        JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "removeAllNotifications", "all timer notifications removed");
        this.counterID = 0;
        JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "removeAllNotifications", "timer notification counter ID reset");
    }

    @Override // javax.management.timer.TimerMBean
    public synchronized int getNbNotifications() {
        return this.timerTable.size();
    }

    @Override // javax.management.timer.TimerMBean
    public synchronized Vector<Integer> getAllNotificationIDs() {
        return new Vector<>(this.timerTable.keySet());
    }

    @Override // javax.management.timer.TimerMBean
    public synchronized Vector<Integer> getNotificationIDs(String str) {
        Vector<Integer> vector = new Vector<>();
        for (Map.Entry<Integer, Object[]> entry : this.timerTable.entrySet()) {
            String type = ((TimerNotification) entry.getValue()[0]).getType();
            if (str == null) {
                if (type == null) {
                    vector.addElement(entry.getKey());
                }
            } else if (str.equals(type)) {
                vector.addElement(entry.getKey());
            }
        }
        return vector;
    }

    @Override // javax.management.timer.TimerMBean
    public synchronized String getNotificationType(Integer num) {
        Object[] objArr = this.timerTable.get(num);
        if (objArr != null) {
            return ((TimerNotification) objArr[0]).getType();
        }
        return null;
    }

    @Override // javax.management.timer.TimerMBean
    public synchronized String getNotificationMessage(Integer num) {
        Object[] objArr = this.timerTable.get(num);
        if (objArr != null) {
            return ((TimerNotification) objArr[0]).getMessage();
        }
        return null;
    }

    @Override // javax.management.timer.TimerMBean
    public synchronized Object getNotificationUserData(Integer num) {
        Object[] objArr = this.timerTable.get(num);
        if (objArr != null) {
            return ((TimerNotification) objArr[0]).getUserData();
        }
        return null;
    }

    @Override // javax.management.timer.TimerMBean
    public synchronized Date getDate(Integer num) {
        Object[] objArr = this.timerTable.get(num);
        if (objArr != null) {
            return new Date(((Date) objArr[1]).getTime());
        }
        return null;
    }

    @Override // javax.management.timer.TimerMBean
    public synchronized Long getPeriod(Integer num) {
        Object[] objArr = this.timerTable.get(num);
        if (objArr != null) {
            return (Long) objArr[2];
        }
        return null;
    }

    @Override // javax.management.timer.TimerMBean
    public synchronized Long getNbOccurences(Integer num) {
        Object[] objArr = this.timerTable.get(num);
        if (objArr != null) {
            return (Long) objArr[3];
        }
        return null;
    }

    @Override // javax.management.timer.TimerMBean
    public synchronized Boolean getFixedRate(Integer num) {
        Object[] objArr = this.timerTable.get(num);
        if (objArr != null) {
            return Boolean.valueOf(((Boolean) objArr[5]).booleanValue());
        }
        return null;
    }

    @Override // javax.management.timer.TimerMBean
    public boolean getSendPastNotifications() {
        return this.sendPastNotifications;
    }

    @Override // javax.management.timer.TimerMBean
    public void setSendPastNotifications(boolean z2) {
        this.sendPastNotifications = z2;
    }

    @Override // javax.management.timer.TimerMBean
    public boolean isActive() {
        return this.isActive;
    }

    @Override // javax.management.timer.TimerMBean
    public synchronized boolean isEmpty() {
        return this.timerTable.isEmpty();
    }

    private synchronized void sendPastNotifications(Date date, boolean z2) {
        Iterator it = new ArrayList(this.timerTable.values()).iterator();
        while (it.hasNext()) {
            Object[] objArr = (Object[]) it.next();
            TimerNotification timerNotification = (TimerNotification) objArr[0];
            Integer notificationID = timerNotification.getNotificationID();
            Date date2 = (Date) objArr[1];
            while (date.after(date2) && this.timerTable.containsKey(notificationID)) {
                if (z2) {
                    if (JmxProperties.TIMER_LOGGER.isLoggable(Level.FINER)) {
                        JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "sendPastNotifications", "sending past timer notification:\n\tNotification source = " + timerNotification.getSource() + "\n\tNotification type = " + timerNotification.getType() + "\n\tNotification ID = " + ((Object) timerNotification.getNotificationID()) + "\n\tNotification date = " + ((Object) date2) + "\n\tNotification period = " + objArr[2] + "\n\tNotification nb of occurrences = " + objArr[3] + "\n\tNotification executes at fixed rate = " + objArr[5]);
                    }
                    sendNotification(date2, timerNotification);
                    JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "sendPastNotifications", "past timer notification sent");
                }
                updateTimerTable(timerNotification.getNotificationID());
            }
        }
    }

    private synchronized void updateTimerTable(Integer num) {
        Object[] objArr = this.timerTable.get(num);
        Date date = (Date) objArr[1];
        Long l2 = (Long) objArr[2];
        Long l3 = (Long) objArr[3];
        Boolean bool = (Boolean) objArr[5];
        TimerAlarmClock timerAlarmClock = (TimerAlarmClock) objArr[4];
        if (l2.longValue() == 0) {
            if (timerAlarmClock != null) {
                timerAlarmClock.cancel();
            }
            this.timerTable.remove(num);
            return;
        }
        if (l3.longValue() == 0 || l3.longValue() > 1) {
            date.setTime(date.getTime() + l2.longValue());
            objArr[3] = Long.valueOf(Math.max(0L, l3.longValue() - 1));
            Long l4 = (Long) objArr[3];
            if (this.isActive) {
                if (bool.booleanValue()) {
                    TimerAlarmClock timerAlarmClock2 = new TimerAlarmClock(this, date);
                    objArr[4] = timerAlarmClock2;
                    this.timer.schedule(timerAlarmClock2, timerAlarmClock2.next);
                } else {
                    TimerAlarmClock timerAlarmClock3 = new TimerAlarmClock(this, l2.longValue());
                    objArr[4] = timerAlarmClock3;
                    this.timer.schedule(timerAlarmClock3, timerAlarmClock3.timeout);
                }
            }
            if (JmxProperties.TIMER_LOGGER.isLoggable(Level.FINER)) {
                TimerNotification timerNotification = (TimerNotification) objArr[0];
                JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "updateTimerTable", "update timer notification with:\n\tNotification source = " + timerNotification.getSource() + "\n\tNotification type = " + timerNotification.getType() + "\n\tNotification ID = " + ((Object) num) + "\n\tNotification date = " + ((Object) date) + "\n\tNotification period = " + ((Object) l2) + "\n\tNotification nb of occurrences = " + ((Object) l4) + "\n\tNotification executes at fixed rate = " + ((Object) bool));
                return;
            }
            return;
        }
        if (timerAlarmClock != null) {
            timerAlarmClock.cancel();
        }
        this.timerTable.remove(num);
    }

    void notifyAlarmClock(TimerAlarmClockNotification timerAlarmClockNotification) {
        TimerNotification timerNotification = null;
        Date date = null;
        TimerAlarmClock timerAlarmClock = (TimerAlarmClock) timerAlarmClockNotification.getSource();
        synchronized (this) {
            Iterator<Object[]> it = this.timerTable.values().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Object[] next = it.next();
                if (next[4] == timerAlarmClock) {
                    timerNotification = (TimerNotification) next[0];
                    date = (Date) next[1];
                    break;
                }
            }
        }
        sendNotification(date, timerNotification);
        updateTimerTable(timerNotification.getNotificationID());
    }

    void sendNotification(Date date, TimerNotification timerNotification) {
        long j2;
        if (JmxProperties.TIMER_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "sendNotification", "sending timer notification:\n\tNotification source = " + timerNotification.getSource() + "\n\tNotification type = " + timerNotification.getType() + "\n\tNotification ID = " + ((Object) timerNotification.getNotificationID()) + "\n\tNotification date = " + ((Object) date));
        }
        synchronized (this) {
            this.sequenceNumber++;
            j2 = this.sequenceNumber;
        }
        synchronized (timerNotification) {
            timerNotification.setTimeStamp(date.getTime());
            timerNotification.setSequenceNumber(j2);
            sendNotification((TimerNotification) timerNotification.cloneTimerNotification());
        }
        JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "sendNotification", "timer notification sent");
    }
}
