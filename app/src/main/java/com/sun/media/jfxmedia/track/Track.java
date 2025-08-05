package com.sun.media.jfxmedia.track;

import java.util.Locale;

/* loaded from: jfxrt.jar:com/sun/media/jfxmedia/track/Track.class */
public abstract class Track {
    private boolean trackEnabled;
    private long trackID;
    private String name;
    private Locale locale;
    private Encoding encoding;

    /* loaded from: jfxrt.jar:com/sun/media/jfxmedia/track/Track$Encoding.class */
    public enum Encoding {
        NONE,
        PCM,
        MPEG1AUDIO,
        MPEG1LAYER3,
        AAC,
        H264,
        VP6,
        CUSTOM;

        public static Encoding toEncoding(int ordinal) {
            for (Encoding value : values()) {
                if (value.ordinal() == ordinal) {
                    return value;
                }
            }
            return NONE;
        }
    }

    protected Track(boolean enabled, long trackID, String name, Locale locale, Encoding encoding) {
        if (name == null) {
            throw new IllegalArgumentException("name == null!");
        }
        if (encoding == null) {
            throw new IllegalArgumentException("encoding == null!");
        }
        this.trackEnabled = enabled;
        this.trackID = trackID;
        this.locale = locale;
        this.encoding = encoding;
        this.name = name;
    }

    public Encoding getEncodingType() {
        return this.encoding;
    }

    public String getName() {
        return this.name;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public long getTrackID() {
        return this.trackID;
    }

    public boolean isEnabled() {
        return this.trackEnabled;
    }
}
