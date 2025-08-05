package jdk.jfr.internal.tool;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import jdk.jfr.EventType;

/* loaded from: jfr.jar:jdk/jfr/internal/tool/Print.class */
final class Print extends Command {
    Print() {
    }

    @Override // jdk.jfr.internal.tool.Command
    public String getName() {
        return "print";
    }

    @Override // jdk.jfr.internal.tool.Command
    public List<String> getOptionSyntax() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("[--xml|--json]");
        arrayList.add("[--categories <filter>]");
        arrayList.add("[--events <filter>]");
        arrayList.add("[--stack-depth <depth>]");
        arrayList.add("<file>");
        return arrayList;
    }

    @Override // jdk.jfr.internal.tool.Command
    protected String getTitle() {
        return "Print contents of a recording file";
    }

    @Override // jdk.jfr.internal.tool.Command
    public String getDescription() {
        return getTitle() + ". See 'jfr help print' for details.";
    }

    @Override // jdk.jfr.internal.tool.Command
    public void displayOptionUsage(PrintStream printStream) {
        printStream.println("  --xml                   Print recording in XML format");
        printStream.println();
        printStream.println("  --json                  Print recording in JSON format");
        printStream.println();
        printStream.println("  --categories <filter>   Select events matching a category name.");
        printStream.println("                          The filter is a comma-separated list of names,");
        printStream.println("                          simple and/or qualified, and/or quoted glob patterns");
        printStream.println();
        printStream.println("  --events <filter>       Select events matching an event name.");
        printStream.println("                          The filter is a comma-separated list of names,");
        printStream.println("                          simple and/or qualified, and/or quoted glob patterns");
        printStream.println();
        printStream.println("  --stack-depth <depth>   Number of frames in stack traces, by default 5");
        printStream.println();
        printStream.println("  <file>                  Location of the recording file (.jfr)");
        printStream.println();
        printStream.println();
        printStream.println("Example usage:");
        printStream.println();
        printStream.println(" jfr print --events OldObjectSample recording.jfr");
        printStream.println();
        printStream.println(" jfr print --events CPULoad,GarbageCollection recording.jfr");
        printStream.println();
        printStream.println(" jfr print --categories \"GC,JVM,Java*\" recording.jfr");
        printStream.println();
        printStream.println(" jfr print --events \"jdk.*\" --stack-depth 64 recording.jfr");
        printStream.println();
        printStream.println(" jfr print --json --events CPULoad recording.jfr");
    }

    @Override // jdk.jfr.internal.tool.Command
    public void execute(Deque<String> deque) throws UserSyntaxException, UserDataException {
        Path jFRInputFile = getJFRInputFile(deque);
        PrintWriter printWriter = new PrintWriter((OutputStream) System.out, false);
        Predicate<EventType> predicateAddCategoryFilter = null;
        int i2 = 5;
        EventPrintWriter prettyWriter = null;
        int size = deque.size();
        boolean z2 = false;
        boolean z3 = false;
        while (size > 0) {
            if (acceptFilterOption(deque, "--events")) {
                if (z2) {
                    throw new UserSyntaxException("use --events event1,event2,event3 to include multiple events");
                }
                z2 = true;
                String strRemove = deque.remove();
                warnForWildcardExpansion("--events", strRemove);
                predicateAddCategoryFilter = addEventFilter(strRemove, predicateAddCategoryFilter);
            }
            if (acceptFilterOption(deque, "--categories")) {
                if (z3) {
                    throw new UserSyntaxException("use --categories category1,category2 to include multiple categories");
                }
                z3 = true;
                String strRemove2 = deque.remove();
                warnForWildcardExpansion("--categories", strRemove2);
                predicateAddCategoryFilter = addCategoryFilter(strRemove2, predicateAddCategoryFilter);
            }
            if (acceptOption(deque, "--stack-depth")) {
                try {
                    i2 = Integer.parseInt(deque.pop());
                    if (i2 < 0) {
                        throw new UserSyntaxException("stack depth must be zero or a positive integer.");
                    }
                } catch (NumberFormatException e2) {
                    throw new UserSyntaxException("not a valid value for --stack-depth");
                }
            }
            if (acceptFormatterOption(deque, prettyWriter, "--json")) {
                prettyWriter = new JSONWriter(printWriter);
            }
            if (acceptFormatterOption(deque, prettyWriter, "--xml")) {
                prettyWriter = new XMLWriter(printWriter);
            }
            if (size == deque.size()) {
                checkCommonError(deque, "--event", "--events");
                checkCommonError(deque, "--category", "--categories");
                throw new UserSyntaxException("unknown option " + deque.peek());
            }
            size = deque.size();
        }
        if (prettyWriter == null) {
            prettyWriter = new PrettyWriter(printWriter);
        }
        prettyWriter.setStackDepth(i2);
        if (predicateAddCategoryFilter != null) {
            prettyWriter.setEventFilter(addCache(predicateAddCategoryFilter, eventType -> {
                return Long.valueOf(eventType.getId());
            }));
        }
        try {
            prettyWriter.print(jFRInputFile);
        } catch (IOException e3) {
            couldNotReadError(jFRInputFile, e3);
        }
        printWriter.flush();
    }

    private void checkCommonError(Deque<String> deque, String str, String str2) throws UserSyntaxException {
        if (str.equals(deque.peek())) {
            throw new UserSyntaxException("unknown option " + str + ", did you mean " + str2 + "?");
        }
    }

    private static boolean acceptFormatterOption(Deque<String> deque, EventPrintWriter eventPrintWriter, String str) throws UserSyntaxException {
        if (str.equals(deque.peek())) {
            if (eventPrintWriter != null) {
                throw new UserSyntaxException("only one format can be specified at a time");
            }
            deque.remove();
            return true;
        }
        return false;
    }

    private static <T, X> Predicate<T> addCache(Predicate<T> predicate, Function<T, X> function) {
        HashMap map = new HashMap();
        return obj -> {
            return ((Boolean) map.computeIfAbsent(function.apply(obj), obj -> {
                return Boolean.valueOf(predicate.test(obj));
            })).booleanValue();
        };
    }

    private static <T> Predicate<T> recurseIfPossible(Predicate<T> predicate) {
        return obj -> {
            return predicate != null && predicate.test(obj);
        };
    }

    private static Predicate<EventType> addCategoryFilter(String str, Predicate<EventType> predicate) throws UserSyntaxException {
        List<String> listExplodeFilter = explodeFilter(str);
        Predicate<EventType> predicateRecurseIfPossible = recurseIfPossible(eventType -> {
            for (String str2 : eventType.getCategoryNames()) {
                Iterator it = listExplodeFilter.iterator();
                while (it.hasNext()) {
                    String str3 = (String) it.next();
                    if (match(str2, str3)) {
                        return true;
                    }
                    if (str2.contains(" ") && acronomify(str2).equals(str3)) {
                        return true;
                    }
                }
            }
            return false;
        });
        return predicate == null ? predicateRecurseIfPossible : predicate.or(predicateRecurseIfPossible);
    }

    private static String acronomify(String str) {
        boolean zIsWhitespace = true;
        String str2 = "";
        for (char c2 : str.toCharArray()) {
            if (zIsWhitespace && Character.isAlphabetic(c2) && Character.isUpperCase(c2)) {
                str2 = str2 + c2;
            }
            zIsWhitespace = Character.isWhitespace(c2);
        }
        return str2;
    }

    private static Predicate<EventType> addEventFilter(String str, Predicate<EventType> predicate) throws UserSyntaxException {
        List<String> listExplodeFilter = explodeFilter(str);
        Predicate<EventType> predicateRecurseIfPossible = recurseIfPossible(eventType -> {
            Iterator it = listExplodeFilter.iterator();
            while (it.hasNext()) {
                String str2 = (String) it.next();
                String name = eventType.getName();
                if (match(name, str2) || match(name.substring(name.lastIndexOf(".") + 1), str2)) {
                    return true;
                }
            }
            return false;
        });
        return predicate == null ? predicateRecurseIfPossible : predicate.or(predicateRecurseIfPossible);
    }

    private static boolean match(String str, String str2) {
        if (str2.length() == 0) {
            return str.length() == 0;
        }
        if (str2.charAt(0) == '*') {
            String strSubstring = str2.substring(1);
            for (int i2 = 0; i2 <= str.length(); i2++) {
                if (match(str.substring(i2), strSubstring)) {
                    return true;
                }
            }
            return false;
        }
        if (str.length() == 0) {
            return false;
        }
        if (str2.charAt(0) == '?') {
            return match(str.substring(1), str2.substring(1));
        }
        if (str2.charAt(0) == str.charAt(0)) {
            return match(str.substring(1), str2.substring(1));
        }
        return false;
    }

    private static List<String> explodeFilter(String str) throws UserSyntaxException {
        ArrayList arrayList = new ArrayList();
        for (String str2 : str.split(",")) {
            String strTrim = str2.trim();
            if (!strTrim.isEmpty()) {
                arrayList.add(strTrim);
            }
        }
        return arrayList;
    }
}
