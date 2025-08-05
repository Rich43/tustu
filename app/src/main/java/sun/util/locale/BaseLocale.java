package sun.util.locale;

import java.lang.ref.SoftReference;

/* loaded from: rt.jar:sun/util/locale/BaseLocale.class */
public final class BaseLocale {
    public static final String SEP = "_";
    private static final Cache CACHE = new Cache();
    private final String language;
    private final String script;
    private final String region;
    private final String variant;
    private volatile int hash;

    private BaseLocale(String str, String str2) {
        this.hash = 0;
        this.language = str;
        this.script = "";
        this.region = str2;
        this.variant = "";
    }

    private BaseLocale(String str, String str2, String str3, String str4) {
        this.hash = 0;
        this.language = str != null ? LocaleUtils.toLowerString(str).intern() : "";
        this.script = str2 != null ? LocaleUtils.toTitleString(str2).intern() : "";
        this.region = str3 != null ? LocaleUtils.toUpperString(str3).intern() : "";
        this.variant = str4 != null ? str4.intern() : "";
    }

    public static BaseLocale createInstance(String str, String str2) {
        BaseLocale baseLocale = new BaseLocale(str, str2);
        CACHE.put(new Key(str, str2), baseLocale);
        return baseLocale;
    }

    public static BaseLocale getInstance(String str, String str2, String str3, String str4) {
        if (str != null) {
            if (LocaleUtils.caseIgnoreMatch(str, "he")) {
                str = "iw";
            } else if (LocaleUtils.caseIgnoreMatch(str, "yi")) {
                str = "ji";
            } else if (LocaleUtils.caseIgnoreMatch(str, "id")) {
                str = "in";
            }
        }
        return CACHE.get(new Key(str, str2, str3, str4));
    }

    public String getLanguage() {
        return this.language;
    }

    public String getScript() {
        return this.script;
    }

    public String getRegion() {
        return this.region;
    }

    public String getVariant() {
        return this.variant;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BaseLocale)) {
            return false;
        }
        BaseLocale baseLocale = (BaseLocale) obj;
        return this.language == baseLocale.language && this.script == baseLocale.script && this.region == baseLocale.region && this.variant == baseLocale.variant;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.language.length() > 0) {
            sb.append("language=");
            sb.append(this.language);
        }
        if (this.script.length() > 0) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append("script=");
            sb.append(this.script);
        }
        if (this.region.length() > 0) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append("region=");
            sb.append(this.region);
        }
        if (this.variant.length() > 0) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append("variant=");
            sb.append(this.variant);
        }
        return sb.toString();
    }

    public int hashCode() {
        int iHashCode = this.hash;
        if (iHashCode == 0) {
            iHashCode = (31 * ((31 * ((31 * this.language.hashCode()) + this.script.hashCode())) + this.region.hashCode())) + this.variant.hashCode();
            this.hash = iHashCode;
        }
        return iHashCode;
    }

    /* loaded from: rt.jar:sun/util/locale/BaseLocale$Key.class */
    private static final class Key {
        private final SoftReference<String> lang;
        private final SoftReference<String> scrt;
        private final SoftReference<String> regn;
        private final SoftReference<String> vart;
        private final boolean normalized;
        private final int hash;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !BaseLocale.class.desiredAssertionStatus();
        }

        private Key(String str, String str2) {
            if (!$assertionsDisabled && (str.intern() != str || str2.intern() != str2)) {
                throw new AssertionError();
            }
            this.lang = new SoftReference<>(str);
            this.scrt = new SoftReference<>("");
            this.regn = new SoftReference<>(str2);
            this.vart = new SoftReference<>("");
            this.normalized = true;
            int iHashCode = str.hashCode();
            if (str2 != "") {
                int length = str2.length();
                for (int i2 = 0; i2 < length; i2++) {
                    iHashCode = (31 * iHashCode) + LocaleUtils.toLower(str2.charAt(i2));
                }
            }
            this.hash = iHashCode;
        }

        public Key(String str, String str2, String str3, String str4) {
            this(str, str2, str3, str4, false);
        }

        private Key(String str, String str2, String str3, String str4, boolean z2) {
            int iCharAt = 0;
            if (str != null) {
                this.lang = new SoftReference<>(str);
                int length = str.length();
                for (int i2 = 0; i2 < length; i2++) {
                    iCharAt = (31 * iCharAt) + LocaleUtils.toLower(str.charAt(i2));
                }
            } else {
                this.lang = new SoftReference<>("");
            }
            if (str2 != null) {
                this.scrt = new SoftReference<>(str2);
                int length2 = str2.length();
                for (int i3 = 0; i3 < length2; i3++) {
                    iCharAt = (31 * iCharAt) + LocaleUtils.toLower(str2.charAt(i3));
                }
            } else {
                this.scrt = new SoftReference<>("");
            }
            if (str3 != null) {
                this.regn = new SoftReference<>(str3);
                int length3 = str3.length();
                for (int i4 = 0; i4 < length3; i4++) {
                    iCharAt = (31 * iCharAt) + LocaleUtils.toLower(str3.charAt(i4));
                }
            } else {
                this.regn = new SoftReference<>("");
            }
            if (str4 != null) {
                this.vart = new SoftReference<>(str4);
                int length4 = str4.length();
                for (int i5 = 0; i5 < length4; i5++) {
                    iCharAt = (31 * iCharAt) + str4.charAt(i5);
                }
            } else {
                this.vart = new SoftReference<>("");
            }
            this.hash = iCharAt;
            this.normalized = z2;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if ((obj instanceof Key) && this.hash == ((Key) obj).hash) {
                String str = this.lang.get();
                String str2 = ((Key) obj).lang.get();
                if (str != null && str2 != null && LocaleUtils.caseIgnoreMatch(str2, str)) {
                    String str3 = this.scrt.get();
                    String str4 = ((Key) obj).scrt.get();
                    if (str3 != null && str4 != null && LocaleUtils.caseIgnoreMatch(str4, str3)) {
                        String str5 = this.regn.get();
                        String str6 = ((Key) obj).regn.get();
                        if (str5 != null && str6 != null && LocaleUtils.caseIgnoreMatch(str6, str5)) {
                            String str7 = this.vart.get();
                            String str8 = ((Key) obj).vart.get();
                            return str8 != null && str8.equals(str7);
                        }
                        return false;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }

        public int hashCode() {
            return this.hash;
        }

        public static Key normalize(Key key) {
            if (key.normalized) {
                return key;
            }
            return new Key(LocaleUtils.toLowerString(key.lang.get()).intern(), LocaleUtils.toTitleString(key.scrt.get()).intern(), LocaleUtils.toUpperString(key.regn.get()).intern(), key.vart.get().intern(), true);
        }
    }

    /* loaded from: rt.jar:sun/util/locale/BaseLocale$Cache.class */
    private static class Cache extends LocaleObjectCache<Key, BaseLocale> {
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !BaseLocale.class.desiredAssertionStatus();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // sun.util.locale.LocaleObjectCache
        public Key normalizeKey(Key key) {
            if (!$assertionsDisabled && (key.lang.get() == null || key.scrt.get() == null || key.regn.get() == null || key.vart.get() == null)) {
                throw new AssertionError();
            }
            return Key.normalize(key);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // sun.util.locale.LocaleObjectCache
        public BaseLocale createObject(Key key) {
            return new BaseLocale((String) key.lang.get(), (String) key.scrt.get(), (String) key.regn.get(), (String) key.vart.get());
        }
    }
}
