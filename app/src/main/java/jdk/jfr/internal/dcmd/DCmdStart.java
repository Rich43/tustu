package jdk.jfr.internal.dcmd;

import com.sun.media.jfxmediaimpl.NativeMediaPlayer;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import jdk.jfr.FlightRecorder;
import jdk.jfr.Recording;
import jdk.jfr.internal.JVM;
import jdk.jfr.internal.LogLevel;
import jdk.jfr.internal.LogTag;
import jdk.jfr.internal.Logger;
import jdk.jfr.internal.OldObjectSample;
import jdk.jfr.internal.PrivateAccess;
import jdk.jfr.internal.SecuritySupport;
import jdk.jfr.internal.Type;
import jdk.jfr.internal.jfc.JFC;
import org.icepdf.core.util.PdfOps;

/* loaded from: jfr.jar:jdk/jfr/internal/dcmd/DCmdStart.class */
final class DCmdStart extends AbstractDCmd {
    DCmdStart() {
    }

    public String execute(String str, String[] strArr, Long l2, Long l3, Boolean bool, String str2, Long l4, Long l5, Boolean bool2, Boolean bool3) throws DCmdException {
        if (Logger.shouldLog(LogTag.JFR_DCMD, LogLevel.DEBUG)) {
            Logger.log(LogTag.JFR_DCMD, LogLevel.DEBUG, "Executing DCmdStart: name=" + str + ", settings=" + (strArr != null ? Arrays.asList(strArr) : "(none)") + ", delay=" + ((Object) l2) + ", duration=" + ((Object) l3) + ", disk=" + ((Object) bool) + ", filename=" + str2 + ", maxage=" + ((Object) l4) + ", maxsize=" + ((Object) l5) + ", dumponexit =" + ((Object) bool2) + ", path-to-gc-roots=" + ((Object) bool3));
        }
        if (str != null) {
            try {
                Integer.parseInt(str);
                throw new DCmdException("Name of recording can't be numeric", new Object[0]);
            } catch (NumberFormatException e2) {
            }
        }
        if (l3 == null && Boolean.FALSE.equals(bool2) && str2 != null) {
            throw new DCmdException("Filename can only be set for a time bound recording or if dumponexit=true. Set duration/dumponexit or omit filename.", new Object[0]);
        }
        if (strArr.length == 1 && strArr[0].length() == 0) {
            throw new DCmdException("No settings specified. Use settings=none to start without any settings", new Object[0]);
        }
        HashMap map = new HashMap();
        for (String str3 : strArr) {
            try {
                map.putAll(JFC.createKnown(str3).getSettings());
            } catch (FileNotFoundException e3) {
                throw new DCmdException("Could not find settings file'" + str3 + PdfOps.SINGLE_QUOTE_TOKEN, e3);
            } catch (IOException | ParseException e4) {
                throw new DCmdException("Could not parse settings file '" + strArr[0] + PdfOps.SINGLE_QUOTE_TOKEN, e4);
            }
        }
        OldObjectSample.updateSettingPathToGcRoots(map, bool3);
        if (l3 != null && l3.longValue() < NativeMediaPlayer.ONE_SECOND) {
            throw new DCmdException("Could not start recording, duration must be at least 1 second.", new Object[0]);
        }
        if (l2 != null && l2.longValue() < NativeMediaPlayer.ONE_SECOND) {
            throw new DCmdException("Could not start recording, delay must be at least 1 second.", new Object[0]);
        }
        if (!FlightRecorder.isInitialized() && l2 == null) {
            initializeWithForcedInstrumentation(map);
        }
        Recording recording = new Recording();
        if (str != null) {
            recording.setName(str);
        }
        if (bool != null) {
            recording.setToDisk(bool.booleanValue());
        }
        recording.setSettings(map);
        SecuritySupport.SafePath safePathResolvePath = null;
        if (str2 != null) {
            if (bool2 == null) {
                try {
                    bool2 = Boolean.TRUE;
                } catch (IOException | InvalidPathException e5) {
                    recording.close();
                    throw new DCmdException("Could not start recording, not able to write to file %s. %s ", str2, e5.getMessage());
                }
            }
            Path path = Paths.get(str2, new String[0]);
            if (Files.isDirectory(path, new LinkOption[0]) && Boolean.TRUE.equals(bool2)) {
                PrivateAccess.getInstance().getPlatformRecording(recording).setDumpOnExitDirectory(new SecuritySupport.SafePath(path));
            } else {
                safePathResolvePath = resolvePath(recording, str2);
                recording.setDestination(safePathResolvePath.toPath());
            }
        }
        if (l4 != null) {
            recording.setMaxAge(Duration.ofNanos(l4.longValue()));
        }
        if (l5 != null) {
            recording.setMaxSize(l5.longValue());
        }
        if (l3 != null) {
            recording.setDuration(Duration.ofNanos(l3.longValue()));
        }
        if (bool2 != null) {
            recording.setDumpOnExit(bool2.booleanValue());
        }
        if (l2 != null) {
            Duration durationOfNanos = Duration.ofNanos(l2.longValue());
            recording.scheduleStart(durationOfNanos);
            print("Recording " + recording.getId() + " scheduled to start in ");
            printTimespan(durationOfNanos, " ");
            print(".");
        } else {
            recording.start();
            print("Started recording " + recording.getId() + ".");
        }
        if (recording.isToDisk() && l3 == null && l4 == null && l5 == null) {
            print(" No limit specified, using maxsize=250MB as default.");
            recording.setMaxSize(262144000L);
        }
        if (safePathResolvePath != null && l3 != null) {
            println(" The result will be written to:", new Object[0]);
            println();
            printPath(safePathResolvePath);
        } else {
            println();
            println();
            String str4 = l3 == null ? "dump" : "stop";
            String str5 = str2 == null ? "filename=FILEPATH " : "";
            String str6 = "name=" + recording.getId();
            if (str != null) {
                str6 = "name=" + quoteIfNeeded(str);
            }
            print("Use jcmd " + getPid() + " JFR." + str4 + " " + str6 + " " + str5 + "to copy recording data to file.");
            println();
        }
        return getResult();
    }

    private void initializeWithForcedInstrumentation(Map<String, String> map) {
        if (!hasJDKEvents(map)) {
            return;
        }
        JVM jvm = JVM.getJVM();
        try {
            jvm.setForceInstrumentation(true);
            FlightRecorder.getFlightRecorder();
        } finally {
            jvm.setForceInstrumentation(false);
        }
    }

    private boolean hasJDKEvents(Map<String, String> map) {
        for (String str : new String[]{"FileRead", "FileWrite", "SocketRead", "SocketWrite", "JavaErrorThrow", "JavaExceptionThrow", "FileForce"}) {
            if ("true".equals(map.get(Type.EVENT_NAME_PREFIX + str + "#enabled"))) {
                return true;
            }
        }
        return false;
    }
}
