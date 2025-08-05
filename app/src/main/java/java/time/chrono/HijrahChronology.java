package java.time.chrono;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.ValueRange;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import sun.util.calendar.BaseCalendar;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:java/time/chrono/HijrahChronology.class */
public final class HijrahChronology extends AbstractChronology implements Serializable {
    private final transient String typeId;
    private final transient String calendarType;
    private static final long serialVersionUID = 3127340209035924785L;
    public static final HijrahChronology INSTANCE;
    private volatile transient boolean initComplete;
    private transient int[] hijrahEpochMonthStartDays;
    private transient int minEpochDay;
    private transient int maxEpochDay;
    private transient int hijrahStartEpochMonth;
    private transient int minMonthLength;
    private transient int maxMonthLength;
    private transient int minYearLength;
    private transient int maxYearLength;
    private static final transient Properties calendarProperties;
    private static final String PROP_PREFIX = "calendar.hijrah.";
    private static final String PROP_TYPE_SUFFIX = ".type";
    private static final String KEY_ID = "id";
    private static final String KEY_TYPE = "type";
    private static final String KEY_VERSION = "version";
    private static final String KEY_ISO_START = "iso-start";

    @Override // java.time.chrono.AbstractChronology, java.time.chrono.Chronology
    public /* bridge */ /* synthetic */ ChronoLocalDate resolveDate(Map map, ResolverStyle resolverStyle) {
        return resolveDate((Map<TemporalField, Long>) map, resolverStyle);
    }

    static {
        try {
            calendarProperties = BaseCalendar.getCalendarProperties();
            try {
                INSTANCE = new HijrahChronology("Hijrah-umalqura");
                AbstractChronology.registerChrono(INSTANCE, "Hijrah");
                AbstractChronology.registerChrono(INSTANCE, "islamic");
                registerVariants();
            } catch (DateTimeException e2) {
                PlatformLogger.getLogger("java.time.chrono").severe("Unable to initialize Hijrah calendar: Hijrah-umalqura", e2);
                throw new RuntimeException("Unable to initialize Hijrah-umalqura calendar", e2.getCause());
            }
        } catch (IOException e3) {
            throw new InternalError("Can't initialize lib/calendars.properties", e3);
        }
    }

    private static void registerVariants() {
        for (String str : calendarProperties.stringPropertyNames()) {
            if (str.startsWith(PROP_PREFIX)) {
                String strSubstring = str.substring(PROP_PREFIX.length());
                if (strSubstring.indexOf(46) < 0 && !strSubstring.equals(INSTANCE.getId())) {
                    try {
                        AbstractChronology.registerChrono(new HijrahChronology(strSubstring));
                    } catch (DateTimeException e2) {
                        PlatformLogger.getLogger("java.time.chrono").severe("Unable to initialize Hijrah calendar: " + strSubstring, e2);
                    }
                }
            }
        }
    }

    private HijrahChronology(String str) throws DateTimeException {
        if (str.isEmpty()) {
            throw new IllegalArgumentException("calendar id is empty");
        }
        String str2 = PROP_PREFIX + str + PROP_TYPE_SUFFIX;
        String property = calendarProperties.getProperty(str2);
        if (property == null || property.isEmpty()) {
            throw new DateTimeException("calendarType is missing or empty for: " + str2);
        }
        this.typeId = str;
        this.calendarType = property;
    }

    private void checkCalendarInit() {
        if (!this.initComplete) {
            loadCalendarData();
            this.initComplete = true;
        }
    }

    @Override // java.time.chrono.Chronology
    public String getId() {
        return this.typeId;
    }

    @Override // java.time.chrono.Chronology
    public String getCalendarType() {
        return this.calendarType;
    }

    @Override // java.time.chrono.Chronology
    public HijrahDate date(Era era, int i2, int i3, int i4) {
        return date(prolepticYear(era, i2), i3, i4);
    }

    @Override // java.time.chrono.Chronology
    public HijrahDate date(int i2, int i3, int i4) {
        return HijrahDate.of(this, i2, i3, i4);
    }

    @Override // java.time.chrono.Chronology
    public HijrahDate dateYearDay(Era era, int i2, int i3) {
        return dateYearDay(prolepticYear(era, i2), i3);
    }

    @Override // java.time.chrono.Chronology
    public HijrahDate dateYearDay(int i2, int i3) {
        HijrahDate hijrahDateOf = HijrahDate.of(this, i2, 1, 1);
        if (i3 > hijrahDateOf.lengthOfYear()) {
            throw new DateTimeException("Invalid dayOfYear: " + i3);
        }
        return hijrahDateOf.plusDays(i3 - 1);
    }

    @Override // java.time.chrono.Chronology
    public HijrahDate dateEpochDay(long j2) {
        return HijrahDate.ofEpochDay(this, j2);
    }

    @Override // java.time.chrono.Chronology
    public HijrahDate dateNow() {
        return dateNow(Clock.systemDefaultZone());
    }

    @Override // java.time.chrono.Chronology
    public HijrahDate dateNow(ZoneId zoneId) {
        return dateNow(Clock.system(zoneId));
    }

    @Override // java.time.chrono.Chronology
    public HijrahDate dateNow(Clock clock) {
        return date((TemporalAccessor) LocalDate.now(clock));
    }

    @Override // java.time.chrono.Chronology
    public HijrahDate date(TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof HijrahDate) {
            return (HijrahDate) temporalAccessor;
        }
        return HijrahDate.ofEpochDay(this, temporalAccessor.getLong(ChronoField.EPOCH_DAY));
    }

    @Override // java.time.chrono.Chronology
    public ChronoLocalDateTime<HijrahDate> localDateTime(TemporalAccessor temporalAccessor) {
        return super.localDateTime(temporalAccessor);
    }

    @Override // java.time.chrono.Chronology
    public ChronoZonedDateTime<HijrahDate> zonedDateTime(TemporalAccessor temporalAccessor) {
        return super.zonedDateTime(temporalAccessor);
    }

    @Override // java.time.chrono.Chronology
    public ChronoZonedDateTime<HijrahDate> zonedDateTime(Instant instant, ZoneId zoneId) {
        return super.zonedDateTime(instant, zoneId);
    }

    @Override // java.time.chrono.Chronology
    public boolean isLeapYear(long j2) {
        checkCalendarInit();
        return j2 >= ((long) getMinimumYear()) && j2 <= ((long) getMaximumYear()) && getYearLength((int) j2) > 354;
    }

    @Override // java.time.chrono.Chronology
    public int prolepticYear(Era era, int i2) {
        if (!(era instanceof HijrahEra)) {
            throw new ClassCastException("Era must be HijrahEra");
        }
        return i2;
    }

    @Override // java.time.chrono.Chronology
    public HijrahEra eraOf(int i2) {
        switch (i2) {
            case 1:
                return HijrahEra.AH;
            default:
                throw new DateTimeException("invalid Hijrah era");
        }
    }

    @Override // java.time.chrono.Chronology
    public List<Era> eras() {
        return Arrays.asList(HijrahEra.values());
    }

    @Override // java.time.chrono.Chronology
    public ValueRange range(ChronoField chronoField) {
        checkCalendarInit();
        if (chronoField instanceof ChronoField) {
            switch (chronoField) {
                case DAY_OF_MONTH:
                    return ValueRange.of(1L, 1L, getMinimumMonthLength(), getMaximumMonthLength());
                case DAY_OF_YEAR:
                    return ValueRange.of(1L, getMaximumDayOfYear());
                case ALIGNED_WEEK_OF_MONTH:
                    return ValueRange.of(1L, 5L);
                case YEAR:
                case YEAR_OF_ERA:
                    return ValueRange.of(getMinimumYear(), getMaximumYear());
                case ERA:
                    return ValueRange.of(1L, 1L);
                default:
                    return chronoField.range();
            }
        }
        return chronoField.range();
    }

    @Override // java.time.chrono.AbstractChronology, java.time.chrono.Chronology
    public HijrahDate resolveDate(Map<TemporalField, Long> map, ResolverStyle resolverStyle) {
        return (HijrahDate) super.resolveDate(map, resolverStyle);
    }

    int checkValidYear(long j2) {
        if (j2 < getMinimumYear() || j2 > getMaximumYear()) {
            throw new DateTimeException("Invalid Hijrah year: " + j2);
        }
        return (int) j2;
    }

    void checkValidDayOfYear(int i2) {
        if (i2 < 1 || i2 > getMaximumDayOfYear()) {
            throw new DateTimeException("Invalid Hijrah day of year: " + i2);
        }
    }

    void checkValidMonth(int i2) {
        if (i2 < 1 || i2 > 12) {
            throw new DateTimeException("Invalid Hijrah month: " + i2);
        }
    }

    int[] getHijrahDateInfo(int i2) {
        checkCalendarInit();
        if (i2 < this.minEpochDay || i2 >= this.maxEpochDay) {
            throw new DateTimeException("Hijrah date out of range");
        }
        int iEpochDayToEpochMonth = epochDayToEpochMonth(i2);
        return new int[]{epochMonthToYear(iEpochDayToEpochMonth), epochMonthToMonth(iEpochDayToEpochMonth) + 1, (i2 - epochMonthToEpochDay(iEpochDayToEpochMonth)) + 1};
    }

    long getEpochDay(int i2, int i3, int i4) {
        checkCalendarInit();
        checkValidMonth(i3);
        int iYearToEpochMonth = yearToEpochMonth(i2) + (i3 - 1);
        if (iYearToEpochMonth < 0 || iYearToEpochMonth >= this.hijrahEpochMonthStartDays.length) {
            throw new DateTimeException("Invalid Hijrah date, year: " + i2 + ", month: " + i3);
        }
        if (i4 < 1 || i4 > getMonthLength(i2, i3)) {
            throw new DateTimeException("Invalid Hijrah day of month: " + i4);
        }
        return epochMonthToEpochDay(iYearToEpochMonth) + (i4 - 1);
    }

    int getDayOfYear(int i2, int i3) {
        return yearMonthToDayOfYear(i2, i3 - 1);
    }

    int getMonthLength(int i2, int i3) {
        int iYearToEpochMonth = yearToEpochMonth(i2) + (i3 - 1);
        if (iYearToEpochMonth < 0 || iYearToEpochMonth >= this.hijrahEpochMonthStartDays.length) {
            throw new DateTimeException("Invalid Hijrah date, year: " + i2 + ", month: " + i3);
        }
        return epochMonthLength(iYearToEpochMonth);
    }

    int getYearLength(int i2) {
        return yearMonthToDayOfYear(i2, 12);
    }

    int getMinimumYear() {
        return epochMonthToYear(0);
    }

    int getMaximumYear() {
        return epochMonthToYear(this.hijrahEpochMonthStartDays.length - 1) - 1;
    }

    int getMaximumMonthLength() {
        return this.maxMonthLength;
    }

    int getMinimumMonthLength() {
        return this.minMonthLength;
    }

    int getMaximumDayOfYear() {
        return this.maxYearLength;
    }

    int getSmallestMaximumDayOfYear() {
        return this.minYearLength;
    }

    private int epochDayToEpochMonth(int i2) {
        int iBinarySearch = Arrays.binarySearch(this.hijrahEpochMonthStartDays, i2);
        if (iBinarySearch < 0) {
            iBinarySearch = (-iBinarySearch) - 2;
        }
        return iBinarySearch;
    }

    private int epochMonthToYear(int i2) {
        return (i2 + this.hijrahStartEpochMonth) / 12;
    }

    private int yearToEpochMonth(int i2) {
        return (i2 * 12) - this.hijrahStartEpochMonth;
    }

    private int epochMonthToMonth(int i2) {
        return (i2 + this.hijrahStartEpochMonth) % 12;
    }

    private int epochMonthToEpochDay(int i2) {
        return this.hijrahEpochMonthStartDays[i2];
    }

    private int yearMonthToDayOfYear(int i2, int i3) {
        int iYearToEpochMonth = yearToEpochMonth(i2);
        return epochMonthToEpochDay(iYearToEpochMonth + i3) - epochMonthToEpochDay(iYearToEpochMonth);
    }

    private int epochMonthLength(int i2) {
        return this.hijrahEpochMonthStartDays[i2 + 1] - this.hijrahEpochMonthStartDays[i2];
    }

    private static Properties readConfigProperties(String str) throws Exception {
        try {
            return (Properties) AccessController.doPrivileged(() -> {
                File file = new File(System.getProperty("java.home") + File.separator + "lib", str);
                Properties properties = new Properties();
                FileInputStream fileInputStream = new FileInputStream(file);
                Throwable th = null;
                try {
                    properties.load(fileInputStream);
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
                    return properties;
                } catch (Throwable th3) {
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
                    throw th3;
                }
            });
        } catch (PrivilegedActionException e2) {
            throw e2.getException();
        }
    }

    private void loadCalendarData() {
        HashMap map;
        int iMin;
        int iMax;
        String str;
        String str2;
        String str3;
        int epochDay;
        String str4;
        try {
            String property = calendarProperties.getProperty(PROP_PREFIX + this.typeId);
            Objects.requireNonNull(property, "Resource missing for calendar: calendar.hijrah." + this.typeId);
            Properties configProperties = readConfigProperties(property);
            map = new HashMap();
            iMin = Integer.MAX_VALUE;
            iMax = Integer.MIN_VALUE;
            str = null;
            str2 = null;
            str3 = null;
            epochDay = 0;
        } catch (Exception e2) {
            PlatformLogger.getLogger("java.time.chrono").severe("Unable to initialize Hijrah calendar proxy: " + this.typeId, e2);
            throw new DateTimeException("Unable to initialize HijrahCalendar: " + this.typeId, e2);
        }
        for (Map.Entry<Object, Object> entry : configProperties.entrySet()) {
            str4 = (String) entry.getKey();
            switch (str4) {
                case "id":
                    str = (String) entry.getValue();
                    continue;
                case "type":
                    str2 = (String) entry.getValue();
                    continue;
                case "version":
                    str3 = (String) entry.getValue();
                    continue;
                case "iso-start":
                    int[] ymd = parseYMD((String) entry.getValue());
                    epochDay = (int) LocalDate.of(ymd[0], ymd[1], ymd[2]).toEpochDay();
                    continue;
                default:
                    try {
                        int iIntValue = Integer.valueOf(str4).intValue();
                        map.put(Integer.valueOf(iIntValue), parseMonths((String) entry.getValue()));
                        iMax = Math.max(iMax, iIntValue);
                        iMin = Math.min(iMin, iIntValue);
                        continue;
                    } catch (NumberFormatException e3) {
                        throw new IllegalArgumentException("bad key: " + str4);
                    }
            }
            PlatformLogger.getLogger("java.time.chrono").severe("Unable to initialize Hijrah calendar proxy: " + this.typeId, e2);
            throw new DateTimeException("Unable to initialize HijrahCalendar: " + this.typeId, e2);
        }
        if (!getId().equals(str)) {
            throw new IllegalArgumentException("Configuration is for a different calendar: " + str);
        }
        if (!getCalendarType().equals(str2)) {
            throw new IllegalArgumentException("Configuration is for a different calendar type: " + str2);
        }
        if (str3 == null || str3.isEmpty()) {
            throw new IllegalArgumentException("Configuration does not contain a version");
        }
        if (epochDay == 0) {
            throw new IllegalArgumentException("Configuration does not contain a ISO start date");
        }
        this.hijrahStartEpochMonth = iMin * 12;
        this.minEpochDay = epochDay;
        this.hijrahEpochMonthStartDays = createEpochMonths(this.minEpochDay, iMin, iMax, map);
        this.maxEpochDay = this.hijrahEpochMonthStartDays[this.hijrahEpochMonthStartDays.length - 1];
        for (int i2 = iMin; i2 < iMax; i2++) {
            int yearLength = getYearLength(i2);
            this.minYearLength = Math.min(this.minYearLength, yearLength);
            this.maxYearLength = Math.max(this.maxYearLength, yearLength);
        }
    }

    private int[] createEpochMonths(int i2, int i3, int i4, Map<Integer, int[]> map) {
        int i5 = 0;
        int[] iArr = new int[(((i4 - i3) + 1) * 12) + 1];
        this.minMonthLength = Integer.MAX_VALUE;
        this.maxMonthLength = Integer.MIN_VALUE;
        for (int i6 = i3; i6 <= i4; i6++) {
            int[] iArr2 = map.get(Integer.valueOf(i6));
            for (int i7 = 0; i7 < 12; i7++) {
                int i8 = iArr2[i7];
                int i9 = i5;
                i5++;
                iArr[i9] = i2;
                if (i8 < 29 || i8 > 32) {
                    throw new IllegalArgumentException("Invalid month length in year: " + i3);
                }
                i2 += i8;
                this.minMonthLength = Math.min(this.minMonthLength, i8);
                this.maxMonthLength = Math.max(this.maxMonthLength, i8);
            }
        }
        int i10 = i5;
        int i11 = i5 + 1;
        iArr[i10] = i2;
        if (i11 != iArr.length) {
            throw new IllegalStateException("Did not fill epochMonths exactly: ndx = " + i11 + " should be " + iArr.length);
        }
        return iArr;
    }

    private int[] parseMonths(String str) {
        int[] iArr = new int[12];
        String[] strArrSplit = str.split("\\s");
        if (strArrSplit.length != 12) {
            throw new IllegalArgumentException("wrong number of months on line: " + Arrays.toString(strArrSplit) + "; count: " + strArrSplit.length);
        }
        for (int i2 = 0; i2 < 12; i2++) {
            try {
                iArr[i2] = Integer.valueOf(strArrSplit[i2]).intValue();
            } catch (NumberFormatException e2) {
                throw new IllegalArgumentException("bad key: " + strArrSplit[i2]);
            }
        }
        return iArr;
    }

    private int[] parseYMD(String str) {
        String strTrim = str.trim();
        try {
            if (strTrim.charAt(4) != '-' || strTrim.charAt(7) != '-') {
                throw new IllegalArgumentException("date must be yyyy-MM-dd");
            }
            return new int[]{Integer.valueOf(strTrim.substring(0, 4)).intValue(), Integer.valueOf(strTrim.substring(5, 7)).intValue(), Integer.valueOf(strTrim.substring(8, 10)).intValue()};
        } catch (NumberFormatException e2) {
            throw new IllegalArgumentException("date must be yyyy-MM-dd", e2);
        }
    }

    @Override // java.time.chrono.AbstractChronology
    Object writeReplace() {
        return super.writeReplace();
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }
}
