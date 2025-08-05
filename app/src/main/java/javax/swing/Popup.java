package javax.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import sun.awt.ModalExclude;

/* loaded from: rt.jar:javax/swing/Popup.class */
public class Popup {
    private Component component;

    protected Popup(Component component, Component component2, int i2, int i3) {
        this();
        if (component2 == null) {
            throw new IllegalArgumentException("Contents must be non-null");
        }
        reset(component, component2, i2, i3);
    }

    protected Popup() {
    }

    public void show() {
        Component component = getComponent();
        if (component != null) {
            component.show();
        }
    }

    public void hide() {
        Component component = getComponent();
        if (component instanceof JWindow) {
            component.hide();
            ((JWindow) component).getContentPane().removeAll();
        }
        dispose();
    }

    void dispose() {
        Component component = getComponent();
        Window windowAncestor = SwingUtilities.getWindowAncestor(component);
        if (component instanceof JWindow) {
            ((Window) component).dispose();
        }
        if (windowAncestor instanceof DefaultFrame) {
            windowAncestor.dispose();
        }
    }

    void reset(Component component, Component component2, int i2, int i3) {
        if (getComponent() == null) {
            this.component = createComponent(component);
        }
        if (getComponent() instanceof JWindow) {
            JWindow jWindow = (JWindow) getComponent();
            jWindow.setLocation(i2, i3);
            jWindow.getContentPane().add(component2, BorderLayout.CENTER);
            jWindow.invalidate();
            jWindow.validate();
            if (jWindow.isVisible()) {
                pack();
            }
        }
    }

    void pack() {
        Component component = getComponent();
        if (component instanceof Window) {
            ((Window) component).pack();
        }
    }

    private Window getParentWindow(Component component) {
        Window windowAncestor = null;
        if (component instanceof Window) {
            windowAncestor = (Window) component;
        } else if (component != null) {
            windowAncestor = SwingUtilities.getWindowAncestor(component);
        }
        if (windowAncestor == null) {
            windowAncestor = new DefaultFrame();
        }
        return windowAncestor;
    }

    Component createComponent(Component component) {
        if (GraphicsEnvironment.isHeadless()) {
            return null;
        }
        return new HeavyWeightWindow(getParentWindow(component));
    }

    Component getComponent() {
        return this.component;
    }

    /* loaded from: rt.jar:javax/swing/Popup$HeavyWeightWindow.class */
    static class HeavyWeightWindow extends JWindow implements ModalExclude {
        HeavyWeightWindow(Window window) {
            super(window);
            setFocusableWindowState(false);
            setType(Window.Type.POPUP);
            getRootPane().setUseTrueDoubleBuffering(false);
            try {
                setAlwaysOnTop(true);
            } catch (SecurityException e2) {
            }
        }

        @Override // javax.swing.JWindow, java.awt.Container, java.awt.Component
        public void update(Graphics graphics) {
            paint(graphics);
        }

        @Override // java.awt.Window, java.awt.Component
        public void show() {
            pack();
            if (getWidth() > 0 && getHeight() > 0) {
                super.show();
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/Popup$DefaultFrame.class */
    static class DefaultFrame extends Frame {
        DefaultFrame() {
        }
    }
}
