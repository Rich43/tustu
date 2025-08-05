package sun.misc;

import java.lang.ref.SoftReference;

@Deprecated
/* loaded from: rt.jar:sun/misc/Ref.class */
public abstract class Ref {
    private SoftReference soft = null;

    public abstract Object reconstitute();

    public synchronized Object get() {
        Object objCheck = check();
        if (objCheck == null) {
            objCheck = reconstitute();
            setThing(objCheck);
        }
        return objCheck;
    }

    public synchronized void flush() {
        SoftReference softReference = this.soft;
        if (softReference != null) {
            softReference.clear();
        }
        this.soft = null;
    }

    public synchronized void setThing(Object obj) {
        flush();
        this.soft = new SoftReference(obj);
    }

    public synchronized Object check() {
        SoftReference softReference = this.soft;
        if (softReference == null) {
            return null;
        }
        return softReference.get();
    }

    public Ref() {
    }

    public Ref(Object obj) {
        setThing(obj);
    }
}
