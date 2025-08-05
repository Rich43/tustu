package javax.swing.event;

import java.awt.AWTEvent;
import javax.swing.JInternalFrame;

/* loaded from: rt.jar:javax/swing/event/InternalFrameEvent.class */
public class InternalFrameEvent extends AWTEvent {
    public static final int INTERNAL_FRAME_FIRST = 25549;
    public static final int INTERNAL_FRAME_LAST = 25555;
    public static final int INTERNAL_FRAME_OPENED = 25549;
    public static final int INTERNAL_FRAME_CLOSING = 25550;
    public static final int INTERNAL_FRAME_CLOSED = 25551;
    public static final int INTERNAL_FRAME_ICONIFIED = 25552;
    public static final int INTERNAL_FRAME_DEICONIFIED = 25553;
    public static final int INTERNAL_FRAME_ACTIVATED = 25554;
    public static final int INTERNAL_FRAME_DEACTIVATED = 25555;

    public InternalFrameEvent(JInternalFrame jInternalFrame, int i2) {
        super(jInternalFrame, i2);
    }

    @Override // java.awt.AWTEvent
    public String paramString() {
        String str;
        switch (this.id) {
            case 25549:
                str = "INTERNAL_FRAME_OPENED";
                break;
            case INTERNAL_FRAME_CLOSING /* 25550 */:
                str = "INTERNAL_FRAME_CLOSING";
                break;
            case INTERNAL_FRAME_CLOSED /* 25551 */:
                str = "INTERNAL_FRAME_CLOSED";
                break;
            case INTERNAL_FRAME_ICONIFIED /* 25552 */:
                str = "INTERNAL_FRAME_ICONIFIED";
                break;
            case INTERNAL_FRAME_DEICONIFIED /* 25553 */:
                str = "INTERNAL_FRAME_DEICONIFIED";
                break;
            case INTERNAL_FRAME_ACTIVATED /* 25554 */:
                str = "INTERNAL_FRAME_ACTIVATED";
                break;
            case 25555:
                str = "INTERNAL_FRAME_DEACTIVATED";
                break;
            default:
                str = "unknown type";
                break;
        }
        return str;
    }

    public JInternalFrame getInternalFrame() {
        if (this.source instanceof JInternalFrame) {
            return (JInternalFrame) this.source;
        }
        return null;
    }
}
