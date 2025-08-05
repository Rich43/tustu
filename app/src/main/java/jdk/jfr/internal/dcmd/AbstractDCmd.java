package jdk.jfr.internal.dcmd;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import jdk.jfr.FlightRecorder;
import jdk.jfr.Recording;
import jdk.jfr.internal.JVM;
import jdk.jfr.internal.SecuritySupport;
import jdk.jfr.internal.Utils;
import org.icepdf.core.util.PdfOps;

/* loaded from: jfr.jar:jdk/jfr/internal/dcmd/AbstractDCmd.class */
abstract class AbstractDCmd {
    private final StringWriter result = new StringWriter();
    private final PrintWriter log = new PrintWriter(this.result);

    protected AbstractDCmd() {
    }

    protected final FlightRecorder getFlightRecorder() {
        return FlightRecorder.getFlightRecorder();
    }

    public final String getResult() {
        return this.result.toString();
    }

    public String getPid() {
        return JVM.getJVM().getPid();
    }

    protected final SecuritySupport.SafePath resolvePath(Recording recording, String str) throws InvalidPathException {
        if (str == null) {
            return makeGenerated(recording, Paths.get(".", new String[0]));
        }
        Path path = Paths.get(str, new String[0]);
        if (Files.isDirectory(path, new LinkOption[0])) {
            return makeGenerated(recording, path);
        }
        return new SecuritySupport.SafePath(path.toAbsolutePath().normalize());
    }

    private SecuritySupport.SafePath makeGenerated(Recording recording, Path path) {
        return new SecuritySupport.SafePath(path.toAbsolutePath().resolve(Utils.makeFilename(recording)).normalize());
    }

    protected final Recording findRecording(String str) throws DCmdException {
        try {
            return findRecordingById(Integer.parseInt(str));
        } catch (NumberFormatException e2) {
            return findRecordingByName(str);
        }
    }

    protected final void reportOperationComplete(String str, String str2, SecuritySupport.SafePath safePath) {
        print(str);
        print(" recording");
        if (str2 != null) {
            print(" \"" + str2 + PdfOps.DOUBLE_QUOTE__TOKEN);
        }
        if (safePath != null) {
            print(",");
            try {
                print(" ");
                printBytes(SecuritySupport.getFileSize(safePath));
            } catch (IOException e2) {
            }
            println(" written to:", new Object[0]);
            println();
            printPath(safePath);
            return;
        }
        println(".", new Object[0]);
    }

    protected final List<Recording> getRecordings() {
        ArrayList arrayList = new ArrayList(getFlightRecorder().getRecordings());
        Collections.sort(arrayList, Comparator.comparing((v0) -> {
            return v0.getId();
        }));
        return arrayList;
    }

    static String quoteIfNeeded(String str) {
        if (str.contains(" ")) {
            return "\\\"" + str + "\\\"";
        }
        return str;
    }

    protected final void println() {
        this.log.println();
    }

    protected final void print(String str) {
        this.log.print(str);
    }

    protected final void print(String str, Object... objArr) {
        this.log.printf(str, objArr);
    }

    protected final void println(String str, Object... objArr) {
        print(str, objArr);
        println();
    }

    protected final void printBytes(long j2) {
        print(Utils.formatBytes(j2));
    }

    protected final void printTimespan(Duration duration, String str) {
        print(Utils.formatTimespan(duration, str));
    }

    protected final void printPath(SecuritySupport.SafePath safePath) {
        if (safePath == null) {
            print("N/A");
            return;
        }
        try {
            printPath(SecuritySupport.getAbsolutePath(safePath).toPath());
        } catch (IOException e2) {
            printPath(safePath.toPath());
        }
    }

    protected final void printPath(Path path) {
        try {
            println(path.toAbsolutePath().toString(), new Object[0]);
        } catch (SecurityException e2) {
            println(path.toString(), new Object[0]);
        }
    }

    private Recording findRecordingById(int i2) throws DCmdException {
        for (Recording recording : getFlightRecorder().getRecordings()) {
            if (recording.getId() == i2) {
                return recording;
            }
        }
        throw new DCmdException("Could not find %d.\n\nUse JFR.check without options to see list of all available recordings.", Integer.valueOf(i2));
    }

    private Recording findRecordingByName(String str) throws DCmdException {
        for (Recording recording : getFlightRecorder().getRecordings()) {
            if (str.equals(recording.getName())) {
                return recording;
            }
        }
        throw new DCmdException("Could not find %s.\n\nUse JFR.check without options to see list of all available recordings.", str);
    }
}
