package jdk.management.jfr;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.file.Paths;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.management.AttributeChangeNotification;
import javax.management.AttributeNotFoundException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationEmitter;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.StandardEmitterMBean;
import jdk.jfr.Configuration;
import jdk.jfr.EventType;
import jdk.jfr.FlightRecorder;
import jdk.jfr.FlightRecorderListener;
import jdk.jfr.FlightRecorderPermission;
import jdk.jfr.Recording;
import jdk.jfr.RecordingState;
import jdk.jfr.internal.Utils;
import jdk.jfr.internal.management.ManagementSupport;

/* loaded from: jfr.jar:jdk/management/jfr/FlightRecorderMXBeanImpl.class */
final class FlightRecorderMXBeanImpl extends StandardEmitterMBean implements FlightRecorderMXBean, NotificationEmitter {
    private static final String ATTRIBUTE_RECORDINGS = "Recordings";
    private static final String OPTION_NAME = "name";
    private static final String OPTION_DURATION = "duration";
    private final StreamManager streamHandler;
    private final Map<Long, Object> changes;
    private final AtomicLong sequenceNumber;
    private final List<MXBeanListener> listeners;
    private FlightRecorder recorder;
    private static final String OPTION_DUMP_ON_EXIT = "dumpOnExit";
    private static final String OPTION_MAX_AGE = "maxAge";
    private static final String OPTION_MAX_SIZE = "maxSize";
    private static final String OPTION_DISK = "disk";
    private static final List<String> OPTIONS = Arrays.asList(OPTION_DUMP_ON_EXIT, "duration", "name", OPTION_MAX_AGE, OPTION_MAX_SIZE, OPTION_DISK);

    /* loaded from: jfr.jar:jdk/management/jfr/FlightRecorderMXBeanImpl$MXBeanListener.class */
    final class MXBeanListener implements FlightRecorderListener {
        private final NotificationListener listener;
        private final NotificationFilter filter;
        private final Object handback;
        private final AccessControlContext context = AccessController.getContext();

        public MXBeanListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) {
            this.listener = notificationListener;
            this.filter = notificationFilter;
            this.handback = obj;
        }

        @Override // jdk.jfr.FlightRecorderListener
        public void recordingStateChanged(final Recording recording) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: jdk.management.jfr.FlightRecorderMXBeanImpl.MXBeanListener.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    FlightRecorderMXBeanImpl.this.sendNotification(FlightRecorderMXBeanImpl.this.createNotication(recording));
                    return null;
                }
            }, this.context);
        }
    }

    FlightRecorderMXBeanImpl() {
        super((Class<?>) FlightRecorderMXBean.class, true, (NotificationEmitter) new NotificationBroadcasterSupport(createNotificationInfo()));
        this.streamHandler = new StreamManager();
        this.changes = new ConcurrentHashMap();
        this.sequenceNumber = new AtomicLong();
        this.listeners = new CopyOnWriteArrayList();
    }

    @Override // jdk.management.jfr.FlightRecorderMXBean
    public void startRecording(long j2) {
        MBeanUtils.checkControl();
        getExistingRecording(j2).start();
    }

    @Override // jdk.management.jfr.FlightRecorderMXBean
    public boolean stopRecording(long j2) {
        MBeanUtils.checkControl();
        return getExistingRecording(j2).stop();
    }

    @Override // jdk.management.jfr.FlightRecorderMXBean
    public void closeRecording(long j2) {
        MBeanUtils.checkControl();
        getExistingRecording(j2).close();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // jdk.management.jfr.FlightRecorderMXBean
    public long openStream(long j2, Map<String, String> map) throws NumberFormatException, IOException {
        MBeanUtils.checkControl();
        if (!FlightRecorder.isInitialized()) {
            throw new IllegalArgumentException("No recording available with id " + j2);
        }
        HashMap map2 = map == null ? new HashMap() : new HashMap(map);
        Instant timestamp = MBeanUtils.parseTimestamp((String) map2.get("startTime"), Instant.MIN);
        Instant timestamp2 = MBeanUtils.parseTimestamp((String) map2.get("endTime"), Instant.MAX);
        int blockSize = MBeanUtils.parseBlockSize((String) map2.get("blockSize"), StreamManager.DEFAULT_BLOCK_SIZE);
        InputStream stream = getExistingRecording(j2).getStream(timestamp, timestamp2);
        if (stream == null) {
            throw new IOException("No recording data available");
        }
        return this.streamHandler.create(stream, blockSize).getId();
    }

    @Override // jdk.management.jfr.FlightRecorderMXBean
    public void closeStream(long j2) throws IOException {
        MBeanUtils.checkControl();
        this.streamHandler.getStream(j2).close();
    }

    @Override // jdk.management.jfr.FlightRecorderMXBean
    public byte[] readStream(long j2) throws IOException {
        MBeanUtils.checkMonitor();
        return this.streamHandler.getStream(j2).read();
    }

    @Override // jdk.management.jfr.FlightRecorderMXBean
    public List<RecordingInfo> getRecordings() {
        MBeanUtils.checkMonitor();
        if (!FlightRecorder.isInitialized()) {
            return Collections.emptyList();
        }
        return MBeanUtils.transformList(getRecorder().getRecordings(), RecordingInfo::new);
    }

    @Override // jdk.management.jfr.FlightRecorderMXBean
    public List<ConfigurationInfo> getConfigurations() {
        MBeanUtils.checkMonitor();
        return MBeanUtils.transformList(Configuration.getConfigurations(), ConfigurationInfo::new);
    }

    @Override // jdk.management.jfr.FlightRecorderMXBean
    public List<EventTypeInfo> getEventTypes() {
        MBeanUtils.checkMonitor();
        return MBeanUtils.transformList((List) AccessController.doPrivileged(new PrivilegedAction<List<EventType>>() { // from class: jdk.management.jfr.FlightRecorderMXBeanImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public List<EventType> run2() {
                return ManagementSupport.getEventTypes();
            }
        }, (AccessControlContext) null, new FlightRecorderPermission(Utils.ACCESS_FLIGHT_RECORDER)), EventTypeInfo::new);
    }

    @Override // jdk.management.jfr.FlightRecorderMXBean
    public Map<String, String> getRecordingSettings(long j2) throws IllegalArgumentException {
        MBeanUtils.checkMonitor();
        return getExistingRecording(j2).getSettings();
    }

    @Override // jdk.management.jfr.FlightRecorderMXBean
    public void setRecordingSettings(long j2, Map<String, String> map) throws IllegalArgumentException {
        Objects.requireNonNull(map);
        MBeanUtils.checkControl();
        getExistingRecording(j2).setSettings(map);
    }

    @Override // jdk.management.jfr.FlightRecorderMXBean
    public long newRecording() throws SecurityException {
        MBeanUtils.checkControl();
        getRecorder();
        return ((Recording) AccessController.doPrivileged(new PrivilegedAction<Recording>() { // from class: jdk.management.jfr.FlightRecorderMXBeanImpl.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Recording run2() {
                return new Recording();
            }
        }, (AccessControlContext) null, new FlightRecorderPermission(Utils.ACCESS_FLIGHT_RECORDER))).getId();
    }

    @Override // jdk.management.jfr.FlightRecorderMXBean
    public long takeSnapshot() {
        MBeanUtils.checkControl();
        return getRecorder().takeSnapshot().getId();
    }

    @Override // jdk.management.jfr.FlightRecorderMXBean
    public void setConfiguration(long j2, String str) throws IllegalArgumentException {
        Objects.requireNonNull(str);
        MBeanUtils.checkControl();
        try {
            getExistingRecording(j2).setSettings(Configuration.create(new StringReader(str)).getSettings());
        } catch (IOException | ParseException e2) {
            throw new IllegalArgumentException("Could not parse configuration", e2);
        }
    }

    @Override // jdk.management.jfr.FlightRecorderMXBean
    public void setPredefinedConfiguration(long j2, String str) throws IllegalArgumentException {
        Objects.requireNonNull(str);
        MBeanUtils.checkControl();
        Recording existingRecording = getExistingRecording(j2);
        for (Configuration configuration : Configuration.getConfigurations()) {
            if (configuration.getName().equals(str)) {
                existingRecording.setSettings(configuration.getSettings());
                return;
            }
        }
        throw new IllegalArgumentException("Could not find configuration with name " + str);
    }

    @Override // jdk.management.jfr.FlightRecorderMXBean
    public void copyTo(long j2, String str) throws IOException {
        Objects.requireNonNull(str);
        MBeanUtils.checkControl();
        getExistingRecording(j2).dump(Paths.get(str, new String[0]));
    }

    @Override // jdk.management.jfr.FlightRecorderMXBean
    public void setRecordingOptions(long j2, Map<String, String> map) throws IllegalArgumentException {
        Objects.requireNonNull(map);
        MBeanUtils.checkControl();
        HashMap map2 = new HashMap(map);
        Iterator it = map2.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (!(key instanceof String)) {
                throw new IllegalArgumentException("Option key must not be null, or other type than " + ((Object) String.class));
            }
            if (!OPTIONS.contains(key)) {
                throw new IllegalArgumentException("Unknown recording option: " + key + ". Valid options are " + ((Object) OPTIONS) + ".");
            }
            if (value != null && !(value instanceof String)) {
                throw new IllegalArgumentException("Incorrect value for option " + key + ". Values must be of type " + ((Object) String.class) + " .");
            }
        }
        Recording existingRecording = getExistingRecording(j2);
        validateOption(map2, OPTION_DUMP_ON_EXIT, MBeanUtils::booleanValue);
        validateOption(map2, OPTION_DISK, MBeanUtils::booleanValue);
        validateOption(map2, "name", Function.identity());
        validateOption(map2, OPTION_MAX_AGE, MBeanUtils::duration);
        validateOption(map2, OPTION_MAX_SIZE, MBeanUtils::size);
        validateOption(map2, "duration", MBeanUtils::duration);
        setOption(map2, OPTION_DUMP_ON_EXIT, "false", MBeanUtils::booleanValue, bool -> {
            existingRecording.setDumpOnExit(bool.booleanValue());
        });
        setOption(map2, OPTION_DISK, "true", MBeanUtils::booleanValue, bool2 -> {
            existingRecording.setToDisk(bool2.booleanValue());
        });
        setOption(map2, "name", String.valueOf(existingRecording.getId()), Function.identity(), str -> {
            existingRecording.setName(str);
        });
        setOption(map2, OPTION_MAX_AGE, null, MBeanUtils::duration, duration -> {
            existingRecording.setMaxAge(duration);
        });
        setOption(map2, OPTION_MAX_SIZE, "0", MBeanUtils::size, l2 -> {
            existingRecording.setMaxSize(l2.longValue());
        });
        setOption(map2, "duration", null, MBeanUtils::duration, duration2 -> {
            existingRecording.setDuration(duration2);
        });
    }

    @Override // jdk.management.jfr.FlightRecorderMXBean
    public Map<String, String> getRecordingOptions(long j2) throws IllegalArgumentException {
        MBeanUtils.checkMonitor();
        Recording existingRecording = getExistingRecording(j2);
        HashMap map = new HashMap(10);
        map.put(OPTION_DUMP_ON_EXIT, String.valueOf(existingRecording.getDumpOnExit()));
        map.put(OPTION_DISK, String.valueOf(existingRecording.isToDisk()));
        map.put("name", String.valueOf(existingRecording.getName()));
        map.put(OPTION_MAX_AGE, ManagementSupport.formatTimespan(existingRecording.getMaxAge(), " "));
        Long lValueOf = Long.valueOf(existingRecording.getMaxSize());
        map.put(OPTION_MAX_SIZE, String.valueOf(lValueOf == null ? "0" : lValueOf.toString()));
        map.put("duration", ManagementSupport.formatTimespan(existingRecording.getDuration(), " "));
        return map;
    }

    @Override // jdk.management.jfr.FlightRecorderMXBean
    public long cloneRecording(long j2, boolean z2) throws IllegalStateException, SecurityException {
        MBeanUtils.checkControl();
        return getRecording(j2).copy(z2).getId();
    }

    @Override // java.lang.management.PlatformManagedObject
    public ObjectName getObjectName() {
        return MBeanUtils.createObjectName();
    }

    private Recording getExistingRecording(long j2) {
        Recording recording;
        if (FlightRecorder.isInitialized() && (recording = getRecording(j2)) != null) {
            return recording;
        }
        throw new IllegalArgumentException("No recording available with id " + j2);
    }

    private Recording getRecording(long j2) {
        return getRecorder().getRecordings().stream().filter(recording -> {
            return recording.getId() == j2;
        }).findFirst().orElse(null);
    }

    private static <T, U> void setOption(Map<String, String> map, String str, String str2, Function<String, U> function, Consumer<U> consumer) {
        if (!map.containsKey(str)) {
            return;
        }
        String str3 = map.get(str);
        if (str3 == null) {
            str3 = str2;
        }
        try {
            consumer.accept(function.apply(str3));
        } catch (IllegalArgumentException e2) {
            throw new IllegalArgumentException("Not a valid value for option '" + str + "'. " + e2.getMessage());
        }
    }

    private static <T, U> void validateOption(Map<String, String> map, String str, Function<String, U> function) {
        try {
            String str2 = map.get(str);
            if (str2 == null) {
                return;
            }
            function.apply(str2);
        } catch (IllegalArgumentException e2) {
            throw new IllegalArgumentException("Not a valid value for option '" + str + "'. " + e2.getMessage());
        }
    }

    private FlightRecorder getRecorder() throws SecurityException {
        FlightRecorder flightRecorder;
        synchronized (this.streamHandler) {
            if (this.recorder == null) {
                this.recorder = (FlightRecorder) AccessController.doPrivileged(new PrivilegedAction<FlightRecorder>() { // from class: jdk.management.jfr.FlightRecorderMXBeanImpl.3
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public FlightRecorder run2() {
                        return FlightRecorder.getFlightRecorder();
                    }
                }, (AccessControlContext) null, new FlightRecorderPermission(Utils.ACCESS_FLIGHT_RECORDER));
            }
            flightRecorder = this.recorder;
        }
        return flightRecorder;
    }

    private static MBeanNotificationInfo[] createNotificationInfo() {
        return new MBeanNotificationInfo[]{new MBeanNotificationInfo(new String[]{AttributeChangeNotification.ATTRIBUTE_CHANGE}, AttributeChangeNotification.class.getName(), "Notifies if the RecordingState has changed for one of the recordings, for example if a recording starts or stops")};
    }

    @Override // javax.management.StandardEmitterMBean, javax.management.NotificationBroadcaster
    public void addNotificationListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) {
        final MXBeanListener mXBeanListener = new MXBeanListener(notificationListener, notificationFilter, obj);
        this.listeners.add(mXBeanListener);
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: jdk.management.jfr.FlightRecorderMXBeanImpl.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() throws SecurityException {
                FlightRecorder.addListener(mXBeanListener);
                return null;
            }
        }, (AccessControlContext) null, new FlightRecorderPermission(Utils.ACCESS_FLIGHT_RECORDER));
        super.addNotificationListener(notificationListener, notificationFilter, obj);
    }

    @Override // javax.management.StandardEmitterMBean, javax.management.NotificationBroadcaster
    public void removeNotificationListener(NotificationListener notificationListener) throws ListenerNotFoundException, SecurityException {
        removeListeners(mXBeanListener -> {
            return notificationListener == mXBeanListener.listener;
        });
        super.removeNotificationListener(notificationListener);
    }

    @Override // javax.management.StandardEmitterMBean, javax.management.NotificationEmitter
    public void removeNotificationListener(NotificationListener notificationListener, NotificationFilter notificationFilter, Object obj) throws ListenerNotFoundException, SecurityException {
        removeListeners(mXBeanListener -> {
            return notificationListener == mXBeanListener.listener && notificationFilter == mXBeanListener.filter && obj == mXBeanListener.handback;
        });
        super.removeNotificationListener(notificationListener, notificationFilter, obj);
    }

    private void removeListeners(Predicate<MXBeanListener> predicate) throws SecurityException {
        ArrayList arrayList = new ArrayList(this.listeners.size());
        for (MXBeanListener mXBeanListener : this.listeners) {
            if (predicate.test(mXBeanListener)) {
                arrayList.add(mXBeanListener);
                FlightRecorder.removeListener(mXBeanListener);
            }
        }
        this.listeners.removeAll(arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Notification createNotication(Recording recording) {
        try {
            Long lValueOf = Long.valueOf(recording.getId());
            Object obj = this.changes.get(Long.valueOf(recording.getId()));
            Object attribute = getAttribute(ATTRIBUTE_RECORDINGS);
            if (recording.getState() != RecordingState.CLOSED) {
                this.changes.put(lValueOf, attribute);
            } else {
                this.changes.remove(lValueOf);
            }
            return new AttributeChangeNotification(getObjectName(), this.sequenceNumber.incrementAndGet(), System.currentTimeMillis(), "Recording " + recording.getName() + " is " + ((Object) recording.getState()), ATTRIBUTE_RECORDINGS, attribute.getClass().getName(), obj, attribute);
        } catch (AttributeNotFoundException | MBeanException | ReflectionException e2) {
            throw new RuntimeException("Could not create notifcation for FlightRecorderMXBean. " + e2.getMessage(), e2);
        }
    }
}
