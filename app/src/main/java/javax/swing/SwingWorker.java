package javax.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import sun.awt.AppContext;
import sun.swing.AccumulativeRunnable;

/* loaded from: rt.jar:javax/swing/SwingWorker.class */
public abstract class SwingWorker<T, V> implements RunnableFuture<T> {
    private static final int MAX_WORKER_THREADS = 10;
    private volatile int progress;
    private static final Object DO_SUBMIT_KEY = new StringBuilder("doSubmit");
    private final AccumulativeRunnable<Runnable> doSubmit = getDoSubmit();
    private final FutureTask<T> future = new FutureTask<T>(new Callable<T>() { // from class: javax.swing.SwingWorker.1
        @Override // java.util.concurrent.Callable
        public T call() throws Exception {
            SwingWorker.this.setState(StateValue.STARTED);
            return (T) SwingWorker.this.doInBackground();
        }
    }) { // from class: javax.swing.SwingWorker.2
        @Override // java.util.concurrent.FutureTask
        protected void done() {
            SwingWorker.this.doneEDT();
            SwingWorker.this.setState(StateValue.DONE);
        }
    };
    private volatile StateValue state = StateValue.PENDING;
    private final PropertyChangeSupport propertyChangeSupport = new SwingWorkerPropertyChangeSupport(this);
    private AccumulativeRunnable<V> doProcess = null;
    private AccumulativeRunnable<Integer> doNotifyProgressChange = null;

    /* loaded from: rt.jar:javax/swing/SwingWorker$StateValue.class */
    public enum StateValue {
        PENDING,
        STARTED,
        DONE
    }

    protected abstract T doInBackground() throws Exception;

    @Override // java.util.concurrent.RunnableFuture, java.lang.Runnable
    public final void run() {
        this.future.run();
    }

    @SafeVarargs
    protected final void publish(V... vArr) {
        synchronized (this) {
            if (this.doProcess == null) {
                this.doProcess = new AccumulativeRunnable<V>() { // from class: javax.swing.SwingWorker.3
                    @Override // sun.swing.AccumulativeRunnable
                    public void run(List<V> list) {
                        SwingWorker.this.process(list);
                    }

                    @Override // sun.swing.AccumulativeRunnable
                    protected void submit() {
                        SwingWorker.this.doSubmit.add(this);
                    }
                };
            }
        }
        this.doProcess.add(vArr);
    }

    protected void process(List<V> list) {
    }

    protected void done() {
    }

    protected final void setProgress(int i2) {
        if (i2 < 0 || i2 > 100) {
            throw new IllegalArgumentException("the value should be from 0 to 100");
        }
        if (this.progress == i2) {
            return;
        }
        int i3 = this.progress;
        this.progress = i2;
        if (!getPropertyChangeSupport().hasListeners("progress")) {
            return;
        }
        synchronized (this) {
            if (this.doNotifyProgressChange == null) {
                this.doNotifyProgressChange = new AccumulativeRunnable<Integer>() { // from class: javax.swing.SwingWorker.4
                    @Override // sun.swing.AccumulativeRunnable
                    public void run(List<Integer> list) {
                        SwingWorker.this.firePropertyChange("progress", list.get(0), list.get(list.size() - 1));
                    }

                    @Override // sun.swing.AccumulativeRunnable
                    protected void submit() {
                        SwingWorker.this.doSubmit.add(this);
                    }
                };
            }
        }
        this.doNotifyProgressChange.add(Integer.valueOf(i3), Integer.valueOf(i2));
    }

    public final int getProgress() {
        return this.progress;
    }

    public final void execute() {
        getWorkersExecutorService().execute(this);
    }

    @Override // java.util.concurrent.Future
    public final boolean cancel(boolean z2) {
        return this.future.cancel(z2);
    }

    @Override // java.util.concurrent.Future
    public final boolean isCancelled() {
        return this.future.isCancelled();
    }

    @Override // java.util.concurrent.Future
    public final boolean isDone() {
        return this.future.isDone();
    }

    @Override // java.util.concurrent.Future
    public final T get() throws ExecutionException, InterruptedException {
        return this.future.get();
    }

    @Override // java.util.concurrent.Future
    public final T get(long j2, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        return this.future.get(j2, timeUnit);
    }

    public final void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        getPropertyChangeSupport().addPropertyChangeListener(propertyChangeListener);
    }

    public final void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        getPropertyChangeSupport().removePropertyChangeListener(propertyChangeListener);
    }

    public final void firePropertyChange(String str, Object obj, Object obj2) {
        getPropertyChangeSupport().firePropertyChange(str, obj, obj2);
    }

    public final PropertyChangeSupport getPropertyChangeSupport() {
        return this.propertyChangeSupport;
    }

    public final StateValue getState() {
        if (isDone()) {
            return StateValue.DONE;
        }
        return this.state;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setState(StateValue stateValue) {
        StateValue stateValue2 = this.state;
        this.state = stateValue;
        firePropertyChange("state", stateValue2, stateValue);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doneEDT() {
        Runnable runnable = new Runnable() { // from class: javax.swing.SwingWorker.5
            @Override // java.lang.Runnable
            public void run() {
                SwingWorker.this.done();
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        } else {
            this.doSubmit.add(runnable);
        }
    }

    private static synchronized ExecutorService getWorkersExecutorService() {
        AppContext appContext = AppContext.getAppContext();
        final ExecutorService threadPoolExecutor = (ExecutorService) appContext.get(SwingWorker.class);
        if (threadPoolExecutor == null) {
            threadPoolExecutor = new ThreadPoolExecutor(10, 10, 10L, TimeUnit.MINUTES, new LinkedBlockingQueue(), new ThreadFactory() { // from class: javax.swing.SwingWorker.6
                final ThreadFactory defaultFactory = Executors.defaultThreadFactory();

                @Override // java.util.concurrent.ThreadFactory
                public Thread newThread(Runnable runnable) {
                    Thread threadNewThread = this.defaultFactory.newThread(runnable);
                    threadNewThread.setName("SwingWorker-" + threadNewThread.getName());
                    threadNewThread.setDaemon(true);
                    return threadNewThread;
                }
            });
            appContext.put(SwingWorker.class, threadPoolExecutor);
            appContext.addPropertyChangeListener(AppContext.DISPOSED_PROPERTY_NAME, new PropertyChangeListener() { // from class: javax.swing.SwingWorker.7
                @Override // java.beans.PropertyChangeListener
                public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                    final ExecutorService executorService;
                    if (((Boolean) propertyChangeEvent.getNewValue()).booleanValue() && (executorService = (ExecutorService) new WeakReference(threadPoolExecutor).get()) != null) {
                        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: javax.swing.SwingWorker.7.1
                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // java.security.PrivilegedAction
                            /* renamed from: run */
                            public Void run2() {
                                executorService.shutdown();
                                return null;
                            }
                        });
                    }
                }
            });
        }
        return threadPoolExecutor;
    }

    private static AccumulativeRunnable<Runnable> getDoSubmit() {
        AccumulativeRunnable<Runnable> accumulativeRunnable;
        synchronized (DO_SUBMIT_KEY) {
            AppContext appContext = AppContext.getAppContext();
            Object doSubmitAccumulativeRunnable = appContext.get(DO_SUBMIT_KEY);
            if (doSubmitAccumulativeRunnable == null) {
                doSubmitAccumulativeRunnable = new DoSubmitAccumulativeRunnable();
                appContext.put(DO_SUBMIT_KEY, doSubmitAccumulativeRunnable);
            }
            accumulativeRunnable = (AccumulativeRunnable) doSubmitAccumulativeRunnable;
        }
        return accumulativeRunnable;
    }

    /* loaded from: rt.jar:javax/swing/SwingWorker$DoSubmitAccumulativeRunnable.class */
    private static class DoSubmitAccumulativeRunnable extends AccumulativeRunnable<Runnable> implements ActionListener {
        private static final int DELAY = 33;

        private DoSubmitAccumulativeRunnable() {
        }

        @Override // sun.swing.AccumulativeRunnable
        protected void run(List<Runnable> list) {
            Iterator<Runnable> it = list.iterator();
            while (it.hasNext()) {
                it.next().run();
            }
        }

        @Override // sun.swing.AccumulativeRunnable
        protected void submit() {
            Timer timer = new Timer(33, this);
            timer.setRepeats(false);
            timer.start();
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            run();
        }
    }

    /* loaded from: rt.jar:javax/swing/SwingWorker$SwingWorkerPropertyChangeSupport.class */
    private class SwingWorkerPropertyChangeSupport extends PropertyChangeSupport {
        SwingWorkerPropertyChangeSupport(Object obj) {
            super(obj);
        }

        @Override // java.beans.PropertyChangeSupport
        public void firePropertyChange(final PropertyChangeEvent propertyChangeEvent) {
            if (!SwingUtilities.isEventDispatchThread()) {
                SwingWorker.this.doSubmit.add(new Runnable() { // from class: javax.swing.SwingWorker.SwingWorkerPropertyChangeSupport.1
                    @Override // java.lang.Runnable
                    public void run() {
                        SwingWorkerPropertyChangeSupport.this.firePropertyChange(propertyChangeEvent);
                    }
                });
            } else {
                super.firePropertyChange(propertyChangeEvent);
            }
        }
    }
}
