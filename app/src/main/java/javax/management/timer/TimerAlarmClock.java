package javax.management.timer;

import com.sun.jmx.defaults.JmxProperties;
import java.util.Date;
import java.util.TimerTask;
import java.util.logging.Level;

/* loaded from: rt.jar:javax/management/timer/TimerAlarmClock.class */
class TimerAlarmClock extends TimerTask {
    Timer listener;
    long timeout;
    Date next;

    public TimerAlarmClock(Timer timer, long j2) {
        this.listener = null;
        this.timeout = 10000L;
        this.next = null;
        this.listener = timer;
        this.timeout = Math.max(0L, j2);
    }

    public TimerAlarmClock(Timer timer, Date date) {
        this.listener = null;
        this.timeout = 10000L;
        this.next = null;
        this.listener = timer;
        this.next = date;
    }

    @Override // java.util.TimerTask, java.lang.Runnable
    public void run() {
        try {
            this.listener.notifyAlarmClock(new TimerAlarmClockNotification(this));
        } catch (Exception e2) {
            JmxProperties.TIMER_LOGGER.logp(Level.FINEST, Timer.class.getName(), "run", "Got unexpected exception when sending a notification", (Throwable) e2);
        }
    }
}
