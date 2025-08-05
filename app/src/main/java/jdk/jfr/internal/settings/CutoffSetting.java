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

@Label("Cutoff")
@Timespan
@MetadataDefinition
@Name("jdk.settings.Cutoff")
@Description("Limit running time of event")
/* loaded from: jfr.jar:jdk/jfr/internal/settings/CutoffSetting.class */
public final class CutoffSetting extends Control {
    private static final long typeId = Type.getTypeId(CutoffSetting.class);
    private String value;
    private final PlatformEventType eventType;

    public CutoffSetting(PlatformEventType platformEventType, String str) {
        super(str);
        this.value = "0 ns";
        this.eventType = (PlatformEventType) Objects.requireNonNull(platformEventType);
    }

    @Override // jdk.jfr.internal.Control
    public String combine(Set<String> set) {
        long j2 = 0;
        String str = "0 ns";
        for (String str2 : set) {
            long timespanWithInfinity = Utils.parseTimespanWithInfinity(str2);
            if (timespanWithInfinity > j2) {
                str = str2;
                j2 = timespanWithInfinity;
            }
        }
        return str;
    }

    @Override // jdk.jfr.internal.Control
    public void setValue(String str) {
        long timespanWithInfinity = Utils.parseTimespanWithInfinity(str);
        this.value = str;
        this.eventType.setCutoff(timespanWithInfinity);
    }

    @Override // jdk.jfr.internal.Control
    public String getValue() {
        return this.value;
    }

    public static boolean isType(long j2) {
        return typeId == j2;
    }

    public static long parseValueSafe(String str) {
        if (str == null) {
            return 0L;
        }
        try {
            return Utils.parseTimespanWithInfinity(str);
        } catch (NumberFormatException e2) {
            return 0L;
        }
    }
}
