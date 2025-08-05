package ao;

import java.util.Comparator;

/* renamed from: ao.ae, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/ae.class */
final class C0614ae implements Comparator {
    C0614ae() {
    }

    @Override // java.util.Comparator
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public int compare(String str, String str2) {
        return str.compareToIgnoreCase(str2);
    }
}
