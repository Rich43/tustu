package sun.security.util;

import java.io.ByteArrayInputStream;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;
import sun.misc.SharedSecrets;

/* loaded from: rt.jar:sun/security/util/Password.class */
public class Password {
    private static volatile CharsetEncoder enc;

    public static char[] readPassword(InputStream inputStream) throws IOException {
        return readPassword(inputStream, false);
    }

    public static char[] readPassword(InputStream inputStream, boolean z2) throws IOException {
        Console console;
        char[] password = null;
        byte[] bArrConvertToBytes = null;
        if (!z2) {
            try {
                if (inputStream == System.in && (console = System.console()) != null) {
                    password = console.readPassword();
                    if (password != null && password.length == 0) {
                        if (password != null) {
                            Arrays.fill(password, ' ');
                        }
                        if (0 != 0) {
                            Arrays.fill((byte[]) null, (byte) 0);
                        }
                        return null;
                    }
                    bArrConvertToBytes = convertToBytes(password);
                    inputStream = new ByteArrayInputStream(bArrConvertToBytes);
                }
            } finally {
                if (password != null) {
                    Arrays.fill(password, ' ');
                }
                if (bArrConvertToBytes != null) {
                    Arrays.fill(bArrConvertToBytes, (byte) 0);
                }
            }
        }
        char[] cArr = new char[128];
        char[] cArr2 = cArr;
        char[] cArr3 = cArr;
        int length = cArr3.length;
        int i2 = 0;
        boolean z3 = false;
        while (!z3) {
            int i3 = inputStream.read();
            switch (i3) {
                case -1:
                case 10:
                    z3 = true;
                    continue;
                case 13:
                    int i4 = inputStream.read();
                    if (i4 != 10 && i4 != -1) {
                        if (!(inputStream instanceof PushbackInputStream)) {
                            inputStream = new PushbackInputStream(inputStream);
                        }
                        ((PushbackInputStream) inputStream).unread(i4);
                        break;
                    } else {
                        z3 = true;
                    }
                    break;
            }
            length--;
            if (length < 0) {
                cArr3 = new char[i2 + 128];
                length = (cArr3.length - i2) - 1;
                System.arraycopy(cArr2, 0, cArr3, 0, i2);
                Arrays.fill(cArr2, ' ');
                cArr2 = cArr3;
            }
            int i5 = i2;
            i2++;
            cArr3[i5] = (char) i3;
        }
        if (i2 == 0) {
            return null;
        }
        char[] cArr4 = new char[i2];
        System.arraycopy(cArr3, 0, cArr4, 0, i2);
        Arrays.fill(cArr3, ' ');
        if (password != null) {
            Arrays.fill(password, ' ');
        }
        if (bArrConvertToBytes != null) {
            Arrays.fill(bArrConvertToBytes, (byte) 0);
        }
        return cArr4;
    }

    private static byte[] convertToBytes(char[] cArr) {
        if (enc == null) {
            synchronized (Password.class) {
                enc = SharedSecrets.getJavaIOAccess().charset().newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
            }
        }
        byte[] bArr = new byte[(int) (enc.maxBytesPerChar() * cArr.length)];
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
        synchronized (enc) {
            enc.reset().encode(CharBuffer.wrap(cArr), byteBufferWrap, true);
        }
        if (byteBufferWrap.position() < bArr.length) {
            bArr[byteBufferWrap.position()] = 10;
        }
        return bArr;
    }
}
