package javafx.scene.media;

import java.util.Locale;
import java.util.Map;

/* loaded from: jfxrt.jar:javafx/scene/media/AudioTrack.class */
public final class AudioTrack extends Track {
    @Deprecated
    public final String getLanguage() {
        Locale l2 = getLocale();
        if (null == l2) {
            return null;
        }
        return l2.getLanguage();
    }

    AudioTrack(long trackID, Map<String, Object> metadata) {
        super(trackID, metadata);
    }
}
