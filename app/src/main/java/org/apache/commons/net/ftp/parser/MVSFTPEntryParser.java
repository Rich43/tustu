package org.apache.commons.net.ftp.parser;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.text.ParseException;
import java.util.List;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/ftp/parser/MVSFTPEntryParser.class */
public class MVSFTPEntryParser extends ConfigurableFTPFileEntryParserImpl {
    static final int UNKNOWN_LIST_TYPE = -1;
    static final int FILE_LIST_TYPE = 0;
    static final int MEMBER_LIST_TYPE = 1;
    static final int UNIX_LIST_TYPE = 2;
    static final int JES_LEVEL_1_LIST_TYPE = 3;
    static final int JES_LEVEL_2_LIST_TYPE = 4;
    private int isType;
    private UnixFTPEntryParser unixFTPEntryParser;
    static final String DEFAULT_DATE_FORMAT = "yyyy/MM/dd HH:mm";
    static final String FILE_LIST_REGEX = "\\S+\\s+\\S+\\s+\\S+\\s+\\S+\\s+\\S+\\s+[FV]\\S*\\s+\\S+\\s+\\S+\\s+(PS|PO|PO-E)\\s+(\\S+)\\s*";
    static final String MEMBER_LIST_REGEX = "(\\S+)\\s+\\S+\\s+\\S+\\s+(\\S+)\\s+(\\S+)\\s+\\S+\\s+\\S+\\s+\\S+\\s+\\S+\\s*";
    static final String JES_LEVEL_1_LIST_REGEX = "(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s*";
    static final String JES_LEVEL_2_LIST_REGEX = "(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+).*";

    public MVSFTPEntryParser() {
        super("");
        this.isType = -1;
        super.configure(null);
    }

    @Override // org.apache.commons.net.ftp.FTPFileEntryParser
    public FTPFile parseFTPEntry(String entry) {
        boolean isParsed = false;
        FTPFile f2 = new FTPFile();
        if (this.isType == 0) {
            isParsed = parseFileList(f2, entry);
        } else if (this.isType == 1) {
            isParsed = parseMemberList(f2, entry);
            if (!isParsed) {
                isParsed = parseSimpleEntry(f2, entry);
            }
        } else if (this.isType == 2) {
            isParsed = parseUnixList(f2, entry);
        } else if (this.isType == 3) {
            isParsed = parseJeslevel1List(f2, entry);
        } else if (this.isType == 4) {
            isParsed = parseJeslevel2List(f2, entry);
        }
        if (!isParsed) {
            f2 = null;
        }
        return f2;
    }

    private boolean parseFileList(FTPFile file, String entry) {
        if (matches(entry)) {
            file.setRawListing(entry);
            String name = group(2);
            String dsorg = group(1);
            file.setName(name);
            if ("PS".equals(dsorg)) {
                file.setType(0);
                return true;
            }
            if ("PO".equals(dsorg) || "PO-E".equals(dsorg)) {
                file.setType(1);
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean parseMemberList(FTPFile file, String entry) {
        if (matches(entry)) {
            file.setRawListing(entry);
            String name = group(1);
            String datestr = group(2) + " " + group(3);
            file.setName(name);
            file.setType(0);
            try {
                file.setTimestamp(super.parseTimestamp(datestr));
                return true;
            } catch (ParseException e2) {
                e2.printStackTrace();
                return false;
            }
        }
        return false;
    }

    private boolean parseSimpleEntry(FTPFile file, String entry) {
        if (entry != null && entry.trim().length() > 0) {
            file.setRawListing(entry);
            String name = entry.split(" ")[0];
            file.setName(name);
            file.setType(0);
            return true;
        }
        return false;
    }

    private boolean parseUnixList(FTPFile file, String entry) {
        FTPFile file2 = this.unixFTPEntryParser.parseFTPEntry(entry);
        if (file2 == null) {
            return false;
        }
        return true;
    }

    private boolean parseJeslevel1List(FTPFile file, String entry) {
        if (matches(entry) && group(3).equalsIgnoreCase("OUTPUT")) {
            file.setRawListing(entry);
            String name = group(2);
            file.setName(name);
            file.setType(0);
            return true;
        }
        return false;
    }

    private boolean parseJeslevel2List(FTPFile file, String entry) {
        if (matches(entry) && group(4).equalsIgnoreCase("OUTPUT")) {
            file.setRawListing(entry);
            String name = group(2);
            file.setName(name);
            file.setType(0);
            return true;
        }
        return false;
    }

    @Override // org.apache.commons.net.ftp.FTPFileEntryParserImpl, org.apache.commons.net.ftp.FTPFileEntryParser
    public List<String> preParse(List<String> orig) {
        if (orig != null && orig.size() > 0) {
            String header = orig.get(0);
            if (header.indexOf("Volume") >= 0 && header.indexOf("Dsname") >= 0) {
                setType(0);
                super.setRegex(FILE_LIST_REGEX);
            } else if (header.indexOf("Name") >= 0 && header.indexOf(Constants._ATT_ID) >= 0) {
                setType(1);
                super.setRegex(MEMBER_LIST_REGEX);
            } else if (header.indexOf("total") == 0) {
                setType(2);
                this.unixFTPEntryParser = new UnixFTPEntryParser();
            } else if (header.indexOf("Spool Files") >= 30) {
                setType(3);
                super.setRegex(JES_LEVEL_1_LIST_REGEX);
            } else if (header.indexOf("JOBNAME") == 0 && header.indexOf("JOBID") > 8) {
                setType(4);
                super.setRegex(JES_LEVEL_2_LIST_REGEX);
            } else {
                setType(-1);
            }
            if (this.isType != 3) {
                orig.remove(0);
            }
        }
        return orig;
    }

    void setType(int type) {
        this.isType = type;
    }

    @Override // org.apache.commons.net.ftp.parser.ConfigurableFTPFileEntryParserImpl
    protected FTPClientConfig getDefaultConfiguration() {
        return new FTPClientConfig(FTPClientConfig.SYST_MVS, DEFAULT_DATE_FORMAT, null);
    }
}
