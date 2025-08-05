package java.awt.dnd;

import java.awt.Insets;
import java.awt.Point;

/* loaded from: rt.jar:java/awt/dnd/Autoscroll.class */
public interface Autoscroll {
    Insets getAutoscrollInsets();

    void autoscroll(Point point);
}
