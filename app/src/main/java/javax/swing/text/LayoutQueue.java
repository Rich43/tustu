package javax.swing.text;

import java.util.Vector;
import sun.awt.AppContext;

/* loaded from: rt.jar:javax/swing/text/LayoutQueue.class */
public class LayoutQueue {
    private static final Object DEFAULT_QUEUE = new Object();
    private Vector<Runnable> tasks = new Vector<>();
    private Thread worker;

    public static LayoutQueue getDefaultQueue() {
        LayoutQueue layoutQueue;
        AppContext appContext = AppContext.getAppContext();
        synchronized (DEFAULT_QUEUE) {
            LayoutQueue layoutQueue2 = (LayoutQueue) appContext.get(DEFAULT_QUEUE);
            if (layoutQueue2 == null) {
                layoutQueue2 = new LayoutQueue();
                appContext.put(DEFAULT_QUEUE, layoutQueue2);
            }
            layoutQueue = layoutQueue2;
        }
        return layoutQueue;
    }

    public static void setDefaultQueue(LayoutQueue layoutQueue) {
        synchronized (DEFAULT_QUEUE) {
            AppContext.getAppContext().put(DEFAULT_QUEUE, layoutQueue);
        }
    }

    public synchronized void addTask(Runnable runnable) {
        if (this.worker == null) {
            this.worker = new LayoutThread();
            this.worker.start();
        }
        this.tasks.addElement(runnable);
        notifyAll();
    }

    protected synchronized Runnable waitForWork() {
        while (this.tasks.size() == 0) {
            try {
                wait();
            } catch (InterruptedException e2) {
                return null;
            }
        }
        Runnable runnableFirstElement = this.tasks.firstElement();
        this.tasks.removeElementAt(0);
        return runnableFirstElement;
    }

    /* loaded from: rt.jar:javax/swing/text/LayoutQueue$LayoutThread.class */
    class LayoutThread extends Thread {
        LayoutThread() {
            super("text-layout");
            setPriority(1);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            Runnable runnableWaitForWork;
            do {
                runnableWaitForWork = LayoutQueue.this.waitForWork();
                if (runnableWaitForWork != null) {
                    runnableWaitForWork.run();
                }
            } while (runnableWaitForWork != null);
        }
    }
}
