package sun.awt.windows;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Map;
import sun.awt.dnd.SunDragSourceContextPeer;

/* loaded from: rt.jar:sun/awt/windows/WDragSourceContextPeer.class */
final class WDragSourceContextPeer extends SunDragSourceContextPeer {
    private static final WDragSourceContextPeer theInstance = new WDragSourceContextPeer(null);

    native long createDragSource(Component component, Transferable transferable, InputEvent inputEvent, int i2, long[] jArr, Map map);

    native void doDragDrop(long j2, Cursor cursor, int[] iArr, int i2, int i3, int i4, int i5);

    @Override // sun.awt.dnd.SunDragSourceContextPeer
    protected native void setNativeCursor(long j2, Cursor cursor, int i2);

    @Override // sun.awt.dnd.SunDragSourceContextPeer
    public void startSecondaryEventLoop() {
        WToolkit.startSecondaryEventLoop();
    }

    @Override // sun.awt.dnd.SunDragSourceContextPeer
    public void quitSecondaryEventLoop() {
        WToolkit.quitSecondaryEventLoop();
    }

    private WDragSourceContextPeer(DragGestureEvent dragGestureEvent) {
        super(dragGestureEvent);
    }

    static WDragSourceContextPeer createDragSourceContextPeer(DragGestureEvent dragGestureEvent) throws InvalidDnDOperationException {
        theInstance.setTrigger(dragGestureEvent);
        return theInstance;
    }

    @Override // sun.awt.dnd.SunDragSourceContextPeer
    protected void startDrag(Transferable transferable, long[] jArr, Map map) {
        long jCreateDragSource = createDragSource(getTrigger().getComponent(), transferable, getTrigger().getTriggerEvent(), getTrigger().getSourceAsDragGestureRecognizer().getSourceActions(), jArr, map);
        if (jCreateDragSource == 0) {
            throw new InvalidDnDOperationException("failed to create native peer");
        }
        int[] data = null;
        Point dragImageOffset = null;
        Image dragImage = getDragImage();
        int width = -1;
        int height = -1;
        if (dragImage != null) {
            try {
                width = dragImage.getWidth(null);
                height = dragImage.getHeight(null);
                if (width < 0 || height < 0) {
                    throw new InvalidDnDOperationException("drag image is not ready");
                }
                dragImageOffset = getDragImageOffset();
                BufferedImage bufferedImage = new BufferedImage(width, height, 2);
                bufferedImage.getGraphics().drawImage(dragImage, 0, 0, null);
                data = ((DataBufferInt) bufferedImage.getData().getDataBuffer()).getData();
            } catch (Throwable th) {
                throw new InvalidDnDOperationException("drag image creation problem: " + th.getMessage());
            }
        }
        setNativeContext(jCreateDragSource);
        WDropTargetContextPeer.setCurrentJVMLocalSourceTransferable(transferable);
        if (data != null) {
            doDragDrop(getNativeContext(), getCursor(), data, width, height, dragImageOffset.f12370x, dragImageOffset.f12371y);
        } else {
            doDragDrop(getNativeContext(), getCursor(), null, -1, -1, 0, 0);
        }
    }
}
