package javax.smartcardio;

/* loaded from: rt.jar:javax/smartcardio/Card.class */
public abstract class Card {
    public abstract ATR getATR();

    public abstract String getProtocol();

    public abstract CardChannel getBasicChannel();

    public abstract CardChannel openLogicalChannel() throws CardException;

    public abstract void beginExclusive() throws CardException;

    public abstract void endExclusive() throws CardException;

    public abstract byte[] transmitControlCommand(int i2, byte[] bArr) throws CardException;

    public abstract void disconnect(boolean z2) throws CardException;

    protected Card() {
    }
}
