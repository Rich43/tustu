package sun.security.smartcardio;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;

/* loaded from: rt.jar:sun/security/smartcardio/PCSCTerminals.class */
final class PCSCTerminals extends CardTerminals {
    private static long contextId;
    private Map<String, ReaderState> stateMap;
    private static final Map<String, Reference<TerminalImpl>> terminals = new HashMap();

    PCSCTerminals() {
    }

    static synchronized void initContext() throws PCSCException {
        if (contextId == 0) {
            contextId = PCSC.SCardEstablishContext(0);
        }
    }

    private static synchronized TerminalImpl implGetTerminal(String str) {
        Reference<TerminalImpl> reference = terminals.get(str);
        TerminalImpl terminalImpl = reference != null ? reference.get() : null;
        if (terminalImpl != null) {
            return terminalImpl;
        }
        TerminalImpl terminalImpl2 = new TerminalImpl(contextId, str);
        terminals.put(str, new WeakReference(terminalImpl2));
        return terminalImpl2;
    }

    @Override // javax.smartcardio.CardTerminals
    public synchronized List<CardTerminal> list(CardTerminals.State state) throws CardException {
        if (state == null) {
            throw new NullPointerException();
        }
        try {
            String[] strArrSCardListReaders = PCSC.SCardListReaders(contextId);
            ArrayList arrayList = new ArrayList(strArrSCardListReaders.length);
            if (this.stateMap == null) {
                if (state == CardTerminals.State.CARD_INSERTION) {
                    state = CardTerminals.State.CARD_PRESENT;
                } else if (state == CardTerminals.State.CARD_REMOVAL) {
                    state = CardTerminals.State.CARD_ABSENT;
                }
            }
            for (String str : strArrSCardListReaders) {
                TerminalImpl terminalImplImplGetTerminal = implGetTerminal(str);
                switch (state) {
                    case ALL:
                        arrayList.add(terminalImplImplGetTerminal);
                        break;
                    case CARD_PRESENT:
                        if (terminalImplImplGetTerminal.isCardPresent()) {
                            arrayList.add(terminalImplImplGetTerminal);
                            break;
                        } else {
                            break;
                        }
                    case CARD_ABSENT:
                        if (terminalImplImplGetTerminal.isCardPresent()) {
                            break;
                        } else {
                            arrayList.add(terminalImplImplGetTerminal);
                            break;
                        }
                    case CARD_INSERTION:
                        ReaderState readerState = this.stateMap.get(str);
                        if (readerState == null || !readerState.isInsertion()) {
                            break;
                        } else {
                            arrayList.add(terminalImplImplGetTerminal);
                            break;
                        }
                    case CARD_REMOVAL:
                        ReaderState readerState2 = this.stateMap.get(str);
                        if (readerState2 == null || !readerState2.isRemoval()) {
                            break;
                        } else {
                            arrayList.add(terminalImplImplGetTerminal);
                            break;
                        }
                        break;
                    default:
                        throw new CardException("Unknown state: " + ((Object) state));
                }
            }
            return Collections.unmodifiableList(arrayList);
        } catch (PCSCException e2) {
            throw new CardException("list() failed", e2);
        }
    }

    /* loaded from: rt.jar:sun/security/smartcardio/PCSCTerminals$ReaderState.class */
    private static class ReaderState {
        private int current = 0;
        private int previous = 0;

        ReaderState() {
        }

        int get() {
            return this.current;
        }

        void update(int i2) {
            this.previous = this.current;
            this.current = i2;
        }

        boolean isInsertion() {
            return !present(this.previous) && present(this.current);
        }

        boolean isRemoval() {
            return present(this.previous) && !present(this.current);
        }

        static boolean present(int i2) {
            return (i2 & 32) != 0;
        }
    }

    @Override // javax.smartcardio.CardTerminals
    public synchronized boolean waitForChange(long j2) throws CardException {
        if (j2 < 0) {
            throw new IllegalArgumentException("Timeout must not be negative: " + j2);
        }
        if (this.stateMap == null) {
            this.stateMap = new HashMap();
            waitForChange(0L);
        }
        if (j2 == 0) {
            j2 = -1;
        }
        try {
            String[] strArrSCardListReaders = PCSC.SCardListReaders(contextId);
            int length = strArrSCardListReaders.length;
            if (length == 0) {
                throw new IllegalStateException("No terminals available");
            }
            int[] iArr = new int[length];
            ReaderState[] readerStateArr = new ReaderState[length];
            for (int i2 = 0; i2 < strArrSCardListReaders.length; i2++) {
                ReaderState readerState = this.stateMap.get(strArrSCardListReaders[i2]);
                if (readerState == null) {
                    readerState = new ReaderState();
                }
                readerStateArr[i2] = readerState;
                iArr[i2] = readerState.get();
            }
            int[] iArrSCardGetStatusChange = PCSC.SCardGetStatusChange(contextId, j2, iArr, strArrSCardListReaders);
            this.stateMap.clear();
            for (int i3 = 0; i3 < length; i3++) {
                ReaderState readerState2 = readerStateArr[i3];
                readerState2.update(iArrSCardGetStatusChange[i3]);
                this.stateMap.put(strArrSCardListReaders[i3], readerState2);
            }
            return true;
        } catch (PCSCException e2) {
            if (e2.code == -2146435062) {
                return false;
            }
            throw new CardException("waitForChange() failed", e2);
        }
    }

    static List<CardTerminal> waitForCards(List<? extends CardTerminal> list, long j2, boolean z2) throws CardException {
        long j3;
        ArrayList arrayList;
        if (j2 == 0) {
            j2 = -1;
            j3 = -1;
        } else {
            j3 = 0;
        }
        String[] strArr = new String[list.size()];
        int i2 = 0;
        for (CardTerminal cardTerminal : list) {
            if (!(cardTerminal instanceof TerminalImpl)) {
                throw new IllegalArgumentException("Invalid terminal type: " + cardTerminal.getClass().getName());
            }
            int i3 = i2;
            i2++;
            strArr[i3] = ((TerminalImpl) cardTerminal).name;
        }
        int[] iArrSCardGetStatusChange = new int[strArr.length];
        Arrays.fill(iArrSCardGetStatusChange, 0);
        do {
            try {
                iArrSCardGetStatusChange = PCSC.SCardGetStatusChange(contextId, j3, iArrSCardGetStatusChange, strArr);
                j3 = j2;
                arrayList = null;
                for (int i4 = 0; i4 < strArr.length; i4++) {
                    if (((iArrSCardGetStatusChange[i4] & 32) != 0) == z2) {
                        if (arrayList == null) {
                            arrayList = new ArrayList();
                        }
                        arrayList.add(implGetTerminal(strArr[i4]));
                    }
                }
            } catch (PCSCException e2) {
                if (e2.code == -2146435062) {
                    return Collections.emptyList();
                }
                throw new CardException("waitForCard() failed", e2);
            }
        } while (arrayList == null);
        return Collections.unmodifiableList(arrayList);
    }
}
