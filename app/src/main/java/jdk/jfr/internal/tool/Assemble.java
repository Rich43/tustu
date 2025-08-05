package jdk.jfr.internal.tool;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.channels.FileChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/* loaded from: jfr.jar:jdk/jfr/internal/tool/Assemble.class */
final class Assemble extends Command {
    Assemble() {
    }

    @Override // jdk.jfr.internal.tool.Command
    public String getName() {
        return "assemble";
    }

    @Override // jdk.jfr.internal.tool.Command
    public List<String> getOptionSyntax() {
        return Collections.singletonList("<repository> <file>");
    }

    @Override // jdk.jfr.internal.tool.Command
    public String getDescription() {
        return "Assemble leftover chunks from a disk repository into a recording file";
    }

    @Override // jdk.jfr.internal.tool.Command
    public void displayOptionUsage(PrintStream printStream) {
        printStream.println("  <repository>   Directory where the repository is located");
        printStream.println();
        printStream.println("  <file>         Name of the recording file (.jfr) to create");
    }

    /* JADX WARN: Failed to calculate best type for var: r10v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r9v1 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException
     */
    /* JADX WARN: Not initialized variable reg: 10, insn: 0x00ce: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r10 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:22:0x00ce */
    /* JADX WARN: Not initialized variable reg: 9, insn: 0x00c9: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r9 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) A[TRY_LEAVE], block:B:20:0x00c9 */
    /* JADX WARN: Type inference failed for: r10v0, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r9v1, types: [java.io.FileOutputStream] */
    @Override // jdk.jfr.internal.tool.Command
    public void execute(Deque<String> deque) throws UserSyntaxException, UserDataException {
        ensureMinArgumentCount(deque, 2);
        ensureMaxArgumentCount(deque, 2);
        Path directory = getDirectory(deque.pop());
        Path path = Paths.get(deque.pop(), new String[0]);
        ensureFileDoesNotExist(path);
        ensureJFRFile(path);
        try {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(path.toFile());
                Throwable th = null;
                List<Path> listListJFRFiles = listJFRFiles(directory);
                if (listListJFRFiles.isEmpty()) {
                    throw new UserDataException("no *.jfr files found at " + ((Object) directory));
                }
                println();
                println("Assembling files... ");
                println();
                transferTo(listListJFRFiles, path, fileOutputStream.getChannel());
                println();
                println("Finished.");
                if (fileOutputStream != null) {
                    if (0 != 0) {
                        try {
                            fileOutputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        fileOutputStream.close();
                    }
                }
            } finally {
            }
        } catch (IOException e2) {
            throw new UserDataException("could not open destination file " + ((Object) path) + ". " + e2.getMessage());
        }
    }

    private List<Path> listJFRFiles(Path path) throws UserDataException {
        try {
            ArrayList arrayList = new ArrayList();
            if (Files.isDirectory(path, new LinkOption[0])) {
                DirectoryStream<Path> directoryStreamNewDirectoryStream = Files.newDirectoryStream(path, "*.jfr");
                Throwable th = null;
                try {
                    try {
                        for (Path path2 : directoryStreamNewDirectoryStream) {
                            if (!Files.isDirectory(path2, new LinkOption[0]) && Files.isReadable(path2)) {
                                arrayList.add(path2);
                            }
                        }
                        if (directoryStreamNewDirectoryStream != null) {
                            if (0 != 0) {
                                try {
                                    directoryStreamNewDirectoryStream.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            } else {
                                directoryStreamNewDirectoryStream.close();
                            }
                        }
                    } finally {
                    }
                } finally {
                }
            }
            arrayList.sort((path3, path4) -> {
                return path3.getFileName().compareTo(path4.getFileName());
            });
            return arrayList;
        } catch (IOException e2) {
            throw new UserDataException("could not list *.jfr for directory " + ((Object) path) + ". " + e2.getMessage());
        }
    }

    private void transferTo(List<Path> list, Path path, FileChannel fileChannel) throws UserDataException {
        long j2 = 0;
        for (Path path2 : list) {
            println(" " + path2.toString());
            try {
                FileChannel fileChannelOpen = FileChannel.open(path2, new OpenOption[0]);
                Throwable th = null;
                try {
                    try {
                        long size = Files.size(path2);
                        while (size > 0) {
                            long jTransferFrom = fileChannel.transferFrom(fileChannelOpen, j2, Math.min(size, 1048576L));
                            j2 += jTransferFrom;
                            size -= jTransferFrom;
                        }
                        if (fileChannelOpen != null) {
                            if (0 != 0) {
                                try {
                                    fileChannelOpen.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            } else {
                                fileChannelOpen.close();
                            }
                        }
                    } finally {
                    }
                } finally {
                }
            } catch (IOException e2) {
                throw new UserDataException("could not copy recording chunk " + ((Object) path2) + " to new file. " + e2.getMessage());
            }
        }
    }
}
