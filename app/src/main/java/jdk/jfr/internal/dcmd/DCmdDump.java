package jdk.jfr.internal.dcmd;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAmount;
import jdk.jfr.FlightRecorder;
import jdk.jfr.Recording;
import jdk.jfr.internal.LogLevel;
import jdk.jfr.internal.LogTag;
import jdk.jfr.internal.Logger;
import jdk.jfr.internal.PlatformRecorder;
import jdk.jfr.internal.PlatformRecording;
import jdk.jfr.internal.PrivateAccess;
import jdk.jfr.internal.SecuritySupport;
import jdk.jfr.internal.Utils;
import jdk.jfr.internal.WriteableUserPath;
import jdk.nashorn.internal.runtime.regexp.joni.constants.AsmConstants;
import sun.util.locale.LanguageTag;

/* loaded from: jfr.jar:jdk/jfr/internal/dcmd/DCmdDump.class */
final class DCmdDump extends AbstractDCmd {
    DCmdDump() {
    }

    public String execute(String str, String str2, Long l2, Long l3, String str3, String str4, Boolean bool) throws DCmdException {
        if (Logger.shouldLog(LogTag.JFR_DCMD, LogLevel.DEBUG)) {
            Logger.log(LogTag.JFR_DCMD, LogLevel.DEBUG, "Executing DCmdDump: name=" + str + ", filename=" + str2 + ", maxage=" + ((Object) l2) + ", maxsize=" + ((Object) l3) + ", begin=" + str3 + ", end" + str4 + ", path-to-gc-roots=" + ((Object) bool));
        }
        if (FlightRecorder.getFlightRecorder().getRecordings().isEmpty()) {
            throw new DCmdException("No recordings to dump from. Use JFR.start to start a recording.", new Object[0]);
        }
        if (l2 != null) {
            if (str4 != null || str3 != null) {
                throw new DCmdException("Dump failed, maxage can't be combined with begin or end.", new Object[0]);
            }
            if (l2.longValue() < 0) {
                throw new DCmdException("Dump failed, maxage can't be negative.", new Object[0]);
            }
            if (l2.longValue() == 0) {
                l2 = 4611686018427387903L;
            }
        }
        if (l3 != null) {
            if (l3.longValue() < 0) {
                throw new DCmdException("Dump failed, maxsize can't be negative.", new Object[0]);
            }
            if (l3.longValue() == 0) {
                l3 = 4611686018427387903L;
            }
        }
        Instant time = parseTime(str3, "begin");
        Instant time2 = parseTime(str4, AsmConstants.END);
        if (time != null && time2 != null && time2.isBefore(time)) {
            throw new DCmdException("Dump failed, begin must preceed end.", new Object[0]);
        }
        if (l2 != null) {
            time = Instant.now().minus((TemporalAmount) Duration.ofNanos(l2.longValue()));
        }
        Recording recordingFindRecording = null;
        if (str != null) {
            recordingFindRecording = findRecording(str);
        }
        PlatformRecorder platformRecorder = PrivateAccess.getInstance().getPlatformRecorder();
        try {
            synchronized (platformRecorder) {
                dump(platformRecorder, recordingFindRecording, str, str2, l3, bool, time, time2);
            }
            return getResult();
        } catch (IOException | InvalidPathException e2) {
            throw new DCmdException("Dump failed. Could not copy recording data. %s", e2.getMessage());
        }
    }

    public void dump(PlatformRecorder platformRecorder, Recording recording, String str, String str2, Long l2, Boolean bool, Instant instant, Instant instant2) throws DCmdException, IOException {
        PlatformRecording platformRecordingNewSnapShot = newSnapShot(platformRecorder, recording, bool);
        Throwable th = null;
        try {
            platformRecordingNewSnapShot.filter(instant, instant2, l2);
            if (platformRecordingNewSnapShot.getChunks().isEmpty()) {
                throw new DCmdException("Dump failed. No data found in the specified interval.", new Object[0]);
            }
            WriteableUserPath writeableUserPath = null;
            if (recording != null) {
                writeableUserPath = PrivateAccess.getInstance().getPlatformRecording(recording).getDestination();
            }
            if (str2 != null || (str2 == null && writeableUserPath == null)) {
                writeableUserPath = new WriteableUserPath(resolvePath(recording, str2).toPath());
            }
            platformRecordingNewSnapShot.dumpStopped(writeableUserPath);
            reportOperationComplete("Dumped", str, new SecuritySupport.SafePath(writeableUserPath.getRealPathText()));
            if (platformRecordingNewSnapShot != null) {
                if (0 != 0) {
                    try {
                        platformRecordingNewSnapShot.close();
                        return;
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                        return;
                    }
                }
                platformRecordingNewSnapShot.close();
            }
        } catch (Throwable th3) {
            if (platformRecordingNewSnapShot != null) {
                if (0 != 0) {
                    try {
                        platformRecordingNewSnapShot.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    platformRecordingNewSnapShot.close();
                }
            }
            throw th3;
        }
    }

    private Instant parseTime(String str, String str2) throws DCmdException {
        if (str == null) {
            return null;
        }
        try {
            return Instant.parse(str);
        } catch (DateTimeParseException e2) {
            try {
                return ZonedDateTime.of(LocalDateTime.parse(str), ZoneId.systemDefault()).toInstant();
            } catch (DateTimeParseException e3) {
                try {
                    LocalTime localTime = LocalTime.parse(str);
                    LocalDate localDateNow = LocalDate.now();
                    Instant instant = ZonedDateTime.of(localDateNow, localTime, ZoneId.systemDefault()).toInstant();
                    Instant instantNow = Instant.now();
                    if (instant.isAfter(instantNow) && !instant.isBefore(instantNow.plusSeconds(3600L))) {
                        localDateNow = localDateNow.minusDays(1L);
                    }
                    return ZonedDateTime.of(localDateNow, localTime, ZoneId.systemDefault()).toInstant();
                } catch (DateTimeParseException e4) {
                    if (str.startsWith(LanguageTag.SEP)) {
                        try {
                            return Instant.now().minus((TemporalAmount) Duration.ofNanos(Utils.parseTimespan(str.substring(1))));
                        } catch (NumberFormatException e5) {
                            throw new DCmdException("Dump failed, not a valid %s time.", str2);
                        }
                    }
                    throw new DCmdException("Dump failed, not a valid %s time.", str2);
                }
            }
        }
    }

    private PlatformRecording newSnapShot(PlatformRecorder platformRecorder, Recording recording, Boolean bool) throws DCmdException, IOException {
        if (recording == null) {
            PlatformRecording platformRecordingNewTemporaryRecording = platformRecorder.newTemporaryRecording();
            platformRecorder.fillWithRecordedData(platformRecordingNewTemporaryRecording, bool);
            return platformRecordingNewTemporaryRecording;
        }
        return PrivateAccess.getInstance().getPlatformRecording(recording).newSnapshotClone("Dumped by user", bool);
    }
}
