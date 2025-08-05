package com.sun.management;

import javax.management.openmbean.CompositeData;
import jdk.Exported;
import sun.management.VMOptionCompositeData;

@Exported
/* loaded from: rt.jar:com/sun/management/VMOption.class */
public class VMOption {
    private String name;
    private String value;
    private boolean writeable;
    private Origin origin;

    @Exported
    /* loaded from: rt.jar:com/sun/management/VMOption$Origin.class */
    public enum Origin {
        DEFAULT,
        VM_CREATION,
        ENVIRON_VAR,
        CONFIG_FILE,
        MANAGEMENT,
        ERGONOMIC,
        OTHER
    }

    public VMOption(String str, String str2, boolean z2, Origin origin) {
        this.name = str;
        this.value = str2;
        this.writeable = z2;
        this.origin = origin;
    }

    private VMOption(CompositeData compositeData) {
        VMOptionCompositeData.validateCompositeData(compositeData);
        this.name = VMOptionCompositeData.getName(compositeData);
        this.value = VMOptionCompositeData.getValue(compositeData);
        this.writeable = VMOptionCompositeData.isWriteable(compositeData);
        this.origin = VMOptionCompositeData.getOrigin(compositeData);
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public Origin getOrigin() {
        return this.origin;
    }

    public boolean isWriteable() {
        return this.writeable;
    }

    public String toString() {
        return "VM option: " + getName() + " value: " + this.value + "  origin: " + ((Object) this.origin) + " " + (this.writeable ? "(read-write)" : "(read-only)");
    }

    public static VMOption from(CompositeData compositeData) {
        if (compositeData == null) {
            return null;
        }
        if (compositeData instanceof VMOptionCompositeData) {
            return ((VMOptionCompositeData) compositeData).getVMOption();
        }
        return new VMOption(compositeData);
    }
}
