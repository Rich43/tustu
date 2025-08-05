package javax.management.monitor;

import com.sun.jmx.defaults.JmxProperties;
import java.util.Iterator;
import java.util.logging.Level;
import javax.management.MBeanNotificationInfo;
import javax.management.ObjectName;
import javax.management.monitor.Monitor;

/* loaded from: rt.jar:javax/management/monitor/StringMonitor.class */
public class StringMonitor extends Monitor implements StringMonitorMBean {
    private String stringToCompare = "";
    private boolean notifyMatch = false;
    private boolean notifyDiffer = false;
    private static final String[] types = {MonitorNotification.RUNTIME_ERROR, MonitorNotification.OBSERVED_OBJECT_ERROR, MonitorNotification.OBSERVED_ATTRIBUTE_ERROR, MonitorNotification.OBSERVED_ATTRIBUTE_TYPE_ERROR, MonitorNotification.STRING_TO_COMPARE_VALUE_MATCHED, MonitorNotification.STRING_TO_COMPARE_VALUE_DIFFERED};
    private static final MBeanNotificationInfo[] notifsInfo = {new MBeanNotificationInfo(types, "javax.management.monitor.MonitorNotification", "Notifications sent by the StringMonitor MBean")};
    private static final int MATCHING = 0;
    private static final int DIFFERING = 1;
    private static final int MATCHING_OR_DIFFERING = 2;

    /* loaded from: rt.jar:javax/management/monitor/StringMonitor$StringMonitorObservedObject.class */
    static class StringMonitorObservedObject extends Monitor.ObservedObject {
        private int status;

        public StringMonitorObservedObject(ObjectName objectName) {
            super(objectName);
        }

        public final synchronized int getStatus() {
            return this.status;
        }

        public final synchronized void setStatus(int i2) {
            this.status = i2;
        }
    }

    @Override // javax.management.monitor.Monitor, javax.management.monitor.MonitorMBean
    public synchronized void start() {
        if (isActive()) {
            JmxProperties.MONITOR_LOGGER.logp(Level.FINER, StringMonitor.class.getName(), "start", "the monitor is already active");
            return;
        }
        Iterator<Monitor.ObservedObject> it = this.observedObjects.iterator();
        while (it.hasNext()) {
            ((StringMonitorObservedObject) it.next()).setStatus(2);
        }
        doStart();
    }

    @Override // javax.management.monitor.Monitor, javax.management.monitor.MonitorMBean
    public synchronized void stop() {
        doStop();
    }

    @Override // javax.management.monitor.Monitor, javax.management.monitor.CounterMonitorMBean
    public synchronized String getDerivedGauge(ObjectName objectName) {
        return (String) super.getDerivedGauge(objectName);
    }

    @Override // javax.management.monitor.Monitor, javax.management.monitor.CounterMonitorMBean
    public synchronized long getDerivedGaugeTimeStamp(ObjectName objectName) {
        return super.getDerivedGaugeTimeStamp(objectName);
    }

    @Override // javax.management.monitor.StringMonitorMBean
    @Deprecated
    public synchronized String getDerivedGauge() {
        if (this.observedObjects.isEmpty()) {
            return null;
        }
        return (String) this.observedObjects.get(0).getDerivedGauge();
    }

    @Override // javax.management.monitor.StringMonitorMBean
    @Deprecated
    public synchronized long getDerivedGaugeTimeStamp() {
        if (this.observedObjects.isEmpty()) {
            return 0L;
        }
        return this.observedObjects.get(0).getDerivedGaugeTimeStamp();
    }

    @Override // javax.management.monitor.StringMonitorMBean
    public synchronized String getStringToCompare() {
        return this.stringToCompare;
    }

    @Override // javax.management.monitor.StringMonitorMBean
    public synchronized void setStringToCompare(String str) throws IllegalArgumentException {
        if (str == null) {
            throw new IllegalArgumentException("Null string to compare");
        }
        if (this.stringToCompare.equals(str)) {
            return;
        }
        this.stringToCompare = str;
        Iterator<Monitor.ObservedObject> it = this.observedObjects.iterator();
        while (it.hasNext()) {
            ((StringMonitorObservedObject) it.next()).setStatus(2);
        }
    }

    @Override // javax.management.monitor.StringMonitorMBean
    public synchronized boolean getNotifyMatch() {
        return this.notifyMatch;
    }

    @Override // javax.management.monitor.StringMonitorMBean
    public synchronized void setNotifyMatch(boolean z2) {
        if (this.notifyMatch == z2) {
            return;
        }
        this.notifyMatch = z2;
    }

    @Override // javax.management.monitor.StringMonitorMBean
    public synchronized boolean getNotifyDiffer() {
        return this.notifyDiffer;
    }

    @Override // javax.management.monitor.StringMonitorMBean
    public synchronized void setNotifyDiffer(boolean z2) {
        if (this.notifyDiffer == z2) {
            return;
        }
        this.notifyDiffer = z2;
    }

    @Override // javax.management.NotificationBroadcasterSupport, javax.management.NotificationBroadcaster
    public MBeanNotificationInfo[] getNotificationInfo() {
        return (MBeanNotificationInfo[]) notifsInfo.clone();
    }

    @Override // javax.management.monitor.Monitor
    Monitor.ObservedObject createObservedObject(ObjectName objectName) {
        StringMonitorObservedObject stringMonitorObservedObject = new StringMonitorObservedObject(objectName);
        stringMonitorObservedObject.setStatus(2);
        return stringMonitorObservedObject;
    }

    @Override // javax.management.monitor.Monitor
    synchronized boolean isComparableTypeValid(ObjectName objectName, String str, Comparable<?> comparable) {
        if (comparable instanceof String) {
            return true;
        }
        return false;
    }

    @Override // javax.management.monitor.Monitor
    synchronized void onErrorNotification(MonitorNotification monitorNotification) {
        StringMonitorObservedObject stringMonitorObservedObject = (StringMonitorObservedObject) getObservedObject(monitorNotification.getObservedObject());
        if (stringMonitorObservedObject == null) {
            return;
        }
        stringMonitorObservedObject.setStatus(2);
    }

    @Override // javax.management.monitor.Monitor
    synchronized MonitorNotification buildAlarmNotification(ObjectName objectName, String str, Comparable<?> comparable) {
        String str2 = null;
        String str3 = null;
        String str4 = null;
        StringMonitorObservedObject stringMonitorObservedObject = (StringMonitorObservedObject) getObservedObject(objectName);
        if (stringMonitorObservedObject == null) {
            return null;
        }
        if (stringMonitorObservedObject.getStatus() == 2) {
            if (stringMonitorObservedObject.getDerivedGauge().equals(this.stringToCompare)) {
                if (this.notifyMatch) {
                    str2 = MonitorNotification.STRING_TO_COMPARE_VALUE_MATCHED;
                    str3 = "";
                    str4 = this.stringToCompare;
                }
                stringMonitorObservedObject.setStatus(1);
            } else {
                if (this.notifyDiffer) {
                    str2 = MonitorNotification.STRING_TO_COMPARE_VALUE_DIFFERED;
                    str3 = "";
                    str4 = this.stringToCompare;
                }
                stringMonitorObservedObject.setStatus(0);
            }
        } else if (stringMonitorObservedObject.getStatus() == 0) {
            if (stringMonitorObservedObject.getDerivedGauge().equals(this.stringToCompare)) {
                if (this.notifyMatch) {
                    str2 = MonitorNotification.STRING_TO_COMPARE_VALUE_MATCHED;
                    str3 = "";
                    str4 = this.stringToCompare;
                }
                stringMonitorObservedObject.setStatus(1);
            }
        } else if (stringMonitorObservedObject.getStatus() == 1 && !stringMonitorObservedObject.getDerivedGauge().equals(this.stringToCompare)) {
            if (this.notifyDiffer) {
                str2 = MonitorNotification.STRING_TO_COMPARE_VALUE_DIFFERED;
                str3 = "";
                str4 = this.stringToCompare;
            }
            stringMonitorObservedObject.setStatus(0);
        }
        return new MonitorNotification(str2, this, 0L, 0L, str3, null, null, null, str4);
    }
}
