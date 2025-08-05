package javax.swing;

/* loaded from: rt.jar:javax/swing/DesktopManager.class */
public interface DesktopManager {
    void openFrame(JInternalFrame jInternalFrame);

    void closeFrame(JInternalFrame jInternalFrame);

    void maximizeFrame(JInternalFrame jInternalFrame);

    void minimizeFrame(JInternalFrame jInternalFrame);

    void iconifyFrame(JInternalFrame jInternalFrame);

    void deiconifyFrame(JInternalFrame jInternalFrame);

    void activateFrame(JInternalFrame jInternalFrame);

    void deactivateFrame(JInternalFrame jInternalFrame);

    void beginDraggingFrame(JComponent jComponent);

    void dragFrame(JComponent jComponent, int i2, int i3);

    void endDraggingFrame(JComponent jComponent);

    void beginResizingFrame(JComponent jComponent, int i2);

    void resizeFrame(JComponent jComponent, int i2, int i3, int i4, int i5);

    void endResizingFrame(JComponent jComponent);

    void setBoundsForFrame(JComponent jComponent, int i2, int i3, int i4, int i5);
}
