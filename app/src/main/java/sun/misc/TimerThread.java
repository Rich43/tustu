package sun.misc;

/* compiled from: Timer.java */
/* loaded from: rt.jar:sun/misc/TimerThread.class */
class TimerThread extends Thread {
    static TimerThread timerThread;
    public static boolean debug = false;
    static boolean notified = false;
    static Timer timerQueue = null;

    protected TimerThread() {
        super("TimerThread");
        timerThread = this;
        start();
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public synchronized void run() {
        while (true) {
            if (timerQueue == null) {
                try {
                    wait();
                } catch (InterruptedException e2) {
                }
            } else {
                notified = false;
                long jCurrentTimeMillis = timerQueue.sleepUntil - System.currentTimeMillis();
                if (jCurrentTimeMillis > 0) {
                    try {
                        wait(jCurrentTimeMillis);
                    } catch (InterruptedException e3) {
                    }
                }
                if (!notified) {
                    Timer timer = timerQueue;
                    timerQueue = timerQueue.next;
                    TimerTickThread timerTickThreadCall = TimerTickThread.call(timer, timer.sleepUntil);
                    if (debug) {
                        long jCurrentTimeMillis2 = System.currentTimeMillis() - timer.sleepUntil;
                        System.out.println("tick(" + timerTickThreadCall.getName() + "," + timer.interval + "," + jCurrentTimeMillis2 + ")");
                        if (jCurrentTimeMillis2 > 250) {
                            System.out.println("*** BIG DELAY ***");
                        }
                    }
                }
            }
        }
    }

    protected static void enqueue(Timer timer) {
        Timer timer2;
        Timer timer3 = timerQueue;
        if (timer3 == null || timer.sleepUntil <= timer3.sleepUntil) {
            timer.next = timerQueue;
            timerQueue = timer;
            notified = true;
            timerThread.notify();
        } else {
            do {
                timer2 = timer3;
                timer3 = timer3.next;
                if (timer3 == null) {
                    break;
                }
            } while (timer.sleepUntil > timer3.sleepUntil);
            timer.next = timer3;
            timer2.next = timer;
        }
        if (debug) {
            long jCurrentTimeMillis = System.currentTimeMillis();
            System.out.print(Thread.currentThread().getName() + ": enqueue " + timer.interval + ": ");
            Timer timer4 = timerQueue;
            while (true) {
                Timer timer5 = timer4;
                if (timer5 != null) {
                    System.out.print(timer5.interval + "(" + (timer5.sleepUntil - jCurrentTimeMillis) + ") ");
                    timer4 = timer5.next;
                } else {
                    System.out.println();
                    return;
                }
            }
        }
    }

    protected static boolean dequeue(Timer timer) {
        Timer timer2;
        Timer timer3 = null;
        Timer timer4 = timerQueue;
        while (true) {
            timer2 = timer4;
            if (timer2 == null || timer2 == timer) {
                break;
            }
            timer3 = timer2;
            timer4 = timer2.next;
        }
        if (timer2 == null) {
            if (debug) {
                System.out.println(Thread.currentThread().getName() + ": dequeue " + timer.interval + ": no-op");
                return false;
            }
            return false;
        }
        if (timer3 == null) {
            timerQueue = timer.next;
            notified = true;
            timerThread.notify();
        } else {
            timer3.next = timer.next;
        }
        timer.next = null;
        if (debug) {
            long jCurrentTimeMillis = System.currentTimeMillis();
            System.out.print(Thread.currentThread().getName() + ": dequeue " + timer.interval + ": ");
            Timer timer5 = timerQueue;
            while (true) {
                Timer timer6 = timer5;
                if (timer6 != null) {
                    System.out.print(timer6.interval + "(" + (timer6.sleepUntil - jCurrentTimeMillis) + ") ");
                    timer5 = timer6.next;
                } else {
                    System.out.println();
                    return true;
                }
            }
        } else {
            return true;
        }
    }

    protected static void requeue(Timer timer) {
        if (timer.stopped) {
            if (debug) {
                System.out.println(Thread.currentThread().getName() + ": requeue " + timer.interval + ": no-op");
            }
        } else {
            long jCurrentTimeMillis = System.currentTimeMillis();
            if (timer.regular) {
                timer.sleepUntil += timer.interval;
            } else {
                timer.sleepUntil = jCurrentTimeMillis + timer.interval;
            }
            enqueue(timer);
        }
    }
}
