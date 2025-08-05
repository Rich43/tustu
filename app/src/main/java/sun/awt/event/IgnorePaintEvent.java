package sun.awt.event;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.PaintEvent;

/* loaded from: rt.jar:sun/awt/event/IgnorePaintEvent.class */
public class IgnorePaintEvent extends PaintEvent {
    public IgnorePaintEvent(Component component, int i2, Rectangle rectangle) {
        super(component, i2, rectangle);
    }
}
