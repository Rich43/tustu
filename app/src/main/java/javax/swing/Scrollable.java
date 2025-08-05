package javax.swing;

import java.awt.Dimension;
import java.awt.Rectangle;

/* loaded from: rt.jar:javax/swing/Scrollable.class */
public interface Scrollable {
    Dimension getPreferredScrollableViewportSize();

    int getScrollableUnitIncrement(Rectangle rectangle, int i2, int i3);

    int getScrollableBlockIncrement(Rectangle rectangle, int i2, int i3);

    boolean getScrollableTracksViewportWidth();

    boolean getScrollableTracksViewportHeight();
}
