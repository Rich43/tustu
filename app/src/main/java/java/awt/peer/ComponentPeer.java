package java.awt.peer;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.BufferCapabilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.PaintEvent;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.VolatileImage;
import sun.awt.CausedFocusEvent;
import sun.java2d.pipe.Region;

/* loaded from: rt.jar:java/awt/peer/ComponentPeer.class */
public interface ComponentPeer {
    public static final int SET_LOCATION = 1;
    public static final int SET_SIZE = 2;
    public static final int SET_BOUNDS = 3;
    public static final int SET_CLIENT_SIZE = 4;
    public static final int RESET_OPERATION = 5;
    public static final int NO_EMBEDDED_CHECK = 16384;
    public static final int DEFAULT_OPERATION = 3;

    boolean isObscured();

    boolean canDetermineObscurity();

    void setVisible(boolean z2);

    void setEnabled(boolean z2);

    void paint(Graphics graphics);

    void print(Graphics graphics);

    void setBounds(int i2, int i3, int i4, int i5, int i6);

    void handleEvent(AWTEvent aWTEvent);

    void coalescePaintEvent(PaintEvent paintEvent);

    Point getLocationOnScreen();

    Dimension getPreferredSize();

    Dimension getMinimumSize();

    ColorModel getColorModel();

    Graphics getGraphics();

    FontMetrics getFontMetrics(Font font);

    void dispose();

    void setForeground(Color color);

    void setBackground(Color color);

    void setFont(Font font);

    void updateCursorImmediately();

    boolean requestFocus(Component component, boolean z2, boolean z3, long j2, CausedFocusEvent.Cause cause);

    boolean isFocusable();

    Image createImage(ImageProducer imageProducer);

    Image createImage(int i2, int i3);

    VolatileImage createVolatileImage(int i2, int i3);

    boolean prepareImage(Image image, int i2, int i3, ImageObserver imageObserver);

    int checkImage(Image image, int i2, int i3, ImageObserver imageObserver);

    GraphicsConfiguration getGraphicsConfiguration();

    boolean handlesWheelScrolling();

    void createBuffers(int i2, BufferCapabilities bufferCapabilities) throws AWTException;

    Image getBackBuffer();

    void flip(int i2, int i3, int i4, int i5, BufferCapabilities.FlipContents flipContents);

    void destroyBuffers();

    void reparent(ContainerPeer containerPeer);

    boolean isReparentSupported();

    void layout();

    void applyShape(Region region);

    void setZOrder(ComponentPeer componentPeer);

    boolean updateGraphicsData(GraphicsConfiguration graphicsConfiguration);
}
