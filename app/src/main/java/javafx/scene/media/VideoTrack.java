package javafx.scene.media;

import java.util.Map;

/* loaded from: jfxrt.jar:javafx/scene/media/VideoTrack.class */
public final class VideoTrack extends Track {
    private int width;
    private int height;

    public final int getWidth() {
        return this.width;
    }

    public final int getHeight() {
        return this.height;
    }

    VideoTrack(long trackID, Map<String, Object> metadata) {
        super(trackID, metadata);
        Object value = metadata.get("video width");
        if (null != value && (value instanceof Number)) {
            this.width = ((Number) value).intValue();
        }
        Object value2 = metadata.get("video height");
        if (null != value2 && (value2 instanceof Number)) {
            this.height = ((Number) value2).intValue();
        }
    }
}
