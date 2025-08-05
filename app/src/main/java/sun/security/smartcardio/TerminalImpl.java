package sun.security.smartcardio;

import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.CardNotPresentException;
import javax.smartcardio.CardPermission;
import javax.smartcardio.CardTerminal;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:sun/security/smartcardio/TerminalImpl.class */
final class TerminalImpl extends CardTerminal {
    final long contextId;
    final String name;
    private CardImpl card;

    TerminalImpl(long j2, String str) {
        this.contextId = j2;
        this.name = str;
    }

    @Override // javax.smartcardio.CardTerminal
    public String getName() {
        return this.name;
    }

    @Override // javax.smartcardio.CardTerminal
    public synchronized Card connect(String str) throws CardException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new CardPermission(this.name, SecurityConstants.SOCKET_CONNECT_ACTION));
        }
        if (this.card != null) {
            if (this.card.isValid()) {
                String protocol = this.card.getProtocol();
                if (str.equals("*") || str.equalsIgnoreCase(protocol)) {
                    return this.card;
                }
                throw new CardException("Cannot connect using " + str + ", connection already established using " + protocol);
            }
            this.card = null;
        }
        try {
            this.card = new CardImpl(this, str);
            return this.card;
        } catch (PCSCException e2) {
            if (e2.code == -2146434967 || e2.code == -2146435060) {
                throw new CardNotPresentException("No card present", e2);
            }
            throw new CardException("connect() failed", e2);
        }
    }

    @Override // javax.smartcardio.CardTerminal
    public boolean isCardPresent() throws CardException {
        try {
            return (PCSC.SCardGetStatusChange(this.contextId, 0L, new int[]{0}, new String[]{this.name})[0] & 32) != 0;
        } catch (PCSCException e2) {
            throw new CardException("isCardPresent() failed", e2);
        }
    }

    private boolean waitForCard(boolean z2, long j2) throws CardException {
        if (j2 < 0) {
            throw new IllegalArgumentException("timeout must not be negative");
        }
        if (j2 == 0) {
            j2 = -1;
        }
        int[] iArr = {0};
        String[] strArr = {this.name};
        try {
            int[] iArrSCardGetStatusChange = PCSC.SCardGetStatusChange(this.contextId, 0L, iArr, strArr);
            boolean z3 = (iArrSCardGetStatusChange[0] & 32) != 0;
            if (z2 == z3) {
                return true;
            }
            long jCurrentTimeMillis = System.currentTimeMillis() + j2;
            while (z2 != z3 && j2 != 0) {
                if (j2 != -1) {
                    j2 = Math.max(jCurrentTimeMillis - System.currentTimeMillis(), 0L);
                }
                iArrSCardGetStatusChange = PCSC.SCardGetStatusChange(this.contextId, j2, iArrSCardGetStatusChange, strArr);
                z3 = (iArrSCardGetStatusChange[0] & 32) != 0;
            }
            return z2 == z3;
        } catch (PCSCException e2) {
            if (e2.code == -2146435062) {
                return false;
            }
            throw new CardException("waitForCard() failed", e2);
        }
    }

    @Override // javax.smartcardio.CardTerminal
    public boolean waitForCardPresent(long j2) throws CardException {
        return waitForCard(true, j2);
    }

    @Override // javax.smartcardio.CardTerminal
    public boolean waitForCardAbsent(long j2) throws CardException {
        return waitForCard(false, j2);
    }

    public String toString() {
        return "PC/SC terminal " + this.name;
    }
}
