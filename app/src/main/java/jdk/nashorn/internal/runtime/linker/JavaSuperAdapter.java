package jdk.nashorn.internal.runtime.linker;

import java.util.Objects;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/linker/JavaSuperAdapter.class */
class JavaSuperAdapter {
    private final Object adapter;

    JavaSuperAdapter(Object adapter) {
        this.adapter = Objects.requireNonNull(adapter);
    }

    public Object getAdapter() {
        return this.adapter;
    }
}
