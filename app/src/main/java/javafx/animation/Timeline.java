package javafx.animation;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.scenario.animation.AbstractMasterTimer;
import com.sun.scenario.animation.shared.TimelineClipCore;
import java.util.Iterator;
import javafx.animation.Animation;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:javafx/animation/Timeline.class */
public final class Timeline extends Animation {
    final TimelineClipCore clipCore;
    private final ObservableList<KeyFrame> keyFrames;

    public final ObservableList<KeyFrame> getKeyFrames() {
        return this.keyFrames;
    }

    public Timeline(double targetFramerate, KeyFrame... keyFrames) {
        super(targetFramerate);
        this.keyFrames = new TrackableObservableList<KeyFrame>() { // from class: javafx.animation.Timeline.1
            @Override // com.sun.javafx.collections.TrackableObservableList
            protected void onChanged(ListChangeListener.Change<KeyFrame> c2) {
                while (c2.next()) {
                    if (!c2.wasPermutated()) {
                        Iterator<KeyFrame> it = c2.getRemoved().iterator();
                        while (it.hasNext()) {
                            String cuePoint = it.next().getName();
                            if (cuePoint != null) {
                                Timeline.this.getCuePoints().remove(cuePoint);
                            }
                        }
                        for (KeyFrame keyFrame : c2.getAddedSubList()) {
                            String cuePoint2 = keyFrame.getName();
                            if (cuePoint2 != null) {
                                Timeline.this.getCuePoints().put(cuePoint2, keyFrame.getTime());
                            }
                        }
                        Duration duration = Timeline.this.clipCore.setKeyFrames(Timeline.this.getKeyFrames());
                        Timeline.this.setCycleDuration(duration);
                    }
                }
            }
        };
        this.clipCore = new TimelineClipCore(this);
        getKeyFrames().setAll(keyFrames);
    }

    public Timeline(KeyFrame... keyFrames) {
        this.keyFrames = new TrackableObservableList<KeyFrame>() { // from class: javafx.animation.Timeline.1
            @Override // com.sun.javafx.collections.TrackableObservableList
            protected void onChanged(ListChangeListener.Change<KeyFrame> c2) {
                while (c2.next()) {
                    if (!c2.wasPermutated()) {
                        Iterator<KeyFrame> it = c2.getRemoved().iterator();
                        while (it.hasNext()) {
                            String cuePoint = it.next().getName();
                            if (cuePoint != null) {
                                Timeline.this.getCuePoints().remove(cuePoint);
                            }
                        }
                        for (KeyFrame keyFrame : c2.getAddedSubList()) {
                            String cuePoint2 = keyFrame.getName();
                            if (cuePoint2 != null) {
                                Timeline.this.getCuePoints().put(cuePoint2, keyFrame.getTime());
                            }
                        }
                        Duration duration = Timeline.this.clipCore.setKeyFrames(Timeline.this.getKeyFrames());
                        Timeline.this.setCycleDuration(duration);
                    }
                }
            }
        };
        this.clipCore = new TimelineClipCore(this);
        getKeyFrames().setAll(keyFrames);
    }

    public Timeline(double targetFramerate) {
        super(targetFramerate);
        this.keyFrames = new TrackableObservableList<KeyFrame>() { // from class: javafx.animation.Timeline.1
            @Override // com.sun.javafx.collections.TrackableObservableList
            protected void onChanged(ListChangeListener.Change<KeyFrame> c2) {
                while (c2.next()) {
                    if (!c2.wasPermutated()) {
                        Iterator<KeyFrame> it = c2.getRemoved().iterator();
                        while (it.hasNext()) {
                            String cuePoint = it.next().getName();
                            if (cuePoint != null) {
                                Timeline.this.getCuePoints().remove(cuePoint);
                            }
                        }
                        for (KeyFrame keyFrame : c2.getAddedSubList()) {
                            String cuePoint2 = keyFrame.getName();
                            if (cuePoint2 != null) {
                                Timeline.this.getCuePoints().put(cuePoint2, keyFrame.getTime());
                            }
                        }
                        Duration duration = Timeline.this.clipCore.setKeyFrames(Timeline.this.getKeyFrames());
                        Timeline.this.setCycleDuration(duration);
                    }
                }
            }
        };
        this.clipCore = new TimelineClipCore(this);
    }

    public Timeline() {
        this.keyFrames = new TrackableObservableList<KeyFrame>() { // from class: javafx.animation.Timeline.1
            @Override // com.sun.javafx.collections.TrackableObservableList
            protected void onChanged(ListChangeListener.Change<KeyFrame> c2) {
                while (c2.next()) {
                    if (!c2.wasPermutated()) {
                        Iterator<KeyFrame> it = c2.getRemoved().iterator();
                        while (it.hasNext()) {
                            String cuePoint = it.next().getName();
                            if (cuePoint != null) {
                                Timeline.this.getCuePoints().remove(cuePoint);
                            }
                        }
                        for (KeyFrame keyFrame : c2.getAddedSubList()) {
                            String cuePoint2 = keyFrame.getName();
                            if (cuePoint2 != null) {
                                Timeline.this.getCuePoints().put(cuePoint2, keyFrame.getTime());
                            }
                        }
                        Duration duration = Timeline.this.clipCore.setKeyFrames(Timeline.this.getKeyFrames());
                        Timeline.this.setCycleDuration(duration);
                    }
                }
            }
        };
        this.clipCore = new TimelineClipCore(this);
    }

    Timeline(AbstractMasterTimer timer) {
        super(timer);
        this.keyFrames = new TrackableObservableList<KeyFrame>() { // from class: javafx.animation.Timeline.1
            @Override // com.sun.javafx.collections.TrackableObservableList
            protected void onChanged(ListChangeListener.Change<KeyFrame> c2) {
                while (c2.next()) {
                    if (!c2.wasPermutated()) {
                        Iterator<KeyFrame> it = c2.getRemoved().iterator();
                        while (it.hasNext()) {
                            String cuePoint = it.next().getName();
                            if (cuePoint != null) {
                                Timeline.this.getCuePoints().remove(cuePoint);
                            }
                        }
                        for (KeyFrame keyFrame : c2.getAddedSubList()) {
                            String cuePoint2 = keyFrame.getName();
                            if (cuePoint2 != null) {
                                Timeline.this.getCuePoints().put(cuePoint2, keyFrame.getTime());
                            }
                        }
                        Duration duration = Timeline.this.clipCore.setKeyFrames(Timeline.this.getKeyFrames());
                        Timeline.this.setCycleDuration(duration);
                    }
                }
            }
        };
        this.clipCore = new TimelineClipCore(this);
    }

    @Override // javafx.animation.Animation
    void impl_playTo(long currentTicks, long cycleTicks) {
        this.clipCore.playTo(currentTicks);
    }

    @Override // javafx.animation.Animation
    void impl_jumpTo(long currentTicks, long cycleTicks, boolean forceJump) {
        impl_sync(false);
        impl_setCurrentTicks(currentTicks);
        this.clipCore.jumpTo(currentTicks, forceJump);
    }

    @Override // javafx.animation.Animation
    void impl_setCurrentRate(double currentRate) {
        super.impl_setCurrentRate(currentRate);
        this.clipCore.notifyCurrentRateChanged();
    }

    @Override // javafx.animation.Animation
    void impl_start(boolean forceSync) {
        super.impl_start(forceSync);
        this.clipCore.start(forceSync);
    }

    @Override // javafx.animation.Animation
    public void stop() {
        if (this.parent != null) {
            throw new IllegalStateException("Cannot stop when embedded in another animation");
        }
        if (getStatus() == Animation.Status.RUNNING) {
            this.clipCore.abort();
        }
        super.stop();
    }
}
