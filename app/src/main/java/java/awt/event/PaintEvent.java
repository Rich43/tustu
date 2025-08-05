package java.awt.event;

import java.awt.Component;
import java.awt.Rectangle;
import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:java/awt/event/PaintEvent.class */
public class PaintEvent extends ComponentEvent {
    public static final int PAINT_FIRST = 800;
    public static final int PAINT_LAST = 801;
    public static final int PAINT = 800;
    public static final int UPDATE = 801;
    Rectangle updateRect;
    private static final long serialVersionUID = 1267492026433337593L;

    public PaintEvent(Component component, int i2, Rectangle rectangle) {
        super(component, i2);
        this.updateRect = rectangle;
    }

    public Rectangle getUpdateRect() {
        return this.updateRect;
    }

    public void setUpdateRect(Rectangle rectangle) {
        this.updateRect = rectangle;
    }

    @Override // java.awt.event.ComponentEvent, java.awt.AWTEvent
    public String paramString() {
        String str;
        switch (this.id) {
            case 800:
                str = "PAINT";
                break;
            case 801:
                str = "UPDATE";
                break;
            default:
                str = "unknown type";
                break;
        }
        return str + ",updateRect=" + (this.updateRect != null ? this.updateRect.toString() : FXMLLoader.NULL_KEYWORD);
    }
}
