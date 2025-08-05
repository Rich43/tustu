package javax.management.monitor;

import com.sun.jmx.defaults.JmxProperties;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.util.Iterator;
import java.util.logging.Level;
import javax.management.MBeanNotificationInfo;
import javax.management.ObjectName;
import javax.management.monitor.Monitor;

/* loaded from: rt.jar:javax/management/monitor/GaugeMonitor.class */
public class GaugeMonitor extends Monitor implements GaugeMonitorMBean {
    private Number highThreshold = INTEGER_ZERO;
    private Number lowThreshold = INTEGER_ZERO;
    private boolean notifyHigh = false;
    private boolean notifyLow = false;
    private boolean differenceMode = false;
    private static final String[] types = {MonitorNotification.RUNTIME_ERROR, MonitorNotification.OBSERVED_OBJECT_ERROR, MonitorNotification.OBSERVED_ATTRIBUTE_ERROR, MonitorNotification.OBSERVED_ATTRIBUTE_TYPE_ERROR, MonitorNotification.THRESHOLD_ERROR, MonitorNotification.THRESHOLD_HIGH_VALUE_EXCEEDED, MonitorNotification.THRESHOLD_LOW_VALUE_EXCEEDED};
    private static final MBeanNotificationInfo[] notifsInfo = {new MBeanNotificationInfo(types, "javax.management.monitor.MonitorNotification", "Notifications sent by the GaugeMonitor MBean")};
    private static final int RISING = 0;
    private static final int FALLING = 1;
    private static final int RISING_OR_FALLING = 2;

    /* loaded from: rt.jar:javax/management/monitor/GaugeMonitor$GaugeMonitorObservedObject.class */
    static class GaugeMonitorObservedObject extends Monitor.ObservedObject {
        private boolean derivedGaugeValid;
        private Monitor.NumericalType type;
        private Number previousScanGauge;
        private int status;

        public GaugeMonitorObservedObject(ObjectName objectName) {
            super(objectName);
        }

        public final synchronized boolean getDerivedGaugeValid() {
            return this.derivedGaugeValid;
        }

        public final synchronized void setDerivedGaugeValid(boolean z2) {
            this.derivedGaugeValid = z2;
        }

        public final synchronized Monitor.NumericalType getType() {
            return this.type;
        }

        public final synchronized void setType(Monitor.NumericalType numericalType) {
            this.type = numericalType;
        }

        public final synchronized Number getPreviousScanGauge() {
            return this.previousScanGauge;
        }

        public final synchronized void setPreviousScanGauge(Number number) {
            this.previousScanGauge = number;
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
            JmxProperties.MONITOR_LOGGER.logp(Level.FINER, GaugeMonitor.class.getName(), "start", "the monitor is already active");
            return;
        }
        Iterator<Monitor.ObservedObject> it = this.observedObjects.iterator();
        while (it.hasNext()) {
            GaugeMonitorObservedObject gaugeMonitorObservedObject = (GaugeMonitorObservedObject) it.next();
            gaugeMonitorObservedObject.setStatus(2);
            gaugeMonitorObservedObject.setPreviousScanGauge(null);
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

    @Override // javax.management.monitor.GaugeMonitorMBean
    @Deprecated
    public synchronized Number getDerivedGauge() {
        if (this.observedObjects.isEmpty()) {
            return null;
        }
        return (Number) this.observedObjects.get(0).getDerivedGauge();
    }

    @Override // javax.management.monitor.GaugeMonitorMBean
    @Deprecated
    public synchronized long getDerivedGaugeTimeStamp() {
        if (this.observedObjects.isEmpty()) {
            return 0L;
        }
        return this.observedObjects.get(0).getDerivedGaugeTimeStamp();
    }

    @Override // javax.management.monitor.GaugeMonitorMBean
    public synchronized Number getHighThreshold() {
        return this.highThreshold;
    }

    @Override // javax.management.monitor.GaugeMonitorMBean
    public synchronized Number getLowThreshold() {
        return this.lowThreshold;
    }

    @Override // javax.management.monitor.GaugeMonitorMBean
    public synchronized void setThresholds(Number number, Number number2) throws IllegalArgumentException {
        if (number == null || number2 == null) {
            throw new IllegalArgumentException("Null threshold value");
        }
        if (number.getClass() != number2.getClass()) {
            throw new IllegalArgumentException("Different type threshold values");
        }
        if (isFirstStrictlyGreaterThanLast(number2, number, number.getClass().getName())) {
            throw new IllegalArgumentException("High threshold less than low threshold");
        }
        if (this.highThreshold.equals(number) && this.lowThreshold.equals(number2)) {
            return;
        }
        this.highThreshold = number;
        this.lowThreshold = number2;
        int i2 = 0;
        for (Monitor.ObservedObject observedObject : this.observedObjects) {
            int i3 = i2;
            i2++;
            resetAlreadyNotified(observedObject, i3, 16);
            ((GaugeMonitorObservedObject) observedObject).setStatus(2);
        }
    }

    @Override // javax.management.monitor.GaugeMonitorMBean
    public synchronized boolean getNotifyHigh() {
        return this.notifyHigh;
    }

    @Override // javax.management.monitor.GaugeMonitorMBean
    public synchronized void setNotifyHigh(boolean z2) {
        if (this.notifyHigh == z2) {
            return;
        }
        this.notifyHigh = z2;
    }

    @Override // javax.management.monitor.GaugeMonitorMBean
    public synchronized boolean getNotifyLow() {
        return this.notifyLow;
    }

    @Override // javax.management.monitor.GaugeMonitorMBean
    public synchronized void setNotifyLow(boolean z2) {
        if (this.notifyLow == z2) {
            return;
        }
        this.notifyLow = z2;
    }

    @Override // javax.management.monitor.GaugeMonitorMBean
    public synchronized boolean getDifferenceMode() {
        return this.differenceMode;
    }

    @Override // javax.management.monitor.GaugeMonitorMBean
    public synchronized void setDifferenceMode(boolean z2) {
        if (this.differenceMode == z2) {
            return;
        }
        this.differenceMode = z2;
        Iterator<Monitor.ObservedObject> it = this.observedObjects.iterator();
        while (it.hasNext()) {
            GaugeMonitorObservedObject gaugeMonitorObservedObject = (GaugeMonitorObservedObject) it.next();
            gaugeMonitorObservedObject.setStatus(2);
            gaugeMonitorObservedObject.setPreviousScanGauge(null);
        }
    }

    @Override // javax.management.NotificationBroadcasterSupport, javax.management.NotificationBroadcaster
    public MBeanNotificationInfo[] getNotificationInfo() {
        return (MBeanNotificationInfo[]) notifsInfo.clone();
    }

    private synchronized boolean updateDerivedGauge(Object obj, GaugeMonitorObservedObject gaugeMonitorObservedObject) {
        boolean z2;
        if (this.differenceMode) {
            if (gaugeMonitorObservedObject.getPreviousScanGauge() != null) {
                setDerivedGaugeWithDifference((Number) obj, gaugeMonitorObservedObject);
                z2 = true;
            } else {
                z2 = false;
            }
            gaugeMonitorObservedObject.setPreviousScanGauge((Number) obj);
        } else {
            gaugeMonitorObservedObject.setDerivedGauge((Number) obj);
            z2 = true;
        }
        return z2;
    }

    private synchronized MonitorNotification updateNotifications(GaugeMonitorObservedObject gaugeMonitorObservedObject) {
        MonitorNotification monitorNotification = null;
        if (gaugeMonitorObservedObject.getStatus() == 2) {
            if (isFirstGreaterThanLast((Number) gaugeMonitorObservedObject.getDerivedGauge(), this.highThreshold, gaugeMonitorObservedObject.getType())) {
                if (this.notifyHigh) {
                    monitorNotification = new MonitorNotification(MonitorNotification.THRESHOLD_HIGH_VALUE_EXCEEDED, this, 0L, 0L, "", null, null, null, this.highThreshold);
                }
                gaugeMonitorObservedObject.setStatus(1);
            } else if (isFirstGreaterThanLast(this.lowThreshold, (Number) gaugeMonitorObservedObject.getDerivedGauge(), gaugeMonitorObservedObject.getType())) {
                if (this.notifyLow) {
                    monitorNotification = new MonitorNotification(MonitorNotification.THRESHOLD_LOW_VALUE_EXCEEDED, this, 0L, 0L, "", null, null, null, this.lowThreshold);
                }
                gaugeMonitorObservedObject.setStatus(0);
            }
        } else if (gaugeMonitorObservedObject.getStatus() == 0) {
            if (isFirstGreaterThanLast((Number) gaugeMonitorObservedObject.getDerivedGauge(), this.highThreshold, gaugeMonitorObservedObject.getType())) {
                if (this.notifyHigh) {
                    monitorNotification = new MonitorNotification(MonitorNotification.THRESHOLD_HIGH_VALUE_EXCEEDED, this, 0L, 0L, "", null, null, null, this.highThreshold);
                }
                gaugeMonitorObservedObject.setStatus(1);
            }
        } else if (gaugeMonitorObservedObject.getStatus() == 1 && isFirstGreaterThanLast(this.lowThreshold, (Number) gaugeMonitorObservedObject.getDerivedGauge(), gaugeMonitorObservedObject.getType())) {
            if (this.notifyLow) {
                monitorNotification = new MonitorNotification(MonitorNotification.THRESHOLD_LOW_VALUE_EXCEEDED, this, 0L, 0L, "", null, null, null, this.lowThreshold);
            }
            gaugeMonitorObservedObject.setStatus(0);
        }
        return monitorNotification;
    }

    private synchronized void setDerivedGaugeWithDifference(Number number, GaugeMonitorObservedObject gaugeMonitorObservedObject) {
        Object objValueOf;
        Number previousScanGauge = gaugeMonitorObservedObject.getPreviousScanGauge();
        switch (gaugeMonitorObservedObject.getType()) {
            case INTEGER:
                objValueOf = Integer.valueOf(((Integer) number).intValue() - ((Integer) previousScanGauge).intValue());
                break;
            case BYTE:
                objValueOf = Byte.valueOf((byte) (((Byte) number).byteValue() - ((Byte) previousScanGauge).byteValue()));
                break;
            case SHORT:
                objValueOf = Short.valueOf((short) (((Short) number).shortValue() - ((Short) previousScanGauge).shortValue()));
                break;
            case LONG:
                objValueOf = Long.valueOf(((Long) number).longValue() - ((Long) previousScanGauge).longValue());
                break;
            case FLOAT:
                objValueOf = Float.valueOf(((Float) number).floatValue() - ((Float) previousScanGauge).floatValue());
                break;
            case DOUBLE:
                objValueOf = Double.valueOf(((Double) number).doubleValue() - ((Double) previousScanGauge).doubleValue());
                break;
            default:
                JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, GaugeMonitor.class.getName(), "setDerivedGaugeWithDifference", "the threshold type is invalid");
                return;
        }
        gaugeMonitorObservedObject.setDerivedGauge(objValueOf);
    }

    private boolean isFirstGreaterThanLast(Number number, Number number2, Monitor.NumericalType numericalType) {
        switch (numericalType) {
            case INTEGER:
            case BYTE:
            case SHORT:
            case LONG:
                if (number.longValue() >= number2.longValue()) {
                }
                break;
            case FLOAT:
            case DOUBLE:
                if (number.doubleValue() >= number2.doubleValue()) {
                }
                break;
            default:
                JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, GaugeMonitor.class.getName(), "isFirstGreaterThanLast", "the threshold type is invalid");
                break;
        }
        return false;
    }

    private boolean isFirstStrictlyGreaterThanLast(Number number, Number number2, String str) {
        if (str.equals(Constants.INTEGER_CLASS) || str.equals("java.lang.Byte") || str.equals("java.lang.Short") || str.equals("java.lang.Long")) {
            return number.longValue() > number2.longValue();
        }
        if (str.equals("java.lang.Float") || str.equals(Constants.DOUBLE_CLASS)) {
            return number.doubleValue() > number2.doubleValue();
        }
        JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, GaugeMonitor.class.getName(), "isFirstStrictlyGreaterThanLast", "the threshold type is invalid");
        return false;
    }

    @Override // javax.management.monitor.Monitor
    Monitor.ObservedObject createObservedObject(ObjectName objectName) {
        GaugeMonitorObservedObject gaugeMonitorObservedObject = new GaugeMonitorObservedObject(objectName);
        gaugeMonitorObservedObject.setStatus(2);
        gaugeMonitorObservedObject.setPreviousScanGauge(null);
        return gaugeMonitorObservedObject;
    }

    @Override // javax.management.monitor.Monitor
    synchronized boolean isComparableTypeValid(ObjectName objectName, String str, Comparable<?> comparable) {
        GaugeMonitorObservedObject gaugeMonitorObservedObject = (GaugeMonitorObservedObject) getObservedObject(objectName);
        if (gaugeMonitorObservedObject == null) {
            return false;
        }
        if (comparable instanceof Integer) {
            gaugeMonitorObservedObject.setType(Monitor.NumericalType.INTEGER);
            return true;
        }
        if (comparable instanceof Byte) {
            gaugeMonitorObservedObject.setType(Monitor.NumericalType.BYTE);
            return true;
        }
        if (comparable instanceof Short) {
            gaugeMonitorObservedObject.setType(Monitor.NumericalType.SHORT);
            return true;
        }
        if (comparable instanceof Long) {
            gaugeMonitorObservedObject.setType(Monitor.NumericalType.LONG);
            return true;
        }
        if (comparable instanceof Float) {
            gaugeMonitorObservedObject.setType(Monitor.NumericalType.FLOAT);
            return true;
        }
        if (comparable instanceof Double) {
            gaugeMonitorObservedObject.setType(Monitor.NumericalType.DOUBLE);
            return true;
        }
        return false;
    }

    @Override // javax.management.monitor.Monitor
    synchronized Comparable<?> getDerivedGaugeFromComparable(ObjectName objectName, String str, Comparable<?> comparable) {
        GaugeMonitorObservedObject gaugeMonitorObservedObject = (GaugeMonitorObservedObject) getObservedObject(objectName);
        if (gaugeMonitorObservedObject == null) {
            return null;
        }
        gaugeMonitorObservedObject.setDerivedGaugeValid(updateDerivedGauge(comparable, gaugeMonitorObservedObject));
        return (Comparable) gaugeMonitorObservedObject.getDerivedGauge();
    }

    @Override // javax.management.monitor.Monitor
    synchronized void onErrorNotification(MonitorNotification monitorNotification) {
        GaugeMonitorObservedObject gaugeMonitorObservedObject = (GaugeMonitorObservedObject) getObservedObject(monitorNotification.getObservedObject());
        if (gaugeMonitorObservedObject == null) {
            return;
        }
        gaugeMonitorObservedObject.setStatus(2);
        gaugeMonitorObservedObject.setPreviousScanGauge(null);
    }

    @Override // javax.management.monitor.Monitor
    synchronized MonitorNotification buildAlarmNotification(ObjectName objectName, String str, Comparable<?> comparable) {
        MonitorNotification monitorNotificationUpdateNotifications;
        GaugeMonitorObservedObject gaugeMonitorObservedObject = (GaugeMonitorObservedObject) getObservedObject(objectName);
        if (gaugeMonitorObservedObject == null) {
            return null;
        }
        if (gaugeMonitorObservedObject.getDerivedGaugeValid()) {
            monitorNotificationUpdateNotifications = updateNotifications(gaugeMonitorObservedObject);
        } else {
            monitorNotificationUpdateNotifications = null;
        }
        return monitorNotificationUpdateNotifications;
    }

    @Override // javax.management.monitor.Monitor
    synchronized boolean isThresholdTypeValid(ObjectName objectName, String str, Comparable<?> comparable) {
        GaugeMonitorObservedObject gaugeMonitorObservedObject = (GaugeMonitorObservedObject) getObservedObject(objectName);
        if (gaugeMonitorObservedObject == null) {
            return false;
        }
        Class<? extends Number> clsClassForType = classForType(gaugeMonitorObservedObject.getType());
        return isValidForType(this.highThreshold, clsClassForType) && isValidForType(this.lowThreshold, clsClassForType);
    }
}
