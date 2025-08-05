package javax.management.monitor;

import com.sun.jmx.defaults.JmxProperties;
import java.util.Iterator;
import java.util.logging.Level;
import javax.management.MBeanNotificationInfo;
import javax.management.ObjectName;
import javax.management.monitor.Monitor;

/* loaded from: rt.jar:javax/management/monitor/CounterMonitor.class */
public class CounterMonitor extends Monitor implements CounterMonitorMBean {
    private Number modulus = INTEGER_ZERO;
    private Number offset = INTEGER_ZERO;
    private boolean notify = false;
    private boolean differenceMode = false;
    private Number initThreshold = INTEGER_ZERO;
    private static final String[] types = {MonitorNotification.RUNTIME_ERROR, MonitorNotification.OBSERVED_OBJECT_ERROR, MonitorNotification.OBSERVED_ATTRIBUTE_ERROR, MonitorNotification.OBSERVED_ATTRIBUTE_TYPE_ERROR, MonitorNotification.THRESHOLD_ERROR, MonitorNotification.THRESHOLD_VALUE_EXCEEDED};
    private static final MBeanNotificationInfo[] notifsInfo = {new MBeanNotificationInfo(types, "javax.management.monitor.MonitorNotification", "Notifications sent by the CounterMonitor MBean")};

    /* loaded from: rt.jar:javax/management/monitor/CounterMonitor$CounterMonitorObservedObject.class */
    static class CounterMonitorObservedObject extends Monitor.ObservedObject {
        private Number threshold;
        private Number previousScanCounter;
        private boolean modulusExceeded;
        private Number derivedGaugeExceeded;
        private boolean derivedGaugeValid;
        private boolean eventAlreadyNotified;
        private Monitor.NumericalType type;

        public CounterMonitorObservedObject(ObjectName objectName) {
            super(objectName);
        }

        public final synchronized Number getThreshold() {
            return this.threshold;
        }

        public final synchronized void setThreshold(Number number) {
            this.threshold = number;
        }

        public final synchronized Number getPreviousScanCounter() {
            return this.previousScanCounter;
        }

        public final synchronized void setPreviousScanCounter(Number number) {
            this.previousScanCounter = number;
        }

        public final synchronized boolean getModulusExceeded() {
            return this.modulusExceeded;
        }

        public final synchronized void setModulusExceeded(boolean z2) {
            this.modulusExceeded = z2;
        }

        public final synchronized Number getDerivedGaugeExceeded() {
            return this.derivedGaugeExceeded;
        }

        public final synchronized void setDerivedGaugeExceeded(Number number) {
            this.derivedGaugeExceeded = number;
        }

        public final synchronized boolean getDerivedGaugeValid() {
            return this.derivedGaugeValid;
        }

        public final synchronized void setDerivedGaugeValid(boolean z2) {
            this.derivedGaugeValid = z2;
        }

        public final synchronized boolean getEventAlreadyNotified() {
            return this.eventAlreadyNotified;
        }

        public final synchronized void setEventAlreadyNotified(boolean z2) {
            this.eventAlreadyNotified = z2;
        }

        public final synchronized Monitor.NumericalType getType() {
            return this.type;
        }

        public final synchronized void setType(Monitor.NumericalType numericalType) {
            this.type = numericalType;
        }
    }

    @Override // javax.management.monitor.Monitor, javax.management.monitor.MonitorMBean
    public synchronized void start() {
        if (isActive()) {
            JmxProperties.MONITOR_LOGGER.logp(Level.FINER, CounterMonitor.class.getName(), "start", "the monitor is already active");
            return;
        }
        Iterator<Monitor.ObservedObject> it = this.observedObjects.iterator();
        while (it.hasNext()) {
            CounterMonitorObservedObject counterMonitorObservedObject = (CounterMonitorObservedObject) it.next();
            counterMonitorObservedObject.setThreshold(this.initThreshold);
            counterMonitorObservedObject.setModulusExceeded(false);
            counterMonitorObservedObject.setEventAlreadyNotified(false);
            counterMonitorObservedObject.setPreviousScanCounter(null);
        }
        doStart();
    }

    @Override // javax.management.monitor.Monitor, javax.management.monitor.MonitorMBean
    public synchronized void stop() {
        doStop();
    }

    @Override // javax.management.monitor.Monitor, javax.management.monitor.CounterMonitorMBean
    public synchronized Number getDerivedGauge(ObjectName objectName) {
        return (Number) super.getDerivedGauge(objectName);
    }

    @Override // javax.management.monitor.Monitor, javax.management.monitor.CounterMonitorMBean
    public synchronized long getDerivedGaugeTimeStamp(ObjectName objectName) {
        return super.getDerivedGaugeTimeStamp(objectName);
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    public synchronized Number getThreshold(ObjectName objectName) {
        CounterMonitorObservedObject counterMonitorObservedObject = (CounterMonitorObservedObject) getObservedObject(objectName);
        if (counterMonitorObservedObject == null) {
            return null;
        }
        if (this.offset.longValue() > 0 && this.modulus.longValue() > 0 && counterMonitorObservedObject.getThreshold().longValue() > this.modulus.longValue()) {
            return this.initThreshold;
        }
        return counterMonitorObservedObject.getThreshold();
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    public synchronized Number getInitThreshold() {
        return this.initThreshold;
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    public synchronized void setInitThreshold(Number number) throws IllegalArgumentException {
        if (number == null) {
            throw new IllegalArgumentException("Null threshold");
        }
        if (number.longValue() < 0) {
            throw new IllegalArgumentException("Negative threshold");
        }
        if (this.initThreshold.equals(number)) {
            return;
        }
        this.initThreshold = number;
        int i2 = 0;
        for (Monitor.ObservedObject observedObject : this.observedObjects) {
            int i3 = i2;
            i2++;
            resetAlreadyNotified(observedObject, i3, 16);
            CounterMonitorObservedObject counterMonitorObservedObject = (CounterMonitorObservedObject) observedObject;
            counterMonitorObservedObject.setThreshold(number);
            counterMonitorObservedObject.setModulusExceeded(false);
            counterMonitorObservedObject.setEventAlreadyNotified(false);
        }
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    @Deprecated
    public synchronized Number getDerivedGauge() {
        if (this.observedObjects.isEmpty()) {
            return null;
        }
        return (Number) this.observedObjects.get(0).getDerivedGauge();
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    @Deprecated
    public synchronized long getDerivedGaugeTimeStamp() {
        if (this.observedObjects.isEmpty()) {
            return 0L;
        }
        return this.observedObjects.get(0).getDerivedGaugeTimeStamp();
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    @Deprecated
    public synchronized Number getThreshold() {
        return getThreshold(getObservedObject());
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    @Deprecated
    public synchronized void setThreshold(Number number) throws IllegalArgumentException {
        setInitThreshold(number);
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    public synchronized Number getOffset() {
        return this.offset;
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    public synchronized void setOffset(Number number) throws IllegalArgumentException {
        if (number == null) {
            throw new IllegalArgumentException("Null offset");
        }
        if (number.longValue() < 0) {
            throw new IllegalArgumentException("Negative offset");
        }
        if (this.offset.equals(number)) {
            return;
        }
        this.offset = number;
        int i2 = 0;
        Iterator<Monitor.ObservedObject> it = this.observedObjects.iterator();
        while (it.hasNext()) {
            int i3 = i2;
            i2++;
            resetAlreadyNotified(it.next(), i3, 16);
        }
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    public synchronized Number getModulus() {
        return this.modulus;
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    public synchronized void setModulus(Number number) throws IllegalArgumentException {
        if (number == null) {
            throw new IllegalArgumentException("Null modulus");
        }
        if (number.longValue() < 0) {
            throw new IllegalArgumentException("Negative modulus");
        }
        if (this.modulus.equals(number)) {
            return;
        }
        this.modulus = number;
        int i2 = 0;
        for (Monitor.ObservedObject observedObject : this.observedObjects) {
            int i3 = i2;
            i2++;
            resetAlreadyNotified(observedObject, i3, 16);
            ((CounterMonitorObservedObject) observedObject).setModulusExceeded(false);
        }
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    public synchronized boolean getNotify() {
        return this.notify;
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    public synchronized void setNotify(boolean z2) {
        if (this.notify == z2) {
            return;
        }
        this.notify = z2;
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    public synchronized boolean getDifferenceMode() {
        return this.differenceMode;
    }

    @Override // javax.management.monitor.CounterMonitorMBean
    public synchronized void setDifferenceMode(boolean z2) {
        if (this.differenceMode == z2) {
            return;
        }
        this.differenceMode = z2;
        Iterator<Monitor.ObservedObject> it = this.observedObjects.iterator();
        while (it.hasNext()) {
            CounterMonitorObservedObject counterMonitorObservedObject = (CounterMonitorObservedObject) it.next();
            counterMonitorObservedObject.setThreshold(this.initThreshold);
            counterMonitorObservedObject.setModulusExceeded(false);
            counterMonitorObservedObject.setEventAlreadyNotified(false);
            counterMonitorObservedObject.setPreviousScanCounter(null);
        }
    }

    @Override // javax.management.NotificationBroadcasterSupport, javax.management.NotificationBroadcaster
    public MBeanNotificationInfo[] getNotificationInfo() {
        return (MBeanNotificationInfo[]) notifsInfo.clone();
    }

    private synchronized boolean updateDerivedGauge(Object obj, CounterMonitorObservedObject counterMonitorObservedObject) {
        boolean z2;
        if (this.differenceMode) {
            if (counterMonitorObservedObject.getPreviousScanCounter() != null) {
                setDerivedGaugeWithDifference((Number) obj, null, counterMonitorObservedObject);
                if (((Number) counterMonitorObservedObject.getDerivedGauge()).longValue() < 0) {
                    if (this.modulus.longValue() > 0) {
                        setDerivedGaugeWithDifference((Number) obj, this.modulus, counterMonitorObservedObject);
                    }
                    counterMonitorObservedObject.setThreshold(this.initThreshold);
                    counterMonitorObservedObject.setEventAlreadyNotified(false);
                }
                z2 = true;
            } else {
                z2 = false;
            }
            counterMonitorObservedObject.setPreviousScanCounter((Number) obj);
        } else {
            counterMonitorObservedObject.setDerivedGauge((Number) obj);
            z2 = true;
        }
        return z2;
    }

    private synchronized MonitorNotification updateNotifications(CounterMonitorObservedObject counterMonitorObservedObject) {
        MonitorNotification monitorNotification = null;
        if (counterMonitorObservedObject.getEventAlreadyNotified()) {
            if (JmxProperties.MONITOR_LOGGER.isLoggable(Level.FINER)) {
                JmxProperties.MONITOR_LOGGER.logp(Level.FINER, CounterMonitor.class.getName(), "updateNotifications", "The notification:\n\tNotification observed object = " + ((Object) counterMonitorObservedObject.getObservedObject()) + "\n\tNotification observed attribute = " + getObservedAttribute() + "\n\tNotification threshold level = " + ((Object) counterMonitorObservedObject.getThreshold()) + "\n\tNotification derived gauge = " + counterMonitorObservedObject.getDerivedGauge() + "\nhas already been sent");
            }
        } else if (((Number) counterMonitorObservedObject.getDerivedGauge()).longValue() >= counterMonitorObservedObject.getThreshold().longValue()) {
            if (this.notify) {
                monitorNotification = new MonitorNotification(MonitorNotification.THRESHOLD_VALUE_EXCEEDED, this, 0L, 0L, "", null, null, null, counterMonitorObservedObject.getThreshold());
            }
            if (!this.differenceMode) {
                counterMonitorObservedObject.setEventAlreadyNotified(true);
            }
        }
        return monitorNotification;
    }

    private synchronized void updateThreshold(CounterMonitorObservedObject counterMonitorObservedObject) {
        long j2;
        if (((Number) counterMonitorObservedObject.getDerivedGauge()).longValue() >= counterMonitorObservedObject.getThreshold().longValue()) {
            if (this.offset.longValue() > 0) {
                long jLongValue = counterMonitorObservedObject.getThreshold().longValue();
                while (true) {
                    j2 = jLongValue;
                    if (((Number) counterMonitorObservedObject.getDerivedGauge()).longValue() < j2) {
                        break;
                    } else {
                        jLongValue = j2 + this.offset.longValue();
                    }
                }
                switch (counterMonitorObservedObject.getType()) {
                    case INTEGER:
                        counterMonitorObservedObject.setThreshold(Integer.valueOf((int) j2));
                        break;
                    case BYTE:
                        counterMonitorObservedObject.setThreshold(Byte.valueOf((byte) j2));
                        break;
                    case SHORT:
                        counterMonitorObservedObject.setThreshold(Short.valueOf((short) j2));
                        break;
                    case LONG:
                        counterMonitorObservedObject.setThreshold(Long.valueOf(j2));
                        break;
                    default:
                        JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, CounterMonitor.class.getName(), "updateThreshold", "the threshold type is invalid");
                        break;
                }
                if (!this.differenceMode && this.modulus.longValue() > 0 && counterMonitorObservedObject.getThreshold().longValue() > this.modulus.longValue()) {
                    counterMonitorObservedObject.setModulusExceeded(true);
                    counterMonitorObservedObject.setDerivedGaugeExceeded((Number) counterMonitorObservedObject.getDerivedGauge());
                }
                counterMonitorObservedObject.setEventAlreadyNotified(false);
                return;
            }
            counterMonitorObservedObject.setModulusExceeded(true);
            counterMonitorObservedObject.setDerivedGaugeExceeded((Number) counterMonitorObservedObject.getDerivedGauge());
        }
    }

    private synchronized void setDerivedGaugeWithDifference(Number number, Number number2, CounterMonitorObservedObject counterMonitorObservedObject) {
        long jLongValue = number.longValue() - counterMonitorObservedObject.getPreviousScanCounter().longValue();
        if (number2 != null) {
            jLongValue += this.modulus.longValue();
        }
        switch (counterMonitorObservedObject.getType()) {
            case INTEGER:
                counterMonitorObservedObject.setDerivedGauge(Integer.valueOf((int) jLongValue));
                break;
            case BYTE:
                counterMonitorObservedObject.setDerivedGauge(Byte.valueOf((byte) jLongValue));
                break;
            case SHORT:
                counterMonitorObservedObject.setDerivedGauge(Short.valueOf((short) jLongValue));
                break;
            case LONG:
                counterMonitorObservedObject.setDerivedGauge(Long.valueOf(jLongValue));
                break;
            default:
                JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, CounterMonitor.class.getName(), "setDerivedGaugeWithDifference", "the threshold type is invalid");
                break;
        }
    }

    @Override // javax.management.monitor.Monitor
    Monitor.ObservedObject createObservedObject(ObjectName objectName) {
        CounterMonitorObservedObject counterMonitorObservedObject = new CounterMonitorObservedObject(objectName);
        counterMonitorObservedObject.setThreshold(this.initThreshold);
        counterMonitorObservedObject.setModulusExceeded(false);
        counterMonitorObservedObject.setEventAlreadyNotified(false);
        counterMonitorObservedObject.setPreviousScanCounter(null);
        return counterMonitorObservedObject;
    }

    @Override // javax.management.monitor.Monitor
    synchronized boolean isComparableTypeValid(ObjectName objectName, String str, Comparable<?> comparable) {
        CounterMonitorObservedObject counterMonitorObservedObject = (CounterMonitorObservedObject) getObservedObject(objectName);
        if (counterMonitorObservedObject == null) {
            return false;
        }
        if (comparable instanceof Integer) {
            counterMonitorObservedObject.setType(Monitor.NumericalType.INTEGER);
            return true;
        }
        if (comparable instanceof Byte) {
            counterMonitorObservedObject.setType(Monitor.NumericalType.BYTE);
            return true;
        }
        if (comparable instanceof Short) {
            counterMonitorObservedObject.setType(Monitor.NumericalType.SHORT);
            return true;
        }
        if (comparable instanceof Long) {
            counterMonitorObservedObject.setType(Monitor.NumericalType.LONG);
            return true;
        }
        return false;
    }

    @Override // javax.management.monitor.Monitor
    synchronized Comparable<?> getDerivedGaugeFromComparable(ObjectName objectName, String str, Comparable<?> comparable) {
        CounterMonitorObservedObject counterMonitorObservedObject = (CounterMonitorObservedObject) getObservedObject(objectName);
        if (counterMonitorObservedObject == null) {
            return null;
        }
        if (counterMonitorObservedObject.getModulusExceeded() && ((Number) counterMonitorObservedObject.getDerivedGauge()).longValue() < counterMonitorObservedObject.getDerivedGaugeExceeded().longValue()) {
            counterMonitorObservedObject.setThreshold(this.initThreshold);
            counterMonitorObservedObject.setModulusExceeded(false);
            counterMonitorObservedObject.setEventAlreadyNotified(false);
        }
        counterMonitorObservedObject.setDerivedGaugeValid(updateDerivedGauge(comparable, counterMonitorObservedObject));
        return (Comparable) counterMonitorObservedObject.getDerivedGauge();
    }

    @Override // javax.management.monitor.Monitor
    synchronized void onErrorNotification(MonitorNotification monitorNotification) {
        CounterMonitorObservedObject counterMonitorObservedObject = (CounterMonitorObservedObject) getObservedObject(monitorNotification.getObservedObject());
        if (counterMonitorObservedObject == null) {
            return;
        }
        counterMonitorObservedObject.setModulusExceeded(false);
        counterMonitorObservedObject.setEventAlreadyNotified(false);
        counterMonitorObservedObject.setPreviousScanCounter(null);
    }

    @Override // javax.management.monitor.Monitor
    synchronized MonitorNotification buildAlarmNotification(ObjectName objectName, String str, Comparable<?> comparable) {
        MonitorNotification monitorNotificationUpdateNotifications;
        CounterMonitorObservedObject counterMonitorObservedObject = (CounterMonitorObservedObject) getObservedObject(objectName);
        if (counterMonitorObservedObject == null) {
            return null;
        }
        if (counterMonitorObservedObject.getDerivedGaugeValid()) {
            monitorNotificationUpdateNotifications = updateNotifications(counterMonitorObservedObject);
            updateThreshold(counterMonitorObservedObject);
        } else {
            monitorNotificationUpdateNotifications = null;
        }
        return monitorNotificationUpdateNotifications;
    }

    @Override // javax.management.monitor.Monitor
    synchronized boolean isThresholdTypeValid(ObjectName objectName, String str, Comparable<?> comparable) {
        CounterMonitorObservedObject counterMonitorObservedObject = (CounterMonitorObservedObject) getObservedObject(objectName);
        if (counterMonitorObservedObject == null) {
            return false;
        }
        Class<? extends Number> clsClassForType = classForType(counterMonitorObservedObject.getType());
        return clsClassForType.isInstance(counterMonitorObservedObject.getThreshold()) && isValidForType(this.offset, clsClassForType) && isValidForType(this.modulus, clsClassForType);
    }
}
