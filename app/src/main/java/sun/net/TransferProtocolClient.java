package sun.net;

import java.io.IOException;
import java.util.Vector;

/* loaded from: rt.jar:sun/net/TransferProtocolClient.class */
public class TransferProtocolClient extends NetworkClient {
    static final boolean debug = false;
    protected Vector<String> serverResponse;
    protected int lastReplyCode;

    public int readServerResponse() throws IOException {
        int i2;
        StringBuffer stringBuffer = new StringBuffer(32);
        int i3 = -1;
        this.serverResponse.setSize(0);
        while (true) {
            int i4 = this.serverInput.read();
            int i5 = i4;
            if (i4 != -1) {
                if (i5 == 13) {
                    int i6 = this.serverInput.read();
                    i5 = i6;
                    if (i6 != 10) {
                        stringBuffer.append('\r');
                    }
                }
                stringBuffer.append((char) i5);
                if (i5 != 10) {
                    continue;
                }
            }
            String string = stringBuffer.toString();
            stringBuffer.setLength(0);
            if (string.length() == 0) {
                i2 = -1;
            } else {
                try {
                    i2 = Integer.parseInt(string.substring(0, 3));
                } catch (NumberFormatException e2) {
                    i2 = -1;
                } catch (StringIndexOutOfBoundsException e3) {
                }
            }
            this.serverResponse.addElement(string);
            if (i3 != -1) {
                if (i2 == i3 && (string.length() < 4 || string.charAt(3) != '-')) {
                    break;
                }
            } else {
                if (string.length() < 4 || string.charAt(3) != '-') {
                    break;
                }
                i3 = i2;
            }
        }
        int i7 = i2;
        this.lastReplyCode = i7;
        return i7;
    }

    public void sendServer(String str) {
        this.serverOutput.print(str);
    }

    public String getResponseString() {
        return this.serverResponse.elementAt(0);
    }

    public Vector<String> getResponseStrings() {
        return this.serverResponse;
    }

    public TransferProtocolClient(String str, int i2) throws IOException {
        super(str, i2);
        this.serverResponse = new Vector<>(1);
    }

    public TransferProtocolClient() {
        this.serverResponse = new Vector<>(1);
    }
}
