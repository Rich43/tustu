package sun.awt;

import java.awt.AWTEvent;
import java.awt.ActiveEvent;

/* loaded from: rt.jar:sun/awt/ModalityEvent.class */
public class ModalityEvent extends AWTEvent implements ActiveEvent {
    public static final int MODALITY_PUSHED = 1300;
    public static final int MODALITY_POPPED = 1301;
    private ModalityListener listener;

    public ModalityEvent(Object obj, ModalityListener modalityListener, int i2) {
        super(obj, i2);
        this.listener = modalityListener;
    }

    @Override // java.awt.ActiveEvent
    public void dispatch() {
        switch (getID()) {
            case MODALITY_PUSHED /* 1300 */:
                this.listener.modalityPushed(this);
                return;
            case MODALITY_POPPED /* 1301 */:
                this.listener.modalityPopped(this);
                return;
            default:
                throw new Error("Invalid event id.");
        }
    }
}
