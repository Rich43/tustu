package org.apache.commons.net.ftp.parser;

import java.text.ParseException;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import sun.util.locale.LanguageTag;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/ftp/parser/MacOsPeterFTPEntryParser.class */
public class MacOsPeterFTPEntryParser extends ConfigurableFTPFileEntryParserImpl {
    static final String DEFAULT_DATE_FORMAT = "MMM d yyyy";
    static final String DEFAULT_RECENT_DATE_FORMAT = "MMM d HH:mm";
    private static final String REGEX = "([bcdelfmpSs-])(((r|-)(w|-)([xsStTL-]))((r|-)(w|-)([xsStTL-]))((r|-)(w|-)([xsStTL-])))\\+?\\s+((folder\\s+)|((\\d+)\\s+(\\d+)\\s+))(\\d+)\\s+((?:\\d+[-/]\\d+[-/]\\d+)|(?:\\S{3}\\s+\\d{1,2})|(?:\\d{1,2}\\s+\\S{3}))\\s+(\\d+(?::\\d+)?)\\s+(\\S*)(\\s*.*)";

    public MacOsPeterFTPEntryParser() {
        this(null);
    }

    public MacOsPeterFTPEntryParser(FTPClientConfig config) {
        super(REGEX);
        configure(config);
    }

    @Override // org.apache.commons.net.ftp.FTPFileEntryParser
    public FTPFile parseFTPEntry(String entry) {
        int type;
        int end;
        FTPFile file = new FTPFile();
        file.setRawListing(entry);
        boolean isDevice = false;
        if (matches(entry)) {
            String typeStr = group(1);
            String filesize = group(20);
            String datestr = group(21) + " " + group(22);
            String name = group(23);
            String endtoken = group(24);
            try {
                file.setTimestamp(super.parseTimestamp(datestr));
            } catch (ParseException e2) {
            }
            switch (typeStr.charAt(0)) {
                case '-':
                case 'f':
                    type = 0;
                    break;
                case 'b':
                case 'c':
                    isDevice = true;
                    type = 0;
                    break;
                case 'd':
                    type = 1;
                    break;
                case 'e':
                    type = 2;
                    break;
                case 'l':
                    type = 2;
                    break;
                default:
                    type = 3;
                    break;
            }
            file.setType(type);
            int g2 = 4;
            int access = 0;
            while (access < 3) {
                file.setPermission(access, 0, !group(g2).equals(LanguageTag.SEP));
                file.setPermission(access, 1, !group(g2 + 1).equals(LanguageTag.SEP));
                String execPerm = group(g2 + 2);
                if (!execPerm.equals(LanguageTag.SEP) && !Character.isUpperCase(execPerm.charAt(0))) {
                    file.setPermission(access, 2, true);
                } else {
                    file.setPermission(access, 2, false);
                }
                access++;
                g2 += 4;
            }
            if (!isDevice) {
                try {
                    file.setHardLinkCount(Integer.parseInt("0"));
                } catch (NumberFormatException e3) {
                }
            }
            file.setUser(null);
            file.setGroup(null);
            try {
                file.setSize(Long.parseLong(filesize));
            } catch (NumberFormatException e4) {
            }
            if (null == endtoken) {
                file.setName(name);
            } else {
                String name2 = name + endtoken;
                if (type != 2 || (end = name2.indexOf(" -> ")) == -1) {
                    file.setName(name2);
                } else {
                    file.setName(name2.substring(0, end));
                    file.setLink(name2.substring(end + 4));
                }
            }
            return file;
        }
        return null;
    }

    @Override // org.apache.commons.net.ftp.parser.ConfigurableFTPFileEntryParserImpl
    protected FTPClientConfig getDefaultConfiguration() {
        return new FTPClientConfig(FTPClientConfig.SYST_UNIX, "MMM d yyyy", "MMM d HH:mm");
    }
}
