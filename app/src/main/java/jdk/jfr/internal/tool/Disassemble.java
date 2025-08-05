package jdk.jfr.internal.tool;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import jdk.jfr.internal.consumer.ChunkHeader;
import jdk.jfr.internal.consumer.RecordingInput;

/* loaded from: jfr.jar:jdk/jfr/internal/tool/Disassemble.class */
final class Disassemble extends Command {
    Disassemble() {
    }

    @Override // jdk.jfr.internal.tool.Command
    public String getName() {
        return "disassemble";
    }

    @Override // jdk.jfr.internal.tool.Command
    public List<String> getOptionSyntax() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("[--output <directory>]");
        arrayList.add("[--max-chunks <chunks>]");
        arrayList.add("[--max-size <size>]");
        arrayList.add("<file>");
        return arrayList;
    }

    @Override // jdk.jfr.internal.tool.Command
    public void displayOptionUsage(PrintStream printStream) {
        printStream.println(" --output <directory>    The location to write the disassembled file,");
        printStream.println("                         by default the current directory");
        printStream.println("");
        printStream.println(" --max-chunks <chunks>   Maximum number of chunks per disassembled file,");
        printStream.println("                         by default 5. The chunk size varies, but is ");
        printStream.println("                         typically around 15 MB.");
        printStream.println("");
        printStream.println(" --max-size <size>       Maximum number of bytes per file.");
        printStream.println("");
        printStream.println("  <file>                 Location of the recording file (.jfr)");
    }

    @Override // jdk.jfr.internal.tool.Command
    public String getDescription() {
        return "Disassamble a recording file into smaller files/chunks";
    }

    @Override // jdk.jfr.internal.tool.Command
    public void execute(Deque<String> deque) throws UserSyntaxException, UserDataException {
        if (deque.isEmpty()) {
            throw new UserSyntaxException("missing file");
        }
        Path jFRInputFile = getJFRInputFile(deque);
        int i2 = Integer.MAX_VALUE;
        int i3 = Integer.MAX_VALUE;
        String property = System.getProperty("user.dir");
        int size = deque.size();
        while (true) {
            int i4 = size;
            if (i4 > 0) {
                if (acceptOption(deque, "--output")) {
                    property = deque.pop();
                }
                if (acceptOption(deque, "--max-size")) {
                    try {
                        i3 = Integer.parseInt(deque.pop());
                        if (i3 < 1) {
                            throw new UserDataException("max size must be at least 1");
                        }
                    } catch (NumberFormatException e2) {
                        throw new UserDataException("not a valid value for --max-size.");
                    }
                }
                if (acceptOption(deque, "--max-chunks")) {
                    try {
                        i2 = Integer.parseInt(deque.pop());
                        if (i2 < 1) {
                            throw new UserDataException("max chunks must be at least 1.");
                        }
                    } catch (NumberFormatException e3) {
                        throw new UserDataException("not a valid value for --max-size.");
                    }
                }
                if (i4 == deque.size()) {
                    throw new UserSyntaxException("unknown option " + deque.peek());
                }
                size = deque.size();
            } else {
                Path directory = getDirectory(property);
                println();
                println("Examining recording " + ((Object) jFRInputFile) + " ...");
                if (i3 != Integer.MAX_VALUE && i2 == Integer.MAX_VALUE) {
                    try {
                        long size2 = Files.size(jFRInputFile);
                        if (i3 >= size2) {
                            println();
                            println("File size (" + size2 + ") does not exceed max size (" + i3 + ")");
                            return;
                        }
                    } catch (IOException e4) {
                        throw new UserDataException("unexpected i/o error when determining file size" + e4.getMessage());
                    }
                }
                if (i3 == Integer.MAX_VALUE && i2 == Integer.MAX_VALUE) {
                    i2 = 5;
                }
                try {
                    List<Long> listFindChunkSizes = findChunkSizes(jFRInputFile);
                    if ((i3 == Integer.MAX_VALUE) == (listFindChunkSizes.size() <= i2)) {
                        throw new UserDataException("number of chunks in recording (" + listFindChunkSizes.size() + ") doesn't exceed max chunks (" + i2 + ")");
                    }
                    println();
                    if (listFindChunkSizes.size() > 0) {
                        List<Long> listCombineChunkSizes = combineChunkSizes(listFindChunkSizes, i2, i3);
                        print("File consists of " + listFindChunkSizes.size() + " chunks. The recording will be split into ");
                        println(listCombineChunkSizes.size() + " files");
                        println();
                        splitFile(directory, jFRInputFile, listCombineChunkSizes);
                        return;
                    }
                    throw new UserDataException("no JFR chunks found in file.");
                } catch (IOException e5) {
                    throw new UserDataException("unexpected i/o error. " + e5.getMessage());
                }
            }
        }
    }

    private List<Long> findChunkSizes(Path path) throws IOException {
        RecordingInput recordingInput = new RecordingInput(path.toFile());
        Throwable th = null;
        try {
            try {
                ArrayList arrayList = new ArrayList();
                ChunkHeader chunkHeader = new ChunkHeader(recordingInput);
                arrayList.add(Long.valueOf(chunkHeader.getSize()));
                while (!chunkHeader.isLastChunk()) {
                    chunkHeader = chunkHeader.nextHeader();
                    arrayList.add(Long.valueOf(chunkHeader.getSize()));
                }
                if (recordingInput != null) {
                    if (0 != 0) {
                        try {
                            recordingInput.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        recordingInput.close();
                    }
                }
                return arrayList;
            } finally {
            }
        } catch (Throwable th3) {
            if (recordingInput != null) {
                if (th != null) {
                    try {
                        recordingInput.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    recordingInput.close();
                }
            }
            throw th3;
        }
    }

    private List<Long> combineChunkSizes(List<Long> list, int i2, long j2) {
        ArrayList arrayList = new ArrayList();
        int i3 = 1;
        long jLongValue = list.get(0).longValue();
        for (int i4 = 1; i4 < list.size(); i4++) {
            long jLongValue2 = list.get(i4).longValue();
            if (jLongValue + jLongValue2 > j2) {
                arrayList.add(Long.valueOf(jLongValue));
                i3 = 1;
                jLongValue = jLongValue2;
            } else {
                jLongValue += jLongValue2;
                if (i3 == i2) {
                    arrayList.add(Long.valueOf(jLongValue));
                    jLongValue = 0;
                    i3 = 1;
                } else {
                    i3++;
                }
            }
        }
        if (jLongValue != 0) {
            arrayList.add(Long.valueOf(jLongValue));
        }
        return arrayList;
    }

    private void splitFile(Path path, Path path2, List<Long> list) throws UserDataException {
        int length = String.valueOf(list.size() - 1).length();
        String string = path2.getFileName().toString();
        String str = ((Object) string.subSequence(0, string.length() - 4)) + "_%0" + length + "d.jfr";
        for (int i2 = 0; i2 < list.size(); i2++) {
            String str2 = String.format(str, Integer.valueOf(i2));
            try {
                Path pathResolve = path.resolve(str2);
                if (Files.exists(pathResolve, new LinkOption[0])) {
                    throw new UserDataException("can't create disassembled file " + ((Object) pathResolve) + ", a file with that name already exist");
                }
            } catch (InvalidPathException e2) {
                throw new UserDataException("can't construct path with filename" + str2);
            }
        }
        try {
            DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(path2.toFile())));
            Throwable th = null;
            for (int i3 = 0; i3 < list.size(); i3++) {
                try {
                    try {
                        byte[] bytes = readBytes(dataInputStream, list.get(i3).intValue());
                        File file = path.resolve(String.format(str, Integer.valueOf(i3))).toFile();
                        println("Writing " + ((Object) file) + " ... " + bytes.length);
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        fileOutputStream.write(bytes);
                        fileOutputStream.close();
                    } finally {
                    }
                } finally {
                }
            }
            if (dataInputStream != null) {
                if (0 != 0) {
                    try {
                        dataInputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    dataInputStream.close();
                }
            }
        } catch (IOException e3) {
            throw new UserDataException("i/o error writing file " + ((Object) path2));
        }
    }

    private byte[] readBytes(InputStream inputStream, int i2) throws UserDataException, IOException {
        byte[] bArr = new byte[i2];
        int i3 = 0;
        while (true) {
            int i4 = i3;
            if (i4 < bArr.length) {
                int i5 = inputStream.read(bArr, i4, bArr.length - i4);
                if (i5 == -1) {
                    throw new UserDataException("unexpected end of data");
                }
                i3 = i4 + i5;
            } else {
                return bArr;
            }
        }
    }
}
