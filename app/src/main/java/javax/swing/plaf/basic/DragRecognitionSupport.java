package javax.swing.plaf.basic;

import java.awt.dnd.DragSource;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import sun.awt.AppContext;
import sun.awt.dnd.SunDragSourceContextPeer;

/* loaded from: rt.jar:javax/swing/plaf/basic/DragRecognitionSupport.class */
class DragRecognitionSupport {
    private int motionThreshold;
    private MouseEvent dndArmedEvent;
    private JComponent component;

    /* loaded from: rt.jar:javax/swing/plaf/basic/DragRecognitionSupport$BeforeDrag.class */
    public interface BeforeDrag {
        void dragStarting(MouseEvent mouseEvent);
    }

    DragRecognitionSupport() {
    }

    private static DragRecognitionSupport getDragRecognitionSupport() {
        DragRecognitionSupport dragRecognitionSupport = (DragRecognitionSupport) AppContext.getAppContext().get(DragRecognitionSupport.class);
        if (dragRecognitionSupport == null) {
            dragRecognitionSupport = new DragRecognitionSupport();
            AppContext.getAppContext().put(DragRecognitionSupport.class, dragRecognitionSupport);
        }
        return dragRecognitionSupport;
    }

    public static boolean mousePressed(MouseEvent mouseEvent) {
        return getDragRecognitionSupport().mousePressedImpl(mouseEvent);
    }

    public static MouseEvent mouseReleased(MouseEvent mouseEvent) {
        return getDragRecognitionSupport().mouseReleasedImpl(mouseEvent);
    }

    public static boolean mouseDragged(MouseEvent mouseEvent, BeforeDrag beforeDrag) {
        return getDragRecognitionSupport().mouseDraggedImpl(mouseEvent, beforeDrag);
    }

    private void clearState() {
        this.dndArmedEvent = null;
        this.component = null;
    }

    private int mapDragOperationFromModifiers(MouseEvent mouseEvent, TransferHandler transferHandler) {
        if (transferHandler == null || !SwingUtilities.isLeftMouseButton(mouseEvent)) {
            return 0;
        }
        return SunDragSourceContextPeer.convertModifiersToDropAction(mouseEvent.getModifiersEx(), transferHandler.getSourceActions(this.component));
    }

    private boolean mousePressedImpl(MouseEvent mouseEvent) {
        this.component = (JComponent) mouseEvent.getSource();
        if (mapDragOperationFromModifiers(mouseEvent, this.component.getTransferHandler()) != 0) {
            this.motionThreshold = DragSource.getDragThreshold();
            this.dndArmedEvent = mouseEvent;
            return true;
        }
        clearState();
        return false;
    }

    private MouseEvent mouseReleasedImpl(MouseEvent mouseEvent) {
        if (this.dndArmedEvent == null) {
            return null;
        }
        MouseEvent mouseEvent2 = null;
        if (mouseEvent.getSource() == this.component) {
            mouseEvent2 = this.dndArmedEvent;
        }
        clearState();
        return mouseEvent2;
    }

    private boolean mouseDraggedImpl(MouseEvent mouseEvent, BeforeDrag beforeDrag) {
        TransferHandler transferHandler;
        int iMapDragOperationFromModifiers;
        if (this.dndArmedEvent == null) {
            return false;
        }
        if (mouseEvent.getSource() != this.component) {
            clearState();
            return false;
        }
        int iAbs = Math.abs(mouseEvent.getX() - this.dndArmedEvent.getX());
        int iAbs2 = Math.abs(mouseEvent.getY() - this.dndArmedEvent.getY());
        if ((iAbs > this.motionThreshold || iAbs2 > this.motionThreshold) && (iMapDragOperationFromModifiers = mapDragOperationFromModifiers(mouseEvent, (transferHandler = this.component.getTransferHandler()))) != 0) {
            if (beforeDrag != null) {
                beforeDrag.dragStarting(this.dndArmedEvent);
            }
            transferHandler.exportAsDrag(this.component, this.dndArmedEvent, iMapDragOperationFromModifiers);
            clearState();
            return true;
        }
        return true;
    }
}
