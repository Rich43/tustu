package jdk.jfr;

import java.security.BasicPermission;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import jdk.jfr.internal.PlatformEventType;
import jdk.jfr.internal.PlatformRecorder;
import jdk.jfr.internal.PlatformRecording;
import jdk.jfr.internal.PrivateAccess;
import jdk.jfr.internal.Type;
import jdk.jfr.internal.Utils;

/* loaded from: jfr.jar:jdk/jfr/FlightRecorderPermission.class */
public final class FlightRecorderPermission extends BasicPermission {
    static {
        PrivateAccess.setPrivateAccess(new InternalAccess());
    }

    /* loaded from: jfr.jar:jdk/jfr/FlightRecorderPermission$InternalAccess.class */
    private static final class InternalAccess extends PrivateAccess {
        private InternalAccess() {
        }

        @Override // jdk.jfr.internal.PrivateAccess
        public Type getType(Object obj) {
            if (obj instanceof AnnotationElement) {
                return ((AnnotationElement) obj).getType();
            }
            if (obj instanceof EventType) {
                return ((EventType) obj).getType();
            }
            if (obj instanceof ValueDescriptor) {
                return ((ValueDescriptor) obj).getType();
            }
            if (obj instanceof SettingDescriptor) {
                return ((SettingDescriptor) obj).getType();
            }
            throw new Error("Unknown type " + ((Object) obj.getClass()));
        }

        @Override // jdk.jfr.internal.PrivateAccess
        public Configuration newConfiguration(String str, String str2, String str3, String str4, Map<String, String> map, String str5) {
            return new Configuration(str, str2, str3, str4, map, str5);
        }

        @Override // jdk.jfr.internal.PrivateAccess
        public EventType newEventType(PlatformEventType platformEventType) {
            return new EventType(platformEventType);
        }

        @Override // jdk.jfr.internal.PrivateAccess
        public AnnotationElement newAnnotation(Type type, List<Object> list, boolean z2) {
            return new AnnotationElement(type, list, z2);
        }

        @Override // jdk.jfr.internal.PrivateAccess
        public ValueDescriptor newValueDescriptor(String str, Type type, List<AnnotationElement> list, int i2, boolean z2, String str2) {
            return new ValueDescriptor(type, str, list, i2, z2, str2);
        }

        @Override // jdk.jfr.internal.PrivateAccess
        public PlatformRecording getPlatformRecording(Recording recording) {
            return recording.getInternal();
        }

        @Override // jdk.jfr.internal.PrivateAccess
        public PlatformEventType getPlatformEventType(EventType eventType) {
            return eventType.getPlatformEventType();
        }

        @Override // jdk.jfr.internal.PrivateAccess
        public boolean isConstantPool(ValueDescriptor valueDescriptor) {
            return valueDescriptor.isConstantPool();
        }

        @Override // jdk.jfr.internal.PrivateAccess
        public void setAnnotations(ValueDescriptor valueDescriptor, List<AnnotationElement> list) {
            valueDescriptor.setAnnotations(list);
        }

        @Override // jdk.jfr.internal.PrivateAccess
        public void setAnnotations(SettingDescriptor settingDescriptor, List<AnnotationElement> list) {
            settingDescriptor.setAnnotations(list);
        }

        @Override // jdk.jfr.internal.PrivateAccess
        public String getFieldName(ValueDescriptor valueDescriptor) {
            return valueDescriptor.getJavaFieldName();
        }

        @Override // jdk.jfr.internal.PrivateAccess
        public ValueDescriptor newValueDescriptor(Class<?> cls, String str) {
            return new ValueDescriptor(cls, str, Collections.emptyList(), true);
        }

        @Override // jdk.jfr.internal.PrivateAccess
        public SettingDescriptor newSettingDescriptor(Type type, String str, String str2, List<AnnotationElement> list) {
            return new SettingDescriptor(type, str, str2, list);
        }

        @Override // jdk.jfr.internal.PrivateAccess
        public boolean isUnsigned(ValueDescriptor valueDescriptor) {
            return valueDescriptor.isUnsigned();
        }

        @Override // jdk.jfr.internal.PrivateAccess
        public PlatformRecorder getPlatformRecorder() {
            return FlightRecorder.getFlightRecorder().getInternal();
        }
    }

    public FlightRecorderPermission(String str) {
        super((String) Objects.requireNonNull(str));
        if (!str.equals(Utils.ACCESS_FLIGHT_RECORDER) && !str.equals(Utils.REGISTER_EVENT)) {
            throw new IllegalArgumentException("name: " + str);
        }
    }
}
