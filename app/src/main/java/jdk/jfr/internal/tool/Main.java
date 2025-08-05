package jdk.jfr.internal.tool;

import java.util.Arrays;
import java.util.LinkedList;
import org.icepdf.core.util.PdfOps;

/* loaded from: jfr.jar:jdk/jfr/internal/tool/Main.class */
public final class Main {
    private static final int EXIT_OK = 0;
    private static final int EXIT_FAILED = 1;
    private static final int EXIT_WRONG_ARGUMENTS = 2;

    /* JADX WARN: Multi-variable type inference failed */
    public static void main(String... strArr) {
        LinkedList linkedList = new LinkedList(Arrays.asList(strArr));
        if (linkedList.isEmpty()) {
            System.out.println(Command.title);
            System.out.println();
            System.out.println("Before using this tool, you must have a recording file.");
            System.out.println("A file can be created by starting a recording from command line:");
            System.out.println();
            System.out.println(" java -XX:StartFlightRecording:filename=recording.jfr,duration=30s ... ");
            System.out.println();
            System.out.println("A recording can also be started on already running Java Virtual Machine:");
            System.out.println();
            System.out.println(" jcmd (to list available pids)");
            System.out.println(" jcmd <pid> JFR.start");
            System.out.println();
            System.out.println("Recording data can be dumped to file using the JFR.dump command:");
            System.out.println();
            System.out.println(" jcmd <pid> JFR.dump filename=recording.jfr");
            System.out.println();
            System.out.println("The contents of the recording can then be printed, for example:");
            System.out.println();
            System.out.println(" jfr print recording.jfr");
            System.out.println();
            System.out.println(" jfr print --events CPULoad,GarbageCollection recording.jfr");
            System.out.println();
            System.out.println(" jfr print --json --events CPULoad recording.jfr");
            System.out.println();
            System.out.println(" jfr print --categories \"GC,JVM,Java*\" recording.jfr");
            System.out.println();
            System.out.println(" jfr print --events \"jdk.*\" --stack-depth 64 recording.jfr");
            System.out.println();
            System.out.println(" jfr summary recording.jfr");
            System.out.println();
            System.out.println(" jfr metadata recording.jfr");
            System.out.println();
            System.out.println("For more information about available commands, use 'jfr help'");
            System.exit(0);
        }
        String str = (String) linkedList.remove();
        for (Command command : Command.getCommands()) {
            if (command.matches(str)) {
                try {
                    command.execute(linkedList);
                    System.exit(0);
                } catch (UserDataException e2) {
                    System.err.println("jfr " + command.getName() + ": " + e2.getMessage());
                    System.exit(1);
                } catch (UserSyntaxException e3) {
                    System.err.println("jfr " + command.getName() + ": " + e3.getMessage());
                    System.err.println();
                    System.err.println("Usage:");
                    System.err.println();
                    command.displayUsage(System.err);
                    System.exit(2);
                } catch (Throwable th) {
                    System.err.println("jfr " + command.getName() + ": unexpected internal error, " + th.getMessage());
                    th.printStackTrace();
                    System.exit(1);
                }
            }
        }
        System.err.println("jfr: unknown command '" + str + PdfOps.SINGLE_QUOTE_TOKEN);
        System.err.println();
        System.err.println("List of available commands:");
        System.err.println();
        Command.displayAvailableCommands(System.err);
        System.exit(2);
    }
}
