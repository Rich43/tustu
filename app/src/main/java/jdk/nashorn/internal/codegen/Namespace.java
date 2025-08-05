package jdk.nashorn.internal.codegen;

import java.util.HashMap;

/* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/Namespace.class */
public class Namespace {
    private final Namespace parent;
    private final HashMap<String, Integer> directory;

    public Namespace() {
        this(null);
    }

    public Namespace(Namespace parent) {
        this.parent = parent;
        this.directory = new HashMap<>();
    }

    public Namespace getParent() {
        return this.parent;
    }

    public String uniqueName(String base) {
        String truncatedBase = base.length() > 32768 ? base.substring(0, 32768) : base;
        Namespace parent = this;
        while (true) {
            Namespace namespace = parent;
            if (namespace != null) {
                HashMap<String, Integer> namespaceDirectory = namespace.directory;
                Integer counter = namespaceDirectory.get(truncatedBase);
                if (counter == null) {
                    parent = namespace.getParent();
                } else {
                    int count = counter.intValue() + 1;
                    namespaceDirectory.put(truncatedBase, Integer.valueOf(count));
                    return truncatedBase + CompilerConstants.ID_FUNCTION_SEPARATOR.symbolName() + count;
                }
            } else {
                this.directory.put(truncatedBase, 0);
                return truncatedBase;
            }
        }
    }

    public String toString() {
        return this.directory.toString();
    }
}
