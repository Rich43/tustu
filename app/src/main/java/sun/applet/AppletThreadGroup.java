package sun.applet;

/* loaded from: rt.jar:sun/applet/AppletThreadGroup.class */
public class AppletThreadGroup extends ThreadGroup {
    public AppletThreadGroup(String str) {
        this(Thread.currentThread().getThreadGroup(), str);
    }

    public AppletThreadGroup(ThreadGroup threadGroup, String str) {
        super(threadGroup, str);
        setMaxPriority(4);
    }
}
