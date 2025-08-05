package java.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javafx.fxml.FXMLLoader;
import jdk.internal.util.xml.BasicXmlPropertiesProvider;
import sun.util.spi.XmlPropertiesProvider;

/* loaded from: rt.jar:java/util/Properties.class */
public class Properties extends Hashtable<Object, Object> {
    private static final long serialVersionUID = 4112578634029874840L;
    protected Properties defaults;
    private static final char[] hexDigit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public Properties() {
        this(null);
    }

    public Properties(Properties properties) {
        this.defaults = properties;
    }

    public synchronized Object setProperty(String str, String str2) {
        return put(str, str2);
    }

    public synchronized void load(Reader reader) throws IOException {
        load0(new LineReader(reader));
    }

    public synchronized void load(InputStream inputStream) throws IOException {
        load0(new LineReader(inputStream));
    }

    private void load0(LineReader lineReader) throws IOException {
        boolean z2;
        char[] cArr = new char[1024];
        while (true) {
            int line = lineReader.readLine();
            if (line >= 0) {
                int i2 = 0;
                int i3 = line;
                boolean z3 = false;
                boolean z4 = false;
                while (true) {
                    if (i2 >= line) {
                        break;
                    }
                    char c2 = lineReader.lineBuf[i2];
                    if ((c2 == '=' || c2 == ':') && !z4) {
                        i3 = i2 + 1;
                        z3 = true;
                        break;
                    } else {
                        if ((c2 == ' ' || c2 == '\t' || c2 == '\f') && !z4) {
                            i3 = i2 + 1;
                            break;
                        }
                        if (c2 == '\\') {
                            z2 = !z4;
                        } else {
                            z2 = false;
                        }
                        z4 = z2;
                        i2++;
                    }
                }
                while (i3 < line) {
                    char c3 = lineReader.lineBuf[i3];
                    if (c3 != ' ' && c3 != '\t' && c3 != '\f') {
                        if (z3 || !(c3 == '=' || c3 == ':')) {
                            break;
                        } else {
                            z3 = true;
                        }
                    }
                    i3++;
                }
                put(loadConvert(lineReader.lineBuf, 0, i2, cArr), loadConvert(lineReader.lineBuf, i3, line - i3, cArr));
            } else {
                return;
            }
        }
    }

    /* loaded from: rt.jar:java/util/Properties$LineReader.class */
    class LineReader {
        byte[] inByteBuf;
        char[] inCharBuf;
        char[] lineBuf;
        int inLimit;
        int inOff;
        InputStream inStream;
        Reader reader;

        public LineReader(InputStream inputStream) {
            this.lineBuf = new char[1024];
            this.inLimit = 0;
            this.inOff = 0;
            this.inStream = inputStream;
            this.inByteBuf = new byte[8192];
        }

        public LineReader(Reader reader) {
            this.lineBuf = new char[1024];
            this.inLimit = 0;
            this.inOff = 0;
            this.reader = reader;
            this.inCharBuf = new char[8192];
        }

        int readLine() throws IOException {
            char c2;
            int i2;
            int i3 = 0;
            boolean z2 = true;
            boolean z3 = false;
            boolean z4 = true;
            boolean z5 = false;
            boolean z6 = false;
            boolean z7 = false;
            while (true) {
                if (this.inOff >= this.inLimit) {
                    this.inLimit = this.inStream == null ? this.reader.read(this.inCharBuf) : this.inStream.read(this.inByteBuf);
                    this.inOff = 0;
                    if (this.inLimit <= 0) {
                        if (i3 == 0 || z3) {
                            return -1;
                        }
                        if (z6) {
                            i3--;
                        }
                        return i3;
                    }
                }
                if (this.inStream != null) {
                    byte[] bArr = this.inByteBuf;
                    int i4 = this.inOff;
                    this.inOff = i4 + 1;
                    c2 = (char) (255 & bArr[i4]);
                } else {
                    char[] cArr = this.inCharBuf;
                    int i5 = this.inOff;
                    this.inOff = i5 + 1;
                    c2 = cArr[i5];
                }
                if (z7) {
                    z7 = false;
                    if (c2 == '\n') {
                        continue;
                    }
                }
                if (z2) {
                    if (c2 != ' ' && c2 != '\t' && c2 != '\f' && (z5 || (c2 != '\r' && c2 != '\n'))) {
                        z2 = false;
                        z5 = false;
                    }
                }
                if (z4) {
                    z4 = false;
                    if (c2 == '#' || c2 == '!') {
                        z3 = true;
                    }
                }
                if (c2 != '\n' && c2 != '\r') {
                    int i6 = i3;
                    i3++;
                    this.lineBuf[i6] = c2;
                    if (i3 == this.lineBuf.length) {
                        int length = this.lineBuf.length * 2;
                        if (length < 0) {
                            length = Integer.MAX_VALUE;
                        }
                        char[] cArr2 = new char[length];
                        System.arraycopy(this.lineBuf, 0, cArr2, 0, this.lineBuf.length);
                        this.lineBuf = cArr2;
                    }
                    if (c2 == '\\') {
                        z6 = !z6;
                    } else {
                        z6 = false;
                    }
                } else if (z3 || i3 == 0) {
                    z3 = false;
                    z4 = true;
                    z2 = true;
                    i3 = 0;
                } else {
                    if (this.inOff >= this.inLimit) {
                        if (this.inStream == null) {
                            i2 = this.reader.read(this.inCharBuf);
                        } else {
                            i2 = this.inStream.read(this.inByteBuf);
                        }
                        this.inLimit = i2;
                        this.inOff = 0;
                        if (this.inLimit <= 0) {
                            if (z6) {
                                i3--;
                            }
                            return i3;
                        }
                    }
                    if (z6) {
                        i3--;
                        z2 = true;
                        z5 = true;
                        z6 = false;
                        if (c2 == '\r') {
                            z7 = true;
                        }
                    } else {
                        return i3;
                    }
                }
            }
        }
    }

    private String loadConvert(char[] cArr, int i2, int i3, char[] cArr2) {
        int i4;
        int i5;
        if (cArr2.length < i3) {
            int i6 = i3 * 2;
            if (i6 < 0) {
                i6 = Integer.MAX_VALUE;
            }
            cArr2 = new char[i6];
        }
        char[] cArr3 = cArr2;
        int i7 = 0;
        int i8 = i2 + i3;
        while (i2 < i8) {
            int i9 = i2;
            i2++;
            char c2 = cArr[i9];
            if (c2 == '\\') {
                i2++;
                char c3 = cArr[i2];
                if (c3 == 'u') {
                    int i10 = 0;
                    for (int i11 = 0; i11 < 4; i11++) {
                        int i12 = i2;
                        i2++;
                        char c4 = cArr[i12];
                        switch (c4) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                i4 = (i10 << 4) + c4;
                                i5 = 48;
                                break;
                            case ':':
                            case ';':
                            case '<':
                            case '=':
                            case '>':
                            case '?':
                            case '@':
                            case 'G':
                            case 'H':
                            case 'I':
                            case 'J':
                            case 'K':
                            case 'L':
                            case 'M':
                            case 'N':
                            case 'O':
                            case 'P':
                            case 'Q':
                            case 'R':
                            case 'S':
                            case 'T':
                            case 'U':
                            case 'V':
                            case 'W':
                            case 'X':
                            case 'Y':
                            case 'Z':
                            case '[':
                            case '\\':
                            case ']':
                            case '^':
                            case '_':
                            case '`':
                            default:
                                throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                i4 = (i10 << 4) + 10 + c4;
                                i5 = 65;
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                i4 = (i10 << 4) + 10 + c4;
                                i5 = 97;
                                break;
                        }
                        i10 = i4 - i5;
                    }
                    int i13 = i7;
                    i7++;
                    cArr3[i13] = (char) i10;
                } else {
                    if (c3 == 't') {
                        c3 = '\t';
                    } else if (c3 == 'r') {
                        c3 = '\r';
                    } else if (c3 == 'n') {
                        c3 = '\n';
                    } else if (c3 == 'f') {
                        c3 = '\f';
                    }
                    int i14 = i7;
                    i7++;
                    cArr3[i14] = c3;
                }
            } else {
                int i15 = i7;
                i7++;
                cArr3[i15] = c2;
            }
        }
        return new String(cArr3, 0, i7);
    }

    private String saveConvert(String str, boolean z2, boolean z3) {
        int length = str.length();
        int i2 = length * 2;
        if (i2 < 0) {
            i2 = Integer.MAX_VALUE;
        }
        StringBuffer stringBuffer = new StringBuffer(i2);
        for (int i3 = 0; i3 < length; i3++) {
            char cCharAt = str.charAt(i3);
            if (cCharAt > '=' && cCharAt < 127) {
                if (cCharAt == '\\') {
                    stringBuffer.append('\\');
                    stringBuffer.append('\\');
                } else {
                    stringBuffer.append(cCharAt);
                }
            } else {
                switch (cCharAt) {
                    case '\t':
                        stringBuffer.append('\\');
                        stringBuffer.append('t');
                        break;
                    case '\n':
                        stringBuffer.append('\\');
                        stringBuffer.append('n');
                        break;
                    case '\f':
                        stringBuffer.append('\\');
                        stringBuffer.append('f');
                        break;
                    case '\r':
                        stringBuffer.append('\\');
                        stringBuffer.append('r');
                        break;
                    case ' ':
                        if (i3 == 0 || z2) {
                            stringBuffer.append('\\');
                        }
                        stringBuffer.append(' ');
                        break;
                    case '!':
                    case '#':
                    case ':':
                    case '=':
                        stringBuffer.append('\\');
                        stringBuffer.append(cCharAt);
                        break;
                    default:
                        if ((cCharAt < ' ' || cCharAt > '~') & z3) {
                            stringBuffer.append('\\');
                            stringBuffer.append('u');
                            stringBuffer.append(toHex((cCharAt >> '\f') & 15));
                            stringBuffer.append(toHex((cCharAt >> '\b') & 15));
                            stringBuffer.append(toHex((cCharAt >> 4) & 15));
                            stringBuffer.append(toHex(cCharAt & 15));
                            break;
                        } else {
                            stringBuffer.append(cCharAt);
                            break;
                        }
                }
            }
        }
        return stringBuffer.toString();
    }

    private static void writeComments(BufferedWriter bufferedWriter, String str) throws IOException {
        bufferedWriter.write(FXMLLoader.CONTROLLER_METHOD_PREFIX);
        int length = str.length();
        int i2 = 0;
        int i3 = 0;
        char[] cArr = new char[6];
        cArr[0] = '\\';
        cArr[1] = 'u';
        while (i2 < length) {
            char cCharAt = str.charAt(i2);
            if (cCharAt > 255 || cCharAt == '\n' || cCharAt == '\r') {
                if (i3 != i2) {
                    bufferedWriter.write(str.substring(i3, i2));
                }
                if (cCharAt > 255) {
                    cArr[2] = toHex((cCharAt >> '\f') & 15);
                    cArr[3] = toHex((cCharAt >> '\b') & 15);
                    cArr[4] = toHex((cCharAt >> 4) & 15);
                    cArr[5] = toHex(cCharAt & 15);
                    bufferedWriter.write(new String(cArr));
                } else {
                    bufferedWriter.newLine();
                    if (cCharAt == '\r' && i2 != length - 1 && str.charAt(i2 + 1) == '\n') {
                        i2++;
                    }
                    if (i2 == length - 1 || (str.charAt(i2 + 1) != '#' && str.charAt(i2 + 1) != '!')) {
                        bufferedWriter.write(FXMLLoader.CONTROLLER_METHOD_PREFIX);
                    }
                }
                i3 = i2 + 1;
            }
            i2++;
        }
        if (i3 != i2) {
            bufferedWriter.write(str.substring(i3, i2));
        }
        bufferedWriter.newLine();
    }

    @Deprecated
    public void save(OutputStream outputStream, String str) {
        try {
            store(outputStream, str);
        } catch (IOException e2) {
        }
    }

    public void store(Writer writer, String str) throws IOException {
        store0(writer instanceof BufferedWriter ? (BufferedWriter) writer : new BufferedWriter(writer), str, false);
    }

    public void store(OutputStream outputStream, String str) throws IOException {
        store0(new BufferedWriter(new OutputStreamWriter(outputStream, "8859_1")), str, true);
    }

    private void store0(BufferedWriter bufferedWriter, String str, boolean z2) throws IOException {
        if (str != null) {
            writeComments(bufferedWriter, str);
        }
        bufferedWriter.write(FXMLLoader.CONTROLLER_METHOD_PREFIX + new Date().toString());
        bufferedWriter.newLine();
        synchronized (this) {
            Enumeration enumerationKeys = keys();
            while (enumerationKeys.hasMoreElements()) {
                String str2 = (String) enumerationKeys.nextElement();
                String str3 = (String) get(str2);
                bufferedWriter.write(saveConvert(str2, true, z2) + "=" + saveConvert(str3, false, z2));
                bufferedWriter.newLine();
            }
        }
        bufferedWriter.flush();
    }

    public synchronized void loadFromXML(InputStream inputStream) throws IOException {
        XmlSupport.load(this, (InputStream) Objects.requireNonNull(inputStream));
        inputStream.close();
    }

    public void storeToXML(OutputStream outputStream, String str) throws IOException {
        storeToXML(outputStream, str, "UTF-8");
    }

    public void storeToXML(OutputStream outputStream, String str, String str2) throws IOException {
        XmlSupport.save(this, (OutputStream) Objects.requireNonNull(outputStream), str, (String) Objects.requireNonNull(str2));
    }

    public String getProperty(String str) {
        Object obj = super.get(str);
        String str2 = obj instanceof String ? (String) obj : null;
        return (str2 != null || this.defaults == null) ? str2 : this.defaults.getProperty(str);
    }

    public String getProperty(String str, String str2) {
        String property = getProperty(str);
        return property == null ? str2 : property;
    }

    public Enumeration<?> propertyNames() {
        Hashtable<String, Object> hashtable = new Hashtable<>();
        enumerate(hashtable);
        return hashtable.keys();
    }

    public Set<String> stringPropertyNames() {
        Hashtable<String, String> hashtable = new Hashtable<>();
        enumerateStringProperties(hashtable);
        return hashtable.keySet();
    }

    public void list(PrintStream printStream) {
        printStream.println("-- listing properties --");
        Hashtable<String, Object> hashtable = new Hashtable<>();
        enumerate(hashtable);
        Enumeration<String> enumerationKeys = hashtable.keys();
        while (enumerationKeys.hasMoreElements()) {
            String strNextElement = enumerationKeys.nextElement();
            String str = (String) hashtable.get(strNextElement);
            if (str.length() > 40) {
                str = str.substring(0, 37) + "...";
            }
            printStream.println(strNextElement + "=" + str);
        }
    }

    public void list(PrintWriter printWriter) {
        printWriter.println("-- listing properties --");
        Hashtable<String, Object> hashtable = new Hashtable<>();
        enumerate(hashtable);
        Enumeration<String> enumerationKeys = hashtable.keys();
        while (enumerationKeys.hasMoreElements()) {
            String strNextElement = enumerationKeys.nextElement();
            String str = (String) hashtable.get(strNextElement);
            if (str.length() > 40) {
                str = str.substring(0, 37) + "...";
            }
            printWriter.println(strNextElement + "=" + str);
        }
    }

    private synchronized void enumerate(Hashtable<String, Object> hashtable) {
        if (this.defaults != null) {
            this.defaults.enumerate(hashtable);
        }
        Enumeration<Object> enumerationKeys = keys();
        while (enumerationKeys.hasMoreElements()) {
            String str = (String) enumerationKeys.nextElement();
            hashtable.put(str, get(str));
        }
    }

    private synchronized void enumerateStringProperties(Hashtable<String, String> hashtable) {
        if (this.defaults != null) {
            this.defaults.enumerateStringProperties(hashtable);
        }
        Enumeration<Object> enumerationKeys = keys();
        while (enumerationKeys.hasMoreElements()) {
            Object objNextElement = enumerationKeys.nextElement();
            Object obj = get(objNextElement);
            if ((objNextElement instanceof String) && (obj instanceof String)) {
                hashtable.put((String) objNextElement, (String) obj);
            }
        }
    }

    private static char toHex(int i2) {
        return hexDigit[i2 & 15];
    }

    /* loaded from: rt.jar:java/util/Properties$XmlSupport.class */
    private static class XmlSupport {
        private static final XmlPropertiesProvider PROVIDER = loadProvider();

        private XmlSupport() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static XmlPropertiesProvider loadProviderFromProperty(ClassLoader classLoader) {
            String property = System.getProperty("sun.util.spi.XmlPropertiesProvider");
            if (property == null) {
                return null;
            }
            try {
                return (XmlPropertiesProvider) Class.forName(property, true, classLoader).newInstance();
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e2) {
                throw new ServiceConfigurationError(null, e2);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static XmlPropertiesProvider loadProviderAsService(ClassLoader classLoader) {
            Iterator it = ServiceLoader.load(XmlPropertiesProvider.class, classLoader).iterator();
            if (it.hasNext()) {
                return (XmlPropertiesProvider) it.next();
            }
            return null;
        }

        private static XmlPropertiesProvider loadProvider() {
            return (XmlPropertiesProvider) AccessController.doPrivileged(new PrivilegedAction<XmlPropertiesProvider>() { // from class: java.util.Properties.XmlSupport.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public XmlPropertiesProvider run() {
                    ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
                    XmlPropertiesProvider xmlPropertiesProviderLoadProviderFromProperty = XmlSupport.loadProviderFromProperty(systemClassLoader);
                    if (xmlPropertiesProviderLoadProviderFromProperty == null) {
                        XmlPropertiesProvider xmlPropertiesProviderLoadProviderAsService = XmlSupport.loadProviderAsService(systemClassLoader);
                        if (xmlPropertiesProviderLoadProviderAsService != null) {
                            return xmlPropertiesProviderLoadProviderAsService;
                        }
                        return new BasicXmlPropertiesProvider();
                    }
                    return xmlPropertiesProviderLoadProviderFromProperty;
                }
            });
        }

        static void load(Properties properties, InputStream inputStream) throws IOException {
            PROVIDER.load(properties, inputStream);
        }

        static void save(Properties properties, OutputStream outputStream, String str, String str2) throws IOException {
            PROVIDER.store(properties, outputStream, str, str2);
        }
    }
}
