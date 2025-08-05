package org.apache.commons.net.ftp.parser;

import java.text.ParseException;
import java.util.Calendar;
import org.apache.commons.net.ftp.Configurable;
import org.apache.commons.net.ftp.FTPClientConfig;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/ftp/parser/ConfigurableFTPFileEntryParserImpl.class */
public abstract class ConfigurableFTPFileEntryParserImpl extends RegexFTPFileEntryParserImpl implements Configurable {
    private final FTPTimestampParser timestampParser;

    protected abstract FTPClientConfig getDefaultConfiguration();

    public ConfigurableFTPFileEntryParserImpl(String regex) {
        super(regex);
        this.timestampParser = new FTPTimestampParserImpl();
    }

    public ConfigurableFTPFileEntryParserImpl(String regex, int flags) {
        super(regex, flags);
        this.timestampParser = new FTPTimestampParserImpl();
    }

    public Calendar parseTimestamp(String timestampStr) throws ParseException {
        return this.timestampParser.parseTimestamp(timestampStr);
    }

    @Override // org.apache.commons.net.ftp.Configurable
    public void configure(FTPClientConfig config) {
        if (this.timestampParser instanceof Configurable) {
            FTPClientConfig defaultCfg = getDefaultConfiguration();
            if (config != null) {
                if (null == config.getDefaultDateFormatStr()) {
                    config.setDefaultDateFormatStr(defaultCfg.getDefaultDateFormatStr());
                }
                if (null == config.getRecentDateFormatStr()) {
                    config.setRecentDateFormatStr(defaultCfg.getRecentDateFormatStr());
                }
                ((Configurable) this.timestampParser).configure(config);
                return;
            }
            ((Configurable) this.timestampParser).configure(defaultCfg);
        }
    }
}
