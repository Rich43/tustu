package org.apache.commons.net.ftp.parser;

import java.text.ParseException;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/ftp/parser/OS2FTPEntryParser.class */
public class OS2FTPEntryParser extends ConfigurableFTPFileEntryParserImpl {
    private static final String DEFAULT_DATE_FORMAT = "MM-dd-yy HH:mm";
    private static final String REGEX = "\\s*([0-9]+)\\s*(\\s+|[A-Z]+)\\s*(DIR|\\s+)\\s*(\\S+)\\s+(\\S+)\\s+(\\S.*)";

    public OS2FTPEntryParser() {
        this(null);
    }

    public OS2FTPEntryParser(FTPClientConfig config) {
        super(REGEX);
        configure(config);
    }

    @Override // org.apache.commons.net.ftp.FTPFileEntryParser
    public FTPFile parseFTPEntry(String entry) {
        FTPFile f2 = new FTPFile();
        if (matches(entry)) {
            String size = group(1);
            String attrib = group(2);
            String dirString = group(3);
            String datestr = group(4) + " " + group(5);
            String name = group(6);
            try {
                f2.setTimestamp(super.parseTimestamp(datestr));
            } catch (ParseException e2) {
            }
            if (dirString.trim().equals("DIR") || attrib.trim().equals("DIR")) {
                f2.setType(1);
            } else {
                f2.setType(0);
            }
            f2.setName(name.trim());
            f2.setSize(Long.parseLong(size.trim()));
            return f2;
        }
        return null;
    }

    @Override // org.apache.commons.net.ftp.parser.ConfigurableFTPFileEntryParserImpl
    protected FTPClientConfig getDefaultConfiguration() {
        return new FTPClientConfig(FTPClientConfig.SYST_OS2, DEFAULT_DATE_FORMAT, null);
    }
}
