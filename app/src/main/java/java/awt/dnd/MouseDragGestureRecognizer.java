package java.awt.dnd;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/* loaded from: rt.jar:java/awt/dnd/MouseDragGestureRecognizer.class */
public abstract class MouseDragGestureRecognizer extends DragGestureRecognizer implements MouseListener, MouseMotionListener {
    private static final long serialVersionUID = 6220099344182281120L;

    protected MouseDragGestureRecognizer(DragSource dragSource, Component component, int i2, DragGestureListener dragGestureListener) {
        super(dragSource, component, i2, dragGestureListener);
    }

    protected MouseDragGestureRecognizer(DragSource dragSource, Component component, int i2) {
        this(dragSource, component, i2, null);
    }

    protected MouseDragGestureRecognizer(DragSource dragSource, Component component) {
        this(dragSource, component, 0);
    }

    protected MouseDragGestureRecognizer(DragSource dragSource) {
        this(dragSource, null);
    }

    @Override // java.awt.dnd.DragGestureRecognizer
    protected void registerListeners() {
        this.component.addMouseListener(this);
        this.component.addMouseMotionListener(this);
    }

    @Override // java.awt.dnd.DragGestureRecognizer
    protected void unregisterListeners() {
        this.component.removeMouseListener(this);
        this.component.removeMouseMotionListener(this);
    }

    @Override // java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseExited(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent mouseEvent) {
    }
}
