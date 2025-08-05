package sun.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: rt.jar:sun/misc/MetaIndex.class */
public class MetaIndex {
    private static volatile Map<File, MetaIndex> jarMap;
    private String[] contents;
    private boolean isClassOnlyJar;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !MetaIndex.class.desiredAssertionStatus();
    }

    public static MetaIndex forJar(File file) {
        return getJarMap().get(file);
    }

    public static synchronized void registerDirectory(File file) {
        File file2 = new File(file, "meta-index");
        if (file2.exists()) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file2));
                String strSubstring = null;
                boolean z2 = false;
                ArrayList arrayList = new ArrayList();
                Map<File, MetaIndex> jarMap2 = getJarMap();
                File canonicalFile = file.getCanonicalFile();
                String line = bufferedReader.readLine();
                if (line == null || !line.equals("% VERSION 2")) {
                    bufferedReader.close();
                    return;
                }
                while (true) {
                    String line2 = bufferedReader.readLine();
                    if (line2 != null) {
                        switch (line2.charAt(0)) {
                            case '!':
                            case '#':
                            case '@':
                                if (strSubstring != null && arrayList.size() > 0) {
                                    jarMap2.put(new File(canonicalFile, strSubstring), new MetaIndex(arrayList, z2));
                                    arrayList.clear();
                                }
                                strSubstring = line2.substring(2);
                                if (line2.charAt(0) == '!') {
                                    z2 = true;
                                    break;
                                } else if (!z2) {
                                    break;
                                } else {
                                    z2 = false;
                                    break;
                                }
                                break;
                            case '%':
                                break;
                            default:
                                arrayList.add(line2);
                                break;
                        }
                    } else {
                        if (strSubstring != null && arrayList.size() > 0) {
                            jarMap2.put(new File(canonicalFile, strSubstring), new MetaIndex(arrayList, z2));
                        }
                        bufferedReader.close();
                        return;
                    }
                }
            } catch (IOException e2) {
            }
        }
    }

    public boolean mayContain(String str) {
        if (this.isClassOnlyJar && !str.endsWith(".class")) {
            return false;
        }
        for (String str2 : this.contents) {
            if (str.startsWith(str2)) {
                return true;
            }
        }
        return false;
    }

    private MetaIndex(List<String> list, boolean z2) throws IllegalArgumentException {
        if (list == null) {
            throw new IllegalArgumentException();
        }
        this.contents = (String[]) list.toArray(new String[0]);
        this.isClassOnlyJar = z2;
    }

    private static Map<File, MetaIndex> getJarMap() {
        if (jarMap == null) {
            synchronized (MetaIndex.class) {
                if (jarMap == null) {
                    jarMap = new HashMap();
                }
            }
        }
        if ($assertionsDisabled || jarMap != null) {
            return jarMap;
        }
        throw new AssertionError();
    }
}
