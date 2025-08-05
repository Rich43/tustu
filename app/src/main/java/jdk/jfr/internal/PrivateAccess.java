package jdk.jfr.internal;

import java.util.List;
import java.util.Map;
import jdk.jfr.AnnotationElement;
import jdk.jfr.Configuration;
import jdk.jfr.EventType;
import jdk.jfr.FlightRecorderPermission;
import jdk.jfr.Recording;
import jdk.jfr.SettingDescriptor;
import jdk.jfr.ValueDescriptor;

/* loaded from: jfr.jar:jdk/jfr/internal/PrivateAccess.class */
public abstract class PrivateAccess {
    private static volatile PrivateAccess instance;

    public abstract Type getType(Object obj);

    public abstract Configuration newConfiguration(String str, String str2, String str3, String str4, Map<String, String> map, String str5);

    public abstract EventType newEventType(PlatformEventType platformEventType);

    public abstract AnnotationElement newAnnotation(Type type, List<Object> list, boolean z2);

    public abstract ValueDescriptor newValueDescriptor(String str, Type type, List<AnnotationElement> list, int i2, boolean z2, String str2);

    public abstract PlatformRecording getPlatformRecording(Recording recording);

    public abstract PlatformEventType getPlatformEventType(EventType eventType);

    public abstract boolean isConstantPool(ValueDescriptor valueDescriptor);

    public abstract String getFieldName(ValueDescriptor valueDescriptor);

    public abstract ValueDescriptor newValueDescriptor(Class<?> cls, String str);

    public abstract SettingDescriptor newSettingDescriptor(Type type, String str, String str2, List<AnnotationElement> list);

    public abstract void setAnnotations(ValueDescriptor valueDescriptor, List<AnnotationElement> list);

    public abstract void setAnnotations(SettingDescriptor settingDescriptor, List<AnnotationElement> list);

    public abstract boolean isUnsigned(ValueDescriptor valueDescriptor);

    public abstract PlatformRecorder getPlatformRecorder();

    public static PrivateAccess getInstance() {
        if (instance == null) {
            new FlightRecorderPermission(Utils.REGISTER_EVENT);
        }
        return instance;
    }

    public static void setPrivateAccess(PrivateAccess privateAccess) {
        instance = privateAccess;
    }
}
