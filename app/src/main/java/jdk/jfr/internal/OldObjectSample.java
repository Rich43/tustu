package jdk.jfr.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jdk.jfr.RecordingState;
import jdk.jfr.internal.settings.CutoffSetting;
import jdk.jfr.internal.test.WhiteBox;

/* loaded from: jfr.jar:jdk/jfr/internal/OldObjectSample.class */
public final class OldObjectSample {
    private static final String EVENT_NAME = "jdk.OldObjectSample";
    private static final String OLD_OBJECT_CUTOFF = "jdk.OldObjectSample#cutoff";
    private static final String OLD_OBJECT_ENABLED = "jdk.OldObjectSample#enabled";

    public static void emit(PlatformRecording platformRecording) {
        if (isEnabled(platformRecording)) {
            JVM.getJVM().emitOldObjectSamples(Utils.nanosToTicks(CutoffSetting.parseValueSafe(platformRecording.getSettings().get(OLD_OBJECT_CUTOFF))), WhiteBox.getWriteAllObjectSamples());
        }
    }

    public static void emit(List<PlatformRecording> list, Boolean bool) {
        boolean z2 = false;
        long jMax = Boolean.TRUE.equals(bool) ? Long.MAX_VALUE : 0L;
        for (PlatformRecording platformRecording : list) {
            if (platformRecording.getState() == RecordingState.RUNNING && isEnabled(platformRecording)) {
                z2 = true;
                jMax = Math.max(CutoffSetting.parseValueSafe(platformRecording.getSettings().get(OLD_OBJECT_CUTOFF)), jMax);
            }
        }
        if (z2) {
            JVM.getJVM().emitOldObjectSamples(Utils.nanosToTicks(jMax), WhiteBox.getWriteAllObjectSamples());
        }
    }

    public static void updateSettingPathToGcRoots(Map<String, String> map, Boolean bool) {
        if (bool != null) {
            map.put(OLD_OBJECT_CUTOFF, bool.booleanValue() ? "infinity" : "0 ns");
        }
    }

    public static Map<String, String> createSettingsForSnapshot(PlatformRecording platformRecording, Boolean bool) {
        HashMap map = new HashMap(platformRecording.getSettings());
        updateSettingPathToGcRoots(map, bool);
        return map;
    }

    private static boolean isEnabled(PlatformRecording platformRecording) {
        return "true".equals(platformRecording.getSettings().get(OLD_OBJECT_ENABLED));
    }
}
