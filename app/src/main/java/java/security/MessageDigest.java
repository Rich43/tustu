package java.security;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import javax.crypto.SecretKey;
import sun.security.util.Debug;
import sun.security.util.MessageDigestSpi2;

/* loaded from: rt.jar:java/security/MessageDigest.class */
public abstract class MessageDigest extends MessageDigestSpi {
    private static final Debug pdebug = Debug.getInstance("provider", "Provider");
    private static final boolean skipDebug;
    private String algorithm;
    private static final int INITIAL = 0;
    private static final int IN_PROGRESS = 1;
    private int state = 0;
    private Provider provider;

    static {
        skipDebug = Debug.isOn("engine=") && !Debug.isOn("messagedigest");
    }

    protected MessageDigest(String str) {
        this.algorithm = str;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v14, types: [java.security.MessageDigest] */
    public static MessageDigest getInstance(String str) throws NoSuchAlgorithmException {
        Delegate delegate;
        try {
            Object[] impl = Security.getImpl(str, "MessageDigest", (String) null);
            if (impl[0] instanceof MessageDigest) {
                delegate = (MessageDigest) impl[0];
            } else {
                delegate = new Delegate((MessageDigestSpi) impl[0], str);
            }
            ((MessageDigest) delegate).provider = (Provider) impl[1];
            if (!skipDebug && pdebug != null) {
                pdebug.println("MessageDigest." + str + " algorithm from: " + ((MessageDigest) delegate).provider.getName());
            }
            return delegate;
        } catch (NoSuchProviderException e2) {
            throw new NoSuchAlgorithmException(str + " not found");
        }
    }

    public static MessageDigest getInstance(String str, String str2) throws NoSuchAlgorithmException, NoSuchProviderException {
        if (str2 == null || str2.length() == 0) {
            throw new IllegalArgumentException("missing provider");
        }
        Object[] impl = Security.getImpl(str, "MessageDigest", str2);
        if (impl[0] instanceof MessageDigest) {
            MessageDigest messageDigest = (MessageDigest) impl[0];
            messageDigest.provider = (Provider) impl[1];
            return messageDigest;
        }
        Delegate delegate = new Delegate((MessageDigestSpi) impl[0], str);
        ((MessageDigest) delegate).provider = (Provider) impl[1];
        return delegate;
    }

    public static MessageDigest getInstance(String str, Provider provider) throws NoSuchAlgorithmException {
        if (provider == null) {
            throw new IllegalArgumentException("missing provider");
        }
        Object[] impl = Security.getImpl(str, "MessageDigest", provider);
        if (impl[0] instanceof MessageDigest) {
            MessageDigest messageDigest = (MessageDigest) impl[0];
            messageDigest.provider = (Provider) impl[1];
            return messageDigest;
        }
        Delegate delegate = new Delegate((MessageDigestSpi) impl[0], str);
        ((MessageDigest) delegate).provider = (Provider) impl[1];
        return delegate;
    }

    public final Provider getProvider() {
        return this.provider;
    }

    public void update(byte b2) {
        engineUpdate(b2);
        this.state = 1;
    }

    public void update(byte[] bArr, int i2, int i3) {
        if (bArr == null) {
            throw new IllegalArgumentException("No input buffer given");
        }
        if (bArr.length - i2 < i3) {
            throw new IllegalArgumentException("Input buffer too short");
        }
        engineUpdate(bArr, i2, i3);
        this.state = 1;
    }

    public void update(byte[] bArr) {
        engineUpdate(bArr, 0, bArr.length);
        this.state = 1;
    }

    public final void update(ByteBuffer byteBuffer) {
        if (byteBuffer == null) {
            throw new NullPointerException();
        }
        engineUpdate(byteBuffer);
        this.state = 1;
    }

    public byte[] digest() {
        byte[] bArrEngineDigest = engineDigest();
        this.state = 0;
        return bArrEngineDigest;
    }

    public int digest(byte[] bArr, int i2, int i3) throws DigestException {
        if (bArr == null) {
            throw new IllegalArgumentException("No output buffer given");
        }
        if (bArr.length - i2 < i3) {
            throw new IllegalArgumentException("Output buffer too small for specified offset and length");
        }
        int iEngineDigest = engineDigest(bArr, i2, i3);
        this.state = 0;
        return iEngineDigest;
    }

    public byte[] digest(byte[] bArr) {
        update(bArr);
        return digest();
    }

    public String toString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        printStream.print(this.algorithm + " Message Digest from " + this.provider.getName() + ", ");
        switch (this.state) {
            case 0:
                printStream.print("<initialized>");
                break;
            case 1:
                printStream.print("<in progress>");
                break;
        }
        printStream.println();
        return byteArrayOutputStream.toString();
    }

    public static boolean isEqual(byte[] bArr, byte[] bArr2) {
        if (bArr == bArr2) {
            return true;
        }
        if (bArr == null || bArr2 == null) {
            return false;
        }
        int length = bArr.length;
        int length2 = bArr2.length;
        if (length2 == 0) {
            return length == 0;
        }
        int i2 = 0 | (length - length2);
        for (int i3 = 0; i3 < length; i3++) {
            i2 |= bArr[i3] ^ bArr2[((i3 - length2) >>> 31) * i3];
        }
        return i2 == 0;
    }

    public void reset() {
        engineReset();
        this.state = 0;
    }

    public final String getAlgorithm() {
        return this.algorithm;
    }

    public final int getDigestLength() {
        int iEngineGetDigestLength = engineGetDigestLength();
        if (iEngineGetDigestLength == 0) {
            try {
                return ((MessageDigest) clone()).digest().length;
            } catch (CloneNotSupportedException e2) {
                return iEngineGetDigestLength;
            }
        }
        return iEngineGetDigestLength;
    }

    @Override // java.security.MessageDigestSpi
    public Object clone() throws CloneNotSupportedException {
        if (this instanceof Cloneable) {
            return super.clone();
        }
        throw new CloneNotSupportedException();
    }

    /* loaded from: rt.jar:java/security/MessageDigest$Delegate.class */
    static class Delegate extends MessageDigest implements MessageDigestSpi2 {
        private MessageDigestSpi digestSpi;

        public Delegate(MessageDigestSpi messageDigestSpi, String str) {
            super(str);
            this.digestSpi = messageDigestSpi;
        }

        @Override // java.security.MessageDigest, java.security.MessageDigestSpi
        public Object clone() throws CloneNotSupportedException {
            if (this.digestSpi instanceof Cloneable) {
                Delegate delegate = new Delegate((MessageDigestSpi) this.digestSpi.clone(), ((MessageDigest) this).algorithm);
                ((MessageDigest) delegate).provider = ((MessageDigest) this).provider;
                ((MessageDigest) delegate).state = ((MessageDigest) this).state;
                return delegate;
            }
            throw new CloneNotSupportedException();
        }

        @Override // java.security.MessageDigestSpi
        protected int engineGetDigestLength() {
            return this.digestSpi.engineGetDigestLength();
        }

        @Override // java.security.MessageDigestSpi
        protected void engineUpdate(byte b2) {
            this.digestSpi.engineUpdate(b2);
        }

        @Override // java.security.MessageDigestSpi
        protected void engineUpdate(byte[] bArr, int i2, int i3) {
            this.digestSpi.engineUpdate(bArr, i2, i3);
        }

        @Override // java.security.MessageDigestSpi
        protected void engineUpdate(ByteBuffer byteBuffer) {
            this.digestSpi.engineUpdate(byteBuffer);
        }

        @Override // sun.security.util.MessageDigestSpi2
        public void engineUpdate(SecretKey secretKey) throws InvalidKeyException {
            if (this.digestSpi instanceof MessageDigestSpi2) {
                ((MessageDigestSpi2) this.digestSpi).engineUpdate(secretKey);
                return;
            }
            throw new UnsupportedOperationException("Digest does not support update of SecretKey object");
        }

        @Override // java.security.MessageDigestSpi
        protected byte[] engineDigest() {
            return this.digestSpi.engineDigest();
        }

        @Override // java.security.MessageDigestSpi
        protected int engineDigest(byte[] bArr, int i2, int i3) throws DigestException {
            return this.digestSpi.engineDigest(bArr, i2, i3);
        }

        @Override // java.security.MessageDigestSpi
        protected void engineReset() {
            this.digestSpi.engineReset();
        }
    }
}
