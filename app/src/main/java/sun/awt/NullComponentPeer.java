package sun.awt;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.BufferCapabilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.PaintEvent;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.VolatileImage;
import java.awt.peer.CanvasPeer;
import java.awt.peer.ComponentPeer;
import java.awt.peer.ContainerPeer;
import java.awt.peer.LightweightPeer;
import java.awt.peer.PanelPeer;
import sun.awt.CausedFocusEvent;
import sun.java2d.pipe.Region;

/* loaded from: rt.jar:sun/awt/NullComponentPeer.class */
public class NullComponentPeer implements LightweightPeer, CanvasPeer, PanelPeer {
    @Override // java.awt.peer.ComponentPeer
    public boolean isObscured() {
        return false;
    }

    @Override // java.awt.peer.ComponentPeer
    public boolean canDetermineObscurity() {
        return false;
    }

    @Override // java.awt.peer.ComponentPeer
    public boolean isFocusable() {
        return false;
    }

    @Override // java.awt.peer.ComponentPeer
    public void setVisible(boolean z2) {
    }

    public void show() {
    }

    public void hide() {
    }

    @Override // java.awt.peer.ComponentPeer
    public void setEnabled(boolean z2) {
    }

    public void enable() {
    }

    public void disable() {
    }

    @Override // java.awt.peer.ComponentPeer
    public void paint(Graphics graphics) {
    }

    public void repaint(long j2, int i2, int i3, int i4, int i5) {
    }

    @Override // java.awt.peer.ComponentPeer
    public void print(Graphics graphics) {
    }

    @Override // java.awt.peer.ComponentPeer
    public void setBounds(int i2, int i3, int i4, int i5, int i6) {
    }

    public void reshape(int i2, int i3, int i4, int i5) {
    }

    @Override // java.awt.peer.ComponentPeer
    public void coalescePaintEvent(PaintEvent paintEvent) {
    }

    public boolean handleEvent(Event event) {
        return false;
    }

    @Override // java.awt.peer.ComponentPeer
    public void handleEvent(AWTEvent aWTEvent) {
    }

    @Override // java.awt.peer.ComponentPeer
    public Dimension getPreferredSize() {
        return new Dimension(1, 1);
    }

    @Override // java.awt.peer.ComponentPeer
    public Dimension getMinimumSize() {
        return new Dimension(1, 1);
    }

    @Override // java.awt.peer.ComponentPeer
    public ColorModel getColorModel() {
        return null;
    }

    @Override // java.awt.peer.ComponentPeer
    public Graphics getGraphics() {
        return null;
    }

    @Override // java.awt.peer.ComponentPeer
    public GraphicsConfiguration getGraphicsConfiguration() {
        return null;
    }

    @Override // java.awt.peer.ComponentPeer
    public FontMetrics getFontMetrics(Font font) {
        return null;
    }

    @Override // java.awt.peer.ComponentPeer
    public void dispose() {
    }

    @Override // java.awt.peer.ComponentPeer
    public void setForeground(Color color) {
    }

    @Override // java.awt.peer.ComponentPeer
    public void setBackground(Color color) {
    }

    @Override // java.awt.peer.ComponentPeer
    public void setFont(Font font) {
    }

    @Override // java.awt.peer.ComponentPeer
    public void updateCursorImmediately() {
    }

    public void setCursor(Cursor cursor) {
    }

    @Override // java.awt.peer.ComponentPeer
    public boolean requestFocus(Component component, boolean z2, boolean z3, long j2, CausedFocusEvent.Cause cause) {
        return false;
    }

    @Override // java.awt.peer.ComponentPeer
    public Image createImage(ImageProducer imageProducer) {
        return null;
    }

    @Override // java.awt.peer.ComponentPeer
    public Image createImage(int i2, int i3) {
        return null;
    }

    @Override // java.awt.peer.ComponentPeer
    public boolean prepareImage(Image image, int i2, int i3, ImageObserver imageObserver) {
        return false;
    }

    @Override // java.awt.peer.ComponentPeer
    public int checkImage(Image image, int i2, int i3, ImageObserver imageObserver) {
        return 0;
    }

    public Dimension preferredSize() {
        return getPreferredSize();
    }

    public Dimension minimumSize() {
        return getMinimumSize();
    }

    @Override // java.awt.peer.ComponentPeer
    public Point getLocationOnScreen() {
        return new Point(0, 0);
    }

    @Override // java.awt.peer.ContainerPeer
    public Insets getInsets() {
        return insets();
    }

    @Override // java.awt.peer.ContainerPeer
    public void beginValidate() {
    }

    @Override // java.awt.peer.ContainerPeer
    public void endValidate() {
    }

    public Insets insets() {
        return new Insets(0, 0, 0, 0);
    }

    public boolean isPaintPending() {
        return false;
    }

    @Override // java.awt.peer.ComponentPeer
    public boolean handlesWheelScrolling() {
        return false;
    }

    @Override // java.awt.peer.ComponentPeer
    public VolatileImage createVolatileImage(int i2, int i3) {
        return null;
    }

    @Override // java.awt.peer.ContainerPeer
    public void beginLayout() {
    }

    @Override // java.awt.peer.ContainerPeer
    public void endLayout() {
    }

    @Override // java.awt.peer.ComponentPeer
    public void createBuffers(int i2, BufferCapabilities bufferCapabilities) throws AWTException {
        throw new AWTException("Page-flipping is not allowed on a lightweight component");
    }

    @Override // java.awt.peer.ComponentPeer
    public Image getBackBuffer() {
        throw new IllegalStateException("Page-flipping is not allowed on a lightweight component");
    }

    @Override // java.awt.peer.ComponentPeer
    public void flip(int i2, int i3, int i4, int i5, BufferCapabilities.FlipContents flipContents) {
        throw new IllegalStateException("Page-flipping is not allowed on a lightweight component");
    }

    @Override // java.awt.peer.ComponentPeer
    public void destroyBuffers() {
    }

    @Override // java.awt.peer.ComponentPeer
    public boolean isReparentSupported() {
        return false;
    }

    @Override // java.awt.peer.ComponentPeer
    public void reparent(ContainerPeer containerPeer) {
        throw new UnsupportedOperationException();
    }

    @Override // java.awt.peer.ComponentPeer
    public void layout() {
    }

    public Rectangle getBounds() {
        return new Rectangle(0, 0, 0, 0);
    }

    @Override // java.awt.peer.ComponentPeer
    public void applyShape(Region region) {
    }

    @Override // java.awt.peer.ComponentPeer
    public void setZOrder(ComponentPeer componentPeer) {
    }

    @Override // java.awt.peer.ComponentPeer
    public boolean updateGraphicsData(GraphicsConfiguration graphicsConfiguration) {
        return false;
    }

    @Override // java.awt.peer.CanvasPeer
    public GraphicsConfiguration getAppropriateGraphicsConfiguration(GraphicsConfiguration graphicsConfiguration) {
        return graphicsConfiguration;
    }
}
