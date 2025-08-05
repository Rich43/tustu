package java.awt;

/* loaded from: rt.jar:java/awt/PointerInfo.class */
public class PointerInfo {
    private final GraphicsDevice device;
    private final Point location;

    PointerInfo(GraphicsDevice graphicsDevice, Point point) {
        this.device = graphicsDevice;
        this.location = point;
    }

    public GraphicsDevice getDevice() {
        return this.device;
    }

    public Point getLocation() {
        return this.location;
    }
}
