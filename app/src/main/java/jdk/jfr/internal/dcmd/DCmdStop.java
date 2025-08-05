package jdk.jfr.internal.dcmd;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import jdk.jfr.Recording;
import jdk.jfr.internal.LogLevel;
import jdk.jfr.internal.LogTag;
import jdk.jfr.internal.Logger;
import jdk.jfr.internal.SecuritySupport;

/* loaded from: jfr.jar:jdk/jfr/internal/dcmd/DCmdStop.class */
final class DCmdStop extends AbstractDCmd {
    DCmdStop() {
    }

    public String execute(String str, String str2) throws DCmdException {
        if (Logger.shouldLog(LogTag.JFR_DCMD, LogLevel.DEBUG)) {
            Logger.log(LogTag.JFR_DCMD, LogLevel.DEBUG, "Executing DCmdStart: name=" + str + ", filename=" + str2);
        }
        try {
            SecuritySupport.SafePath safePathResolvePath = null;
            Recording recordingFindRecording = findRecording(str);
            if (str2 != null) {
                try {
                    safePathResolvePath = resolvePath(null, str2);
                    recordingFindRecording.setDestination(Paths.get(str2, new String[0]));
                } catch (IOException | InvalidPathException e2) {
                    throw new DCmdException("Failed to stop %s. Could not set destination for \"%s\" to file %s", recordingFindRecording.getName(), str2, e2.getMessage());
                }
            }
            recordingFindRecording.stop();
            reportOperationComplete("Stopped", recordingFindRecording.getName(), safePathResolvePath);
            recordingFindRecording.close();
            return getResult();
        } catch (InvalidPathException | DCmdException e3) {
            if (str2 != null) {
                throw new DCmdException("Could not write recording \"%s\" to file. %s", str, e3.getMessage());
            }
            throw new DCmdException(e3, "Could not stop recording \"%s\".", str, e3.getMessage());
        }
    }
}
