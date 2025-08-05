package javax.smartcardio;

/* loaded from: rt.jar:javax/smartcardio/TerminalFactorySpi.class */
public abstract class TerminalFactorySpi {
    protected abstract CardTerminals engineTerminals();

    protected TerminalFactorySpi() {
    }
}
