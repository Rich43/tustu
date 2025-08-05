package jdk.jfr.internal.settings;

import java.util.Objects;
import java.util.Set;
import jdk.jfr.Description;
import jdk.jfr.Label;
import jdk.jfr.MetadataDefinition;
import jdk.jfr.Name;
import jdk.jfr.Timespan;
import jdk.jfr.internal.Control;
import jdk.jfr.internal.PlatformEventType;
import jdk.jfr.internal.Type;
import jdk.jfr.internal.Utils;

@Label("Threshold")
@Timespan
@MetadataDefinition
@Name("jdk.settings.Threshold")
@Description("Record event with duration above or equal to threshold")
/* loaded from: jfr.jar:jdk/jfr/internal/settings/ThresholdSetting.class */
public final class ThresholdSetting extends Control {
    private static final long typeId = Type.getTypeId(ThresholdSetting.class);
    private String value;
    private final PlatformEventType eventType;

    public ThresholdSetting(PlatformEventType platformEventType, String str) {
        super(str);
        this.value = "0 ns";
        this.eventType = (PlatformEventType) Objects.requireNonNull(platformEventType);
    }

    @Override // jdk.jfr.internal.Control
    public String combine(Set<String> set) {
        Long lValueOf = null;
        String str = null;
        for (String str2 : set) {
            long timespanWithInfinity = Utils.parseTimespanWithInfinity(str2);
            if (lValueOf == null) {
                lValueOf = Long.valueOf(timespanWithInfinity);
                str = str2;
            } else if (timespanWithInfinity < lValueOf.longValue()) {
                str = str2;
                lValueOf = Long.valueOf(timespanWithInfinity);
            }
        }
        return str == null ? "0 ns" : str;
    }

    @Override // jdk.jfr.internal.Control
    public void setValue(String str) {
        long timespanWithInfinity = Utils.parseTimespanWithInfinity(str);
        this.value = str;
        this.eventType.setThreshold(timespanWithInfinity);
    }

    @Override // jdk.jfr.internal.Control
    public String getValue() {
        return this.value;
    }

    public static boolean isType(long j2) {
        return typeId == j2;
    }
}
