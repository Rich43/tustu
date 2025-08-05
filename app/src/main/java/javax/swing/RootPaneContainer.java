package javax.swing;

import java.awt.Component;
import java.awt.Container;

/* loaded from: rt.jar:javax/swing/RootPaneContainer.class */
public interface RootPaneContainer {
    JRootPane getRootPane();

    void setContentPane(Container container);

    Container getContentPane();

    void setLayeredPane(JLayeredPane jLayeredPane);

    JLayeredPane getLayeredPane();

    void setGlassPane(Component component);

    Component getGlassPane();
}
