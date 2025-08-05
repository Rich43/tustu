package javax.smartcardio;

import java.util.List;

/* loaded from: rt.jar:javax/smartcardio/CardTerminals.class */
public abstract class CardTerminals {

    /* loaded from: rt.jar:javax/smartcardio/CardTerminals$State.class */
    public enum State {
        ALL,
        CARD_PRESENT,
        CARD_ABSENT,
        CARD_INSERTION,
        CARD_REMOVAL
    }

    public abstract List<CardTerminal> list(State state) throws CardException;

    public abstract boolean waitForChange(long j2) throws CardException;

    protected CardTerminals() {
    }

    public List<CardTerminal> list() throws CardException {
        return list(State.ALL);
    }

    public CardTerminal getTerminal(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        try {
            for (CardTerminal cardTerminal : list()) {
                if (cardTerminal.getName().equals(str)) {
                    return cardTerminal;
                }
            }
            return null;
        } catch (CardException e2) {
            return null;
        }
    }

    public void waitForChange() throws CardException {
        waitForChange(0L);
    }
}
