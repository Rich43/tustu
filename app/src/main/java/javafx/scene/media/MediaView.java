package javafx.scene.media;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.MediaFrameTracker;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.tk.Toolkit;
import com.sun.media.jfxmedia.control.MediaPlayerOverlay;
import com.sun.media.jfxmedia.events.VideoFrameRateListener;
import com.sun.media.jfxmediaimpl.HostUtils;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:javafx/scene/media/MediaView.class */
public class MediaView extends Node {
    private static final String VIDEO_FRAME_RATE_PROPERTY_NAME = "jfxmedia.decodedVideoFPS";
    private static final String DEFAULT_STYLE_CLASS = "media-view";
    private InvalidationListener errorListener;
    private InvalidationListener mediaDimensionListener;
    private VideoFrameRateListener decodedFrameRateListener;
    private boolean registerVideoFrameRateListener;
    private MediaPlayerOverlay mediaPlayerOverlay;
    private ChangeListener<Parent> parentListener;
    private ChangeListener<Boolean> treeVisibleListener;
    private ChangeListener<Number> opacityListener;
    private ObjectProperty<MediaPlayer> mediaPlayer;
    private ObjectProperty<EventHandler<MediaErrorEvent>> onError;
    private BooleanProperty preserveRatio;
    private BooleanProperty smooth;

    /* renamed from: x, reason: collision with root package name */
    private DoubleProperty f12707x;

    /* renamed from: y, reason: collision with root package name */
    private DoubleProperty f12708y;
    private DoubleProperty fitWidth;
    private DoubleProperty fitHeight;
    private ObjectProperty<Rectangle2D> viewport;
    private int decodedFrameCount;
    private int renderedFrameCount;

    /* loaded from: jfxrt.jar:javafx/scene/media/MediaView$MediaErrorInvalidationListener.class */
    private class MediaErrorInvalidationListener implements InvalidationListener {
        private MediaErrorInvalidationListener() {
        }

        @Override // javafx.beans.InvalidationListener
        public void invalidated(Observable value) {
            ObservableObjectValue<MediaException> errorProperty = (ObservableObjectValue) value;
            MediaView.this.fireEvent(new MediaErrorEvent(MediaView.this.getMediaPlayer(), MediaView.this.getMediaView(), errorProperty.get()));
        }
    }

    private VideoFrameRateListener createVideoFrameRateListener() {
        String listenerProp = null;
        try {
            listenerProp = System.getProperty(VIDEO_FRAME_RATE_PROPERTY_NAME);
        } catch (Throwable th) {
        }
        if (listenerProp == null || !Boolean.getBoolean(VIDEO_FRAME_RATE_PROPERTY_NAME)) {
            return null;
        }
        return videoFrameRate -> {
            Platform.runLater(() -> {
                ObservableMap props = getProperties();
                props.put(VIDEO_FRAME_RATE_PROPERTY_NAME, Double.valueOf(videoFrameRate));
            });
        };
    }

    private void createListeners() {
        this.parentListener = (ov2, oldParent, newParent) -> {
            updateOverlayVisibility();
        };
        this.treeVisibleListener = (ov1, oldVisible, newVisible) -> {
            updateOverlayVisibility();
        };
        this.opacityListener = (ov, oldOpacity, newOpacity) -> {
            updateOverlayOpacity();
        };
    }

    private boolean determineVisibility() {
        return getParent() != null && isVisible();
    }

    private synchronized void updateOverlayVisibility() {
        if (this.mediaPlayerOverlay != null) {
            this.mediaPlayerOverlay.setOverlayVisible(determineVisibility());
        }
    }

    private synchronized void updateOverlayOpacity() {
        if (this.mediaPlayerOverlay != null) {
            this.mediaPlayerOverlay.setOverlayOpacity(getOpacity());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void updateOverlayX() {
        if (this.mediaPlayerOverlay != null) {
            this.mediaPlayerOverlay.setOverlayX(getX());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void updateOverlayY() {
        if (this.mediaPlayerOverlay != null) {
            this.mediaPlayerOverlay.setOverlayY(getY());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void updateOverlayWidth() {
        if (this.mediaPlayerOverlay != null) {
            this.mediaPlayerOverlay.setOverlayWidth(getFitWidth());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void updateOverlayHeight() {
        if (this.mediaPlayerOverlay != null) {
            this.mediaPlayerOverlay.setOverlayHeight(getFitHeight());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void updateOverlayPreserveRatio() {
        if (this.mediaPlayerOverlay != null) {
            this.mediaPlayerOverlay.setOverlayPreserveRatio(isPreserveRatio());
        }
    }

    private static Affine3D calculateNodeToSceneTransform(Node node) {
        Affine3D transform = new Affine3D();
        do {
            transform.preConcatenate(node.impl_getLeafTransform());
            node = node.getParent();
        } while (node != null);
        return transform;
    }

    private void updateOverlayTransform() {
        if (this.mediaPlayerOverlay != null) {
            Affine3D trans = calculateNodeToSceneTransform(this);
            this.mediaPlayerOverlay.setOverlayTransform(trans.getMxx(), trans.getMxy(), trans.getMxz(), trans.getMxt(), trans.getMyx(), trans.getMyy(), trans.getMyz(), trans.getMyt(), trans.getMzx(), trans.getMzy(), trans.getMzz(), trans.getMzt());
        }
    }

    private void updateMediaPlayerOverlay() {
        this.mediaPlayerOverlay.setOverlayX(getX());
        this.mediaPlayerOverlay.setOverlayY(getY());
        this.mediaPlayerOverlay.setOverlayPreserveRatio(isPreserveRatio());
        this.mediaPlayerOverlay.setOverlayWidth(getFitWidth());
        this.mediaPlayerOverlay.setOverlayHeight(getFitHeight());
        this.mediaPlayerOverlay.setOverlayOpacity(getOpacity());
        this.mediaPlayerOverlay.setOverlayVisible(determineVisibility());
        updateOverlayTransform();
    }

    @Override // javafx.scene.Node
    @Deprecated
    public void impl_transformsChanged() {
        super.impl_transformsChanged();
        if (this.mediaPlayerOverlay != null) {
            updateOverlayTransform();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MediaView getMediaView() {
        return this;
    }

    public MediaView() {
        this.errorListener = new MediaErrorInvalidationListener();
        this.mediaDimensionListener = value -> {
            impl_markDirty(DirtyBits.NODE_VIEWPORT);
            impl_geomChanged();
        };
        this.registerVideoFrameRateListener = false;
        this.mediaPlayerOverlay = null;
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setSmooth(Toolkit.getToolkit().getDefaultImageSmooth());
        this.decodedFrameRateListener = createVideoFrameRateListener();
        setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
    }

    public MediaView(MediaPlayer mediaPlayer) {
        this();
        setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        setMediaPlayer(mediaPlayer);
    }

    public final void setMediaPlayer(MediaPlayer value) {
        mediaPlayerProperty().set(value);
    }

    public final MediaPlayer getMediaPlayer() {
        if (this.mediaPlayer == null) {
            return null;
        }
        return this.mediaPlayer.get();
    }

    public final ObjectProperty<MediaPlayer> mediaPlayerProperty() {
        if (this.mediaPlayer == null) {
            this.mediaPlayer = new ObjectPropertyBase<MediaPlayer>() { // from class: javafx.scene.media.MediaView.1
                MediaPlayer oldValue = null;

                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    if (this.oldValue != null) {
                        Media media = this.oldValue.getMedia();
                        if (media != null) {
                            media.widthProperty().removeListener(MediaView.this.mediaDimensionListener);
                            media.heightProperty().removeListener(MediaView.this.mediaDimensionListener);
                        }
                        if (MediaView.this.decodedFrameRateListener != null && MediaView.this.getMediaPlayer().retrieveJfxPlayer() != null) {
                            MediaView.this.getMediaPlayer().retrieveJfxPlayer().getVideoRenderControl().removeVideoFrameRateListener(MediaView.this.decodedFrameRateListener);
                        }
                        this.oldValue.errorProperty().removeListener(MediaView.this.errorListener);
                        this.oldValue.removeView(MediaView.this.getMediaView());
                    }
                    MediaPlayer newValue = get();
                    if (newValue != null) {
                        newValue.addView(MediaView.this.getMediaView());
                        newValue.errorProperty().addListener(MediaView.this.errorListener);
                        if (MediaView.this.decodedFrameRateListener == null || MediaView.this.getMediaPlayer().retrieveJfxPlayer() == null) {
                            if (MediaView.this.decodedFrameRateListener != null) {
                                MediaView.this.registerVideoFrameRateListener = true;
                            }
                        } else {
                            MediaView.this.getMediaPlayer().retrieveJfxPlayer().getVideoRenderControl().addVideoFrameRateListener(MediaView.this.decodedFrameRateListener);
                        }
                        Media media2 = newValue.getMedia();
                        if (media2 != null) {
                            media2.widthProperty().addListener(MediaView.this.mediaDimensionListener);
                            media2.heightProperty().addListener(MediaView.this.mediaDimensionListener);
                        }
                    }
                    MediaView.this.impl_markDirty(DirtyBits.MEDIAVIEW_MEDIA);
                    MediaView.this.impl_geomChanged();
                    this.oldValue = newValue;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MediaView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "mediaPlayer";
                }
            };
        }
        return this.mediaPlayer;
    }

    public final void setOnError(EventHandler<MediaErrorEvent> value) {
        onErrorProperty().set(value);
    }

    public final EventHandler<MediaErrorEvent> getOnError() {
        if (this.onError == null) {
            return null;
        }
        return this.onError.get();
    }

    public final ObjectProperty<EventHandler<MediaErrorEvent>> onErrorProperty() {
        if (this.onError == null) {
            this.onError = new ObjectPropertyBase<EventHandler<MediaErrorEvent>>() { // from class: javafx.scene.media.MediaView.2
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    MediaView.this.setEventHandler(MediaErrorEvent.MEDIA_ERROR, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MediaView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "onError";
                }
            };
        }
        return this.onError;
    }

    public final void setPreserveRatio(boolean value) {
        preserveRatioProperty().set(value);
    }

    public final boolean isPreserveRatio() {
        if (this.preserveRatio == null) {
            return true;
        }
        return this.preserveRatio.get();
    }

    public final BooleanProperty preserveRatioProperty() {
        if (this.preserveRatio == null) {
            this.preserveRatio = new BooleanPropertyBase(true) { // from class: javafx.scene.media.MediaView.3
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    if (HostUtils.isIOS()) {
                        MediaView.this.updateOverlayPreserveRatio();
                    } else {
                        MediaView.this.impl_markDirty(DirtyBits.NODE_VIEWPORT);
                        MediaView.this.impl_geomChanged();
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MediaView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "preserveRatio";
                }
            };
        }
        return this.preserveRatio;
    }

    public final void setSmooth(boolean value) {
        smoothProperty().set(value);
    }

    public final boolean isSmooth() {
        if (this.smooth == null) {
            return false;
        }
        return this.smooth.get();
    }

    public final BooleanProperty smoothProperty() {
        if (this.smooth == null) {
            this.smooth = new BooleanPropertyBase() { // from class: javafx.scene.media.MediaView.4
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    MediaView.this.impl_markDirty(DirtyBits.NODE_SMOOTH);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MediaView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "smooth";
                }
            };
        }
        return this.smooth;
    }

    public final void setX(double value) {
        xProperty().set(value);
    }

    public final double getX() {
        if (this.f12707x == null) {
            return 0.0d;
        }
        return this.f12707x.get();
    }

    public final DoubleProperty xProperty() {
        if (this.f12707x == null) {
            this.f12707x = new DoublePropertyBase() { // from class: javafx.scene.media.MediaView.5
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    if (HostUtils.isIOS()) {
                        MediaView.this.updateOverlayX();
                    } else {
                        MediaView.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                        MediaView.this.impl_geomChanged();
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MediaView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return LanguageTag.PRIVATEUSE;
                }
            };
        }
        return this.f12707x;
    }

    public final void setY(double value) {
        yProperty().set(value);
    }

    public final double getY() {
        if (this.f12708y == null) {
            return 0.0d;
        }
        return this.f12708y.get();
    }

    public final DoubleProperty yProperty() {
        if (this.f12708y == null) {
            this.f12708y = new DoublePropertyBase() { // from class: javafx.scene.media.MediaView.6
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    if (HostUtils.isIOS()) {
                        MediaView.this.updateOverlayY();
                    } else {
                        MediaView.this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
                        MediaView.this.impl_geomChanged();
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MediaView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return PdfOps.y_TOKEN;
                }
            };
        }
        return this.f12708y;
    }

    public final void setFitWidth(double value) {
        fitWidthProperty().set(value);
    }

    public final double getFitWidth() {
        if (this.fitWidth == null) {
            return 0.0d;
        }
        return this.fitWidth.get();
    }

    public final DoubleProperty fitWidthProperty() {
        if (this.fitWidth == null) {
            this.fitWidth = new DoublePropertyBase() { // from class: javafx.scene.media.MediaView.7
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    if (HostUtils.isIOS()) {
                        MediaView.this.updateOverlayWidth();
                    } else {
                        MediaView.this.impl_markDirty(DirtyBits.NODE_VIEWPORT);
                        MediaView.this.impl_geomChanged();
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MediaView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "fitWidth";
                }
            };
        }
        return this.fitWidth;
    }

    public final void setFitHeight(double value) {
        fitHeightProperty().set(value);
    }

    public final double getFitHeight() {
        if (this.fitHeight == null) {
            return 0.0d;
        }
        return this.fitHeight.get();
    }

    public final DoubleProperty fitHeightProperty() {
        if (this.fitHeight == null) {
            this.fitHeight = new DoublePropertyBase() { // from class: javafx.scene.media.MediaView.8
                @Override // javafx.beans.property.DoublePropertyBase
                protected void invalidated() {
                    if (HostUtils.isIOS()) {
                        MediaView.this.updateOverlayHeight();
                    } else {
                        MediaView.this.impl_markDirty(DirtyBits.NODE_VIEWPORT);
                        MediaView.this.impl_geomChanged();
                    }
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MediaView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "fitHeight";
                }
            };
        }
        return this.fitHeight;
    }

    public final void setViewport(Rectangle2D value) {
        viewportProperty().set(value);
    }

    public final Rectangle2D getViewport() {
        if (this.viewport == null) {
            return null;
        }
        return this.viewport.get();
    }

    public final ObjectProperty<Rectangle2D> viewportProperty() {
        if (this.viewport == null) {
            this.viewport = new ObjectPropertyBase<Rectangle2D>() { // from class: javafx.scene.media.MediaView.9
                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    MediaView.this.impl_markDirty(DirtyBits.NODE_VIEWPORT);
                    MediaView.this.impl_geomChanged();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MediaView.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "viewport";
                }
            };
        }
        return this.viewport;
    }

    void notifyMediaChange() {
        MediaPlayer player = getMediaPlayer();
        if (player != null) {
            NGMediaView peer = (NGMediaView) impl_getPeer();
            peer.setMediaProvider(player);
        }
        impl_markDirty(DirtyBits.MEDIAVIEW_MEDIA);
        impl_geomChanged();
    }

    void notifyMediaSizeChange() {
        impl_markDirty(DirtyBits.NODE_VIEWPORT);
        impl_geomChanged();
    }

    void notifyMediaFrameUpdated() {
        this.decodedFrameCount++;
        impl_markDirty(DirtyBits.NODE_CONTENTS);
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected NGNode impl_createPeer() {
        NGMediaView peer = new NGMediaView();
        peer.setFrameTracker(new MediaViewFrameTracker());
        return peer;
    }

    @Override // javafx.scene.Node
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        Media media = getMediaPlayer() == null ? null : getMediaPlayer().getMedia();
        double w2 = media != null ? media.getWidth() : 0.0d;
        double h2 = media != null ? media.getHeight() : 0.0d;
        double newW = getFitWidth();
        double newH = getFitHeight();
        double vw = getViewport() != null ? getViewport().getWidth() : 0.0d;
        double vh = getViewport() != null ? getViewport().getHeight() : 0.0d;
        if (vw > 0.0d && vh > 0.0d) {
            w2 = vw;
            h2 = vh;
        }
        if (getFitWidth() <= 0.0d && getFitHeight() <= 0.0d) {
            newW = w2;
            newH = h2;
        } else if (isPreserveRatio()) {
            if (getFitWidth() <= 0.0d) {
                newW = h2 > 0.0d ? w2 * (getFitHeight() / h2) : 0.0d;
                newH = getFitHeight();
            } else if (getFitHeight() <= 0.0d) {
                newW = getFitWidth();
                newH = w2 > 0.0d ? h2 * (getFitWidth() / w2) : 0.0d;
            } else {
                if (w2 == 0.0d) {
                    w2 = getFitWidth();
                }
                if (h2 == 0.0d) {
                    h2 = getFitHeight();
                }
                double scale = Math.min(getFitWidth() / w2, getFitHeight() / h2);
                newW = w2 * scale;
                newH = h2 * scale;
            }
        } else if (getFitHeight() <= 0.0d) {
            newH = h2;
        } else if (getFitWidth() <= 0.0d) {
            newW = w2;
        }
        if (newH < 1.0d) {
            newH = 1.0d;
        }
        if (newW < 1.0d) {
            newW = 1.0d;
        }
        double w3 = newW;
        double h3 = newH;
        if (w3 <= 0.0d || h3 <= 0.0d) {
            return bounds.makeEmpty();
        }
        BaseBounds bounds2 = bounds.deriveWithNewBounds((float) getX(), (float) getY(), 0.0f, (float) (getX() + w3), (float) (getY() + h3), 0.0f);
        return tx.transform(bounds2, bounds2);
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected boolean impl_computeContains(double localX, double localY) {
        return true;
    }

    void updateViewport() {
        if (getMediaPlayer() == null) {
            return;
        }
        NGMediaView peer = (NGMediaView) impl_getPeer();
        if (getViewport() != null) {
            peer.setViewport((float) getFitWidth(), (float) getFitHeight(), (float) getViewport().getMinX(), (float) getViewport().getMinY(), (float) getViewport().getWidth(), (float) getViewport().getHeight(), isPreserveRatio());
        } else {
            peer.setViewport((float) getFitWidth(), (float) getFitHeight(), 0.0f, 0.0f, 0.0f, 0.0f, isPreserveRatio());
        }
    }

    @Override // javafx.scene.Node
    @Deprecated
    public void impl_updatePeer() throws SecurityException {
        super.impl_updatePeer();
        NGMediaView peer = (NGMediaView) impl_getPeer();
        if (impl_isDirty(DirtyBits.NODE_GEOMETRY)) {
            peer.setX((float) getX());
            peer.setY((float) getY());
        }
        if (impl_isDirty(DirtyBits.NODE_SMOOTH)) {
            peer.setSmooth(isSmooth());
        }
        if (impl_isDirty(DirtyBits.NODE_VIEWPORT)) {
            updateViewport();
        }
        if (impl_isDirty(DirtyBits.NODE_CONTENTS)) {
            peer.renderNextFrame();
        }
        if (impl_isDirty(DirtyBits.MEDIAVIEW_MEDIA)) {
            MediaPlayer player = getMediaPlayer();
            if (player != null) {
                peer.setMediaProvider(player);
                updateViewport();
            } else {
                peer.setMediaProvider(null);
            }
        }
    }

    @Deprecated
    public void impl_perfReset() {
        this.decodedFrameCount = 0;
        this.renderedFrameCount = 0;
    }

    @Deprecated
    public int impl_perfGetDecodedFrameCount() {
        return this.decodedFrameCount;
    }

    @Deprecated
    public int impl_perfGetRenderedFrameCount() {
        return this.renderedFrameCount;
    }

    /* loaded from: jfxrt.jar:javafx/scene/media/MediaView$MediaViewFrameTracker.class */
    private class MediaViewFrameTracker implements MediaFrameTracker {
        private MediaViewFrameTracker() {
        }

        @Override // com.sun.javafx.sg.prism.MediaFrameTracker
        public void incrementDecodedFrameCount(int count) {
            MediaView.this.decodedFrameCount += count;
        }

        @Override // com.sun.javafx.sg.prism.MediaFrameTracker
        public void incrementRenderedFrameCount(int count) {
            MediaView.this.renderedFrameCount += count;
        }
    }

    @Override // javafx.scene.Node
    @Deprecated
    public Object impl_processMXNode(MXNodeAlgorithm alg, MXNodeAlgorithmContext ctx) {
        return alg.processLeafNode(this, ctx);
    }

    void _mediaPlayerOnReady() {
        com.sun.media.jfxmedia.MediaPlayer jfxPlayer = getMediaPlayer().retrieveJfxPlayer();
        if (jfxPlayer != null) {
            if (this.decodedFrameRateListener != null && this.registerVideoFrameRateListener) {
                jfxPlayer.getVideoRenderControl().addVideoFrameRateListener(this.decodedFrameRateListener);
                this.registerVideoFrameRateListener = false;
            }
            this.mediaPlayerOverlay = jfxPlayer.getMediaPlayerOverlay();
            if (this.mediaPlayerOverlay != null) {
                createListeners();
                parentProperty().addListener(this.parentListener);
                impl_treeVisibleProperty().addListener(this.treeVisibleListener);
                opacityProperty().addListener(this.opacityListener);
                synchronized (this) {
                    updateMediaPlayerOverlay();
                }
            }
        }
    }
}
