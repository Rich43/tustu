package java.awt.event;

import java.awt.AWTEvent;
import java.awt.ItemSelectable;

/* loaded from: rt.jar:java/awt/event/ItemEvent.class */
public class ItemEvent extends AWTEvent {
    public static final int ITEM_FIRST = 701;
    public static final int ITEM_LAST = 701;
    public static final int ITEM_STATE_CHANGED = 701;
    public static final int SELECTED = 1;
    public static final int DESELECTED = 2;
    Object item;
    int stateChange;
    private static final long serialVersionUID = -608708132447206933L;

    public ItemEvent(ItemSelectable itemSelectable, int i2, Object obj, int i3) {
        super(itemSelectable, i2);
        this.item = obj;
        this.stateChange = i3;
    }

    public ItemSelectable getItemSelectable() {
        return (ItemSelectable) this.source;
    }

    public Object getItem() {
        return this.item;
    }

    public int getStateChange() {
        return this.stateChange;
    }

    @Override // java.awt.AWTEvent
    public String paramString() {
        String str;
        String str2;
        switch (this.id) {
            case 701:
                str = "ITEM_STATE_CHANGED";
                break;
            default:
                str = "unknown type";
                break;
        }
        switch (this.stateChange) {
            case 1:
                str2 = "SELECTED";
                break;
            case 2:
                str2 = "DESELECTED";
                break;
            default:
                str2 = "unknown type";
                break;
        }
        return str + ",item=" + this.item + ",stateChange=" + str2;
    }
}
