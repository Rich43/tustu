package jdk.jfr.internal.settings;

import java.util.Objects;
import java.util.Set;
import jdk.jfr.BooleanFlag;
import jdk.jfr.Description;
import jdk.jfr.Label;
import jdk.jfr.MetadataDefinition;
import jdk.jfr.Name;
import jdk.jfr.internal.Control;
import jdk.jfr.internal.PlatformEventType;

@Label("Enabled")
@BooleanFlag
@MetadataDefinition
@Name("jdk.settings.Enabled")
@Description("Record event")
/* loaded from: jfr.jar:jdk/jfr/internal/settings/EnabledSetting.class */
public final class EnabledSetting extends Control {
    private final BooleanValue booleanValue;
    private final PlatformEventType eventType;

    public EnabledSetting(PlatformEventType platformEventType, String str) {
        super(str);
        this.booleanValue = BooleanValue.valueOf(str);
        this.eventType = (PlatformEventType) Objects.requireNonNull(platformEventType);
    }

    @Override // jdk.jfr.internal.Control
    public String combine(Set<String> set) {
        return this.booleanValue.union(set);
    }

    @Override // jdk.jfr.internal.Control
    public void setValue(String str) {
        this.booleanValue.setValue(str);
        this.eventType.setEnabled(this.booleanValue.getBoolean());
        if (this.eventType.isEnabled() && !this.eventType.isJVM() && !this.eventType.isInstrumented()) {
            this.eventType.markForInstrumentation(true);
        }
    }

    @Override // jdk.jfr.internal.Control
    public String getValue() {
        return this.booleanValue.getValue();
    }
}
