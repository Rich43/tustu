package java.awt.dnd;

import java.util.EventListener;

/* loaded from: rt.jar:java/awt/dnd/DropTargetListener.class */
public interface DropTargetListener extends EventListener {
    void dragEnter(DropTargetDragEvent dropTargetDragEvent);

    void dragOver(DropTargetDragEvent dropTargetDragEvent);

    void dropActionChanged(DropTargetDragEvent dropTargetDragEvent);

    void dragExit(DropTargetEvent dropTargetEvent);

    void drop(DropTargetDropEvent dropTargetDropEvent);
}
