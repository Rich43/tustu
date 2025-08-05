package jdk.jfr.internal.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import jdk.jfr.consumer.RecordingFile;
import jdk.jfr.internal.Type;
import jdk.jfr.internal.consumer.RecordingInternals;

/* loaded from: jfr.jar:jdk/jfr/internal/tool/Metadata.class */
final class Metadata extends Command {
    Metadata() {
    }

    /* loaded from: jfr.jar:jdk/jfr/internal/tool/Metadata$TypeComparator.class */
    private static class TypeComparator implements Comparator<Type> {
        private TypeComparator() {
        }

        @Override // java.util.Comparator
        public int compare(Type type, Type type2) {
            if (groupValue(type) == groupValue(type2)) {
                String name = type.getName();
                String name2 = type2.getName();
                String strSubstring = name.substring(0, name.lastIndexOf(46) + 1);
                String strSubstring2 = name2.substring(0, name2.lastIndexOf(46) + 1);
                if (strSubstring.equals(strSubstring2)) {
                    return name.compareTo(name2);
                }
                if (Type.SUPER_TYPE_EVENT.equals(type.getSuperType()) && !strSubstring.equals(strSubstring2)) {
                    if (strSubstring.equals("jdk.jfr")) {
                        return -1;
                    }
                    if (strSubstring2.equals("jdk.jfr")) {
                        return 1;
                    }
                }
                return strSubstring.compareTo(strSubstring2);
            }
            return Integer.compare(groupValue(type), groupValue(type2));
        }

        int groupValue(Type type) {
            String superType = type.getSuperType();
            if (superType == null) {
                return 1;
            }
            if (Type.SUPER_TYPE_ANNOTATION.equals(superType)) {
                return 3;
            }
            if (Type.SUPER_TYPE_SETTING.equals(superType)) {
                return 4;
            }
            if (Type.SUPER_TYPE_EVENT.equals(superType)) {
                return 5;
            }
            return 2;
        }
    }

    @Override // jdk.jfr.internal.tool.Command
    public String getName() {
        return "metadata";
    }

    @Override // jdk.jfr.internal.tool.Command
    public List<String> getOptionSyntax() {
        return Collections.singletonList("<file>");
    }

    @Override // jdk.jfr.internal.tool.Command
    public String getDescription() {
        return "Display event metadata, such as labels, descriptions and field layout";
    }

    @Override // jdk.jfr.internal.tool.Command
    public void execute(Deque<String> deque) throws UserSyntaxException, UserDataException {
        Path jFRInputFile = getJFRInputFile(deque);
        boolean z2 = false;
        int size = deque.size();
        while (true) {
            int i2 = size;
            if (i2 > 0) {
                if (acceptOption(deque, "--ids")) {
                    z2 = true;
                }
                if (i2 == deque.size()) {
                    throw new UserSyntaxException("unknown option " + deque.peek());
                }
                size = deque.size();
            } else {
                PrintWriter printWriter = new PrintWriter(System.out);
                Throwable th = null;
                try {
                    PrettyWriter prettyWriter = new PrettyWriter(printWriter);
                    prettyWriter.setShowIds(z2);
                    try {
                        RecordingFile recordingFile = new RecordingFile(jFRInputFile);
                        Throwable th2 = null;
                        try {
                            try {
                                List<Type> types = RecordingInternals.INSTANCE.readTypes(recordingFile);
                                Collections.sort(types, new TypeComparator());
                                Iterator<Type> it = types.iterator();
                                while (it.hasNext()) {
                                    prettyWriter.printType(it.next());
                                }
                                prettyWriter.flush(true);
                                if (recordingFile != null) {
                                    if (0 != 0) {
                                        try {
                                            recordingFile.close();
                                        } catch (Throwable th3) {
                                            th2.addSuppressed(th3);
                                        }
                                    } else {
                                        recordingFile.close();
                                    }
                                }
                            } catch (Throwable th4) {
                                if (recordingFile != null) {
                                    if (th2 != null) {
                                        try {
                                            recordingFile.close();
                                        } catch (Throwable th5) {
                                            th2.addSuppressed(th5);
                                        }
                                    } else {
                                        recordingFile.close();
                                    }
                                }
                                throw th4;
                            }
                        } catch (Throwable th6) {
                            th2 = th6;
                            throw th6;
                        }
                    } catch (IOException e2) {
                        couldNotReadError(jFRInputFile, e2);
                    }
                    if (printWriter != null) {
                        if (0 != 0) {
                            try {
                                printWriter.close();
                                return;
                            } catch (Throwable th7) {
                                th.addSuppressed(th7);
                                return;
                            }
                        }
                        printWriter.close();
                        return;
                    }
                    return;
                } catch (Throwable th8) {
                    if (printWriter != null) {
                        if (0 != 0) {
                            try {
                                printWriter.close();
                            } catch (Throwable th9) {
                                th.addSuppressed(th9);
                            }
                        } else {
                            printWriter.close();
                        }
                    }
                    throw th8;
                }
            }
        }
    }
}
