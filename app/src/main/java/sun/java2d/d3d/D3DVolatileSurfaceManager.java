package sun.java2d.d3d;

import java.awt.Component;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.image.ColorModel;
import sun.awt.Win32GraphicsConfig;
import sun.awt.image.SunVolatileImage;
import sun.awt.image.SurfaceManager;
import sun.awt.image.VolatileSurfaceManager;
import sun.awt.windows.WComponentPeer;
import sun.java2d.InvalidPipeException;
import sun.java2d.SurfaceData;
import sun.java2d.windows.GDIWindowSurfaceData;

/* loaded from: rt.jar:sun/java2d/d3d/D3DVolatileSurfaceManager.class */
public class D3DVolatileSurfaceManager extends VolatileSurfaceManager {
    private boolean accelerationEnabled;
    private int restoreCountdown;

    public D3DVolatileSurfaceManager(SunVolatileImage sunVolatileImage, Object obj) {
        super(sunVolatileImage, obj);
        int transparency = sunVolatileImage.getTransparency();
        D3DGraphicsDevice d3DGraphicsDevice = (D3DGraphicsDevice) sunVolatileImage.getGraphicsConfig().getDevice();
        this.accelerationEnabled = transparency == 1 || (transparency == 3 && (d3DGraphicsDevice.isCapPresent(2) || d3DGraphicsDevice.isCapPresent(4)));
    }

    @Override // sun.awt.image.VolatileSurfaceManager
    protected boolean isAccelerationEnabled() {
        return this.accelerationEnabled;
    }

    public void setAccelerationEnabled(boolean z2) {
        this.accelerationEnabled = z2;
    }

    @Override // sun.awt.image.VolatileSurfaceManager
    protected SurfaceData initAcceleratedSurface() {
        D3DSurfaceData d3DSurfaceDataCreateData;
        Component component = this.vImg.getComponent();
        WComponentPeer wComponentPeer = component != null ? (WComponentPeer) component.getPeer() : null;
        try {
            boolean zBooleanValue = false;
            if (this.context instanceof Boolean) {
                zBooleanValue = ((Boolean) this.context).booleanValue();
            }
            if (zBooleanValue) {
                d3DSurfaceDataCreateData = D3DSurfaceData.createData(wComponentPeer, this.vImg);
            } else {
                D3DGraphicsConfig d3DGraphicsConfig = (D3DGraphicsConfig) this.vImg.getGraphicsConfig();
                ColorModel colorModel = d3DGraphicsConfig.getColorModel(this.vImg.getTransparency());
                int forcedAccelSurfaceType = this.vImg.getForcedAccelSurfaceType();
                if (forcedAccelSurfaceType == 0) {
                    forcedAccelSurfaceType = 5;
                }
                d3DSurfaceDataCreateData = D3DSurfaceData.createData(d3DGraphicsConfig, this.vImg.getWidth(), this.vImg.getHeight(), colorModel, this.vImg, forcedAccelSurfaceType);
            }
        } catch (NullPointerException e2) {
            d3DSurfaceDataCreateData = null;
        } catch (OutOfMemoryError e3) {
            d3DSurfaceDataCreateData = null;
        } catch (InvalidPipeException e4) {
            d3DSurfaceDataCreateData = null;
        }
        return d3DSurfaceDataCreateData;
    }

    @Override // sun.awt.image.VolatileSurfaceManager
    protected boolean isConfigValid(GraphicsConfiguration graphicsConfiguration) {
        return graphicsConfiguration == null || graphicsConfiguration == this.vImg.getGraphicsConfig();
    }

    private synchronized void setRestoreCountdown(int i2) {
        this.restoreCountdown = i2;
    }

    @Override // sun.awt.image.VolatileSurfaceManager
    protected void restoreAcceleratedSurface() {
        synchronized (this) {
            if (this.restoreCountdown > 0) {
                this.restoreCountdown--;
                throw new InvalidPipeException("Will attempt to restore surface  in " + this.restoreCountdown);
            }
        }
        SurfaceData surfaceDataInitAcceleratedSurface = initAcceleratedSurface();
        if (surfaceDataInitAcceleratedSurface != null) {
            this.sdAccel = surfaceDataInitAcceleratedSurface;
            return;
        }
        throw new InvalidPipeException("could not restore surface");
    }

    @Override // sun.awt.image.VolatileSurfaceManager, sun.awt.image.SurfaceManager
    public SurfaceData restoreContents() {
        acceleratedSurfaceLost();
        return super.restoreContents();
    }

    static void handleVItoScreenOp(SurfaceData surfaceData, SurfaceData surfaceData2) {
        D3DVolatileSurfaceManager d3DVolatileSurfaceManager;
        if ((surfaceData instanceof D3DSurfaceData) && (surfaceData2 instanceof GDIWindowSurfaceData)) {
            D3DSurfaceData d3DSurfaceData = (D3DSurfaceData) surfaceData;
            SurfaceManager manager = SurfaceManager.getManager((Image) d3DSurfaceData.getDestination());
            if ((manager instanceof D3DVolatileSurfaceManager) && (d3DVolatileSurfaceManager = (D3DVolatileSurfaceManager) manager) != null) {
                d3DSurfaceData.setSurfaceLost(true);
                WComponentPeer peer = ((GDIWindowSurfaceData) surfaceData2).getPeer();
                if (D3DScreenUpdateManager.canUseD3DOnScreen(peer, (Win32GraphicsConfig) peer.getGraphicsConfiguration(), peer.getBackBuffersNum())) {
                    d3DVolatileSurfaceManager.setRestoreCountdown(10);
                } else {
                    d3DVolatileSurfaceManager.setAccelerationEnabled(false);
                }
            }
        }
    }

    @Override // sun.awt.image.VolatileSurfaceManager
    public void initContents() {
        if (this.vImg.getForcedAccelSurfaceType() != 3) {
            super.initContents();
        }
    }
}
