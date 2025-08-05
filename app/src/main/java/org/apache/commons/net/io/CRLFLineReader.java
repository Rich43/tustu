package org.apache.commons.net.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/io/CRLFLineReader.class */
public final class CRLFLineReader extends BufferedReader {
    private static final char LF = '\n';
    private static final char CR = '\r';

    public CRLFLineReader(Reader reader) {
        super(reader);
    }

    @Override // java.io.BufferedReader
    public String readLine() throws IOException {
        StringBuilder sb = new StringBuilder();
        boolean prevWasCR = false;
        synchronized (this.lock) {
            while (true) {
                int intch = read();
                if (intch != -1) {
                    if (prevWasCR && intch == 10) {
                        return sb.substring(0, sb.length() - 1);
                    }
                    if (intch == 13) {
                        prevWasCR = true;
                    } else {
                        prevWasCR = false;
                    }
                    sb.append((char) intch);
                } else {
                    String string = sb.toString();
                    if (string.length() == 0) {
                        return null;
                    }
                    return string;
                }
            }
        }
    }
}
