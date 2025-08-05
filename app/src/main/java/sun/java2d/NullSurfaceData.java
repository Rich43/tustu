package sun.java2d;

import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import sun.java2d.StateTrackable;
import sun.java2d.loops.SurfaceType;
import sun.java2d.pipe.NullPipe;

/* loaded from: rt.jar:sun/java2d/NullSurfaceData.class */
public class NullSurfaceData extends SurfaceData {
    public static final SurfaceData theInstance = new NullSurfaceData();
    private static final NullPipe nullpipe = new NullPipe();

    private NullSurfaceData() {
        super(StateTrackable.State.IMMUTABLE, SurfaceType.Any, ColorModel.getRGBdefault());
    }

    @Override // sun.java2d.SurfaceData
    public void invalidate() {
    }

    @Override // sun.java2d.SurfaceData
    public SurfaceData getReplacement() {
        return this;
    }

    @Override // sun.java2d.SurfaceData
    public void validatePipe(SunGraphics2D sunGraphics2D) {
        sunGraphics2D.drawpipe = nullpipe;
        sunGraphics2D.fillpipe = nullpipe;
        sunGraphics2D.shapepipe = nullpipe;
        sunGraphics2D.textpipe = nullpipe;
        sunGraphics2D.imagepipe = nullpipe;
    }

    @Override // sun.java2d.SurfaceData
    public GraphicsConfiguration getDeviceConfiguration() {
        return null;
    }

    @Override // sun.java2d.SurfaceData
    public Raster getRaster(int i2, int i3, int i4, int i5) {
        throw new InvalidPipeException("should be NOP");
    }

    @Override // sun.java2d.SurfaceData
    public boolean useTightBBoxes() {
        return false;
    }

    @Override // sun.java2d.SurfaceData
    public int pixelFor(int i2) {
        return i2;
    }

    @Override // sun.java2d.SurfaceData
    public int rgbFor(int i2) {
        return i2;
    }

    @Override // sun.java2d.SurfaceData
    public Rectangle getBounds() {
        return new Rectangle();
    }

    @Override // sun.java2d.SurfaceData
    protected void checkCustomComposite() {
    }

    @Override // sun.java2d.SurfaceData
    public boolean copyArea(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7) {
        return true;
    }

    @Override // sun.java2d.SurfaceData
    public Object getDestination() {
        return null;
    }
}
