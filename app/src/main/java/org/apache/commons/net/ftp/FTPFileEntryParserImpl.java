package org.apache.commons.net.ftp;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/ftp/FTPFileEntryParserImpl.class */
public abstract class FTPFileEntryParserImpl implements FTPFileEntryParser {
    @Override // org.apache.commons.net.ftp.FTPFileEntryParser
    public String readNextEntry(BufferedReader reader) throws IOException {
        return reader.readLine();
    }

    @Override // org.apache.commons.net.ftp.FTPFileEntryParser
    public List<String> preParse(List<String> original) {
        return original;
    }
}
