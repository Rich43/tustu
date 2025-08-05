package com.sun.javafx.scene.input;

import javafx.scene.input.Dragboard;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/input/DragboardHelper.class */
public class DragboardHelper {
    private static DragboardAccessor dragboardAccessor;

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/input/DragboardHelper$DragboardAccessor.class */
    public interface DragboardAccessor {
        void setDataAccessRestriction(Dragboard dragboard, boolean z2);
    }

    static {
        forceInit(Dragboard.class);
    }

    private DragboardHelper() {
    }

    public static void setDataAccessRestriction(Dragboard dragboard, boolean restricted) {
        dragboardAccessor.setDataAccessRestriction(dragboard, restricted);
    }

    public static void setDragboardAccessor(DragboardAccessor newAccessor) {
        if (dragboardAccessor != null) {
            throw new IllegalStateException();
        }
        dragboardAccessor = newAccessor;
    }

    private static void forceInit(Class<?> classToInit) {
        try {
            Class.forName(classToInit.getName(), true, classToInit.getClassLoader());
        } catch (ClassNotFoundException e2) {
            throw new AssertionError(e2);
        }
    }
}
