package java.awt.event;

import java.awt.AWTEvent;

/* loaded from: rt.jar:java/awt/event/ActionEvent.class */
public class ActionEvent extends AWTEvent {
    public static final int SHIFT_MASK = 1;
    public static final int CTRL_MASK = 2;
    public static final int META_MASK = 4;
    public static final int ALT_MASK = 8;
    public static final int ACTION_FIRST = 1001;
    public static final int ACTION_LAST = 1001;
    public static final int ACTION_PERFORMED = 1001;
    String actionCommand;
    long when;
    int modifiers;
    private static final long serialVersionUID = -7671078796273832149L;

    public ActionEvent(Object obj, int i2, String str) {
        this(obj, i2, str, 0);
    }

    public ActionEvent(Object obj, int i2, String str, int i3) {
        this(obj, i2, str, 0L, i3);
    }

    public ActionEvent(Object obj, int i2, String str, long j2, int i3) {
        super(obj, i2);
        this.actionCommand = str;
        this.when = j2;
        this.modifiers = i3;
    }

    public String getActionCommand() {
        return this.actionCommand;
    }

    public long getWhen() {
        return this.when;
    }

    public int getModifiers() {
        return this.modifiers;
    }

    @Override // java.awt.AWTEvent
    public String paramString() {
        String str;
        switch (this.id) {
            case 1001:
                str = "ACTION_PERFORMED";
                break;
            default:
                str = "unknown type";
                break;
        }
        return str + ",cmd=" + this.actionCommand + ",when=" + this.when + ",modifiers=" + KeyEvent.getKeyModifiersText(this.modifiers);
    }
}
