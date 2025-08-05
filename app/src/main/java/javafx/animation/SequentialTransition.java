package javafx.animation;

import com.sun.javafx.animation.TickCalculation;
import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.collections.VetoableListDecorator;
import com.sun.scenario.animation.AbstractMasterTimer;
import java.util.Arrays;
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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:javafx/animation/SequentialTransition.class */
public final class SequentialTransition extends Transition {
    private static final int BEFORE = -1;
    private static final double EPSILON = 1.0E-12d;
    private Animation[] cachedChildren;
    private long[] startTimes;
    private long[] durations;
    private long[] delays;
    private double[] rates;
    private boolean[] forceChildSync;
    private int end;
    private int curIndex;
    private long oldTicks;
    private long offsetTicks;
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

    public SequentialTransition(Node node, Animation... children) {
        this.cachedChildren = EMPTY_ANIMATION_ARRAY;
        this.curIndex = -1;
        this.oldTicks = 0L;
        this.childrenChanged = true;
        this.childrenListener = observable -> {
            this.childrenChanged = true;
            if (getStatus() == Animation.Status.STOPPED) {
                setCycleDuration(computeCycleDuration());
            }
        };
        this.rateListener = new ChangeListener<Number>() { // from class: javafx.animation.SequentialTransition.1
            @Override // javafx.beans.value.ChangeListener
            public void changed(ObservableValue<? extends Number> observable2, Number oldValue, Number newValue) {
                if (oldValue.doubleValue() * newValue.doubleValue() < 0.0d) {
                    for (int i2 = 0; i2 < SequentialTransition.this.cachedChildren.length; i2++) {
                        Animation child = SequentialTransition.this.cachedChildren[i2];
                        child.clipEnvelope.setRate(SequentialTransition.this.rates[i2] * Math.signum(SequentialTransition.this.getCurrentRate()));
                    }
                    SequentialTransition.this.toggledRate = true;
                }
            }
        };
        this.childrenSet = new HashSet();
        this.children = new VetoableListDecorator<Animation>(new TrackableObservableList<Animation>() { // from class: javafx.animation.SequentialTransition.2
            @Override // com.sun.javafx.collections.TrackableObservableList
            protected void onChanged(ListChangeListener.Change<Animation> c2) {
                while (c2.next()) {
                    for (Animation animation : c2.getRemoved()) {
                        animation.parent = null;
                        animation.rateProperty().removeListener(SequentialTransition.this.childrenListener);
                        animation.totalDurationProperty().removeListener(SequentialTransition.this.childrenListener);
                        animation.delayProperty().removeListener(SequentialTransition.this.childrenListener);
                    }
                    for (Animation animation2 : c2.getAddedSubList()) {
                        animation2.parent = SequentialTransition.this;
                        animation2.rateProperty().addListener(SequentialTransition.this.childrenListener);
                        animation2.totalDurationProperty().addListener(SequentialTransition.this.childrenListener);
                        animation2.delayProperty().addListener(SequentialTransition.this.childrenListener);
                    }
                }
                SequentialTransition.this.childrenListener.invalidated(SequentialTransition.this.children);
            }
        }) { // from class: javafx.animation.SequentialTransition.3
            @Override // com.sun.javafx.collections.VetoableListDecorator
            protected void onProposedChange(List<Animation> toBeAdded, int... indexes) {
                IllegalArgumentException exception = null;
                for (int i2 = 0; i2 < indexes.length; i2 += 2) {
                    for (int idx = indexes[i2]; idx < indexes[i2 + 1]; idx++) {
                        SequentialTransition.this.childrenSet.remove(SequentialTransition.this.children.get(idx));
                    }
                }
                Iterator<Animation> it = toBeAdded.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Animation child = it.next();
                    if (child != null) {
                        if (SequentialTransition.this.childrenSet.add(child)) {
                            if (SequentialTransition.checkCycle(child, SequentialTransition.this)) {
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
                    SequentialTransition.this.childrenSet.clear();
                    SequentialTransition.this.childrenSet.addAll(SequentialTransition.this.children);
                    throw exception;
                }
            }
        };
        setInterpolator(Interpolator.LINEAR);
        setNode(node);
        getChildren().setAll(children);
    }

    public SequentialTransition(Animation... children) {
        this(null, children);
    }

    public SequentialTransition(Node node) {
        this.cachedChildren = EMPTY_ANIMATION_ARRAY;
        this.curIndex = -1;
        this.oldTicks = 0L;
        this.childrenChanged = true;
        this.childrenListener = observable -> {
            this.childrenChanged = true;
            if (getStatus() == Animation.Status.STOPPED) {
                setCycleDuration(computeCycleDuration());
            }
        };
        this.rateListener = new ChangeListener<Number>() { // from class: javafx.animation.SequentialTransition.1
            @Override // javafx.beans.value.ChangeListener
            public void changed(ObservableValue<? extends Number> observable2, Number oldValue, Number newValue) {
                if (oldValue.doubleValue() * newValue.doubleValue() < 0.0d) {
                    for (int i2 = 0; i2 < SequentialTransition.this.cachedChildren.length; i2++) {
                        Animation child = SequentialTransition.this.cachedChildren[i2];
                        child.clipEnvelope.setRate(SequentialTransition.this.rates[i2] * Math.signum(SequentialTransition.this.getCurrentRate()));
                    }
                    SequentialTransition.this.toggledRate = true;
                }
            }
        };
        this.childrenSet = new HashSet();
        this.children = new VetoableListDecorator<Animation>(new TrackableObservableList<Animation>() { // from class: javafx.animation.SequentialTransition.2
            @Override // com.sun.javafx.collections.TrackableObservableList
            protected void onChanged(ListChangeListener.Change<Animation> c2) {
                while (c2.next()) {
                    for (Animation animation : c2.getRemoved()) {
                        animation.parent = null;
                        animation.rateProperty().removeListener(SequentialTransition.this.childrenListener);
                        animation.totalDurationProperty().removeListener(SequentialTransition.this.childrenListener);
                        animation.delayProperty().removeListener(SequentialTransition.this.childrenListener);
                    }
                    for (Animation animation2 : c2.getAddedSubList()) {
                        animation2.parent = SequentialTransition.this;
                        animation2.rateProperty().addListener(SequentialTransition.this.childrenListener);
                        animation2.totalDurationProperty().addListener(SequentialTransition.this.childrenListener);
                        animation2.delayProperty().addListener(SequentialTransition.this.childrenListener);
                    }
                }
                SequentialTransition.this.childrenListener.invalidated(SequentialTransition.this.children);
            }
        }) { // from class: javafx.animation.SequentialTransition.3
            @Override // com.sun.javafx.collections.VetoableListDecorator
            protected void onProposedChange(List<Animation> toBeAdded, int... indexes) {
                IllegalArgumentException exception = null;
                for (int i2 = 0; i2 < indexes.length; i2 += 2) {
                    for (int idx = indexes[i2]; idx < indexes[i2 + 1]; idx++) {
                        SequentialTransition.this.childrenSet.remove(SequentialTransition.this.children.get(idx));
                    }
                }
                Iterator<Animation> it = toBeAdded.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Animation child = it.next();
                    if (child != null) {
                        if (SequentialTransition.this.childrenSet.add(child)) {
                            if (SequentialTransition.checkCycle(child, SequentialTransition.this)) {
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
                    SequentialTransition.this.childrenSet.clear();
                    SequentialTransition.this.childrenSet.addAll(SequentialTransition.this.children);
                    throw exception;
                }
            }
        };
        setInterpolator(Interpolator.LINEAR);
        setNode(node);
    }

    public SequentialTransition() {
        this((Node) null);
    }

    SequentialTransition(AbstractMasterTimer timer) {
        super(timer);
        this.cachedChildren = EMPTY_ANIMATION_ARRAY;
        this.curIndex = -1;
        this.oldTicks = 0L;
        this.childrenChanged = true;
        this.childrenListener = observable -> {
            this.childrenChanged = true;
            if (getStatus() == Animation.Status.STOPPED) {
                setCycleDuration(computeCycleDuration());
            }
        };
        this.rateListener = new ChangeListener<Number>() { // from class: javafx.animation.SequentialTransition.1
            @Override // javafx.beans.value.ChangeListener
            public void changed(ObservableValue<? extends Number> observable2, Number oldValue, Number newValue) {
                if (oldValue.doubleValue() * newValue.doubleValue() < 0.0d) {
                    for (int i2 = 0; i2 < SequentialTransition.this.cachedChildren.length; i2++) {
                        Animation child = SequentialTransition.this.cachedChildren[i2];
                        child.clipEnvelope.setRate(SequentialTransition.this.rates[i2] * Math.signum(SequentialTransition.this.getCurrentRate()));
                    }
                    SequentialTransition.this.toggledRate = true;
                }
            }
        };
        this.childrenSet = new HashSet();
        this.children = new VetoableListDecorator<Animation>(new TrackableObservableList<Animation>() { // from class: javafx.animation.SequentialTransition.2
            @Override // com.sun.javafx.collections.TrackableObservableList
            protected void onChanged(ListChangeListener.Change<Animation> c2) {
                while (c2.next()) {
                    for (Animation animation : c2.getRemoved()) {
                        animation.parent = null;
                        animation.rateProperty().removeListener(SequentialTransition.this.childrenListener);
                        animation.totalDurationProperty().removeListener(SequentialTransition.this.childrenListener);
                        animation.delayProperty().removeListener(SequentialTransition.this.childrenListener);
                    }
                    for (Animation animation2 : c2.getAddedSubList()) {
                        animation2.parent = SequentialTransition.this;
                        animation2.rateProperty().addListener(SequentialTransition.this.childrenListener);
                        animation2.totalDurationProperty().addListener(SequentialTransition.this.childrenListener);
                        animation2.delayProperty().addListener(SequentialTransition.this.childrenListener);
                    }
                }
                SequentialTransition.this.childrenListener.invalidated(SequentialTransition.this.children);
            }
        }) { // from class: javafx.animation.SequentialTransition.3
            @Override // com.sun.javafx.collections.VetoableListDecorator
            protected void onProposedChange(List<Animation> toBeAdded, int... indexes) {
                IllegalArgumentException exception = null;
                for (int i2 = 0; i2 < indexes.length; i2 += 2) {
                    for (int idx = indexes[i2]; idx < indexes[i2 + 1]; idx++) {
                        SequentialTransition.this.childrenSet.remove(SequentialTransition.this.children.get(idx));
                    }
                }
                Iterator<Animation> it = toBeAdded.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Animation child = it.next();
                    if (child != null) {
                        if (SequentialTransition.this.childrenSet.add(child)) {
                            if (SequentialTransition.checkCycle(child, SequentialTransition.this)) {
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
                    SequentialTransition.this.childrenSet.clear();
                    SequentialTransition.this.childrenSet.addAll(SequentialTransition.this.children);
                    throw exception;
                }
            }
        };
        setInterpolator(Interpolator.LINEAR);
    }

    @Override // javafx.animation.Transition
    protected Node getParentTargetNode() {
        Node _node = getNode();
        if (_node != null) {
            return _node;
        }
        if (this.parent == null || !(this.parent instanceof Transition)) {
            return null;
        }
        return ((Transition) this.parent).getParentTargetNode();
    }

    private Duration computeCycleDuration() {
        Duration currentDur = Duration.ZERO;
        for (Animation animation : getChildren()) {
            Duration currentDur2 = currentDur.add(animation.getDelay());
            double absRate = Math.abs(animation.getRate());
            currentDur = currentDur2.add(absRate < 1.0E-12d ? animation.getTotalDuration() : animation.getTotalDuration().divide(absRate));
            if (currentDur.isIndefinite()) {
                break;
            }
        }
        return currentDur;
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

    private int findNewIndex(long ticks) {
        if (this.curIndex != -1 && this.curIndex != this.end && this.startTimes[this.curIndex] <= ticks && ticks <= this.startTimes[this.curIndex + 1]) {
            return this.curIndex;
        }
        boolean indexUndefined = this.curIndex == -1 || this.curIndex == this.end;
        int fromIndex = (indexUndefined || ticks < this.oldTicks) ? 0 : this.curIndex + 1;
        int toIndex = (indexUndefined || this.oldTicks < ticks) ? this.end : this.curIndex;
        int index = Arrays.binarySearch(this.startTimes, fromIndex, toIndex, ticks);
        if (index < 0) {
            return (-index) - 2;
        }
        if (index > 0) {
            return index - 1;
        }
        return 0;
    }

    @Override // javafx.animation.Transition, javafx.animation.Animation
    void impl_sync(boolean forceSync) {
        long jAdd;
        super.impl_sync(forceSync);
        if ((!forceSync || !this.childrenChanged) && this.startTimes != null) {
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
        this.end = this.cachedChildren.length;
        this.startTimes = new long[this.end + 1];
        this.durations = new long[this.end];
        this.delays = new long[this.end];
        this.rates = new double[this.end];
        this.forceChildSync = new boolean[this.end];
        long cycleTicks = 0;
        int i3 = 0;
        for (Animation animation : this.cachedChildren) {
            this.startTimes[i3] = cycleTicks;
            this.rates[i3] = Math.abs(animation.getRate());
            if (this.rates[i3] < 1.0E-12d) {
                this.rates[i3] = 1.0d;
            }
            this.durations[i3] = TickCalculation.fromDuration(animation.getTotalDuration(), this.rates[i3]);
            this.delays[i3] = TickCalculation.fromDuration(animation.getDelay());
            if (this.durations[i3] == Long.MAX_VALUE || this.delays[i3] == Long.MAX_VALUE || cycleTicks == Long.MAX_VALUE) {
                jAdd = Long.MAX_VALUE;
            } else {
                jAdd = TickCalculation.add(cycleTicks, TickCalculation.add(this.durations[i3], this.delays[i3]));
            }
            cycleTicks = jAdd;
            this.forceChildSync[i3] = true;
            i3++;
        }
        this.startTimes[this.end] = cycleTicks;
        this.childrenChanged = false;
    }

    @Override // javafx.animation.Animation
    void impl_start(boolean forceSync) {
        super.impl_start(forceSync);
        this.toggledRate = false;
        rateProperty().addListener(this.rateListener);
        this.offsetTicks = 0L;
        double curRate = getCurrentRate();
        long currentTicks = TickCalculation.fromDuration(getCurrentTime());
        if (curRate < 0.0d) {
            jumpToEnd();
            this.curIndex = this.end;
            if (currentTicks < this.startTimes[this.end]) {
                impl_jumpTo(currentTicks, this.startTimes[this.end], false);
                return;
            }
            return;
        }
        jumpToBefore();
        this.curIndex = -1;
        if (currentTicks > 0) {
            impl_jumpTo(currentTicks, this.startTimes[this.end], false);
        }
    }

    @Override // javafx.animation.Animation
    void impl_pause() {
        super.impl_pause();
        if (this.curIndex != -1 && this.curIndex != this.end) {
            Animation current = this.cachedChildren[this.curIndex];
            if (current.getStatus() == Animation.Status.RUNNING) {
                current.impl_pause();
            }
        }
    }

    @Override // javafx.animation.Animation
    void impl_resume() {
        super.impl_resume();
        if (this.curIndex != -1 && this.curIndex != this.end) {
            Animation current = this.cachedChildren[this.curIndex];
            if (current.getStatus() == Animation.Status.PAUSED) {
                current.impl_resume();
                current.clipEnvelope.setRate(this.rates[this.curIndex] * Math.signum(getCurrentRate()));
            }
        }
    }

    @Override // javafx.animation.Animation
    void impl_stop() {
        super.impl_stop();
        if (this.curIndex != -1 && this.curIndex != this.end) {
            Animation current = this.cachedChildren[this.curIndex];
            if (current.getStatus() != Animation.Status.STOPPED) {
                current.impl_stop();
            }
        }
        if (this.childrenChanged) {
            setCycleDuration(computeCycleDuration());
        }
        rateProperty().removeListener(this.rateListener);
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
    void impl_playTo(long currentTicks, long cycleTicks) {
        EventHandler<ActionEvent> handler;
        EventHandler<ActionEvent> handler2;
        EventHandler<ActionEvent> handler3;
        EventHandler<ActionEvent> handler4;
        impl_setCurrentTicks(currentTicks);
        double frac = calculateFraction(currentTicks, cycleTicks);
        long newTicks = Math.max(0L, Math.min(getCachedInterpolator().interpolate(0L, cycleTicks, frac), cycleTicks));
        int newIndex = findNewIndex(newTicks);
        Animation current = (this.curIndex == -1 || this.curIndex == this.end) ? null : this.cachedChildren[this.curIndex];
        if (this.toggledRate) {
            if (current != null && current.getStatus() == Animation.Status.RUNNING) {
                this.offsetTicks = (long) (this.offsetTicks - (Math.signum(getCurrentRate()) * (this.durations[this.curIndex] - (2 * ((this.oldTicks - this.delays[this.curIndex]) - this.startTimes[this.curIndex])))));
            }
            this.toggledRate = false;
        }
        if (this.curIndex == newIndex) {
            if (getCurrentRate() > 0.0d) {
                long currentDelay = TickCalculation.add(this.startTimes[this.curIndex], this.delays[this.curIndex]);
                if (newTicks >= currentDelay) {
                    if (this.oldTicks <= currentDelay || current.getStatus() == Animation.Status.STOPPED) {
                        boolean enteringCycle = this.oldTicks <= currentDelay;
                        if (enteringCycle) {
                            current.clipEnvelope.jumpTo(0L);
                        }
                        if (!startChild(current, this.curIndex)) {
                            if (enteringCycle && (handler4 = current.getOnFinished()) != null) {
                                handler4.handle(new ActionEvent(this, null));
                            }
                            this.oldTicks = newTicks;
                            return;
                        }
                    }
                    if (newTicks >= this.startTimes[this.curIndex + 1]) {
                        current.impl_timePulse(TickCalculation.sub(this.durations[this.curIndex], this.offsetTicks));
                        if (newTicks == cycleTicks) {
                            this.curIndex = this.end;
                        }
                    } else {
                        long localTicks = TickCalculation.sub(newTicks - currentDelay, this.offsetTicks);
                        current.impl_timePulse(localTicks);
                    }
                }
            } else {
                long currentDelay2 = TickCalculation.add(this.startTimes[this.curIndex], this.delays[this.curIndex]);
                if (this.oldTicks >= this.startTimes[this.curIndex + 1] || (this.oldTicks >= currentDelay2 && current.getStatus() == Animation.Status.STOPPED)) {
                    boolean enteringCycle2 = this.oldTicks >= this.startTimes[this.curIndex + 1];
                    if (enteringCycle2) {
                        current.clipEnvelope.jumpTo(Math.round(this.durations[this.curIndex] * this.rates[this.curIndex]));
                    }
                    if (!startChild(current, this.curIndex)) {
                        if (enteringCycle2 && (handler3 = current.getOnFinished()) != null) {
                            handler3.handle(new ActionEvent(this, null));
                        }
                        this.oldTicks = newTicks;
                        return;
                    }
                }
                if (newTicks <= currentDelay2) {
                    current.impl_timePulse(TickCalculation.sub(this.durations[this.curIndex], this.offsetTicks));
                    if (newTicks == 0) {
                        this.curIndex = -1;
                    }
                } else {
                    long localTicks2 = TickCalculation.sub(this.startTimes[this.curIndex + 1] - newTicks, this.offsetTicks);
                    current.impl_timePulse(localTicks2);
                }
            }
        } else if (this.curIndex < newIndex) {
            if (current != null) {
                long oldDelay = TickCalculation.add(this.startTimes[this.curIndex], this.delays[this.curIndex]);
                if (this.oldTicks <= oldDelay || (current.getStatus() == Animation.Status.STOPPED && this.oldTicks != this.startTimes[this.curIndex + 1])) {
                    boolean enteringCycle3 = this.oldTicks <= oldDelay;
                    if (enteringCycle3) {
                        current.clipEnvelope.jumpTo(0L);
                    }
                    if (!startChild(current, this.curIndex) && enteringCycle3 && (handler2 = current.getOnFinished()) != null) {
                        handler2.handle(new ActionEvent(this, null));
                    }
                }
                if (current.getStatus() == Animation.Status.RUNNING) {
                    current.impl_timePulse(TickCalculation.sub(this.durations[this.curIndex], this.offsetTicks));
                }
                this.oldTicks = this.startTimes[this.curIndex + 1];
            }
            this.offsetTicks = 0L;
            this.curIndex++;
            while (this.curIndex < newIndex) {
                Animation animation = this.cachedChildren[this.curIndex];
                animation.clipEnvelope.jumpTo(0L);
                if (startChild(animation, this.curIndex)) {
                    animation.impl_timePulse(this.durations[this.curIndex]);
                } else {
                    EventHandler<ActionEvent> handler5 = animation.getOnFinished();
                    if (handler5 != null) {
                        handler5.handle(new ActionEvent(this, null));
                    }
                }
                this.oldTicks = this.startTimes[this.curIndex + 1];
                this.curIndex++;
            }
            Animation newAnimation = this.cachedChildren[this.curIndex];
            newAnimation.clipEnvelope.jumpTo(0L);
            if (!startChild(newAnimation, this.curIndex)) {
                EventHandler<ActionEvent> handler6 = newAnimation.getOnFinished();
                if (handler6 != null) {
                    handler6.handle(new ActionEvent(this, null));
                }
            } else if (newTicks >= this.startTimes[this.curIndex + 1]) {
                newAnimation.impl_timePulse(this.durations[this.curIndex]);
                if (newTicks == cycleTicks) {
                    this.curIndex = this.end;
                }
            } else {
                long localTicks3 = TickCalculation.sub(newTicks, TickCalculation.add(this.startTimes[this.curIndex], this.delays[this.curIndex]));
                newAnimation.impl_timePulse(localTicks3);
            }
        } else {
            if (current != null) {
                long oldDelay2 = TickCalculation.add(this.startTimes[this.curIndex], this.delays[this.curIndex]);
                if (this.oldTicks >= this.startTimes[this.curIndex + 1] || (this.oldTicks > oldDelay2 && current.getStatus() == Animation.Status.STOPPED)) {
                    boolean enteringCycle4 = this.oldTicks >= this.startTimes[this.curIndex + 1];
                    if (enteringCycle4) {
                        current.clipEnvelope.jumpTo(Math.round(this.durations[this.curIndex] * this.rates[this.curIndex]));
                    }
                    if (!startChild(current, this.curIndex) && enteringCycle4 && (handler = current.getOnFinished()) != null) {
                        handler.handle(new ActionEvent(this, null));
                    }
                }
                if (current.getStatus() == Animation.Status.RUNNING) {
                    current.impl_timePulse(TickCalculation.sub(this.durations[this.curIndex], this.offsetTicks));
                }
                this.oldTicks = this.startTimes[this.curIndex];
            }
            this.offsetTicks = 0L;
            this.curIndex--;
            while (this.curIndex > newIndex) {
                Animation animation2 = this.cachedChildren[this.curIndex];
                animation2.clipEnvelope.jumpTo(Math.round(this.durations[this.curIndex] * this.rates[this.curIndex]));
                if (startChild(animation2, this.curIndex)) {
                    animation2.impl_timePulse(this.durations[this.curIndex]);
                } else {
                    EventHandler<ActionEvent> handler7 = animation2.getOnFinished();
                    if (handler7 != null) {
                        handler7.handle(new ActionEvent(this, null));
                    }
                }
                this.oldTicks = this.startTimes[this.curIndex];
                this.curIndex--;
            }
            Animation newAnimation2 = this.cachedChildren[this.curIndex];
            newAnimation2.clipEnvelope.jumpTo(Math.round(this.durations[this.curIndex] * this.rates[this.curIndex]));
            if (!startChild(newAnimation2, this.curIndex)) {
                EventHandler<ActionEvent> handler8 = newAnimation2.getOnFinished();
                if (handler8 != null) {
                    handler8.handle(new ActionEvent(this, null));
                }
            } else if (newTicks <= TickCalculation.add(this.startTimes[this.curIndex], this.delays[this.curIndex])) {
                newAnimation2.impl_timePulse(this.durations[this.curIndex]);
                if (newTicks == 0) {
                    this.curIndex = -1;
                }
            } else {
                long localTicks4 = TickCalculation.sub(this.startTimes[this.curIndex + 1], newTicks);
                newAnimation2.impl_timePulse(localTicks4);
            }
        }
        this.oldTicks = newTicks;
    }

    @Override // javafx.animation.Transition, javafx.animation.Animation
    void impl_jumpTo(long currentTicks, long cycleTicks, boolean forceJump) {
        impl_setCurrentTicks(currentTicks);
        Animation.Status status = getStatus();
        if (status == Animation.Status.STOPPED && !forceJump) {
            return;
        }
        impl_sync(false);
        double frac = calculateFraction(currentTicks, cycleTicks);
        long newTicks = Math.max(0L, Math.min(getCachedInterpolator().interpolate(0L, cycleTicks, frac), cycleTicks));
        int oldIndex = this.curIndex;
        this.curIndex = findNewIndex(newTicks);
        Animation newAnimation = this.cachedChildren[this.curIndex];
        double currentRate = getCurrentRate();
        long currentDelay = TickCalculation.add(this.startTimes[this.curIndex], this.delays[this.curIndex]);
        if (this.curIndex != oldIndex && status != Animation.Status.STOPPED) {
            if (oldIndex != -1 && oldIndex != this.end) {
                Animation oldChild = this.cachedChildren[oldIndex];
                if (oldChild.getStatus() != Animation.Status.STOPPED) {
                    this.cachedChildren[oldIndex].impl_stop();
                }
            }
            if (this.curIndex < oldIndex) {
                for (int i2 = oldIndex == this.end ? this.end - 1 : oldIndex; i2 > this.curIndex; i2--) {
                    this.cachedChildren[i2].impl_jumpTo(0L, this.durations[i2], true);
                }
            } else {
                for (int i3 = oldIndex == -1 ? 0 : oldIndex; i3 < this.curIndex; i3++) {
                    this.cachedChildren[i3].impl_jumpTo(this.durations[i3], this.durations[i3], true);
                }
            }
            if (newTicks >= currentDelay) {
                startChild(newAnimation, this.curIndex);
                if (status == Animation.Status.PAUSED) {
                    newAnimation.impl_pause();
                }
            }
        }
        if (oldIndex == this.curIndex) {
            if (currentRate == 0.0d) {
                this.offsetTicks = (long) (this.offsetTicks + ((newTicks - this.oldTicks) * Math.signum(this.clipEnvelope.getCurrentRate())));
            } else {
                this.offsetTicks += currentRate > 0.0d ? newTicks - this.oldTicks : this.oldTicks - newTicks;
            }
        } else if (currentRate != 0.0d) {
            this.offsetTicks = currentRate > 0.0d ? Math.max(0L, newTicks - currentDelay) : this.startTimes[this.curIndex + 1] - newTicks;
        } else if (this.clipEnvelope.getCurrentRate() > 0.0d) {
            this.offsetTicks = Math.max(0L, newTicks - currentDelay);
        } else {
            this.offsetTicks = (this.startTimes[this.curIndex] + this.durations[this.curIndex]) - newTicks;
        }
        newAnimation.clipEnvelope.jumpTo(Math.round(TickCalculation.sub(newTicks, currentDelay) * this.rates[this.curIndex]));
        this.oldTicks = newTicks;
    }

    private void jumpToEnd() {
        for (int i2 = 0; i2 < this.end; i2++) {
            if (this.forceChildSync[i2]) {
                this.cachedChildren[i2].impl_sync(true);
            }
            this.cachedChildren[i2].impl_jumpTo(this.durations[i2], this.durations[i2], true);
        }
    }

    private void jumpToBefore() {
        for (int i2 = this.end - 1; i2 >= 0; i2--) {
            if (this.forceChildSync[i2]) {
                this.cachedChildren[i2].impl_sync(true);
            }
            this.cachedChildren[i2].impl_jumpTo(0L, this.durations[i2], true);
        }
    }

    @Override // javafx.animation.Transition
    protected void interpolate(double frac) {
    }
}
