package org.apache.commons.net.ftp;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/ftp/FTPFileEntryParser.class */
public interface FTPFileEntryParser {
    FTPFile parseFTPEntry(String str);

    String readNextEntry(BufferedReader bufferedReader) throws IOException;

    List<String> preParse(List<String> list);
}
