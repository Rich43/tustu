package java.awt.dnd;

/* loaded from: rt.jar:java/awt/dnd/DragSourceDragEvent.class */
public class DragSourceDragEvent extends DragSourceEvent {
    private static final long serialVersionUID = 481346297933902471L;
    private static final int JDK_1_3_MODIFIERS = 63;
    private static final int JDK_1_4_MODIFIERS = 16320;
    private int targetActions;
    private int dropAction;
    private int gestureModifiers;
    private boolean invalidModifiers;

    public DragSourceDragEvent(DragSourceContext dragSourceContext, int i2, int i3, int i4) {
        super(dragSourceContext);
        this.targetActions = 0;
        this.dropAction = 0;
        this.gestureModifiers = 0;
        this.targetActions = i3;
        this.gestureModifiers = i4;
        this.dropAction = i2;
        if ((i4 & (-16384)) != 0) {
            this.invalidModifiers = true;
            return;
        }
        if (getGestureModifiers() != 0 && getGestureModifiersEx() == 0) {
            setNewModifiers();
        } else if (getGestureModifiers() == 0 && getGestureModifiersEx() != 0) {
            setOldModifiers();
        } else {
            this.invalidModifiers = true;
        }
    }

    public DragSourceDragEvent(DragSourceContext dragSourceContext, int i2, int i3, int i4, int i5, int i6) {
        super(dragSourceContext, i5, i6);
        this.targetActions = 0;
        this.dropAction = 0;
        this.gestureModifiers = 0;
        this.targetActions = i3;
        this.gestureModifiers = i4;
        this.dropAction = i2;
        if ((i4 & (-16384)) != 0) {
            this.invalidModifiers = true;
            return;
        }
        if (getGestureModifiers() != 0 && getGestureModifiersEx() == 0) {
            setNewModifiers();
        } else if (getGestureModifiers() == 0 && getGestureModifiersEx() != 0) {
            setOldModifiers();
        } else {
            this.invalidModifiers = true;
        }
    }

    public int getTargetActions() {
        return this.targetActions;
    }

    public int getGestureModifiers() {
        return this.invalidModifiers ? this.gestureModifiers : this.gestureModifiers & 63;
    }

    public int getGestureModifiersEx() {
        return this.invalidModifiers ? this.gestureModifiers : this.gestureModifiers & JDK_1_4_MODIFIERS;
    }

    public int getUserAction() {
        return this.dropAction;
    }

    public int getDropAction() {
        return this.targetActions & getDragSourceContext().getSourceActions();
    }

    private void setNewModifiers() {
        if ((this.gestureModifiers & 16) != 0) {
            this.gestureModifiers |= 1024;
        }
        if ((this.gestureModifiers & 8) != 0) {
            this.gestureModifiers |= 2048;
        }
        if ((this.gestureModifiers & 4) != 0) {
            this.gestureModifiers |= 4096;
        }
        if ((this.gestureModifiers & 1) != 0) {
            this.gestureModifiers |= 64;
        }
        if ((this.gestureModifiers & 2) != 0) {
            this.gestureModifiers |= 128;
        }
        if ((this.gestureModifiers & 32) != 0) {
            this.gestureModifiers |= 8192;
        }
    }

    private void setOldModifiers() {
        if ((this.gestureModifiers & 1024) != 0) {
            this.gestureModifiers |= 16;
        }
        if ((this.gestureModifiers & 2048) != 0) {
            this.gestureModifiers |= 8;
        }
        if ((this.gestureModifiers & 4096) != 0) {
            this.gestureModifiers |= 4;
        }
        if ((this.gestureModifiers & 64) != 0) {
            this.gestureModifiers |= 1;
        }
        if ((this.gestureModifiers & 128) != 0) {
            this.gestureModifiers |= 2;
        }
        if ((this.gestureModifiers & 8192) != 0) {
            this.gestureModifiers |= 32;
        }
    }
}
