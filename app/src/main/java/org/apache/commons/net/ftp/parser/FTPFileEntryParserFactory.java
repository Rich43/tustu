package org.apache.commons.net.ftp.parser;

import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFileEntryParser;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/ftp/parser/FTPFileEntryParserFactory.class */
public interface FTPFileEntryParserFactory {
    FTPFileEntryParser createFileEntryParser(String str) throws ParserInitializationException;

    FTPFileEntryParser createFileEntryParser(FTPClientConfig fTPClientConfig) throws ParserInitializationException;
}
