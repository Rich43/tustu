package java.rmi.server;

/* loaded from: rt.jar:java/rmi/server/ServerCloneException.class */
public class ServerCloneException extends CloneNotSupportedException {
    public Exception detail;
    private static final long serialVersionUID = 6617456357664815945L;

    public ServerCloneException(String str) {
        super(str);
        initCause(null);
    }

    public ServerCloneException(String str, Exception exc) {
        super(str);
        initCause(null);
        this.detail = exc;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        if (this.detail == null) {
            return super.getMessage();
        }
        return super.getMessage() + "; nested exception is: \n\t" + this.detail.toString();
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.detail;
    }
}
