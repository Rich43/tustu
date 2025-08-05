package jdk.jfr.internal;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import jdk.jfr.AnnotationElement;
import jdk.jfr.Event;
import jdk.jfr.EventType;
import jdk.jfr.Period;
import jdk.jfr.StackTrace;
import jdk.jfr.Threshold;
import jdk.jfr.ValueDescriptor;
import jdk.jfr.internal.RequestEngine;
import jdk.jfr.internal.handlers.EventHandler;

/* loaded from: jfr.jar:jdk/jfr/internal/MetadataRepository.class */
public final class MetadataRepository {
    private static final JVM jvm = JVM.getJVM();
    private static final MetadataRepository instace = new MetadataRepository();
    private boolean unregistered;
    private final List<EventType> nativeEventTypes = new ArrayList(100);
    private final List<EventControl> nativeControls = new ArrayList(100);
    private final TypeLibrary typeLibrary = TypeLibrary.getInstance();
    private final SettingsManager settingsManager = new SettingsManager();
    private boolean staleMetadata = true;
    private long lastUnloaded = -1;

    public MetadataRepository() {
        initializeJVMEventTypes();
    }

    private void initializeJVMEventTypes() {
        ArrayList arrayList = new ArrayList();
        for (Type type : this.typeLibrary.getTypes()) {
            if (type instanceof PlatformEventType) {
                PlatformEventType platformEventType = (PlatformEventType) type;
                EventType eventTypeNewEventType = PrivateAccess.getInstance().newEventType(platformEventType);
                platformEventType.setHasDuration(eventTypeNewEventType.getAnnotation(Threshold.class) != null);
                platformEventType.setHasStackTrace(eventTypeNewEventType.getAnnotation(StackTrace.class) != null);
                platformEventType.setHasCutoff(eventTypeNewEventType.getAnnotation(Cutoff.class) != null);
                platformEventType.setHasPeriod(eventTypeNewEventType.getAnnotation(Period.class) != null);
                if (platformEventType.hasPeriod()) {
                    platformEventType.setEventHook(true);
                    if (!"jdk.ExecutionSample".equals(type.getName())) {
                        arrayList.add(new RequestEngine.RequestHook(platformEventType));
                    }
                }
                this.nativeControls.add(new EventControl(platformEventType));
                this.nativeEventTypes.add(eventTypeNewEventType);
            }
        }
        RequestEngine.addHooks(arrayList);
    }

    public static MetadataRepository getInstance() {
        return instace;
    }

    public synchronized List<EventType> getRegisteredEventTypes() {
        List<EventHandler> eventHandlers = getEventHandlers();
        ArrayList arrayList = new ArrayList(eventHandlers.size() + this.nativeEventTypes.size());
        for (EventHandler eventHandler : eventHandlers) {
            if (eventHandler.isRegistered()) {
                arrayList.add(eventHandler.getEventType());
            }
        }
        arrayList.addAll(this.nativeEventTypes);
        return arrayList;
    }

    public synchronized EventType getEventType(Class<? extends Event> cls) {
        EventHandler handler = getHandler(cls);
        if (handler != null && handler.isRegistered()) {
            return handler.getEventType();
        }
        throw new IllegalStateException("Event class " + cls.getName() + " is not registered");
    }

    public synchronized void unregister(Class<? extends Event> cls) {
        Utils.checkRegisterPermission();
        EventHandler handler = getHandler(cls);
        if (handler != null) {
            handler.setRegistered(false);
        }
    }

    public synchronized EventType register(Class<? extends Event> cls) {
        return register(cls, Collections.emptyList(), Collections.emptyList());
    }

    public synchronized EventType register(Class<? extends Event> cls, List<AnnotationElement> list, List<ValueDescriptor> list2) throws Error, SecurityException {
        Utils.checkRegisterPermission();
        EventHandler handler = getHandler(cls);
        if (handler == null) {
            handler = makeHandler(cls, list, list2);
        }
        handler.setRegistered(true);
        this.typeLibrary.addType(handler.getPlatformEventType());
        if (jvm.isRecording()) {
            storeDescriptorInJVM();
            this.settingsManager.setEventControl(handler.getEventControl());
            this.settingsManager.updateRetransform(Collections.singletonList(cls));
        } else {
            setStaleMetadata();
        }
        return handler.getEventType();
    }

    private EventHandler getHandler(Class<? extends Event> cls) {
        Utils.ensureValidEventSubclass(cls);
        SecuritySupport.makeVisibleToJFR(cls);
        Utils.ensureInitialized(cls);
        return Utils.getHandler(cls);
    }

    private EventHandler makeHandler(Class<? extends Event> cls, List<AnnotationElement> list, List<ValueDescriptor> list2) throws Error {
        Class<? extends EventHandler> clsMakeEventHandlerClass;
        SecuritySupport.addHandlerExport(cls);
        PlatformEventType platformEventType = (PlatformEventType) TypeLibrary.createType(cls, list, list2);
        EventType eventTypeNewEventType = PrivateAccess.getInstance().newEventType(platformEventType);
        EventControl eventControl = new EventControl(platformEventType, cls);
        try {
            clsMakeEventHandlerClass = Class.forName(EventHandlerCreator.makeEventHandlerName(eventTypeNewEventType.getId()), false, Event.class.getClassLoader()).asSubclass(EventHandler.class);
            platformEventType.setInstrumented();
            Logger.log(LogTag.JFR_SYSTEM, LogLevel.DEBUG, "Found existing event handler for " + eventTypeNewEventType.getName());
        } catch (ClassNotFoundException e2) {
            clsMakeEventHandlerClass = new EventHandlerCreator(eventTypeNewEventType.getId(), eventControl.getSettingInfos(), eventTypeNewEventType, cls).makeEventHandlerClass();
            Logger.log(LogTag.JFR_SYSTEM, LogLevel.DEBUG, "Created event handler for " + eventTypeNewEventType.getName());
        }
        EventHandler eventHandlerInstantiateEventHandler = EventHandlerCreator.instantiateEventHandler(clsMakeEventHandlerClass, true, eventTypeNewEventType, eventControl);
        Utils.setHandler(cls, eventHandlerInstantiateEventHandler);
        return eventHandlerInstantiateEventHandler;
    }

    public synchronized void setSettings(List<Map<String, String>> list) {
        this.settingsManager.setSettings(list);
    }

    synchronized void disableEvents() {
        Iterator<EventControl> it = getEventControls().iterator();
        while (it.hasNext()) {
            it.next().disable();
        }
    }

    public synchronized List<EventControl> getEventControls() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.nativeControls);
        Iterator<EventHandler> it = getEventHandlers().iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().getEventControl());
        }
        return arrayList;
    }

    private void storeDescriptorInJVM() throws InternalError {
        jvm.storeMetadataDescriptor(getBinaryRepresentation());
        this.staleMetadata = false;
    }

    private static List<EventHandler> getEventHandlers() {
        List<Class<? extends Event>> allEventClasses = jvm.getAllEventClasses();
        ArrayList arrayList = new ArrayList(allEventClasses.size());
        Iterator<Class<? extends Event>> it = allEventClasses.iterator();
        while (it.hasNext()) {
            EventHandler handler = Utils.getHandler(it.next());
            if (handler != null) {
                arrayList.add(handler);
            }
        }
        return arrayList;
    }

    private byte[] getBinaryRepresentation() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(40000);
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            List<Type> types = this.typeLibrary.getTypes();
            Collections.sort(types);
            MetadataDescriptor.write(types, dataOutputStream);
            dataOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e2) {
            throw new InternalError(e2);
        }
    }

    synchronized boolean isEnabled(String str) {
        return this.settingsManager.isEnabled(str);
    }

    synchronized void setStaleMetadata() {
        this.staleMetadata = true;
    }

    synchronized void setOutput(String str) {
        jvm.setOutput(str);
        unregisterUnloaded();
        if (this.unregistered) {
            this.staleMetadata = this.typeLibrary.clearUnregistered();
            this.unregistered = false;
        }
        if (this.staleMetadata) {
            storeDescriptorInJVM();
        }
    }

    private void unregisterUnloaded() {
        long unloadedEventClassCount = jvm.getUnloadedEventClassCount();
        if (this.lastUnloaded != unloadedEventClassCount) {
            this.lastUnloaded = unloadedEventClassCount;
            List<Class<? extends Event>> allEventClasses = jvm.getAllEventClasses();
            HashSet hashSet = new HashSet(allEventClasses.size());
            Iterator<Class<? extends Event>> it = allEventClasses.iterator();
            while (it.hasNext()) {
                hashSet.add(Long.valueOf(Type.getTypeId(it.next())));
            }
            for (Type type : this.typeLibrary.getTypes()) {
                if ((type instanceof PlatformEventType) && !hashSet.contains(Long.valueOf(type.getId()))) {
                    PlatformEventType platformEventType = (PlatformEventType) type;
                    if (!platformEventType.isJVM()) {
                        platformEventType.setRegistered(false);
                    }
                }
            }
        }
    }

    public synchronized void setUnregistered() {
        this.unregistered = true;
    }
}
