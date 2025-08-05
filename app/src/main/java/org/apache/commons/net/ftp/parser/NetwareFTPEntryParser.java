package org.apache.commons.net.ftp.parser;

import java.text.ParseException;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.icepdf.core.util.PdfOps;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/ftp/parser/NetwareFTPEntryParser.class */
public class NetwareFTPEntryParser extends ConfigurableFTPFileEntryParserImpl {
    private static final String DEFAULT_DATE_FORMAT = "MMM dd yyyy";
    private static final String DEFAULT_RECENT_DATE_FORMAT = "MMM dd HH:mm";
    private static final String REGEX = "(d|-){1}\\s+\\[([-A-Z]+)\\]\\s+(\\S+)\\s+(\\d+)\\s+(\\S+\\s+\\S+\\s+((\\d+:\\d+)|(\\d{4})))\\s+(.*)";

    public NetwareFTPEntryParser() {
        this(null);
    }

    public NetwareFTPEntryParser(FTPClientConfig config) {
        super(REGEX);
        configure(config);
    }

    @Override // org.apache.commons.net.ftp.FTPFileEntryParser
    public FTPFile parseFTPEntry(String entry) {
        FTPFile f2 = new FTPFile();
        if (matches(entry)) {
            String dirString = group(1);
            String attrib = group(2);
            String user = group(3);
            String size = group(4);
            String datestr = group(5);
            String name = group(9);
            try {
                f2.setTimestamp(super.parseTimestamp(datestr));
            } catch (ParseException e2) {
            }
            if (dirString.trim().equals(PdfOps.d_TOKEN)) {
                f2.setType(1);
            } else {
                f2.setType(0);
            }
            f2.setUser(user);
            f2.setName(name.trim());
            f2.setSize(Long.parseLong(size.trim()));
            if (attrib.indexOf("R") != -1) {
                f2.setPermission(0, 0, true);
            }
            if (attrib.indexOf(PdfOps.W_TOKEN) != -1) {
                f2.setPermission(0, 1, true);
            }
            return f2;
        }
        return null;
    }

    @Override // org.apache.commons.net.ftp.parser.ConfigurableFTPFileEntryParserImpl
    protected FTPClientConfig getDefaultConfiguration() {
        return new FTPClientConfig(FTPClientConfig.SYST_NETWARE, DEFAULT_DATE_FORMAT, DEFAULT_RECENT_DATE_FORMAT);
    }
}
