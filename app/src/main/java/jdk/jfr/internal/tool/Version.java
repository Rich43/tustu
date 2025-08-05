package jdk.jfr.internal.tool;

import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/* loaded from: jfr.jar:jdk/jfr/internal/tool/Version.class */
final class Version extends Command {
    Version() {
    }

    @Override // jdk.jfr.internal.tool.Command
    public String getName() {
        return "version";
    }

    @Override // jdk.jfr.internal.tool.Command
    public String getDescription() {
        return "Display version of the jfr tool";
    }

    @Override // jdk.jfr.internal.tool.Command
    public void execute(Deque<String> deque) {
        System.out.println("1.0");
    }

    @Override // jdk.jfr.internal.tool.Command
    protected List<String> getAliases() {
        return Arrays.asList("--version");
    }
}
