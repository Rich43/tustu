package java.awt.dnd.peer;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.InvalidDnDOperationException;

/* loaded from: rt.jar:java/awt/dnd/peer/DragSourceContextPeer.class */
public interface DragSourceContextPeer {
    void startDrag(DragSourceContext dragSourceContext, Cursor cursor, Image image, Point point) throws InvalidDnDOperationException;

    Cursor getCursor();

    void setCursor(Cursor cursor) throws InvalidDnDOperationException;

    void transferablesFlavorsChanged();
}
