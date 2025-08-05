package jdk.jfr.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jdk.jfr.AnnotationElement;
import jdk.jfr.Enabled;
import jdk.jfr.Event;
import jdk.jfr.Name;
import jdk.jfr.Period;
import jdk.jfr.SettingControl;
import jdk.jfr.SettingDefinition;
import jdk.jfr.StackTrace;
import jdk.jfr.Threshold;
import jdk.jfr.events.ActiveSettingEvent;
import jdk.jfr.internal.EventInstrumentation;
import jdk.jfr.internal.settings.CutoffSetting;
import jdk.jfr.internal.settings.EnabledSetting;
import jdk.jfr.internal.settings.PeriodSetting;
import jdk.jfr.internal.settings.StackTraceSetting;
import jdk.jfr.internal.settings.ThresholdSetting;

/* loaded from: jfr.jar:jdk/jfr/internal/EventControl.class */
public final class EventControl {
    static final String FIELD_SETTING_PREFIX = "setting";
    private static final Type TYPE_ENABLED = TypeLibrary.createType(EnabledSetting.class);
    private static final Type TYPE_THRESHOLD = TypeLibrary.createType(ThresholdSetting.class);
    private static final Type TYPE_STACK_TRACE = TypeLibrary.createType(StackTraceSetting.class);
    private static final Type TYPE_PERIOD = TypeLibrary.createType(PeriodSetting.class);
    private static final Type TYPE_CUTOFF = TypeLibrary.createType(CutoffSetting.class);
    private final List<EventInstrumentation.SettingInfo> settingInfos;
    private final Map<String, Control> eventControls;
    private final PlatformEventType type;
    private final String idName;

    EventControl(PlatformEventType platformEventType) {
        this.settingInfos = new ArrayList();
        this.eventControls = new HashMap(5);
        this.eventControls.put(Enabled.NAME, defineEnabled(platformEventType));
        if (platformEventType.hasDuration()) {
            this.eventControls.put(Threshold.NAME, defineThreshold(platformEventType));
        }
        if (platformEventType.hasStackTrace()) {
            this.eventControls.put("stackTrace", defineStackTrace(platformEventType));
        }
        if (platformEventType.hasPeriod()) {
            this.eventControls.put("period", definePeriod(platformEventType));
        }
        if (platformEventType.hasCutoff()) {
            this.eventControls.put(Cutoff.NAME, defineCutoff(platformEventType));
        }
        ArrayList arrayList = new ArrayList(platformEventType.getAnnotationElements());
        remove(platformEventType, arrayList, Threshold.class);
        remove(platformEventType, arrayList, Period.class);
        remove(platformEventType, arrayList, Enabled.class);
        remove(platformEventType, arrayList, StackTrace.class);
        remove(platformEventType, arrayList, Cutoff.class);
        arrayList.trimToSize();
        platformEventType.setAnnotations(arrayList);
        this.type = platformEventType;
        this.idName = String.valueOf(platformEventType.getId());
    }

    static void remove(PlatformEventType platformEventType, List<AnnotationElement> list, Class<? extends Annotation> cls) {
        long typeId = Type.getTypeId(cls);
        for (AnnotationElement annotationElement : platformEventType.getAnnotationElements()) {
            if (annotationElement.getTypeId() == typeId && annotationElement.getTypeName().equals(cls.getName())) {
                list.remove(annotationElement);
            }
        }
    }

    EventControl(PlatformEventType platformEventType, Class<? extends Event> cls) throws SecurityException {
        this(platformEventType);
        defineSettings(cls);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void defineSettings(Class<?> cls) throws SecurityException {
        boolean z2 = true;
        while (true) {
            boolean z3 = z2;
            if (cls != null) {
                for (Method method : cls.getDeclaredMethods()) {
                    boolean zIsPrivate = Modifier.isPrivate(method.getModifiers());
                    if (method.getReturnType() == Boolean.TYPE && method.getParameterCount() == 1 && ((!zIsPrivate || z3) && ((SettingDefinition) method.getDeclaredAnnotation(SettingDefinition.class)) != null)) {
                        Class<?> type = method.getParameters()[0].getType();
                        if (!Modifier.isAbstract(type.getModifiers()) && SettingControl.class.isAssignableFrom(type)) {
                            String name = method.getName();
                            Name name2 = (Name) method.getAnnotation(Name.class);
                            if (name2 != null) {
                                name = name2.value();
                            }
                            if (!this.eventControls.containsKey(name)) {
                                defineSetting(type, method, this.type, name);
                            }
                        }
                    }
                }
                cls = cls.getSuperclass();
                z2 = false;
            } else {
                return;
            }
        }
    }

    private void defineSetting(Class<? extends SettingControl> cls, Method method, PlatformEventType platformEventType, String str) throws SecurityException {
        try {
            int size = this.settingInfos.size();
            EventInstrumentation.SettingInfo settingInfo = new EventInstrumentation.SettingInfo(FIELD_SETTING_PREFIX + size, size);
            settingInfo.settingControl = instantiateSettingControl(cls);
            SettingControl settingControl = settingInfo.settingControl;
            settingControl.setDefault();
            String valueSafe = settingControl.getValueSafe();
            if (valueSafe != null) {
                Type typeCreateType = TypeLibrary.createType(cls);
                ArrayList arrayList = new ArrayList();
                for (Annotation annotation : method.getDeclaredAnnotations()) {
                    AnnotationElement annotationElementCreateAnnotation = TypeLibrary.createAnnotation(annotation);
                    if (annotationElementCreateAnnotation != null) {
                        arrayList.add(annotationElementCreateAnnotation);
                    }
                }
                arrayList.trimToSize();
                this.eventControls.put(str, settingInfo.settingControl);
                platformEventType.add(PrivateAccess.getInstance().newSettingDescriptor(typeCreateType, str, valueSafe, arrayList));
                this.settingInfos.add(settingInfo);
            }
        } catch (IllegalAccessException e2) {
            throw new IllegalAccessError("Could not access setting " + cls.getName() + " for event " + platformEventType.getLogName() + ". " + e2.getMessage());
        } catch (InstantiationException e3) {
            throw new InstantiationError("Could not instantiate setting " + cls.getName() + " for event " + platformEventType.getLogName() + ". " + e3.getMessage());
        }
    }

    private SettingControl instantiateSettingControl(Class<? extends SettingControl> cls) throws IllegalAccessException, InstantiationException {
        SecuritySupport.makeVisibleToJFR(cls);
        try {
            Constructor<?> constructor = cls.getDeclaredConstructors()[0];
            SecuritySupport.setAccessible(constructor);
            try {
                return (SettingControl) constructor.newInstance(new Object[0]);
            } catch (IllegalArgumentException | InvocationTargetException e2) {
                throw new InternalError("Could not instantiate setting for class " + cls.getName());
            }
        } catch (Exception e3) {
            throw ((Error) new InternalError("Could not get constructor for " + cls.getName()).initCause(e3));
        }
    }

    private static Control defineEnabled(PlatformEventType platformEventType) {
        Enabled enabled = (Enabled) platformEventType.getAnnotation(Enabled.class);
        String string = platformEventType.isJVM() ? "false" : "true";
        if (enabled != null) {
            string = Boolean.toString(enabled.value());
        }
        platformEventType.add(PrivateAccess.getInstance().newSettingDescriptor(TYPE_ENABLED, Enabled.NAME, string, Collections.emptyList()));
        return new EnabledSetting(platformEventType, string);
    }

    private static Control defineThreshold(PlatformEventType platformEventType) {
        Threshold threshold = (Threshold) platformEventType.getAnnotation(Threshold.class);
        String strValue = "0 ns";
        if (threshold != null) {
            strValue = threshold.value();
        }
        platformEventType.add(PrivateAccess.getInstance().newSettingDescriptor(TYPE_THRESHOLD, Threshold.NAME, strValue, Collections.emptyList()));
        return new ThresholdSetting(platformEventType, strValue);
    }

    private static Control defineStackTrace(PlatformEventType platformEventType) {
        StackTrace stackTrace = (StackTrace) platformEventType.getAnnotation(StackTrace.class);
        String string = "true";
        if (stackTrace != null) {
            string = Boolean.toString(stackTrace.value());
        }
        platformEventType.add(PrivateAccess.getInstance().newSettingDescriptor(TYPE_STACK_TRACE, "stackTrace", string, Collections.emptyList()));
        return new StackTraceSetting(platformEventType, string);
    }

    private static Control defineCutoff(PlatformEventType platformEventType) {
        Cutoff cutoff = (Cutoff) platformEventType.getAnnotation(Cutoff.class);
        String strValue = "infinity";
        if (cutoff != null) {
            strValue = cutoff.value();
        }
        platformEventType.add(PrivateAccess.getInstance().newSettingDescriptor(TYPE_CUTOFF, Cutoff.NAME, strValue, Collections.emptyList()));
        return new CutoffSetting(platformEventType, strValue);
    }

    private static Control definePeriod(PlatformEventType platformEventType) {
        Period period = (Period) platformEventType.getAnnotation(Period.class);
        String strValue = PeriodSetting.EVERY_CHUNK;
        if (period != null) {
            strValue = period.value();
        }
        platformEventType.add(PrivateAccess.getInstance().newSettingDescriptor(TYPE_PERIOD, "period", strValue, Collections.emptyList()));
        return new PeriodSetting(platformEventType, strValue);
    }

    void disable() {
        for (Control control : this.eventControls.values()) {
            if (control instanceof EnabledSetting) {
                control.setValueSafe("false");
                return;
            }
        }
    }

    void writeActiveSettingEvent() {
        if (!this.type.isRegistered()) {
            return;
        }
        for (Map.Entry<String, Control> entry : this.eventControls.entrySet()) {
            Control value = entry.getValue();
            if (Utils.isSettingVisible(value, this.type.hasEventHook())) {
                String lastValue = value.getLastValue();
                if (lastValue == null) {
                    lastValue = value.getDefaultValue();
                }
                ActiveSettingEvent activeSettingEvent = new ActiveSettingEvent();
                activeSettingEvent.id = this.type.getId();
                activeSettingEvent.name = entry.getKey();
                activeSettingEvent.value = lastValue;
                activeSettingEvent.commit();
            }
        }
    }

    public Set<Map.Entry<String, Control>> getEntries() {
        return this.eventControls.entrySet();
    }

    public PlatformEventType getEventType() {
        return this.type;
    }

    public String getSettingsId() {
        return this.idName;
    }

    public List<EventInstrumentation.SettingInfo> getSettingInfos() {
        return this.settingInfos;
    }
}
