package java.util;

import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.time.ZoneId;
import java.util.Locale;
import sun.security.action.GetPropertyAction;
import sun.util.calendar.ZoneInfo;
import sun.util.calendar.ZoneInfoFile;
import sun.util.locale.provider.TimeZoneNameUtility;

/* loaded from: rt.jar:java/util/TimeZone.class */
public abstract class TimeZone implements Serializable, Cloneable {
    public static final int SHORT = 0;
    public static final int LONG = 1;
    private static final int ONE_MINUTE = 60000;
    private static final int ONE_HOUR = 3600000;
    private static final int ONE_DAY = 86400000;
    static final long serialVersionUID = 3581463369166924961L;
    static final TimeZone NO_TIMEZONE;
    private String ID;
    private static volatile TimeZone defaultTimeZone;
    static final String GMT_ID = "GMT";
    private static final int GMT_ID_LENGTH = 3;
    private static volatile TimeZone mainAppContextDefault;
    static final /* synthetic */ boolean $assertionsDisabled;

    public abstract int getOffset(int i2, int i3, int i4, int i5, int i6, int i7);

    public abstract void setRawOffset(int i2);

    public abstract int getRawOffset();

    public abstract boolean useDaylightTime();

    public abstract boolean inDaylightTime(Date date);

    private static native String getSystemTimeZoneID(String str);

    private static native String getSystemGMTOffsetID();

    static {
        $assertionsDisabled = !TimeZone.class.desiredAssertionStatus();
        NO_TIMEZONE = null;
    }

    public int getOffset(long j2) {
        if (inDaylightTime(new Date(j2))) {
            return getRawOffset() + getDSTSavings();
        }
        return getRawOffset();
    }

    int getOffsets(long j2, int[] iArr) {
        int rawOffset = getRawOffset();
        int dSTSavings = 0;
        if (inDaylightTime(new Date(j2))) {
            dSTSavings = getDSTSavings();
        }
        if (iArr != null) {
            iArr[0] = rawOffset;
            iArr[1] = dSTSavings;
        }
        return rawOffset + dSTSavings;
    }

    public String getID() {
        return this.ID;
    }

    public void setID(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        this.ID = str;
    }

    public final String getDisplayName() {
        return getDisplayName(false, 1, Locale.getDefault(Locale.Category.DISPLAY));
    }

    public final String getDisplayName(Locale locale) {
        return getDisplayName(false, 1, locale);
    }

    public final String getDisplayName(boolean z2, int i2) {
        return getDisplayName(z2, i2, Locale.getDefault(Locale.Category.DISPLAY));
    }

    public String getDisplayName(boolean z2, int i2, Locale locale) {
        char cCharAt;
        if (i2 != 0 && i2 != 1) {
            throw new IllegalArgumentException("Illegal style: " + i2);
        }
        String id = getID();
        String strRetrieveDisplayName = TimeZoneNameUtility.retrieveDisplayName(id, z2, i2, locale);
        if (strRetrieveDisplayName != null) {
            return strRetrieveDisplayName;
        }
        if (id.startsWith(GMT_ID) && id.length() > 3 && ((cCharAt = id.charAt(3)) == '+' || cCharAt == '-')) {
            return id;
        }
        int rawOffset = getRawOffset();
        if (z2) {
            rawOffset += getDSTSavings();
        }
        return ZoneInfoFile.toCustomID(rawOffset);
    }

    private static String[] getDisplayNames(String str, Locale locale) {
        return TimeZoneNameUtility.retrieveDisplayNames(str, locale);
    }

    public int getDSTSavings() {
        if (useDaylightTime()) {
            return ONE_HOUR;
        }
        return 0;
    }

    public boolean observesDaylightTime() {
        return useDaylightTime() || inDaylightTime(new Date());
    }

    public static synchronized TimeZone getTimeZone(String str) {
        return getTimeZone(str, true);
    }

    public static TimeZone getTimeZone(ZoneId zoneId) {
        String id = zoneId.getId();
        char cCharAt = id.charAt(0);
        if (cCharAt == '+' || cCharAt == '-') {
            id = GMT_ID + id;
        } else if (cCharAt == 'Z' && id.length() == 1) {
            id = "UTC";
        }
        return getTimeZone(id, true);
    }

    public ZoneId toZoneId() {
        String id = getID();
        if (ZoneInfoFile.useOldMapping() && id.length() == 3) {
            if ("EST".equals(id)) {
                return ZoneId.of("America/New_York");
            }
            if ("MST".equals(id)) {
                return ZoneId.of("America/Denver");
            }
            if ("HST".equals(id)) {
                return ZoneId.of("America/Honolulu");
            }
        }
        return ZoneId.of(id, ZoneId.SHORT_IDS);
    }

    private static TimeZone getTimeZone(String str, boolean z2) {
        TimeZone timeZone = ZoneInfo.getTimeZone(str);
        if (timeZone == null) {
            timeZone = parseCustomTimeZone(str);
            if (timeZone == null && z2) {
                timeZone = new ZoneInfo(GMT_ID, 0);
            }
        }
        return timeZone;
    }

    public static synchronized String[] getAvailableIDs(int i2) {
        return ZoneInfo.getAvailableIDs(i2);
    }

    public static synchronized String[] getAvailableIDs() {
        return ZoneInfo.getAvailableIDs();
    }

    public static TimeZone getDefault() {
        return (TimeZone) getDefaultRef().clone();
    }

    static TimeZone getDefaultRef() {
        TimeZone defaultZone = defaultTimeZone;
        if (defaultZone == null) {
            defaultZone = setDefaultZone();
            if (!$assertionsDisabled && defaultZone == null) {
                throw new AssertionError();
            }
        }
        return defaultZone;
    }

    private static synchronized TimeZone setDefaultZone() {
        String systemTimeZoneID = (String) AccessController.doPrivileged(new GetPropertyAction("user.timezone"));
        if (systemTimeZoneID == null || systemTimeZoneID.isEmpty()) {
            try {
                systemTimeZoneID = getSystemTimeZoneID((String) AccessController.doPrivileged(new GetPropertyAction("java.home")));
                if (systemTimeZoneID == null) {
                    systemTimeZoneID = GMT_ID;
                }
            } catch (NullPointerException e2) {
                systemTimeZoneID = GMT_ID;
            }
        }
        TimeZone timeZone = getTimeZone(systemTimeZoneID, false);
        if (timeZone == null) {
            String systemGMTOffsetID = getSystemGMTOffsetID();
            if (systemGMTOffsetID != null) {
                systemTimeZoneID = systemGMTOffsetID;
            }
            timeZone = getTimeZone(systemTimeZoneID, true);
        }
        if (!$assertionsDisabled && timeZone == null) {
            throw new AssertionError();
        }
        final String str = systemTimeZoneID;
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.util.TimeZone.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Void run() {
                System.setProperty("user.timezone", str);
                return null;
            }
        });
        defaultTimeZone = timeZone;
        return timeZone;
    }

    public static void setDefault(TimeZone timeZone) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new PropertyPermission("user.timezone", "write"));
        }
        defaultTimeZone = timeZone;
    }

    public boolean hasSameRules(TimeZone timeZone) {
        return timeZone != null && getRawOffset() == timeZone.getRawOffset() && useDaylightTime() == timeZone.useDaylightTime();
    }

    public Object clone() {
        try {
            TimeZone timeZone = (TimeZone) super.clone();
            timeZone.ID = this.ID;
            return timeZone;
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }

    private static final TimeZone parseCustomTimeZone(String str) {
        ZoneInfo customTimeZone;
        int length = str.length();
        if (length < 5 || str.indexOf(GMT_ID) != 0) {
            return null;
        }
        ZoneInfo zoneInfo = ZoneInfoFile.getZoneInfo(str);
        if (zoneInfo != null) {
            return zoneInfo;
        }
        boolean z2 = false;
        int i2 = 3 + 1;
        char cCharAt = str.charAt(3);
        if (cCharAt == '-') {
            z2 = true;
        } else if (cCharAt != '+') {
            return null;
        }
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        while (i2 < length) {
            int i7 = i2;
            i2++;
            char cCharAt2 = str.charAt(i7);
            if (cCharAt2 == ':') {
                if (i5 > 0 || i6 > 2) {
                    return null;
                }
                i3 = i4;
                i5++;
                i4 = 0;
                i6 = 0;
            } else {
                if (cCharAt2 < '0' || cCharAt2 > '9') {
                    return null;
                }
                i4 = (i4 * 10) + (cCharAt2 - '0');
                i6++;
            }
        }
        if (i2 != length) {
            return null;
        }
        if (i5 == 0) {
            if (i6 <= 2) {
                i3 = i4;
                i4 = 0;
            } else {
                i3 = i4 / 100;
                i4 %= 100;
            }
        } else if (i6 != 2) {
            return null;
        }
        if (i3 > 23 || i4 > 59) {
            return null;
        }
        int i8 = ((i3 * 60) + i4) * 60 * 1000;
        if (i8 == 0) {
            customTimeZone = ZoneInfoFile.getZoneInfo(GMT_ID);
            if (z2) {
                customTimeZone.setID("GMT-00:00");
            } else {
                customTimeZone.setID("GMT+00:00");
            }
        } else {
            customTimeZone = ZoneInfoFile.getCustomTimeZone(str, z2 ? -i8 : i8);
        }
        return customTimeZone;
    }
}
