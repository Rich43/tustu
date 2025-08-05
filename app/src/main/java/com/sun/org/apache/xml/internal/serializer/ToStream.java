package com.sun.org.apache.xml.internal.serializer;

import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
import com.sun.org.apache.xml.internal.serializer.NamespaceMappings;
import com.sun.org.apache.xml.internal.serializer.utils.Utils;
import com.sun.org.apache.xml.internal.serializer.utils.WrappedRuntimeException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.xml.internal.JdkXmlUtils;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/ToStream.class */
public abstract class ToStream extends SerializerBase {
    private static final String COMMENT_BEGIN = "<!--";
    private static final String COMMENT_END = "-->";
    java.lang.reflect.Method m_canConvertMeth;
    protected CharInfo m_charInfo;
    boolean m_startNewLine;
    OutputStream m_outputStream;
    private boolean m_writer_set_by_user;
    protected BoolStack m_disableOutputEscapingStates = new BoolStack();
    EncodingInfo m_encodingInfo = new EncodingInfo(null, null);
    boolean m_triedToGetConverter = false;
    Object m_charToByteConverter = null;
    protected BoolStack m_preserves = new BoolStack();
    protected boolean m_ispreserve = false;
    protected boolean m_isprevtext = false;
    protected int m_maxCharacter = Encodings.getLastPrintable();
    protected char[] m_lineSep = SecuritySupport.getSystemProperty("line.separator").toCharArray();
    protected boolean m_lineSepUse = true;
    protected int m_lineSepLen = this.m_lineSep.length;
    boolean m_shouldFlush = true;
    protected boolean m_spaceBeforeClose = false;
    protected boolean m_inDoctype = false;
    boolean m_isUTF8 = false;
    protected boolean m_cdataStartCalled = false;
    private boolean m_expandDTDEntities = true;
    private char m_highSurrogate = 0;
    private boolean m_escaping = true;

    protected void closeCDATA() throws SAXException {
        try {
            this.m_writer.write("]]>");
            this.m_cdataTagOpen = false;
        } catch (IOException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler, com.sun.org.apache.xml.internal.serializer.DOMSerializer
    public void serialize(Node node) throws IOException {
        try {
            TreeWalker walker = new TreeWalker(this);
            walker.traverse(node);
        } catch (SAXException se) {
            throw new WrappedRuntimeException(se);
        }
    }

    static final boolean isUTF16Surrogate(char c2) {
        return (c2 & 64512) == 55296;
    }

    protected final void flushWriter() throws SAXException {
        Writer writer = this.m_writer;
        if (null != writer) {
            try {
                if (writer instanceof WriterToUTF8Buffered) {
                    if (this.m_shouldFlush) {
                        ((WriterToUTF8Buffered) writer).flush();
                    } else {
                        ((WriterToUTF8Buffered) writer).flushBuffer();
                    }
                }
                if (!(writer instanceof WriterToASCI) || this.m_shouldFlush) {
                    writer.flush();
                }
            } catch (IOException ioe) {
                throw new SAXException(ioe);
            }
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.Serializer
    public OutputStream getOutputStream() {
        return this.m_outputStream;
    }

    public void elementDecl(String name, String model) throws SAXException {
        if (this.m_inExternalDTD) {
            return;
        }
        try {
            Writer writer = this.m_writer;
            DTDprolog();
            writer.write("<!ELEMENT ");
            writer.write(name);
            writer.write(32);
            writer.write(model);
            writer.write(62);
            writer.write(this.m_lineSep, 0, this.m_lineSepLen);
        } catch (IOException e2) {
            throw new SAXException(e2);
        }
    }

    public void internalEntityDecl(String name, String value) throws SAXException {
        if (this.m_inExternalDTD) {
            return;
        }
        try {
            DTDprolog();
            outputEntityDecl(name, value);
        } catch (IOException e2) {
            throw new SAXException(e2);
        }
    }

    void outputEntityDecl(String name, String value) throws IOException {
        Writer writer = this.m_writer;
        writer.write("<!ENTITY ");
        writer.write(name);
        writer.write(" \"");
        writer.write(value);
        writer.write("\">");
        writer.write(this.m_lineSep, 0, this.m_lineSepLen);
    }

    protected final void outputLineSep() throws IOException {
        this.m_writer.write(this.m_lineSep, 0, this.m_lineSepLen);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase
    void setProp(String name, String val, boolean defaultVal) {
        if (val != null) {
            char first = getFirstCharLocName(name);
            switch (first) {
                case 'c':
                    if ("cdata-section-elements".equals(name)) {
                        addCdataSectionElements(val);
                        break;
                    }
                    break;
                case 'd':
                    if ("doctype-system".equals(name)) {
                        this.m_doctypeSystem = val;
                        break;
                    } else if ("doctype-public".equals(name)) {
                        this.m_doctypePublic = val;
                        if (val.startsWith("-//W3C//DTD XHTML")) {
                            this.m_spaceBeforeClose = true;
                            break;
                        }
                    }
                    break;
                case 'e':
                    String newEncoding = val;
                    if ("encoding".equals(name)) {
                        String possible_encoding = Encodings.getMimeEncoding(val);
                        if (possible_encoding != null) {
                            super.setProp("mime-name", possible_encoding, defaultVal);
                        }
                        String oldExplicitEncoding = getOutputPropertyNonDefault("encoding");
                        String oldDefaultEncoding = getOutputPropertyDefault("encoding");
                        if ((defaultVal && (oldDefaultEncoding == null || !oldDefaultEncoding.equalsIgnoreCase(newEncoding))) || (!defaultVal && (oldExplicitEncoding == null || !oldExplicitEncoding.equalsIgnoreCase(newEncoding)))) {
                            EncodingInfo encodingInfo = Encodings.getEncodingInfo(newEncoding);
                            if (newEncoding != null && encodingInfo.name == null) {
                                String msg = Utils.messages.createMessage("ER_ENCODING_NOT_SUPPORTED", new Object[]{newEncoding});
                                String msg2 = "Warning: encoding \"" + newEncoding + "\" not supported, using UTF-8";
                                try {
                                    Transformer tran = super.getTransformer();
                                    if (tran != null) {
                                        ErrorListener errHandler = tran.getErrorListener();
                                        if (null != errHandler && this.m_sourceLocator != null) {
                                            errHandler.warning(new TransformerException(msg, this.m_sourceLocator));
                                            errHandler.warning(new TransformerException(msg2, this.m_sourceLocator));
                                        } else {
                                            System.out.println(msg);
                                            System.out.println(msg2);
                                        }
                                    } else {
                                        System.out.println(msg);
                                        System.out.println(msg2);
                                    }
                                } catch (Exception e2) {
                                }
                                newEncoding = "UTF-8";
                                val = "UTF-8";
                                encodingInfo = Encodings.getEncodingInfo(newEncoding);
                            }
                            if (!defaultVal || oldExplicitEncoding == null) {
                                this.m_encodingInfo = encodingInfo;
                                if (newEncoding != null) {
                                    this.m_isUTF8 = newEncoding.equals("UTF-8");
                                }
                                OutputStream os = getOutputStream();
                                if (os != null) {
                                    Writer w2 = getWriter();
                                    String oldEncoding = getOutputProperty("encoding");
                                    if ((w2 == null || !this.m_writer_set_by_user) && !newEncoding.equalsIgnoreCase(oldEncoding)) {
                                        super.setProp(name, val, defaultVal);
                                        setOutputStreamInternal(os, false);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 'i':
                    if (OutputPropertiesFactory.S_KEY_INDENT_AMOUNT.equals(name)) {
                        setIndentAmount(Integer.parseInt(val));
                        break;
                    } else if ("indent".equals(name)) {
                        boolean b2 = "yes".equals(val);
                        this.m_doIndent = b2;
                        break;
                    }
                    break;
                case 'l':
                    if (OutputPropertiesFactory.S_KEY_LINE_SEPARATOR.equals(name)) {
                        this.m_lineSep = val.toCharArray();
                        this.m_lineSepLen = this.m_lineSep.length;
                        break;
                    }
                    break;
                case 'm':
                    if ("media-type".equals(name)) {
                        this.m_mediatype = val;
                        break;
                    }
                    break;
                case 'o':
                    if ("omit-xml-declaration".equals(name)) {
                        boolean b3 = "yes".equals(val);
                        this.m_shouldNotWriteXMLHeader = b3;
                        break;
                    }
                    break;
                case 's':
                    if ("standalone".equals(name)) {
                        if (defaultVal) {
                            setStandaloneInternal(val);
                            break;
                        } else {
                            this.m_standaloneWasSpecified = true;
                            setStandaloneInternal(val);
                            break;
                        }
                    }
                    break;
                case 'v':
                    if ("version".equals(name)) {
                        this.m_version = val;
                        break;
                    }
                    break;
            }
            super.setProp(name, val, defaultVal);
        }
    }

    public void setOutputFormat(Properties format) {
        boolean shouldFlush = this.m_shouldFlush;
        if (format != null) {
            Enumeration propNames = format.propertyNames();
            while (propNames.hasMoreElements()) {
                String key = (String) propNames.nextElement2();
                String value = format.getProperty(key);
                String explicitValue = (String) format.get(key);
                if (explicitValue == null && value != null) {
                    setOutputPropertyDefault(key, value);
                }
                if (explicitValue != null) {
                    setOutputProperty(key, explicitValue);
                }
            }
        }
        String entitiesFileName = (String) format.get(OutputPropertiesFactory.S_KEY_ENTITIES);
        if (null != entitiesFileName) {
            String method = (String) format.get("method");
            this.m_charInfo = CharInfo.getCharInfo(entitiesFileName, method);
        }
        this.m_shouldFlush = shouldFlush;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.Serializer
    public Properties getOutputFormat() {
        Properties def = new Properties();
        Set<String> s2 = getOutputPropDefaultKeys();
        for (String key : s2) {
            def.put(key, getOutputPropertyDefault(key));
        }
        Properties props = new Properties(def);
        Set<String> s3 = getOutputPropKeys();
        for (String key2 : s3) {
            String val = getOutputPropertyNonDefault(key2);
            if (val != null) {
                props.put(key2, val);
            }
        }
        return props;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.Serializer
    public void setWriter(Writer writer) {
        setWriterInternal(writer, true);
    }

    private void setWriterInternal(Writer writer, boolean setByUser) {
        this.m_writer_set_by_user = setByUser;
        this.m_writer = writer;
        if (this.m_tracer != null) {
            boolean noTracerYet = true;
            Writer writer2 = this.m_writer;
            while (true) {
                Object obj = writer2;
                if (!(obj instanceof WriterChain)) {
                    break;
                }
                if (obj instanceof SerializerTraceWriter) {
                    noTracerYet = false;
                    break;
                }
                writer2 = ((WriterChain) obj).getWriter();
            }
            if (noTracerYet) {
                this.m_writer = new SerializerTraceWriter(this.m_writer, this.m_tracer);
            }
        }
    }

    public boolean setLineSepUse(boolean use_sytem_line_break) {
        boolean oldValue = this.m_lineSepUse;
        this.m_lineSepUse = use_sytem_line_break;
        return oldValue;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.Serializer
    public void setOutputStream(OutputStream output) {
        setOutputStreamInternal(output, true);
    }

    private void setOutputStreamInternal(OutputStream output, boolean setByUser) {
        Writer osw;
        this.m_outputStream = output;
        String encoding = getOutputProperty("encoding");
        if ("UTF-8".equalsIgnoreCase(encoding)) {
            try {
                setWriterInternal(new WriterToUTF8Buffered(output), false);
                return;
            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
                return;
            }
        }
        if ("WINDOWS-1250".equals(encoding) || "US-ASCII".equals(encoding) || "ASCII".equals(encoding)) {
            setWriterInternal(new WriterToASCI(output), false);
            return;
        }
        if (encoding != null) {
            try {
                osw = Encodings.getWriter(output, encoding);
            } catch (UnsupportedEncodingException e3) {
                osw = null;
            }
            if (osw == null) {
                System.out.println("Warning: encoding \"" + encoding + "\" not supported, using UTF-8");
                setEncoding("UTF-8");
                try {
                    osw = Encodings.getWriter(output, "UTF-8");
                } catch (UnsupportedEncodingException e4) {
                    e4.printStackTrace();
                }
            }
            setWriterInternal(osw, false);
            return;
        }
        Writer osw2 = new OutputStreamWriter(output);
        setWriterInternal(osw2, false);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public boolean setEscaping(boolean escape) {
        boolean temp = this.m_escaping;
        this.m_escaping = escape;
        return temp;
    }

    protected void indent(int depth) throws IOException {
        if (this.m_startNewLine) {
            outputLineSep();
        }
        if (this.m_indentAmount > 0) {
            printSpace(depth * this.m_indentAmount);
        }
    }

    protected void indent() throws IOException {
        indent(this.m_elemContext.m_currentElemDepth);
    }

    private void printSpace(int n2) throws IOException {
        Writer writer = this.m_writer;
        for (int i2 = 0; i2 < n2; i2++) {
            writer.write(32);
        }
    }

    public void attributeDecl(String eName, String aName, String type, String valueDefault, String value) throws SAXException {
        if (this.m_inExternalDTD) {
            return;
        }
        try {
            Writer writer = this.m_writer;
            DTDprolog();
            writer.write("<!ATTLIST ");
            writer.write(eName);
            writer.write(32);
            writer.write(aName);
            writer.write(32);
            writer.write(type);
            if (valueDefault != null) {
                writer.write(32);
                writer.write(valueDefault);
            }
            writer.write(62);
            writer.write(this.m_lineSep, 0, this.m_lineSepLen);
        } catch (IOException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.Serializer
    public Writer getWriter() {
        return this.m_writer;
    }

    public void externalEntityDecl(String name, String publicId, String systemId) throws SAXException {
        try {
            DTDprolog();
            this.m_writer.write("<!ENTITY ");
            this.m_writer.write(name);
            this.m_writer.write(JdkXmlUtils.getDTDExternalDecl(publicId, systemId));
            this.m_writer.write(">");
            this.m_writer.write(this.m_lineSep, 0, this.m_lineSepLen);
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    protected boolean escapingNotNeeded(char ch) {
        boolean ret;
        if (ch < 127) {
            if (ch >= ' ' || '\n' == ch || '\r' == ch || '\t' == ch) {
                ret = true;
            } else {
                ret = false;
            }
        } else {
            ret = this.m_encodingInfo.isInEncoding(ch);
        }
        return ret;
    }

    protected int writeUTF16Surrogate(char c2, char[] ch, int i2, int end) throws SAXException, IOException {
        char high;
        char low;
        int status = -1;
        if (i2 + 1 >= end) {
            this.m_highSurrogate = c2;
            return -1;
        }
        if (this.m_highSurrogate == 0) {
            high = c2;
            low = ch[i2 + 1];
            status = 0;
        } else {
            high = this.m_highSurrogate;
            low = c2;
            this.m_highSurrogate = (char) 0;
        }
        if (!Encodings.isLowUTF16Surrogate(low)) {
            throwIOE(high, low);
        }
        Writer writer = this.m_writer;
        if (this.m_encodingInfo.isInEncoding(high, low)) {
            writer.write(new char[]{high, low}, 0, 2);
        } else {
            String encoding = getEncoding();
            if (encoding != null) {
                status = writeCharRef(writer, high, low);
            } else {
                writer.write(new char[]{high, low}, 0, 2);
            }
        }
        return status;
    }

    protected int accumDefaultEntity(Writer writer, char ch, int i2, char[] chars, int len, boolean fromTextNode, boolean escLF) throws IOException {
        if (!escLF && '\n' == ch) {
            writer.write(this.m_lineSep, 0, this.m_lineSepLen);
        } else if ((fromTextNode && this.m_charInfo.isSpecialTextChar(ch)) || (!fromTextNode && this.m_charInfo.isSpecialAttrChar(ch))) {
            String outputStringForChar = this.m_charInfo.getOutputStringForChar(ch);
            if (null != outputStringForChar) {
                writer.write(outputStringForChar);
            } else {
                return i2;
            }
        } else {
            return i2;
        }
        return i2 + 1;
    }

    void writeNormalizedChars(char[] ch, int start, int length, boolean isCData, boolean useSystemLineSeparator) throws SAXException, IOException {
        Writer writer = this.m_writer;
        int end = start + length;
        int i2 = start;
        while (i2 < end) {
            char c2 = ch[i2];
            if ('\n' == c2 && useSystemLineSeparator) {
                writer.write(this.m_lineSep, 0, this.m_lineSepLen);
            } else if (isCData && !escapingNotNeeded(c2)) {
                i2 = handleEscaping(writer, c2, ch, i2, end);
            } else if (isCData && i2 < end - 2 && ']' == c2 && ']' == ch[i2 + 1] && '>' == ch[i2 + 2]) {
                writer.write(SerializerConstants.CDATA_CONTINUE);
                i2 += 2;
            } else if (escapingNotNeeded(c2)) {
                if (isCData && !this.m_cdataTagOpen) {
                    writer.write("<![CDATA[");
                    this.m_cdataTagOpen = true;
                }
                writer.write(c2);
            } else {
                i2 = handleEscaping(writer, c2, ch, i2, end);
            }
            i2++;
        }
    }

    private int handleEscaping(Writer writer, char c2, char[] ch, int i2, int end) throws SAXException, IOException {
        if (Encodings.isHighUTF16Surrogate(c2) || Encodings.isLowUTF16Surrogate(c2)) {
            if (writeUTF16Surrogate(c2, ch, i2, end) >= 0 && Encodings.isHighUTF16Surrogate(c2)) {
                i2++;
            }
        } else {
            writeCharRef(writer, c2);
        }
        return i2;
    }

    public void endNonEscaping() throws SAXException {
        this.m_disableOutputEscapingStates.pop();
    }

    public void startNonEscaping() throws SAXException {
        this.m_disableOutputEscapingStates.push(true);
    }

    protected void cdata(char[] ch, int start, int length) throws SAXException {
        try {
            if (this.m_elemContext.m_startTagOpen) {
                closeStartTag();
                this.m_elemContext.m_startTagOpen = false;
            }
            this.m_ispreserve = true;
            if (!this.m_cdataTagOpen && shouldIndent()) {
                indent();
            }
            boolean writeCDataBrackets = length >= 1 && escapingNotNeeded(ch[start]);
            if (writeCDataBrackets && !this.m_cdataTagOpen) {
                this.m_writer.write("<![CDATA[");
                this.m_cdataTagOpen = true;
            }
            if (isEscapingDisabled()) {
                charactersRaw(ch, start, length);
            } else {
                writeNormalizedChars(ch, start, length, true, this.m_lineSepUse);
            }
            if (writeCDataBrackets && ch[(start + length) - 1] == ']') {
                closeCDATA();
            }
            if (this.m_tracer != null) {
                super.fireCDATAEvent(ch, start, length);
            }
        } catch (IOException ioe) {
            throw new SAXException(Utils.messages.createMessage("ER_OIERROR", null), ioe);
        }
    }

    private boolean isEscapingDisabled() {
        return this.m_disableOutputEscapingStates.peekOrFalse();
    }

    protected void charactersRaw(char[] ch, int start, int length) throws SAXException {
        if (this.m_inEntityRef) {
            return;
        }
        try {
            if (this.m_elemContext.m_startTagOpen) {
                closeStartTag();
                this.m_elemContext.m_startTagOpen = false;
            }
            this.m_ispreserve = true;
            this.m_writer.write(ch, start, length);
        } catch (IOException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void characters(char[] chars, int start, int length) throws SAXException {
        char ch2;
        char ch1;
        if (length != 0) {
            if (this.m_inEntityRef && !this.m_expandDTDEntities) {
                return;
            }
            if (this.m_elemContext.m_startTagOpen) {
                closeStartTag();
                this.m_elemContext.m_startTagOpen = false;
            } else if (this.m_needToCallStartDocument) {
                startDocumentInternal();
            }
            if (this.m_cdataStartCalled || this.m_elemContext.m_isCdataSection) {
                cdata(chars, start, length);
                return;
            }
            if (this.m_cdataTagOpen) {
                closeCDATA();
            }
            if (this.m_disableOutputEscapingStates.peekOrFalse() || !this.m_escaping) {
                charactersRaw(chars, start, length);
                if (this.m_tracer != null) {
                    super.fireCharEvent(chars, start, length);
                    return;
                }
                return;
            }
            if (this.m_elemContext.m_startTagOpen) {
                closeStartTag();
                this.m_elemContext.m_startTagOpen = false;
            }
            try {
                int end = start + length;
                int lastDirty = start - 1;
                int i2 = start;
                while (i2 < end && ((ch1 = chars[i2]) == ' ' || ((ch1 == '\n' && this.m_lineSepUse) || ch1 == '\r' || ch1 == '\t'))) {
                    if (!this.m_charInfo.isTextASCIIClean(ch1)) {
                        lastDirty = processDirty(chars, end, i2, ch1, lastDirty, true);
                        i2 = lastDirty;
                    }
                    i2++;
                }
                if (i2 < end) {
                    this.m_ispreserve = true;
                }
                boolean isXML10 = "1.0".equals(getVersion());
                while (i2 < end) {
                    while (i2 < end && (ch2 = chars[i2]) < 127 && this.m_charInfo.isTextASCIIClean(ch2)) {
                        i2++;
                    }
                    if (i2 == end) {
                        break;
                    }
                    char ch = chars[i2];
                    if ((isCharacterInC0orC1Range(ch) || ((!isXML10 && isNELorLSEPCharacter(ch)) || !escapingNotNeeded(ch) || this.m_charInfo.isSpecialTextChar(ch))) && '\"' != ch) {
                        lastDirty = processDirty(chars, end, i2, ch, lastDirty, true);
                        i2 = lastDirty;
                    }
                    i2++;
                }
                int startClean = lastDirty + 1;
                if (i2 > startClean) {
                    int lengthClean = i2 - startClean;
                    this.m_writer.write(chars, startClean, lengthClean);
                }
                this.m_isprevtext = true;
                if (this.m_tracer != null) {
                    super.fireCharEvent(chars, start, length);
                }
            } catch (IOException e2) {
                throw new SAXException(e2);
            }
        }
    }

    private static boolean isCharacterInC0orC1Range(char ch) {
        if (ch == '\t' || ch == '\n' || ch == '\r') {
            return false;
        }
        return (ch >= 127 && ch <= 159) || (ch >= 1 && ch <= 31);
    }

    private static boolean isNELorLSEPCharacter(char ch) {
        return ch == 133 || ch == 8232;
    }

    private int processDirty(char[] chars, int end, int i2, char ch, int lastDirty, boolean fromTextNode) throws SAXException, IOException {
        int startClean = lastDirty + 1;
        if (i2 > startClean) {
            int lengthClean = i2 - startClean;
            this.m_writer.write(chars, startClean, lengthClean);
        }
        if ('\n' == ch && fromTextNode) {
            this.m_writer.write(this.m_lineSep, 0, this.m_lineSepLen);
        } else {
            i2 = accumDefaultEscape(this.m_writer, ch, i2, chars, end, fromTextNode, false) - 1;
        }
        return i2;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void characters(String s2) throws SAXException {
        if (this.m_inEntityRef && !this.m_expandDTDEntities) {
            return;
        }
        int length = s2.length();
        if (length > this.m_charsBuff.length) {
            this.m_charsBuff = new char[(length * 2) + 1];
        }
        s2.getChars(0, length, this.m_charsBuff, 0);
        characters(this.m_charsBuff, 0, length);
    }

    protected int accumDefaultEscape(Writer writer, char ch, int i2, char[] chars, int len, boolean fromTextNode, boolean escLF) throws SAXException, IOException {
        int pos = accumDefaultEntity(writer, ch, i2, chars, len, fromTextNode, escLF);
        if (i2 == pos) {
            if (this.m_highSurrogate != 0) {
                if (!Encodings.isLowUTF16Surrogate(ch)) {
                    throwIOE(this.m_highSurrogate, ch);
                }
                writeCharRef(writer, this.m_highSurrogate, ch);
                this.m_highSurrogate = (char) 0;
                return pos + 1;
            }
            if (Encodings.isHighUTF16Surrogate(ch)) {
                if (i2 + 1 >= len) {
                    this.m_highSurrogate = ch;
                    pos++;
                } else {
                    char next = chars[i2 + 1];
                    if (!Encodings.isLowUTF16Surrogate(next)) {
                        throwIOE(ch, next);
                    }
                    writeCharRef(writer, ch, next);
                    pos += 2;
                }
            } else {
                if (isCharacterInC0orC1Range(ch) || (SerializerConstants.XMLVERSION11.equals(getVersion()) && isNELorLSEPCharacter(ch))) {
                    writeCharRef(writer, ch);
                } else if ((!escapingNotNeeded(ch) || ((fromTextNode && this.m_charInfo.isSpecialTextChar(ch)) || (!fromTextNode && this.m_charInfo.isSpecialAttrChar(ch)))) && this.m_elemContext.m_currentElemDepth > 0) {
                    writeCharRef(writer, ch);
                } else {
                    writer.write(ch);
                }
                pos++;
            }
        }
        return pos;
    }

    private void writeCharRef(Writer writer, char c2) throws SAXException, IOException {
        if (this.m_cdataTagOpen) {
            closeCDATA();
        }
        writer.write("&#");
        writer.write(Integer.toString(c2));
        writer.write(59);
    }

    private int writeCharRef(Writer writer, char high, char low) throws SAXException, IOException {
        if (this.m_cdataTagOpen) {
            closeCDATA();
        }
        int codePoint = Encodings.toCodePoint(high, low);
        writer.write("&#");
        writer.write(Integer.toString(codePoint));
        writer.write(59);
        return codePoint;
    }

    private void throwIOE(char ch, char next) throws IOException {
        throw new IOException(Utils.messages.createMessage("ER_INVALID_UTF16_SURROGATE", new Object[]{Integer.toHexString(ch) + " " + Integer.toHexString(next)}));
    }

    @Override // org.xml.sax.ContentHandler
    public void startElement(String namespaceURI, String localName, String name, Attributes atts) throws SAXException {
        if (this.m_inEntityRef) {
            return;
        }
        if (this.m_needToCallStartDocument) {
            startDocumentInternal();
            this.m_needToCallStartDocument = false;
        } else if (this.m_cdataTagOpen) {
            closeCDATA();
        }
        try {
            if (true == this.m_needToOutputDocTypeDecl && null != getDoctypeSystem()) {
                outputDocTypeDecl(name, true);
            }
            this.m_needToOutputDocTypeDecl = false;
            if (this.m_elemContext.m_startTagOpen) {
                closeStartTag();
                this.m_elemContext.m_startTagOpen = false;
            }
            if (namespaceURI != null) {
                ensurePrefixIsDeclared(namespaceURI, name);
            }
            this.m_ispreserve = false;
            if (shouldIndent() && this.m_startNewLine) {
                indent();
            }
            this.m_startNewLine = true;
            Writer writer = this.m_writer;
            writer.write(60);
            writer.write(name);
            if (atts != null) {
                addAttributes(atts);
            }
            this.m_elemContext = this.m_elemContext.push(namespaceURI, localName, name);
            this.m_isprevtext = false;
            if (this.m_tracer != null) {
                firePseudoAttributes();
            }
        } catch (IOException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void startElement(String elementNamespaceURI, String elementLocalName, String elementName) throws SAXException {
        startElement(elementNamespaceURI, elementLocalName, elementName, null);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void startElement(String elementName) throws SAXException {
        startElement(null, null, elementName, null);
    }

    void outputDocTypeDecl(String name, boolean closeDecl) throws SAXException {
        if (this.m_cdataTagOpen) {
            closeCDATA();
        }
        try {
            Writer writer = this.m_writer;
            writer.write("<!DOCTYPE ");
            writer.write(name);
            String systemId = getDoctypeSystem();
            writer.write(JdkXmlUtils.getDTDExternalDecl(getDoctypePublic(), systemId));
            if (null != systemId && closeDecl) {
                writer.write(">");
                writer.write(this.m_lineSep, 0, this.m_lineSepLen);
            }
        } catch (IOException e2) {
            throw new SAXException(e2);
        }
    }

    public void processAttributes(Writer writer, int nAttrs) throws SAXException, IOException {
        String encoding = getEncoding();
        for (int i2 = 0; i2 < nAttrs; i2++) {
            String name = this.m_attributes.getQName(i2);
            String value = this.m_attributes.getValue(i2);
            writer.write(32);
            writer.write(name);
            writer.write("=\"");
            writeAttrString(writer, value, encoding);
            writer.write(34);
        }
    }

    public void writeAttrString(Writer writer, String string, String encoding) throws SAXException, IOException {
        int len = string.length();
        if (len > this.m_attrBuff.length) {
            this.m_attrBuff = new char[(len * 2) + 1];
        }
        string.getChars(0, len, this.m_attrBuff, 0);
        char[] stringChars = this.m_attrBuff;
        int i2 = 0;
        while (i2 < len) {
            char ch = stringChars[i2];
            if (escapingNotNeeded(ch) && !this.m_charInfo.isSpecialAttrChar(ch)) {
                writer.write(ch);
                i2++;
            } else {
                i2 = accumDefaultEscape(writer, ch, i2, stringChars, len, false, true);
            }
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void endElement(String namespaceURI, String localName, String name) throws SAXException {
        if (this.m_inEntityRef) {
            return;
        }
        this.m_prefixMap.popNamespaces(this.m_elemContext.m_currentElemDepth, null);
        try {
            Writer writer = this.m_writer;
            if (this.m_elemContext.m_startTagOpen) {
                if (this.m_tracer != null) {
                    super.fireStartElem(this.m_elemContext.m_elementName);
                }
                int nAttrs = this.m_attributes.getLength();
                if (nAttrs > 0) {
                    processAttributes(this.m_writer, nAttrs);
                    this.m_attributes.clear();
                }
                if (this.m_spaceBeforeClose) {
                    writer.write(" />");
                } else {
                    writer.write("/>");
                }
            } else {
                if (this.m_cdataTagOpen) {
                    closeCDATA();
                }
                if (shouldIndent()) {
                    indent(this.m_elemContext.m_currentElemDepth - 1);
                }
                writer.write(60);
                writer.write(47);
                writer.write(name);
                writer.write(62);
            }
            if (!this.m_elemContext.m_startTagOpen && this.m_doIndent) {
                this.m_ispreserve = this.m_preserves.isEmpty() ? false : this.m_preserves.pop();
            }
            this.m_isprevtext = false;
            if (this.m_tracer != null) {
                super.fireEndElem(name);
            }
            this.m_elemContext = this.m_elemContext.m_prev;
        } catch (IOException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public void endElement(String name) throws SAXException {
        endElement(null, null, name);
    }

    @Override // org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        startPrefixMapping(prefix, uri, true);
    }

    @Override // com.sun.org.apache.xml.internal.serializer.ExtendedContentHandler
    public boolean startPrefixMapping(String prefix, String uri, boolean shouldFlush) throws SAXException {
        int pushDepth;
        if (shouldFlush) {
            flushPending();
            pushDepth = this.m_elemContext.m_currentElemDepth + 1;
        } else {
            pushDepth = this.m_elemContext.m_currentElemDepth;
        }
        boolean pushed = this.m_prefixMap.pushNamespace(prefix, uri, pushDepth);
        if (pushed) {
            if ("".equals(prefix)) {
                addAttributeAlways("http://www.w3.org/2000/xmlns/", "xmlns", "xmlns", "CDATA", uri, false);
            } else if (!"".equals(uri)) {
                String name = "xmlns:" + prefix;
                addAttributeAlways("http://www.w3.org/2000/xmlns/", prefix, name, "CDATA", uri, false);
            }
        }
        return pushed;
    }

    public void comment(char[] ch, int start, int length) throws SAXException {
        if (this.m_inEntityRef) {
            return;
        }
        if (this.m_elemContext.m_startTagOpen) {
            closeStartTag();
            this.m_elemContext.m_startTagOpen = false;
        } else if (this.m_needToCallStartDocument) {
            startDocumentInternal();
            this.m_needToCallStartDocument = false;
        }
        try {
            if (shouldIndent() && this.m_isStandalone) {
                indent();
            }
            int limit = start + length;
            boolean wasDash = false;
            if (this.m_cdataTagOpen) {
                closeCDATA();
            }
            if (shouldIndent() && !this.m_isStandalone) {
                indent();
            }
            Writer writer = this.m_writer;
            writer.write("<!--");
            for (int i2 = start; i2 < limit; i2++) {
                if (wasDash && ch[i2] == '-') {
                    writer.write(ch, start, i2 - start);
                    writer.write(" -");
                    start = i2 + 1;
                }
                wasDash = ch[i2] == '-';
            }
            if (length > 0) {
                int remainingChars = limit - start;
                if (remainingChars > 0) {
                    writer.write(ch, start, remainingChars);
                }
                if (ch[limit - 1] == '-') {
                    writer.write(32);
                }
            }
            writer.write("-->");
            this.m_startNewLine = true;
            if (this.m_tracer != null) {
                super.fireCommentEvent(ch, start, length);
            }
        } catch (IOException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endCDATA() throws SAXException {
        if (this.m_cdataTagOpen) {
            closeCDATA();
        }
        this.m_cdataStartCalled = false;
    }

    public void endDTD() throws SAXException {
        try {
            if (this.m_needToCallStartDocument) {
                return;
            }
            if (this.m_needToOutputDocTypeDecl) {
                outputDocTypeDecl(this.m_elemContext.m_elementName, false);
                this.m_needToOutputDocTypeDecl = false;
            }
            Writer writer = this.m_writer;
            if (!this.m_inDoctype) {
                writer.write("]>");
            } else {
                writer.write(62);
            }
            writer.write(this.m_lineSep, 0, this.m_lineSepLen);
        } catch (IOException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void endPrefixMapping(String prefix) throws SAXException {
    }

    @Override // org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        if (0 == length) {
            return;
        }
        characters(ch, start, length);
    }

    @Override // org.xml.sax.ContentHandler
    public void skippedEntity(String name) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startCDATA() throws SAXException {
        this.m_cdataStartCalled = true;
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startEntity(String name) throws SAXException {
        if (name.equals("[dtd]")) {
            this.m_inExternalDTD = true;
        }
        if (!this.m_expandDTDEntities && !this.m_inExternalDTD) {
            startNonEscaping();
            characters("&" + name + ';');
            endNonEscaping();
        }
        this.m_inEntityRef = true;
    }

    protected void closeStartTag() throws SAXException {
        if (this.m_elemContext.m_startTagOpen) {
            try {
                if (this.m_tracer != null) {
                    super.fireStartElem(this.m_elemContext.m_elementName);
                }
                int nAttrs = this.m_attributes.getLength();
                if (nAttrs > 0) {
                    processAttributes(this.m_writer, nAttrs);
                    this.m_attributes.clear();
                }
                this.m_writer.write(62);
                if (this.m_StringOfCDATASections != null) {
                    this.m_elemContext.m_isCdataSection = isCdataSection();
                }
                if (this.m_doIndent) {
                    this.m_isprevtext = false;
                    this.m_preserves.push(this.m_ispreserve);
                }
            } catch (IOException e2) {
                throw new SAXException(e2);
            }
        }
    }

    public void startDTD(String name, String publicId, String systemId) throws SAXException {
        setDoctypeSystem(systemId);
        setDoctypePublic(publicId);
        this.m_elemContext.m_elementName = name;
        this.m_inDoctype = true;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public int getIndentAmount() {
        return this.m_indentAmount;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public void setIndentAmount(int m_indentAmount) {
        this.m_indentAmount = m_indentAmount;
    }

    protected boolean shouldIndent() {
        return this.m_doIndent && !this.m_ispreserve && !this.m_isprevtext && (this.m_elemContext.m_currentElemDepth > 0 || this.m_isStandalone);
    }

    private void setCdataSectionElements(String key, Properties props) {
        String s2 = props.getProperty(key);
        if (null != s2) {
            ArrayList<String> v2 = new ArrayList<>();
            int l2 = s2.length();
            boolean inCurly = false;
            StringBuilder buf = new StringBuilder();
            for (int i2 = 0; i2 < l2; i2++) {
                char c2 = s2.charAt(i2);
                if (Character.isWhitespace(c2)) {
                    if (!inCurly) {
                        if (buf.length() > 0) {
                            addCdataSectionElement(buf.toString(), v2);
                            buf.setLength(0);
                        }
                    }
                } else if ('{' == c2) {
                    inCurly = true;
                } else if ('}' == c2) {
                    inCurly = false;
                }
                buf.append(c2);
            }
            if (buf.length() > 0) {
                addCdataSectionElement(buf.toString(), v2);
                buf.setLength(0);
            }
            setCdataSectionElements(v2);
        }
    }

    private void addCdataSectionElement(String URI_and_localName, ArrayList<String> v2) {
        StringTokenizer tokenizer = new StringTokenizer(URI_and_localName, "{}", false);
        String s1 = tokenizer.nextToken();
        String s2 = tokenizer.hasMoreTokens() ? tokenizer.nextToken() : null;
        if (null == s2) {
            v2.add(null);
            v2.add(s1);
        } else {
            v2.add(s1);
            v2.add(s2);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setCdataSectionElements(ArrayList<String> URI_and_localNames) {
        int len;
        if (URI_and_localNames != null && (len = URI_and_localNames.size() - 1) > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i2 = 0; i2 < len; i2 += 2) {
                if (i2 != 0) {
                    sb.append(' ');
                }
                String uri = URI_and_localNames.get(i2);
                String localName = URI_and_localNames.get(i2 + 1);
                if (uri != null) {
                    sb.append('{');
                    sb.append(uri);
                    sb.append('}');
                }
                sb.append(localName);
            }
            this.m_StringOfCDATASections = sb.toString();
        }
        initCdataElems(this.m_StringOfCDATASections);
    }

    protected String ensureAttributesNamespaceIsDeclared(String ns, String localName, String rawName) throws SAXException {
        if (ns != null && ns.length() > 0) {
            int index = rawName.indexOf(CallSiteDescriptor.TOKEN_DELIMITER);
            String prefixFromRawName = index < 0 ? "" : rawName.substring(0, index);
            if (index > 0) {
                String uri = this.m_prefixMap.lookupNamespace(prefixFromRawName);
                if (uri != null && uri.equals(ns)) {
                    return null;
                }
                startPrefixMapping(prefixFromRawName, ns, false);
                addAttribute("http://www.w3.org/2000/xmlns/", prefixFromRawName, "xmlns:" + prefixFromRawName, "CDATA", ns, false);
                return prefixFromRawName;
            }
            String prefix = this.m_prefixMap.lookupPrefix(ns);
            if (prefix == null) {
                prefix = this.m_prefixMap.generateNextPrefix();
                startPrefixMapping(prefix, ns, false);
                addAttribute("http://www.w3.org/2000/xmlns/", prefix, "xmlns:" + prefix, "CDATA", ns, false);
            }
            return prefix;
        }
        return null;
    }

    void ensurePrefixIsDeclared(String ns, String rawName) throws SAXException {
        if (ns != null && ns.length() > 0) {
            int index = rawName.indexOf(CallSiteDescriptor.TOKEN_DELIMITER);
            boolean no_prefix = index < 0;
            String prefix = no_prefix ? "" : rawName.substring(0, index);
            if (null != prefix) {
                String foundURI = this.m_prefixMap.lookupNamespace(prefix);
                if (null == foundURI || !foundURI.equals(ns)) {
                    startPrefixMapping(prefix, ns);
                    addAttributeAlways("http://www.w3.org/2000/xmlns/", no_prefix ? "xmlns" : prefix, no_prefix ? "xmlns" : "xmlns:" + prefix, "CDATA", ns, false);
                }
            }
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public void flushPending() throws SAXException {
        if (this.m_needToCallStartDocument) {
            startDocumentInternal();
            this.m_needToCallStartDocument = false;
        }
        if (this.m_elemContext.m_startTagOpen) {
            closeStartTag();
            this.m_elemContext.m_startTagOpen = false;
        }
        if (this.m_cdataTagOpen) {
            closeCDATA();
            this.m_cdataTagOpen = false;
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public void setContentHandler(ContentHandler ch) {
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase
    public boolean addAttributeAlways(String uri, String localName, String rawName, String type, String value, boolean xslAttribute) {
        boolean was_added;
        int index = this.m_attributes.getIndex(rawName);
        if (index >= 0) {
            String old_value = null;
            if (this.m_tracer != null) {
                old_value = this.m_attributes.getValue(index);
                if (value.equals(old_value)) {
                    old_value = null;
                }
            }
            this.m_attributes.setValue(index, value);
            was_added = false;
            if (old_value != null) {
                firePseudoAttributes();
            }
        } else {
            if (xslAttribute) {
                int colonIndex = rawName.indexOf(58);
                if (colonIndex > 0) {
                    NamespaceMappings.MappingRecord existing_mapping = this.m_prefixMap.getMappingFromPrefix(rawName.substring(0, colonIndex));
                    if (existing_mapping != null && existing_mapping.m_declarationDepth == this.m_elemContext.m_currentElemDepth && !existing_mapping.m_uri.equals(uri)) {
                        String prefix = this.m_prefixMap.lookupPrefix(uri);
                        if (prefix == null) {
                            prefix = this.m_prefixMap.generateNextPrefix();
                        }
                        rawName = prefix + ':' + localName;
                    }
                }
                try {
                    ensureAttributesNamespaceIsDeclared(uri, localName, rawName);
                } catch (SAXException e2) {
                    e2.printStackTrace();
                }
            }
            this.m_attributes.addAttribute(uri, localName, rawName, type, value);
            was_added = true;
            if (this.m_tracer != null) {
                firePseudoAttributes();
            }
        }
        return was_added;
    }

    protected void firePseudoAttributes() {
        if (this.m_tracer != null) {
            try {
                this.m_writer.flush();
                StringBuffer sb = new StringBuffer();
                int nAttrs = this.m_attributes.getLength();
                if (nAttrs > 0) {
                    Writer writer = new WritertoStringBuffer(sb);
                    processAttributes(writer, nAttrs);
                }
                sb.append('>');
                char[] ch = sb.toString().toCharArray();
                this.m_tracer.fireGenerateEvent(11, ch, 0, ch.length);
            } catch (IOException e2) {
            } catch (SAXException e3) {
            }
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/ToStream$WritertoStringBuffer.class */
    private class WritertoStringBuffer extends Writer {
        private final StringBuffer m_stringbuf;

        WritertoStringBuffer(StringBuffer sb) {
            this.m_stringbuf = sb;
        }

        @Override // java.io.Writer
        public void write(char[] arg0, int arg1, int arg2) throws IOException {
            this.m_stringbuf.append(arg0, arg1, arg2);
        }

        @Override // java.io.Writer, java.io.Flushable
        public void flush() throws IOException {
        }

        @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
        }

        @Override // java.io.Writer
        public void write(int i2) {
            this.m_stringbuf.append((char) i2);
        }

        @Override // java.io.Writer, com.sun.org.apache.xml.internal.serializer.WriterChain
        public void write(String s2) {
            this.m_stringbuf.append(s2);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public void setTransformer(Transformer transformer) {
        super.setTransformer(transformer);
        if (this.m_tracer != null && !(this.m_writer instanceof SerializerTraceWriter)) {
            this.m_writer = new SerializerTraceWriter(this.m_writer, this.m_tracer);
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.Serializer
    public boolean reset() {
        boolean wasReset = false;
        if (super.reset()) {
            resetToStream();
            wasReset = true;
        }
        return wasReset;
    }

    private void resetToStream() {
        this.m_cdataStartCalled = false;
        this.m_disableOutputEscapingStates.clear();
        this.m_escaping = true;
        this.m_inDoctype = false;
        this.m_ispreserve = false;
        this.m_ispreserve = false;
        this.m_isprevtext = false;
        this.m_isUTF8 = false;
        this.m_preserves.clear();
        this.m_shouldFlush = true;
        this.m_spaceBeforeClose = false;
        this.m_startNewLine = false;
        this.m_lineSepUse = true;
        this.m_expandDTDEntities = true;
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.XSLOutputAttributes
    public void setEncoding(String encoding) {
        setOutputProperty("encoding", encoding);
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/ToStream$BoolStack.class */
    static final class BoolStack {
        private boolean[] m_values;
        private int m_allocatedSize;
        private int m_index;

        public BoolStack() {
            this(32);
        }

        public BoolStack(int size) {
            this.m_allocatedSize = size;
            this.m_values = new boolean[size];
            this.m_index = -1;
        }

        public final int size() {
            return this.m_index + 1;
        }

        public final void clear() {
            this.m_index = -1;
        }

        public final boolean push(boolean val) {
            if (this.m_index == this.m_allocatedSize - 1) {
                grow();
            }
            boolean[] zArr = this.m_values;
            int i2 = this.m_index + 1;
            this.m_index = i2;
            zArr[i2] = val;
            return val;
        }

        public final boolean pop() {
            boolean[] zArr = this.m_values;
            int i2 = this.m_index;
            this.m_index = i2 - 1;
            return zArr[i2];
        }

        public final boolean popAndTop() {
            this.m_index--;
            if (this.m_index >= 0) {
                return this.m_values[this.m_index];
            }
            return false;
        }

        public final void setTop(boolean b2) {
            this.m_values[this.m_index] = b2;
        }

        public final boolean peek() {
            return this.m_values[this.m_index];
        }

        public final boolean peekOrFalse() {
            if (this.m_index > -1) {
                return this.m_values[this.m_index];
            }
            return false;
        }

        public final boolean peekOrTrue() {
            if (this.m_index > -1) {
                return this.m_values[this.m_index];
            }
            return true;
        }

        public boolean isEmpty() {
            return this.m_index == -1;
        }

        private void grow() {
            this.m_allocatedSize *= 2;
            boolean[] newVector = new boolean[this.m_allocatedSize];
            System.arraycopy(this.m_values, 0, newVector, 0, this.m_index + 1);
            this.m_values = newVector;
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, org.xml.sax.DTDHandler
    public void notationDecl(String name, String pubID, String sysID) throws SAXException {
        try {
            DTDprolog();
            this.m_writer.write("<!NOTATION ");
            this.m_writer.write(name);
            this.m_writer.write(JdkXmlUtils.getDTDExternalDecl(pubID, sysID));
            this.m_writer.write(">");
            this.m_writer.write(this.m_lineSep, 0, this.m_lineSepLen);
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, org.xml.sax.DTDHandler
    public void unparsedEntityDecl(String name, String pubID, String sysID, String notationName) throws SAXException {
        try {
            DTDprolog();
            this.m_writer.write("<!ENTITY ");
            this.m_writer.write(name);
            this.m_writer.write(JdkXmlUtils.getDTDExternalDecl(pubID, sysID));
            this.m_writer.write(" NDATA ");
            this.m_writer.write(notationName);
            this.m_writer.write(" >");
            this.m_writer.write(this.m_lineSep, 0, this.m_lineSepLen);
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    private void DTDprolog() throws SAXException, IOException {
        Writer writer = this.m_writer;
        if (this.m_needToOutputDocTypeDecl) {
            outputDocTypeDecl(this.m_elemContext.m_elementName, false);
            this.m_needToOutputDocTypeDecl = false;
        }
        if (this.m_inDoctype) {
            writer.write(" [");
            writer.write(this.m_lineSep, 0, this.m_lineSepLen);
            this.m_inDoctype = false;
        }
    }

    @Override // com.sun.org.apache.xml.internal.serializer.SerializerBase, com.sun.org.apache.xml.internal.serializer.SerializationHandler
    public void setDTDEntityExpansion(boolean expand) {
        this.m_expandDTDEntities = expand;
    }

    public void addCdataSectionElements(String URI_and_localNames) {
        if (URI_and_localNames != null) {
            initCdataElems(URI_and_localNames);
        }
        if (this.m_StringOfCDATASections == null) {
            this.m_StringOfCDATASections = URI_and_localNames;
        } else {
            this.m_StringOfCDATASections += " " + URI_and_localNames;
        }
    }
}
