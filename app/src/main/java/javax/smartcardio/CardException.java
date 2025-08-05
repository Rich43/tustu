package javax.smartcardio;

/* loaded from: rt.jar:javax/smartcardio/CardException.class */
public class CardException extends Exception {
    private static final long serialVersionUID = 7787607144922050628L;

    public CardException(String str) {
        super(str);
    }

    public CardException(Throwable th) {
        super(th);
    }

    public CardException(String str, Throwable th) {
        super(str, th);
    }
}
