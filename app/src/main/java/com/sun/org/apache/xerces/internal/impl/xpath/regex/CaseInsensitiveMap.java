package com.sun.org.apache.xerces.internal.impl.xpath.regex;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xpath/regex/CaseInsensitiveMap.class */
public class CaseInsensitiveMap {
    private static int[][][] caseInsensitiveMap;
    private static int CHUNK_SHIFT = 10;
    private static int CHUNK_SIZE = 1 << CHUNK_SHIFT;
    private static int CHUNK_MASK = CHUNK_SIZE - 1;
    private static int INITIAL_CHUNK_COUNT = 64;
    private static Boolean mapBuilt = Boolean.FALSE;
    private static int LOWER_CASE_MATCH = 1;
    private static int UPPER_CASE_MATCH = 2;

    public static int[] get(int codePoint) {
        if (mapBuilt == Boolean.FALSE) {
            synchronized (mapBuilt) {
                if (mapBuilt == Boolean.FALSE) {
                    buildCaseInsensitiveMap();
                }
            }
        }
        if (codePoint < 65536) {
            return getMapping(codePoint);
        }
        return null;
    }

    private static int[] getMapping(int codePoint) {
        int chunk = codePoint >>> CHUNK_SHIFT;
        int offset = codePoint & CHUNK_MASK;
        return caseInsensitiveMap[chunk][offset];
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [int[][], int[][][]] */
    private static void buildCaseInsensitiveMap() {
        caseInsensitiveMap = new int[INITIAL_CHUNK_COUNT][];
        for (int i2 = 0; i2 < INITIAL_CHUNK_COUNT; i2++) {
            caseInsensitiveMap[i2] = new int[CHUNK_SIZE];
        }
        for (int i3 = 0; i3 < 65536; i3++) {
            int lc = Character.toLowerCase(i3);
            int uc = Character.toUpperCase(i3);
            if (lc != uc || lc != i3) {
                int[] map = new int[2];
                int index = 0;
                if (lc != i3) {
                    int index2 = 0 + 1;
                    map[0] = lc;
                    index = index2 + 1;
                    map[index2] = LOWER_CASE_MATCH;
                    int[] lcMap = getMapping(lc);
                    if (lcMap != null) {
                        map = updateMap(i3, map, lc, lcMap, LOWER_CASE_MATCH);
                    }
                }
                if (uc != i3) {
                    if (index == map.length) {
                        map = expandMap(map, 2);
                    }
                    int i4 = index;
                    int index3 = index + 1;
                    map[i4] = uc;
                    int i5 = index3 + 1;
                    map[index3] = UPPER_CASE_MATCH;
                    int[] ucMap = getMapping(uc);
                    if (ucMap != null) {
                        map = updateMap(i3, map, uc, ucMap, UPPER_CASE_MATCH);
                    }
                }
                set(i3, map);
            }
        }
        mapBuilt = Boolean.TRUE;
    }

    private static int[] expandMap(int[] srcMap, int expandBy) {
        int oldLen = srcMap.length;
        int[] newMap = new int[oldLen + expandBy];
        System.arraycopy(srcMap, 0, newMap, 0, oldLen);
        return newMap;
    }

    private static void set(int codePoint, int[] map) {
        int chunk = codePoint >>> CHUNK_SHIFT;
        int offset = codePoint & CHUNK_MASK;
        caseInsensitiveMap[chunk][offset] = map;
    }

    private static int[] updateMap(int codePoint, int[] codePointMap, int ciCodePoint, int[] ciCodePointMap, int matchType) {
        for (int i2 = 0; i2 < ciCodePointMap.length; i2 += 2) {
            int c2 = ciCodePointMap[i2];
            int[] cMap = getMapping(c2);
            if (cMap != null && contains(cMap, ciCodePoint, matchType)) {
                if (!contains(cMap, codePoint)) {
                    set(c2, expandAndAdd(cMap, codePoint, matchType));
                }
                if (!contains(codePointMap, c2)) {
                    codePointMap = expandAndAdd(codePointMap, c2, matchType);
                }
            }
        }
        if (!contains(ciCodePointMap, codePoint)) {
            set(ciCodePoint, expandAndAdd(ciCodePointMap, codePoint, matchType));
        }
        return codePointMap;
    }

    private static boolean contains(int[] map, int codePoint) {
        for (int i2 = 0; i2 < map.length; i2 += 2) {
            if (map[i2] == codePoint) {
                return true;
            }
        }
        return false;
    }

    private static boolean contains(int[] map, int codePoint, int matchType) {
        for (int i2 = 0; i2 < map.length; i2 += 2) {
            if (map[i2] == codePoint && map[i2 + 1] == matchType) {
                return true;
            }
        }
        return false;
    }

    private static int[] expandAndAdd(int[] srcMap, int codePoint, int matchType) {
        int oldLen = srcMap.length;
        int[] newMap = new int[oldLen + 2];
        System.arraycopy(srcMap, 0, newMap, 0, oldLen);
        newMap[oldLen] = codePoint;
        newMap[oldLen + 1] = matchType;
        return newMap;
    }
}
