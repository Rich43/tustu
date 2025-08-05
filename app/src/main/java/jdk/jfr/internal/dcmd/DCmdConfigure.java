package jdk.jfr.internal.dcmd;

import jdk.jfr.internal.LogLevel;
import jdk.jfr.internal.LogTag;
import jdk.jfr.internal.Logger;
import jdk.jfr.internal.Options;
import jdk.jfr.internal.Repository;
import jdk.jfr.internal.SecuritySupport;

/* loaded from: jfr.jar:jdk/jfr/internal/dcmd/DCmdConfigure.class */
final class DCmdConfigure extends AbstractDCmd {
    DCmdConfigure() {
    }

    public String execute(String str, String str2, Integer num, Long l2, Long l3, Long l4, Long l5, Long l6, Boolean bool) throws IllegalStateException, DCmdException, IllegalArgumentException {
        if (Logger.shouldLog(LogTag.JFR_DCMD, LogLevel.DEBUG)) {
            Logger.log(LogTag.JFR_DCMD, LogLevel.DEBUG, "Executing DCmdConfigure: repositorypath=" + str + ", dumppath=" + str2 + ", stackdepth=" + ((Object) num) + ", globalbuffercount=" + ((Object) l2) + ", globalbuffersize=" + ((Object) l3) + ", thread_buffer_size" + ((Object) l4) + ", memorysize" + ((Object) l5) + ", maxchunksize=" + ((Object) l6) + ", samplethreads" + ((Object) bool));
        }
        boolean z2 = false;
        if (str != null) {
            try {
                Repository.getRepository().setBasePath(new SecuritySupport.SafePath(str));
                Logger.log(LogTag.JFR, LogLevel.INFO, "Base repository path set to " + str);
                printRepositoryPath();
                z2 = true;
            } catch (Exception e2) {
                throw new DCmdException("Could not use " + str + " as repository. " + e2.getMessage(), e2);
            }
        }
        if (str2 != null) {
            Options.setDumpPath(new SecuritySupport.SafePath(str2));
            Logger.log(LogTag.JFR, LogLevel.INFO, "Emergency dump path set to " + str2);
            printDumpPath();
            z2 = true;
        }
        if (num != null) {
            Options.setStackDepth(num);
            Logger.log(LogTag.JFR, LogLevel.INFO, "Stack depth set to " + ((Object) num));
            printStackDepth();
            z2 = true;
        }
        if (l2 != null) {
            Options.setGlobalBufferCount(l2.longValue());
            Logger.log(LogTag.JFR, LogLevel.INFO, "Global buffer count set to " + ((Object) l2));
            printGlobalBufferCount();
            z2 = true;
        }
        if (l3 != null) {
            Options.setGlobalBufferSize(l3.longValue());
            Logger.log(LogTag.JFR, LogLevel.INFO, "Global buffer size set to " + ((Object) l3));
            printGlobalBufferSize();
            z2 = true;
        }
        if (l4 != null) {
            Options.setThreadBufferSize(l4.longValue());
            Logger.log(LogTag.JFR, LogLevel.INFO, "Thread buffer size set to " + ((Object) l4));
            printThreadBufferSize();
            z2 = true;
        }
        if (l5 != null) {
            Options.setMemorySize(l5.longValue());
            Logger.log(LogTag.JFR, LogLevel.INFO, "Memory size set to " + ((Object) l5));
            printMemorySize();
            z2 = true;
        }
        if (l6 != null) {
            Options.setMaxChunkSize(l6.longValue());
            Logger.log(LogTag.JFR, LogLevel.INFO, "Max chunk size set to " + ((Object) l6));
            printMaxChunkSize();
            z2 = true;
        }
        if (bool != null) {
            Options.setSampleThreads(bool);
            Logger.log(LogTag.JFR, LogLevel.INFO, "Sample threads set to " + ((Object) bool));
            printSampleThreads();
            z2 = true;
        }
        if (!z2) {
            println("Current configuration:", new Object[0]);
            println();
            printRepositoryPath();
            printStackDepth();
            printGlobalBufferCount();
            printGlobalBufferSize();
            printThreadBufferSize();
            printMemorySize();
            printMaxChunkSize();
            printSampleThreads();
        }
        return getResult();
    }

    private void printRepositoryPath() {
        print("Repository path: ");
        printPath(Repository.getRepository().getRepositoryPath());
        println();
    }

    private void printDumpPath() {
        print("Dump path: ");
        printPath(Options.getDumpPath());
        println();
    }

    private void printSampleThreads() {
        println("Sample threads: " + Options.getSampleThreads(), new Object[0]);
    }

    private void printStackDepth() {
        println("Stack depth: " + Options.getStackDepth(), new Object[0]);
    }

    private void printGlobalBufferCount() {
        println("Global buffer count: " + Options.getGlobalBufferCount(), new Object[0]);
    }

    private void printGlobalBufferSize() {
        print("Global buffer size: ");
        printBytes(Options.getGlobalBufferSize());
        println();
    }

    private void printThreadBufferSize() {
        print("Thread buffer size: ");
        printBytes(Options.getThreadBufferSize());
        println();
    }

    private void printMemorySize() {
        print("Memory size: ");
        printBytes(Options.getMemorySize());
        println();
    }

    private void printMaxChunkSize() {
        print("Max chunk size: ");
        printBytes(Options.getMaxChunkSize());
        println();
    }
}
