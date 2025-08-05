package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.Media;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.track.Track;
import com.sun.media.jfxmediaimpl.platform.Platform;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: jfxrt.jar:com/sun/media/jfxmediaimpl/NativeMedia.class */
public abstract class NativeMedia extends Media {
    protected final Lock markerLock;
    protected final Lock listenerLock;
    protected Map<String, Double> markersByName;
    protected NavigableMap<Double, String> markersByTime;
    protected WeakHashMap<MarkerStateListener, Boolean> markerListeners;

    public abstract Platform getPlatform();

    public abstract void dispose();

    protected NativeMedia(Locator locator) {
        super(locator);
        this.markerLock = new ReentrantLock();
        this.listenerLock = new ReentrantLock();
    }

    @Override // com.sun.media.jfxmedia.Media
    public void addTrack(Track track) {
        super.addTrack(track);
    }

    @Override // com.sun.media.jfxmedia.Media
    public void addMarker(String markerName, double presentationTime) {
        if (markerName == null) {
            throw new IllegalArgumentException("markerName == null!");
        }
        if (presentationTime < 0.0d) {
            throw new IllegalArgumentException("presentationTime < 0");
        }
        this.markerLock.lock();
        try {
            if (this.markersByName == null) {
                this.markersByName = new HashMap();
                this.markersByTime = new TreeMap();
            }
            this.markersByName.put(markerName, Double.valueOf(presentationTime));
            this.markersByTime.put(Double.valueOf(presentationTime), markerName);
            this.markerLock.unlock();
            fireMarkerStateEvent(true);
        } catch (Throwable th) {
            this.markerLock.unlock();
            throw th;
        }
    }

    @Override // com.sun.media.jfxmedia.Media
    public Map<String, Double> getMarkers() {
        Map<String, Double> markers = null;
        this.markerLock.lock();
        try {
            if (this.markersByName != null && !this.markersByName.isEmpty()) {
                markers = Collections.unmodifiableMap(this.markersByName);
            }
            return markers;
        } finally {
            this.markerLock.unlock();
        }
    }

    @Override // com.sun.media.jfxmedia.Media
    public double removeMarker(String markerName) {
        if (markerName == null) {
            throw new IllegalArgumentException("markerName == null!");
        }
        double time = -1.0d;
        boolean hasMarkers = false;
        this.markerLock.lock();
        try {
            if (this.markersByName.containsKey(markerName)) {
                time = this.markersByName.get(markerName).doubleValue();
                this.markersByName.remove(markerName);
                this.markersByTime.remove(Double.valueOf(time));
                hasMarkers = this.markersByName.size() > 0;
            }
            fireMarkerStateEvent(hasMarkers);
            return time;
        } finally {
            this.markerLock.unlock();
        }
    }

    @Override // com.sun.media.jfxmedia.Media
    public void removeAllMarkers() {
        this.markerLock.lock();
        try {
            this.markersByName.clear();
            this.markersByTime.clear();
            fireMarkerStateEvent(false);
        } finally {
            this.markerLock.unlock();
        }
    }

    Map.Entry<Double, String> getNextMarker(double time, boolean inclusive) {
        Map.Entry<Double, String> entry = null;
        this.markerLock.lock();
        try {
            if (this.markersByTime != null) {
                if (inclusive) {
                    entry = this.markersByTime.ceilingEntry(Double.valueOf(time));
                } else {
                    entry = this.markersByTime.higherEntry(Double.valueOf(time));
                }
            }
            return entry;
        } finally {
            this.markerLock.unlock();
        }
    }

    void addMarkerStateListener(MarkerStateListener listener) {
        if (listener != null) {
            this.listenerLock.lock();
            try {
                if (this.markerListeners == null) {
                    this.markerListeners = new WeakHashMap<>();
                }
                this.markerListeners.put(listener, Boolean.TRUE);
            } finally {
                this.listenerLock.unlock();
            }
        }
    }

    void removeMarkerStateListener(MarkerStateListener listener) {
        if (listener != null) {
            this.listenerLock.lock();
            try {
                if (this.markerListeners != null) {
                    this.markerListeners.remove(listener);
                }
            } finally {
                this.listenerLock.unlock();
            }
        }
    }

    void fireMarkerStateEvent(boolean hasMarkers) {
        this.listenerLock.lock();
        try {
            if (this.markerListeners != null && !this.markerListeners.isEmpty()) {
                for (MarkerStateListener listener : this.markerListeners.keySet()) {
                    if (listener != null) {
                        listener.markerStateChanged(hasMarkers);
                    }
                }
            }
        } finally {
            this.listenerLock.unlock();
        }
    }
}
