package java.awt.datatransfer;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.net.ftp.FTP;
import sun.awt.AppContext;
import sun.awt.datatransfer.DataTransferer;

/* loaded from: rt.jar:java/awt/datatransfer/SystemFlavorMap.class */
public final class SystemFlavorMap implements FlavorMap, FlavorTable {
    private static final String keyValueSeparators = "=: \t\r\n\f";
    private static final String strictKeyValueSeparators = "=:";
    private static final String whiteSpaceChars = " \t\r\n\f";
    private static final String TEXT_PLAIN_BASE_TYPE = "text/plain";
    private static final String HTML_TEXT_BASE_TYPE = "text/html";
    private final Map<String, LinkedHashSet<DataFlavor>> nativeToFlavor = new HashMap();
    private final Map<DataFlavor, LinkedHashSet<String>> flavorToNative = new HashMap();
    private Map<String, LinkedHashSet<String>> textTypeToNative = new HashMap();
    private boolean isMapInitialized = false;
    private final SoftCache<DataFlavor, String> nativesForFlavorCache = new SoftCache<>();
    private final SoftCache<String, DataFlavor> flavorsForNativeCache = new SoftCache<>();
    private Set<Object> disabledMappingGenerationKeys = new HashSet();
    private static String JavaMIME = "JAVA_DATAFLAVOR:";
    private static final Object FLAVOR_MAP_KEY = new Object();
    private static final String[] UNICODE_TEXT_CLASSES = {"java.io.Reader", "java.lang.String", "java.nio.CharBuffer", "\"[C\""};
    private static final String[] ENCODED_TEXT_CLASSES = {"java.io.InputStream", "java.nio.ByteBuffer", "\"[B\""};
    private static final String[] htmlDocumntTypes = {"all", "selection", "fragment"};

    private Map<String, LinkedHashSet<DataFlavor>> getNativeToFlavor() {
        if (!this.isMapInitialized) {
            initSystemFlavorMap();
        }
        return this.nativeToFlavor;
    }

    private synchronized Map<DataFlavor, LinkedHashSet<String>> getFlavorToNative() {
        if (!this.isMapInitialized) {
            initSystemFlavorMap();
        }
        return this.flavorToNative;
    }

    private synchronized Map<String, LinkedHashSet<String>> getTextTypeToNative() {
        if (!this.isMapInitialized) {
            initSystemFlavorMap();
            this.textTypeToNative = Collections.unmodifiableMap(this.textTypeToNative);
        }
        return this.textTypeToNative;
    }

    public static FlavorMap getDefaultFlavorMap() {
        AppContext appContext = AppContext.getAppContext();
        FlavorMap systemFlavorMap = (FlavorMap) appContext.get(FLAVOR_MAP_KEY);
        if (systemFlavorMap == null) {
            systemFlavorMap = new SystemFlavorMap();
            appContext.put(FLAVOR_MAP_KEY, systemFlavorMap);
        }
        return systemFlavorMap;
    }

    private SystemFlavorMap() {
    }

    private void initSystemFlavorMap() {
        if (this.isMapInitialized) {
            return;
        }
        this.isMapInitialized = true;
        BufferedReader bufferedReader = (BufferedReader) AccessController.doPrivileged(new PrivilegedAction<BufferedReader>() { // from class: java.awt.datatransfer.SystemFlavorMap.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public BufferedReader run2() {
                String str = System.getProperty("java.home") + File.separator + "lib" + File.separator + "flavormap.properties";
                try {
                    return new BufferedReader(new InputStreamReader(new File(str).toURI().toURL().openStream(), FTP.DEFAULT_CONTROL_ENCODING));
                } catch (MalformedURLException e2) {
                    System.err.println("MalformedURLException:" + ((Object) e2) + " while loading default flavormap.properties file:" + str);
                    return null;
                } catch (IOException e3) {
                    System.err.println("IOException:" + ((Object) e3) + " while loading default flavormap.properties file:" + str);
                    return null;
                }
            }
        });
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: java.awt.datatransfer.SystemFlavorMap.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                return Toolkit.getProperty("AWT.DnD.flavorMapFileURL", null);
            }
        });
        if (bufferedReader != null) {
            try {
                parseAndStoreReader(bufferedReader);
            } catch (IOException e2) {
                System.err.println("IOException:" + ((Object) e2) + " while parsing default flavormap.properties file");
            }
        }
        BufferedReader bufferedReader2 = null;
        if (str != null) {
            try {
                bufferedReader2 = new BufferedReader(new InputStreamReader(new URL(str).openStream(), FTP.DEFAULT_CONTROL_ENCODING));
            } catch (SecurityException e3) {
            } catch (MalformedURLException e4) {
                System.err.println("MalformedURLException:" + ((Object) e4) + " while reading AWT.DnD.flavorMapFileURL:" + str);
            } catch (IOException e5) {
                System.err.println("IOException:" + ((Object) e5) + " while reading AWT.DnD.flavorMapFileURL:" + str);
            }
        }
        if (bufferedReader2 != null) {
            try {
                parseAndStoreReader(bufferedReader2);
            } catch (IOException e6) {
                System.err.println("IOException:" + ((Object) e6) + " while parsing AWT.DnD.flavorMapFileURL");
            }
        }
    }

    private void parseAndStoreReader(BufferedReader bufferedReader) throws IOException {
        char cCharAt;
        DataFlavor dataFlavor;
        DataTransferer dataTransferer;
        while (true) {
            String line = bufferedReader.readLine();
            if (line == null) {
                return;
            }
            if (line.length() > 0 && (cCharAt = line.charAt(0)) != '#' && cCharAt != '!') {
                while (continueLine(line)) {
                    String line2 = bufferedReader.readLine();
                    if (line2 == null) {
                        line2 = "";
                    }
                    String strSubstring = line.substring(0, line.length() - 1);
                    int i2 = 0;
                    while (i2 < line2.length() && whiteSpaceChars.indexOf(line2.charAt(i2)) != -1) {
                        i2++;
                    }
                    line = strSubstring + line2.substring(i2, line2.length());
                }
                int length = line.length();
                int i3 = 0;
                while (i3 < length && whiteSpaceChars.indexOf(line.charAt(i3)) != -1) {
                    i3++;
                }
                if (i3 != length) {
                    int i4 = i3;
                    while (i4 < length) {
                        char cCharAt2 = line.charAt(i4);
                        if (cCharAt2 == '\\') {
                            i4++;
                        } else if (keyValueSeparators.indexOf(cCharAt2) != -1) {
                            break;
                        }
                        i4++;
                    }
                    int i5 = i4;
                    while (i5 < length && whiteSpaceChars.indexOf(line.charAt(i5)) != -1) {
                        i5++;
                    }
                    if (i5 < length && strictKeyValueSeparators.indexOf(line.charAt(i5)) != -1) {
                        i5++;
                    }
                    while (i5 < length && whiteSpaceChars.indexOf(line.charAt(i5)) != -1) {
                        i5++;
                    }
                    String strSubstring2 = line.substring(i3, i4);
                    String strSubstring3 = i4 < length ? line.substring(i5, length) : "";
                    String strLoadConvert = loadConvert(strSubstring2);
                    String strLoadConvert2 = loadConvert(strSubstring3);
                    try {
                        MimeType mimeType = new MimeType(strLoadConvert2);
                        if ("text".equals(mimeType.getPrimaryType())) {
                            String parameter = mimeType.getParameter("charset");
                            if (DataTransferer.doesSubtypeSupportCharset(mimeType.getSubType(), parameter) && (dataTransferer = DataTransferer.getInstance()) != null) {
                                dataTransferer.registerTextFlavorProperties(strLoadConvert, parameter, mimeType.getParameter("eoln"), mimeType.getParameter("terminators"));
                            }
                            mimeType.removeParameter("charset");
                            mimeType.removeParameter(Constants.ATTRNAME_CLASS);
                            mimeType.removeParameter("eoln");
                            mimeType.removeParameter("terminators");
                            strLoadConvert2 = mimeType.toString();
                        }
                        try {
                            dataFlavor = new DataFlavor(strLoadConvert2);
                        } catch (Exception e2) {
                            try {
                                dataFlavor = new DataFlavor(strLoadConvert2, (String) null);
                            } catch (Exception e3) {
                                e3.printStackTrace();
                            }
                        }
                        LinkedHashSet linkedHashSet = new LinkedHashSet();
                        linkedHashSet.add(dataFlavor);
                        if ("text".equals(dataFlavor.getPrimaryType())) {
                            linkedHashSet.addAll(convertMimeTypeToDataFlavors(strLoadConvert2));
                            store(dataFlavor.mimeType.getBaseType(), strLoadConvert, getTextTypeToNative());
                        }
                        Iterator<E> it = linkedHashSet.iterator();
                        while (it.hasNext()) {
                            DataFlavor dataFlavor2 = (DataFlavor) it.next();
                            store(dataFlavor2, strLoadConvert, getFlavorToNative());
                            store(strLoadConvert, dataFlavor2, getNativeToFlavor());
                        }
                    } catch (MimeTypeParseException e4) {
                        e4.printStackTrace();
                    }
                }
            }
        }
    }

    private boolean continueLine(String str) {
        int i2 = 0;
        int length = str.length() - 1;
        while (length >= 0) {
            int i3 = length;
            length--;
            if (str.charAt(i3) != '\\') {
                break;
            }
            i2++;
        }
        return i2 % 2 == 1;
    }

    private String loadConvert(String str) {
        int i2;
        int i3;
        int length = str.length();
        StringBuilder sb = new StringBuilder(length);
        int i4 = 0;
        while (i4 < length) {
            int i5 = i4;
            i4++;
            char cCharAt = str.charAt(i5);
            if (cCharAt == '\\') {
                i4++;
                char cCharAt2 = str.charAt(i4);
                if (cCharAt2 == 'u') {
                    int i6 = 0;
                    for (int i7 = 0; i7 < 4; i7++) {
                        int i8 = i4;
                        i4++;
                        char cCharAt3 = str.charAt(i8);
                        switch (cCharAt3) {
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
                                i2 = (i6 << 4) + cCharAt3;
                                i3 = 48;
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
                                i2 = (i6 << 4) + 10 + cCharAt3;
                                i3 = 65;
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                i2 = (i6 << 4) + 10 + cCharAt3;
                                i3 = 97;
                                break;
                        }
                        i6 = i2 - i3;
                    }
                    sb.append((char) i6);
                } else {
                    if (cCharAt2 == 't') {
                        cCharAt2 = '\t';
                    } else if (cCharAt2 == 'r') {
                        cCharAt2 = '\r';
                    } else if (cCharAt2 == 'n') {
                        cCharAt2 = '\n';
                    } else if (cCharAt2 == 'f') {
                        cCharAt2 = '\f';
                    }
                    sb.append(cCharAt2);
                }
            } else {
                sb.append(cCharAt);
            }
        }
        return sb.toString();
    }

    private <H, L> void store(H h2, L l2, Map<H, LinkedHashSet<L>> map) {
        LinkedHashSet<L> linkedHashSet = map.get(h2);
        if (linkedHashSet == null) {
            linkedHashSet = new LinkedHashSet<>(1);
            map.put(h2, linkedHashSet);
        }
        if (!linkedHashSet.contains(l2)) {
            linkedHashSet.add(l2);
        }
    }

    private LinkedHashSet<DataFlavor> nativeToFlavorLookup(String str) {
        DataTransferer dataTransferer;
        LinkedHashSet<DataFlavor> linkedHashSet = getNativeToFlavor().get(str);
        if (str != null && !this.disabledMappingGenerationKeys.contains(str) && (dataTransferer = DataTransferer.getInstance()) != null) {
            LinkedHashSet<DataFlavor> platformMappingsForNative = dataTransferer.getPlatformMappingsForNative(str);
            if (!platformMappingsForNative.isEmpty()) {
                if (linkedHashSet != null) {
                    platformMappingsForNative.addAll(linkedHashSet);
                }
                linkedHashSet = platformMappingsForNative;
            }
        }
        if (linkedHashSet == null && isJavaMIMEType(str)) {
            String strDecodeJavaMIMEType = decodeJavaMIMEType(str);
            DataFlavor dataFlavor = null;
            try {
                dataFlavor = new DataFlavor(strDecodeJavaMIMEType);
            } catch (Exception e2) {
                System.err.println("Exception \"" + e2.getClass().getName() + ": " + e2.getMessage() + "\"while constructing DataFlavor for: " + strDecodeJavaMIMEType);
            }
            if (dataFlavor != null) {
                linkedHashSet = new LinkedHashSet<>(1);
                getNativeToFlavor().put(str, linkedHashSet);
                linkedHashSet.add(dataFlavor);
                this.flavorsForNativeCache.remove(str);
                LinkedHashSet<String> linkedHashSet2 = getFlavorToNative().get(dataFlavor);
                if (linkedHashSet2 == null) {
                    linkedHashSet2 = new LinkedHashSet<>(1);
                    getFlavorToNative().put(dataFlavor, linkedHashSet2);
                }
                linkedHashSet2.add(str);
                this.nativesForFlavorCache.remove(dataFlavor);
            }
        }
        return linkedHashSet != null ? linkedHashSet : new LinkedHashSet<>(0);
    }

    private LinkedHashSet<String> flavorToNativeLookup(DataFlavor dataFlavor, boolean z2) {
        DataTransferer dataTransferer;
        LinkedHashSet<String> linkedHashSet = getFlavorToNative().get(dataFlavor);
        if (dataFlavor != null && !this.disabledMappingGenerationKeys.contains(dataFlavor) && (dataTransferer = DataTransferer.getInstance()) != null) {
            LinkedHashSet<String> platformMappingsForFlavor = dataTransferer.getPlatformMappingsForFlavor(dataFlavor);
            if (!platformMappingsForFlavor.isEmpty()) {
                if (linkedHashSet != null) {
                    platformMappingsForFlavor.addAll(linkedHashSet);
                }
                linkedHashSet = platformMappingsForFlavor;
            }
        }
        if (linkedHashSet == null) {
            if (z2) {
                String strEncodeDataFlavor = encodeDataFlavor(dataFlavor);
                linkedHashSet = new LinkedHashSet<>(1);
                getFlavorToNative().put(dataFlavor, linkedHashSet);
                linkedHashSet.add(strEncodeDataFlavor);
                LinkedHashSet<DataFlavor> linkedHashSet2 = getNativeToFlavor().get(strEncodeDataFlavor);
                if (linkedHashSet2 == null) {
                    linkedHashSet2 = new LinkedHashSet<>(1);
                    getNativeToFlavor().put(strEncodeDataFlavor, linkedHashSet2);
                }
                linkedHashSet2.add(dataFlavor);
                this.nativesForFlavorCache.remove(dataFlavor);
                this.flavorsForNativeCache.remove(strEncodeDataFlavor);
            } else {
                linkedHashSet = new LinkedHashSet<>(0);
            }
        }
        return new LinkedHashSet<>(linkedHashSet);
    }

    @Override // java.awt.datatransfer.FlavorTable
    public synchronized List<String> getNativesForFlavor(DataFlavor dataFlavor) {
        LinkedHashSet<String> linkedHashSetFlavorToNativeLookup;
        LinkedHashSet<String> linkedHashSet;
        LinkedHashSet<String> linkedHashSetCheck = this.nativesForFlavorCache.check(dataFlavor);
        if (linkedHashSetCheck != null) {
            return new ArrayList(linkedHashSetCheck);
        }
        if (dataFlavor == null) {
            linkedHashSetFlavorToNativeLookup = new LinkedHashSet<>(getNativeToFlavor().keySet());
        } else if (this.disabledMappingGenerationKeys.contains(dataFlavor)) {
            linkedHashSetFlavorToNativeLookup = flavorToNativeLookup(dataFlavor, false);
        } else if (DataTransferer.isFlavorCharsetTextType(dataFlavor)) {
            linkedHashSetFlavorToNativeLookup = new LinkedHashSet<>(0);
            if ("text".equals(dataFlavor.getPrimaryType()) && (linkedHashSet = getTextTypeToNative().get(dataFlavor.mimeType.getBaseType())) != null) {
                linkedHashSetFlavorToNativeLookup.addAll(linkedHashSet);
            }
            LinkedHashSet<String> linkedHashSet2 = getTextTypeToNative().get("text/plain");
            if (linkedHashSet2 != null) {
                linkedHashSetFlavorToNativeLookup.addAll(linkedHashSet2);
            }
            if (linkedHashSetFlavorToNativeLookup.isEmpty()) {
                linkedHashSetFlavorToNativeLookup = flavorToNativeLookup(dataFlavor, true);
            } else {
                linkedHashSetFlavorToNativeLookup.addAll(flavorToNativeLookup(dataFlavor, false));
            }
        } else if (DataTransferer.isFlavorNoncharsetTextType(dataFlavor)) {
            linkedHashSetFlavorToNativeLookup = getTextTypeToNative().get(dataFlavor.mimeType.getBaseType());
            if (linkedHashSetFlavorToNativeLookup == null || linkedHashSetFlavorToNativeLookup.isEmpty()) {
                linkedHashSetFlavorToNativeLookup = flavorToNativeLookup(dataFlavor, true);
            } else {
                linkedHashSetFlavorToNativeLookup.addAll(flavorToNativeLookup(dataFlavor, false));
            }
        } else {
            linkedHashSetFlavorToNativeLookup = flavorToNativeLookup(dataFlavor, true);
        }
        this.nativesForFlavorCache.put(dataFlavor, linkedHashSetFlavorToNativeLookup);
        return new ArrayList(linkedHashSetFlavorToNativeLookup);
    }

    @Override // java.awt.datatransfer.FlavorTable
    public synchronized List<DataFlavor> getFlavorsForNative(String str) {
        LinkedHashSet<DataFlavor> linkedHashSetCheck = this.flavorsForNativeCache.check(str);
        if (linkedHashSetCheck != null) {
            return new ArrayList(linkedHashSetCheck);
        }
        LinkedHashSet<DataFlavor> linkedHashSet = new LinkedHashSet<>();
        if (str == null) {
            Iterator<String> it = getNativesForFlavor(null).iterator();
            while (it.hasNext()) {
                linkedHashSet.addAll(getFlavorsForNative(it.next()));
            }
        } else {
            LinkedHashSet<DataFlavor> linkedHashSetNativeToFlavorLookup = nativeToFlavorLookup(str);
            if (this.disabledMappingGenerationKeys.contains(str)) {
                return new ArrayList(linkedHashSetNativeToFlavorLookup);
            }
            Iterator<DataFlavor> it2 = nativeToFlavorLookup(str).iterator();
            while (it2.hasNext()) {
                DataFlavor next = it2.next();
                linkedHashSet.add(next);
                if ("text".equals(next.getPrimaryType())) {
                    linkedHashSet.addAll(convertMimeTypeToDataFlavors(next.mimeType.getBaseType()));
                }
            }
        }
        this.flavorsForNativeCache.put(str, linkedHashSet);
        return new ArrayList(linkedHashSet);
    }

    private static Set<DataFlavor> convertMimeTypeToDataFlavors(String str) {
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        String subType = null;
        try {
            subType = new MimeType(str).getSubType();
        } catch (MimeTypeParseException e2) {
        }
        if (DataTransferer.doesSubtypeSupportCharset(subType, null)) {
            if ("text/plain".equals(str)) {
                linkedHashSet.add(DataFlavor.stringFlavor);
            }
            for (String str2 : UNICODE_TEXT_CLASSES) {
                Iterator<String> it = handleHtmlMimeTypes(str, str + ";charset=Unicode;class=" + str2).iterator();
                while (it.hasNext()) {
                    DataFlavor dataFlavor = null;
                    try {
                        dataFlavor = new DataFlavor(it.next());
                    } catch (ClassNotFoundException e3) {
                    }
                    linkedHashSet.add(dataFlavor);
                }
            }
            for (String str3 : DataTransferer.standardEncodings()) {
                for (String str4 : ENCODED_TEXT_CLASSES) {
                    Iterator<String> it2 = handleHtmlMimeTypes(str, str + ";charset=" + str3 + ";class=" + str4).iterator();
                    while (it2.hasNext()) {
                        DataFlavor dataFlavor2 = null;
                        try {
                            dataFlavor2 = new DataFlavor(it2.next());
                            if (dataFlavor2.equals(DataFlavor.plainTextFlavor)) {
                                dataFlavor2 = DataFlavor.plainTextFlavor;
                            }
                        } catch (ClassNotFoundException e4) {
                        }
                        linkedHashSet.add(dataFlavor2);
                    }
                }
            }
            if ("text/plain".equals(str)) {
                linkedHashSet.add(DataFlavor.plainTextFlavor);
            }
        } else {
            for (String str5 : ENCODED_TEXT_CLASSES) {
                DataFlavor dataFlavor3 = null;
                try {
                    dataFlavor3 = new DataFlavor(str + ";class=" + str5);
                } catch (ClassNotFoundException e5) {
                }
                linkedHashSet.add(dataFlavor3);
            }
        }
        return linkedHashSet;
    }

    private static LinkedHashSet<String> handleHtmlMimeTypes(String str, String str2) {
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<>();
        if ("text/html".equals(str)) {
            for (String str3 : htmlDocumntTypes) {
                linkedHashSet.add(str2 + ";document=" + str3);
            }
        } else {
            linkedHashSet.add(str2);
        }
        return linkedHashSet;
    }

    @Override // java.awt.datatransfer.FlavorMap
    public synchronized Map<DataFlavor, String> getNativesForFlavors(DataFlavor[] dataFlavorArr) {
        if (dataFlavorArr == null) {
            List<DataFlavor> flavorsForNative = getFlavorsForNative(null);
            dataFlavorArr = new DataFlavor[flavorsForNative.size()];
            flavorsForNative.toArray(dataFlavorArr);
        }
        HashMap map = new HashMap(dataFlavorArr.length, 1.0f);
        for (DataFlavor dataFlavor : dataFlavorArr) {
            List<String> nativesForFlavor = getNativesForFlavor(dataFlavor);
            map.put(dataFlavor, nativesForFlavor.isEmpty() ? null : nativesForFlavor.get(0));
        }
        return map;
    }

    @Override // java.awt.datatransfer.FlavorMap
    public synchronized Map<String, DataFlavor> getFlavorsForNatives(String[] strArr) {
        if (strArr == null) {
            List<String> nativesForFlavor = getNativesForFlavor(null);
            strArr = new String[nativesForFlavor.size()];
            nativesForFlavor.toArray(strArr);
        }
        HashMap map = new HashMap(strArr.length, 1.0f);
        for (String str : strArr) {
            List<DataFlavor> flavorsForNative = getFlavorsForNative(str);
            map.put(str, flavorsForNative.isEmpty() ? null : flavorsForNative.get(0));
        }
        return map;
    }

    public synchronized void addUnencodedNativeForFlavor(DataFlavor dataFlavor, String str) {
        Objects.requireNonNull(str, "Null native not permitted");
        Objects.requireNonNull(dataFlavor, "Null flavor not permitted");
        LinkedHashSet<String> linkedHashSet = getFlavorToNative().get(dataFlavor);
        if (linkedHashSet == null) {
            linkedHashSet = new LinkedHashSet<>(1);
            getFlavorToNative().put(dataFlavor, linkedHashSet);
        }
        linkedHashSet.add(str);
        this.nativesForFlavorCache.remove(dataFlavor);
    }

    public synchronized void setNativesForFlavor(DataFlavor dataFlavor, String[] strArr) {
        Objects.requireNonNull(strArr, "Null natives not permitted");
        Objects.requireNonNull(dataFlavor, "Null flavors not permitted");
        getFlavorToNative().remove(dataFlavor);
        for (String str : strArr) {
            addUnencodedNativeForFlavor(dataFlavor, str);
        }
        this.disabledMappingGenerationKeys.add(dataFlavor);
        this.nativesForFlavorCache.remove(dataFlavor);
    }

    public synchronized void addFlavorForUnencodedNative(String str, DataFlavor dataFlavor) {
        Objects.requireNonNull(str, "Null native not permitted");
        Objects.requireNonNull(dataFlavor, "Null flavor not permitted");
        LinkedHashSet<DataFlavor> linkedHashSet = getNativeToFlavor().get(str);
        if (linkedHashSet == null) {
            linkedHashSet = new LinkedHashSet<>(1);
            getNativeToFlavor().put(str, linkedHashSet);
        }
        linkedHashSet.add(dataFlavor);
        this.flavorsForNativeCache.remove(str);
    }

    public synchronized void setFlavorsForNative(String str, DataFlavor[] dataFlavorArr) {
        Objects.requireNonNull(str, "Null native not permitted");
        Objects.requireNonNull(dataFlavorArr, "Null flavors not permitted");
        getNativeToFlavor().remove(str);
        for (DataFlavor dataFlavor : dataFlavorArr) {
            addFlavorForUnencodedNative(str, dataFlavor);
        }
        this.disabledMappingGenerationKeys.add(str);
        this.flavorsForNativeCache.remove(str);
    }

    public static String encodeJavaMIMEType(String str) {
        if (str != null) {
            return JavaMIME + str;
        }
        return null;
    }

    public static String encodeDataFlavor(DataFlavor dataFlavor) {
        if (dataFlavor != null) {
            return encodeJavaMIMEType(dataFlavor.getMimeType());
        }
        return null;
    }

    public static boolean isJavaMIMEType(String str) {
        return str != null && str.startsWith(JavaMIME, 0);
    }

    public static String decodeJavaMIMEType(String str) {
        if (isJavaMIMEType(str)) {
            return str.substring(JavaMIME.length(), str.length()).trim();
        }
        return null;
    }

    public static DataFlavor decodeDataFlavor(String str) throws ClassNotFoundException {
        String strDecodeJavaMIMEType = decodeJavaMIMEType(str);
        if (strDecodeJavaMIMEType != null) {
            return new DataFlavor(strDecodeJavaMIMEType);
        }
        return null;
    }

    /* loaded from: rt.jar:java/awt/datatransfer/SystemFlavorMap$SoftCache.class */
    private static final class SoftCache<K, V> {
        Map<K, SoftReference<LinkedHashSet<V>>> cache;

        private SoftCache() {
        }

        public void put(K k2, LinkedHashSet<V> linkedHashSet) {
            if (this.cache == null) {
                this.cache = new HashMap(1);
            }
            this.cache.put(k2, new SoftReference<>(linkedHashSet));
        }

        public void remove(K k2) {
            if (this.cache == null) {
                return;
            }
            this.cache.remove(null);
            this.cache.remove(k2);
        }

        public LinkedHashSet<V> check(K k2) {
            SoftReference<LinkedHashSet<V>> softReference;
            if (this.cache != null && (softReference = this.cache.get(k2)) != null) {
                return softReference.get();
            }
            return null;
        }
    }
}
