package jdk.management.jfr;

import java.util.concurrent.Callable;
import javax.management.JMX;
import javax.management.openmbean.CompositeData;
import jdk.jfr.SettingDescriptor;
import jdk.management.jfr.internal.FlightRecorderMXBeanProvider;

/* loaded from: jfr.jar:jdk/management/jfr/SettingDescriptorInfo.class */
public final class SettingDescriptorInfo {
    private final String name;
    private final String label;
    private final String description;
    private final String typeName;
    private final String contentType;
    private final String defaultValue;

    static {
        FlightRecorderMXBeanProvider.setFlightRecorderMXBeanFactory(new Callable<FlightRecorderMXBean>() { // from class: jdk.management.jfr.SettingDescriptorInfo.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public FlightRecorderMXBean call() throws Exception {
                return new FlightRecorderMXBeanImpl();
            }
        });
    }

    SettingDescriptorInfo(SettingDescriptor settingDescriptor) {
        this.name = settingDescriptor.getName();
        this.label = settingDescriptor.getLabel();
        this.description = settingDescriptor.getDescription();
        this.typeName = settingDescriptor.getTypeName();
        this.contentType = settingDescriptor.getContentType();
        this.defaultValue = settingDescriptor.getDefaultValue();
    }

    private SettingDescriptorInfo(CompositeData compositeData) {
        this.name = (String) compositeData.get("name");
        this.label = (String) compositeData.get("label");
        this.description = (String) compositeData.get("description");
        this.typeName = (String) compositeData.get("typeName");
        this.defaultValue = (String) compositeData.get(JMX.DEFAULT_VALUE_FIELD);
        this.contentType = (String) compositeData.get("contentType");
    }

    public String getLabel() {
        return this.label;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public String getContentType() {
        return this.contentType;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public static SettingDescriptorInfo from(CompositeData compositeData) {
        if (compositeData == null) {
            return null;
        }
        return new SettingDescriptorInfo(compositeData);
    }

    public String toString() {
        Stringifier stringifier = new Stringifier();
        stringifier.add("name", this.name);
        stringifier.add("label", this.label);
        stringifier.add("description", this.description);
        stringifier.add("typeName", this.typeName);
        stringifier.add("contentType", this.contentType);
        stringifier.add(JMX.DEFAULT_VALUE_FIELD, this.defaultValue);
        return stringifier.toString();
    }
}
