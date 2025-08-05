package javax.smartcardio;

/* loaded from: rt.jar:javax/smartcardio/CardTerminal.class */
public abstract class CardTerminal {
    public abstract String getName();

    public abstract Card connect(String str) throws CardException;

    public abstract boolean isCardPresent() throws CardException;

    public abstract boolean waitForCardPresent(long j2) throws CardException;

    public abstract boolean waitForCardAbsent(long j2) throws CardException;

    protected CardTerminal() {
    }
}
