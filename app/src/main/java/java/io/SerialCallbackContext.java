package java.io;

/* loaded from: rt.jar:java/io/SerialCallbackContext.class */
final class SerialCallbackContext {
    private final Object obj;
    private final ObjectStreamClass desc;
    private Thread thread = Thread.currentThread();

    public SerialCallbackContext(Object obj, ObjectStreamClass objectStreamClass) {
        this.obj = obj;
        this.desc = objectStreamClass;
    }

    public Object getObj() throws NotActiveException {
        checkAndSetUsed();
        return this.obj;
    }

    public ObjectStreamClass getDesc() {
        return this.desc;
    }

    public void check() throws NotActiveException {
        if (this.thread != null && this.thread != Thread.currentThread()) {
            throw new NotActiveException("expected thread: " + ((Object) this.thread) + ", but got: " + ((Object) Thread.currentThread()));
        }
    }

    private void checkAndSetUsed() throws NotActiveException {
        if (this.thread != Thread.currentThread()) {
            throw new NotActiveException("not in readObject invocation or fields already read");
        }
        this.thread = null;
    }

    public void setUsed() {
        this.thread = null;
    }
}
