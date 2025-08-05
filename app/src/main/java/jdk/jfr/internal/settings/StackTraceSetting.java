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
import jdk.jfr.internal.Type;

@Label("Stack Trace")
@BooleanFlag
@MetadataDefinition
@Name("jdk.settings.StackTrace")
@Description("Record stack traces")
/* loaded from: jfr.jar:jdk/jfr/internal/settings/StackTraceSetting.class */
public final class StackTraceSetting extends Control {
    private static final long typeId = Type.getTypeId(StackTraceSetting.class);
    private final BooleanValue booleanValue;
    private final PlatformEventType eventType;

    public StackTraceSetting(PlatformEventType platformEventType, String str) {
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
        this.eventType.setStackTraceEnabled(this.booleanValue.getBoolean());
    }

    @Override // jdk.jfr.internal.Control
    public String getValue() {
        return this.booleanValue.getValue();
    }

    public static boolean isType(long j2) {
        return typeId == j2;
    }
}
