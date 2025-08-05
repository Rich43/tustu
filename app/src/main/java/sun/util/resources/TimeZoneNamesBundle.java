package sun.util.resources;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/* loaded from: rt.jar:sun/util/resources/TimeZoneNamesBundle.class */
public abstract class TimeZoneNamesBundle extends OpenListResourceBundle {
    @Override // sun.util.resources.OpenListResourceBundle
    protected abstract Object[][] getContents();

    @Override // sun.util.resources.OpenListResourceBundle, java.util.ResourceBundle
    public Object handleGetObject(String str) {
        String[] strArr = (String[]) super.handleGetObject(str);
        if (Objects.isNull(strArr)) {
            return null;
        }
        int length = strArr.length;
        String[] strArr2 = new String[7];
        strArr2[0] = str;
        System.arraycopy(strArr, 0, strArr2, 1, length);
        return strArr2;
    }

    @Override // sun.util.resources.OpenListResourceBundle
    protected <K, V> Map<K, V> createMap(int i2) {
        return new LinkedHashMap(i2);
    }

    @Override // sun.util.resources.OpenListResourceBundle
    protected <E> Set<E> createSet() {
        return new LinkedHashSet();
    }
}
