package com.sun.scenario.animation;

import com.sun.javafx.animation.TickCalculation;
import com.sun.scenario.DelayedRunnable;
import com.sun.scenario.Settings;
import com.sun.scenario.animation.shared.PulseReceiver;
import com.sun.scenario.animation.shared.TimerReceiver;
import java.util.Arrays;
import javafx.util.Callback;

/* loaded from: jfxrt.jar:com/sun/scenario/animation/AbstractMasterTimer.class */
public abstract class AbstractMasterTimer {
    protected static final String PULSE_PROP = "javafx.animation.pulse";
    protected static final String FRAMERATE_PROP = "javafx.animation.framerate";
    protected static final String FIXED_PULSE_LENGTH_PROP = "com.sun.scenario.animation.fixed.pulse.length";
    protected static final String ANIMATION_MBEAN_ENABLED = "com.sun.scenario.animation.AnimationMBean.enabled";
    protected static final boolean enableAnimationMBean = false;
    private long totalPausedTime;
    private long startPauseTime;
    private int receiversLength;
    private boolean receiversLocked;
    private int animationTimersLength;
    private boolean animationTimersLocked;
    private final long fixedPulseLength;
    private long debugNanos;
    private final MainLoop theMaster;
    protected static final String FULLSPEED_PROP = "javafx.animation.fullspeed";
    private static boolean fullspeed = Settings.getBoolean(FULLSPEED_PROP);
    protected static final String ADAPTIVE_PULSE_PROP = "com.sun.scenario.animation.adaptivepulse";
    private static boolean useAdaptivePulse = Settings.getBoolean(ADAPTIVE_PULSE_PROP);
    private static Callback<String, Void> pcl = key -> {
        switch (key) {
            case "javafx.animation.fullspeed":
                fullspeed = Settings.getBoolean(FULLSPEED_PROP);
                break;
            case "com.sun.scenario.animation.adaptivepulse":
                useAdaptivePulse = Settings.getBoolean(ADAPTIVE_PULSE_PROP);
                break;
            case "com.sun.scenario.animation.AnimationMBean.enabled":
                AnimationPulse.getDefaultBean().setEnabled(Settings.getBoolean(ANIMATION_MBEAN_ENABLED));
                break;
        }
        return null;
    };
    private final int PULSE_DURATION_NS = getPulseDuration(1000000000);
    private final int PULSE_DURATION_TICKS = getPulseDuration((int) TickCalculation.fromMillis(1000.0d));
    private boolean paused = false;
    private PulseReceiver[] receivers = new PulseReceiver[2];
    private TimerReceiver[] animationTimers = new TimerReceiver[2];

    protected abstract void postUpdateAnimationRunnable(DelayedRunnable delayedRunnable);

    protected abstract int getPulseDuration(int i2);

    static {
        Settings.addPropertyChangeListener(pcl);
        int pulse = Settings.getInt(PULSE_PROP, -1);
        if (pulse != -1) {
            System.err.println("Setting PULSE_DURATION to " + pulse + " hz");
        }
    }

    boolean isPaused() {
        return this.paused;
    }

    long getTotalPausedTime() {
        return this.totalPausedTime;
    }

    long getStartPauseTime() {
        return this.startPauseTime;
    }

    public int getDefaultResolution() {
        return this.PULSE_DURATION_TICKS;
    }

    public void pause() {
        if (!this.paused) {
            this.startPauseTime = nanos();
            this.paused = true;
        }
    }

    public void resume() {
        if (this.paused) {
            this.paused = false;
            this.totalPausedTime += nanos() - this.startPauseTime;
        }
    }

    public long nanos() {
        if (this.fixedPulseLength > 0) {
            return this.debugNanos;
        }
        return this.paused ? this.startPauseTime : System.nanoTime() - this.totalPausedTime;
    }

    public boolean isFullspeed() {
        return fullspeed;
    }

    protected AbstractMasterTimer() {
        this.fixedPulseLength = Boolean.getBoolean(FIXED_PULSE_LENGTH_PROP) ? this.PULSE_DURATION_NS : 0L;
        this.debugNanos = 0L;
        this.theMaster = new MainLoop();
    }

    public void addPulseReceiver(PulseReceiver target) {
        boolean needMoreSize = this.receiversLength == this.receivers.length;
        if (this.receiversLocked || needMoreSize) {
            this.receivers = (PulseReceiver[]) Arrays.copyOf(this.receivers, needMoreSize ? ((this.receivers.length * 3) / 2) + 1 : this.receivers.length);
            this.receiversLocked = false;
        }
        PulseReceiver[] pulseReceiverArr = this.receivers;
        int i2 = this.receiversLength;
        this.receiversLength = i2 + 1;
        pulseReceiverArr[i2] = target;
        if (this.receiversLength != 1) {
            return;
        }
        this.theMaster.updateAnimationRunnable();
    }

    public void removePulseReceiver(PulseReceiver target) {
        if (this.receiversLocked) {
            this.receivers = (PulseReceiver[]) this.receivers.clone();
            this.receiversLocked = false;
        }
        int i2 = 0;
        while (true) {
            if (i2 >= this.receiversLength) {
                break;
            }
            if (target != this.receivers[i2]) {
                i2++;
            } else {
                if (i2 == this.receiversLength - 1) {
                    this.receivers[i2] = null;
                } else {
                    System.arraycopy(this.receivers, i2 + 1, this.receivers, i2, (this.receiversLength - i2) - 1);
                    this.receivers[this.receiversLength - 1] = null;
                }
                this.receiversLength--;
            }
        }
        if (this.receiversLength != 0) {
            return;
        }
        this.theMaster.updateAnimationRunnable();
    }

    public void addAnimationTimer(TimerReceiver timer) {
        boolean needMoreSize = this.animationTimersLength == this.animationTimers.length;
        if (this.animationTimersLocked || needMoreSize) {
            this.animationTimers = (TimerReceiver[]) Arrays.copyOf(this.animationTimers, needMoreSize ? ((this.animationTimers.length * 3) / 2) + 1 : this.animationTimers.length);
            this.animationTimersLocked = false;
        }
        TimerReceiver[] timerReceiverArr = this.animationTimers;
        int i2 = this.animationTimersLength;
        this.animationTimersLength = i2 + 1;
        timerReceiverArr[i2] = timer;
        if (this.animationTimersLength != 1) {
            return;
        }
        this.theMaster.updateAnimationRunnable();
    }

    public void removeAnimationTimer(TimerReceiver timer) {
        if (this.animationTimersLocked) {
            this.animationTimers = (TimerReceiver[]) this.animationTimers.clone();
            this.animationTimersLocked = false;
        }
        int i2 = 0;
        while (true) {
            if (i2 >= this.animationTimersLength) {
                break;
            }
            if (timer != this.animationTimers[i2]) {
                i2++;
            } else {
                if (i2 == this.animationTimersLength - 1) {
                    this.animationTimers[i2] = null;
                } else {
                    System.arraycopy(this.animationTimers, i2 + 1, this.animationTimers, i2, (this.animationTimersLength - i2) - 1);
                    this.animationTimers[this.animationTimersLength - 1] = null;
                }
                this.animationTimersLength--;
            }
        }
        if (this.animationTimersLength != 0) {
            return;
        }
        this.theMaster.updateAnimationRunnable();
    }

    protected void recordStart(long shiftMillis) {
    }

    protected void recordEnd() {
    }

    protected void recordAnimationEnd() {
    }

    /* loaded from: jfxrt.jar:com/sun/scenario/animation/AbstractMasterTimer$MainLoop.class */
    private final class MainLoop implements DelayedRunnable {
        private boolean inactive;
        private long nextPulseTime;
        private long lastPulseDuration;

        private MainLoop() {
            this.inactive = true;
            this.nextPulseTime = AbstractMasterTimer.this.nanos();
            this.lastPulseDuration = -2147483648L;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (AbstractMasterTimer.this.paused) {
                return;
            }
            long now = AbstractMasterTimer.this.nanos();
            AbstractMasterTimer.this.recordStart((this.nextPulseTime - now) / 1000000);
            AbstractMasterTimer.this.timePulseImpl(now);
            AbstractMasterTimer.this.recordEnd();
            updateNextPulseTime(now);
            updateAnimationRunnable();
        }

        @Override // com.sun.scenario.DelayedRunnable
        public long getDelay() {
            long now = AbstractMasterTimer.this.nanos();
            long timeUntilPulse = (this.nextPulseTime - now) / 1000000;
            return Math.max(0L, timeUntilPulse);
        }

        private void updateNextPulseTime(long pulseStarted) {
            long now = AbstractMasterTimer.this.nanos();
            if (!AbstractMasterTimer.fullspeed) {
                if (AbstractMasterTimer.useAdaptivePulse) {
                    this.nextPulseTime += AbstractMasterTimer.this.PULSE_DURATION_NS;
                    long pulseDuration = now - pulseStarted;
                    if (pulseDuration - this.lastPulseDuration > 500000) {
                        pulseDuration /= 2;
                    }
                    if (pulseDuration < 2000000) {
                        pulseDuration = 2000000;
                    }
                    if (pulseDuration >= AbstractMasterTimer.this.PULSE_DURATION_NS) {
                        pulseDuration = (3 * AbstractMasterTimer.this.PULSE_DURATION_NS) / 4;
                    }
                    this.lastPulseDuration = pulseDuration;
                    this.nextPulseTime -= pulseDuration;
                    return;
                }
                this.nextPulseTime = ((this.nextPulseTime + AbstractMasterTimer.this.PULSE_DURATION_NS) / AbstractMasterTimer.this.PULSE_DURATION_NS) * AbstractMasterTimer.this.PULSE_DURATION_NS;
                return;
            }
            this.nextPulseTime = now;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateAnimationRunnable() {
            boolean newInactive = AbstractMasterTimer.this.animationTimersLength == 0 && AbstractMasterTimer.this.receiversLength == 0;
            if (this.inactive != newInactive) {
                this.inactive = newInactive;
                DelayedRunnable animationRunnable = this.inactive ? null : this;
                AbstractMasterTimer.this.postUpdateAnimationRunnable(animationRunnable);
            }
        }
    }

    protected void timePulseImpl(long now) {
        if (this.fixedPulseLength > 0) {
            this.debugNanos += this.fixedPulseLength;
            now = this.debugNanos;
        }
        PulseReceiver[] receiversSnapshot = this.receivers;
        int rLength = this.receiversLength;
        try {
            this.receiversLocked = true;
            for (int i2 = 0; i2 < rLength; i2++) {
                receiversSnapshot[i2].timePulse(TickCalculation.fromNano(now));
            }
            recordAnimationEnd();
            TimerReceiver[] animationTimersSnapshot = this.animationTimers;
            int aTLength = this.animationTimersLength;
            try {
                this.animationTimersLocked = true;
                for (int i3 = 0; i3 < aTLength; i3++) {
                    animationTimersSnapshot[i3].handle(now);
                }
            } finally {
                this.animationTimersLocked = false;
            }
        } finally {
            this.receiversLocked = false;
        }
    }
}
