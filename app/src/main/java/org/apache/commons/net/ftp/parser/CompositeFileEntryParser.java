package org.apache.commons.net.ftp.parser;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileEntryParser;
import org.apache.commons.net.ftp.FTPFileEntryParserImpl;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/ftp/parser/CompositeFileEntryParser.class */
public class CompositeFileEntryParser extends FTPFileEntryParserImpl {
    private final FTPFileEntryParser[] ftpFileEntryParsers;
    private FTPFileEntryParser cachedFtpFileEntryParser = null;

    public CompositeFileEntryParser(FTPFileEntryParser[] ftpFileEntryParsers) {
        this.ftpFileEntryParsers = ftpFileEntryParsers;
    }

    @Override // org.apache.commons.net.ftp.FTPFileEntryParser
    public FTPFile parseFTPEntry(String listEntry) {
        if (this.cachedFtpFileEntryParser != null) {
            FTPFile matched = this.cachedFtpFileEntryParser.parseFTPEntry(listEntry);
            if (matched != null) {
                return matched;
            }
            return null;
        }
        FTPFileEntryParser[] arr$ = this.ftpFileEntryParsers;
        for (FTPFileEntryParser ftpFileEntryParser : arr$) {
            FTPFile matched2 = ftpFileEntryParser.parseFTPEntry(listEntry);
            if (matched2 != null) {
                this.cachedFtpFileEntryParser = ftpFileEntryParser;
                return matched2;
            }
        }
        return null;
    }
}
