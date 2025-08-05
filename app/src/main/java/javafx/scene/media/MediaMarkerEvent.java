package javafx.scene.media;

import javafx.event.ActionEvent;
import javafx.util.Duration;
import javafx.util.Pair;

/* loaded from: jfxrt.jar:javafx/scene/media/MediaMarkerEvent.class */
public class MediaMarkerEvent extends ActionEvent {
    private static final long serialVersionUID = 20121107;
    private Pair<String, Duration> marker;

    MediaMarkerEvent(Pair<String, Duration> marker) {
        this.marker = marker;
    }

    public Pair<String, Duration> getMarker() {
        return this.marker;
    }
}
