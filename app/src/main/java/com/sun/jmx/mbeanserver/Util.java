package com.sun.jmx.mbeanserver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/* loaded from: rt.jar:com/sun/jmx/mbeanserver/Util.class */
public class Util {
    public static ObjectName newObjectName(String str) {
        try {
            return new ObjectName(str);
        } catch (MalformedObjectNameException e2) {
            throw new IllegalArgumentException(e2);
        }
    }

    static <K, V> Map<K, V> newMap() {
        return new HashMap();
    }

    static <K, V> Map<K, V> newSynchronizedMap() {
        return Collections.synchronizedMap(newMap());
    }

    static <K, V> IdentityHashMap<K, V> newIdentityHashMap() {
        return new IdentityHashMap<>();
    }

    static <K, V> Map<K, V> newSynchronizedIdentityHashMap() {
        return Collections.synchronizedMap(newIdentityHashMap());
    }

    static <K, V> SortedMap<K, V> newSortedMap() {
        return new TreeMap();
    }

    static <K, V> SortedMap<K, V> newSortedMap(Comparator<? super K> comparator) {
        return new TreeMap(comparator);
    }

    static <K, V> Map<K, V> newInsertionOrderMap() {
        return new LinkedHashMap();
    }

    static <E> Set<E> newSet() {
        return new HashSet();
    }

    static <E> Set<E> newSet(Collection<E> collection) {
        return new HashSet(collection);
    }

    static <E> List<E> newList() {
        return new ArrayList();
    }

    static <E> List<E> newList(Collection<E> collection) {
        return new ArrayList(collection);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> T cast(Object obj) {
        return obj;
    }

    public static int hashCode(String[] strArr, Object[] objArr) {
        int iDeepHashCode;
        int iHashCode = 0;
        for (int i2 = 0; i2 < strArr.length; i2++) {
            Object obj = objArr[i2];
            if (obj == null) {
                iDeepHashCode = 0;
            } else if (obj instanceof Object[]) {
                iDeepHashCode = Arrays.deepHashCode((Object[]) obj);
            } else {
                iDeepHashCode = obj.getClass().isArray() ? Arrays.deepHashCode(new Object[]{obj}) - 31 : obj.hashCode();
            }
            iHashCode += strArr[i2].toLowerCase().hashCode() ^ iDeepHashCode;
        }
        return iHashCode;
    }

    private static boolean wildmatch(String str, String str2, int i2, int i3, int i4, int i5) {
        int i6 = -1;
        int i7 = -1;
        while (true) {
            if (i4 < i5) {
                char cCharAt = str2.charAt(i4);
                switch (cCharAt) {
                    case '*':
                        i4++;
                        i6 = i4;
                        i7 = i2;
                        continue;
                    case '?':
                        if (i2 == i3) {
                            break;
                        } else {
                            i2++;
                            i4++;
                        }
                    default:
                        if (i2 >= i3 || str.charAt(i2) != cCharAt) {
                            break;
                        } else {
                            i2++;
                            i4++;
                        }
                        break;
                }
            } else if (i2 == i3) {
                return true;
            }
            if (i6 < 0 || i7 == i3) {
                return false;
            }
            i4 = i6;
            i7++;
            i2 = i7;
        }
    }

    public static boolean wildmatch(String str, String str2) {
        return wildmatch(str, str2, 0, str.length(), 0, str2.length());
    }
}
