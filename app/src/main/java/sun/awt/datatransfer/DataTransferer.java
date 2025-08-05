package sun.awt.datatransfer;

import com.sun.glass.ui.Clipboard;
import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorMap;
import java.awt.datatransfer.FlavorTable;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.SequenceInputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import org.apache.commons.net.ftp.FTP;
import sun.awt.AppContext;
import sun.awt.ComponentFactory;
import sun.awt.SunToolkit;
import sun.awt.image.ImageRepresentation;
import sun.awt.image.ToolkitImage;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:sun/awt/datatransfer/DataTransferer.class */
public abstract class DataTransferer {
    public static final DataFlavor plainTextStringFlavor;
    public static final DataFlavor javaTextEncodingFlavor;
    private static final Map textMIMESubtypeCharsetSupport;
    private static String defaultEncoding;
    private static final String DATA_CONVERTER_KEY = "DATA_CONVERTER_KEY";
    private static DataTransferer transferer;
    private static final String[] DEPLOYMENT_CACHE_PROPERTIES;
    private static final ArrayList<File> deploymentCacheDirectoryList;
    private static final Set textNatives = Collections.synchronizedSet(new HashSet());
    private static final Map nativeCharsets = Collections.synchronizedMap(new HashMap());
    private static final Map nativeEOLNs = Collections.synchronizedMap(new HashMap());
    private static final Map nativeTerminators = Collections.synchronizedMap(new HashMap());
    private static final PlatformLogger dtLog = PlatformLogger.getLogger("sun.awt.datatransfer.DataTransfer");

    public abstract String getDefaultUnicodeEncoding();

    public abstract boolean isLocaleDependentTextFormat(long j2);

    public abstract boolean isFileFormat(long j2);

    public abstract boolean isImageFormat(long j2);

    protected abstract Long getFormatForNativeAsLong(String str);

    protected abstract String getNativeForFormat(long j2);

    protected abstract ByteArrayOutputStream convertFileListToBytes(ArrayList<String> arrayList) throws IOException;

    protected abstract String[] dragQueryFile(byte[] bArr);

    protected abstract Image platformImageBytesToImage(byte[] bArr, long j2) throws IOException;

    protected abstract byte[] imageToPlatformBytes(Image image, long j2) throws IOException;

    public abstract ToolkitThreadBlockedHandler getToolkitThreadBlockedHandler();

    /* loaded from: rt.jar:sun/awt/datatransfer/DataTransferer$StandardEncodingsHolder.class */
    private static class StandardEncodingsHolder {
        private static final SortedSet<String> standardEncodings = load();

        private StandardEncodingsHolder() {
        }

        private static SortedSet<String> load() {
            TreeSet treeSet = new TreeSet(new CharsetComparator(false));
            treeSet.add("US-ASCII");
            treeSet.add(FTP.DEFAULT_CONTROL_ENCODING);
            treeSet.add("UTF-8");
            treeSet.add(FastInfosetSerializer.UTF_16BE);
            treeSet.add("UTF-16LE");
            treeSet.add("UTF-16");
            treeSet.add(DataTransferer.getDefaultTextCharset());
            return Collections.unmodifiableSortedSet(treeSet);
        }
    }

    static {
        DataFlavor dataFlavor = null;
        try {
            dataFlavor = new DataFlavor("text/plain;charset=Unicode;class=java.lang.String");
        } catch (ClassNotFoundException e2) {
        }
        plainTextStringFlavor = dataFlavor;
        DataFlavor dataFlavor2 = null;
        try {
            dataFlavor2 = new DataFlavor("application/x-java-text-encoding;class=\"[B\"");
        } catch (ClassNotFoundException e3) {
        }
        javaTextEncodingFlavor = dataFlavor2;
        HashMap map = new HashMap(17);
        map.put("sgml", Boolean.TRUE);
        map.put("xml", Boolean.TRUE);
        map.put("html", Boolean.TRUE);
        map.put("enriched", Boolean.TRUE);
        map.put("richtext", Boolean.TRUE);
        map.put("uri-list", Boolean.TRUE);
        map.put("directory", Boolean.TRUE);
        map.put("css", Boolean.TRUE);
        map.put("calendar", Boolean.TRUE);
        map.put("plain", Boolean.TRUE);
        map.put("rtf", Boolean.FALSE);
        map.put("tab-separated-values", Boolean.FALSE);
        map.put("t140", Boolean.FALSE);
        map.put("rfc822-headers", Boolean.FALSE);
        map.put("parityfec", Boolean.FALSE);
        textMIMESubtypeCharsetSupport = Collections.synchronizedMap(map);
        DEPLOYMENT_CACHE_PROPERTIES = new String[]{"deployment.system.cachedir", "deployment.user.cachedir", "deployment.javaws.cachedir", "deployment.javapi.cachedir"};
        deploymentCacheDirectoryList = new ArrayList<>();
    }

    public static synchronized DataTransferer getInstance() {
        return ((ComponentFactory) Toolkit.getDefaultToolkit()).getDataTransferer();
    }

    public static String canonicalName(String str) {
        if (str == null) {
            return null;
        }
        try {
            return Charset.forName(str).name();
        } catch (IllegalCharsetNameException e2) {
            return str;
        } catch (UnsupportedCharsetException e3) {
            return str;
        }
    }

    public static String getTextCharset(DataFlavor dataFlavor) {
        if (!isFlavorCharsetTextType(dataFlavor)) {
            return null;
        }
        String parameter = dataFlavor.getParameter("charset");
        return parameter != null ? parameter : getDefaultTextCharset();
    }

    public static String getDefaultTextCharset() {
        if (defaultEncoding != null) {
            return defaultEncoding;
        }
        String strName = Charset.defaultCharset().name();
        defaultEncoding = strName;
        return strName;
    }

    public static boolean doesSubtypeSupportCharset(DataFlavor dataFlavor) {
        if (dtLog.isLoggable(PlatformLogger.Level.FINE) && !"text".equals(dataFlavor.getPrimaryType())) {
            dtLog.fine("Assertion (\"text\".equals(flavor.getPrimaryType())) failed");
        }
        String subType = dataFlavor.getSubType();
        if (subType == null) {
            return false;
        }
        Object obj = textMIMESubtypeCharsetSupport.get(subType);
        if (obj != null) {
            return obj == Boolean.TRUE;
        }
        boolean z2 = dataFlavor.getParameter("charset") != null;
        textMIMESubtypeCharsetSupport.put(subType, z2 ? Boolean.TRUE : Boolean.FALSE);
        return z2;
    }

    public static boolean doesSubtypeSupportCharset(String str, String str2) {
        Object obj = textMIMESubtypeCharsetSupport.get(str);
        if (obj != null) {
            return obj == Boolean.TRUE;
        }
        boolean z2 = str2 != null;
        textMIMESubtypeCharsetSupport.put(str, z2 ? Boolean.TRUE : Boolean.FALSE);
        return z2;
    }

    public static boolean isFlavorCharsetTextType(DataFlavor dataFlavor) {
        if (DataFlavor.stringFlavor.equals(dataFlavor)) {
            return true;
        }
        if (!"text".equals(dataFlavor.getPrimaryType()) || !doesSubtypeSupportCharset(dataFlavor)) {
            return false;
        }
        Class<?> representationClass = dataFlavor.getRepresentationClass();
        if (dataFlavor.isRepresentationClassReader() || String.class.equals(representationClass) || dataFlavor.isRepresentationClassCharBuffer() || char[].class.equals(representationClass)) {
            return true;
        }
        if (!dataFlavor.isRepresentationClassInputStream() && !dataFlavor.isRepresentationClassByteBuffer() && !byte[].class.equals(representationClass)) {
            return false;
        }
        String parameter = dataFlavor.getParameter("charset");
        if (parameter != null) {
            return isEncodingSupported(parameter);
        }
        return true;
    }

    public static boolean isFlavorNoncharsetTextType(DataFlavor dataFlavor) {
        if (!"text".equals(dataFlavor.getPrimaryType()) || doesSubtypeSupportCharset(dataFlavor)) {
            return false;
        }
        return dataFlavor.isRepresentationClassInputStream() || dataFlavor.isRepresentationClassByteBuffer() || byte[].class.equals(dataFlavor.getRepresentationClass());
    }

    public static boolean isEncodingSupported(String str) {
        if (str == null) {
            return false;
        }
        try {
            return Charset.isSupported(str);
        } catch (IllegalCharsetNameException e2) {
            return false;
        }
    }

    public static boolean isRemote(Class<?> cls) {
        return RMI.isRemote(cls);
    }

    public static Set<String> standardEncodings() {
        return StandardEncodingsHolder.standardEncodings;
    }

    public static FlavorTable adaptFlavorMap(final FlavorMap flavorMap) {
        if (flavorMap instanceof FlavorTable) {
            return (FlavorTable) flavorMap;
        }
        return new FlavorTable() { // from class: sun.awt.datatransfer.DataTransferer.1
            @Override // java.awt.datatransfer.FlavorMap
            public Map getNativesForFlavors(DataFlavor[] dataFlavorArr) {
                return flavorMap.getNativesForFlavors(dataFlavorArr);
            }

            @Override // java.awt.datatransfer.FlavorMap
            public Map getFlavorsForNatives(String[] strArr) {
                return flavorMap.getFlavorsForNatives(strArr);
            }

            @Override // java.awt.datatransfer.FlavorTable
            public List getNativesForFlavor(DataFlavor dataFlavor) {
                String str = (String) getNativesForFlavors(new DataFlavor[]{dataFlavor}).get(dataFlavor);
                if (str != null) {
                    ArrayList arrayList = new ArrayList(1);
                    arrayList.add(str);
                    return arrayList;
                }
                return Collections.EMPTY_LIST;
            }

            @Override // java.awt.datatransfer.FlavorTable
            public List getFlavorsForNative(String str) {
                DataFlavor dataFlavor = (DataFlavor) getFlavorsForNatives(new String[]{str}).get(str);
                if (dataFlavor != null) {
                    ArrayList arrayList = new ArrayList(1);
                    arrayList.add(dataFlavor);
                    return arrayList;
                }
                return Collections.EMPTY_LIST;
            }
        };
    }

    public void registerTextFlavorProperties(String str, String str2, String str3, String str4) {
        Long formatForNativeAsLong = getFormatForNativeAsLong(str);
        textNatives.add(formatForNativeAsLong);
        nativeCharsets.put(formatForNativeAsLong, (str2 == null || str2.length() == 0) ? getDefaultTextCharset() : str2);
        if (str3 != null && str3.length() != 0 && !str3.equals("\n")) {
            nativeEOLNs.put(formatForNativeAsLong, str3);
        }
        if (str4 != null && str4.length() != 0) {
            Integer numValueOf = Integer.valueOf(str4);
            if (numValueOf.intValue() > 0) {
                nativeTerminators.put(formatForNativeAsLong, numValueOf);
            }
        }
    }

    protected boolean isTextFormat(long j2) {
        return textNatives.contains(Long.valueOf(j2));
    }

    protected String getCharsetForTextFormat(Long l2) {
        return (String) nativeCharsets.get(l2);
    }

    protected boolean isURIListFormat(long j2) {
        return false;
    }

    public SortedMap<Long, DataFlavor> getFormatsForTransferable(Transferable transferable, FlavorTable flavorTable) {
        DataFlavor[] transferDataFlavors = transferable.getTransferDataFlavors();
        if (transferDataFlavors == null) {
            return new TreeMap();
        }
        return getFormatsForFlavors(transferDataFlavors, flavorTable);
    }

    public SortedMap getFormatsForFlavor(DataFlavor dataFlavor, FlavorTable flavorTable) {
        return getFormatsForFlavors(new DataFlavor[]{dataFlavor}, flavorTable);
    }

    public SortedMap<Long, DataFlavor> getFormatsForFlavors(DataFlavor[] dataFlavorArr, FlavorTable flavorTable) {
        HashMap map = new HashMap(dataFlavorArr.length);
        HashMap map2 = new HashMap(dataFlavorArr.length);
        HashMap map3 = new HashMap(dataFlavorArr.length);
        HashMap map4 = new HashMap(dataFlavorArr.length);
        int size = 0;
        for (int length = dataFlavorArr.length - 1; length >= 0; length--) {
            DataFlavor dataFlavor = dataFlavorArr[length];
            if (dataFlavor != null && (dataFlavor.isFlavorTextType() || dataFlavor.isFlavorJavaFileListType() || DataFlavor.imageFlavor.equals(dataFlavor) || dataFlavor.isRepresentationClassSerializable() || dataFlavor.isRepresentationClassInputStream() || dataFlavor.isRepresentationClassRemote())) {
                List<String> nativesForFlavor = flavorTable.getNativesForFlavor(dataFlavor);
                int size2 = size + nativesForFlavor.size();
                Iterator<String> it = nativesForFlavor.iterator();
                while (it.hasNext()) {
                    Long formatForNativeAsLong = getFormatForNativeAsLong(it.next());
                    int i2 = size2;
                    size2--;
                    Integer numValueOf = Integer.valueOf(i2);
                    map.put(formatForNativeAsLong, dataFlavor);
                    map3.put(formatForNativeAsLong, numValueOf);
                    if (("text".equals(dataFlavor.getPrimaryType()) && "plain".equals(dataFlavor.getSubType())) || dataFlavor.equals(DataFlavor.stringFlavor)) {
                        map2.put(formatForNativeAsLong, dataFlavor);
                        map4.put(formatForNativeAsLong, numValueOf);
                    }
                }
                size = size2 + nativesForFlavor.size();
            }
        }
        map.putAll(map2);
        map3.putAll(map4);
        TreeMap treeMap = new TreeMap(new IndexOrderComparator(map3, false));
        treeMap.putAll(map);
        return treeMap;
    }

    public long[] getFormatsForTransferableAsArray(Transferable transferable, FlavorTable flavorTable) {
        return keysToLongArray(getFormatsForTransferable(transferable, flavorTable));
    }

    public long[] getFormatsForFlavorAsArray(DataFlavor dataFlavor, FlavorTable flavorTable) {
        return keysToLongArray(getFormatsForFlavor(dataFlavor, flavorTable));
    }

    public long[] getFormatsForFlavorsAsArray(DataFlavor[] dataFlavorArr, FlavorTable flavorTable) {
        return keysToLongArray(getFormatsForFlavors(dataFlavorArr, flavorTable));
    }

    public Map getFlavorsForFormat(long j2, FlavorTable flavorTable) {
        return getFlavorsForFormats(new long[]{j2}, flavorTable);
    }

    public Map getFlavorsForFormats(long[] jArr, FlavorTable flavorTable) {
        HashMap map = new HashMap(jArr.length);
        HashSet hashSet = new HashSet(jArr.length);
        HashSet<DataFlavor> hashSet2 = new HashSet(jArr.length);
        for (long j2 : jArr) {
            for (DataFlavor dataFlavor : flavorTable.getFlavorsForNative(getNativeForFormat(j2))) {
                if (dataFlavor.isFlavorTextType() || dataFlavor.isFlavorJavaFileListType() || DataFlavor.imageFlavor.equals(dataFlavor) || dataFlavor.isRepresentationClassSerializable() || dataFlavor.isRepresentationClassInputStream() || dataFlavor.isRepresentationClassRemote()) {
                    Long lValueOf = Long.valueOf(j2);
                    Object objCreateMapping = createMapping(lValueOf, dataFlavor);
                    map.put(dataFlavor, lValueOf);
                    hashSet.add(objCreateMapping);
                    hashSet2.add(dataFlavor);
                }
            }
        }
        for (DataFlavor dataFlavor2 : hashSet2) {
            Iterator<String> it = flavorTable.getNativesForFlavor(dataFlavor2).iterator();
            while (true) {
                if (it.hasNext()) {
                    Long formatForNativeAsLong = getFormatForNativeAsLong(it.next());
                    if (hashSet.contains(createMapping(formatForNativeAsLong, dataFlavor2))) {
                        map.put(dataFlavor2, formatForNativeAsLong);
                        break;
                    }
                }
            }
        }
        return map;
    }

    public Set getFlavorsForFormatsAsSet(long[] jArr, FlavorTable flavorTable) {
        HashSet hashSet = new HashSet(jArr.length);
        for (long j2 : jArr) {
            for (DataFlavor dataFlavor : flavorTable.getFlavorsForNative(getNativeForFormat(j2))) {
                if (dataFlavor.isFlavorTextType() || dataFlavor.isFlavorJavaFileListType() || DataFlavor.imageFlavor.equals(dataFlavor) || dataFlavor.isRepresentationClassSerializable() || dataFlavor.isRepresentationClassInputStream() || dataFlavor.isRepresentationClassRemote()) {
                    hashSet.add(dataFlavor);
                }
            }
        }
        return hashSet;
    }

    public DataFlavor[] getFlavorsForFormatAsArray(long j2, FlavorTable flavorTable) {
        return getFlavorsForFormatsAsArray(new long[]{j2}, flavorTable);
    }

    public DataFlavor[] getFlavorsForFormatsAsArray(long[] jArr, FlavorTable flavorTable) {
        return setToSortedDataFlavorArray(getFlavorsForFormatsAsSet(jArr, flavorTable));
    }

    private static Object createMapping(Object obj, Object obj2) {
        return Arrays.asList(obj, obj2);
    }

    private String getBestCharsetForTextFormat(Long l2, Transferable transferable) throws IOException {
        String charsetForTextFormat = null;
        if (transferable != null && isLocaleDependentTextFormat(l2.longValue()) && transferable.isDataFlavorSupported(javaTextEncodingFlavor)) {
            try {
                charsetForTextFormat = new String((byte[]) transferable.getTransferData(javaTextEncodingFlavor), "UTF-8");
            } catch (UnsupportedFlavorException e2) {
            }
        } else {
            charsetForTextFormat = getCharsetForTextFormat(l2);
        }
        if (charsetForTextFormat == null) {
            charsetForTextFormat = getDefaultTextCharset();
        }
        return charsetForTextFormat;
    }

    private byte[] translateTransferableString(String str, long j2) throws IOException {
        Long lValueOf = Long.valueOf(j2);
        String bestCharsetForTextFormat = getBestCharsetForTextFormat(lValueOf, null);
        String str2 = (String) nativeEOLNs.get(lValueOf);
        if (str2 != null) {
            int length = str.length();
            StringBuffer stringBuffer = new StringBuffer(length * 2);
            int length2 = 0;
            while (length2 < length) {
                if (str.startsWith(str2, length2)) {
                    stringBuffer.append(str2);
                    length2 += str2.length() - 1;
                } else {
                    char cCharAt = str.charAt(length2);
                    if (cCharAt == '\n') {
                        stringBuffer.append(str2);
                    } else {
                        stringBuffer.append(cCharAt);
                    }
                }
                length2++;
            }
            str = stringBuffer.toString();
        }
        byte[] bytes = str.getBytes(bestCharsetForTextFormat);
        Integer num = (Integer) nativeTerminators.get(lValueOf);
        if (num != null) {
            byte[] bArr = new byte[bytes.length + num.intValue()];
            System.arraycopy(bytes, 0, bArr, 0, bytes.length);
            for (int length3 = bytes.length; length3 < bArr.length; length3++) {
                bArr[length3] = 0;
            }
            bytes = bArr;
        }
        return bytes;
    }

    private String translateBytesToString(byte[] bArr, long j2, Transferable transferable) throws IOException {
        int length;
        Long lValueOf = Long.valueOf(j2);
        String bestCharsetForTextFormat = getBestCharsetForTextFormat(lValueOf, transferable);
        String str = (String) nativeEOLNs.get(lValueOf);
        Integer num = (Integer) nativeTerminators.get(lValueOf);
        if (num != null) {
            int iIntValue = num.intValue();
            int i2 = 0;
            loop0: while (true) {
                length = i2;
                if (length >= (bArr.length - iIntValue) + 1) {
                    break;
                }
                for (int i3 = length; i3 < length + iIntValue; i3++) {
                    if (bArr[i3] != 0) {
                        break;
                    }
                }
                break loop0;
                i2 = length + iIntValue;
            }
        } else {
            length = bArr.length;
        }
        String str2 = new String(bArr, 0, length, bestCharsetForTextFormat);
        if (str != null) {
            char[] charArray = str2.toCharArray();
            char[] charArray2 = str.toCharArray();
            int i4 = 0;
            int length2 = 0;
            while (length2 < charArray.length) {
                if (length2 + charArray2.length > charArray.length) {
                    int i5 = i4;
                    i4++;
                    int i6 = length2;
                    length2++;
                    charArray[i5] = charArray[i6];
                } else {
                    boolean z2 = true;
                    int i7 = 0;
                    int i8 = length2;
                    while (true) {
                        if (i7 >= charArray2.length) {
                            break;
                        }
                        if (charArray2[i7] == charArray[i8]) {
                            i7++;
                            i8++;
                        } else {
                            z2 = false;
                            break;
                        }
                    }
                    if (z2) {
                        int i9 = i4;
                        i4++;
                        charArray[i9] = '\n';
                        length2 += charArray2.length;
                    } else {
                        int i10 = i4;
                        i4++;
                        int i11 = length2;
                        length2++;
                        charArray[i10] = charArray[i11];
                    }
                }
            }
            str2 = new String(charArray, 0, i4);
        }
        return str2;
    }

    public byte[] translateTransferable(Transferable transferable, DataFlavor dataFlavor, long j2) throws IOException {
        boolean z2;
        byte[] bArrConvertObjectToBytes;
        boolean z3;
        try {
            Object transferData = transferable.getTransferData(dataFlavor);
            if (transferData == null) {
                return null;
            }
            if (dataFlavor.equals(DataFlavor.plainTextFlavor) && !(transferData instanceof InputStream)) {
                transferData = transferable.getTransferData(DataFlavor.stringFlavor);
                if (transferData == null) {
                    return null;
                }
                z2 = true;
            } else {
                z2 = false;
            }
            if (z2 || (String.class.equals(dataFlavor.getRepresentationClass()) && isFlavorCharsetTextType(dataFlavor) && isTextFormat(j2))) {
                return translateTransferableString(removeSuspectedData(dataFlavor, transferable, (String) transferData), j2);
            }
            if (dataFlavor.isRepresentationClassReader()) {
                if (!isFlavorCharsetTextType(dataFlavor) || !isTextFormat(j2)) {
                    throw new IOException("cannot transfer non-text data as Reader");
                }
                StringBuffer stringBuffer = new StringBuffer();
                Reader reader = (Reader) transferData;
                Throwable th = null;
                while (true) {
                    try {
                        try {
                            int i2 = reader.read();
                            if (i2 == -1) {
                                break;
                            }
                            stringBuffer.append((char) i2);
                        } catch (Throwable th2) {
                            if (reader != null) {
                                if (th != null) {
                                    try {
                                        reader.close();
                                    } catch (Throwable th3) {
                                        th.addSuppressed(th3);
                                    }
                                } else {
                                    reader.close();
                                }
                            }
                            throw th2;
                        }
                    } finally {
                    }
                }
                if (reader != null) {
                    if (0 != 0) {
                        try {
                            reader.close();
                        } catch (Throwable th4) {
                            th.addSuppressed(th4);
                        }
                    } else {
                        reader.close();
                    }
                }
                return translateTransferableString(stringBuffer.toString(), j2);
            }
            if (dataFlavor.isRepresentationClassCharBuffer()) {
                if (!isFlavorCharsetTextType(dataFlavor) || !isTextFormat(j2)) {
                    throw new IOException("cannot transfer non-text data as CharBuffer");
                }
                CharBuffer charBuffer = (CharBuffer) transferData;
                int iRemaining = charBuffer.remaining();
                char[] cArr = new char[iRemaining];
                charBuffer.get(cArr, 0, iRemaining);
                return translateTransferableString(new String(cArr), j2);
            }
            if (char[].class.equals(dataFlavor.getRepresentationClass())) {
                if (!isFlavorCharsetTextType(dataFlavor) || !isTextFormat(j2)) {
                    throw new IOException("cannot transfer non-text data as char array");
                }
                return translateTransferableString(new String((char[]) transferData), j2);
            }
            if (dataFlavor.isRepresentationClassByteBuffer()) {
                ByteBuffer byteBuffer = (ByteBuffer) transferData;
                int iRemaining2 = byteBuffer.remaining();
                byte[] bArr = new byte[iRemaining2];
                byteBuffer.get(bArr, 0, iRemaining2);
                if (isFlavorCharsetTextType(dataFlavor) && isTextFormat(j2)) {
                    return translateTransferableString(new String(bArr, getTextCharset(dataFlavor)), j2);
                }
                return bArr;
            }
            if (byte[].class.equals(dataFlavor.getRepresentationClass())) {
                byte[] bArr2 = (byte[]) transferData;
                if (isFlavorCharsetTextType(dataFlavor) && isTextFormat(j2)) {
                    return translateTransferableString(new String(bArr2, getTextCharset(dataFlavor)), j2);
                }
                return bArr2;
            }
            if (DataFlavor.imageFlavor.equals(dataFlavor)) {
                if (!isImageFormat(j2)) {
                    throw new IOException("Data translation failed: not an image format");
                }
                byte[] bArrImageToPlatformBytes = imageToPlatformBytes((Image) transferData, j2);
                if (bArrImageToPlatformBytes == null) {
                    throw new IOException("Data translation failed: cannot convert java image to native format");
                }
                return bArrImageToPlatformBytes;
            }
            if (isFileFormat(j2)) {
                if (!DataFlavor.javaFileListFlavor.equals(dataFlavor)) {
                    throw new IOException("data translation failed");
                }
                ByteArrayOutputStream byteArrayOutputStreamConvertFileListToBytes = convertFileListToBytes(castToFiles((List) transferData, getUserProtectionDomain(transferable)));
                Throwable th5 = null;
                try {
                    try {
                        bArrConvertObjectToBytes = byteArrayOutputStreamConvertFileListToBytes.toByteArray();
                        if (byteArrayOutputStreamConvertFileListToBytes != null) {
                            if (0 != 0) {
                                try {
                                    byteArrayOutputStreamConvertFileListToBytes.close();
                                } catch (Throwable th6) {
                                    th5.addSuppressed(th6);
                                }
                            } else {
                                byteArrayOutputStreamConvertFileListToBytes.close();
                            }
                        }
                    } catch (Throwable th7) {
                        if (byteArrayOutputStreamConvertFileListToBytes != null) {
                            if (th5 != null) {
                                try {
                                    byteArrayOutputStreamConvertFileListToBytes.close();
                                } catch (Throwable th8) {
                                    th5.addSuppressed(th8);
                                }
                            } else {
                                byteArrayOutputStreamConvertFileListToBytes.close();
                            }
                        }
                        throw th7;
                    }
                } finally {
                }
            } else if (isURIListFormat(j2)) {
                if (!DataFlavor.javaFileListFlavor.equals(dataFlavor)) {
                    throw new IOException("data translation failed");
                }
                String nativeForFormat = getNativeForFormat(j2);
                String parameter = null;
                if (nativeForFormat != null) {
                    try {
                        parameter = new DataFlavor(nativeForFormat).getParameter("charset");
                    } catch (ClassNotFoundException e2) {
                        throw new IOException(e2);
                    }
                }
                if (parameter == null) {
                    parameter = "UTF-8";
                }
                ArrayList<String> arrayListCastToFiles = castToFiles((List) transferData, getUserProtectionDomain(transferable));
                ArrayList arrayList = new ArrayList(arrayListCastToFiles.size());
                Iterator<String> it = arrayListCastToFiles.iterator();
                while (it.hasNext()) {
                    URI uri = new File(it.next()).toURI();
                    try {
                        arrayList.add(new URI(uri.getScheme(), "", uri.getPath(), uri.getFragment()).toString());
                    } catch (URISyntaxException e3) {
                        throw new IOException(e3);
                    }
                }
                byte[] bytes = "\r\n".getBytes(parameter);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                Throwable th9 = null;
                try {
                    for (int i3 = 0; i3 < arrayList.size(); i3++) {
                        byte[] bytes2 = ((String) arrayList.get(i3)).getBytes(parameter);
                        byteArrayOutputStream.write(bytes2, 0, bytes2.length);
                        byteArrayOutputStream.write(bytes, 0, bytes.length);
                    }
                    bArrConvertObjectToBytes = byteArrayOutputStream.toByteArray();
                    if (byteArrayOutputStream != null) {
                        if (0 != 0) {
                            try {
                                byteArrayOutputStream.close();
                            } catch (Throwable th10) {
                                th9.addSuppressed(th10);
                            }
                        } else {
                            byteArrayOutputStream.close();
                        }
                    }
                } catch (Throwable th11) {
                    if (byteArrayOutputStream != null) {
                        if (0 != 0) {
                            try {
                                byteArrayOutputStream.close();
                            } catch (Throwable th12) {
                                th9.addSuppressed(th12);
                            }
                        } else {
                            byteArrayOutputStream.close();
                        }
                    }
                    throw th11;
                }
            } else if (dataFlavor.isRepresentationClassInputStream()) {
                if (!(transferData instanceof InputStream)) {
                    return new byte[0];
                }
                ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                Throwable th13 = null;
                try {
                    InputStream inputStream = (InputStream) transferData;
                    Throwable th14 = null;
                    try {
                        try {
                            int iAvailable = inputStream.available();
                            byte[] bArr3 = new byte[iAvailable > 8192 ? iAvailable : 8192];
                            do {
                                int i4 = inputStream.read(bArr3, 0, bArr3.length);
                                boolean z4 = i4 == -1;
                                z3 = z4;
                                if (!z4) {
                                    byteArrayOutputStream2.write(bArr3, 0, i4);
                                }
                            } while (!z3);
                            if (inputStream != null) {
                                if (0 != 0) {
                                    try {
                                        inputStream.close();
                                    } catch (Throwable th15) {
                                        th14.addSuppressed(th15);
                                    }
                                } else {
                                    inputStream.close();
                                }
                            }
                            if (isFlavorCharsetTextType(dataFlavor) && isTextFormat(j2)) {
                                byte[] bArrTranslateTransferableString = translateTransferableString(new String(byteArrayOutputStream2.toByteArray(), getTextCharset(dataFlavor)), j2);
                                if (byteArrayOutputStream2 != null) {
                                    if (0 != 0) {
                                        try {
                                            byteArrayOutputStream2.close();
                                        } catch (Throwable th16) {
                                            th13.addSuppressed(th16);
                                        }
                                    } else {
                                        byteArrayOutputStream2.close();
                                    }
                                }
                                return bArrTranslateTransferableString;
                            }
                            bArrConvertObjectToBytes = byteArrayOutputStream2.toByteArray();
                            if (byteArrayOutputStream2 != null) {
                                if (0 != 0) {
                                    try {
                                        byteArrayOutputStream2.close();
                                    } catch (Throwable th17) {
                                        th13.addSuppressed(th17);
                                    }
                                } else {
                                    byteArrayOutputStream2.close();
                                }
                            }
                        } catch (Throwable th18) {
                            if (inputStream != null) {
                                if (th14 != null) {
                                    try {
                                        inputStream.close();
                                    } catch (Throwable th19) {
                                        th14.addSuppressed(th19);
                                    }
                                } else {
                                    inputStream.close();
                                }
                            }
                            throw th18;
                        }
                    } finally {
                    }
                } catch (Throwable th20) {
                    if (byteArrayOutputStream2 != null) {
                        if (0 != 0) {
                            try {
                                byteArrayOutputStream2.close();
                            } catch (Throwable th21) {
                                th13.addSuppressed(th21);
                            }
                        } else {
                            byteArrayOutputStream2.close();
                        }
                    }
                    throw th20;
                }
            } else if (dataFlavor.isRepresentationClassRemote()) {
                bArrConvertObjectToBytes = convertObjectToBytes(RMI.newMarshalledObject(transferData));
            } else if (dataFlavor.isRepresentationClassSerializable()) {
                bArrConvertObjectToBytes = convertObjectToBytes(transferData);
            } else {
                throw new IOException("data translation failed");
            }
            return bArrConvertObjectToBytes;
        } catch (UnsupportedFlavorException e4) {
            throw new IOException(e4.getMessage());
        }
    }

    private static byte[] convertObjectToBytes(Object obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Throwable th = null;
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            Throwable th2 = null;
            try {
                try {
                    objectOutputStream.writeObject(obj);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    if (objectOutputStream != null) {
                        if (0 != 0) {
                            try {
                                objectOutputStream.close();
                            } catch (Throwable th3) {
                                th2.addSuppressed(th3);
                            }
                        } else {
                            objectOutputStream.close();
                        }
                    }
                    return byteArray;
                } catch (Throwable th4) {
                    if (objectOutputStream != null) {
                        if (th2 != null) {
                            try {
                                objectOutputStream.close();
                            } catch (Throwable th5) {
                                th2.addSuppressed(th5);
                            }
                        } else {
                            objectOutputStream.close();
                        }
                    }
                    throw th4;
                }
            } finally {
            }
        } finally {
            if (byteArrayOutputStream != null) {
                if (0 != 0) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (Throwable th6) {
                        th.addSuppressed(th6);
                    }
                } else {
                    byteArrayOutputStream.close();
                }
            }
        }
    }

    private String removeSuspectedData(DataFlavor dataFlavor, Transferable transferable, final String str) throws IOException {
        if (null == System.getSecurityManager() || !dataFlavor.isMimeTypeEqual(Clipboard.URI_TYPE)) {
            return str;
        }
        final ProtectionDomain userProtectionDomain = getUserProtectionDomain(transferable);
        try {
            return (String) AccessController.doPrivileged(new PrivilegedExceptionAction() { // from class: sun.awt.datatransfer.DataTransferer.2
                @Override // java.security.PrivilegedExceptionAction
                public Object run() {
                    StringBuffer stringBuffer = new StringBuffer(str.length());
                    for (String str2 : str.split("(\\s)+")) {
                        File file = new File(str2);
                        if (file.exists() && !DataTransferer.isFileInWebstartedCache(file) && !DataTransferer.this.isForbiddenToRead(file, userProtectionDomain)) {
                            if (0 != stringBuffer.length()) {
                                stringBuffer.append("\\r\\n");
                            }
                            stringBuffer.append(str2);
                        }
                    }
                    return stringBuffer.toString();
                }
            });
        } catch (PrivilegedActionException e2) {
            throw new IOException(e2.getMessage(), e2);
        }
    }

    private static ProtectionDomain getUserProtectionDomain(Transferable transferable) {
        return transferable.getClass().getProtectionDomain();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isForbiddenToRead(File file, ProtectionDomain protectionDomain) {
        if (null == protectionDomain) {
            return false;
        }
        try {
            if (protectionDomain.implies(new FilePermission(file.getCanonicalPath(), "read, delete"))) {
                return false;
            }
            return true;
        } catch (IOException e2) {
            return true;
        }
    }

    private ArrayList<String> castToFiles(final List list, final ProtectionDomain protectionDomain) throws IOException {
        final ArrayList<String> arrayList = new ArrayList<>();
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction() { // from class: sun.awt.datatransfer.DataTransferer.3
                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws IOException {
                    Iterator it = list.iterator();
                    while (it.hasNext()) {
                        File fileCastToFile = DataTransferer.this.castToFile(it.next());
                        if (fileCastToFile != null && (null == System.getSecurityManager() || (!DataTransferer.isFileInWebstartedCache(fileCastToFile) && !DataTransferer.this.isForbiddenToRead(fileCastToFile, protectionDomain)))) {
                            arrayList.add(fileCastToFile.getCanonicalPath());
                        }
                    }
                    return null;
                }
            });
            return arrayList;
        } catch (PrivilegedActionException e2) {
            throw new IOException(e2.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public File castToFile(Object obj) throws IOException {
        String canonicalPath;
        if (obj instanceof File) {
            canonicalPath = ((File) obj).getCanonicalPath();
        } else if (obj instanceof String) {
            canonicalPath = (String) obj;
        } else {
            return null;
        }
        return new File(canonicalPath);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isFileInWebstartedCache(File file) {
        if (deploymentCacheDirectoryList.isEmpty()) {
            for (String str : DEPLOYMENT_CACHE_PROPERTIES) {
                String property = System.getProperty(str);
                if (property != null) {
                    try {
                        File canonicalFile = new File(property).getCanonicalFile();
                        if (canonicalFile != null) {
                            deploymentCacheDirectoryList.add(canonicalFile);
                        }
                    } catch (IOException e2) {
                    }
                }
            }
        }
        Iterator<File> it = deploymentCacheDirectoryList.iterator();
        while (it.hasNext()) {
            File next = it.next();
            File parentFile = file;
            while (true) {
                File file2 = parentFile;
                if (file2 != null) {
                    if (!file2.equals(next)) {
                        parentFile = file2.getParentFile();
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /* JADX WARN: Failed to calculate best type for var: r14v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r15v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.RegisterArg.getSVar()" because the return value of "jadx.core.dex.nodes.InsnNode.getResult()" is null
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.collectRelatedVars(AbstractTypeConstraint.java:31)
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.<init>(AbstractTypeConstraint.java:19)
    	at jadx.core.dex.visitors.typeinference.TypeSearch$1.<init>(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeMoveConstraint(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeConstraint(TypeSearch.java:361)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.collectConstraints(TypeSearch.java:341)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:60)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.runMultiVariableSearch(FixTypesVisitor.java:116)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:91)
     */
    /* JADX WARN: Not initialized variable reg: 14, insn: 0x0405: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r14 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) A[TRY_LEAVE], block:B:195:0x0405 */
    /* JADX WARN: Not initialized variable reg: 15, insn: 0x040a: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r15 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:197:0x040a */
    /* JADX WARN: Type inference failed for: r0v173, types: [java.util.ArrayList, java.util.List] */
    /* JADX WARN: Type inference failed for: r14v0, types: [java.io.ByteArrayInputStream] */
    /* JADX WARN: Type inference failed for: r15v0, types: [java.lang.Throwable] */
    public Object translateBytes(byte[] bArr, DataFlavor dataFlavor, long j2, Transferable transferable) throws IOException {
        ByteArrayInputStream byteArrayInputStream;
        Object marshalledObject = null;
        if (isFileFormat(j2)) {
            if (!DataFlavor.javaFileListFlavor.equals(dataFlavor)) {
                throw new IOException("data translation failed");
            }
            String[] strArrDragQueryFile = dragQueryFile(bArr);
            if (strArrDragQueryFile == null) {
                return null;
            }
            File[] fileArr = new File[strArrDragQueryFile.length];
            for (int i2 = 0; i2 < strArrDragQueryFile.length; i2++) {
                fileArr[i2] = new File(strArrDragQueryFile[i2]);
            }
            marshalledObject = Arrays.asList(fileArr);
        } else if (isURIListFormat(j2) && DataFlavor.javaFileListFlavor.equals(dataFlavor)) {
            byteArrayInputStream = new ByteArrayInputStream(bArr);
            Throwable th = null;
            try {
                try {
                    URI[] uriArrDragQueryURIs = dragQueryURIs(byteArrayInputStream, j2, transferable);
                    if (uriArrDragQueryURIs == null) {
                        if (byteArrayInputStream != null) {
                            if (0 != 0) {
                                try {
                                    byteArrayInputStream.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            } else {
                                byteArrayInputStream.close();
                            }
                        }
                        return null;
                    }
                    ?? arrayList = new ArrayList();
                    for (URI uri : uriArrDragQueryURIs) {
                        try {
                            arrayList.add(new File(uri));
                        } catch (IllegalArgumentException e2) {
                        }
                    }
                    marshalledObject = arrayList;
                    if (byteArrayInputStream != null) {
                        if (0 != 0) {
                            try {
                                byteArrayInputStream.close();
                            } catch (Throwable th3) {
                                th.addSuppressed(th3);
                            }
                        } else {
                            byteArrayInputStream.close();
                        }
                    }
                } finally {
                }
            } finally {
            }
        } else if (String.class.equals(dataFlavor.getRepresentationClass()) && isFlavorCharsetTextType(dataFlavor) && isTextFormat(j2)) {
            marshalledObject = translateBytesToString(bArr, j2, transferable);
        } else if (dataFlavor.isRepresentationClassReader()) {
            byteArrayInputStream = new ByteArrayInputStream(bArr);
            Throwable th4 = null;
            try {
                try {
                    marshalledObject = translateStream(byteArrayInputStream, dataFlavor, j2, transferable);
                    if (byteArrayInputStream != null) {
                        if (0 != 0) {
                            try {
                                byteArrayInputStream.close();
                            } catch (Throwable th5) {
                                th4.addSuppressed(th5);
                            }
                        } else {
                            byteArrayInputStream.close();
                        }
                    }
                } finally {
                }
            } finally {
            }
        } else if (dataFlavor.isRepresentationClassCharBuffer()) {
            if (!isFlavorCharsetTextType(dataFlavor) || !isTextFormat(j2)) {
                throw new IOException("cannot transfer non-text data as CharBuffer");
            }
            marshalledObject = constructFlavoredObject(CharBuffer.wrap(translateBytesToString(bArr, j2, transferable)), dataFlavor, CharBuffer.class);
        } else if (char[].class.equals(dataFlavor.getRepresentationClass())) {
            if (!isFlavorCharsetTextType(dataFlavor) || !isTextFormat(j2)) {
                throw new IOException("cannot transfer non-text data as char array");
            }
            marshalledObject = translateBytesToString(bArr, j2, transferable).toCharArray();
        } else if (dataFlavor.isRepresentationClassByteBuffer()) {
            if (isFlavorCharsetTextType(dataFlavor) && isTextFormat(j2)) {
                bArr = translateBytesToString(bArr, j2, transferable).getBytes(getTextCharset(dataFlavor));
            }
            marshalledObject = constructFlavoredObject(ByteBuffer.wrap(bArr), dataFlavor, ByteBuffer.class);
        } else if (byte[].class.equals(dataFlavor.getRepresentationClass())) {
            marshalledObject = (isFlavorCharsetTextType(dataFlavor) && isTextFormat(j2)) ? translateBytesToString(bArr, j2, transferable).getBytes(getTextCharset(dataFlavor)) : bArr;
        } else if (dataFlavor.isRepresentationClassInputStream()) {
            byteArrayInputStream = new ByteArrayInputStream(bArr);
            Throwable th6 = null;
            try {
                try {
                    marshalledObject = translateStream(byteArrayInputStream, dataFlavor, j2, transferable);
                    if (byteArrayInputStream != null) {
                        if (0 != 0) {
                            try {
                                byteArrayInputStream.close();
                            } catch (Throwable th7) {
                                th6.addSuppressed(th7);
                            }
                        } else {
                            byteArrayInputStream.close();
                        }
                    }
                } finally {
                }
            } finally {
                if (byteArrayInputStream != null) {
                    if (th6 != null) {
                        try {
                            byteArrayInputStream.close();
                        } catch (Throwable th8) {
                            th6.addSuppressed(th8);
                        }
                    } else {
                        byteArrayInputStream.close();
                    }
                }
            }
        } else if (dataFlavor.isRepresentationClassRemote()) {
            try {
                try {
                    ByteArrayInputStream byteArrayInputStream2 = new ByteArrayInputStream(bArr);
                    Throwable th9 = null;
                    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream2);
                    Throwable th10 = null;
                    try {
                        try {
                            marshalledObject = RMI.getMarshalledObject(objectInputStream.readObject());
                            if (objectInputStream != null) {
                                if (0 != 0) {
                                    try {
                                        objectInputStream.close();
                                    } catch (Throwable th11) {
                                        th10.addSuppressed(th11);
                                    }
                                } else {
                                    objectInputStream.close();
                                }
                            }
                            if (byteArrayInputStream2 != null) {
                                if (0 != 0) {
                                    try {
                                        byteArrayInputStream2.close();
                                    } catch (Throwable th12) {
                                        th9.addSuppressed(th12);
                                    }
                                } else {
                                    byteArrayInputStream2.close();
                                }
                            }
                        } catch (Throwable th13) {
                            if (objectInputStream != null) {
                                if (th10 != null) {
                                    try {
                                        objectInputStream.close();
                                    } catch (Throwable th14) {
                                        th10.addSuppressed(th14);
                                    }
                                } else {
                                    objectInputStream.close();
                                }
                            }
                            throw th13;
                        }
                    } finally {
                    }
                } finally {
                }
            } catch (Exception e3) {
                throw new IOException(e3.getMessage());
            }
        } else if (dataFlavor.isRepresentationClassSerializable()) {
            ByteArrayInputStream byteArrayInputStream3 = new ByteArrayInputStream(bArr);
            Throwable th15 = null;
            try {
                try {
                    marshalledObject = translateStream(byteArrayInputStream3, dataFlavor, j2, transferable);
                    if (byteArrayInputStream3 != null) {
                        if (0 != 0) {
                            try {
                                byteArrayInputStream3.close();
                            } catch (Throwable th16) {
                                th15.addSuppressed(th16);
                            }
                        } else {
                            byteArrayInputStream3.close();
                        }
                    }
                } finally {
                    if (byteArrayInputStream3 != null) {
                        if (th15 != null) {
                            try {
                                byteArrayInputStream3.close();
                            } catch (Throwable th17) {
                                th15.addSuppressed(th17);
                            }
                        } else {
                            byteArrayInputStream3.close();
                        }
                    }
                }
            } finally {
            }
        } else if (DataFlavor.imageFlavor.equals(dataFlavor)) {
            if (!isImageFormat(j2)) {
                throw new IOException("data translation failed");
            }
            marshalledObject = platformImageBytesToImage(bArr, j2);
        }
        if (marshalledObject == null) {
            throw new IOException("data translation failed");
        }
        return marshalledObject;
    }

    public Object translateStream(InputStream inputStream, DataFlavor dataFlavor, long j2, Transferable transferable) throws IOException {
        Object marshalledObject = null;
        if (isURIListFormat(j2) && DataFlavor.javaFileListFlavor.equals(dataFlavor)) {
            URI[] uriArrDragQueryURIs = dragQueryURIs(inputStream, j2, transferable);
            if (uriArrDragQueryURIs == null) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            for (URI uri : uriArrDragQueryURIs) {
                try {
                    arrayList.add(new File(uri));
                } catch (IllegalArgumentException e2) {
                }
            }
            marshalledObject = arrayList;
        } else {
            if (String.class.equals(dataFlavor.getRepresentationClass()) && isFlavorCharsetTextType(dataFlavor) && isTextFormat(j2)) {
                return translateBytesToString(inputStreamToByteArray(inputStream), j2, transferable);
            }
            if (DataFlavor.plainTextFlavor.equals(dataFlavor)) {
                marshalledObject = new StringReader(translateBytesToString(inputStreamToByteArray(inputStream), j2, transferable));
            } else if (dataFlavor.isRepresentationClassInputStream()) {
                marshalledObject = translateStreamToInputStream(inputStream, dataFlavor, j2, transferable);
            } else if (dataFlavor.isRepresentationClassReader()) {
                if (!isFlavorCharsetTextType(dataFlavor) || !isTextFormat(j2)) {
                    throw new IOException("cannot transfer non-text data as Reader");
                }
                marshalledObject = constructFlavoredObject(new InputStreamReader((InputStream) translateStreamToInputStream(inputStream, DataFlavor.plainTextFlavor, j2, transferable), getTextCharset(DataFlavor.plainTextFlavor)), dataFlavor, Reader.class);
            } else if (byte[].class.equals(dataFlavor.getRepresentationClass())) {
                if (isFlavorCharsetTextType(dataFlavor) && isTextFormat(j2)) {
                    marshalledObject = translateBytesToString(inputStreamToByteArray(inputStream), j2, transferable).getBytes(getTextCharset(dataFlavor));
                } else {
                    marshalledObject = inputStreamToByteArray(inputStream);
                }
            } else if (dataFlavor.isRepresentationClassRemote()) {
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                    Throwable th = null;
                    try {
                        marshalledObject = RMI.getMarshalledObject(objectInputStream.readObject());
                        if (objectInputStream != null) {
                            if (0 != 0) {
                                try {
                                    objectInputStream.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            } else {
                                objectInputStream.close();
                            }
                        }
                    } finally {
                    }
                } catch (Exception e3) {
                    throw new IOException(e3.getMessage());
                }
            } else if (dataFlavor.isRepresentationClassSerializable()) {
                try {
                    ObjectInputStream objectInputStream2 = new ObjectInputStream(inputStream);
                    Throwable th3 = null;
                    try {
                        try {
                            marshalledObject = objectInputStream2.readObject();
                            if (objectInputStream2 != null) {
                                if (0 != 0) {
                                    try {
                                        objectInputStream2.close();
                                    } catch (Throwable th4) {
                                        th3.addSuppressed(th4);
                                    }
                                } else {
                                    objectInputStream2.close();
                                }
                            }
                        } finally {
                        }
                    } finally {
                    }
                } catch (Exception e4) {
                    throw new IOException(e4.getMessage());
                }
            } else if (DataFlavor.imageFlavor.equals(dataFlavor)) {
                if (!isImageFormat(j2)) {
                    throw new IOException("data translation failed");
                }
                marshalledObject = platformImageBytesToImage(inputStreamToByteArray(inputStream), j2);
            }
        }
        if (marshalledObject == null) {
            throw new IOException("data translation failed");
        }
        return marshalledObject;
    }

    private Object translateStreamToInputStream(InputStream inputStream, DataFlavor dataFlavor, long j2, Transferable transferable) throws IOException {
        if (isFlavorCharsetTextType(dataFlavor) && isTextFormat(j2)) {
            inputStream = new ReencodingInputStream(inputStream, j2, getTextCharset(dataFlavor), transferable);
        }
        return constructFlavoredObject(inputStream, dataFlavor, InputStream.class);
    }

    private Object constructFlavoredObject(Object obj, DataFlavor dataFlavor, Class cls) throws IOException {
        Class<?>[] parameterTypes;
        final Class<?> representationClass = dataFlavor.getRepresentationClass();
        if (cls.equals(representationClass)) {
            return obj;
        }
        try {
            Constructor[] constructorArr = (Constructor[]) AccessController.doPrivileged(new PrivilegedAction() { // from class: sun.awt.datatransfer.DataTransferer.4
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Object run2() {
                    return representationClass.getConstructors();
                }
            });
            Constructor constructor = null;
            int i2 = 0;
            while (true) {
                if (i2 >= constructorArr.length) {
                    break;
                }
                if (!Modifier.isPublic(constructorArr[i2].getModifiers()) || (parameterTypes = constructorArr[i2].getParameterTypes()) == null || parameterTypes.length != 1 || !cls.equals(parameterTypes[0])) {
                    i2++;
                } else {
                    constructor = constructorArr[i2];
                    break;
                }
            }
            if (constructor == null) {
                throw new IOException("can't find <init>(L" + ((Object) cls) + ";)V for class: " + representationClass.getName());
            }
            try {
                return constructor.newInstance(obj);
            } catch (Exception e2) {
                throw new IOException(e2.getMessage());
            }
        } catch (SecurityException e3) {
            throw new IOException(e3.getMessage());
        }
    }

    /* loaded from: rt.jar:sun/awt/datatransfer/DataTransferer$ReencodingInputStream.class */
    public class ReencodingInputStream extends InputStream {
        protected BufferedReader wrapped;
        protected final char[] in = new char[2];
        protected byte[] out;
        protected CharsetEncoder encoder;
        protected CharBuffer inBuf;
        protected ByteBuffer outBuf;
        protected char[] eoln;
        protected int numTerminators;
        protected boolean eos;
        protected int index;
        protected int limit;

        public ReencodingInputStream(InputStream inputStream, long j2, String str, Transferable transferable) throws IOException {
            Long lValueOf = Long.valueOf(j2);
            String charsetForTextFormat = null;
            if (DataTransferer.this.isLocaleDependentTextFormat(j2) && transferable != null && transferable.isDataFlavorSupported(DataTransferer.javaTextEncodingFlavor)) {
                try {
                    charsetForTextFormat = new String((byte[]) transferable.getTransferData(DataTransferer.javaTextEncodingFlavor), "UTF-8");
                } catch (UnsupportedFlavorException e2) {
                }
            } else {
                charsetForTextFormat = DataTransferer.this.getCharsetForTextFormat(lValueOf);
            }
            this.wrapped = new BufferedReader(new InputStreamReader(inputStream, charsetForTextFormat == null ? DataTransferer.getDefaultTextCharset() : charsetForTextFormat));
            if (str == null) {
                throw new NullPointerException("null target encoding");
            }
            try {
                this.encoder = Charset.forName(str).newEncoder();
                this.out = new byte[(int) ((this.encoder.maxBytesPerChar() * 2.0f) + 0.5d)];
                this.inBuf = CharBuffer.wrap(this.in);
                this.outBuf = ByteBuffer.wrap(this.out);
                String str2 = (String) DataTransferer.nativeEOLNs.get(lValueOf);
                if (str2 != null) {
                    this.eoln = str2.toCharArray();
                }
                Integer num = (Integer) DataTransferer.nativeTerminators.get(lValueOf);
                if (num != null) {
                    this.numTerminators = num.intValue();
                }
            } catch (UnsupportedOperationException e3) {
                throw new IOException(e3.toString());
            } catch (IllegalCharsetNameException e4) {
                throw new IOException(e4.toString());
            } catch (UnsupportedCharsetException e5) {
                throw new IOException(e5.toString());
            }
        }

        private int readChar() throws IOException {
            int i2 = this.wrapped.read();
            if (i2 == -1) {
                this.eos = true;
                return -1;
            }
            if (this.numTerminators > 0 && i2 == 0) {
                this.eos = true;
                return -1;
            }
            if (this.eoln != null && matchCharArray(this.eoln, i2)) {
                i2 = 10;
            }
            return i2;
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            int i2;
            if (this.eos) {
                return -1;
            }
            if (this.index >= this.limit) {
                int i3 = readChar();
                if (i3 == -1) {
                    return -1;
                }
                this.in[0] = (char) i3;
                this.in[1] = 0;
                this.inBuf.limit(1);
                if (Character.isHighSurrogate((char) i3) && (i2 = readChar()) != -1) {
                    this.in[1] = (char) i2;
                    this.inBuf.limit(2);
                }
                this.inBuf.rewind();
                this.outBuf.limit(this.out.length).rewind();
                this.encoder.encode(this.inBuf, this.outBuf, false);
                this.outBuf.flip();
                this.limit = this.outBuf.limit();
                this.index = 0;
                return read();
            }
            byte[] bArr = this.out;
            int i4 = this.index;
            this.index = i4 + 1;
            return bArr[i4] & 255;
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            if (this.eos) {
                return 0;
            }
            return this.limit - this.index;
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.wrapped.close();
        }

        private boolean matchCharArray(char[] cArr, int i2) throws IOException {
            int i3;
            this.wrapped.mark(cArr.length);
            int i4 = 0;
            if (((char) i2) == cArr[0]) {
                i4 = 1;
                while (i4 < cArr.length && (i3 = this.wrapped.read()) != -1 && ((char) i3) == cArr[i4]) {
                    i4++;
                }
            }
            if (i4 == cArr.length) {
                return true;
            }
            this.wrapped.reset();
            return false;
        }
    }

    protected URI[] dragQueryURIs(InputStream inputStream, long j2, Transferable transferable) throws IOException {
        throw new IOException(new UnsupportedOperationException("not implemented on this platform"));
    }

    protected Image standardImageBytesToImage(byte[] bArr, String str) throws IOException {
        ByteArrayInputStream byteArrayInputStream;
        Throwable th;
        ImageInputStream imageInputStreamCreateImageInputStream;
        BufferedImage bufferedImage;
        Iterator<ImageReader> imageReadersByMIMEType = ImageIO.getImageReadersByMIMEType(str);
        if (!imageReadersByMIMEType.hasNext()) {
            throw new IOException("No registered service provider can decode  an image from " + str);
        }
        IOException iOException = null;
        while (imageReadersByMIMEType.hasNext()) {
            ImageReader next = imageReadersByMIMEType.next();
            try {
                byteArrayInputStream = new ByteArrayInputStream(bArr);
                th = null;
                try {
                    try {
                        imageInputStreamCreateImageInputStream = ImageIO.createImageInputStream(byteArrayInputStream);
                        try {
                            ImageReadParam defaultReadParam = next.getDefaultReadParam();
                            next.setInput(imageInputStreamCreateImageInputStream, true, true);
                            bufferedImage = next.read(next.getMinIndex(), defaultReadParam);
                        } catch (Throwable th2) {
                            imageInputStreamCreateImageInputStream.close();
                            next.dispose();
                            throw th2;
                        }
                    } finally {
                    }
                } catch (Throwable th3) {
                    th = th3;
                    throw th3;
                }
            } catch (IOException e2) {
                iOException = e2;
            }
            if (bufferedImage != null) {
                imageInputStreamCreateImageInputStream.close();
                next.dispose();
                if (byteArrayInputStream != null) {
                    if (0 != 0) {
                        try {
                            byteArrayInputStream.close();
                        } catch (Throwable th4) {
                            th.addSuppressed(th4);
                        }
                    } else {
                        byteArrayInputStream.close();
                    }
                }
                return bufferedImage;
            }
            imageInputStreamCreateImageInputStream.close();
            next.dispose();
            if (byteArrayInputStream != null) {
                if (0 != 0) {
                    try {
                        byteArrayInputStream.close();
                    } catch (Throwable th5) {
                        th.addSuppressed(th5);
                    }
                } else {
                    byteArrayInputStream.close();
                }
            }
            iOException = e2;
        }
        if (iOException == null) {
            iOException = new IOException("Registered service providers failed to decode an image from " + str);
        }
        throw iOException;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected byte[] imageToStandardBytes(Image image, String str) throws IOException {
        int width;
        int height;
        IOException iOException = null;
        if (!ImageIO.getImageWritersByMIMEType(str).hasNext()) {
            throw new IOException("No registered service provider can encode  an image to " + str);
        }
        if (image instanceof RenderedImage) {
            try {
                return imageToStandardBytesImpl((RenderedImage) image, str);
            } catch (IOException e2) {
                iOException = e2;
            }
        }
        if (image instanceof ToolkitImage) {
            ImageRepresentation imageRep = ((ToolkitImage) image).getImageRep();
            imageRep.reconstruct(32);
            width = imageRep.getWidth();
            height = imageRep.getHeight();
        } else {
            width = image.getWidth(null);
            height = image.getHeight(null);
        }
        ColorModel rGBdefault = ColorModel.getRGBdefault();
        BufferedImage bufferedImage = new BufferedImage(rGBdefault, rGBdefault.createCompatibleWritableRaster(width, height), rGBdefault.isAlphaPremultiplied(), (Hashtable<?, ?>) null);
        Graphics graphics = bufferedImage.getGraphics();
        try {
            graphics.drawImage(image, 0, 0, width, height, null);
            graphics.dispose();
            try {
                return imageToStandardBytesImpl(bufferedImage, str);
            } catch (IOException e3) {
                if (iOException != null) {
                    throw iOException;
                }
                throw e3;
            }
        } catch (Throwable th) {
            graphics.dispose();
            throw th;
        }
    }

    /* JADX WARN: Finally extract failed */
    protected byte[] imageToStandardBytesImpl(RenderedImage renderedImage, String str) throws IOException {
        Iterator<ImageWriter> imageWritersByMIMEType = ImageIO.getImageWritersByMIMEType(str);
        ImageTypeSpecifier imageTypeSpecifier = new ImageTypeSpecifier(renderedImage);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOException iOException = null;
        while (imageWritersByMIMEType.hasNext()) {
            ImageWriter next = imageWritersByMIMEType.next();
            if (next.getOriginatingProvider().canEncodeImage(imageTypeSpecifier)) {
                try {
                    ImageOutputStream imageOutputStreamCreateImageOutputStream = ImageIO.createImageOutputStream(byteArrayOutputStream);
                    try {
                        next.setOutput(imageOutputStreamCreateImageOutputStream);
                        next.write(renderedImage);
                        imageOutputStreamCreateImageOutputStream.flush();
                        imageOutputStreamCreateImageOutputStream.close();
                        next.dispose();
                        byteArrayOutputStream.close();
                        return byteArrayOutputStream.toByteArray();
                    } catch (Throwable th) {
                        imageOutputStreamCreateImageOutputStream.close();
                        throw th;
                    }
                } catch (IOException e2) {
                    next.dispose();
                    byteArrayOutputStream.reset();
                    iOException = e2;
                }
            }
        }
        byteArrayOutputStream.close();
        if (iOException == null) {
            iOException = new IOException("Registered service providers failed to encode " + ((Object) renderedImage) + " to " + str);
        }
        throw iOException;
    }

    private Object concatData(Object obj, Object obj2) {
        InputStream byteArrayInputStream;
        InputStream byteArrayInputStream2;
        if (obj instanceof byte[]) {
            byte[] bArr = (byte[]) obj;
            if (obj2 instanceof byte[]) {
                byte[] bArr2 = (byte[]) obj2;
                byte[] bArr3 = new byte[bArr.length + bArr2.length];
                System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
                System.arraycopy(bArr2, 0, bArr3, bArr.length, bArr2.length);
                return bArr3;
            }
            byteArrayInputStream = new ByteArrayInputStream(bArr);
            byteArrayInputStream2 = (InputStream) obj2;
        } else {
            byteArrayInputStream = (InputStream) obj;
            if (obj2 instanceof byte[]) {
                byteArrayInputStream2 = new ByteArrayInputStream((byte[]) obj2);
            } else {
                byteArrayInputStream2 = (InputStream) obj2;
            }
        }
        return new SequenceInputStream(byteArrayInputStream, byteArrayInputStream2);
    }

    public byte[] convertData(Object obj, final Transferable transferable, final long j2, final Map map, boolean z2) throws IOException {
        byte[] bArrTranslateTransferable = null;
        if (z2) {
            try {
                final Stack stack = new Stack();
                Runnable runnable = new Runnable() { // from class: sun.awt.datatransfer.DataTransferer.5
                    private boolean done = false;

                    @Override // java.lang.Runnable
                    public void run() {
                        if (this.done) {
                            return;
                        }
                        byte[] bArrTranslateTransferable2 = null;
                        try {
                            DataFlavor dataFlavor = (DataFlavor) map.get(Long.valueOf(j2));
                            if (dataFlavor != null) {
                                bArrTranslateTransferable2 = DataTransferer.this.translateTransferable(transferable, dataFlavor, j2);
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            bArrTranslateTransferable2 = null;
                        }
                        try {
                            DataTransferer.this.getToolkitThreadBlockedHandler().lock();
                            stack.push(bArrTranslateTransferable2);
                            DataTransferer.this.getToolkitThreadBlockedHandler().exit();
                        } finally {
                            DataTransferer.this.getToolkitThreadBlockedHandler().unlock();
                            this.done = true;
                        }
                    }
                };
                AppContext appContextTargetToAppContext = SunToolkit.targetToAppContext(obj);
                getToolkitThreadBlockedHandler().lock();
                if (appContextTargetToAppContext != null) {
                    appContextTargetToAppContext.put(DATA_CONVERTER_KEY, runnable);
                }
                SunToolkit.executeOnEventHandlerThread(obj, runnable);
                while (stack.empty()) {
                    getToolkitThreadBlockedHandler().enter();
                }
                if (appContextTargetToAppContext != null) {
                    appContextTargetToAppContext.remove(DATA_CONVERTER_KEY);
                }
                bArrTranslateTransferable = (byte[]) stack.pop();
                getToolkitThreadBlockedHandler().unlock();
            } catch (Throwable th) {
                getToolkitThreadBlockedHandler().unlock();
                throw th;
            }
        } else {
            DataFlavor dataFlavor = (DataFlavor) map.get(Long.valueOf(j2));
            if (dataFlavor != null) {
                bArrTranslateTransferable = translateTransferable(transferable, dataFlavor, j2);
            }
        }
        return bArrTranslateTransferable;
    }

    public void processDataConversionRequests() {
        if (EventQueue.isDispatchThread()) {
            AppContext appContext = AppContext.getAppContext();
            getToolkitThreadBlockedHandler().lock();
            try {
                Runnable runnable = (Runnable) appContext.get(DATA_CONVERTER_KEY);
                if (runnable != null) {
                    runnable.run();
                    appContext.remove(DATA_CONVERTER_KEY);
                }
            } finally {
                getToolkitThreadBlockedHandler().unlock();
            }
        }
    }

    public static long[] keysToLongArray(SortedMap sortedMap) {
        Set setKeySet = sortedMap.keySet();
        long[] jArr = new long[setKeySet.size()];
        int i2 = 0;
        Iterator it = setKeySet.iterator();
        while (it.hasNext()) {
            jArr[i2] = ((Long) it.next()).longValue();
            i2++;
        }
        return jArr;
    }

    public static DataFlavor[] setToSortedDataFlavorArray(Set set) {
        DataFlavor[] dataFlavorArr = new DataFlavor[set.size()];
        set.toArray(dataFlavorArr);
        Arrays.sort(dataFlavorArr, new DataFlavorComparator(false));
        return dataFlavorArr;
    }

    protected static byte[] inputStreamToByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Throwable th = null;
        try {
            byte[] bArr = new byte[8192];
            while (true) {
                int i2 = inputStream.read(bArr);
                if (i2 == -1) {
                    break;
                }
                byteArrayOutputStream.write(bArr, 0, i2);
            }
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            if (byteArrayOutputStream != null) {
                if (0 != 0) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    byteArrayOutputStream.close();
                }
            }
            return byteArray;
        } catch (Throwable th3) {
            if (byteArrayOutputStream != null) {
                if (0 != 0) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    byteArrayOutputStream.close();
                }
            }
            throw th3;
        }
    }

    public LinkedHashSet<DataFlavor> getPlatformMappingsForNative(String str) {
        return new LinkedHashSet<>();
    }

    public LinkedHashSet<String> getPlatformMappingsForFlavor(DataFlavor dataFlavor) {
        return new LinkedHashSet<>();
    }

    /* loaded from: rt.jar:sun/awt/datatransfer/DataTransferer$IndexedComparator.class */
    public static abstract class IndexedComparator implements Comparator {
        public static final boolean SELECT_BEST = true;
        public static final boolean SELECT_WORST = false;
        protected final boolean order;

        public IndexedComparator() {
            this(true);
        }

        public IndexedComparator(boolean z2) {
            this.order = z2;
        }

        protected static int compareIndices(Map map, Object obj, Object obj2, Integer num) {
            Integer num2 = (Integer) map.get(obj);
            Integer num3 = (Integer) map.get(obj2);
            if (num2 == null) {
                num2 = num;
            }
            if (num3 == null) {
                num3 = num;
            }
            return num2.compareTo(num3);
        }

        protected static int compareLongs(Map map, Object obj, Object obj2, Long l2) {
            Long l3 = (Long) map.get(obj);
            Long l4 = (Long) map.get(obj2);
            if (l3 == null) {
                l3 = l2;
            }
            if (l4 == null) {
                l4 = l2;
            }
            return l3.compareTo(l4);
        }
    }

    /* loaded from: rt.jar:sun/awt/datatransfer/DataTransferer$CharsetComparator.class */
    public static class CharsetComparator extends IndexedComparator {
        private static final Map charsets;
        private static String defaultEncoding;
        private static final Integer DEFAULT_CHARSET_INDEX = 2;
        private static final Integer OTHER_CHARSET_INDEX = 1;
        private static final Integer WORST_CHARSET_INDEX = 0;
        private static final Integer UNSUPPORTED_CHARSET_INDEX = Integer.MIN_VALUE;
        private static final String UNSUPPORTED_CHARSET = "UNSUPPORTED";

        static {
            HashMap map = new HashMap(8, 1.0f);
            map.put(DataTransferer.canonicalName("UTF-16LE"), 4);
            map.put(DataTransferer.canonicalName(FastInfosetSerializer.UTF_16BE), 5);
            map.put(DataTransferer.canonicalName("UTF-8"), 6);
            map.put(DataTransferer.canonicalName("UTF-16"), 7);
            map.put(DataTransferer.canonicalName("US-ASCII"), WORST_CHARSET_INDEX);
            DataTransferer.canonicalName(DataTransferer.getDefaultTextCharset());
            if (map.get(defaultEncoding) == null) {
                map.put(defaultEncoding, DEFAULT_CHARSET_INDEX);
            }
            map.put(UNSUPPORTED_CHARSET, UNSUPPORTED_CHARSET_INDEX);
            charsets = Collections.unmodifiableMap(map);
        }

        public CharsetComparator() {
            this(true);
        }

        public CharsetComparator(boolean z2) {
            super(z2);
        }

        @Override // java.util.Comparator
        public int compare(Object obj, Object obj2) {
            String str;
            String str2;
            if (this.order) {
                str = (String) obj;
                str2 = (String) obj2;
            } else {
                str = (String) obj2;
                str2 = (String) obj;
            }
            return compareCharsets(str, str2);
        }

        protected int compareCharsets(String str, String str2) {
            String encoding = getEncoding(str);
            String encoding2 = getEncoding(str2);
            int iCompareIndices = compareIndices(charsets, encoding, encoding2, OTHER_CHARSET_INDEX);
            if (iCompareIndices == 0) {
                return encoding2.compareTo(encoding);
            }
            return iCompareIndices;
        }

        protected static String getEncoding(String str) {
            if (str == null) {
                return null;
            }
            if (!DataTransferer.isEncodingSupported(str)) {
                return UNSUPPORTED_CHARSET;
            }
            String strCanonicalName = DataTransferer.canonicalName(str);
            return charsets.containsKey(strCanonicalName) ? strCanonicalName : str;
        }
    }

    /* loaded from: rt.jar:sun/awt/datatransfer/DataTransferer$DataFlavorComparator.class */
    public static class DataFlavorComparator extends IndexedComparator {
        private final CharsetComparator charsetComparator;
        private static final Map exactTypes;
        private static final Map primaryTypes;
        private static final Map nonTextRepresentations;
        private static final Map textTypes;
        private static final Map decodedTextRepresentations;
        private static final Map encodedTextRepresentations;
        private static final Integer UNKNOWN_OBJECT_LOSES = Integer.MIN_VALUE;
        private static final Integer UNKNOWN_OBJECT_WINS = Integer.MAX_VALUE;
        private static final Long UNKNOWN_OBJECT_LOSES_L = Long.MIN_VALUE;
        private static final Long UNKNOWN_OBJECT_WINS_L = Long.valueOf(Long.MAX_VALUE);

        static {
            HashMap map = new HashMap(4, 1.0f);
            map.put(Clipboard.FILE_LIST_TYPE, 0);
            map.put(DataFlavor.javaSerializedObjectMimeType, 1);
            map.put(DataFlavor.javaJVMLocalObjectMimeType, 2);
            map.put(DataFlavor.javaRemoteObjectMimeType, 3);
            exactTypes = Collections.unmodifiableMap(map);
            HashMap map2 = new HashMap(1, 1.0f);
            map2.put("application", 0);
            primaryTypes = Collections.unmodifiableMap(map2);
            HashMap map3 = new HashMap(3, 1.0f);
            map3.put(InputStream.class, 0);
            map3.put(Serializable.class, 1);
            Class<?> clsRemoteClass = RMI.remoteClass();
            if (clsRemoteClass != null) {
                map3.put(clsRemoteClass, 2);
            }
            nonTextRepresentations = Collections.unmodifiableMap(map3);
            HashMap map4 = new HashMap(16, 1.0f);
            map4.put(Clipboard.TEXT_TYPE, 0);
            map4.put(DataFlavor.javaSerializedObjectMimeType, 1);
            map4.put("text/calendar", 2);
            map4.put("text/css", 3);
            map4.put("text/directory", 4);
            map4.put("text/parityfec", 5);
            map4.put("text/rfc822-headers", 6);
            map4.put("text/t140", 7);
            map4.put("text/tab-separated-values", 8);
            map4.put(Clipboard.URI_TYPE, 9);
            map4.put("text/richtext", 10);
            map4.put("text/enriched", 11);
            map4.put(Clipboard.RTF_TYPE, 12);
            map4.put(Clipboard.HTML_TYPE, 13);
            map4.put("text/xml", 14);
            map4.put("text/sgml", 15);
            textTypes = Collections.unmodifiableMap(map4);
            HashMap map5 = new HashMap(4, 1.0f);
            map5.put(char[].class, 0);
            map5.put(CharBuffer.class, 1);
            map5.put(String.class, 2);
            map5.put(Reader.class, 3);
            decodedTextRepresentations = Collections.unmodifiableMap(map5);
            HashMap map6 = new HashMap(3, 1.0f);
            map6.put(byte[].class, 0);
            map6.put(ByteBuffer.class, 1);
            map6.put(InputStream.class, 2);
            encodedTextRepresentations = Collections.unmodifiableMap(map6);
        }

        public DataFlavorComparator() {
            this(true);
        }

        public DataFlavorComparator(boolean z2) {
            super(z2);
            this.charsetComparator = new CharsetComparator(z2);
        }

        @Override // java.util.Comparator
        public int compare(Object obj, Object obj2) {
            DataFlavor dataFlavor;
            DataFlavor dataFlavor2;
            if (this.order) {
                dataFlavor = (DataFlavor) obj;
                dataFlavor2 = (DataFlavor) obj2;
            } else {
                dataFlavor = (DataFlavor) obj2;
                dataFlavor2 = (DataFlavor) obj;
            }
            if (dataFlavor.equals(dataFlavor2)) {
                return 0;
            }
            String primaryType = dataFlavor.getPrimaryType();
            String str = primaryType + "/" + dataFlavor.getSubType();
            Class<?> representationClass = dataFlavor.getRepresentationClass();
            String primaryType2 = dataFlavor2.getPrimaryType();
            String str2 = primaryType2 + "/" + dataFlavor2.getSubType();
            Class<?> representationClass2 = dataFlavor2.getRepresentationClass();
            if (dataFlavor.isFlavorTextType() && dataFlavor2.isFlavorTextType()) {
                int iCompareIndices = compareIndices(textTypes, str, str2, UNKNOWN_OBJECT_LOSES);
                if (iCompareIndices != 0) {
                    return iCompareIndices;
                }
                if (DataTransferer.doesSubtypeSupportCharset(dataFlavor)) {
                    int iCompareIndices2 = compareIndices(decodedTextRepresentations, representationClass, representationClass2, UNKNOWN_OBJECT_LOSES);
                    if (iCompareIndices2 != 0) {
                        return iCompareIndices2;
                    }
                    int iCompareCharsets = this.charsetComparator.compareCharsets(DataTransferer.getTextCharset(dataFlavor), DataTransferer.getTextCharset(dataFlavor2));
                    if (iCompareCharsets != 0) {
                        return iCompareCharsets;
                    }
                }
                int iCompareIndices3 = compareIndices(encodedTextRepresentations, representationClass, representationClass2, UNKNOWN_OBJECT_LOSES);
                if (iCompareIndices3 != 0) {
                    return iCompareIndices3;
                }
            } else {
                if (dataFlavor.isFlavorTextType()) {
                    return 1;
                }
                if (dataFlavor2.isFlavorTextType()) {
                    return -1;
                }
                int iCompareIndices4 = compareIndices(primaryTypes, primaryType, primaryType2, UNKNOWN_OBJECT_LOSES);
                if (iCompareIndices4 != 0) {
                    return iCompareIndices4;
                }
                int iCompareIndices5 = compareIndices(exactTypes, str, str2, UNKNOWN_OBJECT_WINS);
                if (iCompareIndices5 != 0) {
                    return iCompareIndices5;
                }
                int iCompareIndices6 = compareIndices(nonTextRepresentations, representationClass, representationClass2, UNKNOWN_OBJECT_LOSES);
                if (iCompareIndices6 != 0) {
                    return iCompareIndices6;
                }
            }
            return dataFlavor.getMimeType().compareTo(dataFlavor2.getMimeType());
        }
    }

    /* loaded from: rt.jar:sun/awt/datatransfer/DataTransferer$IndexOrderComparator.class */
    public static class IndexOrderComparator extends IndexedComparator {
        private final Map indexMap;
        private static final Integer FALLBACK_INDEX = Integer.MIN_VALUE;

        public IndexOrderComparator(Map map) {
            super(true);
            this.indexMap = map;
        }

        public IndexOrderComparator(Map map, boolean z2) {
            super(z2);
            this.indexMap = map;
        }

        @Override // java.util.Comparator
        public int compare(Object obj, Object obj2) {
            if (!this.order) {
                return -compareIndices(this.indexMap, obj, obj2, FALLBACK_INDEX);
            }
            return compareIndices(this.indexMap, obj, obj2, FALLBACK_INDEX);
        }
    }

    /* loaded from: rt.jar:sun/awt/datatransfer/DataTransferer$RMI.class */
    private static class RMI {
        private static final Class<?> remoteClass = getClass("java.rmi.Remote");
        private static final Class<?> marshallObjectClass = getClass("java.rmi.MarshalledObject");
        private static final Constructor<?> marshallCtor = getConstructor(marshallObjectClass, Object.class);
        private static final Method marshallGet = getMethod(marshallObjectClass, "get", new Class[0]);

        private RMI() {
        }

        private static Class<?> getClass(String str) {
            try {
                return Class.forName(str, true, null);
            } catch (ClassNotFoundException e2) {
                return null;
            }
        }

        private static Constructor<?> getConstructor(Class<?> cls, Class<?>... clsArr) {
            if (cls == null) {
                return null;
            }
            try {
                return cls.getDeclaredConstructor(clsArr);
            } catch (NoSuchMethodException e2) {
                throw new AssertionError(e2);
            }
        }

        private static Method getMethod(Class<?> cls, String str, Class<?>... clsArr) {
            if (cls == null) {
                return null;
            }
            try {
                return cls.getMethod(str, clsArr);
            } catch (NoSuchMethodException e2) {
                throw new AssertionError(e2);
            }
        }

        static boolean isRemote(Class<?> cls) {
            return (remoteClass == null ? null : Boolean.valueOf(remoteClass.isAssignableFrom(cls))).booleanValue();
        }

        static Class<?> remoteClass() {
            return remoteClass;
        }

        static Object newMarshalledObject(Object obj) throws IOException {
            try {
                return marshallCtor.newInstance(obj);
            } catch (IllegalAccessException e2) {
                throw new AssertionError(e2);
            } catch (InstantiationException e3) {
                throw new AssertionError(e3);
            } catch (InvocationTargetException e4) {
                Throwable cause = e4.getCause();
                if (cause instanceof IOException) {
                    throw ((IOException) cause);
                }
                throw new AssertionError(e4);
            }
        }

        static Object getMarshalledObject(Object obj) throws IOException, ClassNotFoundException {
            try {
                return marshallGet.invoke(obj, new Object[0]);
            } catch (IllegalAccessException e2) {
                throw new AssertionError(e2);
            } catch (InvocationTargetException e3) {
                Throwable cause = e3.getCause();
                if (cause instanceof IOException) {
                    throw ((IOException) cause);
                }
                if (cause instanceof ClassNotFoundException) {
                    throw ((ClassNotFoundException) cause);
                }
                throw new AssertionError(e3);
            }
        }
    }
}
