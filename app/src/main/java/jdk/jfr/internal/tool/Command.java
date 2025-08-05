package jdk.jfr.internal.tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import net.lingala.zip4j.util.InternalZipConstants;
import org.icepdf.core.util.PdfOps;

/* loaded from: jfr.jar:jdk/jfr/internal/tool/Command.class */
abstract class Command {
    public static final String title = "Tool for working with Flight Recorder files (.jfr)";
    private static final Command HELP = new Help();
    private static final List<Command> COMMANDS = createCommands();

    public abstract String getName();

    public abstract String getDescription();

    public abstract void execute(Deque<String> deque) throws UserSyntaxException, UserDataException;

    Command() {
    }

    private static List<Command> createCommands() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new Print());
        arrayList.add(new Metadata());
        arrayList.add(new Summary());
        arrayList.add(new Assemble());
        arrayList.add(new Disassemble());
        arrayList.add(new Version());
        arrayList.add(HELP);
        return Collections.unmodifiableList(arrayList);
    }

    static void displayHelp() {
        System.out.println(title);
        System.out.println();
        displayAvailableCommands(System.out);
    }

    protected String getTitle() {
        return getDescription();
    }

    static void displayAvailableCommands(PrintStream printStream) {
        boolean z2 = true;
        for (Command command : COMMANDS) {
            if (!z2) {
                System.out.println();
            }
            displayCommand(printStream, command);
            printStream.println("     " + command.getDescription());
            z2 = false;
        }
    }

    protected static void displayCommand(PrintStream printStream, Command command) {
        boolean z2 = true;
        String strBuildAlias = buildAlias(command);
        String str = " jfr " + command.getName();
        for (String str2 : command.getOptionSyntax()) {
            if (z2) {
                if (str2.length() != 0) {
                    printStream.println(str + " " + str2 + strBuildAlias);
                } else {
                    printStream.println(str + strBuildAlias);
                }
            } else {
                for (int i2 = 0; i2 < str.length(); i2++) {
                    printStream.print(" ");
                }
                printStream.println(" " + str2);
            }
            z2 = false;
        }
    }

    private static String buildAlias(Command command) {
        List<String> aliases = command.getAliases();
        if (aliases.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if (aliases.size() == 1) {
            sb.append(" (alias ");
            sb.append(aliases.get(0));
            sb.append(")");
            return sb.toString();
        }
        sb.append(" (aliases ");
        for (int i2 = 0; i2 < aliases.size(); i2++) {
            sb.append(aliases.get(i2));
            if (i2 < aliases.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public static List<Command> getCommands() {
        return COMMANDS;
    }

    public static Command valueOf(String str) {
        for (Command command : COMMANDS) {
            if (command.getName().equals(str)) {
                return command;
            }
        }
        return null;
    }

    public List<String> getOptionSyntax() {
        return Collections.singletonList("");
    }

    public void displayOptionUsage(PrintStream printStream) {
    }

    protected boolean acceptOption(Deque<String> deque, String str) throws UserSyntaxException {
        if (str.equals(deque.peek())) {
            if (deque.size() < 2) {
                throw new UserSyntaxException("missing value for " + deque.peek());
            }
            deque.remove();
            return true;
        }
        return false;
    }

    protected void warnForWildcardExpansion(String str, String str2) throws UserDataException {
        try {
            if (!str2.contains(File.pathSeparator) && !Files.exists(Paths.get(".", str2), new LinkOption[0])) {
            } else {
                throw new UserDataException("wildcards should be quoted, for example " + str + " \"Foo*\"");
            }
        } catch (InvalidPathException e2) {
        }
    }

    protected boolean acceptFilterOption(Deque<String> deque, String str) throws UserSyntaxException {
        if (!acceptOption(deque, str)) {
            return false;
        }
        if (deque.isEmpty()) {
            throw new UserSyntaxException("missing filter after " + str);
        }
        if (deque.peek().startsWith("--")) {
            throw new UserSyntaxException("missing filter after " + str);
        }
        return true;
    }

    protected final void ensureMaxArgumentCount(Deque<String> deque, int i2) throws UserSyntaxException {
        if (deque.size() > i2) {
            throw new UserSyntaxException("too many arguments");
        }
    }

    protected final void ensureMinArgumentCount(Deque<String> deque, int i2) throws UserSyntaxException {
        if (deque.size() < i2) {
            throw new UserSyntaxException("too few arguments");
        }
    }

    protected final Path getDirectory(String str) throws UserDataException {
        try {
            Path absolutePath = Paths.get(str, new String[0]).toAbsolutePath();
            if (!Files.exists(absolutePath, new LinkOption[0])) {
                throw new UserDataException("directory does not exist, " + str);
            }
            if (!Files.isDirectory(absolutePath, new LinkOption[0])) {
                throw new UserDataException("path must be directory, " + str);
            }
            return absolutePath;
        } catch (InvalidPathException e2) {
            throw new UserDataException("invalid path '" + str + PdfOps.SINGLE_QUOTE_TOKEN);
        }
    }

    protected final Path getJFRInputFile(Deque<String> deque) throws UserSyntaxException, UserDataException {
        if (deque.isEmpty()) {
            throw new UserSyntaxException("missing file");
        }
        String strRemoveLast = deque.removeLast();
        if (strRemoveLast.startsWith("--")) {
            throw new UserSyntaxException("missing file");
        }
        try {
            Path absolutePath = Paths.get(strRemoveLast, new String[0]).toAbsolutePath();
            ensureAccess(absolutePath);
            ensureJFRFile(absolutePath);
            return absolutePath;
        } catch (IOError e2) {
            throw new UserDataException("i/o error reading file '" + strRemoveLast + "', " + e2.getMessage());
        } catch (InvalidPathException e3) {
            throw new UserDataException("invalid path '" + strRemoveLast + PdfOps.SINGLE_QUOTE_TOKEN);
        }
    }

    /* JADX WARN: Failed to calculate best type for var: r7v2 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r8v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException
     */
    /* JADX WARN: Not initialized variable reg: 7, insn: 0x006b: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r7 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) A[TRY_LEAVE], block:B:19:0x006b */
    /* JADX WARN: Not initialized variable reg: 8, insn: 0x006f: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r8 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:21:0x006f */
    /* JADX WARN: Type inference failed for: r7v2, types: [java.io.RandomAccessFile] */
    /* JADX WARN: Type inference failed for: r8v0, types: [java.lang.Throwable] */
    private void ensureAccess(Path path) throws UserDataException {
        try {
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(path.toFile(), InternalZipConstants.READ_MODE);
                Throwable th = null;
                if (randomAccessFile.length() == 0) {
                    throw new UserDataException("file is empty '" + ((Object) path) + PdfOps.SINGLE_QUOTE_TOKEN);
                }
                randomAccessFile.read();
                if (randomAccessFile != null) {
                    if (0 != 0) {
                        try {
                            randomAccessFile.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        randomAccessFile.close();
                    }
                }
            } finally {
            }
        } catch (FileNotFoundException e2) {
            throw new UserDataException("could not open file " + e2.getMessage());
        } catch (IOException e3) {
            throw new UserDataException("i/o error reading file '" + ((Object) path) + "', " + e3.getMessage());
        }
    }

    protected final void couldNotReadError(Path path, IOException iOException) throws UserDataException {
        throw new UserDataException("could not read recording at " + ((Object) path.toAbsolutePath()) + ". " + iOException.getMessage());
    }

    protected final Path ensureFileDoesNotExist(Path path) throws UserDataException {
        if (Files.exists(path, new LinkOption[0])) {
            throw new UserDataException("file '" + ((Object) path) + "' already exists");
        }
        return path;
    }

    protected final void ensureJFRFile(Path path) throws UserDataException {
        if (!path.toString().endsWith(".jfr")) {
            throw new UserDataException("filename must end with '.jfr'");
        }
    }

    protected void displayUsage(PrintStream printStream) {
        displayCommand(printStream, this);
        printStream.println();
        displayOptionUsage(printStream);
    }

    protected final void println() {
        System.out.println();
    }

    protected final void print(String str) {
        System.out.print(str);
    }

    protected final void println(String str) {
        System.out.println(str);
    }

    protected final boolean matches(String str) {
        Iterator<String> it = getNames().iterator();
        while (it.hasNext()) {
            if (it.next().equals(str)) {
                return true;
            }
        }
        return false;
    }

    protected List<String> getAliases() {
        return Collections.emptyList();
    }

    public List<String> getNames() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(getName());
        arrayList.addAll(getAliases());
        return arrayList;
    }
}
