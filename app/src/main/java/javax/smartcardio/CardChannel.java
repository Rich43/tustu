package javax.smartcardio;

import java.nio.ByteBuffer;

/* loaded from: rt.jar:javax/smartcardio/CardChannel.class */
public abstract class CardChannel {
    public abstract Card getCard();

    public abstract int getChannelNumber();

    public abstract ResponseAPDU transmit(CommandAPDU commandAPDU) throws CardException;

    public abstract int transmit(ByteBuffer byteBuffer, ByteBuffer byteBuffer2) throws CardException;

    public abstract void close() throws CardException;

    protected CardChannel() {
    }
}
