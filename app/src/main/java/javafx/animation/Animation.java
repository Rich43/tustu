package javafx.animation;

import com.sun.javafx.animation.TickCalculation;
import com.sun.javafx.tk.Toolkit;
import com.sun.scenario.animation.AbstractMasterTimer;
import com.sun.scenario.animation.shared.ClipEnvelope;
import com.sun.scenario.animation.shared.PulseReceiver;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.HashMap;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.IntegerPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoublePropertyBase;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import jdk.nashorn.internal.runtime.regexp.joni.constants.AsmConstants;

/*  JADX ERROR: NullPointerException in pass: ClassModifier
    java.lang.NullPointerException: Cannot invoke "java.util.List.forEach(java.util.function.Consumer)" because "blocks" is null
    	at jadx.core.utils.BlockUtils.collectAllInsns(BlockUtils.java:1029)
    	at jadx.core.dex.visitors.ClassModifier.removeBridgeMethod(ClassModifier.java:245)
    	at jadx.core.dex.visitors.ClassModifier.removeSyntheticMethods(ClassModifier.java:160)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.ClassModifier.visit(ClassModifier.java:65)
    */
/* loaded from: jfxrt.jar:javafx/animation/Animation.class */
public abstract class Animation {
    public static final int INDEFINITE = -1;
    private static final double EPSILON = 1.0E-12d;
    private long startTime;
    private long pauseTime;
    private boolean paused;
    private final AbstractMasterTimer timer;
    private AccessControlContext accessCtrlCtx;
    final PulseReceiver pulseReceiver;
    Animation parent;
    ClipEnvelope clipEnvelope;
    private boolean lastPlayedFinished;
    private boolean lastPlayedForward;
    private DoubleProperty rate;
    private static final double DEFAULT_RATE = 1.0d;
    private double oldRate;
    private ReadOnlyDoubleProperty currentRate;
    private static final double DEFAULT_CURRENT_RATE = 0.0d;
    private ReadOnlyObjectProperty<Duration> cycleDuration;
    private static final Duration DEFAULT_CYCLE_DURATION;
    private ReadOnlyObjectProperty<Duration> totalDuration;
    private static final Duration DEFAULT_TOTAL_DURATION;
    private CurrentTimeProperty currentTime;
    private long currentTicks;
    private ObjectProperty<Duration> delay;
    private static final Duration DEFAULT_DELAY;
    private IntegerProperty cycleCount;
    private static final int DEFAULT_CYCLE_COUNT = 1;
    private BooleanProperty autoReverse;
    private static final boolean DEFAULT_AUTO_REVERSE = false;
    private ReadOnlyObjectProperty<Status> status;
    private static final Status DEFAULT_STATUS;
    private final double targetFramerate;
    private final int resolution;
    private long lastPulse;
    private ObjectProperty<EventHandler<ActionEvent>> onFinished;
    private static final EventHandler<ActionEvent> DEFAULT_ON_FINISHED;
    private final ObservableMap<String, Duration> cuePoints;

    /* loaded from: jfxrt.jar:javafx/animation/Animation$Status.class */
    public enum Status {
        PAUSED,
        RUNNING,
        STOPPED
    }

    abstract void impl_playTo(long j2, long j3);

    abstract void impl_jumpTo(long j2, long j3, boolean z2);

    /*  JADX ERROR: Failed to decode insn: 0x0002: MOVE_MULTI
        java.lang.ArrayIndexOutOfBoundsException: arraycopy: source index -1 out of bounds for object array[6]
        	at java.base/java.lang.System.arraycopy(Native Method)
        	at jadx.plugins.input.java.data.code.StackState.insert(StackState.java:52)
        	at jadx.plugins.input.java.data.code.CodeDecodeState.insert(CodeDecodeState.java:137)
        	at jadx.plugins.input.java.data.code.JavaInsnsRegister.dup2x1(JavaInsnsRegister.java:313)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:50)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:85)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:46)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:458)
        	at jadx.core.ProcessClass.process(ProcessClass.java:69)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:109)
        	at jadx.core.dex.nodes.ClassNode.generateClassCode(ClassNode.java:401)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:389)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:339)
        */
    static /* synthetic */ double access$302(javafx.animation.Animation r6, double r7) {
        /*
            r0 = r6
            r1 = r7
            // decode failed: arraycopy: source index -1 out of bounds for object array[6]
            r0.oldRate = r1
            return r-1
        */
        throw new UnsupportedOperationException("Method not decompiled: javafx.animation.Animation.access$302(javafx.animation.Animation, double):double");
    }

    static {
        AnimationAccessorImpl.DEFAULT = new AnimationAccessorImpl();
        DEFAULT_CYCLE_DURATION = Duration.ZERO;
        DEFAULT_TOTAL_DURATION = Duration.ZERO;
        DEFAULT_DELAY = Duration.ZERO;
        DEFAULT_STATUS = Status.STOPPED;
        DEFAULT_ON_FINISHED = null;
    }

    private long now() {
        return TickCalculation.fromNano(this.timer.nanos());
    }

    private void addPulseReceiver() {
        this.accessCtrlCtx = AccessController.getContext();
        this.timer.addPulseReceiver(this.pulseReceiver);
    }

    void startReceiver(long delay) {
        this.paused = false;
        this.startTime = now() + delay;
        addPulseReceiver();
    }

    void pauseReceiver() {
        if (!this.paused) {
            this.pauseTime = now();
            this.paused = true;
            this.timer.removePulseReceiver(this.pulseReceiver);
        }
    }

    void resumeReceiver() {
        if (this.paused) {
            long deltaTime = now() - this.pauseTime;
            this.startTime += deltaTime;
            this.paused = false;
            addPulseReceiver();
        }
    }

    /* loaded from: jfxrt.jar:javafx/animation/Animation$CurrentRateProperty.class */
    private class CurrentRateProperty extends ReadOnlyDoublePropertyBase {
        private double value;

        private CurrentRateProperty() {
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Animation.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "currentRate";
        }

        @Override // javafx.beans.value.ObservableDoubleValue
        public double get() {
            return this.value;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void set(double value) {
            this.value = value;
            fireValueChangedEvent();
        }
    }

    /* loaded from: jfxrt.jar:javafx/animation/Animation$AnimationReadOnlyProperty.class */
    private class AnimationReadOnlyProperty<T> extends ReadOnlyObjectPropertyBase<T> {
        private final String name;
        private T value;

        private AnimationReadOnlyProperty(String name, T value) {
            this.name = name;
            this.value = value;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Animation.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return this.name;
        }

        @Override // javafx.beans.value.ObservableObjectValue
        public T get() {
            return this.value;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void set(T value) {
            this.value = value;
            fireValueChangedEvent();
        }
    }

    public final void setRate(double value) {
        if (this.rate != null || Math.abs(value - 1.0d) > 1.0E-12d) {
            rateProperty().set(value);
        }
    }

    public final double getRate() {
        if (this.rate == null) {
            return 1.0d;
        }
        return this.rate.get();
    }

    public final DoubleProperty rateProperty() {
        if (this.rate == null) {
            this.rate = new DoublePropertyBase(1.0d) { // from class: javafx.animation.Animation.2
                /* JADX WARN: Failed to check method for inline after forced processjavafx.animation.Animation.access$302(javafx.animation.Animation, double):double */
                @Override // javafx.beans.property.DoublePropertyBase
                public void invalidated() {
                    double newRate = Animation.this.getRate();
                    if (Animation.this.isRunningEmbedded()) {
                        if (isBound()) {
                            unbind();
                        }
                        set(Animation.this.oldRate);
                        throw new IllegalArgumentException("Cannot set rate of embedded animation while running.");
                    }
                    if (Math.abs(newRate) < 1.0E-12d) {
                        if (Animation.this.getStatus() == Status.RUNNING) {
                            Animation.this.lastPlayedForward = Math.abs(Animation.this.getCurrentRate() - Animation.this.oldRate) < 1.0E-12d;
                        }
                        Animation.this.setCurrentRate(0.0d);
                        Animation.this.pauseReceiver();
                    } else {
                        if (Animation.this.getStatus() == Status.RUNNING) {
                            double currentRate = Animation.this.getCurrentRate();
                            if (Math.abs(currentRate) < 1.0E-12d) {
                                Animation.this.setCurrentRate(Animation.this.lastPlayedForward ? newRate : -newRate);
                                Animation.this.resumeReceiver();
                            } else {
                                boolean playingForward = Math.abs(currentRate - Animation.this.oldRate) < 1.0E-12d;
                                Animation.this.setCurrentRate(playingForward ? newRate : -newRate);
                            }
                        }
                        Animation.access$302(Animation.this, newRate);
                    }
                    Animation.this.clipEnvelope.setRate(newRate);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Animation.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "rate";
                }
            };
        }
        return this.rate;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isRunningEmbedded() {
        if (this.parent == null) {
            return false;
        }
        return this.parent.getStatus() != Status.STOPPED || this.parent.isRunningEmbedded();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCurrentRate(double value) {
        if (this.currentRate != null || Math.abs(value - 0.0d) > 1.0E-12d) {
            ((CurrentRateProperty) currentRateProperty()).set(value);
        }
    }

    public final double getCurrentRate() {
        if (this.currentRate == null) {
            return 0.0d;
        }
        return this.currentRate.get();
    }

    public final ReadOnlyDoubleProperty currentRateProperty() {
        if (this.currentRate == null) {
            this.currentRate = new CurrentRateProperty();
        }
        return this.currentRate;
    }

    protected final void setCycleDuration(Duration value) {
        if (this.cycleDuration != null || !DEFAULT_CYCLE_DURATION.equals(value)) {
            if (!value.lessThan(Duration.ZERO)) {
                ((AnimationReadOnlyProperty) cycleDurationProperty()).set(value);
                updateTotalDuration();
                return;
            }
            throw new IllegalArgumentException("Cycle duration cannot be negative");
        }
    }

    public final Duration getCycleDuration() {
        return this.cycleDuration == null ? DEFAULT_CYCLE_DURATION : this.cycleDuration.get();
    }

    public final ReadOnlyObjectProperty<Duration> cycleDurationProperty() {
        if (this.cycleDuration == null) {
            this.cycleDuration = new AnimationReadOnlyProperty("cycleDuration", DEFAULT_CYCLE_DURATION);
        }
        return this.cycleDuration;
    }

    public final Duration getTotalDuration() {
        return this.totalDuration == null ? DEFAULT_TOTAL_DURATION : this.totalDuration.get();
    }

    public final ReadOnlyObjectProperty<Duration> totalDurationProperty() {
        if (this.totalDuration == null) {
            this.totalDuration = new AnimationReadOnlyProperty("totalDuration", DEFAULT_TOTAL_DURATION);
        }
        return this.totalDuration;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTotalDuration() {
        Duration durationMultiply;
        int cycleCount = getCycleCount();
        Duration cycleDuration = getCycleDuration();
        if (Duration.ZERO.equals(cycleDuration)) {
            durationMultiply = Duration.ZERO;
        } else if (cycleCount == -1) {
            durationMultiply = Duration.INDEFINITE;
        } else {
            durationMultiply = cycleCount <= 1 ? cycleDuration : cycleDuration.multiply(cycleCount);
        }
        Duration newTotalDuration = durationMultiply;
        if (this.totalDuration != null || !DEFAULT_TOTAL_DURATION.equals(newTotalDuration)) {
            ((AnimationReadOnlyProperty) totalDurationProperty()).set(newTotalDuration);
        }
        if (getStatus() == Status.STOPPED) {
            syncClipEnvelope();
            if (newTotalDuration.lessThan(getCurrentTime())) {
                this.clipEnvelope.jumpTo(TickCalculation.fromDuration(newTotalDuration));
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/animation/Animation$CurrentTimeProperty.class */
    private class CurrentTimeProperty extends ReadOnlyObjectPropertyBase<Duration> {
        private CurrentTimeProperty() {
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return Animation.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "currentTime";
        }

        @Override // javafx.beans.value.ObservableObjectValue
        public Duration get() {
            return Animation.this.getCurrentTime();
        }

        @Override // javafx.beans.property.ReadOnlyObjectPropertyBase
        public void fireValueChangedEvent() {
            super.fireValueChangedEvent();
        }
    }

    public final Duration getCurrentTime() {
        return TickCalculation.toDuration(this.currentTicks);
    }

    public final ReadOnlyObjectProperty<Duration> currentTimeProperty() {
        if (this.currentTime == null) {
            this.currentTime = new CurrentTimeProperty();
        }
        return this.currentTime;
    }

    public final void setDelay(Duration value) {
        if (this.delay != null || !DEFAULT_DELAY.equals(value)) {
            delayProperty().set(value);
        }
    }

    public final Duration getDelay() {
        return this.delay == null ? DEFAULT_DELAY : this.delay.get();
    }

    public final ObjectProperty<Duration> delayProperty() {
        if (this.delay == null) {
            this.delay = new ObjectPropertyBase<Duration>(DEFAULT_DELAY) { // from class: javafx.animation.Animation.3
                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Animation.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "delay";
                }

                @Override // javafx.beans.property.ObjectPropertyBase
                protected void invalidated() {
                    Duration newDuration = get();
                    if (newDuration.lessThan(Duration.ZERO)) {
                        if (isBound()) {
                            unbind();
                        }
                        set(Duration.ZERO);
                        throw new IllegalArgumentException("Cannot set delay to negative value. Setting to Duration.ZERO");
                    }
                }
            };
        }
        return this.delay;
    }

    public final void setCycleCount(int value) {
        if (this.cycleCount != null || value != 1) {
            cycleCountProperty().set(value);
        }
    }

    public final int getCycleCount() {
        if (this.cycleCount == null) {
            return 1;
        }
        return this.cycleCount.get();
    }

    public final IntegerProperty cycleCountProperty() {
        if (this.cycleCount == null) {
            this.cycleCount = new IntegerPropertyBase(1) { // from class: javafx.animation.Animation.4
                @Override // javafx.beans.property.IntegerPropertyBase
                public void invalidated() {
                    Animation.this.updateTotalDuration();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Animation.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "cycleCount";
                }
            };
        }
        return this.cycleCount;
    }

    public final void setAutoReverse(boolean value) {
        if (this.autoReverse != null || value) {
            autoReverseProperty().set(value);
        }
    }

    public final boolean isAutoReverse() {
        if (this.autoReverse == null) {
            return false;
        }
        return this.autoReverse.get();
    }

    public final BooleanProperty autoReverseProperty() {
        if (this.autoReverse == null) {
            this.autoReverse = new SimpleBooleanProperty(this, "autoReverse", false);
        }
        return this.autoReverse;
    }

    protected final void setStatus(Status value) {
        if (this.status != null || !DEFAULT_STATUS.equals(value)) {
            ((AnimationReadOnlyProperty) statusProperty()).set(value);
        }
    }

    public final Status getStatus() {
        return this.status == null ? DEFAULT_STATUS : this.status.get();
    }

    public final ReadOnlyObjectProperty<Status> statusProperty() {
        if (this.status == null) {
            this.status = new AnimationReadOnlyProperty("status", Status.STOPPED);
        }
        return this.status;
    }

    public final double getTargetFramerate() {
        return this.targetFramerate;
    }

    public final void setOnFinished(EventHandler<ActionEvent> value) {
        if (this.onFinished != null || value != null) {
            onFinishedProperty().set(value);
        }
    }

    public final EventHandler<ActionEvent> getOnFinished() {
        return this.onFinished == null ? DEFAULT_ON_FINISHED : this.onFinished.get();
    }

    public final ObjectProperty<EventHandler<ActionEvent>> onFinishedProperty() {
        if (this.onFinished == null) {
            this.onFinished = new SimpleObjectProperty(this, "onFinished", DEFAULT_ON_FINISHED);
        }
        return this.onFinished;
    }

    public final ObservableMap<String, Duration> getCuePoints() {
        return this.cuePoints;
    }

    public void jumpTo(Duration time) {
        Duration time2;
        if (time == null) {
            throw new NullPointerException("Time needs to be specified.");
        }
        if (time.isUnknown()) {
            throw new IllegalArgumentException("The time is invalid");
        }
        if (this.parent != null) {
            throw new IllegalStateException("Cannot jump when embedded in another animation");
        }
        this.lastPlayedFinished = false;
        Duration totalDuration = getTotalDuration();
        if (time.lessThan(Duration.ZERO)) {
            time2 = Duration.ZERO;
        } else {
            time2 = time.greaterThan(totalDuration) ? totalDuration : time;
        }
        long ticks = TickCalculation.fromDuration(time2);
        if (getStatus() == Status.STOPPED) {
            syncClipEnvelope();
        }
        this.clipEnvelope.jumpTo(ticks);
    }

    public void jumpTo(String cuePoint) {
        if (cuePoint == null) {
            throw new NullPointerException("CuePoint needs to be specified");
        }
        if ("start".equalsIgnoreCase(cuePoint)) {
            jumpTo(Duration.ZERO);
            return;
        }
        if (AsmConstants.END.equalsIgnoreCase(cuePoint)) {
            jumpTo(getTotalDuration());
            return;
        }
        Duration target = getCuePoints().get(cuePoint);
        if (target != null) {
            jumpTo(target);
        }
    }

    public void playFrom(String cuePoint) {
        jumpTo(cuePoint);
        play();
    }

    public void playFrom(Duration time) {
        jumpTo(time);
        play();
    }

    public void play() {
        if (this.parent != null) {
            throw new IllegalStateException("Cannot start when embedded in another animation");
        }
        switch (getStatus()) {
            case STOPPED:
                if (impl_startable(true)) {
                    double rate = getRate();
                    if (this.lastPlayedFinished) {
                        jumpTo(rate < 0.0d ? getTotalDuration() : Duration.ZERO);
                    }
                    impl_start(true);
                    startReceiver(TickCalculation.fromDuration(getDelay()));
                    if (Math.abs(rate) < 1.0E-12d) {
                        pauseReceiver();
                        return;
                    }
                    return;
                }
                EventHandler<ActionEvent> handler = getOnFinished();
                if (handler != null) {
                    handler.handle(new ActionEvent(this, null));
                    return;
                }
                return;
            case PAUSED:
                impl_resume();
                if (Math.abs(getRate()) >= 1.0E-12d) {
                    resumeReceiver();
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void playFromStart() {
        stop();
        setRate(Math.abs(getRate()));
        jumpTo(Duration.ZERO);
        play();
    }

    public void stop() {
        if (this.parent != null) {
            throw new IllegalStateException("Cannot stop when embedded in another animation");
        }
        if (getStatus() != Status.STOPPED) {
            this.clipEnvelope.abortCurrentPulse();
            impl_stop();
            jumpTo(Duration.ZERO);
        }
    }

    public void pause() {
        if (this.parent != null) {
            throw new IllegalStateException("Cannot pause when embedded in another animation");
        }
        if (getStatus() == Status.RUNNING) {
            this.clipEnvelope.abortCurrentPulse();
            pauseReceiver();
            impl_pause();
        }
    }

    protected Animation(double targetFramerate) {
        this.paused = false;
        this.accessCtrlCtx = null;
        this.pulseReceiver = new PulseReceiver() { // from class: javafx.animation.Animation.1
            @Override // com.sun.scenario.animation.shared.PulseReceiver
            public void timePulse(long now) {
                long elapsedTime = now - Animation.this.startTime;
                if (elapsedTime >= 0) {
                    if (Animation.this.accessCtrlCtx == null) {
                        throw new IllegalStateException("Error: AccessControlContext not captured");
                    }
                    AccessController.doPrivileged(() -> {
                        Animation.this.impl_timePulse(elapsedTime);
                        return null;
                    }, Animation.this.accessCtrlCtx);
                }
            }
        };
        this.parent = null;
        this.lastPlayedFinished = false;
        this.lastPlayedForward = true;
        this.oldRate = 1.0d;
        this.cuePoints = FXCollections.observableMap(new HashMap(0));
        this.targetFramerate = targetFramerate;
        this.resolution = (int) Math.max(1L, Math.round(6000.0d / targetFramerate));
        this.clipEnvelope = ClipEnvelope.create(this);
        this.timer = Toolkit.getToolkit().getMasterTimer();
    }

    protected Animation() {
        this.paused = false;
        this.accessCtrlCtx = null;
        this.pulseReceiver = new PulseReceiver() { // from class: javafx.animation.Animation.1
            @Override // com.sun.scenario.animation.shared.PulseReceiver
            public void timePulse(long now) {
                long elapsedTime = now - Animation.this.startTime;
                if (elapsedTime >= 0) {
                    if (Animation.this.accessCtrlCtx == null) {
                        throw new IllegalStateException("Error: AccessControlContext not captured");
                    }
                    AccessController.doPrivileged(() -> {
                        Animation.this.impl_timePulse(elapsedTime);
                        return null;
                    }, Animation.this.accessCtrlCtx);
                }
            }
        };
        this.parent = null;
        this.lastPlayedFinished = false;
        this.lastPlayedForward = true;
        this.oldRate = 1.0d;
        this.cuePoints = FXCollections.observableMap(new HashMap(0));
        this.resolution = 1;
        this.targetFramerate = TickCalculation.TICKS_PER_SECOND / Toolkit.getToolkit().getMasterTimer().getDefaultResolution();
        this.clipEnvelope = ClipEnvelope.create(this);
        this.timer = Toolkit.getToolkit().getMasterTimer();
    }

    Animation(AbstractMasterTimer timer) {
        this.paused = false;
        this.accessCtrlCtx = null;
        this.pulseReceiver = new PulseReceiver() { // from class: javafx.animation.Animation.1
            @Override // com.sun.scenario.animation.shared.PulseReceiver
            public void timePulse(long now) {
                long elapsedTime = now - Animation.this.startTime;
                if (elapsedTime >= 0) {
                    if (Animation.this.accessCtrlCtx == null) {
                        throw new IllegalStateException("Error: AccessControlContext not captured");
                    }
                    AccessController.doPrivileged(() -> {
                        Animation.this.impl_timePulse(elapsedTime);
                        return null;
                    }, Animation.this.accessCtrlCtx);
                }
            }
        };
        this.parent = null;
        this.lastPlayedFinished = false;
        this.lastPlayedForward = true;
        this.oldRate = 1.0d;
        this.cuePoints = FXCollections.observableMap(new HashMap(0));
        this.resolution = 1;
        this.targetFramerate = TickCalculation.TICKS_PER_SECOND / timer.getDefaultResolution();
        this.clipEnvelope = ClipEnvelope.create(this);
        this.timer = timer;
    }

    Animation(AbstractMasterTimer timer, ClipEnvelope clipEnvelope, int resolution) {
        this.paused = false;
        this.accessCtrlCtx = null;
        this.pulseReceiver = new PulseReceiver() { // from class: javafx.animation.Animation.1
            @Override // com.sun.scenario.animation.shared.PulseReceiver
            public void timePulse(long now) {
                long elapsedTime = now - Animation.this.startTime;
                if (elapsedTime >= 0) {
                    if (Animation.this.accessCtrlCtx == null) {
                        throw new IllegalStateException("Error: AccessControlContext not captured");
                    }
                    AccessController.doPrivileged(() -> {
                        Animation.this.impl_timePulse(elapsedTime);
                        return null;
                    }, Animation.this.accessCtrlCtx);
                }
            }
        };
        this.parent = null;
        this.lastPlayedFinished = false;
        this.lastPlayedForward = true;
        this.oldRate = 1.0d;
        this.cuePoints = FXCollections.observableMap(new HashMap(0));
        this.resolution = resolution;
        this.targetFramerate = TickCalculation.TICKS_PER_SECOND / resolution;
        this.clipEnvelope = clipEnvelope;
        this.timer = timer;
    }

    boolean impl_startable(boolean forceSync) {
        return TickCalculation.fromDuration(getCycleDuration()) > 0 || (!forceSync && this.clipEnvelope.wasSynched());
    }

    void impl_sync(boolean forceSync) {
        if (forceSync || !this.clipEnvelope.wasSynched()) {
            syncClipEnvelope();
        }
    }

    private void syncClipEnvelope() {
        int publicCycleCount = getCycleCount();
        int internalCycleCount = (publicCycleCount > 0 || publicCycleCount == -1) ? publicCycleCount : 1;
        this.clipEnvelope = this.clipEnvelope.setCycleCount(internalCycleCount);
        this.clipEnvelope.setCycleDuration(getCycleDuration());
        this.clipEnvelope.setAutoReverse(isAutoReverse());
    }

    void impl_start(boolean forceSync) {
        impl_sync(forceSync);
        setStatus(Status.RUNNING);
        this.clipEnvelope.start();
        setCurrentRate(this.clipEnvelope.getCurrentRate());
        this.lastPulse = 0L;
    }

    void impl_pause() {
        double currentRate = getCurrentRate();
        if (Math.abs(currentRate) >= 1.0E-12d) {
            this.lastPlayedForward = Math.abs(getCurrentRate() - getRate()) < 1.0E-12d;
        }
        setCurrentRate(0.0d);
        setStatus(Status.PAUSED);
    }

    void impl_resume() {
        setStatus(Status.RUNNING);
        setCurrentRate(this.lastPlayedForward ? getRate() : -getRate());
    }

    void impl_stop() {
        if (!this.paused) {
            this.timer.removePulseReceiver(this.pulseReceiver);
        }
        setStatus(Status.STOPPED);
        setCurrentRate(0.0d);
    }

    void impl_timePulse(long elapsedTime) {
        if (this.resolution == 1) {
            this.clipEnvelope.timePulse(elapsedTime);
        } else if (elapsedTime - this.lastPulse >= this.resolution) {
            this.lastPulse = (elapsedTime / this.resolution) * this.resolution;
            this.clipEnvelope.timePulse(elapsedTime);
        }
    }

    void impl_setCurrentTicks(long ticks) {
        this.currentTicks = ticks;
        if (this.currentTime != null) {
            this.currentTime.fireValueChangedEvent();
        }
    }

    void impl_setCurrentRate(double currentRate) {
        setCurrentRate(currentRate);
    }

    final void impl_finished() {
        this.lastPlayedFinished = true;
        impl_stop();
        EventHandler<ActionEvent> handler = getOnFinished();
        if (handler != null) {
            try {
                handler.handle(new ActionEvent(this, null));
            } catch (Exception ex) {
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), ex);
            }
        }
    }
}
