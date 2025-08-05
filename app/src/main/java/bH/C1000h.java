package bH;

import java.util.Map;

/* renamed from: bH.h, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bH/h.class */
public class C1000h {

    /* renamed from: d, reason: collision with root package name */
    private static C1000h f7045d = null;

    /* renamed from: a, reason: collision with root package name */
    boolean f7046a = false;

    /* renamed from: b, reason: collision with root package name */
    Runnable f7047b = new RunnableC1001i(this);

    /* renamed from: c, reason: collision with root package name */
    C1002j f7048c;

    private C1000h() {
    }

    public void a() {
        if (this.f7048c != null) {
            this.f7048c.a();
        }
        this.f7048c = new C1002j(this);
        this.f7048c.start();
    }

    public static C1000h b() {
        if (f7045d == null) {
            f7045d = new C1000h();
        }
        return f7045d;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        C.c("Swing Event Thread not heard from in 10 seconds, assuming dead lock. Printing all stacks");
        Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
        for (Thread thread : allStackTraces.keySet()) {
            System.err.println("Thread " + thread.getName());
            for (StackTraceElement stackTraceElement : allStackTraces.get(thread)) {
                System.err.println("\tat " + ((Object) stackTraceElement));
            }
        }
    }
}
