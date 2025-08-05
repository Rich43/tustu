package javafx.animation;

import com.sun.javafx.animation.TickCalculation;
import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.collections.VetoableListDecorator;
import com.sun.scenario.animation.AbstractMasterTimer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javafx.animation.Animation;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:javafx/animation/ParallelTransition.class */
public final class ParallelTransition extends Transition {
    private static final double EPSILON = 1.0E-12d;
    private Animation[] cachedChildren;
    private long[] durations;
    private long[] delays;
    private double[] rates;
    private long[] offsetTicks;
    private boolean[] forceChildSync;
    private long oldTicks;
    private long cycleTime;
    private boolean childrenChanged;
    private boolean toggledRate;
    private final InvalidationListener childrenListener;
    private final ChangeListener<Number> rateListener;
    private ObjectProperty<Node> node;
    private final Set<Animation> childrenSet;
    private final ObservableList<Animation> children;
    private static final Animation[] EMPTY_ANIMATION_ARRAY = new Animation[0];
    private static final Node DEFAULT_NODE = null;

    public final void setNode(Node value) {
        if (this.node != null || value != null) {
            nodeProperty().set(value);
        }
    }

    public final Node getNode() {
        return this.node == null ? DEFAULT_NODE : this.node.get();
    }

    public final ObjectProperty<Node> nodeProperty() {
        if (this.node == null) {
            this.node = new SimpleObjectProperty(this, "node", DEFAULT_NODE);
        }
        return this.node;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean checkCycle(Animation child, Animation parent) {
        Animation animation = parent;
        while (true) {
            Animation a2 = animation;
            if (a2 != child) {
                if (a2.parent != null) {
                    animation = a2.parent;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    public final ObservableList<Animation> getChildren() {
        return this.children;
    }

    public ParallelTransition(Node node, Animation... children) {
        this.cachedChildren = EMPTY_ANIMATION_ARRAY;
        this.childrenChanged = true;
        this.childrenListener = observable -> {
            this.childrenChanged = true;
            if (getStatus() == Animation.Status.STOPPED) {
                setCycleDuration(computeCycleDuration());
            }
        };
        this.rateListener = new ChangeListener<Number>() { // from class: javafx.animation.ParallelTransition.1
            @Override // javafx.beans.value.ChangeListener
            public void changed(ObservableValue<? extends Number> observable2, Number oldValue, Number newValue) {
                if (oldValue.doubleValue() * newValue.doubleValue() < 0.0d) {
                    for (int i2 = 0; i2 < ParallelTransition.this.cachedChildren.length; i2++) {
                        Animation child = ParallelTransition.this.cachedChildren[i2];
                        child.clipEnvelope.setRate(ParallelTransition.this.rates[i2] * Math.signum(ParallelTransition.this.getCurrentRate()));
                    }
                    ParallelTransition.this.toggledRate = true;
                }
            }
        };
        this.childrenSet = new HashSet();
        this.children = new VetoableListDecorator<Animation>(new TrackableObservableList<Animation>() { // from class: javafx.animation.ParallelTransition.2
            @Override // com.sun.javafx.collections.TrackableObservableList
            protected void onChanged(ListChangeListener.Change<Animation> c2) {
                while (c2.next()) {
                    for (Animation animation : c2.getRemoved()) {
                        animation.parent = null;
                        animation.rateProperty().removeListener(ParallelTransition.this.childrenListener);
                        animation.totalDurationProperty().removeListener(ParallelTransition.this.childrenListener);
                        animation.delayProperty().removeListener(ParallelTransition.this.childrenListener);
                    }
                    for (Animation animation2 : c2.getAddedSubList()) {
                        animation2.parent = ParallelTransition.this;
                        animation2.rateProperty().addListener(ParallelTransition.this.childrenListener);
                        animation2.totalDurationProperty().addListener(ParallelTransition.this.childrenListener);
                        animation2.delayProperty().addListener(ParallelTransition.this.childrenListener);
                    }
                }
                ParallelTransition.this.childrenListener.invalidated(ParallelTransition.this.children);
            }
        }) { // from class: javafx.animation.ParallelTransition.3
            @Override // com.sun.javafx.collections.VetoableListDecorator
            protected void onProposedChange(List<Animation> toBeAdded, int... indexes) {
                IllegalArgumentException exception = null;
                for (int i2 = 0; i2 < indexes.length; i2 += 2) {
                    for (int idx = indexes[i2]; idx < indexes[i2 + 1]; idx++) {
                        ParallelTransition.this.childrenSet.remove(ParallelTransition.this.children.get(idx));
                    }
                }
                Iterator<Animation> it = toBeAdded.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Animation child = it.next();
                    if (child != null) {
                        if (ParallelTransition.this.childrenSet.add(child)) {
                            if (ParallelTransition.checkCycle(child, ParallelTransition.this)) {
                                exception = new IllegalArgumentException("This change would create cycle");
                                break;
                            }
                        } else {
                            exception = new IllegalArgumentException("Attempting to add a duplicate to the list of children");
                            break;
                        }
                    } else {
                        exception = new IllegalArgumentException("Child cannot be null");
                        break;
                    }
                }
                if (exception != null) {
                    ParallelTransition.this.childrenSet.clear();
                    ParallelTransition.this.childrenSet.addAll(ParallelTransition.this.children);
                    throw exception;
                }
            }
        };
        setInterpolator(Interpolator.LINEAR);
        setNode(node);
        getChildren().setAll(children);
    }

    public ParallelTransition(Animation... children) {
        this(null, children);
    }

    public ParallelTransition(Node node) {
        this.cachedChildren = EMPTY_ANIMATION_ARRAY;
        this.childrenChanged = true;
        this.childrenListener = observable -> {
            this.childrenChanged = true;
            if (getStatus() == Animation.Status.STOPPED) {
                setCycleDuration(computeCycleDuration());
            }
        };
        this.rateListener = new ChangeListener<Number>() { // from class: javafx.animation.ParallelTransition.1
            @Override // javafx.beans.value.ChangeListener
            public void changed(ObservableValue<? extends Number> observable2, Number oldValue, Number newValue) {
                if (oldValue.doubleValue() * newValue.doubleValue() < 0.0d) {
                    for (int i2 = 0; i2 < ParallelTransition.this.cachedChildren.length; i2++) {
                        Animation child = ParallelTransition.this.cachedChildren[i2];
                        child.clipEnvelope.setRate(ParallelTransition.this.rates[i2] * Math.signum(ParallelTransition.this.getCurrentRate()));
                    }
                    ParallelTransition.this.toggledRate = true;
                }
            }
        };
        this.childrenSet = new HashSet();
        this.children = new VetoableListDecorator<Animation>(new TrackableObservableList<Animation>() { // from class: javafx.animation.ParallelTransition.2
            @Override // com.sun.javafx.collections.TrackableObservableList
            protected void onChanged(ListChangeListener.Change<Animation> c2) {
                while (c2.next()) {
                    for (Animation animation : c2.getRemoved()) {
                        animation.parent = null;
                        animation.rateProperty().removeListener(ParallelTransition.this.childrenListener);
                        animation.totalDurationProperty().removeListener(ParallelTransition.this.childrenListener);
                        animation.delayProperty().removeListener(ParallelTransition.this.childrenListener);
                    }
                    for (Animation animation2 : c2.getAddedSubList()) {
                        animation2.parent = ParallelTransition.this;
                        animation2.rateProperty().addListener(ParallelTransition.this.childrenListener);
                        animation2.totalDurationProperty().addListener(ParallelTransition.this.childrenListener);
                        animation2.delayProperty().addListener(ParallelTransition.this.childrenListener);
                    }
                }
                ParallelTransition.this.childrenListener.invalidated(ParallelTransition.this.children);
            }
        }) { // from class: javafx.animation.ParallelTransition.3
            @Override // com.sun.javafx.collections.VetoableListDecorator
            protected void onProposedChange(List<Animation> toBeAdded, int... indexes) {
                IllegalArgumentException exception = null;
                for (int i2 = 0; i2 < indexes.length; i2 += 2) {
                    for (int idx = indexes[i2]; idx < indexes[i2 + 1]; idx++) {
                        ParallelTransition.this.childrenSet.remove(ParallelTransition.this.children.get(idx));
                    }
                }
                Iterator<Animation> it = toBeAdded.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Animation child = it.next();
                    if (child != null) {
                        if (ParallelTransition.this.childrenSet.add(child)) {
                            if (ParallelTransition.checkCycle(child, ParallelTransition.this)) {
                                exception = new IllegalArgumentException("This change would create cycle");
                                break;
                            }
                        } else {
                            exception = new IllegalArgumentException("Attempting to add a duplicate to the list of children");
                            break;
                        }
                    } else {
                        exception = new IllegalArgumentException("Child cannot be null");
                        break;
                    }
                }
                if (exception != null) {
                    ParallelTransition.this.childrenSet.clear();
                    ParallelTransition.this.childrenSet.addAll(ParallelTransition.this.children);
                    throw exception;
                }
            }
        };
        setInterpolator(Interpolator.LINEAR);
        setNode(node);
    }

    public ParallelTransition() {
        this((Node) null);
    }

    ParallelTransition(AbstractMasterTimer timer) {
        super(timer);
        this.cachedChildren = EMPTY_ANIMATION_ARRAY;
        this.childrenChanged = true;
        this.childrenListener = observable -> {
            this.childrenChanged = true;
            if (getStatus() == Animation.Status.STOPPED) {
                setCycleDuration(computeCycleDuration());
            }
        };
        this.rateListener = new ChangeListener<Number>() { // from class: javafx.animation.ParallelTransition.1
            @Override // javafx.beans.value.ChangeListener
            public void changed(ObservableValue<? extends Number> observable2, Number oldValue, Number newValue) {
                if (oldValue.doubleValue() * newValue.doubleValue() < 0.0d) {
                    for (int i2 = 0; i2 < ParallelTransition.this.cachedChildren.length; i2++) {
                        Animation child = ParallelTransition.this.cachedChildren[i2];
                        child.clipEnvelope.setRate(ParallelTransition.this.rates[i2] * Math.signum(ParallelTransition.this.getCurrentRate()));
                    }
                    ParallelTransition.this.toggledRate = true;
                }
            }
        };
        this.childrenSet = new HashSet();
        this.children = new VetoableListDecorator<Animation>(new TrackableObservableList<Animation>() { // from class: javafx.animation.ParallelTransition.2
            @Override // com.sun.javafx.collections.TrackableObservableList
            protected void onChanged(ListChangeListener.Change<Animation> c2) {
                while (c2.next()) {
                    for (Animation animation : c2.getRemoved()) {
                        animation.parent = null;
                        animation.rateProperty().removeListener(ParallelTransition.this.childrenListener);
                        animation.totalDurationProperty().removeListener(ParallelTransition.this.childrenListener);
                        animation.delayProperty().removeListener(ParallelTransition.this.childrenListener);
                    }
                    for (Animation animation2 : c2.getAddedSubList()) {
                        animation2.parent = ParallelTransition.this;
                        animation2.rateProperty().addListener(ParallelTransition.this.childrenListener);
                        animation2.totalDurationProperty().addListener(ParallelTransition.this.childrenListener);
                        animation2.delayProperty().addListener(ParallelTransition.this.childrenListener);
                    }
                }
                ParallelTransition.this.childrenListener.invalidated(ParallelTransition.this.children);
            }
        }) { // from class: javafx.animation.ParallelTransition.3
            @Override // com.sun.javafx.collections.VetoableListDecorator
            protected void onProposedChange(List<Animation> toBeAdded, int... indexes) {
                IllegalArgumentException exception = null;
                for (int i2 = 0; i2 < indexes.length; i2 += 2) {
                    for (int idx = indexes[i2]; idx < indexes[i2 + 1]; idx++) {
                        ParallelTransition.this.childrenSet.remove(ParallelTransition.this.children.get(idx));
                    }
                }
                Iterator<Animation> it = toBeAdded.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Animation child = it.next();
                    if (child != null) {
                        if (ParallelTransition.this.childrenSet.add(child)) {
                            if (ParallelTransition.checkCycle(child, ParallelTransition.this)) {
                                exception = new IllegalArgumentException("This change would create cycle");
                                break;
                            }
                        } else {
                            exception = new IllegalArgumentException("Attempting to add a duplicate to the list of children");
                            break;
                        }
                    } else {
                        exception = new IllegalArgumentException("Child cannot be null");
                        break;
                    }
                }
                if (exception != null) {
                    ParallelTransition.this.childrenSet.clear();
                    ParallelTransition.this.childrenSet.addAll(ParallelTransition.this.children);
                    throw exception;
                }
            }
        };
        setInterpolator(Interpolator.LINEAR);
    }

    @Override // javafx.animation.Transition
    protected Node getParentTargetNode() {
        Node node = getNode();
        if (node != null) {
            return node;
        }
        if (this.parent == null || !(this.parent instanceof Transition)) {
            return null;
        }
        return ((Transition) this.parent).getParentTargetNode();
    }

    private Duration computeCycleDuration() {
        Duration maxTime = Duration.ZERO;
        for (Animation animation : getChildren()) {
            double absRate = Math.abs(animation.getRate());
            Duration totalDuration = absRate < 1.0E-12d ? animation.getTotalDuration() : animation.getTotalDuration().divide(absRate);
            Duration childDuration = totalDuration.add(animation.getDelay());
            if (childDuration.isIndefinite()) {
                return Duration.INDEFINITE;
            }
            if (childDuration.greaterThan(maxTime)) {
                maxTime = childDuration;
            }
        }
        return maxTime;
    }

    private double calculateFraction(long currentTicks, long cycleTicks) {
        double frac = currentTicks / cycleTicks;
        if (frac <= 0.0d) {
            return 0.0d;
        }
        if (frac >= 1.0d) {
            return 1.0d;
        }
        return frac;
    }

    private boolean startChild(Animation child, int index) {
        boolean forceSync = this.forceChildSync[index];
        if (child.impl_startable(forceSync)) {
            child.clipEnvelope.setRate(this.rates[index] * Math.signum(getCurrentRate()));
            child.impl_start(forceSync);
            this.forceChildSync[index] = false;
            return true;
        }
        return false;
    }

    @Override // javafx.animation.Transition, javafx.animation.Animation
    void impl_sync(boolean forceSync) {
        super.impl_sync(forceSync);
        if ((!forceSync || !this.childrenChanged) && this.durations != null) {
            if (forceSync) {
                int n2 = this.forceChildSync.length;
                for (int i2 = 0; i2 < n2; i2++) {
                    this.forceChildSync[i2] = true;
                }
                return;
            }
            return;
        }
        this.cachedChildren = (Animation[]) getChildren().toArray(EMPTY_ANIMATION_ARRAY);
        int n3 = this.cachedChildren.length;
        this.durations = new long[n3];
        this.delays = new long[n3];
        this.rates = new double[n3];
        this.offsetTicks = new long[n3];
        this.forceChildSync = new boolean[n3];
        this.cycleTime = 0L;
        int i3 = 0;
        for (Animation animation : this.cachedChildren) {
            this.rates[i3] = Math.abs(animation.getRate());
            if (this.rates[i3] < 1.0E-12d) {
                this.rates[i3] = 1.0d;
            }
            this.durations[i3] = TickCalculation.fromDuration(animation.getTotalDuration(), this.rates[i3]);
            this.delays[i3] = TickCalculation.fromDuration(animation.getDelay());
            this.cycleTime = Math.max(this.cycleTime, TickCalculation.add(this.durations[i3], this.delays[i3]));
            this.forceChildSync[i3] = true;
            i3++;
        }
        this.childrenChanged = false;
    }

    @Override // javafx.animation.Animation
    void impl_pause() {
        super.impl_pause();
        for (Animation animation : this.cachedChildren) {
            if (animation.getStatus() == Animation.Status.RUNNING) {
                animation.impl_pause();
            }
        }
    }

    @Override // javafx.animation.Animation
    void impl_resume() {
        super.impl_resume();
        int i2 = 0;
        for (Animation animation : this.cachedChildren) {
            if (animation.getStatus() == Animation.Status.PAUSED) {
                animation.impl_resume();
                animation.clipEnvelope.setRate(this.rates[i2] * Math.signum(getCurrentRate()));
            }
            i2++;
        }
    }

    @Override // javafx.animation.Animation
    void impl_start(boolean forceSync) {
        super.impl_start(forceSync);
        this.toggledRate = false;
        rateProperty().addListener(this.rateListener);
        double curRate = getCurrentRate();
        long currentTicks = TickCalculation.fromDuration(getCurrentTime());
        if (curRate < 0.0d) {
            jumpToEnd();
            if (currentTicks < this.cycleTime) {
                impl_jumpTo(currentTicks, this.cycleTime, false);
                return;
            }
            return;
        }
        jumpToStart();
        if (currentTicks > 0) {
            impl_jumpTo(currentTicks, this.cycleTime, false);
        }
    }

    @Override // javafx.animation.Animation
    void impl_stop() {
        super.impl_stop();
        for (Animation animation : this.cachedChildren) {
            if (animation.getStatus() != Animation.Status.STOPPED) {
                animation.impl_stop();
            }
        }
        if (this.childrenChanged) {
            setCycleDuration(computeCycleDuration());
        }
        rateProperty().removeListener(this.rateListener);
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x014c  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0178  */
    @Override // javafx.animation.Transition, javafx.animation.Animation
    @java.lang.Deprecated
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void impl_playTo(long r16, long r18) {
        /*
            Method dump skipped, instructions count: 746
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: javafx.animation.ParallelTransition.impl_playTo(long, long):void");
    }

    @Override // javafx.animation.Transition, javafx.animation.Animation
    @Deprecated
    public void impl_jumpTo(long currentTicks, long cycleTicks, boolean forceJump) {
        impl_setCurrentTicks(currentTicks);
        if (getStatus() == Animation.Status.STOPPED && !forceJump) {
            return;
        }
        impl_sync(false);
        double frac = calculateFraction(currentTicks, cycleTicks);
        long newTicks = Math.max(0L, Math.min(getCachedInterpolator().interpolate(0L, cycleTicks, frac), cycleTicks));
        int i2 = 0;
        for (Animation animation : this.cachedChildren) {
            Animation.Status status = animation.getStatus();
            if (newTicks <= this.delays[i2]) {
                this.offsetTicks[i2] = 0;
                if (status != Animation.Status.STOPPED) {
                    animation.clipEnvelope.jumpTo(0L);
                    animation.impl_stop();
                } else if (TickCalculation.fromDuration(animation.getCurrentTime()) != 0) {
                    animation.impl_jumpTo(0L, this.durations[i2], true);
                }
            } else if (newTicks >= TickCalculation.add(this.durations[i2], this.delays[i2])) {
                this.offsetTicks[i2] = 0;
                if (status != Animation.Status.STOPPED) {
                    animation.clipEnvelope.jumpTo(Math.round(this.durations[i2] * this.rates[i2]));
                    animation.impl_stop();
                } else if (TickCalculation.fromDuration(animation.getCurrentTime()) != this.durations[i2]) {
                    animation.impl_jumpTo(this.durations[i2], this.durations[i2], true);
                }
            } else {
                if (status == Animation.Status.STOPPED) {
                    startChild(animation, i2);
                    if (getStatus() == Animation.Status.PAUSED) {
                        animation.impl_pause();
                    }
                    this.offsetTicks[i2] = getCurrentRate() > 0.0d ? newTicks - this.delays[i2] : TickCalculation.add(this.durations[i2], this.delays[i2]) - newTicks;
                } else if (status == Animation.Status.PAUSED) {
                    this.offsetTicks[i2] = (long) (r0[r1] + ((newTicks - this.oldTicks) * Math.signum(this.clipEnvelope.getCurrentRate())));
                } else {
                    long[] jArr = this.offsetTicks;
                    int i3 = i2;
                    jArr[i3] = jArr[i3] + (getCurrentRate() > 0.0d ? newTicks - this.oldTicks : this.oldTicks - newTicks);
                }
                animation.clipEnvelope.jumpTo(Math.round(TickCalculation.sub(newTicks, this.delays[i2]) * this.rates[i2]));
            }
            i2++;
        }
        this.oldTicks = newTicks;
    }

    @Override // javafx.animation.Transition
    protected void interpolate(double frac) {
    }

    private void jumpToEnd() {
        for (int i2 = 0; i2 < this.cachedChildren.length; i2++) {
            if (this.forceChildSync[i2]) {
                this.cachedChildren[i2].impl_sync(true);
            }
            this.cachedChildren[i2].impl_jumpTo(this.durations[i2], this.durations[i2], true);
        }
    }

    private void jumpToStart() {
        for (int i2 = this.cachedChildren.length - 1; i2 >= 0; i2--) {
            if (this.forceChildSync[i2]) {
                this.cachedChildren[i2].impl_sync(true);
            }
            this.cachedChildren[i2].impl_jumpTo(0L, this.durations[i2], true);
        }
    }
}
