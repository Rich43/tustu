package sun.awt.windows;

import java.awt.Component;
import java.awt.Point;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.MouseDragGestureRecognizer;
import java.awt.event.MouseEvent;
import sun.awt.dnd.SunDragSourceContextPeer;

/* loaded from: rt.jar:sun/awt/windows/WMouseDragGestureRecognizer.class */
final class WMouseDragGestureRecognizer extends MouseDragGestureRecognizer {
    private static final long serialVersionUID = -3527844310018033570L;
    protected static int motionThreshold;
    protected static final int ButtonMask = 7168;

    protected WMouseDragGestureRecognizer(DragSource dragSource, Component component, int i2, DragGestureListener dragGestureListener) {
        super(dragSource, component, i2, dragGestureListener);
    }

    protected WMouseDragGestureRecognizer(DragSource dragSource, Component component, int i2) {
        this(dragSource, component, i2, null);
    }

    protected WMouseDragGestureRecognizer(DragSource dragSource, Component component) {
        this(dragSource, component, 0);
    }

    protected WMouseDragGestureRecognizer(DragSource dragSource) {
        this(dragSource, null);
    }

    protected int mapDragOperationFromModifiers(MouseEvent mouseEvent) {
        int modifiersEx = mouseEvent.getModifiersEx();
        int i2 = modifiersEx & ButtonMask;
        if (i2 != 1024 && i2 != 2048 && i2 != 4096) {
            return 0;
        }
        return SunDragSourceContextPeer.convertModifiersToDropAction(modifiersEx, getSourceActions());
    }

    @Override // java.awt.dnd.MouseDragGestureRecognizer, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    @Override // java.awt.dnd.MouseDragGestureRecognizer, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        this.events.clear();
        if (mapDragOperationFromModifiers(mouseEvent) != 0) {
            try {
                motionThreshold = DragSource.getDragThreshold();
            } catch (Exception e2) {
                motionThreshold = 5;
            }
            appendEvent(mouseEvent);
        }
    }

    @Override // java.awt.dnd.MouseDragGestureRecognizer, java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
        this.events.clear();
    }

    @Override // java.awt.dnd.MouseDragGestureRecognizer, java.awt.event.MouseListener
    public void mouseEntered(MouseEvent mouseEvent) {
        this.events.clear();
    }

    @Override // java.awt.dnd.MouseDragGestureRecognizer, java.awt.event.MouseListener
    public void mouseExited(MouseEvent mouseEvent) {
        if (!this.events.isEmpty() && mapDragOperationFromModifiers(mouseEvent) == 0) {
            this.events.clear();
        }
    }

    @Override // java.awt.dnd.MouseDragGestureRecognizer, java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent mouseEvent) {
        int iMapDragOperationFromModifiers;
        if (this.events.isEmpty() || (iMapDragOperationFromModifiers = mapDragOperationFromModifiers(mouseEvent)) == 0) {
            return;
        }
        Point point = ((MouseEvent) this.events.get(0)).getPoint();
        Point point2 = mouseEvent.getPoint();
        int iAbs = Math.abs(point.f12370x - point2.f12370x);
        int iAbs2 = Math.abs(point.f12371y - point2.f12371y);
        if (iAbs > motionThreshold || iAbs2 > motionThreshold) {
            fireDragGestureRecognized(iMapDragOperationFromModifiers, ((MouseEvent) getTriggerEvent()).getPoint());
        } else {
            appendEvent(mouseEvent);
        }
    }

    @Override // java.awt.dnd.MouseDragGestureRecognizer, java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent mouseEvent) {
    }
}
