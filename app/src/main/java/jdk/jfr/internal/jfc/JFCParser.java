package jdk.jfr.internal.jfc;

import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import jdk.internal.org.xml.sax.InputSource;
import jdk.internal.org.xml.sax.SAXException;
import jdk.internal.util.xml.impl.SAXParserImpl;
import jdk.jfr.Configuration;
import jdk.jfr.internal.PrivateAccess;

/* loaded from: jfr.jar:jdk/jfr/internal/jfc/JFCParser.class */
final class JFCParser {
    static final String FILE_EXTENSION = ".jfc";
    private static final int MAXIMUM_FILE_SIZE = 1048576;

    JFCParser() {
    }

    public static Configuration createConfiguration(String str, Reader reader) throws IOException, ParseException {
        return createConfiguration(str, readContent(reader));
    }

    public static Configuration createConfiguration(String str, String str2) throws IOException, ParseException {
        try {
            JFCParserHandler jFCParserHandler = new JFCParserHandler();
            parseXML(str2, jFCParserHandler);
            return PrivateAccess.getInstance().newConfiguration(str, jFCParserHandler.label, jFCParserHandler.description, jFCParserHandler.provider, jFCParserHandler.settings, str2);
        } catch (IllegalArgumentException e2) {
            throw new ParseException(e2.getMessage(), -1);
        } catch (SAXException e3) {
            ParseException parseException = new ParseException("Error reading JFC file. " + e3.getMessage(), -1);
            parseException.initCause(e3);
            throw parseException;
        }
    }

    private static void parseXML(String str, JFCParserHandler jFCParserHandler) throws IOException, SAXException {
        new SAXParserImpl().parse(new InputSource(new CharArrayReader(str.toCharArray())), jFCParserHandler);
    }

    private static String readContent(Reader reader) throws IOException {
        CharArrayWriter charArrayWriter = new CharArrayWriter(1024);
        int i2 = 0;
        do {
            int i3 = reader.read();
            if (i3 != -1) {
                charArrayWriter.write(i3);
                i2++;
            } else {
                return new String(charArrayWriter.toCharArray());
            }
        } while (i2 < 1048576);
        throw new IOException("Presets with more than 1048576 characters can't be read.");
    }
}
