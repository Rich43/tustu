package java.awt;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.peer.ScrollPanePeer;
import java.io.Serializable;

/* compiled from: ScrollPane.java */
/* loaded from: rt.jar:java/awt/PeerFixer.class */
class PeerFixer implements AdjustmentListener, Serializable {
    private static final long serialVersionUID = 7051237413532574756L;
    private ScrollPane scroller;

    PeerFixer(ScrollPane scrollPane) {
        this.scroller = scrollPane;
    }

    @Override // java.awt.event.AdjustmentListener
    public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
        Adjustable adjustable = adjustmentEvent.getAdjustable();
        int value = adjustmentEvent.getValue();
        ScrollPanePeer scrollPanePeer = (ScrollPanePeer) this.scroller.peer;
        if (scrollPanePeer != null) {
            scrollPanePeer.setValue(adjustable, value);
        }
        Component component = this.scroller.getComponent(0);
        switch (adjustable.getOrientation()) {
            case 0:
                component.move(-value, component.getLocation().f12371y);
                return;
            case 1:
                component.move(component.getLocation().f12370x, -value);
                return;
            default:
                throw new IllegalArgumentException("Illegal adjustable orientation");
        }
    }
}
