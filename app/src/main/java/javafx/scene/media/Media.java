package javafx.scene.media;

import com.sun.media.jfxmedia.MediaManager;
import com.sun.media.jfxmedia.MetadataParser;
import com.sun.media.jfxmedia.events.MetadataListener;
import com.sun.media.jfxmedia.locator.Locator;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Pack200;
import javafx.application.Platform;
import javafx.beans.NamedArg;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.image.Image;
import javafx.scene.media.MediaException;
import javafx.util.Duration;
import jdk.jfr.Enabled;

/* loaded from: jfxrt.jar:javafx/scene/media/Media.class */
public final class Media {
    private ReadOnlyObjectWrapper<MediaException> error;
    private ObjectProperty<Runnable> onError;
    private ObservableMap<String, Object> metadata;
    private ReadOnlyIntegerWrapper width;
    private ReadOnlyIntegerWrapper height;
    private ReadOnlyObjectWrapper<Duration> duration;
    private ObservableList<Track> tracks;
    private final String source;
    private final Locator jfxLocator;
    private MetadataParser jfxParser;
    private MetadataListener metadataListener = new _MetadataListener();
    private final ObservableMap<String, Object> metadataBacking = FXCollections.observableMap(new HashMap());
    private final ObservableList<Track> tracksBacking = FXCollections.observableArrayList();
    private ObservableMap<String, Duration> markers = FXCollections.observableMap(new HashMap());

    private void setError(MediaException value) {
        if (getError() == null) {
            errorPropertyImpl().set(value);
        }
    }

    public final MediaException getError() {
        if (this.error == null) {
            return null;
        }
        return this.error.get();
    }

    public ReadOnlyObjectProperty<MediaException> errorProperty() {
        return errorPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyObjectWrapper<MediaException> errorPropertyImpl() {
        if (this.error == null) {
            this.error = new ReadOnlyObjectWrapper<MediaException>() { // from class: javafx.scene.media.Media.1
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    if (Media.this.getOnError() != null) {
                        Platform.runLater(Media.this.getOnError());
                    }
                }

                @Override // javafx.beans.property.SimpleObjectProperty, javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Media.this;
                }

                @Override // javafx.beans.property.SimpleObjectProperty, javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return Pack200.Packer.ERROR;
                }
            };
        }
        return this.error;
    }

    public final void setOnError(Runnable value) {
        onErrorProperty().set(value);
    }

    public final Runnable getOnError() {
        if (this.onError == null) {
            return null;
        }
        return this.onError.get();
    }

    public ObjectProperty<Runnable> onErrorProperty() {
        if (this.onError == null) {
            this.onError = new ObjectPropertyBase<Runnable>() { // from class: javafx.scene.media.Media.2
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    if (get() != null && Media.this.getError() != null) {
                        Platform.runLater(get());
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Media.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onError";
                }
            };
        }
        return this.onError;
    }

    public final ObservableMap<String, Object> getMetadata() {
        return this.metadata;
    }

    final void setWidth(int value) {
        widthPropertyImpl().set(value);
    }

    public final int getWidth() {
        if (this.width == null) {
            return 0;
        }
        return this.width.get();
    }

    public ReadOnlyIntegerProperty widthProperty() {
        return widthPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyIntegerWrapper widthPropertyImpl() {
        if (this.width == null) {
            this.width = new ReadOnlyIntegerWrapper(this, MetadataParser.WIDTH_TAG_NAME);
        }
        return this.width;
    }

    final void setHeight(int value) {
        heightPropertyImpl().set(value);
    }

    public final int getHeight() {
        if (this.height == null) {
            return 0;
        }
        return this.height.get();
    }

    public ReadOnlyIntegerProperty heightProperty() {
        return heightPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyIntegerWrapper heightPropertyImpl() {
        if (this.height == null) {
            this.height = new ReadOnlyIntegerWrapper(this, MetadataParser.HEIGHT_TAG_NAME);
        }
        return this.height;
    }

    final void setDuration(Duration value) {
        durationPropertyImpl().set(value);
    }

    public final Duration getDuration() {
        return (this.duration == null || this.duration.get() == null) ? Duration.UNKNOWN : this.duration.get();
    }

    public ReadOnlyObjectProperty<Duration> durationProperty() {
        return durationPropertyImpl().getReadOnlyProperty();
    }

    private ReadOnlyObjectWrapper<Duration> durationPropertyImpl() {
        if (this.duration == null) {
            this.duration = new ReadOnlyObjectWrapper<>(this, "duration");
        }
        return this.duration;
    }

    public final ObservableList<Track> getTracks() {
        return this.tracks;
    }

    public final ObservableMap<String, Duration> getMarkers() {
        return this.markers;
    }

    public Media(@NamedArg("source") String source) {
        this.source = source;
        try {
            URI uri = new URI(source);
            this.metadata = FXCollections.unmodifiableObservableMap(this.metadataBacking);
            this.tracks = FXCollections.unmodifiableObservableList(this.tracksBacking);
            try {
                Locator locator = new Locator(uri);
                this.jfxLocator = locator;
                if (locator.canBlock()) {
                    InitLocator locatorInit = new InitLocator();
                    Thread t2 = new Thread(locatorInit);
                    t2.setDaemon(true);
                    t2.start();
                } else {
                    locator.init();
                    runMetadataParser();
                }
            } catch (com.sun.media.jfxmedia.MediaException me) {
                throw new MediaException(MediaException.Type.MEDIA_UNSUPPORTED, me.getMessage());
            } catch (FileNotFoundException fnfe) {
                throw new MediaException(MediaException.Type.MEDIA_UNAVAILABLE, fnfe.getMessage());
            } catch (IOException ioe) {
                throw new MediaException(MediaException.Type.MEDIA_INACCESSIBLE, ioe.getMessage());
            } catch (URISyntaxException use) {
                throw new IllegalArgumentException(use);
            }
        } catch (URISyntaxException use2) {
            throw new IllegalArgumentException(use2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runMetadataParser() {
        try {
            this.jfxParser = MediaManager.getMetadataParser(this.jfxLocator);
            this.jfxParser.addListener(this.metadataListener);
            this.jfxParser.startParser();
        } catch (Exception e2) {
            this.jfxParser = null;
        }
    }

    public String getSource() {
        return this.source;
    }

    Locator retrieveJfxLocator() {
        return this.jfxLocator;
    }

    private Track getTrackWithID(long trackID) {
        for (Track track : this.tracksBacking) {
            if (track.getTrackID() == trackID) {
                return track;
            }
        }
        return null;
    }

    void _updateMedia(com.sun.media.jfxmedia.Media _media) {
        try {
            List<com.sun.media.jfxmedia.track.Track> trackList = _media.getTracks();
            if (trackList != null) {
                for (com.sun.media.jfxmedia.track.Track trackElement : trackList) {
                    long trackID = trackElement.getTrackID();
                    if (getTrackWithID(trackID) == null) {
                        Track newTrack = null;
                        Map<String, Object> trackMetadata = new HashMap<>();
                        if (null != trackElement.getName()) {
                            trackMetadata.put("name", trackElement.getName());
                        }
                        if (null != trackElement.getLocale()) {
                            trackMetadata.put("locale", trackElement.getLocale());
                        }
                        trackMetadata.put("encoding", trackElement.getEncodingType().toString());
                        trackMetadata.put(Enabled.NAME, Boolean.valueOf(trackElement.isEnabled()));
                        if (trackElement instanceof com.sun.media.jfxmedia.track.VideoTrack) {
                            com.sun.media.jfxmedia.track.VideoTrack vt = (com.sun.media.jfxmedia.track.VideoTrack) trackElement;
                            int videoWidth = vt.getFrameSize().getWidth();
                            int videoHeight = vt.getFrameSize().getHeight();
                            setWidth(videoWidth);
                            setHeight(videoHeight);
                            trackMetadata.put("video width", Integer.valueOf(videoWidth));
                            trackMetadata.put("video height", Integer.valueOf(videoHeight));
                            newTrack = new VideoTrack(trackElement.getTrackID(), trackMetadata);
                        } else if (trackElement instanceof com.sun.media.jfxmedia.track.AudioTrack) {
                            newTrack = new AudioTrack(trackElement.getTrackID(), trackMetadata);
                        } else if (trackElement instanceof com.sun.media.jfxmedia.track.SubtitleTrack) {
                            newTrack = new SubtitleTrack(trackID, trackMetadata);
                        }
                        if (null != newTrack) {
                            this.tracksBacking.add(newTrack);
                        }
                    }
                }
            }
        } catch (Exception e2) {
            setError(new MediaException(MediaException.Type.UNKNOWN, e2));
        }
    }

    void _setError(MediaException.Type type, String message) {
        setError(new MediaException(type, message));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void updateMetadata(Map<String, Object> metadata) {
        if (metadata != null) {
            for (Map.Entry<String, Object> entry : metadata.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (key.equals(MetadataParser.IMAGE_TAG_NAME) && (value instanceof byte[])) {
                    byte[] imageData = (byte[]) value;
                    Image image = new Image((InputStream) new ByteArrayInputStream(imageData));
                    if (!image.isError()) {
                        this.metadataBacking.put(MetadataParser.IMAGE_TAG_NAME, image);
                    }
                } else if (key.equals("duration") && (value instanceof Long)) {
                    Duration d2 = new Duration(((Long) value).longValue());
                    if (d2 != null) {
                        this.metadataBacking.put("duration", d2);
                    }
                } else {
                    this.metadataBacking.put(key, value);
                }
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/media/Media$_MetadataListener.class */
    private class _MetadataListener implements MetadataListener {
        private _MetadataListener() {
        }

        @Override // com.sun.media.jfxmedia.events.MetadataListener
        public void onMetadata(Map<String, Object> metadata) {
            Platform.runLater(() -> {
                Media.this.updateMetadata(metadata);
                Media.this.jfxParser.removeListener(Media.this.metadataListener);
                Media.this.jfxParser.stopParser();
                Media.this.jfxParser = null;
            });
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/media/Media$InitLocator.class */
    private class InitLocator implements Runnable {
        private InitLocator() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                Media.this.jfxLocator.init();
                Media.this.runMetadataParser();
            } catch (com.sun.media.jfxmedia.MediaException me) {
                Media.this._setError(MediaException.Type.MEDIA_UNSUPPORTED, me.getMessage());
            } catch (FileNotFoundException fnfe) {
                Media.this._setError(MediaException.Type.MEDIA_UNAVAILABLE, fnfe.getMessage());
            } catch (IOException ioe) {
                Media.this._setError(MediaException.Type.MEDIA_INACCESSIBLE, ioe.getMessage());
            } catch (URISyntaxException use) {
                Media.this._setError(MediaException.Type.OPERATION_UNSUPPORTED, use.getMessage());
            } catch (Exception e2) {
                Media.this._setError(MediaException.Type.UNKNOWN, e2.getMessage());
            }
        }
    }
}
