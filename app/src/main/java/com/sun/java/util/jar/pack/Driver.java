package com.sun.java.util.jar.pack;

import com.sun.istack.internal.localization.Localizable;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Pack200;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javafx.fxml.FXMLLoader;
import net.lingala.zip4j.util.InternalZipConstants;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:com/sun/java/util/jar/pack/Driver.class */
class Driver {
    private static final ResourceBundle RESOURCE;
    private static final String PACK200_OPTION_MAP = "--repack                 $ \n  -r +>- @--repack              $ \n--no-gzip                $ \n  -g +>- @--no-gzip             $ \n--strip-debug            $ \n  -G +>- @--strip-debug         $ \n--no-keep-file-order     $ \n  -O +>- @--no-keep-file-order  $ \n--segment-limit=      *> = \n  -S +>  @--segment-limit=      = \n--effort=             *> = \n  -E +>  @--effort=             = \n--deflate-hint=       *> = \n  -H +>  @--deflate-hint=       = \n--modification-time=  *> = \n  -m +>  @--modification-time=  = \n--pass-file=        *> &�� \n  -P +>  @--pass-file=        &�� \n--unknown-attribute=  *> = \n  -U +>  @--unknown-attribute=  = \n--class-attribute=  *> &�� \n  -C +>  @--class-attribute=  &�� \n--field-attribute=  *> &�� \n  -F +>  @--field-attribute=  &�� \n--method-attribute= *> &�� \n  -M +>  @--method-attribute= &�� \n--code-attribute=   *> &�� \n  -D +>  @--code-attribute=   &�� \n--config-file=      *>   . \n  -f +>  @--config-file=        . \n--no-strip-debug  !--strip-debug         \n--gzip            !--no-gzip             \n--keep-file-order !--no-keep-file-order  \n--verbose                $ \n  -v +>- @--verbose             $ \n--quiet        !--verbose  \n  -q +>- !--verbose               \n--log-file=           *> = \n  -l +>  @--log-file=           = \n--version                . \n  -V +>  @--version             . \n--help               . \n  -? +> @--help . \n  -h +> @--help . \n--           . \n-   +?    >- . \n";
    private static final String UNPACK200_OPTION_MAP = "--deflate-hint=       *> = \n  -H +>  @--deflate-hint=       = \n--verbose                $ \n  -v +>- @--verbose             $ \n--quiet        !--verbose  \n  -q +>- !--verbose               \n--remove-pack-file       $ \n  -r +>- @--remove-pack-file    $ \n--log-file=           *> = \n  -l +>  @--log-file=           = \n--config-file=        *> . \n  -f +>  @--config-file=        . \n--           . \n-   +?    >- . \n--version                . \n  -V +>  @--version             . \n--help               . \n  -? +> @--help . \n  -h +> @--help . \n";
    private static final String[] PACK200_PROPERTY_TO_OPTION;
    private static final String[] UNPACK200_PROPERTY_TO_OPTION;
    static final /* synthetic */ boolean $assertionsDisabled;

    Driver() {
    }

    static {
        $assertionsDisabled = !Driver.class.desiredAssertionStatus();
        RESOURCE = ResourceBundle.getBundle("com.sun.java.util.jar.pack.DriverResource");
        PACK200_PROPERTY_TO_OPTION = new String[]{Pack200.Packer.SEGMENT_LIMIT, "--segment-limit=", Pack200.Packer.KEEP_FILE_ORDER, "--no-keep-file-order", Pack200.Packer.EFFORT, "--effort=", Pack200.Packer.DEFLATE_HINT, "--deflate-hint=", Pack200.Packer.MODIFICATION_TIME, "--modification-time=", Pack200.Packer.PASS_FILE_PFX, "--pass-file=", Pack200.Packer.UNKNOWN_ATTRIBUTE, "--unknown-attribute=", Pack200.Packer.CLASS_ATTRIBUTE_PFX, "--class-attribute=", Pack200.Packer.FIELD_ATTRIBUTE_PFX, "--field-attribute=", Pack200.Packer.METHOD_ATTRIBUTE_PFX, "--method-attribute=", Pack200.Packer.CODE_ATTRIBUTE_PFX, "--code-attribute=", "com.sun.java.util.jar.pack.verbose", "--verbose", "com.sun.java.util.jar.pack.strip.debug", "--strip-debug"};
        UNPACK200_PROPERTY_TO_OPTION = new String[]{Pack200.Unpacker.DEFLATE_HINT, "--deflate-hint=", "com.sun.java.util.jar.pack.verbose", "--verbose", "com.sun.java.util.jar.pack.unpack.remove.packfile", "--remove-pack-file"};
    }

    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v171, types: [java.io.InputStream] */
    public static void main(String[] strArr) throws IOException {
        ArrayList arrayList;
        boolean z2;
        boolean z3;
        String str;
        String[] strArr2;
        OutputStream bufferedOutputStream;
        FileInputStream fileInputStream;
        OutputStream fileOutputStream;
        String str2;
        String str3;
        arrayList = new ArrayList(Arrays.asList(strArr));
        z2 = true;
        z3 = false;
        boolean z4 = false;
        boolean z5 = true;
        String str4 = null;
        switch (arrayList.isEmpty() ? "" : (String) arrayList.get(0)) {
            case "--pack":
                arrayList.remove(0);
                break;
            case "--unpack":
                arrayList.remove(0);
                z2 = false;
                z3 = true;
                break;
        }
        HashMap map = new HashMap();
        map.put("com.sun.java.util.jar.pack.verbose", System.getProperty("com.sun.java.util.jar.pack.verbose"));
        if (z2) {
            str = PACK200_OPTION_MAP;
            strArr2 = PACK200_PROPERTY_TO_OPTION;
        } else {
            str = UNPACK200_OPTION_MAP;
            strArr2 = UNPACK200_PROPERTY_TO_OPTION;
        }
        HashMap map2 = new HashMap();
        while (true) {
            try {
                String commandOptions = parseCommandOptions(arrayList, str, map2);
                Iterator it = map2.keySet().iterator();
                while (it.hasNext()) {
                    String str5 = (String) it.next();
                    String str6 = null;
                    int i2 = 0;
                    while (true) {
                        if (i2 < strArr2.length) {
                            if (!str5.equals(strArr2[1 + i2])) {
                                i2 += 2;
                            } else {
                                str6 = strArr2[0 + i2];
                            }
                        }
                    }
                    if (str6 != null) {
                        String str7 = (String) map2.get(str5);
                        it.remove();
                        if (!str6.endsWith(".")) {
                            if (!str5.equals("--verbose") && !str5.endsWith("=")) {
                                boolean z6 = str7 != null;
                                if (str5.startsWith("--no-")) {
                                    z6 = !z6;
                                }
                                str7 = z6 ? "true" : "false";
                            }
                            map.put(str6, str7);
                        } else if (str6.contains(".attribute.")) {
                            for (String str8 : str7.split(Localizable.NOT_LOCALIZABLE)) {
                                String[] strArrSplit = str8.split("=", 2);
                                map.put(str6 + strArrSplit[0], strArrSplit[1]);
                            }
                        } else {
                            int i3 = 1;
                            for (String str9 : str7.split(Localizable.NOT_LOCALIZABLE)) {
                                do {
                                    int i4 = i3;
                                    i3++;
                                    str3 = str6 + "cli." + i4;
                                } while (map.containsKey(str3));
                                map.put(str3, str9);
                            }
                        }
                    }
                }
                if (!"--config-file=".equals(commandOptions)) {
                    if ("--version".equals(commandOptions)) {
                        System.out.println(MessageFormat.format(RESOURCE.getString("VERSION"), Driver.class.getName(), "1.31, 07/05/05"));
                        return;
                    }
                    if ("--help".equals(commandOptions)) {
                        printUsage(z2, true, System.out);
                        System.exit(1);
                        return;
                    }
                    for (String str10 : map2.keySet()) {
                        str2 = (String) map2.get(str10);
                        switch (str10) {
                            case "--repack":
                                z4 = true;
                                break;
                            case "--no-gzip":
                                z5 = str2 == null;
                                break;
                            case "--log-file=":
                                str4 = str2;
                                break;
                            default:
                                throw new InternalError(MessageFormat.format(RESOURCE.getString("BAD_OPTION"), str10, map2.get(str10)));
                        }
                    }
                    if (str4 != null && !str4.equals("")) {
                        if (str4.equals(LanguageTag.SEP)) {
                            System.setErr(System.out);
                        } else {
                            System.setErr(new PrintStream(new FileOutputStream(str4)));
                        }
                    }
                    boolean z7 = map.get("com.sun.java.util.jar.pack.verbose") != null;
                    String str11 = "";
                    if (!arrayList.isEmpty()) {
                        str11 = (String) arrayList.remove(0);
                    }
                    String str12 = "";
                    if (!arrayList.isEmpty()) {
                        str12 = (String) arrayList.remove(0);
                    }
                    String str13 = "";
                    String path = "";
                    String path2 = "";
                    if (z4) {
                        if (str11.toLowerCase().endsWith(".pack") || str11.toLowerCase().endsWith(".pac") || str11.toLowerCase().endsWith(".gz")) {
                            System.err.println(MessageFormat.format(RESOURCE.getString("BAD_REPACK_OUTPUT"), str11));
                            printUsage(z2, false, System.err);
                            System.exit(2);
                        }
                        str13 = str11;
                        if (str12.equals("")) {
                            str12 = str13;
                        }
                        path2 = createTempFile(str13, ".pack").getPath();
                        str11 = path2;
                        z5 = false;
                    }
                    if (!arrayList.isEmpty() || (!str12.toLowerCase().endsWith(".jar") && !str12.toLowerCase().endsWith(".zip") && (!str12.equals(LanguageTag.SEP) || z2))) {
                        printUsage(z2, false, System.err);
                        System.exit(2);
                        return;
                    }
                    if (z4) {
                        z3 = true;
                        z2 = true;
                    } else if (z2) {
                        z3 = false;
                    }
                    Pack200.Packer packerNewPacker = Pack200.newPacker();
                    Pack200.Unpacker unpackerNewUnpacker = Pack200.newUnpacker();
                    packerNewPacker.properties().putAll(map);
                    unpackerNewUnpacker.properties().putAll(map);
                    if (z4 && str13.equals(str12)) {
                        String zipComment = getZipComment(str12);
                        if (z7 && zipComment.length() > 0) {
                            System.out.println(MessageFormat.format(RESOURCE.getString("DETECTED_ZIP_COMMENT"), zipComment));
                        }
                        if (zipComment.indexOf("PACK200") >= 0) {
                            System.out.println(MessageFormat.format(RESOURCE.getString("SKIP_FOR_REPACKED"), str12));
                            z2 = false;
                            z3 = false;
                            z4 = false;
                        }
                    }
                    if (z2) {
                        try {
                            JarFile jarFile = new JarFile(new File(str12));
                            if (str11.equals(LanguageTag.SEP)) {
                                bufferedOutputStream = System.out;
                                System.setOut(System.err);
                            } else if (z5) {
                                if (!str11.endsWith(".gz")) {
                                    System.err.println(MessageFormat.format(RESOURCE.getString("WRITE_PACK_FILE"), str11));
                                    printUsage(z2, false, System.err);
                                    System.exit(2);
                                }
                                bufferedOutputStream = new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(str11)));
                            } else {
                                if (!str11.toLowerCase().endsWith(".pack") && !str11.toLowerCase().endsWith(".pac")) {
                                    System.err.println(MessageFormat.format(RESOURCE.getString("WRITE_PACKGZ_FILE"), str11));
                                    printUsage(z2, false, System.err);
                                    System.exit(2);
                                }
                                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(str11));
                            }
                            packerNewPacker.pack(jarFile, bufferedOutputStream);
                            bufferedOutputStream.close();
                        } catch (Throwable th) {
                            if (!path.equals("")) {
                                File file = new File(str12);
                                file.delete();
                                new File(path).renameTo(file);
                            }
                            if (!path2.equals("")) {
                                new File(path2).delete();
                            }
                            throw th;
                        }
                    }
                    if (z4 && str13.equals(str12)) {
                        File fileCreateTempFile = createTempFile(str12, ".bak");
                        fileCreateTempFile.delete();
                        if (!new File(str12).renameTo(fileCreateTempFile)) {
                            throw new Error(MessageFormat.format(RESOURCE.getString("SKIP_FOR_MOVE_FAILED"), path));
                        }
                        path = fileCreateTempFile.getPath();
                    }
                    if (z3) {
                        if (str11.equals(LanguageTag.SEP)) {
                            fileInputStream = System.in;
                        } else {
                            fileInputStream = new FileInputStream(new File(str11));
                        }
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                        InputStream gZIPInputStream = bufferedInputStream;
                        if (Utils.isGZIPMagic(Utils.readMagic(bufferedInputStream))) {
                            gZIPInputStream = new GZIPInputStream(gZIPInputStream);
                        }
                        String str14 = str13.equals("") ? str12 : str13;
                        if (str14.equals(LanguageTag.SEP)) {
                            fileOutputStream = System.out;
                        } else {
                            fileOutputStream = new FileOutputStream(str14);
                        }
                        JarOutputStream jarOutputStream = new JarOutputStream(new BufferedOutputStream(fileOutputStream));
                        Throwable th2 = null;
                        try {
                            unpackerNewUnpacker.unpack(gZIPInputStream, jarOutputStream);
                            if (jarOutputStream != null) {
                                if (0 != 0) {
                                    try {
                                        jarOutputStream.close();
                                    } catch (Throwable th3) {
                                        th2.addSuppressed(th3);
                                    }
                                } else {
                                    jarOutputStream.close();
                                }
                            }
                        } finally {
                        }
                    }
                    if (!path.equals("")) {
                        new File(path).delete();
                        path = "";
                    }
                    if (!path.equals("")) {
                        File file2 = new File(str12);
                        file2.delete();
                        new File(path).renameTo(file2);
                    }
                    if (!path2.equals("")) {
                        new File(path2).delete();
                        return;
                    }
                    return;
                }
                String str15 = (String) arrayList.remove(0);
                Properties properties = new Properties();
                FileInputStream fileInputStream2 = new FileInputStream(str15);
                Throwable th4 = null;
                try {
                    try {
                        properties.load(fileInputStream2);
                        if (fileInputStream2 != null) {
                            if (0 != 0) {
                                try {
                                    fileInputStream2.close();
                                } catch (Throwable th5) {
                                    th4.addSuppressed(th5);
                                }
                            } else {
                                fileInputStream2.close();
                            }
                        }
                        if (map.get("com.sun.java.util.jar.pack.verbose") != null) {
                            properties.list(System.out);
                        }
                        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                            map.put((String) entry.getKey(), (String) entry.getValue());
                        }
                    } finally {
                    }
                } catch (Throwable th6) {
                    th4 = th6;
                    throw th6;
                }
            } catch (IllegalArgumentException e2) {
                System.err.println(MessageFormat.format(RESOURCE.getString("BAD_ARGUMENT"), e2));
                printUsage(z2, false, System.err);
                System.exit(2);
                return;
            }
        }
    }

    private static File createTempFile(String str, String str2) throws IOException {
        File parentFile;
        Path pathCreateTempFile;
        File file = new File(str);
        String name = file.getName();
        if (name.length() < 3) {
            name = name + "tmp";
        }
        if (file.getParentFile() == null && str2.equals(".bak")) {
            parentFile = new File(".").getAbsoluteFile();
        } else {
            parentFile = file.getParentFile();
        }
        File file2 = parentFile;
        if (file2 == null) {
            pathCreateTempFile = Files.createTempFile(name, str2, new FileAttribute[0]);
        } else {
            pathCreateTempFile = Files.createTempFile(file2.toPath(), name, str2, new FileAttribute[0]);
        }
        return pathCreateTempFile.toFile();
    }

    private static void printUsage(boolean z2, boolean z3, PrintStream printStream) {
        String str = z2 ? "pack200" : "unpack200";
        for (String str2 : z2 ? (String[]) RESOURCE.getObject("PACK_HELP") : (String[]) RESOURCE.getObject("UNPACK_HELP")) {
            printStream.println(str2);
            if (!z3) {
                printStream.println(MessageFormat.format(RESOURCE.getString("MORE_INFO"), str));
                return;
            }
        }
    }

    private static String getZipComment(String str) throws IOException {
        byte[] bArr = new byte[1000];
        long length = new File(str).length();
        if (length <= 0) {
            return "";
        }
        long jMax = Math.max(0L, length - bArr.length);
        FileInputStream fileInputStream = new FileInputStream(new File(str));
        Throwable th = null;
        try {
            try {
                fileInputStream.skip(jMax);
                fileInputStream.read(bArr);
                for (int length2 = bArr.length - 4; length2 >= 0; length2--) {
                    if (bArr[length2 + 0] == 80 && bArr[length2 + 1] == 75 && bArr[length2 + 2] == 5 && bArr[length2 + 3] == 6) {
                        int i2 = length2 + 22;
                        if (i2 >= bArr.length) {
                            if (fileInputStream != null) {
                                if (0 != 0) {
                                    try {
                                        fileInputStream.close();
                                    } catch (Throwable th2) {
                                        th.addSuppressed(th2);
                                    }
                                } else {
                                    fileInputStream.close();
                                }
                            }
                            return "";
                        }
                        String str2 = new String(bArr, i2, bArr.length - i2, InternalZipConstants.CHARSET_UTF8);
                        if (fileInputStream != null) {
                            if (0 != 0) {
                                try {
                                    fileInputStream.close();
                                } catch (Throwable th3) {
                                    th.addSuppressed(th3);
                                }
                            } else {
                                fileInputStream.close();
                            }
                        }
                        return str2;
                    }
                }
                if (fileInputStream != null) {
                    if (0 != 0) {
                        try {
                            fileInputStream.close();
                        } catch (Throwable th4) {
                            th.addSuppressed(th4);
                        }
                    } else {
                        fileInputStream.close();
                    }
                }
                return "";
            } catch (Throwable th5) {
                if (fileInputStream != null) {
                    if (th != null) {
                        try {
                            fileInputStream.close();
                        } catch (Throwable th6) {
                            th.addSuppressed(th6);
                        }
                    } else {
                        fileInputStream.close();
                    }
                }
                throw th5;
            }
        } finally {
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static String parseCommandOptions(List<String> list, String str, Map<String, String> map) {
        String str2;
        boolean z2;
        String str3;
        String str4;
        String str5;
        String strIntern = null;
        TreeMap treeMap = new TreeMap();
        for (String str6 : str.split("\n")) {
            String[] strArrSplit = str6.split("\\p{Space}+");
            if (strArrSplit.length != 0) {
                String str7 = strArrSplit[0];
                strArrSplit[0] = "";
                if (str7.length() == 0 && strArrSplit.length >= 1) {
                    str7 = strArrSplit[1];
                    strArrSplit[1] = "";
                }
                if (str7.length() != 0 && ((String[]) treeMap.put(str7, strArrSplit)) != null) {
                    throw new RuntimeException(MessageFormat.format(RESOURCE.getString("DUPLICATE_OPTION"), str6.trim()));
                }
            }
        }
        ListIterator listIterator = list.listIterator();
        ListIterator listIterator2 = new ArrayList().listIterator();
        while (true) {
            if (listIterator2.hasPrevious()) {
                str2 = (String) listIterator2.previous();
                listIterator2.remove();
            } else if (listIterator.hasNext()) {
                str2 = (String) listIterator.next();
            }
            int length = str2.length();
            while (true) {
                String strSubstring = str2.substring(0, length);
                if (!treeMap.containsKey(strSubstring)) {
                    if (length != 0) {
                        SortedMap sortedMapHeadMap = treeMap.headMap(strSubstring);
                        length = Math.min(sortedMapHeadMap.isEmpty() ? 0 : ((String) sortedMapHeadMap.lastKey()).length(), length - 1);
                        str2.substring(0, length);
                    }
                } else {
                    String strIntern2 = strSubstring.intern();
                    if (!$assertionsDisabled && !str2.startsWith(strIntern2)) {
                        throw new AssertionError();
                    }
                    if (!$assertionsDisabled && strIntern2.length() != length) {
                        throw new AssertionError();
                    }
                    String strSubstring2 = str2.substring(length);
                    boolean z3 = false;
                    boolean z4 = false;
                    int iNextIndex = listIterator2.nextIndex();
                    String[] strArr = (String[]) treeMap.get(strIntern2);
                    int length2 = strArr.length;
                    int i2 = 0;
                    while (true) {
                        if (i2 < length2) {
                            String str8 = strArr[i2];
                            if (str8.length() != 0) {
                                if (!str8.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX)) {
                                    int i3 = 0 + 1;
                                    char cCharAt = str8.charAt(0);
                                    switch (cCharAt) {
                                        case '*':
                                            z2 = true;
                                            i3++;
                                            cCharAt = str8.charAt(i3);
                                            break;
                                        case '+':
                                            z2 = strSubstring2.length() != 0;
                                            i3++;
                                            cCharAt = str8.charAt(i3);
                                            break;
                                        default:
                                            z2 = strSubstring2.length() == 0;
                                            break;
                                    }
                                    if (z2) {
                                        String strSubstring3 = str8.substring(i3);
                                        switch (cCharAt) {
                                            case '!':
                                                String strIntern3 = strSubstring3.length() != 0 ? strSubstring3.intern() : strIntern2;
                                                map.remove(strIntern3);
                                                map.put(strIntern3, null);
                                                z3 = true;
                                                break;
                                            case '$':
                                                if (strSubstring3.length() != 0) {
                                                    str5 = strSubstring3;
                                                } else {
                                                    String str9 = map.get(strIntern2);
                                                    if (str9 == null || str9.length() == 0) {
                                                        str5 = "1";
                                                    } else {
                                                        str5 = "" + (1 + Integer.parseInt(str9));
                                                    }
                                                }
                                                map.put(strIntern2, str5);
                                                z3 = true;
                                                break;
                                            case '&':
                                            case '=':
                                                boolean z5 = cCharAt == '&';
                                                if (listIterator2.hasPrevious()) {
                                                    str3 = (String) listIterator2.previous();
                                                    listIterator2.remove();
                                                } else if (listIterator.hasNext()) {
                                                    str3 = (String) listIterator.next();
                                                } else {
                                                    strIntern = str2 + " ?";
                                                    z4 = true;
                                                    break;
                                                }
                                                if (z5 && (str4 = map.get(strIntern2)) != null) {
                                                    if (strSubstring3.length() == 0) {
                                                    }
                                                    str3 = str4 + strSubstring3 + str3;
                                                }
                                                map.put(strIntern2, str3);
                                                z3 = true;
                                                break;
                                            case '.':
                                                strIntern = strSubstring3.length() != 0 ? strSubstring3.intern() : strIntern2;
                                                break;
                                            case '>':
                                                listIterator2.add(strSubstring3 + strSubstring2);
                                                strSubstring2 = "";
                                                break;
                                            case '?':
                                                strIntern = strSubstring3.length() != 0 ? strSubstring3.intern() : str2;
                                                z4 = true;
                                                break;
                                            case '@':
                                                strIntern2 = strSubstring3.intern();
                                                break;
                                            default:
                                                throw new RuntimeException(MessageFormat.format(RESOURCE.getString("BAD_SPEC"), strIntern2, str8));
                                        }
                                    } else {
                                        continue;
                                    }
                                }
                            }
                            i2++;
                        }
                    }
                    if (!z3 || z4) {
                        while (listIterator2.nextIndex() > iNextIndex) {
                            listIterator2.previous();
                            listIterator2.remove();
                        }
                        if (z4) {
                            throw new IllegalArgumentException(strIntern);
                        }
                        if (length != 0) {
                            length--;
                        }
                    }
                }
            }
        }
        listIterator2.add(str2);
        list.subList(0, listIterator.nextIndex()).clear();
        while (listIterator2.hasPrevious()) {
            list.add(0, listIterator2.previous());
        }
        return strIntern;
    }
}
