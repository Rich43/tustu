package sun.security.smartcardio;

import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;
import java.security.AccessController;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/security/smartcardio/ChannelImpl.class */
final class ChannelImpl extends CardChannel {
    private final CardImpl card;
    private final int channel;
    private volatile boolean isClosed;
    private static final int RESPONSE_ITERATIONS = 256;
    private static final boolean t0GetResponse = getBooleanProperty("sun.security.smartcardio.t0GetResponse", true);
    private static final boolean t1GetResponse = getBooleanProperty("sun.security.smartcardio.t1GetResponse", true);
    private static final boolean t1StripLe = getBooleanProperty("sun.security.smartcardio.t1StripLe", false);
    private static final byte[] B0 = new byte[0];

    ChannelImpl(CardImpl cardImpl, int i2) {
        this.card = cardImpl;
        this.channel = i2;
    }

    void checkClosed() {
        this.card.checkState();
        if (this.isClosed) {
            throw new IllegalStateException("Logical channel has been closed");
        }
    }

    @Override // javax.smartcardio.CardChannel
    public Card getCard() {
        return this.card;
    }

    @Override // javax.smartcardio.CardChannel
    public int getChannelNumber() {
        checkClosed();
        return this.channel;
    }

    private static void checkManageChannel(byte[] bArr) {
        if (bArr.length < 4) {
            throw new IllegalArgumentException("Command APDU must be at least 4 bytes long");
        }
        if (bArr[0] >= 0 && bArr[1] == 112) {
            throw new IllegalArgumentException("Manage channel command not allowed, use openLogicalChannel()");
        }
    }

    @Override // javax.smartcardio.CardChannel
    public ResponseAPDU transmit(CommandAPDU commandAPDU) throws CardException {
        checkClosed();
        this.card.checkExclusive();
        return new ResponseAPDU(doTransmit(commandAPDU.getBytes()));
    }

    @Override // javax.smartcardio.CardChannel
    public int transmit(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) throws CardException {
        checkClosed();
        this.card.checkExclusive();
        if (byteBuffer == null || byteBuffer2 == null) {
            throw new NullPointerException();
        }
        if (byteBuffer2.isReadOnly()) {
            throw new ReadOnlyBufferException();
        }
        if (byteBuffer == byteBuffer2) {
            throw new IllegalArgumentException("command and response must not be the same object");
        }
        if (byteBuffer2.remaining() < 258) {
            throw new IllegalArgumentException("Insufficient space in response buffer");
        }
        byte[] bArr = new byte[byteBuffer.remaining()];
        byteBuffer.get(bArr);
        byte[] bArrDoTransmit = doTransmit(bArr);
        byteBuffer2.put(bArrDoTransmit);
        return bArrDoTransmit.length;
    }

    private static boolean getBooleanProperty(String str, boolean z2) {
        String str2 = (String) AccessController.doPrivileged(new GetPropertyAction(str));
        if (str2 == null) {
            return z2;
        }
        if (str2.equalsIgnoreCase("true")) {
            return true;
        }
        if (str2.equalsIgnoreCase("false")) {
            return false;
        }
        throw new IllegalArgumentException(str + " must be either 'true' or 'false'");
    }

    private byte[] concat(byte[] bArr, byte[] bArr2, int i2) {
        int length = bArr.length;
        if (length == 0 && i2 == bArr2.length) {
            return bArr2;
        }
        byte[] bArr3 = new byte[length + i2];
        System.arraycopy(bArr, 0, bArr3, 0, length);
        System.arraycopy(bArr2, 0, bArr3, length, i2);
        return bArr3;
    }

    private byte[] doTransmit(byte[] bArr) throws CardException {
        byte[] bArrSCardTransmit;
        int length;
        try {
            checkManageChannel(bArr);
            setChannel(bArr);
            int length2 = bArr.length;
            boolean z2 = this.card.protocol == 1;
            boolean z3 = this.card.protocol == 2;
            if (z2 && length2 >= 7 && bArr[4] == 0) {
                throw new CardException("Extended length forms not supported for T=0");
            }
            if ((z2 || (z3 && t1StripLe)) && length2 >= 7) {
                int i2 = bArr[4] & 255;
                if (i2 != 0) {
                    if (length2 == i2 + 6) {
                        length2--;
                    }
                } else if (length2 == (((bArr[5] & 255) << 8) | (bArr[6] & 255)) + 9) {
                    length2 -= 2;
                }
            }
            boolean z4 = (z2 && t0GetResponse) || (z3 && t1GetResponse);
            int i3 = 0;
            byte[] bArrConcat = B0;
            while (true) {
                i3++;
                if (i3 > 256) {
                    throw new CardException("Number of response iterations exceeded maximum 256");
                }
                bArrSCardTransmit = PCSC.SCardTransmit(this.card.cardId, this.card.protocol, bArr, 0, length2);
                length = bArrSCardTransmit.length;
                if (!z4 || length < 2) {
                    break;
                }
                if (length == 2 && bArrSCardTransmit[0] == 108) {
                    bArr[length2 - 1] = bArrSCardTransmit[1];
                } else {
                    if (bArrSCardTransmit[length - 2] != 97) {
                        break;
                    }
                    if (length > 2) {
                        bArrConcat = concat(bArrConcat, bArrSCardTransmit, length - 2);
                    }
                    bArr[1] = -64;
                    bArr[2] = 0;
                    bArr[3] = 0;
                    bArr[4] = bArrSCardTransmit[length - 1];
                    length2 = 5;
                }
            }
            return concat(bArrConcat, bArrSCardTransmit, length);
        } catch (PCSCException e2) {
            this.card.handleError(e2);
            throw new CardException(e2);
        }
    }

    private static int getSW(byte[] bArr) throws CardException {
        if (bArr.length < 2) {
            throw new CardException("Invalid response length: " + bArr.length);
        }
        return ((bArr[bArr.length - 2] & 255) << 8) | (bArr[bArr.length - 1] & 255);
    }

    private static boolean isOK(byte[] bArr) throws CardException {
        return bArr.length == 2 && getSW(bArr) == 36864;
    }

    private void setChannel(byte[] bArr) {
        byte b2 = bArr[0];
        if (b2 < 0 || (b2 & 224) == 32) {
            return;
        }
        if (this.channel <= 3) {
            bArr[0] = (byte) (bArr[0] & 188);
            bArr[0] = (byte) (bArr[0] | this.channel);
        } else {
            if (this.channel <= 19) {
                bArr[0] = (byte) (bArr[0] & 176);
                bArr[0] = (byte) (bArr[0] | 64);
                bArr[0] = (byte) (bArr[0] | (this.channel - 4));
                return;
            }
            throw new RuntimeException("Unsupported channel number: " + this.channel);
        }
    }

    @Override // javax.smartcardio.CardChannel
    public void close() throws CardException {
        if (getChannelNumber() == 0) {
            throw new IllegalStateException("Cannot close basic logical channel");
        }
        if (this.isClosed) {
            return;
        }
        this.card.checkExclusive();
        try {
            try {
                byte[] bArr = {0, 112, Byte.MIN_VALUE, 0};
                bArr[3] = (byte) getChannelNumber();
                setChannel(bArr);
                byte[] bArrSCardTransmit = PCSC.SCardTransmit(this.card.cardId, this.card.protocol, bArr, 0, bArr.length);
                if (!isOK(bArrSCardTransmit)) {
                    throw new CardException("close() failed: " + PCSC.toString(bArrSCardTransmit));
                }
            } catch (PCSCException e2) {
                this.card.handleError(e2);
                throw new CardException("Could not close channel", e2);
            }
        } finally {
            this.isClosed = true;
        }
    }

    public String toString() {
        return "PC/SC channel " + this.channel;
    }
}
