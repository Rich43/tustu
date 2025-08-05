package jdk.jfr.internal.tool;

import com.sun.media.jfxmediaimpl.NativeMediaPlayer;
import com.sun.xml.internal.ws.addressing.W3CAddressingConstants;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import jdk.jfr.EventType;
import jdk.jfr.internal.Type;
import jdk.jfr.internal.consumer.ChunkHeader;
import jdk.jfr.internal.consumer.RecordingInput;

/* loaded from: jfr.jar:jdk/jfr/internal/tool/Summary.class */
final class Summary extends Command {
    private final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withLocale(Locale.UK).withZone(ZoneOffset.UTC);

    Summary() {
    }

    @Override // jdk.jfr.internal.tool.Command
    public String getName() {
        return "summary";
    }

    /* loaded from: jfr.jar:jdk/jfr/internal/tool/Summary$Statistics.class */
    private static class Statistics {
        String name;
        long count;
        long size;

        Statistics(String str) {
            this.name = str;
        }
    }

    @Override // jdk.jfr.internal.tool.Command
    public List<String> getOptionSyntax() {
        return Collections.singletonList("<file>");
    }

    @Override // jdk.jfr.internal.tool.Command
    public void displayOptionUsage(PrintStream printStream) {
        printStream.println("  <file>   Location of the recording file (.jfr) to display information about");
    }

    @Override // jdk.jfr.internal.tool.Command
    public String getDescription() {
        return "Display general information about a recording file (.jfr)";
    }

    @Override // jdk.jfr.internal.tool.Command
    public void execute(Deque<String> deque) throws UserSyntaxException, UserDataException {
        ensureMaxArgumentCount(deque, 1);
        Path jFRInputFile = getJFRInputFile(deque);
        try {
            printInformation(jFRInputFile);
        } catch (IOException e2) {
            couldNotReadError(jFRInputFile, e2);
        }
    }

    private void printInformation(Path path) throws IOException {
        long durationNanos = 0;
        long j2 = 0;
        RecordingInput recordingInput = new RecordingInput(path.toFile());
        Throwable th = null;
        try {
            try {
                ChunkHeader chunkHeader = new ChunkHeader(recordingInput);
                ChunkHeader chunkHeaderNextHeader = chunkHeader;
                String str = Type.EVENT_NAME_PREFIX;
                if (chunkHeader.getMajor() == 1) {
                    str = "com.oracle.jdk.";
                }
                HashMap map = new HashMap();
                map.put(0L, new Statistics(str + W3CAddressingConstants.WSA_METADATA_NAME));
                map.put(1L, new Statistics(str + "CheckPoint"));
                int iMax = 0;
                while (true) {
                    long end = chunkHeaderNextHeader.getEnd();
                    for (EventType eventType : chunkHeaderNextHeader.readMetadata().getEventTypes()) {
                        map.computeIfAbsent(Long.valueOf(eventType.getId()), l2 -> {
                            return new Statistics(eventType.getName());
                        });
                        iMax = Math.max(iMax, eventType.getName().length());
                    }
                    durationNanos += chunkHeaderNextHeader.getDurationNanos();
                    j2++;
                    recordingInput.position(chunkHeaderNextHeader.getEventStart());
                    while (recordingInput.position() < end) {
                        long jPosition = recordingInput.position();
                        int i2 = recordingInput.readInt();
                        Statistics statistics = (Statistics) map.get(Long.valueOf(recordingInput.readLong()));
                        if (statistics != null) {
                            statistics.count++;
                            statistics.size += i2;
                        }
                        recordingInput.position(jPosition + i2);
                    }
                    if (chunkHeaderNextHeader.isLastChunk()) {
                        break;
                    } else {
                        chunkHeaderNextHeader = chunkHeaderNextHeader.nextHeader();
                    }
                }
                println();
                long startNanos = chunkHeader.getStartNanos() / NativeMediaPlayer.ONE_SECOND;
                long startNanos2 = chunkHeader.getStartNanos() - (startNanos * NativeMediaPlayer.ONE_SECOND);
                println(" Version: " + ((int) chunkHeader.getMajor()) + "." + ((int) chunkHeader.getMinor()));
                println(" Chunks: " + j2);
                println(" Start: " + this.DATE_FORMAT.format(Instant.ofEpochSecond(startNanos, startNanos2)) + " (UTC)");
                println(" Duration: " + ((durationNanos + 500000000) / NativeMediaPlayer.ONE_SECOND) + " s");
                ArrayList<Statistics> arrayList = new ArrayList(map.values());
                Collections.sort(arrayList, (statistics2, statistics3) -> {
                    return Long.compare(statistics3.count, statistics2.count);
                });
                println();
                int iMax2 = Math.max(iMax, " Event Type".length());
                println(" Event Type" + pad(iMax2 - " Event Type".length(), ' ') + "      Count  Size (bytes) ");
                println(pad(iMax2 + "      Count  Size (bytes) ".length(), '='));
                for (Statistics statistics4 : arrayList) {
                    System.out.printf(" %-" + iMax2 + "s%10d  %12d\n", statistics4.name, Long.valueOf(statistics4.count), Long.valueOf(statistics4.size));
                }
                if (recordingInput != null) {
                    if (0 != 0) {
                        try {
                            recordingInput.close();
                            return;
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                            return;
                        }
                    }
                    recordingInput.close();
                }
            } catch (Throwable th3) {
                th = th3;
                throw th3;
            }
        } catch (Throwable th4) {
            if (recordingInput != null) {
                if (th != null) {
                    try {
                        recordingInput.close();
                    } catch (Throwable th5) {
                        th.addSuppressed(th5);
                    }
                } else {
                    recordingInput.close();
                }
            }
            throw th4;
        }
    }

    private String pad(int i2, char c2) {
        StringBuilder sb = new StringBuilder();
        for (int i3 = 0; i3 < i2; i3++) {
            sb.append(c2);
        }
        return sb.toString();
    }
}
