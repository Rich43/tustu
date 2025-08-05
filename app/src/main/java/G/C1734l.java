package g;

import java.util.Comparator;

/* renamed from: g.l, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:g/l.class */
final class C1734l implements Comparator {
    C1734l() {
    }

    @Override // java.util.Comparator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(String str, String str2) {
        return str.replaceAll("\\d", "").equalsIgnoreCase(str2.replaceAll("\\d", "")) ? a(str) - a(str2) : str.compareTo(str2);
    }

    int a(String str) {
        String strReplaceAll = str.replaceAll("\\D", "");
        if (strReplaceAll.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(strReplaceAll);
    }
}
