package com.sun.javafx.cursor;

/* loaded from: jfxrt.jar:com/sun/javafx/cursor/StandardCursorFrame.class */
public final class StandardCursorFrame extends CursorFrame {
    private CursorType cursorType;

    public StandardCursorFrame(CursorType cursorType) {
        this.cursorType = cursorType;
    }

    @Override // com.sun.javafx.cursor.CursorFrame
    public CursorType getCursorType() {
        return this.cursorType;
    }
}
