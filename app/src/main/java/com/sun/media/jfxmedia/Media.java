package com.sun.media.jfxmedia;

import com.sun.media.jfxmedia.locator.Locator;
import com.sun.media.jfxmedia.track.Track;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/media/jfxmedia/Media.class */
public abstract class Media {
    private Locator locator;
    private final List<Track> tracks = new ArrayList();

    public abstract void addMarker(String str, double d2);

    public abstract double removeMarker(String str);

    public abstract void removeAllMarkers();

    public abstract Map<String, Double> getMarkers();

    protected Media(Locator locator) {
        if (locator == null) {
            throw new IllegalArgumentException("locator == null!");
        }
        this.locator = locator;
    }

    public List<Track> getTracks() {
        List<Track> returnValue;
        synchronized (this.tracks) {
            if (this.tracks.isEmpty()) {
                returnValue = null;
            } else {
                returnValue = Collections.unmodifiableList(new ArrayList(this.tracks));
            }
        }
        return returnValue;
    }

    public Locator getLocator() {
        return this.locator;
    }

    protected void addTrack(Track track) {
        if (track == null) {
            throw new IllegalArgumentException("track == null!");
        }
        synchronized (this.tracks) {
            this.tracks.add(track);
        }
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        if (this.tracks != null && !this.tracks.isEmpty()) {
            for (Track track : this.tracks) {
                buffer.append((Object) track);
                buffer.append("\n");
            }
        }
        return buffer.toString();
    }
}
