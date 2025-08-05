package java.awt.peer;

import java.awt.Rectangle;

/* loaded from: rt.jar:java/awt/peer/RobotPeer.class */
public interface RobotPeer {
    void mouseMove(int i2, int i3);

    void mousePress(int i2);

    void mouseRelease(int i2);

    void mouseWheel(int i2);

    void keyPress(int i2);

    void keyRelease(int i2);

    int getRGBPixel(int i2, int i3);

    int[] getRGBPixels(Rectangle rectangle);

    void dispose();
}
