package sun.util.calendar;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.CRC32;
import jdk.nashorn.internal.runtime.regexp.joni.constants.StackType;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/util/calendar/ZoneInfoFile.class */
public final class ZoneInfoFile {
    private static String versionId;
    private static byte[][] ruleArray;
    private static String[] regions;
    private static int[] indices;
    private static final boolean USE_OLDMAPPING;
    private static final long UTC1900 = -2208988800L;
    private static final long UTC2037 = 2145916799;
    private static final long LDT2037 = 2114380800;
    private static final long CURRT;
    static final int SECONDS_PER_DAY = 86400;
    static final int DAYS_PER_CYCLE = 146097;
    static final long DAYS_0000_TO_1970 = 719528;
    private static final int[] toCalendarDOW;
    private static final int[] toSTZTime;
    private static final long OFFSET_MASK = 15;
    private static final long DST_MASK = 240;
    private static final int DST_NSHIFT = 4;
    private static final int TRANSITION_NSHIFT = 12;
    private static final int LASTYEAR = 2037;
    private static final Map<String, ZoneInfo> zones = new ConcurrentHashMap();
    private static Map<String, String> aliases = new HashMap();
    private static String[][] oldMappings = {new String[]{"ACT", "Australia/Darwin"}, new String[]{"AET", "Australia/Sydney"}, new String[]{"AGT", "America/Argentina/Buenos_Aires"}, new String[]{"ART", "Africa/Cairo"}, new String[]{"AST", "America/Anchorage"}, new String[]{"BET", "America/Sao_Paulo"}, new String[]{"BST", "Asia/Dhaka"}, new String[]{"CAT", "Africa/Harare"}, new String[]{"CNT", "America/St_Johns"}, new String[]{"CST", "America/Chicago"}, new String[]{"CTT", "Asia/Shanghai"}, new String[]{"EAT", "Africa/Addis_Ababa"}, new String[]{"ECT", "Europe/Paris"}, new String[]{"IET", "America/Indiana/Indianapolis"}, new String[]{"IST", "Asia/Kolkata"}, new String[]{"JST", "Asia/Tokyo"}, new String[]{"MIT", "Pacific/Apia"}, new String[]{"NET", "Asia/Yerevan"}, new String[]{"NST", "Pacific/Auckland"}, new String[]{"PLT", "Asia/Karachi"}, new String[]{"PNT", "America/Phoenix"}, new String[]{"PRT", "America/Puerto_Rico"}, new String[]{"PST", "America/Los_Angeles"}, new String[]{"SST", "Pacific/Guadalcanal"}, new String[]{"VST", "Asia/Ho_Chi_Minh"}};

    public static String[] getZoneIds() {
        int length = regions.length + oldMappings.length;
        if (!USE_OLDMAPPING) {
            length += 3;
        }
        String[] strArr = (String[]) Arrays.copyOf(regions, length);
        int length2 = regions.length;
        if (!USE_OLDMAPPING) {
            int i2 = length2 + 1;
            strArr[length2] = "EST";
            int i3 = i2 + 1;
            strArr[i2] = "HST";
            length2 = i3 + 1;
            strArr[i3] = "MST";
        }
        for (int i4 = 0; i4 < oldMappings.length; i4++) {
            int i5 = length2;
            length2++;
            strArr[i5] = oldMappings[i4][0];
        }
        return strArr;
    }

    public static String[] getZoneIds(int i2) {
        ArrayList arrayList = new ArrayList();
        for (String str : getZoneIds()) {
            if (getZoneInfo(str).getRawOffset() == i2) {
                arrayList.add(str);
            }
        }
        String[] strArr = (String[]) arrayList.toArray(new String[arrayList.size()]);
        Arrays.sort(strArr);
        return strArr;
    }

    public static ZoneInfo getZoneInfo(String str) {
        if (str == null) {
            return null;
        }
        ZoneInfo zoneInfo0 = getZoneInfo0(str);
        if (zoneInfo0 != null) {
            zoneInfo0 = (ZoneInfo) zoneInfo0.clone();
            zoneInfo0.setID(str);
        }
        return zoneInfo0;
    }

    private static ZoneInfo getZoneInfo0(String str) {
        try {
            ZoneInfo zoneInfo = zones.get(str);
            if (zoneInfo != null) {
                return zoneInfo;
            }
            String str2 = str;
            if (aliases.containsKey(str)) {
                str2 = aliases.get(str);
            }
            int iBinarySearch = Arrays.binarySearch(regions, str2);
            if (iBinarySearch < 0) {
                return null;
            }
            ZoneInfo zoneInfo2 = getZoneInfo(new DataInputStream(new ByteArrayInputStream(ruleArray[indices[iBinarySearch]])), str2);
            zones.put(str, zoneInfo2);
            return zoneInfo2;
        } catch (Exception e2) {
            throw new RuntimeException("Invalid binary time-zone data: TZDB:" + str + ", version: " + versionId, e2);
        }
    }

    public static Map<String, String> getAliasMap() {
        return Collections.unmodifiableMap(aliases);
    }

    public static String getVersion() {
        return versionId;
    }

    public static ZoneInfo getCustomTimeZone(String str, int i2) {
        return new ZoneInfo(toCustomID(i2), i2);
    }

    public static String toCustomID(int i2) {
        char c2;
        int i3 = i2 / 60000;
        if (i3 >= 0) {
            c2 = '+';
        } else {
            c2 = '-';
            i3 = -i3;
        }
        int i4 = i3 / 60;
        int i5 = i3 % 60;
        char[] cArr = {'G', 'M', 'T', c2, '0', '0', ':', '0', '0'};
        if (i4 >= 10) {
            cArr[4] = (char) (cArr[4] + (i4 / 10));
        }
        cArr[5] = (char) (cArr[5] + (i4 % 10));
        if (i5 != 0) {
            cArr[7] = (char) (cArr[7] + (i5 / 10));
            cArr[8] = (char) (cArr[8] + (i5 % 10));
        }
        return new String(cArr);
    }

    private ZoneInfoFile() {
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [java.lang.String[], java.lang.String[][]] */
    static {
        String lowerCase = ((String) AccessController.doPrivileged(new GetPropertyAction("sun.timezone.ids.oldmapping", "false"))).toLowerCase(Locale.ROOT);
        USE_OLDMAPPING = lowerCase.equals("yes") || lowerCase.equals("true");
        AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: sun.util.calendar.ZoneInfoFile.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                try {
                    DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(new File(System.getProperty("java.home") + File.separator + "lib", "tzdb.dat"))));
                    Throwable th = null;
                    try {
                        try {
                            ZoneInfoFile.load(dataInputStream);
                            if (dataInputStream != null) {
                                if (0 != 0) {
                                    try {
                                        dataInputStream.close();
                                    } catch (Throwable th2) {
                                        th.addSuppressed(th2);
                                    }
                                } else {
                                    dataInputStream.close();
                                }
                            }
                            return null;
                        } finally {
                        }
                    } finally {
                    }
                } catch (Exception e2) {
                    throw new Error(e2);
                }
            }
        });
        CURRT = System.currentTimeMillis() / 1000;
        toCalendarDOW = new int[]{-1, 2, 3, 4, 5, 6, 7, 1};
        toSTZTime = new int[]{2, 0, 1};
    }

    private static void addOldMapping() {
        for (String[] strArr : oldMappings) {
            aliases.put(strArr[0], strArr[1]);
        }
        if (USE_OLDMAPPING) {
            aliases.put("EST", "America/New_York");
            aliases.put("MST", "America/Denver");
            aliases.put("HST", "Pacific/Honolulu");
        } else {
            zones.put("EST", new ZoneInfo("EST", -18000000));
            zones.put("MST", new ZoneInfo("MST", -25200000));
            zones.put("HST", new ZoneInfo("HST", -36000000));
        }
    }

    public static boolean useOldMapping() {
        return USE_OLDMAPPING;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference failed for: r0v19, types: [byte[], byte[][]] */
    public static void load(DataInputStream dataInputStream) throws IOException, ClassNotFoundException {
        if (dataInputStream.readByte() != 1) {
            throw new StreamCorruptedException("File format not recognised");
        }
        if (!"TZDB".equals(dataInputStream.readUTF())) {
            throw new StreamCorruptedException("File format not recognised");
        }
        int i2 = dataInputStream.readShort();
        for (int i3 = 0; i3 < i2; i3++) {
            versionId = dataInputStream.readUTF();
        }
        int i4 = dataInputStream.readShort();
        String[] strArr = new String[i4];
        for (int i5 = 0; i5 < i4; i5++) {
            strArr[i5] = dataInputStream.readUTF();
        }
        int i6 = dataInputStream.readShort();
        ruleArray = new byte[i6];
        for (int i7 = 0; i7 < i6; i7++) {
            byte[] bArr = new byte[dataInputStream.readShort()];
            dataInputStream.readFully(bArr);
            ruleArray[i7] = bArr;
        }
        for (int i8 = 0; i8 < i2; i8++) {
            int i9 = dataInputStream.readShort();
            regions = new String[i9];
            indices = new int[i9];
            for (int i10 = 0; i10 < i9; i10++) {
                regions[i10] = strArr[dataInputStream.readShort()];
                indices[i10] = dataInputStream.readShort();
            }
        }
        zones.remove("ROC");
        for (int i11 = 0; i11 < i2; i11++) {
            int i12 = dataInputStream.readShort();
            aliases.clear();
            for (int i13 = 0; i13 < i12; i13++) {
                aliases.put(strArr[dataInputStream.readShort()], strArr[dataInputStream.readShort()]);
            }
        }
        addOldMapping();
    }

    public static ZoneInfo getZoneInfo(DataInput dataInput, String str) throws Exception {
        dataInput.readByte();
        int i2 = dataInput.readInt();
        long[] jArr = new long[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            jArr[i3] = readEpochSec(dataInput);
        }
        int[] iArr = new int[i2 + 1];
        for (int i4 = 0; i4 < iArr.length; i4++) {
            iArr[i4] = readOffset(dataInput);
        }
        int i5 = dataInput.readInt();
        long[] jArr2 = new long[i5];
        for (int i6 = 0; i6 < i5; i6++) {
            jArr2[i6] = readEpochSec(dataInput);
        }
        int[] iArr2 = new int[i5 + 1];
        for (int i7 = 0; i7 < iArr2.length; i7++) {
            iArr2[i7] = readOffset(dataInput);
        }
        int i8 = dataInput.readByte();
        ZoneOffsetTransitionRule[] zoneOffsetTransitionRuleArr = new ZoneOffsetTransitionRule[i8];
        for (int i9 = 0; i9 < i8; i9++) {
            zoneOffsetTransitionRuleArr[i9] = new ZoneOffsetTransitionRule(dataInput);
        }
        return getZoneInfo(str, jArr, iArr, jArr2, iArr2, zoneOffsetTransitionRuleArr);
    }

    public static int readOffset(DataInput dataInput) throws IOException {
        byte b2 = dataInput.readByte();
        return b2 == Byte.MAX_VALUE ? dataInput.readInt() : b2 * 900;
    }

    static long readEpochSec(DataInput dataInput) throws IOException {
        if ((dataInput.readByte() & 255) == 255) {
            return dataInput.readLong();
        }
        int i2 = dataInput.readByte() & 255;
        return ((((r0 << 16) + (i2 << 8)) + (dataInput.readByte() & 255)) * 900) - 4575744000L;
    }

    private static ZoneInfo getZoneInfo(String str, long[] jArr, int[] iArr, long[] jArr2, int[] iArr2, ZoneOffsetTransitionRule[] zoneOffsetTransitionRuleArr) {
        int i2;
        LocalDateTime localDateTimePlusSeconds;
        LocalDateTime localDateTimePlusSeconds2;
        int i3 = 0;
        int value = 0;
        int[] iArr3 = null;
        boolean z2 = false;
        if (jArr.length > 0) {
            i2 = iArr[iArr.length - 1] * 1000;
            z2 = jArr[jArr.length - 1] > CURRT;
        } else {
            i2 = iArr[0] * 1000;
        }
        long[] jArrCopyOf = null;
        int[] iArrCopyOf = null;
        int iAddTrans = 0;
        int i4 = 0;
        if (jArr2.length != 0) {
            jArrCopyOf = new long[250];
            iArrCopyOf = new int[100];
            int year = getYear(jArr2[jArr2.length - 1], iArr2[jArr2.length - 1]);
            int i5 = 0;
            int i6 = 1;
            while (i5 < jArr2.length && jArr2[i5] < UTC1900) {
                i5++;
            }
            if (i5 < jArr2.length) {
                if (i5 < jArr2.length) {
                    iArrCopyOf[0] = iArr[iArr.length - 1] * 1000;
                    iAddTrans = 1;
                }
                i4 = 0 + 1;
                iAddTrans = addTrans(jArrCopyOf, 0, iArrCopyOf, iAddTrans, UTC1900, iArr2[i5], getStandardOffset(jArr, iArr, UTC1900));
            }
            while (true) {
                if (i5 >= jArr2.length) {
                    break;
                }
                long j2 = jArr2[i5];
                if (j2 > UTC2037) {
                    year = LASTYEAR;
                    break;
                }
                while (i6 < jArr.length) {
                    long j3 = jArr[i6];
                    if (j3 >= UTC1900) {
                        if (j3 > j2) {
                            break;
                        }
                        if (j3 < j2) {
                            if (iAddTrans + 2 >= iArrCopyOf.length) {
                                iArrCopyOf = Arrays.copyOf(iArrCopyOf, iArrCopyOf.length + 100);
                            }
                            if (i4 + 1 >= jArrCopyOf.length) {
                                jArrCopyOf = Arrays.copyOf(jArrCopyOf, jArrCopyOf.length + 100);
                            }
                            int i7 = i4;
                            i4++;
                            iAddTrans = addTrans(jArrCopyOf, i7, iArrCopyOf, iAddTrans, j3, iArr2[i5], iArr[i6 + 1]);
                        }
                    }
                    i6++;
                }
                if (iAddTrans + 2 >= iArrCopyOf.length) {
                    iArrCopyOf = Arrays.copyOf(iArrCopyOf, iArrCopyOf.length + 100);
                }
                if (i4 + 1 >= jArrCopyOf.length) {
                    jArrCopyOf = Arrays.copyOf(jArrCopyOf, jArrCopyOf.length + 100);
                }
                int i8 = i4;
                i4++;
                iAddTrans = addTrans(jArrCopyOf, i8, iArrCopyOf, iAddTrans, j2, iArr2[i5 + 1], getStandardOffset(jArr, iArr, j2));
                i5++;
            }
            while (i6 < jArr.length) {
                long j4 = jArr[i6];
                if (j4 >= UTC1900) {
                    int iIndexOf = indexOf(iArrCopyOf, 0, iAddTrans, iArr2[i5]);
                    if (iIndexOf == iAddTrans) {
                        iAddTrans++;
                    }
                    int i9 = i4;
                    i4++;
                    jArrCopyOf[i9] = ((j4 * 1000) << 12) | (iIndexOf & OFFSET_MASK);
                }
                i6++;
            }
            if (zoneOffsetTransitionRuleArr.length > 1) {
                while (true) {
                    int i10 = year;
                    year++;
                    if (i10 >= LASTYEAR) {
                        break;
                    }
                    for (ZoneOffsetTransitionRule zoneOffsetTransitionRule : zoneOffsetTransitionRuleArr) {
                        long transitionEpochSecond = zoneOffsetTransitionRule.getTransitionEpochSecond(year);
                        if (iAddTrans + 2 >= iArrCopyOf.length) {
                            iArrCopyOf = Arrays.copyOf(iArrCopyOf, iArrCopyOf.length + 100);
                        }
                        if (i4 + 1 >= jArrCopyOf.length) {
                            jArrCopyOf = Arrays.copyOf(jArrCopyOf, jArrCopyOf.length + 100);
                        }
                        int i11 = i4;
                        i4++;
                        iAddTrans = addTrans(jArrCopyOf, i11, iArrCopyOf, iAddTrans, transitionEpochSecond, zoneOffsetTransitionRule.offsetAfter, zoneOffsetTransitionRule.standardOffset);
                    }
                }
                ZoneOffsetTransitionRule zoneOffsetTransitionRule2 = zoneOffsetTransitionRuleArr[zoneOffsetTransitionRuleArr.length - 2];
                ZoneOffsetTransitionRule zoneOffsetTransitionRule3 = zoneOffsetTransitionRuleArr[zoneOffsetTransitionRuleArr.length - 1];
                iArr3 = new int[10];
                if (zoneOffsetTransitionRule2.offsetAfter - zoneOffsetTransitionRule2.offsetBefore < 0 && zoneOffsetTransitionRule3.offsetAfter - zoneOffsetTransitionRule3.offsetBefore > 0) {
                    zoneOffsetTransitionRule2 = zoneOffsetTransitionRule3;
                    zoneOffsetTransitionRule3 = zoneOffsetTransitionRule2;
                }
                iArr3[0] = zoneOffsetTransitionRule2.month - 1;
                byte b2 = zoneOffsetTransitionRule2.dom;
                int i12 = zoneOffsetTransitionRule2.dow;
                if (i12 == -1) {
                    iArr3[1] = b2;
                    iArr3[2] = 0;
                } else if (b2 < 0 || b2 >= 24) {
                    iArr3[1] = -1;
                    iArr3[2] = toCalendarDOW[i12];
                } else {
                    iArr3[1] = b2;
                    iArr3[2] = -toCalendarDOW[i12];
                }
                iArr3[3] = zoneOffsetTransitionRule2.secondOfDay * 1000;
                iArr3[4] = toSTZTime[zoneOffsetTransitionRule2.timeDefinition];
                iArr3[5] = zoneOffsetTransitionRule3.month - 1;
                byte b3 = zoneOffsetTransitionRule3.dom;
                int i13 = zoneOffsetTransitionRule3.dow;
                if (i13 == -1) {
                    iArr3[6] = b3;
                    iArr3[7] = 0;
                } else if (b3 < 0 || b3 >= 24) {
                    iArr3[6] = -1;
                    iArr3[7] = toCalendarDOW[i13];
                } else {
                    iArr3[6] = b3;
                    iArr3[7] = -toCalendarDOW[i13];
                }
                iArr3[8] = zoneOffsetTransitionRule3.secondOfDay * 1000;
                iArr3[9] = toSTZTime[zoneOffsetTransitionRule3.timeDefinition];
                i3 = (zoneOffsetTransitionRule2.offsetAfter - zoneOffsetTransitionRule2.offsetBefore) * 1000;
                if (iArr3[2] == 6 && iArr3[3] == 0 && str.equals("Asia/Amman")) {
                    iArr3[2] = 5;
                    iArr3[3] = 86400000;
                }
                if (iArr3[2] == 7 && iArr3[3] == 0 && str.equals("Asia/Amman")) {
                    iArr3[2] = 6;
                    iArr3[3] = 86400000;
                }
                if (str.equals("Africa/Cairo") && iArr3[7] == 6 && iArr3[8] == 0) {
                    iArr3[7] = 5;
                    iArr3[8] = 86400000;
                }
            } else if (i4 > 0) {
                if (year < LASTYEAR) {
                    long j5 = LDT2037 - (i2 / 1000);
                    int iIndexOf2 = indexOf(iArrCopyOf, 0, iAddTrans, i2 / 1000);
                    if (iIndexOf2 == iAddTrans) {
                        iAddTrans++;
                    }
                    int i14 = i4;
                    i4++;
                    jArrCopyOf[i14] = ((j5 * 1000) << 12) | (iIndexOf2 & OFFSET_MASK);
                } else if (jArr2.length > 2) {
                    int length = jArr2.length;
                    long j6 = jArr2[length - 2];
                    int i15 = iArr2[(length - 2) + 1];
                    int standardOffset = getStandardOffset(jArr, iArr, j6);
                    long j7 = jArr2[length - 1];
                    int i16 = iArr2[(length - 1) + 1];
                    int standardOffset2 = getStandardOffset(jArr, iArr, j7);
                    if (i15 > standardOffset && i16 == standardOffset2) {
                        int length2 = jArr2.length - 2;
                        ZoneOffset zoneOffsetOfTotalSeconds = ZoneOffset.ofTotalSeconds(iArr2[length2]);
                        ZoneOffset zoneOffsetOfTotalSeconds2 = ZoneOffset.ofTotalSeconds(iArr2[length2 + 1]);
                        LocalDateTime localDateTimeOfEpochSecond = LocalDateTime.ofEpochSecond(jArr2[length2], 0, zoneOffsetOfTotalSeconds);
                        if (zoneOffsetOfTotalSeconds2.getTotalSeconds() > zoneOffsetOfTotalSeconds.getTotalSeconds()) {
                            localDateTimePlusSeconds = localDateTimeOfEpochSecond;
                        } else {
                            localDateTimePlusSeconds = localDateTimeOfEpochSecond.plusSeconds(iArr2[length2 + 1] - iArr2[length2]);
                        }
                        int length3 = jArr2.length - 1;
                        ZoneOffset zoneOffsetOfTotalSeconds3 = ZoneOffset.ofTotalSeconds(iArr2[length3]);
                        ZoneOffset zoneOffsetOfTotalSeconds4 = ZoneOffset.ofTotalSeconds(iArr2[length3 + 1]);
                        LocalDateTime localDateTimeOfEpochSecond2 = LocalDateTime.ofEpochSecond(jArr2[length3], 0, zoneOffsetOfTotalSeconds3);
                        if (zoneOffsetOfTotalSeconds4.getTotalSeconds() > zoneOffsetOfTotalSeconds3.getTotalSeconds()) {
                            localDateTimePlusSeconds2 = localDateTimeOfEpochSecond2.plusSeconds(iArr2[length3 + 1] - iArr2[length3]);
                        } else {
                            localDateTimePlusSeconds2 = localDateTimeOfEpochSecond2;
                        }
                        iArr3 = new int[]{localDateTimePlusSeconds.getMonthValue() - 1, localDateTimePlusSeconds.getDayOfMonth(), 0, localDateTimePlusSeconds.toLocalTime().toSecondOfDay() * 1000, 0, localDateTimePlusSeconds2.getMonthValue() - 1, localDateTimePlusSeconds2.getDayOfMonth(), 0, localDateTimePlusSeconds2.toLocalTime().toSecondOfDay() * 1000, 0};
                        i3 = (i15 - standardOffset) * 1000;
                    }
                }
            }
            if (jArrCopyOf != null && jArrCopyOf.length != i4) {
                if (i4 == 0) {
                    jArrCopyOf = null;
                } else {
                    jArrCopyOf = Arrays.copyOf(jArrCopyOf, i4);
                }
            }
            if (iArrCopyOf != null && iArrCopyOf.length != iAddTrans) {
                if (iAddTrans == 0) {
                    iArrCopyOf = null;
                } else {
                    iArrCopyOf = Arrays.copyOf(iArrCopyOf, iAddTrans);
                }
            }
            if (jArrCopyOf != null) {
                Checksum checksum = new Checksum();
                for (long j8 : jArrCopyOf) {
                    int i17 = (int) ((j8 >>> 4) & OFFSET_MASK);
                    int i18 = i17 == 0 ? 0 : iArrCopyOf[i17];
                    int i19 = (int) (j8 & OFFSET_MASK);
                    int i20 = iArrCopyOf[i19];
                    checksum.update((j8 >> 12) + i19);
                    checksum.update(i19);
                    checksum.update(i17 == 0 ? -1 : i17);
                }
                value = (int) checksum.getValue();
            }
        }
        return new ZoneInfo(str, i2, i3, value, jArrCopyOf, iArrCopyOf, iArr3, z2);
    }

    private static int getStandardOffset(long[] jArr, int[] iArr, long j2) {
        int i2 = 0;
        while (i2 < jArr.length && j2 >= jArr[i2]) {
            i2++;
        }
        return iArr[i2];
    }

    private static int getYear(long j2, int i2) {
        long jFloorDiv = (Math.floorDiv(j2 + i2, 86400L) + DAYS_0000_TO_1970) - 60;
        long j3 = 0;
        if (jFloorDiv < 0) {
            long j4 = ((jFloorDiv + 1) / 146097) - 1;
            j3 = j4 * 400;
            jFloorDiv += (-j4) * 146097;
        }
        long j5 = ((400 * jFloorDiv) + 591) / 146097;
        long j6 = jFloorDiv - ((((365 * j5) + (j5 / 4)) - (j5 / 100)) + (j5 / 400));
        if (j6 < 0) {
            j5--;
            j6 = jFloorDiv - ((((365 * j5) + (j5 / 4)) - (j5 / 100)) + (j5 / 400));
        }
        int i3 = (int) j6;
        int i4 = ((i3 * 5) + 2) / 153;
        int i5 = ((i4 + 2) % 12) + 1;
        int i6 = (i3 - (((i4 * 306) + 5) / 10)) + 1;
        return (int) (j5 + j3 + (i4 / 10));
    }

    private static int indexOf(int[] iArr, int i2, int i3, int i4) {
        int i5 = i4 * 1000;
        while (i2 < i3) {
            if (iArr[i2] != i5) {
                i2++;
            } else {
                return i2;
            }
        }
        iArr[i2] = i5;
        return i2;
    }

    private static int addTrans(long[] jArr, int i2, int[] iArr, int i3, long j2, int i4, int i5) {
        int iIndexOf = indexOf(iArr, 0, i3, i4);
        if (iIndexOf == i3) {
            i3++;
        }
        int iIndexOf2 = 0;
        if (i4 != i5) {
            iIndexOf2 = indexOf(iArr, 1, i3, i4 - i5);
            if (iIndexOf2 == i3) {
                i3++;
            }
        }
        jArr[i2] = ((j2 * 1000) << 12) | ((iIndexOf2 << 4) & 240) | (iIndexOf & OFFSET_MASK);
        return i3;
    }

    /* loaded from: rt.jar:sun/util/calendar/ZoneInfoFile$Checksum.class */
    private static class Checksum extends CRC32 {
        private Checksum() {
        }

        @Override // java.util.zip.CRC32, java.util.zip.Checksum
        public void update(int i2) {
            update(new byte[]{(byte) (i2 >>> 24), (byte) (i2 >>> 16), (byte) (i2 >>> 8), (byte) i2});
        }

        void update(long j2) {
            update(new byte[]{(byte) (j2 >>> 56), (byte) (j2 >>> 48), (byte) (j2 >>> 40), (byte) (j2 >>> 32), (byte) (j2 >>> 24), (byte) (j2 >>> 16), (byte) (j2 >>> 8), (byte) j2});
        }
    }

    /* loaded from: rt.jar:sun/util/calendar/ZoneInfoFile$ZoneOffsetTransitionRule.class */
    private static class ZoneOffsetTransitionRule {
        private final int month;
        private final byte dom;
        private final int dow;
        private final int secondOfDay;
        private final int timeDefinition;
        private final int standardOffset;
        private final int offsetBefore;
        private final int offsetAfter;

        ZoneOffsetTransitionRule(DataInput dataInput) throws IOException {
            int i2 = dataInput.readInt();
            int i3 = (i2 & 3670016) >>> 19;
            int i4 = (i2 & 507904) >>> 14;
            int i5 = (i2 & 4080) >>> 4;
            int i6 = (i2 & 12) >>> 2;
            int i7 = i2 & 3;
            this.month = i2 >>> 28;
            this.dom = (byte) (((i2 & 264241152) >>> 22) - 32);
            this.dow = i3 == 0 ? -1 : i3;
            this.secondOfDay = i4 == 31 ? dataInput.readInt() : i4 * 3600;
            this.timeDefinition = (i2 & StackType.NULL_CHECK_START) >>> 12;
            this.standardOffset = i5 == 255 ? dataInput.readInt() : (i5 - 128) * 900;
            this.offsetBefore = i6 == 3 ? dataInput.readInt() : this.standardOffset + (i6 * 1800);
            this.offsetAfter = i7 == 3 ? dataInput.readInt() : this.standardOffset + (i7 * 1800);
        }

        long getTransitionEpochSecond(int i2) {
            long epochDay;
            if (this.dom < 0) {
                epochDay = toEpochDay(i2, this.month, lengthOfMonth(i2, this.month) + 1 + this.dom);
                if (this.dow != -1) {
                    epochDay = previousOrSame(epochDay, this.dow);
                }
            } else {
                epochDay = toEpochDay(i2, this.month, this.dom);
                if (this.dow != -1) {
                    epochDay = nextOrSame(epochDay, this.dow);
                }
            }
            int i3 = 0;
            switch (this.timeDefinition) {
                case 0:
                    i3 = 0;
                    break;
                case 1:
                    i3 = -this.offsetBefore;
                    break;
                case 2:
                    i3 = -this.standardOffset;
                    break;
            }
            return (epochDay * 86400) + this.secondOfDay + i3;
        }

        static final boolean isLeapYear(int i2) {
            return (i2 & 3) == 0 && (i2 % 100 != 0 || i2 % 400 == 0);
        }

        static final int lengthOfMonth(int i2, int i3) {
            switch (i3) {
                case 2:
                    return isLeapYear(i2) ? 29 : 28;
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                default:
                    return 31;
                case 4:
                case 6:
                case 9:
                case 11:
                    return 30;
            }
        }

        static final long toEpochDay(int i2, int i3, int i4) {
            long j2;
            long j3 = i2;
            long j4 = i3;
            long j5 = 0 + (365 * j3);
            if (j3 >= 0) {
                j2 = j5 + (((j3 + 3) / 4) - ((j3 + 99) / 100)) + ((j3 + 399) / 400);
            } else {
                j2 = j5 - (((j3 / (-4)) - (j3 / (-100))) + (j3 / (-400)));
            }
            long j6 = j2 + (((367 * j4) - 362) / 12) + (i4 - 1);
            if (j4 > 2) {
                j6--;
                if (!isLeapYear(i2)) {
                    j6--;
                }
            }
            return j6 - ZoneInfoFile.DAYS_0000_TO_1970;
        }

        static final long previousOrSame(long j2, int i2) {
            return adjust(j2, i2, 1);
        }

        static final long nextOrSame(long j2, int i2) {
            return adjust(j2, i2, 0);
        }

        static final long adjust(long j2, int i2, int i3) {
            int iFloorMod = ((int) Math.floorMod(j2 + 3, 7L)) + 1;
            if (i3 < 2 && iFloorMod == i2) {
                return j2;
            }
            if ((i3 & 1) == 0) {
                return j2 + (iFloorMod - i2 >= 0 ? 7 - r0 : -r0);
            }
            return j2 - (i2 - iFloorMod >= 0 ? 7 - r0 : -r0);
        }
    }
}
