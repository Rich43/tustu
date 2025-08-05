package javax.management.monitor;

import com.sun.jmx.defaults.JmxProperties;
import com.sun.jmx.mbeanserver.GetPropertyAction;
import com.sun.jmx.mbeanserver.Introspector;
import java.io.IOException;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;
import javax.management.ReflectionException;

/* loaded from: rt.jar:javax/management/monitor/Monitor.class */
public abstract class Monitor extends NotificationBroadcasterSupport implements MonitorMBean, MBeanRegistration {
    private String observedAttribute;
    private String firstAttribute;
    private static final int maximumPoolSize;
    private Future<?> monitorFuture;
    private ScheduledFuture<?> schedulerFuture;
    protected static final int capacityIncrement = 16;
    protected MBeanServer server;
    protected static final int RESET_FLAGS_ALREADY_NOTIFIED = 0;
    protected static final int OBSERVED_OBJECT_ERROR_NOTIFIED = 1;
    protected static final int OBSERVED_ATTRIBUTE_ERROR_NOTIFIED = 2;
    protected static final int OBSERVED_ATTRIBUTE_TYPE_ERROR_NOTIFIED = 4;
    protected static final int RUNTIME_ERROR_NOTIFIED = 8;
    static final int THRESHOLD_ERROR_NOTIFIED = 16;
    static final Integer INTEGER_ZERO;
    private static final AccessControlContext noPermissionsACC = new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, null)});
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory("Scheduler"));
    private static final Map<ThreadPoolExecutor, Void> executors = new WeakHashMap();
    private static final Object executorsLock = new Object();
    private long granularityPeriod = 10000;
    private boolean isActive = false;
    private final AtomicLong sequenceNumber = new AtomicLong();
    private boolean isComplexTypeAttribute = false;
    private final List<String> remainingAttributes = new CopyOnWriteArrayList();
    private volatile AccessControlContext acc = noPermissionsACC;
    private final SchedulerTask schedulerTask = new SchedulerTask();
    protected int elementCount = 0;

    @Deprecated
    protected int alreadyNotified = 0;
    protected int[] alreadyNotifieds = new int[16];

    @Deprecated
    protected String dbgTag = Monitor.class.getName();
    final List<ObservedObject> observedObjects = new CopyOnWriteArrayList();

    /* loaded from: rt.jar:javax/management/monitor/Monitor$NumericalType.class */
    enum NumericalType {
        BYTE,
        SHORT,
        INTEGER,
        LONG,
        FLOAT,
        DOUBLE
    }

    public abstract void start();

    public abstract void stop();

    /* loaded from: rt.jar:javax/management/monitor/Monitor$ObservedObject.class */
    static class ObservedObject {
        private final ObjectName observedObject;
        private int alreadyNotified;
        private Object derivedGauge;
        private long derivedGaugeTimeStamp;

        public ObservedObject(ObjectName objectName) {
            this.observedObject = objectName;
        }

        public final ObjectName getObservedObject() {
            return this.observedObject;
        }

        public final synchronized int getAlreadyNotified() {
            return this.alreadyNotified;
        }

        public final synchronized void setAlreadyNotified(int i2) {
            this.alreadyNotified = i2;
        }

        public final synchronized Object getDerivedGauge() {
            return this.derivedGauge;
        }

        public final synchronized void setDerivedGauge(Object obj) {
            this.derivedGauge = obj;
        }

        public final synchronized long getDerivedGaugeTimeStamp() {
            return this.derivedGaugeTimeStamp;
        }

        public final synchronized void setDerivedGaugeTimeStamp(long j2) {
            this.derivedGaugeTimeStamp = j2;
        }
    }

    static {
        int i2;
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("jmx.x.monitor.maximum.pool.size"));
        if (str == null || str.trim().length() == 0) {
            maximumPoolSize = 10;
        } else {
            try {
                i2 = Integer.parseInt(str);
            } catch (NumberFormatException e2) {
                if (JmxProperties.MONITOR_LOGGER.isLoggable(Level.FINER)) {
                    JmxProperties.MONITOR_LOGGER.logp(Level.FINER, Monitor.class.getName(), "<static initializer>", "Wrong value for jmx.x.monitor.maximum.pool.size system property", (Throwable) e2);
                    JmxProperties.MONITOR_LOGGER.logp(Level.FINER, Monitor.class.getName(), "<static initializer>", "jmx.x.monitor.maximum.pool.size defaults to 10");
                }
                i2 = 10;
            }
            if (i2 < 1) {
                maximumPoolSize = 1;
            } else {
                maximumPoolSize = i2;
            }
        }
        INTEGER_ZERO = 0;
    }

    @Override // javax.management.MBeanRegistration
    public ObjectName preRegister(MBeanServer mBeanServer, ObjectName objectName) throws Exception {
        JmxProperties.MONITOR_LOGGER.logp(Level.FINER, Monitor.class.getName(), "preRegister(MBeanServer, ObjectName)", "initialize the reference on the MBean server");
        this.server = mBeanServer;
        return objectName;
    }

    @Override // javax.management.MBeanRegistration
    public void postRegister(Boolean bool) {
    }

    @Override // javax.management.MBeanRegistration
    public void preDeregister() throws Exception {
        JmxProperties.MONITOR_LOGGER.logp(Level.FINER, Monitor.class.getName(), "preDeregister()", "stop the monitor");
        stop();
    }

    @Override // javax.management.MBeanRegistration
    public void postDeregister() {
    }

    @Override // javax.management.monitor.MonitorMBean
    @Deprecated
    public synchronized ObjectName getObservedObject() {
        if (this.observedObjects.isEmpty()) {
            return null;
        }
        return this.observedObjects.get(0).getObservedObject();
    }

    @Override // javax.management.monitor.MonitorMBean
    @Deprecated
    public synchronized void setObservedObject(ObjectName objectName) throws IllegalArgumentException {
        if (objectName == null) {
            throw new IllegalArgumentException("Null observed object");
        }
        if (this.observedObjects.size() == 1 && containsObservedObject(objectName)) {
            return;
        }
        this.observedObjects.clear();
        addObservedObject(objectName);
    }

    @Override // javax.management.monitor.MonitorMBean
    public synchronized void addObservedObject(ObjectName objectName) throws IllegalArgumentException {
        if (objectName == null) {
            throw new IllegalArgumentException("Null observed object");
        }
        if (containsObservedObject(objectName)) {
            return;
        }
        ObservedObject observedObjectCreateObservedObject = createObservedObject(objectName);
        observedObjectCreateObservedObject.setAlreadyNotified(0);
        observedObjectCreateObservedObject.setDerivedGauge(INTEGER_ZERO);
        observedObjectCreateObservedObject.setDerivedGaugeTimeStamp(System.currentTimeMillis());
        this.observedObjects.add(observedObjectCreateObservedObject);
        createAlreadyNotified();
    }

    @Override // javax.management.monitor.MonitorMBean
    public synchronized void removeObservedObject(ObjectName objectName) {
        ObservedObject observedObject;
        if (objectName != null && (observedObject = getObservedObject(objectName)) != null) {
            this.observedObjects.remove(observedObject);
            createAlreadyNotified();
        }
    }

    @Override // javax.management.monitor.MonitorMBean
    public synchronized boolean containsObservedObject(ObjectName objectName) {
        return getObservedObject(objectName) != null;
    }

    @Override // javax.management.monitor.MonitorMBean
    public synchronized ObjectName[] getObservedObjects() {
        ObjectName[] objectNameArr = new ObjectName[this.observedObjects.size()];
        for (int i2 = 0; i2 < objectNameArr.length; i2++) {
            objectNameArr[i2] = this.observedObjects.get(i2).getObservedObject();
        }
        return objectNameArr;
    }

    @Override // javax.management.monitor.MonitorMBean
    public synchronized String getObservedAttribute() {
        return this.observedAttribute;
    }

    @Override // javax.management.monitor.MonitorMBean
    public void setObservedAttribute(String str) throws IllegalArgumentException {
        if (str == null) {
            throw new IllegalArgumentException("Null observed attribute");
        }
        synchronized (this) {
            if (this.observedAttribute == null || !this.observedAttribute.equals(str)) {
                this.observedAttribute = str;
                cleanupIsComplexTypeAttribute();
                int i2 = 0;
                Iterator<ObservedObject> it = this.observedObjects.iterator();
                while (it.hasNext()) {
                    int i3 = i2;
                    i2++;
                    resetAlreadyNotified(it.next(), i3, 6);
                }
            }
        }
    }

    @Override // javax.management.monitor.MonitorMBean
    public synchronized long getGranularityPeriod() {
        return this.granularityPeriod;
    }

    @Override // javax.management.monitor.MonitorMBean
    public synchronized void setGranularityPeriod(long j2) throws IllegalArgumentException {
        if (j2 <= 0) {
            throw new IllegalArgumentException("Nonpositive granularity period");
        }
        if (this.granularityPeriod == j2) {
            return;
        }
        this.granularityPeriod = j2;
        if (isActive()) {
            cleanupFutures();
            this.schedulerFuture = scheduler.schedule(this.schedulerTask, j2, TimeUnit.MILLISECONDS);
        }
    }

    @Override // javax.management.monitor.MonitorMBean
    public synchronized boolean isActive() {
        return this.isActive;
    }

    void doStart() {
        JmxProperties.MONITOR_LOGGER.logp(Level.FINER, Monitor.class.getName(), "doStart()", "start the monitor");
        synchronized (this) {
            if (isActive()) {
                JmxProperties.MONITOR_LOGGER.logp(Level.FINER, Monitor.class.getName(), "doStart()", "the monitor is already active");
                return;
            }
            this.isActive = true;
            cleanupIsComplexTypeAttribute();
            this.acc = AccessController.getContext();
            cleanupFutures();
            this.schedulerTask.setMonitorTask(new MonitorTask());
            this.schedulerFuture = scheduler.schedule(this.schedulerTask, getGranularityPeriod(), TimeUnit.MILLISECONDS);
        }
    }

    void doStop() {
        JmxProperties.MONITOR_LOGGER.logp(Level.FINER, Monitor.class.getName(), "doStop()", "stop the monitor");
        synchronized (this) {
            if (!isActive()) {
                JmxProperties.MONITOR_LOGGER.logp(Level.FINER, Monitor.class.getName(), "doStop()", "the monitor is not active");
                return;
            }
            this.isActive = false;
            cleanupFutures();
            this.acc = noPermissionsACC;
            cleanupIsComplexTypeAttribute();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized Object getDerivedGauge(ObjectName objectName) {
        ObservedObject observedObject = getObservedObject(objectName);
        if (observedObject == null) {
            return null;
        }
        return observedObject.getDerivedGauge();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized long getDerivedGaugeTimeStamp(ObjectName objectName) {
        ObservedObject observedObject = getObservedObject(objectName);
        if (observedObject == null) {
            return 0L;
        }
        return observedObject.getDerivedGaugeTimeStamp();
    }

    Object getAttribute(MBeanServerConnection mBeanServerConnection, ObjectName objectName, String str) throws MBeanException, AttributeNotFoundException, IOException, InstanceNotFoundException, ReflectionException {
        boolean z2;
        MBeanInfo mBeanInfo;
        String str2;
        synchronized (this) {
            if (!isActive()) {
                throw new IllegalArgumentException("The monitor has been stopped");
            }
            if (!str.equals(getObservedAttribute())) {
                throw new IllegalArgumentException("The observed attribute has been changed");
            }
            z2 = this.firstAttribute == null && str.indexOf(46) != -1;
        }
        if (z2) {
            try {
                mBeanInfo = mBeanServerConnection.getMBeanInfo(objectName);
            } catch (IntrospectionException e2) {
                throw new IllegalArgumentException(e2);
            }
        } else {
            mBeanInfo = null;
        }
        synchronized (this) {
            if (!isActive()) {
                throw new IllegalArgumentException("The monitor has been stopped");
            }
            if (!str.equals(getObservedAttribute())) {
                throw new IllegalArgumentException("The observed attribute has been changed");
            }
            if (this.firstAttribute == null) {
                if (str.indexOf(46) != -1) {
                    MBeanAttributeInfo[] attributes = mBeanInfo.getAttributes();
                    int length = attributes.length;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= length) {
                            break;
                        }
                        if (!str.equals(attributes[i2].getName())) {
                            i2++;
                        } else {
                            this.firstAttribute = str;
                            break;
                        }
                    }
                    if (this.firstAttribute == null) {
                        String[] strArrSplit = str.split("\\.", -1);
                        this.firstAttribute = strArrSplit[0];
                        for (int i3 = 1; i3 < strArrSplit.length; i3++) {
                            this.remainingAttributes.add(strArrSplit[i3]);
                        }
                        this.isComplexTypeAttribute = true;
                    }
                } else {
                    this.firstAttribute = str;
                }
            }
            str2 = this.firstAttribute;
        }
        return mBeanServerConnection.getAttribute(objectName, str2);
    }

    Comparable<?> getComparableFromAttribute(ObjectName objectName, String str, Object obj) throws AttributeNotFoundException {
        if (this.isComplexTypeAttribute) {
            Object objElementFromComplex = obj;
            Iterator<String> it = this.remainingAttributes.iterator();
            while (it.hasNext()) {
                objElementFromComplex = Introspector.elementFromComplex(objElementFromComplex, it.next());
            }
            return (Comparable) objElementFromComplex;
        }
        return (Comparable) obj;
    }

    boolean isComparableTypeValid(ObjectName objectName, String str, Comparable<?> comparable) {
        return true;
    }

    String buildErrorNotification(ObjectName objectName, String str, Comparable<?> comparable) {
        return null;
    }

    void onErrorNotification(MonitorNotification monitorNotification) {
    }

    Comparable<?> getDerivedGaugeFromComparable(ObjectName objectName, String str, Comparable<?> comparable) {
        return comparable;
    }

    MonitorNotification buildAlarmNotification(ObjectName objectName, String str, Comparable<?> comparable) {
        return null;
    }

    boolean isThresholdTypeValid(ObjectName objectName, String str, Comparable<?> comparable) {
        return true;
    }

    static Class<? extends Number> classForType(NumericalType numericalType) {
        switch (numericalType) {
            case BYTE:
                return Byte.class;
            case SHORT:
                return Short.class;
            case INTEGER:
                return Integer.class;
            case LONG:
                return Long.class;
            case FLOAT:
                return Float.class;
            case DOUBLE:
                return Double.class;
            default:
                throw new IllegalArgumentException("Unsupported numerical type");
        }
    }

    static boolean isValidForType(Object obj, Class<? extends Number> cls) {
        return obj == INTEGER_ZERO || cls.isInstance(obj);
    }

    synchronized ObservedObject getObservedObject(ObjectName objectName) {
        for (ObservedObject observedObject : this.observedObjects) {
            if (observedObject.getObservedObject().equals(objectName)) {
                return observedObject;
            }
        }
        return null;
    }

    ObservedObject createObservedObject(ObjectName objectName) {
        return new ObservedObject(objectName);
    }

    synchronized void createAlreadyNotified() {
        this.elementCount = this.observedObjects.size();
        this.alreadyNotifieds = new int[this.elementCount];
        for (int i2 = 0; i2 < this.elementCount; i2++) {
            this.alreadyNotifieds[i2] = this.observedObjects.get(i2).getAlreadyNotified();
        }
        updateDeprecatedAlreadyNotified();
    }

    synchronized void updateDeprecatedAlreadyNotified() {
        if (this.elementCount > 0) {
            this.alreadyNotified = this.alreadyNotifieds[0];
        } else {
            this.alreadyNotified = 0;
        }
    }

    synchronized void updateAlreadyNotified(ObservedObject observedObject, int i2) {
        this.alreadyNotifieds[i2] = observedObject.getAlreadyNotified();
        if (i2 == 0) {
            updateDeprecatedAlreadyNotified();
        }
    }

    synchronized boolean isAlreadyNotified(ObservedObject observedObject, int i2) {
        return (observedObject.getAlreadyNotified() & i2) != 0;
    }

    synchronized void setAlreadyNotified(ObservedObject observedObject, int i2, int i3, int[] iArr) {
        int iComputeAlreadyNotifiedIndex = computeAlreadyNotifiedIndex(observedObject, i2, iArr);
        if (iComputeAlreadyNotifiedIndex == -1) {
            return;
        }
        observedObject.setAlreadyNotified(observedObject.getAlreadyNotified() | i3);
        updateAlreadyNotified(observedObject, iComputeAlreadyNotifiedIndex);
    }

    synchronized void resetAlreadyNotified(ObservedObject observedObject, int i2, int i3) {
        observedObject.setAlreadyNotified(observedObject.getAlreadyNotified() & (i3 ^ (-1)));
        updateAlreadyNotified(observedObject, i2);
    }

    synchronized void resetAllAlreadyNotified(ObservedObject observedObject, int i2, int[] iArr) {
        if (computeAlreadyNotifiedIndex(observedObject, i2, iArr) == -1) {
            return;
        }
        observedObject.setAlreadyNotified(0);
        updateAlreadyNotified(observedObject, i2);
    }

    synchronized int computeAlreadyNotifiedIndex(ObservedObject observedObject, int i2, int[] iArr) {
        if (iArr == this.alreadyNotifieds) {
            return i2;
        }
        return this.observedObjects.indexOf(observedObject);
    }

    private void sendNotification(String str, long j2, String str2, Object obj, Object obj2, ObjectName objectName, boolean z2) {
        if (!isActive()) {
            return;
        }
        if (JmxProperties.MONITOR_LOGGER.isLoggable(Level.FINER)) {
            JmxProperties.MONITOR_LOGGER.logp(Level.FINER, Monitor.class.getName(), "sendNotification", "send notification: \n\tNotification observed object = " + ((Object) objectName) + "\n\tNotification observed attribute = " + this.observedAttribute + "\n\tNotification derived gauge = " + obj);
        }
        MonitorNotification monitorNotification = new MonitorNotification(str, this, this.sequenceNumber.getAndIncrement(), j2, str2, objectName, this.observedAttribute, obj, obj2);
        if (z2) {
            onErrorNotification(monitorNotification);
        }
        sendNotification(monitorNotification);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void monitor(ObservedObject observedObject, int i2, int[] iArr) {
        String str = null;
        String message = null;
        Comparable<?> derivedGaugeFromComparable = null;
        Comparable<?> comparableFromAttribute = null;
        MonitorNotification monitorNotificationBuildAlarmNotification = null;
        if (isActive()) {
            synchronized (this) {
                ObjectName observedObject2 = observedObject.getObservedObject();
                String observedAttribute = getObservedAttribute();
                if (observedObject2 == null || observedAttribute == null) {
                    return;
                }
                Object attribute = null;
                try {
                    attribute = getAttribute(this.server, observedObject2, observedAttribute);
                    if (attribute == null) {
                        if (isAlreadyNotified(observedObject, 4)) {
                            return;
                        }
                        str = MonitorNotification.OBSERVED_ATTRIBUTE_TYPE_ERROR;
                        setAlreadyNotified(observedObject, i2, 4, iArr);
                        message = "The observed attribute value is null.";
                        JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", message);
                    }
                } catch (IOException e2) {
                    if (isAlreadyNotified(observedObject, 8)) {
                        return;
                    }
                    str = MonitorNotification.RUNTIME_ERROR;
                    setAlreadyNotified(observedObject, i2, 8, iArr);
                    message = e2.getMessage() == null ? "" : e2.getMessage();
                    JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", message);
                    JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", e2.toString());
                } catch (NullPointerException e3) {
                    if (isAlreadyNotified(observedObject, 8)) {
                        return;
                    }
                    str = MonitorNotification.RUNTIME_ERROR;
                    setAlreadyNotified(observedObject, i2, 8, iArr);
                    message = "The monitor must be registered in the MBean server or an MBeanServerConnection must be explicitly supplied.";
                    JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", message);
                    JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", e3.toString());
                } catch (RuntimeException e4) {
                    if (isAlreadyNotified(observedObject, 8)) {
                        return;
                    }
                    str = MonitorNotification.RUNTIME_ERROR;
                    setAlreadyNotified(observedObject, i2, 8, iArr);
                    message = e4.getMessage() == null ? "" : e4.getMessage();
                    JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", message);
                    JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", e4.toString());
                } catch (AttributeNotFoundException e5) {
                    if (isAlreadyNotified(observedObject, 2)) {
                        return;
                    }
                    str = MonitorNotification.OBSERVED_ATTRIBUTE_ERROR;
                    setAlreadyNotified(observedObject, i2, 2, iArr);
                    message = "The observed attribute must be accessible in the observed object.";
                    JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", message);
                    JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", e5.toString());
                } catch (InstanceNotFoundException e6) {
                    if (isAlreadyNotified(observedObject, 1)) {
                        return;
                    }
                    str = MonitorNotification.OBSERVED_OBJECT_ERROR;
                    setAlreadyNotified(observedObject, i2, 1, iArr);
                    message = "The observed object must be accessible in the MBeanServerConnection.";
                    JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", message);
                    JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", e6.toString());
                } catch (MBeanException e7) {
                    if (isAlreadyNotified(observedObject, 8)) {
                        return;
                    }
                    str = MonitorNotification.RUNTIME_ERROR;
                    setAlreadyNotified(observedObject, i2, 8, iArr);
                    message = e7.getMessage() == null ? "" : e7.getMessage();
                    JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", message);
                    JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", e7.toString());
                } catch (ReflectionException e8) {
                    if (isAlreadyNotified(observedObject, 8)) {
                        return;
                    }
                    str = MonitorNotification.RUNTIME_ERROR;
                    setAlreadyNotified(observedObject, i2, 8, iArr);
                    message = e8.getMessage() == null ? "" : e8.getMessage();
                    JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", message);
                    JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", e8.toString());
                }
                synchronized (this) {
                    if (isActive()) {
                        if (observedAttribute.equals(getObservedAttribute())) {
                            if (message == null) {
                                try {
                                    try {
                                        comparableFromAttribute = getComparableFromAttribute(observedObject2, observedAttribute, attribute);
                                    } catch (RuntimeException e9) {
                                        if (isAlreadyNotified(observedObject, 8)) {
                                            return;
                                        }
                                        str = MonitorNotification.RUNTIME_ERROR;
                                        setAlreadyNotified(observedObject, i2, 8, iArr);
                                        message = e9.getMessage() == null ? "" : e9.getMessage();
                                        JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", message);
                                        JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", e9.toString());
                                    } catch (AttributeNotFoundException e10) {
                                        if (isAlreadyNotified(observedObject, 2)) {
                                            return;
                                        }
                                        str = MonitorNotification.OBSERVED_ATTRIBUTE_ERROR;
                                        setAlreadyNotified(observedObject, i2, 2, iArr);
                                        message = "The observed attribute must be accessible in the observed object.";
                                        JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", message);
                                        JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", e10.toString());
                                    }
                                } catch (ClassCastException e11) {
                                    if (isAlreadyNotified(observedObject, 4)) {
                                        return;
                                    }
                                    str = MonitorNotification.OBSERVED_ATTRIBUTE_TYPE_ERROR;
                                    setAlreadyNotified(observedObject, i2, 4, iArr);
                                    message = "The observed attribute value does not implement the Comparable interface.";
                                    JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", message);
                                    JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", e11.toString());
                                }
                            }
                            if (message == null && !isComparableTypeValid(observedObject2, observedAttribute, comparableFromAttribute)) {
                                if (isAlreadyNotified(observedObject, 4)) {
                                    return;
                                }
                                str = MonitorNotification.OBSERVED_ATTRIBUTE_TYPE_ERROR;
                                setAlreadyNotified(observedObject, i2, 4, iArr);
                                message = "The observed attribute type is not valid.";
                                JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", message);
                            }
                            if (message == null && !isThresholdTypeValid(observedObject2, observedAttribute, comparableFromAttribute)) {
                                if (isAlreadyNotified(observedObject, 16)) {
                                    return;
                                }
                                str = MonitorNotification.THRESHOLD_ERROR;
                                setAlreadyNotified(observedObject, i2, 16, iArr);
                                message = "The threshold type is not valid.";
                                JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", message);
                            }
                            if (message == null) {
                                message = buildErrorNotification(observedObject2, observedAttribute, comparableFromAttribute);
                                if (message != null) {
                                    if (isAlreadyNotified(observedObject, 8)) {
                                        return;
                                    }
                                    str = MonitorNotification.RUNTIME_ERROR;
                                    setAlreadyNotified(observedObject, i2, 8, iArr);
                                    JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", message);
                                }
                            }
                            if (message == null) {
                                resetAllAlreadyNotified(observedObject, i2, iArr);
                                derivedGaugeFromComparable = getDerivedGaugeFromComparable(observedObject2, observedAttribute, comparableFromAttribute);
                                observedObject.setDerivedGauge(derivedGaugeFromComparable);
                                observedObject.setDerivedGaugeTimeStamp(System.currentTimeMillis());
                                monitorNotificationBuildAlarmNotification = buildAlarmNotification(observedObject2, observedAttribute, derivedGaugeFromComparable);
                            }
                            if (message != null) {
                                sendNotification(str, System.currentTimeMillis(), message, derivedGaugeFromComparable, null, observedObject2, true);
                            }
                            if (monitorNotificationBuildAlarmNotification == null || monitorNotificationBuildAlarmNotification.getType() == null) {
                                return;
                            }
                            sendNotification(monitorNotificationBuildAlarmNotification.getType(), System.currentTimeMillis(), monitorNotificationBuildAlarmNotification.getMessage(), derivedGaugeFromComparable, monitorNotificationBuildAlarmNotification.getTrigger(), observedObject2, false);
                        }
                    }
                }
            }
        }
    }

    private synchronized void cleanupFutures() {
        if (this.schedulerFuture != null) {
            this.schedulerFuture.cancel(false);
            this.schedulerFuture = null;
        }
        if (this.monitorFuture != null) {
            this.monitorFuture.cancel(false);
            this.monitorFuture = null;
        }
    }

    private synchronized void cleanupIsComplexTypeAttribute() {
        this.firstAttribute = null;
        this.remainingAttributes.clear();
        this.isComplexTypeAttribute = false;
    }

    /* loaded from: rt.jar:javax/management/monitor/Monitor$SchedulerTask.class */
    private class SchedulerTask implements Runnable {
        private MonitorTask task;

        public SchedulerTask() {
        }

        public void setMonitorTask(MonitorTask monitorTask) {
            this.task = monitorTask;
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (Monitor.this) {
                Monitor.this.monitorFuture = this.task.submit();
            }
        }
    }

    /* loaded from: rt.jar:javax/management/monitor/Monitor$MonitorTask.class */
    private class MonitorTask implements Runnable {
        private ThreadPoolExecutor executor;

        public MonitorTask() {
            SecurityManager securityManager = System.getSecurityManager();
            ThreadGroup threadGroup = securityManager != null ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
            synchronized (Monitor.executorsLock) {
                Iterator it = Monitor.executors.keySet().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) it.next();
                    if (((DaemonThreadFactory) threadPoolExecutor.getThreadFactory()).getThreadGroup() == threadGroup) {
                        this.executor = threadPoolExecutor;
                        break;
                    }
                }
                if (this.executor == null) {
                    this.executor = new ThreadPoolExecutor(Monitor.maximumPoolSize, Monitor.maximumPoolSize, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue(), new DaemonThreadFactory("ThreadGroup<" + threadGroup.getName() + "> Executor", threadGroup));
                    this.executor.allowCoreThreadTimeOut(true);
                    Monitor.executors.put(this.executor, null);
                }
            }
        }

        public Future<?> submit() {
            return this.executor.submit(this);
        }

        @Override // java.lang.Runnable
        public void run() {
            ScheduledFuture scheduledFuture;
            AccessControlContext accessControlContext;
            synchronized (Monitor.this) {
                scheduledFuture = Monitor.this.schedulerFuture;
                accessControlContext = Monitor.this.acc;
            }
            PrivilegedAction<Void> privilegedAction = new PrivilegedAction<Void>() { // from class: javax.management.monitor.Monitor.MonitorTask.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    if (Monitor.this.isActive()) {
                        int[] iArr = Monitor.this.alreadyNotifieds;
                        int i2 = 0;
                        for (ObservedObject observedObject : Monitor.this.observedObjects) {
                            if (Monitor.this.isActive()) {
                                int i3 = i2;
                                i2++;
                                Monitor.this.monitor(observedObject, i3, iArr);
                            }
                        }
                        return null;
                    }
                    return null;
                }
            };
            if (accessControlContext == null) {
                throw new SecurityException("AccessControlContext cannot be null");
            }
            AccessController.doPrivileged(privilegedAction, accessControlContext);
            synchronized (Monitor.this) {
                if (Monitor.this.isActive() && Monitor.this.schedulerFuture == scheduledFuture) {
                    Monitor.this.monitorFuture = null;
                    Monitor.this.schedulerFuture = Monitor.scheduler.schedule(Monitor.this.schedulerTask, Monitor.this.getGranularityPeriod(), TimeUnit.MILLISECONDS);
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/management/monitor/Monitor$DaemonThreadFactory.class */
    private static class DaemonThreadFactory implements ThreadFactory {
        final ThreadGroup group;
        final AtomicInteger threadNumber = new AtomicInteger(1);
        final String namePrefix;
        static final String nameSuffix = "]";

        public DaemonThreadFactory(String str) {
            SecurityManager securityManager = System.getSecurityManager();
            this.group = securityManager != null ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.namePrefix = "JMX Monitor " + str + " Pool [Thread-";
        }

        public DaemonThreadFactory(String str, ThreadGroup threadGroup) {
            this.group = threadGroup;
            this.namePrefix = "JMX Monitor " + str + " Pool [Thread-";
        }

        public ThreadGroup getThreadGroup() {
            return this.group;
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(this.group, runnable, this.namePrefix + this.threadNumber.getAndIncrement() + nameSuffix, 0L);
            thread.setDaemon(true);
            if (thread.getPriority() != 5) {
                thread.setPriority(5);
            }
            return thread;
        }
    }
}
