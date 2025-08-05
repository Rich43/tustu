package sun.awt.image;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.ImageCapabilities;
import java.awt.image.BufferedImage;
import sun.awt.DisplayChangedListener;
import sun.java2d.InvalidPipeException;
import sun.java2d.SunGraphicsEnvironment;
import sun.java2d.SurfaceData;

/* loaded from: rt.jar:sun/awt/image/VolatileSurfaceManager.class */
public abstract class VolatileSurfaceManager extends SurfaceManager implements DisplayChangedListener {
    protected SunVolatileImage vImg;
    protected SurfaceData sdAccel;
    protected SurfaceData sdBackup;
    protected SurfaceData sdCurrent;
    protected SurfaceData sdPrevious;
    protected boolean lostSurface;
    protected Object context;

    protected abstract boolean isAccelerationEnabled();

    protected abstract SurfaceData initAcceleratedSurface();

    protected VolatileSurfaceManager(SunVolatileImage sunVolatileImage, Object obj) {
        this.vImg = sunVolatileImage;
        this.context = obj;
        GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        if (localGraphicsEnvironment instanceof SunGraphicsEnvironment) {
            ((SunGraphicsEnvironment) localGraphicsEnvironment).addDisplayChangedListener(this);
        }
    }

    public void initialize() {
        if (isAccelerationEnabled()) {
            this.sdAccel = initAcceleratedSurface();
            if (this.sdAccel != null) {
                this.sdCurrent = this.sdAccel;
            }
        }
        if (this.sdCurrent == null && this.vImg.getForcedAccelSurfaceType() == 0) {
            this.sdCurrent = getBackupSurface();
        }
    }

    @Override // sun.awt.image.SurfaceManager
    public SurfaceData getPrimarySurfaceData() {
        return this.sdCurrent;
    }

    public int validate(GraphicsConfiguration graphicsConfiguration) {
        int i2 = 0;
        boolean z2 = this.lostSurface;
        this.lostSurface = false;
        if (isAccelerationEnabled()) {
            if (!isConfigValid(graphicsConfiguration)) {
                i2 = 2;
            } else if (this.sdAccel == null) {
                this.sdAccel = initAcceleratedSurface();
                if (this.sdAccel != null) {
                    this.sdCurrent = this.sdAccel;
                    this.sdBackup = null;
                    i2 = 1;
                } else {
                    this.sdCurrent = getBackupSurface();
                }
            } else if (this.sdAccel.isSurfaceLost()) {
                try {
                    restoreAcceleratedSurface();
                    this.sdCurrent = this.sdAccel;
                    this.sdAccel.setSurfaceLost(false);
                    this.sdBackup = null;
                    i2 = 1;
                } catch (InvalidPipeException e2) {
                    this.sdCurrent = getBackupSurface();
                }
            } else if (z2) {
                i2 = 1;
            }
        } else if (this.sdAccel != null) {
            this.sdCurrent = getBackupSurface();
            this.sdAccel = null;
            i2 = 1;
        }
        if (i2 != 2 && this.sdCurrent != this.sdPrevious) {
            this.sdPrevious = this.sdCurrent;
            i2 = 1;
        }
        if (i2 == 1) {
            initContents();
        }
        return i2;
    }

    public boolean contentsLost() {
        return this.lostSurface;
    }

    protected SurfaceData getBackupSurface() {
        if (this.sdBackup == null) {
            BufferedImage backupImage = this.vImg.getBackupImage();
            SunWritableRaster.stealTrackable(backupImage.getRaster().getDataBuffer()).setUntrackable();
            this.sdBackup = BufImgSurfaceData.createData(backupImage);
        }
        return this.sdBackup;
    }

    public void initContents() {
        if (this.sdCurrent != null) {
            Graphics2D graphics2DCreateGraphics = this.vImg.createGraphics();
            graphics2DCreateGraphics.clearRect(0, 0, this.vImg.getWidth(), this.vImg.getHeight());
            graphics2DCreateGraphics.dispose();
        }
    }

    @Override // sun.awt.image.SurfaceManager
    public SurfaceData restoreContents() {
        return getBackupSurface();
    }

    @Override // sun.awt.image.SurfaceManager
    public void acceleratedSurfaceLost() {
        if (isAccelerationEnabled() && this.sdCurrent == this.sdAccel) {
            this.lostSurface = true;
        }
    }

    protected void restoreAcceleratedSurface() {
    }

    @Override // sun.awt.DisplayChangedListener
    public void displayChanged() {
        if (!isAccelerationEnabled()) {
            return;
        }
        this.lostSurface = true;
        if (this.sdAccel != null) {
            this.sdBackup = null;
            SurfaceData surfaceData = this.sdAccel;
            this.sdAccel = null;
            surfaceData.invalidate();
            this.sdCurrent = getBackupSurface();
        }
        this.vImg.updateGraphicsConfig();
    }

    @Override // sun.awt.DisplayChangedListener
    public void paletteChanged() {
        this.lostSurface = true;
    }

    protected boolean isConfigValid(GraphicsConfiguration graphicsConfiguration) {
        return graphicsConfiguration == null || graphicsConfiguration.getDevice() == this.vImg.getGraphicsConfig().getDevice();
    }

    @Override // sun.awt.image.SurfaceManager
    public ImageCapabilities getCapabilities(GraphicsConfiguration graphicsConfiguration) {
        if (isConfigValid(graphicsConfiguration)) {
            return isAccelerationEnabled() ? new AcceleratedImageCapabilities() : new ImageCapabilities(false);
        }
        return super.getCapabilities(graphicsConfiguration);
    }

    /* loaded from: rt.jar:sun/awt/image/VolatileSurfaceManager$AcceleratedImageCapabilities.class */
    private class AcceleratedImageCapabilities extends ImageCapabilities {
        AcceleratedImageCapabilities() {
            super(false);
        }

        @Override // java.awt.ImageCapabilities
        public boolean isAccelerated() {
            return VolatileSurfaceManager.this.sdCurrent == VolatileSurfaceManager.this.sdAccel;
        }

        @Override // java.awt.ImageCapabilities
        public boolean isTrueVolatile() {
            return isAccelerated();
        }
    }

    @Override // sun.awt.image.SurfaceManager
    public void flush() {
        this.lostSurface = true;
        SurfaceData surfaceData = this.sdAccel;
        this.sdAccel = null;
        if (surfaceData != null) {
            surfaceData.flush();
        }
    }
}
