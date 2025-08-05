package sun.awt.datatransfer;

/* loaded from: rt.jar:sun/awt/datatransfer/ToolkitThreadBlockedHandler.class */
public interface ToolkitThreadBlockedHandler {
    void lock();

    void unlock();

    void enter();

    void exit();
}
