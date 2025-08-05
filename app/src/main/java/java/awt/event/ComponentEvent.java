package java.awt.event;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Rectangle;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:java/awt/event/ComponentEvent.class */
public class ComponentEvent extends AWTEvent {
    public static final int COMPONENT_FIRST = 100;
    public static final int COMPONENT_LAST = 103;
    public static final int COMPONENT_MOVED = 100;
    public static final int COMPONENT_RESIZED = 101;
    public static final int COMPONENT_SHOWN = 102;
    public static final int COMPONENT_HIDDEN = 103;
    private static final long serialVersionUID = 8101406823902992965L;

    public ComponentEvent(Component component, int i2) {
        super(component, i2);
    }

    public Component getComponent() {
        if (this.source instanceof Component) {
            return (Component) this.source;
        }
        return null;
    }

    @Override // java.awt.AWTEvent
    public String paramString() {
        String str;
        Rectangle bounds = this.source != null ? ((Component) this.source).getBounds() : null;
        switch (this.id) {
            case 100:
                str = "COMPONENT_MOVED (" + bounds.f12372x + "," + bounds.f12373y + " " + bounds.width + LanguageTag.PRIVATEUSE + bounds.height + ")";
                break;
            case 101:
                str = "COMPONENT_RESIZED (" + bounds.f12372x + "," + bounds.f12373y + " " + bounds.width + LanguageTag.PRIVATEUSE + bounds.height + ")";
                break;
            case 102:
                str = "COMPONENT_SHOWN";
                break;
            case 103:
                str = "COMPONENT_HIDDEN";
                break;
            default:
                str = "unknown type";
                break;
        }
        return str;
    }
}
