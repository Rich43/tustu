package java.beans;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Locale;
import java.util.Map;
import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:java/beans/NameGenerator.class */
class NameGenerator {
    private Map<Object, String> valueToName = new IdentityHashMap();
    private Map<String, Integer> nameToCount = new HashMap();

    public void clear() {
        this.valueToName.clear();
        this.nameToCount.clear();
    }

    public static String unqualifiedClassName(Class cls) {
        if (cls.isArray()) {
            return unqualifiedClassName(cls.getComponentType()) + "Array";
        }
        String name = cls.getName();
        return name.substring(name.lastIndexOf(46) + 1);
    }

    public static String capitalize(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        return str.substring(0, 1).toUpperCase(Locale.ENGLISH) + str.substring(1);
    }

    public String instanceName(Object obj) {
        if (obj == null) {
            return FXMLLoader.NULL_KEYWORD;
        }
        if (obj instanceof Class) {
            return unqualifiedClassName((Class) obj);
        }
        String str = this.valueToName.get(obj);
        if (str != null) {
            return str;
        }
        String strUnqualifiedClassName = unqualifiedClassName(obj.getClass());
        Integer num = this.nameToCount.get(strUnqualifiedClassName);
        int iIntValue = num == null ? 0 : num.intValue() + 1;
        this.nameToCount.put(strUnqualifiedClassName, new Integer(iIntValue));
        String str2 = strUnqualifiedClassName + iIntValue;
        this.valueToName.put(obj, str2);
        return str2;
    }
}
