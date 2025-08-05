package jdk.jfr.internal.tool;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import org.icepdf.core.util.PdfOps;

/* loaded from: jfr.jar:jdk/jfr/internal/tool/Help.class */
final class Help extends Command {
    Help() {
    }

    @Override // jdk.jfr.internal.tool.Command
    public String getName() {
        return "help";
    }

    @Override // jdk.jfr.internal.tool.Command
    public List<String> getOptionSyntax() {
        return Collections.singletonList("[<command>]");
    }

    @Override // jdk.jfr.internal.tool.Command
    protected List<String> getAliases() {
        return Arrays.asList("--help", "-h", "-?");
    }

    @Override // jdk.jfr.internal.tool.Command
    public void displayOptionUsage(PrintStream printStream) {
        println("  <command>   The name of the command to get help for");
    }

    @Override // jdk.jfr.internal.tool.Command
    public String getDescription() {
        return "Display all available commands, or help about a specific command";
    }

    @Override // jdk.jfr.internal.tool.Command
    public void execute(Deque<String> deque) throws UserSyntaxException, UserDataException {
        if (deque.isEmpty()) {
            Command.displayHelp();
            return;
        }
        ensureMaxArgumentCount(deque, 1);
        String strRemove = deque.remove();
        Command commandValueOf = Command.valueOf(strRemove);
        if (commandValueOf == null) {
            throw new UserDataException("unknown command '" + strRemove + PdfOps.SINGLE_QUOTE_TOKEN);
        }
        println(commandValueOf.getTitle());
        println();
        commandValueOf.displayUsage(System.out);
    }
}
