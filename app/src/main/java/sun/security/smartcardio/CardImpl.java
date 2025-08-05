package sun.security.smartcardio;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.security.AccessController;
import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardPermission;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/security/smartcardio/CardImpl.class */
final class CardImpl extends Card {
    private final TerminalImpl terminal;
    final long cardId;
    private final ATR atr;
    final int protocol;
    private final ChannelImpl basicChannel;
    private volatile State state;
    private volatile Thread exclusiveThread;
    private static final boolean isWindows = ((String) AccessController.doPrivileged(() -> {
        return System.getProperty("os.name");
    })).startsWith("Windows");
    private static byte[] commandOpenChannel = {0, 112, 0, 0, 1};
    private static final boolean invertReset = Boolean.parseBoolean((String) AccessController.doPrivileged(new GetPropertyAction("sun.security.smartcardio.invertCardReset", "false")));

    /* loaded from: rt.jar:sun/security/smartcardio/CardImpl$State.class */
    private enum State {
        OK,
        REMOVED,
        DISCONNECTED
    }

    CardImpl(TerminalImpl terminalImpl, String str) throws PCSCException {
        int i2;
        this.terminal = terminalImpl;
        int i3 = 2;
        if (str.equals("*")) {
            i2 = 3;
        } else if (str.equalsIgnoreCase("T=0")) {
            i2 = 1;
        } else if (str.equalsIgnoreCase("T=1")) {
            i2 = 2;
        } else if (str.equalsIgnoreCase("direct")) {
            i2 = isWindows ? 0 : 65536;
            i3 = 3;
        } else {
            throw new IllegalArgumentException("Unsupported protocol " + str);
        }
        this.cardId = PCSC.SCardConnect(terminalImpl.contextId, terminalImpl.name, i3, i2);
        byte[] bArr = new byte[2];
        this.atr = new ATR(PCSC.SCardStatus(this.cardId, bArr));
        this.protocol = bArr[1] & 255;
        this.basicChannel = new ChannelImpl(this, 0);
        this.state = State.OK;
    }

    void checkState() {
        State state = this.state;
        if (state == State.DISCONNECTED) {
            throw new IllegalStateException("Card has been disconnected");
        }
        if (state == State.REMOVED) {
            throw new IllegalStateException("Card has been removed");
        }
    }

    boolean isValid() {
        if (this.state != State.OK) {
            return false;
        }
        try {
            PCSC.SCardStatus(this.cardId, new byte[2]);
            return true;
        } catch (PCSCException e2) {
            this.state = State.REMOVED;
            return false;
        }
    }

    private void checkSecurity(String str) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new CardPermission(this.terminal.name, str));
        }
    }

    void handleError(PCSCException pCSCException) {
        if (pCSCException.code == -2146434967) {
            this.state = State.REMOVED;
        }
    }

    @Override // javax.smartcardio.Card
    public ATR getATR() {
        return this.atr;
    }

    @Override // javax.smartcardio.Card
    public String getProtocol() {
        switch (this.protocol) {
            case 1:
                return "T=0";
            case 2:
                return "T=1";
            default:
                return "Unknown protocol " + this.protocol;
        }
    }

    @Override // javax.smartcardio.Card
    public CardChannel getBasicChannel() {
        checkSecurity("getBasicChannel");
        checkState();
        return this.basicChannel;
    }

    private static int getSW(byte[] bArr) {
        if (bArr.length < 2) {
            return -1;
        }
        int i2 = bArr[bArr.length - 2] & 255;
        return (i2 << 8) | (bArr[bArr.length - 1] & 255);
    }

    @Override // javax.smartcardio.Card
    public CardChannel openLogicalChannel() throws CardException {
        checkSecurity("openLogicalChannel");
        checkState();
        checkExclusive();
        try {
            byte[] bArrSCardTransmit = PCSC.SCardTransmit(this.cardId, this.protocol, commandOpenChannel, 0, commandOpenChannel.length);
            if (bArrSCardTransmit.length != 3 || getSW(bArrSCardTransmit) != 36864) {
                throw new CardException("openLogicalChannel() failed, card response: " + PCSC.toString(bArrSCardTransmit));
            }
            return new ChannelImpl(this, bArrSCardTransmit[0]);
        } catch (PCSCException e2) {
            handleError(e2);
            throw new CardException("openLogicalChannel() failed", e2);
        }
    }

    void checkExclusive() throws CardException {
        Thread thread = this.exclusiveThread;
        if (thread != null && thread != Thread.currentThread()) {
            throw new CardException("Exclusive access established by another Thread");
        }
    }

    @Override // javax.smartcardio.Card
    public synchronized void beginExclusive() throws CardException {
        checkSecurity("exclusive");
        checkState();
        if (this.exclusiveThread != null) {
            throw new CardException("Exclusive access has already been assigned to Thread " + this.exclusiveThread.getName());
        }
        try {
            PCSC.SCardBeginTransaction(this.cardId);
            this.exclusiveThread = Thread.currentThread();
        } catch (PCSCException e2) {
            handleError(e2);
            throw new CardException("beginExclusive() failed", e2);
        }
    }

    @Override // javax.smartcardio.Card
    public synchronized void endExclusive() throws CardException {
        checkState();
        try {
            if (this.exclusiveThread != Thread.currentThread()) {
                throw new IllegalStateException("Exclusive access not assigned to current Thread");
            }
            try {
                PCSC.SCardEndTransaction(this.cardId, 0);
                this.exclusiveThread = null;
            } catch (PCSCException e2) {
                handleError(e2);
                throw new CardException("endExclusive() failed", e2);
            }
        } catch (Throwable th) {
            this.exclusiveThread = null;
            throw th;
        }
    }

    @Override // javax.smartcardio.Card
    public byte[] transmitControlCommand(int i2, byte[] bArr) throws CardException {
        checkSecurity("transmitControl");
        checkState();
        checkExclusive();
        if (bArr == null) {
            throw new NullPointerException();
        }
        try {
            return PCSC.SCardControl(this.cardId, i2, bArr);
        } catch (PCSCException e2) {
            handleError(e2);
            throw new CardException("transmitControlCommand() failed", e2);
        }
    }

    @Override // javax.smartcardio.Card
    public void disconnect(boolean z2) throws CardException {
        if (z2) {
            checkSecurity(Constants.RESET);
        }
        if (this.state != State.OK) {
            return;
        }
        checkExclusive();
        if (invertReset) {
            z2 = !z2;
        }
        try {
            try {
                PCSC.SCardDisconnect(this.cardId, z2 ? 1 : 0);
                this.state = State.DISCONNECTED;
                this.exclusiveThread = null;
            } catch (PCSCException e2) {
                throw new CardException("disconnect() failed", e2);
            }
        } catch (Throwable th) {
            this.state = State.DISCONNECTED;
            this.exclusiveThread = null;
            throw th;
        }
    }

    public String toString() {
        return "PC/SC card in " + this.terminal.name + ", protocol " + getProtocol() + ", state " + ((Object) this.state);
    }

    protected void finalize() throws Throwable {
        try {
            if (this.state == State.OK) {
                this.state = State.DISCONNECTED;
                PCSC.SCardDisconnect(this.cardId, 0);
            }
        } finally {
            super.finalize();
        }
    }
}
