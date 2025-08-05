package com.sun.scenario.animation;

import com.sun.javafx.tk.Toolkit;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: jfxrt.jar:com/sun/scenario/animation/AnimationPulse.class */
public class AnimationPulse implements AnimationPulseMBean {
    private final Queue<PulseData> pulseDataQueue = new ConcurrentLinkedQueue();
    private PulseData pulseData = null;
    private volatile boolean isEnabled = false;
    private final AtomicLong pulseCounter = new AtomicLong();
    private final AtomicLong startMax = new AtomicLong();
    private final AtomicLong startSum = new AtomicLong();
    private final AtomicLong startAv = new AtomicLong();
    private final AtomicLong endMax = new AtomicLong();
    private final AtomicLong endSum = new AtomicLong();
    private final AtomicLong endAv = new AtomicLong();
    private final AtomicLong animationDurationMax = new AtomicLong();
    private final AtomicLong animationDurationSum = new AtomicLong();
    private final AtomicLong animationDurationAv = new AtomicLong();
    private final AtomicLong paintingDurationMax = new AtomicLong();
    private final AtomicLong paintingDurationSum = new AtomicLong();
    private final AtomicLong paintingDurationAv = new AtomicLong();
    private final AtomicLong pulseDurationMax = new AtomicLong();
    private final AtomicLong pulseDurationSum = new AtomicLong();
    private final AtomicLong pulseDurationAv = new AtomicLong();
    private final AtomicLong[] maxAndAv = {this.startMax, this.startSum, this.startAv, this.endMax, this.endSum, this.endAv, this.animationDurationMax, this.animationDurationSum, this.animationDurationAv, this.paintingDurationMax, this.paintingDurationSum, this.paintingDurationAv, this.pulseDurationMax, this.pulseDurationSum, this.pulseDurationAv};
    private final PulseData.Accessor[] maxAndAvAccessors = {PulseData.PulseStartAccessor, PulseData.PulseEndAccessor, PulseData.AnimationDurationAccessor, PulseData.PaintingDurationAccessor, PulseData.PulseDurationAccessor};
    private final AtomicLong skippedPulses = new AtomicLong();
    private int skipPulses = 100;

    public static AnimationPulse getDefaultBean() {
        return AnimationPulseHolder.holder;
    }

    /* loaded from: jfxrt.jar:com/sun/scenario/animation/AnimationPulse$AnimationPulseHolder.class */
    private static class AnimationPulseHolder {
        private static final AnimationPulse holder = new AnimationPulse();

        private AnimationPulseHolder() {
        }
    }

    /* loaded from: jfxrt.jar:com/sun/scenario/animation/AnimationPulse$PulseData.class */
    private static class PulseData {
        private final long scheduledNanos;
        static final Accessor PulseStartAccessor = (pulseData1, unit) -> {
            return pulseData1.getPulseStart(unit);
        };
        static final Accessor AnimationDurationAccessor = (pulseData1, unit) -> {
            return pulseData1.getAnimationDuration(unit);
        };
        static final Accessor PaintingDurationAccessor = (pulseData1, unit) -> {
            return pulseData1.getPaintingDuration(unit);
        };
        static final Accessor ScenePaintingDurationAccessor = (pulseData1, unit) -> {
            return pulseData1.getScenePaintingDuration(unit);
        };
        static final Accessor PulseDurationAccessor = (pulseData1, unit) -> {
            return pulseData1.getPulseDuration(unit);
        };
        static final Accessor PulseEndAccessor = (pulseData1, unit) -> {
            return pulseData1.getPulseEnd(unit);
        };
        static final Accessor PaintingPreparationDuration = (pulseData1, unit) -> {
            return pulseData1.getPaintingDuration(unit);
        };
        static final Accessor PaintingFinalizationDuration = (pulseData1, unit) -> {
            return pulseData1.getPaintingFinalizationDuration(unit);
        };
        private long animationEndNanos = Long.MIN_VALUE;
        private long paintingStartNanos = Long.MIN_VALUE;
        private long paintingEndNanos = Long.MIN_VALUE;
        private long scenePaintingStartNanos = Long.MIN_VALUE;
        private long scenePaintingEndNanos = Long.MIN_VALUE;
        private long endNanos = Long.MIN_VALUE;
        private final long startNanos = Toolkit.getToolkit().getMasterTimer().nanos();

        /* loaded from: jfxrt.jar:com/sun/scenario/animation/AnimationPulse$PulseData$Accessor.class */
        interface Accessor {
            long get(PulseData pulseData, TimeUnit timeUnit);
        }

        PulseData(long shiftNanos) {
            this.scheduledNanos = this.startNanos + shiftNanos;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public long getPulseStart(TimeUnit unit) {
            return unit.convert(this.startNanos - this.scheduledNanos, TimeUnit.NANOSECONDS);
        }

        void recordAnimationEnd() {
            this.animationEndNanos = Toolkit.getToolkit().getMasterTimer().nanos();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public long getAnimationDuration(TimeUnit unit) {
            if (this.animationEndNanos > Long.MIN_VALUE) {
                return unit.convert(this.animationEndNanos - this.startNanos, TimeUnit.NANOSECONDS);
            }
            return 0L;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public long getPaintingDuration(TimeUnit unit) {
            if (this.paintingEndNanos <= Long.MIN_VALUE || this.paintingStartNanos <= Long.MIN_VALUE) {
                return 0L;
            }
            return unit.convert(this.paintingEndNanos - this.paintingStartNanos, TimeUnit.NANOSECONDS);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public long getScenePaintingDuration(TimeUnit unit) {
            if (this.scenePaintingEndNanos <= Long.MIN_VALUE || this.scenePaintingStartNanos <= Long.MIN_VALUE) {
                return 0L;
            }
            return unit.convert(this.scenePaintingEndNanos - this.scenePaintingStartNanos, TimeUnit.NANOSECONDS);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public long getPaintingFinalizationDuration(TimeUnit unit) {
            if (this.scenePaintingEndNanos <= Long.MIN_VALUE || this.paintingEndNanos <= Long.MIN_VALUE) {
                return 0L;
            }
            return unit.convert(this.paintingEndNanos - this.scenePaintingEndNanos, TimeUnit.NANOSECONDS);
        }

        void recordEnd() {
            this.endNanos = Toolkit.getToolkit().getMasterTimer().nanos();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public long getPulseDuration(TimeUnit unit) {
            return unit.convert(this.endNanos - this.startNanos, TimeUnit.NANOSECONDS);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public long getPulseEnd(TimeUnit unit) {
            return unit.convert(this.endNanos - this.scheduledNanos, TimeUnit.NANOSECONDS);
        }

        long getPulseStartFromNow(TimeUnit unit) {
            return unit.convert(Toolkit.getToolkit().getMasterTimer().nanos() - this.startNanos, TimeUnit.NANOSECONDS);
        }

        long getSkippedPulses() {
            return getPulseEnd(TimeUnit.MILLISECONDS) / AnimationPulse.getDefaultBean().getPULSE_DURATION();
        }
    }

    @Override // com.sun.scenario.animation.AnimationPulseMBean
    public boolean getEnabled() {
        return this.isEnabled;
    }

    @Override // com.sun.scenario.animation.AnimationPulseMBean
    public void setEnabled(boolean enabled) {
        if (enabled == this.isEnabled) {
            return;
        }
        this.isEnabled = enabled;
    }

    @Override // com.sun.scenario.animation.AnimationPulseMBean
    public long getPULSE_DURATION() {
        return Toolkit.getToolkit().getMasterTimer().getPulseDuration(1000);
    }

    @Override // com.sun.scenario.animation.AnimationPulseMBean
    public long getSkippedPulses() {
        return this.skippedPulses.get();
    }

    @Override // com.sun.scenario.animation.AnimationPulseMBean
    public long getSkippedPulsesIn1Sec() {
        long rv = 0;
        for (PulseData pulseData : this.pulseDataQueue) {
            if (pulseData.getPulseStartFromNow(TimeUnit.SECONDS) == 0) {
                rv += pulseData.getSkippedPulses();
            }
        }
        return rv;
    }

    public void recordStart(long shiftMillis) {
        if (!getEnabled()) {
            return;
        }
        this.pulseData = new PulseData(TimeUnit.MILLISECONDS.toNanos(shiftMillis));
    }

    private void purgeOldPulseData() {
        Iterator<PulseData> iterator = this.pulseDataQueue.iterator();
        while (iterator.hasNext() && iterator.next().getPulseStartFromNow(TimeUnit.SECONDS) > 1) {
            iterator.remove();
        }
    }

    private void updateMaxAndAv() {
        long pulseCounterLong = this.pulseCounter.incrementAndGet();
        for (int i2 = 0; i2 < this.maxAndAvAccessors.length; i2++) {
            int j2 = i2 * 3;
            long tmpLong = this.maxAndAvAccessors[i2].get(this.pulseData, TimeUnit.MILLISECONDS);
            this.maxAndAv[j2].set(Math.max(this.maxAndAv[j2].get(), tmpLong));
            this.maxAndAv[j2 + 1].addAndGet(tmpLong);
            this.maxAndAv[j2 + 2].set(this.maxAndAv[j2 + 1].get() / pulseCounterLong);
        }
    }

    public void recordEnd() {
        if (!getEnabled()) {
            return;
        }
        if (this.skipPulses > 0) {
            this.skipPulses--;
            this.pulseData = null;
            return;
        }
        this.pulseData.recordEnd();
        purgeOldPulseData();
        updateMaxAndAv();
        this.skippedPulses.addAndGet(this.pulseData.getSkippedPulses());
        this.pulseDataQueue.add(this.pulseData);
        this.pulseData = null;
    }

    private long getAv(PulseData.Accessor accessor, long timeOut, TimeUnit unit) {
        if (!getEnabled()) {
            return 0L;
        }
        long time = 0;
        long items = 0;
        for (PulseData currentPulseData : this.pulseDataQueue) {
            if (currentPulseData.getPulseStartFromNow(unit) <= timeOut) {
                time += accessor.get(currentPulseData, unit);
                items++;
            }
        }
        if (items == 0) {
            return 0L;
        }
        return time / items;
    }

    private long getMax(PulseData.Accessor accessor, long timeOut, TimeUnit unit) {
        if (!getEnabled()) {
            return 0L;
        }
        long max = 0;
        for (PulseData currentPulseData : this.pulseDataQueue) {
            if (currentPulseData.getPulseStartFromNow(unit) <= timeOut) {
                max = Math.max(accessor.get(currentPulseData, unit), max);
            }
        }
        return max;
    }

    @Override // com.sun.scenario.animation.AnimationPulseMBean
    public long getStartMax() {
        return this.startMax.get();
    }

    @Override // com.sun.scenario.animation.AnimationPulseMBean
    public long getStartAv() {
        return this.startAv.get();
    }

    @Override // com.sun.scenario.animation.AnimationPulseMBean
    public long getStartMaxIn1Sec() {
        return getMax(PulseData.PulseStartAccessor, 1000L, TimeUnit.MILLISECONDS);
    }

    @Override // com.sun.scenario.animation.AnimationPulseMBean
    public long getStartAvIn100Millis() {
        return getAv(PulseData.PulseStartAccessor, 100L, TimeUnit.MILLISECONDS);
    }

    @Override // com.sun.scenario.animation.AnimationPulseMBean
    public long getEndMax() {
        return this.endMax.get();
    }

    @Override // com.sun.scenario.animation.AnimationPulseMBean
    public long getEndMaxIn1Sec() {
        return getMax(PulseData.PulseEndAccessor, 1000L, TimeUnit.MILLISECONDS);
    }

    @Override // com.sun.scenario.animation.AnimationPulseMBean
    public long getEndAv() {
        return this.endAv.get();
    }

    @Override // com.sun.scenario.animation.AnimationPulseMBean
    public long getEndAvIn100Millis() {
        return getAv(PulseData.PulseEndAccessor, 100L, TimeUnit.MILLISECONDS);
    }

    public void recordAnimationEnd() {
        if (getEnabled() && this.pulseData != null) {
            this.pulseData.recordAnimationEnd();
        }
    }

    @Override // com.sun.scenario.animation.AnimationPulseMBean
    public long getAnimationDurationMax() {
        return this.animationDurationMax.get();
    }

    @Override // com.sun.scenario.animation.AnimationPulseMBean
    public long getAnimationMaxIn1Sec() {
        return getMax(PulseData.AnimationDurationAccessor, 1000L, TimeUnit.MILLISECONDS);
    }

    @Override // com.sun.scenario.animation.AnimationPulseMBean
    public long getAnimationDurationAv() {
        return this.animationDurationAv.get();
    }

    @Override // com.sun.scenario.animation.AnimationPulseMBean
    public long getAnimationDurationAvIn100Millis() {
        return getAv(PulseData.AnimationDurationAccessor, 100L, TimeUnit.MILLISECONDS);
    }

    @Override // com.sun.scenario.animation.AnimationPulseMBean
    public long getPaintingDurationMax() {
        return this.paintingDurationMax.get();
    }

    @Override // com.sun.scenario.animation.AnimationPulseMBean
    public long getPaintingDurationMaxIn1Sec() {
        return getMax(PulseData.PaintingDurationAccessor, 1000L, TimeUnit.MILLISECONDS);
    }

    @Override // com.sun.scenario.animation.AnimationPulseMBean
    public long getPaintingDurationAv() {
        return this.paintingDurationAv.get();
    }

    @Override // com.sun.scenario.animation.AnimationPulseMBean
    public long getPaintingDurationAvIn100Millis() {
        return getAv(PulseData.PaintingDurationAccessor, 100L, TimeUnit.MILLISECONDS);
    }

    @Override // com.sun.scenario.animation.AnimationPulseMBean
    public long getScenePaintingDurationMaxIn1Sec() {
        return getMax(PulseData.ScenePaintingDurationAccessor, 1000L, TimeUnit.MILLISECONDS);
    }

    @Override // com.sun.scenario.animation.AnimationPulseMBean
    public long getPulseDurationMax() {
        return this.pulseDurationMax.get();
    }

    @Override // com.sun.scenario.animation.AnimationPulseMBean
    public long getPulseDurationMaxIn1Sec() {
        return getMax(PulseData.PulseDurationAccessor, 1000L, TimeUnit.MILLISECONDS);
    }

    @Override // com.sun.scenario.animation.AnimationPulseMBean
    public long getPulseDurationAv() {
        return this.pulseDurationAv.get();
    }

    @Override // com.sun.scenario.animation.AnimationPulseMBean
    public long getPulseDurationAvIn100Millis() {
        return getAv(PulseData.PulseDurationAccessor, 100L, TimeUnit.MILLISECONDS);
    }

    @Override // com.sun.scenario.animation.AnimationPulseMBean
    public long getPaintingPreparationDurationMaxIn1Sec() {
        return getMax(PulseData.PaintingPreparationDuration, 1000L, TimeUnit.MILLISECONDS);
    }

    @Override // com.sun.scenario.animation.AnimationPulseMBean
    public long getPaintingFinalizationDurationMaxIn1Sec() {
        return getMax(PulseData.PaintingFinalizationDuration, 1000L, TimeUnit.MILLISECONDS);
    }
}
