package javax.smartcardio;

/* loaded from: rt.jar:javax/smartcardio/CardNotPresentException.class */
public class CardNotPresentException extends CardException {
    private static final long serialVersionUID = 1346879911706545215L;

    public CardNotPresentException(String str) {
        super(str);
    }

    public CardNotPresentException(Throwable th) {
        super(th);
    }

    public CardNotPresentException(String str, Throwable th) {
        super(str, th);
    }
}
