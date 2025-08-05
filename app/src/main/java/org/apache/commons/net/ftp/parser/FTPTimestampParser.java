package org.apache.commons.net.ftp.parser;

import java.text.ParseException;
import java.util.Calendar;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/ftp/parser/FTPTimestampParser.class */
public interface FTPTimestampParser {
    public static final String DEFAULT_SDF = "MMM d yyyy";
    public static final String DEFAULT_RECENT_SDF = "MMM d HH:mm";

    Calendar parseTimestamp(String str) throws ParseException;
}
