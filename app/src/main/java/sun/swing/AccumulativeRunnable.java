package sun.swing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.SwingUtilities;

/* loaded from: rt.jar:sun/swing/AccumulativeRunnable.class */
public abstract class AccumulativeRunnable<T> implements Runnable {
    private List<T> arguments = null;

    protected abstract void run(List<T> list);

    @Override // java.lang.Runnable
    public final void run() {
        run(flush());
    }

    @SafeVarargs
    public final synchronized void add(T... tArr) {
        boolean z2 = true;
        if (this.arguments == null) {
            z2 = false;
            this.arguments = new ArrayList();
        }
        Collections.addAll(this.arguments, tArr);
        if (!z2) {
            submit();
        }
    }

    protected void submit() {
        SwingUtilities.invokeLater(this);
    }

    private final synchronized List<T> flush() {
        List<T> list = this.arguments;
        this.arguments = null;
        return list;
    }
}
