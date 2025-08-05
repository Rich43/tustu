package jdk.jfr;

import java.security.AccessController;
import java.util.Set;
import jdk.jfr.internal.Control;

@MetadataDefinition
/* loaded from: jfr.jar:jdk/jfr/SettingControl.class */
public abstract class SettingControl extends Control {
    @Override // jdk.jfr.internal.Control
    public abstract String combine(Set<String> set);

    @Override // jdk.jfr.internal.Control
    public abstract void setValue(String str);

    @Override // jdk.jfr.internal.Control
    public abstract String getValue();

    protected SettingControl() {
        super(AccessController.getContext());
    }
}
