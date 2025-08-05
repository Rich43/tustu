package sun.java2d;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.security.AccessController;
import sun.awt.DisplayChangedListener;
import sun.awt.image.SurfaceManager;
import sun.java2d.StateTrackable;
import sun.java2d.loops.Blit;
import sun.java2d.loops.BlitBg;
import sun.java2d.loops.CompositeType;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/java2d/SurfaceDataProxy.class */
public abstract class SurfaceDataProxy implements DisplayChangedListener, SurfaceManager.FlushableCacheData {
    private static boolean cachingAllowed;
    private static int defaultThreshold;
    public static SurfaceDataProxy UNCACHED;
    private int threshold;
    private StateTracker srcTracker;
    private int numtries;
    private SurfaceData cachedSD;
    private StateTracker cacheTracker;
    private boolean valid;

    public abstract boolean isSupportedOperation(SurfaceData surfaceData, int i2, CompositeType compositeType, Color color);

    public abstract SurfaceData validateSurfaceData(SurfaceData surfaceData, SurfaceData surfaceData2, int i2, int i3);

    static {
        cachingAllowed = true;
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("sun.java2d.managedimages"));
        if (str != null && str.equals("false")) {
            cachingAllowed = false;
            System.out.println("Disabling managed images");
        }
        defaultThreshold = 1;
        String str2 = (String) AccessController.doPrivileged(new GetPropertyAction("sun.java2d.accthreshold"));
        if (str2 != null) {
            try {
                int i2 = Integer.parseInt(str2);
                if (i2 >= 0) {
                    defaultThreshold = i2;
                    System.out.println("New Default Acceleration Threshold: " + defaultThreshold);
                }
            } catch (NumberFormatException e2) {
                System.err.println("Error setting new threshold:" + ((Object) e2));
            }
        }
        UNCACHED = new SurfaceDataProxy(0) { // from class: sun.java2d.SurfaceDataProxy.1
            @Override // sun.java2d.SurfaceDataProxy
            public boolean isAccelerated() {
                return false;
            }

            @Override // sun.java2d.SurfaceDataProxy
            public boolean isSupportedOperation(SurfaceData surfaceData, int i3, CompositeType compositeType, Color color) {
                return false;
            }

            @Override // sun.java2d.SurfaceDataProxy
            public SurfaceData validateSurfaceData(SurfaceData surfaceData, SurfaceData surfaceData2, int i3, int i4) {
                throw new InternalError("UNCACHED should never validate SDs");
            }

            @Override // sun.java2d.SurfaceDataProxy
            public SurfaceData replaceData(SurfaceData surfaceData, int i3, CompositeType compositeType, Color color) {
                return surfaceData;
            }
        };
    }

    public static boolean isCachingAllowed() {
        return cachingAllowed;
    }

    public StateTracker getRetryTracker(SurfaceData surfaceData) {
        return new CountdownTracker(this.threshold);
    }

    /* loaded from: rt.jar:sun/java2d/SurfaceDataProxy$CountdownTracker.class */
    public static class CountdownTracker implements StateTracker {
        private int countdown;

        public CountdownTracker(int i2) {
            this.countdown = i2;
        }

        @Override // sun.java2d.StateTracker
        public synchronized boolean isCurrent() {
            int i2 = this.countdown - 1;
            this.countdown = i2;
            return i2 >= 0;
        }
    }

    public SurfaceDataProxy() {
        this(defaultThreshold);
    }

    public SurfaceDataProxy(int i2) {
        this.threshold = i2;
        this.srcTracker = StateTracker.NEVER_CURRENT;
        this.cacheTracker = StateTracker.NEVER_CURRENT;
        this.valid = true;
    }

    public boolean isValid() {
        return this.valid;
    }

    public void invalidate() {
        this.valid = false;
    }

    @Override // sun.awt.image.SurfaceManager.FlushableCacheData
    public boolean flush(boolean z2) {
        if (z2) {
            invalidate();
        }
        flush();
        return !isValid();
    }

    public synchronized void flush() {
        SurfaceData surfaceData = this.cachedSD;
        this.cachedSD = null;
        this.cacheTracker = StateTracker.NEVER_CURRENT;
        if (surfaceData != null) {
            surfaceData.flush();
        }
    }

    public boolean isAccelerated() {
        return isValid() && this.srcTracker.isCurrent() && this.cacheTracker.isCurrent();
    }

    protected void activateDisplayListener() {
        GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        if (localGraphicsEnvironment instanceof SunGraphicsEnvironment) {
            ((SunGraphicsEnvironment) localGraphicsEnvironment).addDisplayChangedListener(this);
        }
    }

    @Override // sun.awt.DisplayChangedListener
    public void displayChanged() {
        flush();
    }

    @Override // sun.awt.DisplayChangedListener
    public void paletteChanged() {
        this.srcTracker = StateTracker.NEVER_CURRENT;
    }

    public SurfaceData replaceData(SurfaceData surfaceData, int i2, CompositeType compositeType, Color color) {
        if (isSupportedOperation(surfaceData, i2, compositeType, color)) {
            if (!this.srcTracker.isCurrent()) {
                synchronized (this) {
                    this.numtries = this.threshold;
                    this.srcTracker = surfaceData.getStateTracker();
                    this.cacheTracker = StateTracker.NEVER_CURRENT;
                }
                if (!this.srcTracker.isCurrent()) {
                    if (surfaceData.getState() == StateTrackable.State.UNTRACKABLE) {
                        invalidate();
                        flush();
                    }
                    return surfaceData;
                }
            }
            SurfaceData surfaceDataValidateSurfaceData = this.cachedSD;
            if (!this.cacheTracker.isCurrent()) {
                synchronized (this) {
                    if (this.numtries > 0) {
                        this.numtries--;
                        return surfaceData;
                    }
                    Rectangle bounds = surfaceData.getBounds();
                    int i3 = bounds.width;
                    int i4 = bounds.height;
                    StateTracker stateTracker = this.srcTracker;
                    surfaceDataValidateSurfaceData = validateSurfaceData(surfaceData, surfaceDataValidateSurfaceData, i3, i4);
                    if (surfaceDataValidateSurfaceData == null) {
                        synchronized (this) {
                            if (stateTracker == this.srcTracker) {
                                this.cacheTracker = getRetryTracker(surfaceData);
                                this.cachedSD = null;
                            }
                        }
                        return surfaceData;
                    }
                    updateSurfaceData(surfaceData, surfaceDataValidateSurfaceData, i3, i4);
                    if (!surfaceDataValidateSurfaceData.isValid()) {
                        return surfaceData;
                    }
                    synchronized (this) {
                        if (stateTracker == this.srcTracker && stateTracker.isCurrent()) {
                            this.cacheTracker = surfaceDataValidateSurfaceData.getStateTracker();
                            this.cachedSD = surfaceDataValidateSurfaceData;
                        }
                    }
                }
            }
            if (surfaceDataValidateSurfaceData != null) {
                return surfaceDataValidateSurfaceData;
            }
        }
        return surfaceData;
    }

    public void updateSurfaceData(SurfaceData surfaceData, SurfaceData surfaceData2, int i2, int i3) {
        Blit.getFromCache(surfaceData.getSurfaceType(), CompositeType.SrcNoEa, surfaceData2.getSurfaceType()).Blit(surfaceData, surfaceData2, AlphaComposite.Src, null, 0, 0, 0, 0, i2, i3);
        surfaceData2.markDirty();
    }

    public void updateSurfaceDataBg(SurfaceData surfaceData, SurfaceData surfaceData2, int i2, int i3, Color color) {
        BlitBg.getFromCache(surfaceData.getSurfaceType(), CompositeType.SrcNoEa, surfaceData2.getSurfaceType()).BlitBg(surfaceData, surfaceData2, AlphaComposite.Src, null, color.getRGB(), 0, 0, 0, 0, i2, i3);
        surfaceData2.markDirty();
    }
}
