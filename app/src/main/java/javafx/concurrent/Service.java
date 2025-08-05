package javafx.concurrent;

import java.lang.Thread;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Worker;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import sun.util.logging.PlatformLogger;

/* loaded from: jfxrt.jar:javafx/concurrent/Service.class */
public abstract class Service<V> implements Worker<V>, EventTarget {
    private static final int THREAD_POOL_SIZE = 32;
    private static final long THREAD_TIME_OUT = 1000;
    private Task<V> task;
    private static final PlatformLogger LOG = PlatformLogger.getLogger(Service.class.getName());
    private static final BlockingQueue<Runnable> IO_QUEUE = new LinkedBlockingQueue<Runnable>() { // from class: javafx.concurrent.Service.1
        @Override // java.util.concurrent.LinkedBlockingQueue, java.util.Queue
        public boolean offer(Runnable runnable) {
            if (Service.EXECUTOR.getPoolSize() < 32) {
                return false;
            }
            return super.offer((AnonymousClass1) runnable);
        }
    };
    private static final ThreadGroup THREAD_GROUP = (ThreadGroup) AccessController.doPrivileged(() -> {
        return new ThreadGroup("javafx concurrent thread pool");
    });
    private static final Thread.UncaughtExceptionHandler UNCAUGHT_HANDLER = (thread, throwable) -> {
        if (!(throwable instanceof IllegalMonitorStateException)) {
            LOG.warning("Uncaught throwable in " + THREAD_GROUP.getName(), throwable);
        }
    };
    private static final ThreadFactory THREAD_FACTORY = run -> {
        return (Thread) AccessController.doPrivileged(() -> {
            Thread th = new Thread(THREAD_GROUP, run);
            th.setUncaughtExceptionHandler(UNCAUGHT_HANDLER);
            th.setPriority(1);
            th.setDaemon(true);
            return th;
        });
    };
    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(2, 32, 1000, TimeUnit.MILLISECONDS, IO_QUEUE, THREAD_FACTORY, new ThreadPoolExecutor.AbortPolicy());
    private final ObjectProperty<Worker.State> state = new SimpleObjectProperty(this, "state", Worker.State.READY);
    private final ObjectProperty<V> value = new SimpleObjectProperty(this, "value");
    private final ObjectProperty<Throwable> exception = new SimpleObjectProperty(this, "exception");
    private final DoubleProperty workDone = new SimpleDoubleProperty(this, "workDone", -1.0d);
    private final DoubleProperty totalWorkToBeDone = new SimpleDoubleProperty(this, "totalWork", -1.0d);
    private final DoubleProperty progress = new SimpleDoubleProperty(this, "progress", -1.0d);
    private final BooleanProperty running = new SimpleBooleanProperty(this, "running", false);
    private final StringProperty message = new SimpleStringProperty(this, "message", "");
    private final StringProperty title = new SimpleStringProperty(this, "title", "");
    private final ObjectProperty<Executor> executor = new SimpleObjectProperty(this, "executor");
    private volatile boolean startedOnce = false;
    private EventHelper eventHelper = null;

    protected abstract Task<V> createTask();

    static {
        EXECUTOR.allowCoreThreadTimeOut(true);
    }

    @Override // javafx.concurrent.Worker
    public final Worker.State getState() {
        checkThread();
        return this.state.get();
    }

    @Override // javafx.concurrent.Worker
    public final ReadOnlyObjectProperty<Worker.State> stateProperty() {
        checkThread();
        return this.state;
    }

    @Override // javafx.concurrent.Worker
    public final V getValue() {
        checkThread();
        return this.value.get();
    }

    @Override // javafx.concurrent.Worker
    public final ReadOnlyObjectProperty<V> valueProperty() {
        checkThread();
        return this.value;
    }

    @Override // javafx.concurrent.Worker
    public final Throwable getException() {
        checkThread();
        return this.exception.get();
    }

    @Override // javafx.concurrent.Worker
    public final ReadOnlyObjectProperty<Throwable> exceptionProperty() {
        checkThread();
        return this.exception;
    }

    @Override // javafx.concurrent.Worker
    public final double getWorkDone() {
        checkThread();
        return this.workDone.get();
    }

    @Override // javafx.concurrent.Worker
    public final ReadOnlyDoubleProperty workDoneProperty() {
        checkThread();
        return this.workDone;
    }

    @Override // javafx.concurrent.Worker
    public final double getTotalWork() {
        checkThread();
        return this.totalWorkToBeDone.get();
    }

    @Override // javafx.concurrent.Worker
    public final ReadOnlyDoubleProperty totalWorkProperty() {
        checkThread();
        return this.totalWorkToBeDone;
    }

    @Override // javafx.concurrent.Worker
    public final double getProgress() {
        checkThread();
        return this.progress.get();
    }

    @Override // javafx.concurrent.Worker
    public final ReadOnlyDoubleProperty progressProperty() {
        checkThread();
        return this.progress;
    }

    @Override // javafx.concurrent.Worker
    public final boolean isRunning() {
        checkThread();
        return this.running.get();
    }

    @Override // javafx.concurrent.Worker
    public final ReadOnlyBooleanProperty runningProperty() {
        checkThread();
        return this.running;
    }

    @Override // javafx.concurrent.Worker
    public final String getMessage() {
        checkThread();
        return this.message.get();
    }

    @Override // javafx.concurrent.Worker
    public final ReadOnlyStringProperty messageProperty() {
        checkThread();
        return this.message;
    }

    @Override // javafx.concurrent.Worker
    public final String getTitle() {
        checkThread();
        return this.title.get();
    }

    @Override // javafx.concurrent.Worker
    public final ReadOnlyStringProperty titleProperty() {
        checkThread();
        return this.title;
    }

    public final void setExecutor(Executor value) {
        checkThread();
        this.executor.set(value);
    }

    public final Executor getExecutor() {
        checkThread();
        return this.executor.get();
    }

    public final ObjectProperty<Executor> executorProperty() {
        checkThread();
        return this.executor;
    }

    public final ObjectProperty<EventHandler<WorkerStateEvent>> onReadyProperty() {
        checkThread();
        return getEventHelper().onReadyProperty();
    }

    public final EventHandler<WorkerStateEvent> getOnReady() {
        checkThread();
        if (this.eventHelper == null) {
            return null;
        }
        return this.eventHelper.getOnReady();
    }

    public final void setOnReady(EventHandler<WorkerStateEvent> value) {
        checkThread();
        getEventHelper().setOnReady(value);
    }

    protected void ready() {
    }

    public final ObjectProperty<EventHandler<WorkerStateEvent>> onScheduledProperty() {
        checkThread();
        return getEventHelper().onScheduledProperty();
    }

    public final EventHandler<WorkerStateEvent> getOnScheduled() {
        checkThread();
        if (this.eventHelper == null) {
            return null;
        }
        return this.eventHelper.getOnScheduled();
    }

    public final void setOnScheduled(EventHandler<WorkerStateEvent> value) {
        checkThread();
        getEventHelper().setOnScheduled(value);
    }

    protected void scheduled() {
    }

    public final ObjectProperty<EventHandler<WorkerStateEvent>> onRunningProperty() {
        checkThread();
        return getEventHelper().onRunningProperty();
    }

    public final EventHandler<WorkerStateEvent> getOnRunning() {
        checkThread();
        if (this.eventHelper == null) {
            return null;
        }
        return this.eventHelper.getOnRunning();
    }

    public final void setOnRunning(EventHandler<WorkerStateEvent> value) {
        checkThread();
        getEventHelper().setOnRunning(value);
    }

    protected void running() {
    }

    public final ObjectProperty<EventHandler<WorkerStateEvent>> onSucceededProperty() {
        checkThread();
        return getEventHelper().onSucceededProperty();
    }

    public final EventHandler<WorkerStateEvent> getOnSucceeded() {
        checkThread();
        if (this.eventHelper == null) {
            return null;
        }
        return this.eventHelper.getOnSucceeded();
    }

    public final void setOnSucceeded(EventHandler<WorkerStateEvent> value) {
        checkThread();
        getEventHelper().setOnSucceeded(value);
    }

    protected void succeeded() {
    }

    public final ObjectProperty<EventHandler<WorkerStateEvent>> onCancelledProperty() {
        checkThread();
        return getEventHelper().onCancelledProperty();
    }

    public final EventHandler<WorkerStateEvent> getOnCancelled() {
        checkThread();
        if (this.eventHelper == null) {
            return null;
        }
        return this.eventHelper.getOnCancelled();
    }

    public final void setOnCancelled(EventHandler<WorkerStateEvent> value) {
        checkThread();
        getEventHelper().setOnCancelled(value);
    }

    protected void cancelled() {
    }

    public final ObjectProperty<EventHandler<WorkerStateEvent>> onFailedProperty() {
        checkThread();
        return getEventHelper().onFailedProperty();
    }

    public final EventHandler<WorkerStateEvent> getOnFailed() {
        checkThread();
        if (this.eventHelper == null) {
            return null;
        }
        return this.eventHelper.getOnFailed();
    }

    public final void setOnFailed(EventHandler<WorkerStateEvent> value) {
        checkThread();
        getEventHelper().setOnFailed(value);
    }

    protected void failed() {
    }

    protected Service() {
        this.state.addListener((observableValue, old, value1) -> {
            switch (value1) {
                case CANCELLED:
                    fireEvent(new WorkerStateEvent(this, WorkerStateEvent.WORKER_STATE_CANCELLED));
                    cancelled();
                    return;
                case FAILED:
                    fireEvent(new WorkerStateEvent(this, WorkerStateEvent.WORKER_STATE_FAILED));
                    failed();
                    return;
                case READY:
                    fireEvent(new WorkerStateEvent(this, WorkerStateEvent.WORKER_STATE_READY));
                    ready();
                    return;
                case RUNNING:
                    fireEvent(new WorkerStateEvent(this, WorkerStateEvent.WORKER_STATE_RUNNING));
                    running();
                    return;
                case SCHEDULED:
                    fireEvent(new WorkerStateEvent(this, WorkerStateEvent.WORKER_STATE_SCHEDULED));
                    scheduled();
                    return;
                case SUCCEEDED:
                    fireEvent(new WorkerStateEvent(this, WorkerStateEvent.WORKER_STATE_SUCCEEDED));
                    succeeded();
                    return;
                default:
                    throw new AssertionError((Object) "Should be unreachable");
            }
        });
    }

    @Override // javafx.concurrent.Worker
    public boolean cancel() {
        checkThread();
        if (this.task == null) {
            if (this.state.get() == Worker.State.CANCELLED || this.state.get() == Worker.State.SUCCEEDED) {
                return false;
            }
            this.state.set(Worker.State.CANCELLED);
            return true;
        }
        return this.task.cancel(true);
    }

    public void restart() {
        checkThread();
        if (this.task != null) {
            this.task.cancel();
            this.task = null;
            this.state.unbind();
            this.state.set(Worker.State.CANCELLED);
        }
        reset();
        start();
    }

    public void reset() {
        checkThread();
        Worker.State s2 = getState();
        if (s2 == Worker.State.SCHEDULED || s2 == Worker.State.RUNNING) {
            throw new IllegalStateException();
        }
        this.task = null;
        this.state.unbind();
        this.state.set(Worker.State.READY);
        this.value.unbind();
        this.value.set(null);
        this.exception.unbind();
        this.exception.set(null);
        this.workDone.unbind();
        this.workDone.set(-1.0d);
        this.totalWorkToBeDone.unbind();
        this.totalWorkToBeDone.set(-1.0d);
        this.progress.unbind();
        this.progress.set(-1.0d);
        this.running.unbind();
        this.running.set(false);
        this.message.unbind();
        this.message.set("");
        this.title.unbind();
        this.title.set("");
    }

    public void start() {
        checkThread();
        if (getState() != Worker.State.READY) {
            throw new IllegalStateException("Can only start a Service in the READY state. Was in state " + ((Object) getState()));
        }
        this.task = createTask();
        this.state.bind(this.task.stateProperty());
        this.value.bind(this.task.valueProperty());
        this.exception.bind(this.task.exceptionProperty());
        this.workDone.bind(this.task.workDoneProperty());
        this.totalWorkToBeDone.bind(this.task.totalWorkProperty());
        this.progress.bind(this.task.progressProperty());
        this.running.bind(this.task.runningProperty());
        this.message.bind(this.task.messageProperty());
        this.title.bind(this.task.titleProperty());
        this.startedOnce = true;
        if (!isFxApplicationThread()) {
            runLater(() -> {
                this.task.setState(Worker.State.SCHEDULED);
                executeTask(this.task);
            });
        } else {
            this.task.setState(Worker.State.SCHEDULED);
            executeTask(this.task);
        }
    }

    void cancelFromReadyState() {
        this.state.set(Worker.State.SCHEDULED);
        this.state.set(Worker.State.CANCELLED);
    }

    protected void executeTask(Task<V> task) {
        AccessControlContext acc = AccessController.getContext();
        Executor e2 = getExecutor() != null ? getExecutor() : EXECUTOR;
        e2.execute(() -> {
            AccessController.doPrivileged(() -> {
                task.run();
                return null;
            }, acc);
        });
    }

    private EventHelper getEventHelper() {
        if (this.eventHelper == null) {
            this.eventHelper = new EventHelper(this);
        }
        return this.eventHelper;
    }

    public final <T extends Event> void addEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        checkThread();
        getEventHelper().addEventHandler(eventType, eventHandler);
    }

    public final <T extends Event> void removeEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        checkThread();
        getEventHelper().removeEventHandler(eventType, eventHandler);
    }

    public final <T extends Event> void addEventFilter(EventType<T> eventType, EventHandler<? super T> eventFilter) {
        checkThread();
        getEventHelper().addEventFilter(eventType, eventFilter);
    }

    public final <T extends Event> void removeEventFilter(EventType<T> eventType, EventHandler<? super T> eventFilter) {
        checkThread();
        getEventHelper().removeEventFilter(eventType, eventFilter);
    }

    protected final <T extends Event> void setEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        checkThread();
        getEventHelper().setEventHandler(eventType, eventHandler);
    }

    protected final void fireEvent(Event event) {
        checkThread();
        getEventHelper().fireEvent(event);
    }

    @Override // javafx.event.EventTarget
    public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
        checkThread();
        return getEventHelper().buildEventDispatchChain(tail);
    }

    void checkThread() {
        if (this.startedOnce && !isFxApplicationThread()) {
            throw new IllegalStateException("Service must only be used from the FX Application Thread");
        }
    }

    void runLater(Runnable r2) {
        Platform.runLater(r2);
    }

    boolean isFxApplicationThread() {
        return Platform.isFxApplicationThread();
    }
}
