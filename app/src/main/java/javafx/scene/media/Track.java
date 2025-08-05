package javafx.scene.media;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

/* loaded from: jfxrt.jar:javafx/scene/media/Track.class */
public abstract class Track {
    private String name;
    private long trackID;
    private Locale locale;
    private Map<String, Object> metadata;
    private String description;

    public final String getName() {
        return this.name;
    }

    public final Locale getLocale() {
        return this.locale;
    }

    public final long getTrackID() {
        return this.trackID;
    }

    public final Map<String, Object> getMetadata() {
        return this.metadata;
    }

    Track(long trackID, Map<String, Object> metadata) {
        this.trackID = trackID;
        Object value = metadata.get("name");
        if (null != value && (value instanceof String)) {
            this.name = (String) value;
        }
        Object value2 = metadata.get("locale");
        if (null != value2 && (value2 instanceof Locale)) {
            this.locale = (Locale) value2;
        }
        this.metadata = Collections.unmodifiableMap(metadata);
    }

    public final String toString() {
        synchronized (this) {
            if (null == this.description) {
                StringBuilder sb = new StringBuilder();
                Map<String, Object> md = getMetadata();
                sb.append(getClass().getName());
                sb.append("[ track id = ");
                sb.append(this.trackID);
                for (Map.Entry<String, Object> entry : md.entrySet()) {
                    Object value = entry.getValue();
                    if (null != value) {
                        sb.append(", ");
                        sb.append(entry.getKey());
                        sb.append(" = ");
                        sb.append(value.toString());
                    }
                }
                sb.append("]");
                this.description = sb.toString();
            }
        }
        return this.description;
    }
}
