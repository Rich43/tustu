package javax.print;

/* loaded from: rt.jar:javax/print/PrintException.class */
public class PrintException extends Exception {
    public PrintException() {
    }

    public PrintException(String str) {
        super(str);
    }

    public PrintException(Exception exc) {
        super(exc);
    }

    public PrintException(String str, Exception exc) {
        super(str, exc);
    }
}
