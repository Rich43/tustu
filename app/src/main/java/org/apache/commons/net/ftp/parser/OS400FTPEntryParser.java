package org.apache.commons.net.ftp.parser;

import java.io.File;
import java.text.ParseException;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/ftp/parser/OS400FTPEntryParser.class */
public class OS400FTPEntryParser extends ConfigurableFTPFileEntryParserImpl {
    private static final String DEFAULT_DATE_FORMAT = "yy/MM/dd HH:mm:ss";
    private static final String REGEX = "(\\S+)\\s+(?:(\\d+)\\s+)?(?:(\\S+)\\s+(\\S+)\\s+)?(\\*STMF|\\*DIR|\\*FILE|\\*MEM)\\s+(?:(\\S+)\\s*)?";

    public OS400FTPEntryParser() {
        this(null);
    }

    public OS400FTPEntryParser(FTPClientConfig config) {
        super(REGEX);
        configure(config);
    }

    @Override // org.apache.commons.net.ftp.FTPFileEntryParser
    public FTPFile parseFTPEntry(String entry) {
        int type;
        int pos;
        FTPFile file = new FTPFile();
        file.setRawListing(entry);
        if (matches(entry)) {
            String usr = group(1);
            String filesize = group(2);
            String datestr = "";
            if (!isNullOrEmpty(group(3)) || !isNullOrEmpty(group(4))) {
                datestr = group(3) + " " + group(4);
            }
            String typeStr = group(5);
            String name = group(6);
            boolean mustScanForPathSeparator = true;
            try {
                file.setTimestamp(super.parseTimestamp(datestr));
            } catch (ParseException e2) {
            }
            if (typeStr.equalsIgnoreCase("*STMF")) {
                type = 0;
                if (isNullOrEmpty(filesize) || isNullOrEmpty(name)) {
                    return null;
                }
            } else if (typeStr.equalsIgnoreCase("*DIR")) {
                type = 1;
                if (isNullOrEmpty(filesize) || isNullOrEmpty(name)) {
                    return null;
                }
            } else if (typeStr.equalsIgnoreCase("*FILE")) {
                if (name != null && name.toUpperCase().endsWith(".SAVF")) {
                    mustScanForPathSeparator = false;
                    type = 0;
                } else {
                    return null;
                }
            } else if (typeStr.equalsIgnoreCase("*MEM")) {
                mustScanForPathSeparator = false;
                type = 0;
                if (isNullOrEmpty(name) || !isNullOrEmpty(filesize) || !isNullOrEmpty(datestr)) {
                    return null;
                }
                name = name.replace('/', File.separatorChar);
            } else {
                type = 3;
            }
            file.setType(type);
            file.setUser(usr);
            try {
                file.setSize(Long.parseLong(filesize));
            } catch (NumberFormatException e3) {
            }
            if (name.endsWith("/")) {
                name = name.substring(0, name.length() - 1);
            }
            if (mustScanForPathSeparator && (pos = name.lastIndexOf(47)) > -1) {
                name = name.substring(pos + 1);
            }
            file.setName(name);
            return file;
        }
        return null;
    }

    private boolean isNullOrEmpty(String string) {
        if (string == null || string.length() == 0) {
            return true;
        }
        return false;
    }

    @Override // org.apache.commons.net.ftp.parser.ConfigurableFTPFileEntryParserImpl
    protected FTPClientConfig getDefaultConfiguration() {
        return new FTPClientConfig(FTPClientConfig.SYST_OS400, DEFAULT_DATE_FORMAT, null);
    }
}
