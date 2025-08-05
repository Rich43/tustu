package sun.util.locale;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import sun.util.locale.InternalLocaleBuilder;

/* loaded from: rt.jar:sun/util/locale/LocaleExtensions.class */
public class LocaleExtensions {
    private final Map<Character, Extension> extensionMap;
    private final String id;
    public static final LocaleExtensions CALENDAR_JAPANESE;
    public static final LocaleExtensions NUMBER_THAI;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !LocaleExtensions.class.desiredAssertionStatus();
        CALENDAR_JAPANESE = new LocaleExtensions("u-ca-japanese", (Character) 'u', (Extension) UnicodeLocaleExtension.CA_JAPANESE);
        NUMBER_THAI = new LocaleExtensions("u-nu-thai", (Character) 'u', (Extension) UnicodeLocaleExtension.NU_THAI);
    }

    private LocaleExtensions(String str, Character ch, Extension extension) {
        this.id = str;
        this.extensionMap = Collections.singletonMap(ch, extension);
    }

    LocaleExtensions(Map<InternalLocaleBuilder.CaseInsensitiveChar, String> map, Set<InternalLocaleBuilder.CaseInsensitiveString> set, Map<InternalLocaleBuilder.CaseInsensitiveString, String> map2) {
        boolean z2 = !LocaleUtils.isEmpty(map);
        boolean z3 = !LocaleUtils.isEmpty(set);
        boolean z4 = !LocaleUtils.isEmpty(map2);
        if (!z2 && !z3 && !z4) {
            this.id = "";
            this.extensionMap = Collections.emptyMap();
            return;
        }
        TreeMap treeMap = new TreeMap();
        if (z2) {
            for (Map.Entry<InternalLocaleBuilder.CaseInsensitiveChar, String> entry : map.entrySet()) {
                char lower = LocaleUtils.toLower(entry.getKey().value());
                String value = entry.getValue();
                if (LanguageTag.isPrivateusePrefixChar(lower)) {
                    value = InternalLocaleBuilder.removePrivateuseVariant(value);
                    if (value == null) {
                    }
                }
                treeMap.put(Character.valueOf(lower), new Extension(lower, LocaleUtils.toLowerString(value)));
            }
        }
        if (z3 || z4) {
            TreeSet treeSet = null;
            TreeMap treeMap2 = null;
            if (z3) {
                treeSet = new TreeSet();
                Iterator<InternalLocaleBuilder.CaseInsensitiveString> it = set.iterator();
                while (it.hasNext()) {
                    treeSet.add(LocaleUtils.toLowerString(it.next().value()));
                }
            }
            if (z4) {
                treeMap2 = new TreeMap();
                for (Map.Entry<InternalLocaleBuilder.CaseInsensitiveString, String> entry2 : map2.entrySet()) {
                    treeMap2.put(LocaleUtils.toLowerString(entry2.getKey().value()), LocaleUtils.toLowerString(entry2.getValue()));
                }
            }
            treeMap.put('u', new UnicodeLocaleExtension(treeSet, treeMap2));
        }
        if (treeMap.isEmpty()) {
            this.id = "";
            this.extensionMap = Collections.emptyMap();
        } else {
            this.id = toID(treeMap);
            this.extensionMap = treeMap;
        }
    }

    public Set<Character> getKeys() {
        if (this.extensionMap.isEmpty()) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(this.extensionMap.keySet());
    }

    public Extension getExtension(Character ch) {
        return this.extensionMap.get(Character.valueOf(LocaleUtils.toLower(ch.charValue())));
    }

    public String getExtensionValue(Character ch) {
        Extension extension = this.extensionMap.get(Character.valueOf(LocaleUtils.toLower(ch.charValue())));
        if (extension == null) {
            return null;
        }
        return extension.getValue();
    }

    public Set<String> getUnicodeLocaleAttributes() {
        Extension extension = this.extensionMap.get('u');
        if (extension == null) {
            return Collections.emptySet();
        }
        if ($assertionsDisabled || (extension instanceof UnicodeLocaleExtension)) {
            return ((UnicodeLocaleExtension) extension).getUnicodeLocaleAttributes();
        }
        throw new AssertionError();
    }

    public Set<String> getUnicodeLocaleKeys() {
        Extension extension = this.extensionMap.get('u');
        if (extension == null) {
            return Collections.emptySet();
        }
        if ($assertionsDisabled || (extension instanceof UnicodeLocaleExtension)) {
            return ((UnicodeLocaleExtension) extension).getUnicodeLocaleKeys();
        }
        throw new AssertionError();
    }

    public String getUnicodeLocaleType(String str) {
        Extension extension = this.extensionMap.get('u');
        if (extension == null) {
            return null;
        }
        if ($assertionsDisabled || (extension instanceof UnicodeLocaleExtension)) {
            return ((UnicodeLocaleExtension) extension).getUnicodeLocaleType(LocaleUtils.toLowerString(str));
        }
        throw new AssertionError();
    }

    public boolean isEmpty() {
        return this.extensionMap.isEmpty();
    }

    public static boolean isValidKey(char c2) {
        return LanguageTag.isExtensionSingletonChar(c2) || LanguageTag.isPrivateusePrefixChar(c2);
    }

    public static boolean isValidUnicodeLocaleKey(String str) {
        return UnicodeLocaleExtension.isKey(str);
    }

    private static String toID(SortedMap<Character, Extension> sortedMap) {
        StringBuilder sb = new StringBuilder();
        Extension extension = null;
        for (Map.Entry<Character, Extension> entry : sortedMap.entrySet()) {
            char cCharValue = entry.getKey().charValue();
            Extension value = entry.getValue();
            if (LanguageTag.isPrivateusePrefixChar(cCharValue)) {
                extension = value;
            } else {
                if (sb.length() > 0) {
                    sb.append(LanguageTag.SEP);
                }
                sb.append((Object) value);
            }
        }
        if (extension != null) {
            if (sb.length() > 0) {
                sb.append(LanguageTag.SEP);
            }
            sb.append((Object) extension);
        }
        return sb.toString();
    }

    public String toString() {
        return this.id;
    }

    public String getID() {
        return this.id;
    }

    public int hashCode() {
        return this.id.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LocaleExtensions)) {
            return false;
        }
        return this.id.equals(((LocaleExtensions) obj).id);
    }
}
