package com.sun.javafx.cursor;

import java.util.HashMap;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/javafx/cursor/CursorFrame.class */
public abstract class CursorFrame {
    private Class<?> firstPlatformCursorClass;
    private Object firstPlatformCursor;
    private Map<Class<?>, Object> otherPlatformCursors;

    public abstract CursorType getCursorType();

    public <T> T getPlatformCursor(Class<T> cls) {
        if (this.firstPlatformCursorClass == cls) {
            return (T) this.firstPlatformCursor;
        }
        if (this.otherPlatformCursors != null) {
            return (T) this.otherPlatformCursors.get(cls);
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> void setPlatforCursor(Class<T> cls, T platformCursor) {
        if (this.firstPlatformCursorClass == null || this.firstPlatformCursorClass == cls) {
            this.firstPlatformCursorClass = cls;
            this.firstPlatformCursor = platformCursor;
        } else {
            if (this.otherPlatformCursors == null) {
                this.otherPlatformCursors = new HashMap();
            }
            this.otherPlatformCursors.put(cls, platformCursor);
        }
    }
}
