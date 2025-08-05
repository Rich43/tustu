package jdk.jfr.internal.management;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jdk.jfr.EventType;
import jdk.jfr.Recording;
import jdk.jfr.internal.JVMSupport;
import jdk.jfr.internal.LogLevel;
import jdk.jfr.internal.LogTag;
import jdk.jfr.internal.Logger;
import jdk.jfr.internal.MetadataRepository;
import jdk.jfr.internal.PrivateAccess;
import jdk.jfr.internal.Utils;
import jdk.jfr.internal.WriteableUserPath;
import jdk.jfr.internal.instrument.JDKEvents;

/* loaded from: jfr.jar:jdk/jfr/internal/management/ManagementSupport.class */
public final class ManagementSupport {
    public static List<EventType> getEventTypes() throws SecurityException {
        Utils.checkAccessFlightRecorder();
        if (JVMSupport.isNotAvailable()) {
            return new ArrayList();
        }
        JDKEvents.initialize();
        return Collections.unmodifiableList(MetadataRepository.getInstance().getRegisteredEventTypes());
    }

    public static long parseTimespan(String str) {
        return Utils.parseTimespan(str);
    }

    public static final String formatTimespan(Duration duration, String str) {
        return Utils.formatTimespan(duration, str);
    }

    public static void logError(String str) {
        Logger.log(LogTag.JFR, LogLevel.ERROR, str);
    }

    public static String getDestinationOriginalText(Recording recording) {
        WriteableUserPath destination = PrivateAccess.getInstance().getPlatformRecording(recording).getDestination();
        if (destination == null) {
            return null;
        }
        return destination.getOriginalText();
    }
}
