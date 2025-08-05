package sun.awt.windows;

import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.peer.RobotPeer;

/* loaded from: rt.jar:sun/awt/windows/WRobotPeer.class */
final class WRobotPeer extends WObjectPeer implements RobotPeer {
    private native synchronized void _dispose();

    public native void create();

    public native void mouseMoveImpl(int i2, int i3);

    @Override // java.awt.peer.RobotPeer
    public native void mousePress(int i2);

    @Override // java.awt.peer.RobotPeer
    public native void mouseRelease(int i2);

    @Override // java.awt.peer.RobotPeer
    public native void mouseWheel(int i2);

    @Override // java.awt.peer.RobotPeer
    public native void keyPress(int i2);

    @Override // java.awt.peer.RobotPeer
    public native void keyRelease(int i2);

    private native void getRGBPixels(int i2, int i3, int i4, int i5, int[] iArr);

    WRobotPeer() {
        create();
    }

    WRobotPeer(GraphicsDevice graphicsDevice) {
        create();
    }

    @Override // sun.awt.windows.WObjectPeer
    protected void disposeImpl() {
        _dispose();
    }

    @Override // java.awt.peer.RobotPeer
    public void mouseMove(int i2, int i3) {
        mouseMoveImpl(i2, i3);
    }

    @Override // java.awt.peer.RobotPeer
    public int getRGBPixel(int i2, int i3) {
        return getRGBPixels(new Rectangle(i2, i3, 1, 1))[0];
    }

    @Override // java.awt.peer.RobotPeer
    public int[] getRGBPixels(Rectangle rectangle) {
        int[] iArr = new int[rectangle.width * rectangle.height];
        getRGBPixels(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height, iArr);
        return iArr;
    }
}
