package java.awt.event;

import java.awt.AWTEvent;

/* loaded from: rt.jar:java/awt/event/TextEvent.class */
public class TextEvent extends AWTEvent {
    public static final int TEXT_FIRST = 900;
    public static final int TEXT_LAST = 900;
    public static final int TEXT_VALUE_CHANGED = 900;
    private static final long serialVersionUID = 6269902291250941179L;

    public TextEvent(Object obj, int i2) {
        super(obj, i2);
    }

    @Override // java.awt.AWTEvent
    public String paramString() {
        String str;
        switch (this.id) {
            case 900:
                str = "TEXT_VALUE_CHANGED";
                break;
            default:
                str = "unknown type";
                break;
        }
        return str;
    }
}
