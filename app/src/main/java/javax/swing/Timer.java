package javax.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.EventListener;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.TimerQueue;
import javax.swing.event.EventListenerList;

/* loaded from: rt.jar:javax/swing/Timer.class */
public class Timer implements Serializable {
    private volatile int initialDelay;
    private volatile int delay;
    private static volatile boolean logTimers;
    private volatile String actionCommand;
    protected EventListenerList listenerList = new EventListenerList();
    private final transient AtomicBoolean notify = new AtomicBoolean(false);
    private volatile boolean repeats = true;
    private volatile boolean coalesce = true;
    private final transient Lock lock = new ReentrantLock();
    transient TimerQueue.DelayedTimer delayedTimer = null;
    private volatile transient AccessControlContext acc = AccessController.getContext();
    private final transient Runnable doPostEvent = new DoPostEvent();

    public Timer(int i2, ActionListener actionListener) {
        this.delay = i2;
        this.initialDelay = i2;
        if (actionListener != null) {
            addActionListener(actionListener);
        }
    }

    final AccessControlContext getAccessControlContext() {
        if (this.acc == null) {
            throw new SecurityException("Timer is missing AccessControlContext");
        }
        return this.acc;
    }

    /* loaded from: rt.jar:javax/swing/Timer$DoPostEvent.class */
    class DoPostEvent implements Runnable {
        DoPostEvent() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (Timer.logTimers) {
                System.out.println("Timer ringing: " + ((Object) Timer.this));
            }
            if (Timer.this.notify.get()) {
                Timer.this.fireActionPerformed(new ActionEvent(Timer.this, 0, Timer.this.getActionCommand(), System.currentTimeMillis(), 0));
                if (Timer.this.coalesce) {
                    Timer.this.cancelEvent();
                }
            }
        }

        Timer getTimer() {
            return Timer.this;
        }
    }

    public void addActionListener(ActionListener actionListener) {
        this.listenerList.add(ActionListener.class, actionListener);
    }

    public void removeActionListener(ActionListener actionListener) {
        this.listenerList.remove(ActionListener.class, actionListener);
    }

    public ActionListener[] getActionListeners() {
        return (ActionListener[]) this.listenerList.getListeners(ActionListener.class);
    }

    protected void fireActionPerformed(ActionEvent actionEvent) {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == ActionListener.class) {
                ((ActionListener) listenerList[length + 1]).actionPerformed(actionEvent);
            }
        }
    }

    public <T extends EventListener> T[] getListeners(Class<T> cls) {
        return (T[]) this.listenerList.getListeners(cls);
    }

    private TimerQueue timerQueue() {
        return TimerQueue.sharedInstance();
    }

    public static void setLogTimers(boolean z2) {
        logTimers = z2;
    }

    public static boolean getLogTimers() {
        return logTimers;
    }

    public void setDelay(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("Invalid delay: " + i2);
        }
        this.delay = i2;
    }

    public int getDelay() {
        return this.delay;
    }

    public void setInitialDelay(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("Invalid initial delay: " + i2);
        }
        this.initialDelay = i2;
    }

    public int getInitialDelay() {
        return this.initialDelay;
    }

    public void setRepeats(boolean z2) {
        this.repeats = z2;
    }

    public boolean isRepeats() {
        return this.repeats;
    }

    public void setCoalesce(boolean z2) {
        boolean z3 = this.coalesce;
        this.coalesce = z2;
        if (!z3 && this.coalesce) {
            cancelEvent();
        }
    }

    public boolean isCoalesce() {
        return this.coalesce;
    }

    public void setActionCommand(String str) {
        this.actionCommand = str;
    }

    public String getActionCommand() {
        return this.actionCommand;
    }

    public void start() {
        timerQueue().addTimer(this, getInitialDelay());
    }

    public boolean isRunning() {
        return timerQueue().containsTimer(this);
    }

    public void stop() {
        getLock().lock();
        try {
            cancelEvent();
            timerQueue().removeTimer(this);
        } finally {
            getLock().unlock();
        }
    }

    public void restart() {
        getLock().lock();
        try {
            stop();
            start();
        } finally {
            getLock().unlock();
        }
    }

    void cancelEvent() {
        this.notify.set(false);
    }

    void post() {
        if (this.notify.compareAndSet(false, true) || !this.coalesce) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: javax.swing.Timer.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    SwingUtilities.invokeLater(Timer.this.doPostEvent);
                    return null;
                }
            }, getAccessControlContext());
        }
    }

    Lock getLock() {
        return this.lock;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.acc = AccessController.getContext();
        objectInputStream.defaultReadObject();
    }

    private Object readResolve() {
        Timer timer = new Timer(getDelay(), null);
        timer.listenerList = this.listenerList;
        timer.initialDelay = this.initialDelay;
        timer.delay = this.delay;
        timer.repeats = this.repeats;
        timer.coalesce = this.coalesce;
        timer.actionCommand = this.actionCommand;
        return timer;
    }
}
