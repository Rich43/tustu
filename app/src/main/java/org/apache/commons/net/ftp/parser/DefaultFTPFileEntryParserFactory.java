package org.apache.commons.net.ftp.parser;

import java.util.Locale;
import java.util.regex.Pattern;
import org.apache.commons.net.ftp.Configurable;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFileEntryParser;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/ftp/parser/DefaultFTPFileEntryParserFactory.class */
public class DefaultFTPFileEntryParserFactory implements FTPFileEntryParserFactory {
    private static final String JAVA_IDENTIFIER = "\\p{javaJavaIdentifierStart}(\\p{javaJavaIdentifierPart})*";
    private static final String JAVA_QUALIFIED_NAME = "(\\p{javaJavaIdentifierStart}(\\p{javaJavaIdentifierPart})*\\.)+\\p{javaJavaIdentifierStart}(\\p{javaJavaIdentifierPart})*";
    private static final Pattern JAVA_QUALIFIED_NAME_PATTERN = Pattern.compile(JAVA_QUALIFIED_NAME);

    @Override // org.apache.commons.net.ftp.parser.FTPFileEntryParserFactory
    public FTPFileEntryParser createFileEntryParser(String key) {
        if (key == null) {
            throw new ParserInitializationException("Parser key cannot be null");
        }
        return createFileEntryParser(key, null);
    }

    private FTPFileEntryParser createFileEntryParser(String key, FTPClientConfig config) {
        FTPFileEntryParser parser = null;
        if (JAVA_QUALIFIED_NAME_PATTERN.matcher(key).matches()) {
            try {
                Class<?> parserClass = Class.forName(key);
                try {
                    try {
                        try {
                            parser = (FTPFileEntryParser) parserClass.newInstance();
                        } catch (ClassCastException e2) {
                            throw new ParserInitializationException(parserClass.getName() + " does not implement the interface org.apache.commons.net.ftp.FTPFileEntryParser.", e2);
                        }
                    } catch (Exception e3) {
                        throw new ParserInitializationException("Error initializing parser", e3);
                    }
                } catch (ExceptionInInitializerError e4) {
                    throw new ParserInitializationException("Error initializing parser", e4);
                }
            } catch (ClassNotFoundException e5) {
            }
        }
        if (parser == null) {
            String ukey = key.toUpperCase(Locale.ENGLISH);
            if (ukey.indexOf(FTPClientConfig.SYST_UNIX_TRIM_LEADING) >= 0) {
                parser = new UnixFTPEntryParser(config, true);
            } else if (ukey.indexOf(FTPClientConfig.SYST_UNIX) >= 0) {
                parser = new UnixFTPEntryParser(config, false);
            } else if (ukey.indexOf(FTPClientConfig.SYST_VMS) >= 0) {
                parser = new VMSVersioningFTPEntryParser(config);
            } else if (ukey.indexOf(FTPClientConfig.SYST_NT) >= 0) {
                parser = createNTFTPEntryParser(config);
            } else if (ukey.indexOf(FTPClientConfig.SYST_OS2) >= 0) {
                parser = new OS2FTPEntryParser(config);
            } else if (ukey.indexOf(FTPClientConfig.SYST_OS400) >= 0 || ukey.indexOf(FTPClientConfig.SYST_AS400) >= 0) {
                parser = createOS400FTPEntryParser(config);
            } else if (ukey.indexOf(FTPClientConfig.SYST_MVS) >= 0) {
                parser = new MVSFTPEntryParser();
            } else if (ukey.indexOf(FTPClientConfig.SYST_NETWARE) >= 0) {
                parser = new NetwareFTPEntryParser(config);
            } else if (ukey.indexOf(FTPClientConfig.SYST_MACOS_PETER) >= 0) {
                parser = new MacOsPeterFTPEntryParser(config);
            } else if (ukey.indexOf(FTPClientConfig.SYST_L8) >= 0) {
                parser = new UnixFTPEntryParser(config);
            } else {
                throw new ParserInitializationException("Unknown parser type: " + key);
            }
        }
        if (parser instanceof Configurable) {
            ((Configurable) parser).configure(config);
        }
        return parser;
    }

    @Override // org.apache.commons.net.ftp.parser.FTPFileEntryParserFactory
    public FTPFileEntryParser createFileEntryParser(FTPClientConfig config) throws ParserInitializationException {
        String key = config.getServerSystemKey();
        return createFileEntryParser(key, config);
    }

    public FTPFileEntryParser createUnixFTPEntryParser() {
        return new UnixFTPEntryParser();
    }

    public FTPFileEntryParser createVMSVersioningFTPEntryParser() {
        return new VMSVersioningFTPEntryParser();
    }

    public FTPFileEntryParser createNetwareFTPEntryParser() {
        return new NetwareFTPEntryParser();
    }

    public FTPFileEntryParser createNTFTPEntryParser() {
        return createNTFTPEntryParser(null);
    }

    private FTPFileEntryParser createNTFTPEntryParser(FTPClientConfig config) {
        if (config != null && FTPClientConfig.SYST_NT.equals(config.getServerSystemKey())) {
            return new NTFTPEntryParser(config);
        }
        FTPClientConfig config2 = config != null ? new FTPClientConfig(config) : null;
        FTPFileEntryParser[] fTPFileEntryParserArr = new FTPFileEntryParser[2];
        fTPFileEntryParserArr[0] = new NTFTPEntryParser(config);
        fTPFileEntryParserArr[1] = new UnixFTPEntryParser(config2, config2 != null && FTPClientConfig.SYST_UNIX_TRIM_LEADING.equals(config2.getServerSystemKey()));
        return new CompositeFileEntryParser(fTPFileEntryParserArr);
    }

    public FTPFileEntryParser createOS2FTPEntryParser() {
        return new OS2FTPEntryParser();
    }

    public FTPFileEntryParser createOS400FTPEntryParser() {
        return createOS400FTPEntryParser(null);
    }

    private FTPFileEntryParser createOS400FTPEntryParser(FTPClientConfig config) {
        if (config != null && FTPClientConfig.SYST_OS400.equals(config.getServerSystemKey())) {
            return new OS400FTPEntryParser(config);
        }
        FTPClientConfig config2 = config != null ? new FTPClientConfig(config) : null;
        FTPFileEntryParser[] fTPFileEntryParserArr = new FTPFileEntryParser[2];
        fTPFileEntryParserArr[0] = new OS400FTPEntryParser(config);
        fTPFileEntryParserArr[1] = new UnixFTPEntryParser(config2, config2 != null && FTPClientConfig.SYST_UNIX_TRIM_LEADING.equals(config2.getServerSystemKey()));
        return new CompositeFileEntryParser(fTPFileEntryParserArr);
    }

    public FTPFileEntryParser createMVSEntryParser() {
        return new MVSFTPEntryParser();
    }
}
