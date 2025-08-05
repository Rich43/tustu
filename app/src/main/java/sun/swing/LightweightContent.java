package sun.swing;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.dnd.peer.DragSourceContextPeer;
import javax.swing.JComponent;

/* loaded from: rt.jar:sun/swing/LightweightContent.class */
public interface LightweightContent {
    JComponent getComponent();

    void paintLock();

    void paintUnlock();

    void imageReshaped(int i2, int i3, int i4, int i5);

    void imageUpdated(int i2, int i3, int i4, int i5);

    void focusGrabbed();

    void focusUngrabbed();

    void preferredSizeChanged(int i2, int i3);

    void maximumSizeChanged(int i2, int i3);

    void minimumSizeChanged(int i2, int i3);

    default void imageBufferReset(int[] iArr, int i2, int i3, int i4, int i5, int i6, int i7) {
        imageBufferReset(iArr, i2, i3, i4, i5, i6);
    }

    default void imageBufferReset(int[] iArr, int i2, int i3, int i4, int i5, int i6) {
        imageBufferReset(iArr, i2, i3, i4, i5, i6, 1);
    }

    default void setCursor(Cursor cursor) {
    }

    default <T extends DragGestureRecognizer> T createDragGestureRecognizer(Class<T> cls, DragSource dragSource, Component component, int i2, DragGestureListener dragGestureListener) {
        return null;
    }

    default DragSourceContextPeer createDragSourceContextPeer(DragGestureEvent dragGestureEvent) throws InvalidDnDOperationException {
        return null;
    }

    default void addDropTarget(DropTarget dropTarget) {
    }

    default void removeDropTarget(DropTarget dropTarget) {
    }
}
