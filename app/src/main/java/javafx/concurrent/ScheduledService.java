package javafx.concurrent;

import java.util.Timer;
import java.util.TimerTask;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Callback;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:javafx/concurrent/ScheduledService.class */
public abstract class ScheduledService<V> extends Service<V> {
    public static final Callback<ScheduledService<?>, Duration> EXPONENTIAL_BACKOFF_STRATEGY;
    public static final Callback<ScheduledService<?>, Duration> LOGARITHMIC_BACKOFF_STRATEGY;
    public static final Callback<ScheduledService<?>, Duration> LINEAR_BACKOFF_STRATEGY;
    private static final Timer DELAY_TIMER;
    private ObjectProperty<Duration> delay = new SimpleObjectProperty(this, "delay", Duration.ZERO);
    private ObjectProperty<Duration> period = new SimpleObjectProperty(this, "period", Duration.ZERO);
    private ObjectProperty<Callback<ScheduledService<?>, Duration>> backoffStrategy = new SimpleObjectProperty(this, "backoffStrategy", LOGARITHMIC_BACKOFF_STRATEGY);
    private BooleanProperty restartOnFailure = new SimpleBooleanProperty(this, "restartOnFailure", true);
    private IntegerProperty maximumFailureCount = new SimpleIntegerProperty(this, "maximumFailureCount", Integer.MAX_VALUE);
    private ReadOnlyIntegerWrapper currentFailureCount = new ReadOnlyIntegerWrapper(this, "currentFailureCount", 0);
    private ReadOnlyObjectWrapper<Duration> cumulativePeriod = new ReadOnlyObjectWrapper<>(this, "cumulativePeriod", Duration.ZERO);
    private ObjectProperty<Duration> maximumCumulativePeriod = new SimpleObjectProperty(this, "maximumCumulativePeriod", Duration.INDEFINITE);
    private ReadOnlyObjectWrapper<V> lastValue = new ReadOnlyObjectWrapper<>(this, "lastValue", null);
    private long lastRunTime = 0;
    private boolean freshStart = true;
    private TimerTask delayTask = null;
    private boolean stop = false;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ScheduledService.class.desiredAssertionStatus();
        EXPONENTIAL_BACKOFF_STRATEGY = new Callback<ScheduledService<?>, Duration>() { // from class: javafx.concurrent.ScheduledService.1
            @Override // javafx.util.Callback
            public Duration call(ScheduledService<?> service) {
                if (service == null) {
                    return Duration.ZERO;
                }
                double period = service.getPeriod() == null ? 0.0d : service.getPeriod().toMillis();
                double x2 = service.getCurrentFailureCount();
                return Duration.millis(period == 0.0d ? Math.exp(x2) : period + (period * Math.exp(x2)));
            }
        };
        LOGARITHMIC_BACKOFF_STRATEGY = new Callback<ScheduledService<?>, Duration>() { // from class: javafx.concurrent.ScheduledService.2
            @Override // javafx.util.Callback
            public Duration call(ScheduledService<?> service) {
                if (service == null) {
                    return Duration.ZERO;
                }
                double period = service.getPeriod() == null ? 0.0d : service.getPeriod().toMillis();
                double x2 = service.getCurrentFailureCount();
                return Duration.millis(period == 0.0d ? Math.log1p(x2) : period + (period * Math.log1p(x2)));
            }
        };
        LINEAR_BACKOFF_STRATEGY = new Callback<ScheduledService<?>, Duration>() { // from class: javafx.concurrent.ScheduledService.3
            @Override // javafx.util.Callback
            public Duration call(ScheduledService<?> service) {
                if (service == null) {
                    return Duration.ZERO;
                }
                double period = service.getPeriod() == null ? 0.0d : service.getPeriod().toMillis();
                double x2 = service.getCurrentFailureCount();
                return Duration.millis(period == 0.0d ? x2 : period + (period * x2));
            }
        };
        DELAY_TIMER = new Timer("ScheduledService Delay Timer", true);
    }

    public final Duration getDelay() {
        return this.delay.get();
    }

    public final void setDelay(Duration value) {
        this.delay.set(value);
    }

    public final ObjectProperty<Duration> delayProperty() {
        return this.delay;
    }

    public final Duration getPeriod() {
        return this.period.get();
    }

    public final void setPeriod(Duration value) {
        this.period.set(value);
    }

    public final ObjectProperty<Duration> periodProperty() {
        return this.period;
    }

    public final Callback<ScheduledService<?>, Duration> getBackoffStrategy() {
        return this.backoffStrategy.get();
    }

    public final void setBackoffStrategy(Callback<ScheduledService<?>, Duration> value) {
        this.backoffStrategy.set(value);
    }

    public final ObjectProperty<Callback<ScheduledService<?>, Duration>> backoffStrategyProperty() {
        return this.backoffStrategy;
    }

    public final boolean getRestartOnFailure() {
        return this.restartOnFailure.get();
    }

    public final void setRestartOnFailure(boolean value) {
        this.restartOnFailure.set(value);
    }

    public final BooleanProperty restartOnFailureProperty() {
        return this.restartOnFailure;
    }

    public final int getMaximumFailureCount() {
        return this.maximumFailureCount.get();
    }

    public final void setMaximumFailureCount(int value) {
        this.maximumFailureCount.set(value);
    }

    public final IntegerProperty maximumFailureCountProperty() {
        return this.maximumFailureCount;
    }

    public final int getCurrentFailureCount() {
        return this.currentFailureCount.get();
    }

    public final ReadOnlyIntegerProperty currentFailureCountProperty() {
        return this.currentFailureCount.getReadOnlyProperty();
    }

    private void setCurrentFailureCount(int value) {
        this.currentFailureCount.set(value);
    }

    public final Duration getCumulativePeriod() {
        return this.cumulativePeriod.get();
    }

    public final ReadOnlyObjectProperty<Duration> cumulativePeriodProperty() {
        return this.cumulativePeriod.getReadOnlyProperty();
    }

    void setCumulativePeriod(Duration value) {
        Duration newValue = (value == null || value.toMillis() < 0.0d) ? Duration.ZERO : value;
        Duration maxPeriod = this.maximumCumulativePeriod.get();
        if (maxPeriod != null && !maxPeriod.isUnknown() && !newValue.isUnknown()) {
            if (maxPeriod.toMillis() < 0.0d) {
                newValue = Duration.ZERO;
            } else if (!maxPeriod.isIndefinite() && newValue.greaterThan(maxPeriod)) {
                newValue = maxPeriod;
            }
        }
        this.cumulativePeriod.set(newValue);
    }

    public final Duration getMaximumCumulativePeriod() {
        return this.maximumCumulativePeriod.get();
    }

    public final void setMaximumCumulativePeriod(Duration value) {
        this.maximumCumulativePeriod.set(value);
    }

    public final ObjectProperty<Duration> maximumCumulativePeriodProperty() {
        return this.maximumCumulativePeriod;
    }

    public final V getLastValue() {
        return this.lastValue.get();
    }

    public final ReadOnlyObjectProperty<V> lastValueProperty() {
        return this.lastValue.getReadOnlyProperty();
    }

    @Override // javafx.concurrent.Service
    protected void executeTask(Task<V> task) {
        if (!$assertionsDisabled && task == null) {
            throw new AssertionError();
        }
        checkThread();
        if (this.freshStart) {
            if (!$assertionsDisabled && this.delayTask != null) {
                throw new AssertionError();
            }
            setCumulativePeriod(getPeriod());
            long d2 = (long) normalize(getDelay());
            if (d2 == 0) {
                executeTaskNow(task);
                return;
            }
            TimerTask timerTaskCreateTimerTask = createTimerTask(task);
            this.delayTask = timerTaskCreateTimerTask;
            schedule(timerTaskCreateTimerTask, d2);
            return;
        }
        double cumulative = normalize(getCumulativePeriod());
        double runPeriod = clock() - this.lastRunTime;
        if (runPeriod < cumulative) {
            if (!$assertionsDisabled && this.delayTask != null) {
                throw new AssertionError();
            }
            TimerTask timerTaskCreateTimerTask2 = createTimerTask(task);
            this.delayTask = timerTaskCreateTimerTask2;
            schedule(timerTaskCreateTimerTask2, (long) (cumulative - runPeriod));
            return;
        }
        executeTaskNow(task);
    }

    @Override // javafx.concurrent.Service
    protected void succeeded() {
        super.succeeded();
        this.lastValue.set(getValue());
        Duration d2 = getPeriod();
        setCumulativePeriod(d2);
        boolean wasCancelled = this.stop;
        superReset();
        if (!$assertionsDisabled && this.freshStart) {
            throw new AssertionError();
        }
        if (wasCancelled) {
            cancelFromReadyState();
        } else {
            start();
        }
    }

    @Override // javafx.concurrent.Service
    protected void failed() {
        super.failed();
        if (!$assertionsDisabled && this.delayTask != null) {
            throw new AssertionError();
        }
        setCurrentFailureCount(getCurrentFailureCount() + 1);
        if (getRestartOnFailure() && getMaximumFailureCount() > getCurrentFailureCount()) {
            Callback<ScheduledService<?>, Duration> func = getBackoffStrategy();
            if (func != null) {
                Duration d2 = func.call(this);
                setCumulativePeriod(d2);
            }
            superReset();
            if (!$assertionsDisabled && this.freshStart) {
                throw new AssertionError();
            }
            start();
        }
    }

    @Override // javafx.concurrent.Service
    public void reset() {
        super.reset();
        this.stop = false;
        setCumulativePeriod(getPeriod());
        this.lastValue.set(null);
        setCurrentFailureCount(0);
        this.lastRunTime = 0L;
        this.freshStart = true;
    }

    @Override // javafx.concurrent.Service, javafx.concurrent.Worker
    public boolean cancel() {
        boolean ret = super.cancel();
        this.stop = true;
        if (this.delayTask != null) {
            this.delayTask.cancel();
            this.delayTask = null;
        }
        return ret;
    }

    void schedule(TimerTask task, long delay) {
        DELAY_TIMER.schedule(task, delay);
    }

    boolean isFreshStart() {
        return this.freshStart;
    }

    long clock() {
        return System.currentTimeMillis();
    }

    private void superReset() {
        super.reset();
    }

    private TimerTask createTimerTask(final Task<V> task) {
        if ($assertionsDisabled || task != null) {
            return new TimerTask() { // from class: javafx.concurrent.ScheduledService.4
                @Override // java.util.TimerTask, java.lang.Runnable
                public void run() {
                    Task task2 = task;
                    Runnable r2 = () -> {
                        ScheduledService.this.executeTaskNow(task2);
                        ScheduledService.this.delayTask = null;
                    };
                    if (ScheduledService.this.isFxApplicationThread()) {
                        r2.run();
                    } else {
                        ScheduledService.this.runLater(r2);
                    }
                }
            };
        }
        throw new AssertionError();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void executeTaskNow(Task<V> task) {
        if (!$assertionsDisabled && task == null) {
            throw new AssertionError();
        }
        this.lastRunTime = clock();
        this.freshStart = false;
        super.executeTask(task);
    }

    private static double normalize(Duration d2) {
        if (d2 == null || d2.isUnknown()) {
            return 0.0d;
        }
        if (d2.isIndefinite()) {
            return Double.MAX_VALUE;
        }
        return d2.toMillis();
    }
}
