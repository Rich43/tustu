package jdk.jfr.internal.settings;

import java.util.Objects;
import java.util.Set;
import jdk.jfr.Description;
import jdk.jfr.Label;
import jdk.jfr.MetadataDefinition;
import jdk.jfr.Name;
import jdk.jfr.internal.Control;
import jdk.jfr.internal.PlatformEventType;
import jdk.jfr.internal.Type;
import jdk.jfr.internal.Utils;

@Label("Period")
@MetadataDefinition
@Name("jdk.settings.Period")
@Description("Record event at interval")
/* loaded from: jfr.jar:jdk/jfr/internal/settings/PeriodSetting.class */
public final class PeriodSetting extends Control {
    private static final long typeId = Type.getTypeId(PeriodSetting.class);
    public static final String EVERY_CHUNK = "everyChunk";
    public static final String BEGIN_CHUNK = "beginChunk";
    public static final String END_CHUNK = "endChunk";
    public static final String NAME = "period";
    private final PlatformEventType eventType;
    private String value;

    public PeriodSetting(PlatformEventType platformEventType, String str) {
        super(str);
        this.value = EVERY_CHUNK;
        this.eventType = (PlatformEventType) Objects.requireNonNull(platformEventType);
    }

    @Override // jdk.jfr.internal.Control
    public String combine(Set<String> set) {
        boolean z2 = false;
        boolean z3 = false;
        Long lValueOf = null;
        String str = null;
        for (String str2 : set) {
            switch (str2) {
                case "everyChunk":
                    z2 = true;
                    z3 = true;
                    break;
                case "beginChunk":
                    z2 = true;
                    break;
                case "endChunk":
                    z3 = true;
                    break;
                default:
                    long timespanWithInfinity = Utils.parseTimespanWithInfinity(str2);
                    if (lValueOf == null) {
                        str = str2;
                        lValueOf = Long.valueOf(timespanWithInfinity);
                        break;
                    } else if (timespanWithInfinity < lValueOf.longValue()) {
                        str = str2;
                        lValueOf = Long.valueOf(timespanWithInfinity);
                        break;
                    } else {
                        break;
                    }
            }
        }
        if (lValueOf != null) {
            return str;
        }
        if (z2 && !z3) {
            return BEGIN_CHUNK;
        }
        if (!z2 && z3) {
            return END_CHUNK;
        }
        return EVERY_CHUNK;
    }

    @Override // jdk.jfr.internal.Control
    public void setValue(String str) {
        switch (str) {
            case "everyChunk":
                this.eventType.setPeriod(0L, true, true);
                break;
            case "beginChunk":
                this.eventType.setPeriod(0L, true, false);
                break;
            case "endChunk":
                this.eventType.setPeriod(0L, false, true);
                break;
            default:
                long timespanWithInfinity = Utils.parseTimespanWithInfinity(str);
                if (timespanWithInfinity != Long.MAX_VALUE) {
                    this.eventType.setPeriod(timespanWithInfinity / 1000000, false, false);
                    break;
                } else {
                    this.eventType.setPeriod(Long.MAX_VALUE, false, false);
                    break;
                }
        }
        this.value = str;
    }

    @Override // jdk.jfr.internal.Control
    public String getValue() {
        return this.value;
    }

    public static boolean isType(long j2) {
        return typeId == j2;
    }
}
