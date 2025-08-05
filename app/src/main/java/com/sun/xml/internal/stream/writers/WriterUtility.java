package com.sun.xml.internal.stream.writers;

import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
import com.sun.org.apache.xml.internal.serializer.SerializerConstants;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/* loaded from: rt.jar:com/sun/xml/internal/stream/writers/WriterUtility.class */
public class WriterUtility {
    public static final String START_COMMENT = "<!--";
    public static final String END_COMMENT = "-->";
    public static final String DEFAULT_ENCODING = " encoding=\"utf-8\"";
    public static final String DEFAULT_XMLDECL = "<?xml version=\"1.0\" ?>";
    public static final String DEFAULT_XML_VERSION = "1.0";
    public static final char CLOSE_START_TAG = '>';
    public static final char OPEN_START_TAG = '<';
    public static final String OPEN_END_TAG = "</";
    public static final char CLOSE_END_TAG = '>';
    public static final String START_CDATA = "<![CDATA[";
    public static final String END_CDATA = "]]>";
    public static final String CLOSE_EMPTY_ELEMENT = "/>";
    public static final String SPACE = " ";
    public static final String UTF_8 = "utf-8";
    static final boolean DEBUG_XML_CONTENT = false;
    boolean fEscapeCharacters;
    Writer fWriter;
    CharsetEncoder fEncoder;

    public WriterUtility() {
        this.fEscapeCharacters = true;
        this.fWriter = null;
        this.fEncoder = getDefaultEncoder();
    }

    public WriterUtility(Writer writer) {
        this.fEscapeCharacters = true;
        this.fWriter = null;
        this.fWriter = writer;
        if (writer instanceof OutputStreamWriter) {
            String charset = ((OutputStreamWriter) writer).getEncoding();
            if (charset != null) {
                this.fEncoder = Charset.forName(charset).newEncoder();
                return;
            }
            return;
        }
        if (writer instanceof FileWriter) {
            String charset2 = ((FileWriter) writer).getEncoding();
            if (charset2 != null) {
                this.fEncoder = Charset.forName(charset2).newEncoder();
                return;
            }
            return;
        }
        this.fEncoder = getDefaultEncoder();
    }

    public void setWriter(Writer writer) {
        this.fWriter = writer;
    }

    public void setEscapeCharacters(boolean escape) {
        this.fEscapeCharacters = escape;
    }

    public boolean getEscapeCharacters() {
        return this.fEscapeCharacters;
    }

    public void writeXMLContent(char[] content, int start, int length) throws IOException {
        writeXMLContent(content, start, length, getEscapeCharacters());
    }

    private void writeXMLContent(char[] content, int start, int length, boolean escapeCharacter) throws IOException {
        int end = start + length;
        int startWritePos = start;
        for (int index = start; index < end; index++) {
            char ch = content[index];
            if (this.fEncoder != null && !this.fEncoder.canEncode(ch)) {
                this.fWriter.write(content, startWritePos, index - startWritePos);
                this.fWriter.write(jdk.internal.util.xml.impl.XMLStreamWriterImpl.ENCODING_PREFIX);
                this.fWriter.write(Integer.toHexString(ch));
                this.fWriter.write(59);
                startWritePos = index + 1;
            }
            switch (ch) {
                case '&':
                    if (escapeCharacter) {
                        this.fWriter.write(content, startWritePos, index - startWritePos);
                        this.fWriter.write(SerializerConstants.ENTITY_AMP);
                        startWritePos = index + 1;
                        break;
                    } else {
                        break;
                    }
                case '<':
                    if (escapeCharacter) {
                        this.fWriter.write(content, startWritePos, index - startWritePos);
                        this.fWriter.write(SerializerConstants.ENTITY_LT);
                        startWritePos = index + 1;
                        break;
                    } else {
                        break;
                    }
                case '>':
                    if (escapeCharacter) {
                        this.fWriter.write(content, startWritePos, index - startWritePos);
                        this.fWriter.write(SerializerConstants.ENTITY_GT);
                        startWritePos = index + 1;
                        break;
                    } else {
                        break;
                    }
            }
        }
        this.fWriter.write(content, startWritePos, end - startWritePos);
    }

    public void writeXMLContent(String content) throws IOException {
        if (content == null || content.length() == 0) {
            return;
        }
        writeXMLContent(content.toCharArray(), 0, content.length());
    }

    public void writeXMLAttributeValue(String value) throws IOException {
        writeXMLContent(value.toCharArray(), 0, value.length(), true);
    }

    private CharsetEncoder getDefaultEncoder() {
        try {
            String encoding = SecuritySupport.getSystemProperty("file.encoding");
            if (encoding != null) {
                return Charset.forName(encoding).newEncoder();
            }
            return null;
        } catch (Exception e2) {
            return null;
        }
    }
}
