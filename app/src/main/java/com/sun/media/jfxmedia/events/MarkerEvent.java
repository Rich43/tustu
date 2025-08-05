package com.sun.media.jfxmedia.events;

/* loaded from: jfxrt.jar:com/sun/media/jfxmedia/events/MarkerEvent.class */
public class MarkerEvent extends PlayerEvent {
    private String markerName;
    private double presentationTime;

    public MarkerEvent(String name, double time) {
        if (name == null) {
            throw new IllegalArgumentException("name == null!");
        }
        if (time < 0.0d) {
            throw new IllegalArgumentException("time < 0.0!");
        }
        this.markerName = name;
        this.presentationTime = time;
    }

    public String getMarkerName() {
        return this.markerName;
    }

    public double getPresentationTime() {
        return this.presentationTime;
    }
}
