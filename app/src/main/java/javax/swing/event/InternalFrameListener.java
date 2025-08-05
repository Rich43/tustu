package javax.swing.event;

import java.util.EventListener;

/* loaded from: rt.jar:javax/swing/event/InternalFrameListener.class */
public interface InternalFrameListener extends EventListener {
    void internalFrameOpened(InternalFrameEvent internalFrameEvent);

    void internalFrameClosing(InternalFrameEvent internalFrameEvent);

    void internalFrameClosed(InternalFrameEvent internalFrameEvent);

    void internalFrameIconified(InternalFrameEvent internalFrameEvent);

    void internalFrameDeiconified(InternalFrameEvent internalFrameEvent);

    void internalFrameActivated(InternalFrameEvent internalFrameEvent);

    void internalFrameDeactivated(InternalFrameEvent internalFrameEvent);
}
