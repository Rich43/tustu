package org.bluez.dbus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.UInt32;
import org.freedesktop.dbus.Variant;

/* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/dbus/DBusProperties.class */
public abstract class DBusProperties {

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/dbus/DBusProperties$DBusProperty.class */
    public @interface DBusProperty {
        String name() default "";

        Class<?> type() default void.class;

        DBusPropertyAccessType access() default DBusPropertyAccessType.READWRITE;
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/dbus/DBusProperties$DBusPropertyAccessType.class */
    public enum DBusPropertyAccessType {
        READWRITE,
        READONLY
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/dbus/DBusProperties$PropertiesAccess.class */
    public interface PropertiesAccess extends DBusInterface {
        /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$DoesNotExist, org.bluez.Error$InvalidArguments] */
        Map<String, Variant<?>> GetProperties();

        /* JADX WARN: Byte code manipulation detected: skipped illegal throws declarations: [org.bluez.Error$DoesNotExist, org.bluez.Error$InvalidArguments] */
        void SetProperty(String str, Variant<?> variant);
    }

    /* loaded from: bluecove-bluez-2.1.1.jar:org/bluez/dbus/DBusProperties$PropertyEnum.class */
    public interface PropertyEnum {
    }

    public static String getStringValue(PropertiesAccess dBusInterface, PropertyEnum propertyEnum) {
        return getStringValue(dBusInterface.GetProperties(), propertyEnum);
    }

    public static String getStringValue(Map<String, Variant<?>> properties, PropertyEnum propertyEnum) {
        Variant<?> variant;
        if (properties == null || (variant = properties.get(getPropertyName(propertyEnum))) == null) {
            return null;
        }
        return (String) variant.getValue();
    }

    public static boolean getBooleanValue(PropertiesAccess dBusInterface, PropertyEnum propertyEnum) {
        return getBooleanValue(dBusInterface.GetProperties(), propertyEnum);
    }

    public static boolean getBooleanValue(Map<String, Variant<?>> properties, PropertyEnum propertyEnum) {
        return ((Boolean) properties.get(getPropertyName(propertyEnum)).getValue()).booleanValue();
    }

    public static boolean getBooleanValue(Map<String, Variant<?>> properties, PropertyEnum propertyEnum, boolean defaultValue) {
        Variant<?> variant = properties.get(getPropertyName(propertyEnum));
        if (variant == null) {
            return defaultValue;
        }
        return ((Boolean) variant.getValue()).booleanValue();
    }

    public static Integer getIntValue(PropertiesAccess dBusInterface, PropertyEnum propertyEnum) {
        return getIntValue(dBusInterface.GetProperties(), propertyEnum);
    }

    public static Integer getIntValue(Map<String, Variant<?>> properties, PropertyEnum propertyEnum) {
        Variant<?> variant;
        if (properties == null || (variant = properties.get(getPropertyName(propertyEnum))) == null) {
            return null;
        }
        return Integer.valueOf(((UInt32) variant.getValue()).intValue());
    }

    public static String getPropertyName(PropertyEnum propertyEnum) {
        return propertyEnum.toString();
    }
}
