package javax.swing;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import sun.awt.AWTAccessor;

/* loaded from: rt.jar:javax/swing/Autoscroller.class */
class Autoscroller implements ActionListener {
    private static Autoscroller sharedInstance = new Autoscroller();
    private static MouseEvent event;
    private static Timer timer;
    private static JComponent component;

    public static void stop(JComponent jComponent) {
        sharedInstance._stop(jComponent);
    }

    public static boolean isRunning(JComponent jComponent) {
        return sharedInstance._isRunning(jComponent);
    }

    public static void processMouseDragged(MouseEvent mouseEvent) {
        sharedInstance._processMouseDragged(mouseEvent);
    }

    Autoscroller() {
    }

    private void start(JComponent jComponent, MouseEvent mouseEvent) {
        Point locationOnScreen = jComponent.getLocationOnScreen();
        if (component != jComponent) {
            _stop(component);
        }
        component = jComponent;
        event = new MouseEvent(component, mouseEvent.getID(), mouseEvent.getWhen(), mouseEvent.getModifiers(), mouseEvent.getX() + locationOnScreen.f12370x, mouseEvent.getY() + locationOnScreen.f12371y, mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen(), mouseEvent.getClickCount(), mouseEvent.isPopupTrigger(), 0);
        AWTAccessor.MouseEventAccessor mouseEventAccessor = AWTAccessor.getMouseEventAccessor();
        mouseEventAccessor.setCausedByTouchEvent(event, mouseEventAccessor.isCausedByTouchEvent(mouseEvent));
        if (timer == null) {
            timer = new Timer(100, this);
        }
        if (!timer.isRunning()) {
            timer.start();
        }
    }

    private void _stop(JComponent jComponent) {
        if (component == jComponent) {
            if (timer != null) {
                timer.stop();
            }
            timer = null;
            event = null;
            component = null;
        }
    }

    private boolean _isRunning(JComponent jComponent) {
        return jComponent == component && timer != null && timer.isRunning();
    }

    private void _processMouseDragged(MouseEvent mouseEvent) {
        JComponent jComponent = (JComponent) mouseEvent.getComponent();
        boolean zContains = true;
        if (jComponent.isShowing()) {
            zContains = jComponent.getVisibleRect().contains(mouseEvent.getX(), mouseEvent.getY());
        }
        if (zContains) {
            _stop(jComponent);
        } else {
            start(jComponent, mouseEvent);
        }
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        JComponent jComponent = component;
        if (jComponent == null || !jComponent.isShowing() || event == null) {
            _stop(jComponent);
            return;
        }
        Point locationOnScreen = jComponent.getLocationOnScreen();
        MouseEvent mouseEvent = new MouseEvent(jComponent, event.getID(), event.getWhen(), event.getModifiers(), event.getX() - locationOnScreen.f12370x, event.getY() - locationOnScreen.f12371y, event.getXOnScreen(), event.getYOnScreen(), event.getClickCount(), event.isPopupTrigger(), 0);
        AWTAccessor.MouseEventAccessor mouseEventAccessor = AWTAccessor.getMouseEventAccessor();
        mouseEventAccessor.setCausedByTouchEvent(mouseEvent, mouseEventAccessor.isCausedByTouchEvent(event));
        jComponent.superProcessMouseMotionEvent(mouseEvent);
    }
}
