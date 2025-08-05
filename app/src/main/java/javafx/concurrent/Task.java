package javafx.concurrent;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permission;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReference;
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

/* loaded from: jfxrt.jar:javafx/concurrent/Task.class */
public abstract class Task<V> extends FutureTask<V> implements Worker<V>, EventTarget {
    private AtomicReference<ProgressUpdate> progressUpdate;
    private AtomicReference<String> messageUpdate;
    private AtomicReference<String> titleUpdate;
    private AtomicReference<V> valueUpdate;
    private volatile boolean started;
    private ObjectProperty<Worker.State> state;
    private final ObjectProperty<V> value;
    private final ObjectProperty<Throwable> exception;
    private final DoubleProperty workDone;
    private final DoubleProperty totalWork;
    private final DoubleProperty progress;
    private final BooleanProperty running;
    private final StringProperty message;
    private final StringProperty title;
    private static final Permission modifyThreadPerm = new RuntimePermission("modifyThread");
    private EventHelper eventHelper;

    protected abstract V call() throws Exception;

    public Task() {
        this(new TaskCallable());
    }

    private Task(TaskCallable<V> callableAdapter) {
        super(callableAdapter);
        this.progressUpdate = new AtomicReference<>();
        this.messageUpdate = new AtomicReference<>();
        this.titleUpdate = new AtomicReference<>();
        this.valueUpdate = new AtomicReference<>();
        this.started = false;
        this.state = new SimpleObjectProperty(this, "state", Worker.State.READY);
        this.value = new SimpleObjectProperty(this, "value");
        this.exception = new SimpleObjectProperty(this, "exception");
        this.workDone = new SimpleDoubleProperty(this, "workDone", -1.0d);
        this.totalWork = new SimpleDoubleProperty(this, "totalWork", -1.0d);
        this.progress = new SimpleDoubleProperty(this, "progress", -1.0d);
        this.running = new SimpleBooleanProperty(this, "running", false);
        this.message = new SimpleStringProperty(this, "message", "");
        this.title = new SimpleStringProperty(this, "title", "");
        this.eventHelper = null;
        ((TaskCallable) callableAdapter).task = this;
    }

    final void setState(Worker.State value) {
        checkThread();
        Worker.State s2 = getState();
        if (s2 != Worker.State.CANCELLED) {
            this.state.set(value);
            setRunning(value == Worker.State.SCHEDULED || value == Worker.State.RUNNING);
            switch (this.state.get()) {
                case CANCELLED:
                    fireEvent(new WorkerStateEvent(this, WorkerStateEvent.WORKER_STATE_CANCELLED));
                    cancelled();
                    return;
                case FAILED:
                    fireEvent(new WorkerStateEvent(this, WorkerStateEvent.WORKER_STATE_FAILED));
                    failed();
                    return;
                case READY:
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
        }
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

    private void setValue(V v2) {
        checkThread();
        this.value.set(v2);
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

    /* JADX INFO: Access modifiers changed from: private */
    public void _setException(Throwable value) {
        checkThread();
        this.exception.set(value);
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

    private void setWorkDone(double value) {
        checkThread();
        this.workDone.set(value);
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

    private void setTotalWork(double value) {
        checkThread();
        this.totalWork.set(value);
    }

    @Override // javafx.concurrent.Worker
    public final double getTotalWork() {
        checkThread();
        return this.totalWork.get();
    }

    @Override // javafx.concurrent.Worker
    public final ReadOnlyDoubleProperty totalWorkProperty() {
        checkThread();
        return this.totalWork;
    }

    private void setProgress(double value) {
        checkThread();
        this.progress.set(value);
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

    private void setRunning(boolean value) {
        checkThread();
        this.running.set(value);
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

    @Override // javafx.concurrent.Worker
    public final boolean cancel() {
        return cancel(true);
    }

    @Override // java.util.concurrent.FutureTask, java.util.concurrent.Future
    public boolean cancel(boolean mayInterruptIfRunning) {
        boolean flag = ((Boolean) AccessController.doPrivileged(() -> {
            return Boolean.valueOf(super.cancel(mayInterruptIfRunning));
        }, (AccessControlContext) null, modifyThreadPerm)).booleanValue();
        if (flag) {
            if (isFxApplicationThread()) {
                setState(Worker.State.CANCELLED);
            } else {
                runLater(() -> {
                    setState(Worker.State.CANCELLED);
                });
            }
        }
        return flag;
    }

    protected void updateProgress(long workDone, long max) {
        updateProgress(workDone, max);
    }

    protected void updateProgress(double workDone, double max) {
        if (Double.isInfinite(workDone) || Double.isNaN(workDone)) {
            workDone = -1.0d;
        }
        if (Double.isInfinite(max) || Double.isNaN(max)) {
            max = -1.0d;
        }
        if (workDone < 0.0d) {
            workDone = -1.0d;
        }
        if (max < 0.0d) {
            max = -1.0d;
        }
        if (workDone > max) {
            workDone = max;
        }
        if (isFxApplicationThread()) {
            _updateProgress(workDone, max);
        } else if (this.progressUpdate.getAndSet(new ProgressUpdate(workDone, max)) == null) {
            runLater(() -> {
                ProgressUpdate update = this.progressUpdate.getAndSet(null);
                _updateProgress(update.workDone, update.totalWork);
            });
        }
    }

    private void _updateProgress(double workDone, double max) {
        setTotalWork(max);
        setWorkDone(workDone);
        if (workDone == -1.0d) {
            setProgress(-1.0d);
        } else {
            setProgress(workDone / max);
        }
    }

    protected void updateMessage(String message) {
        if (isFxApplicationThread()) {
            this.message.set(message);
        } else if (this.messageUpdate.getAndSet(message) == null) {
            runLater(new Runnable() { // from class: javafx.concurrent.Task.1
                @Override // java.lang.Runnable
                public void run() {
                    String message2 = (String) Task.this.messageUpdate.getAndSet(null);
                    Task.this.message.set(message2);
                }
            });
        }
    }

    protected void updateTitle(String title) {
        if (isFxApplicationThread()) {
            this.title.set(title);
        } else if (this.titleUpdate.getAndSet(title) == null) {
            runLater(new Runnable() { // from class: javafx.concurrent.Task.2
                @Override // java.lang.Runnable
                public void run() {
                    String title2 = (String) Task.this.titleUpdate.getAndSet(null);
                    Task.this.title.set(title2);
                }
            });
        }
    }

    protected void updateValue(V value) {
        if (isFxApplicationThread()) {
            this.value.set(value);
        } else if (this.valueUpdate.getAndSet(value) == null) {
            runLater(() -> {
                this.value.set(this.valueUpdate.getAndSet(null));
            });
        }
    }

    private void checkThread() {
        if (this.started && !isFxApplicationThread()) {
            throw new IllegalStateException("Task must only be used from the FX Application Thread");
        }
    }

    void runLater(Runnable r2) {
        Platform.runLater(r2);
    }

    boolean isFxApplicationThread() {
        return Platform.isFxApplicationThread();
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

    public final void fireEvent(Event event) {
        checkThread();
        getEventHelper().fireEvent(event);
    }

    @Override // javafx.event.EventTarget
    public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
        checkThread();
        return getEventHelper().buildEventDispatchChain(tail);
    }

    /* loaded from: jfxrt.jar:javafx/concurrent/Task$ProgressUpdate.class */
    private static final class ProgressUpdate {
        private final double workDone;
        private final double totalWork;

        private ProgressUpdate(double p2, double m2) {
            this.workDone = p2;
            this.totalWork = m2;
        }
    }

    /* loaded from: jfxrt.jar:javafx/concurrent/Task$TaskCallable.class */
    private static final class TaskCallable<V> implements Callable<V> {
        private Task<V> task;

        private TaskCallable() {
        }

        @Override // java.util.concurrent.Callable
        public V call() throws Exception {
            ((Task) this.task).started = true;
            this.task.runLater(() -> {
                this.task.setState(Worker.State.SCHEDULED);
                this.task.setState(Worker.State.RUNNING);
            });
            try {
                V result = this.task.call();
                if (!this.task.isCancelled()) {
                    this.task.runLater(() -> {
                        this.task.updateValue(result);
                        this.task.setState(Worker.State.SUCCEEDED);
                    });
                    return result;
                }
                return null;
            } catch (Throwable th) {
                this.task.runLater(() -> {
                    this.task._setException(th);
                    this.task.setState(Worker.State.FAILED);
                });
                if (th instanceof Exception) {
                    throw ((Exception) th);
                }
                throw new Exception(th);
            }
        }
    }
}
