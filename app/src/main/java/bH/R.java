package bH;

import java.util.List;

/* loaded from: TunerStudioMS.jar:bH/R.class */
public class R {
    public static Object[] a(Object[] objArr) {
        for (int i2 = 0; i2 < objArr.length; i2++) {
            for (int i3 = i2 + 1; i3 < objArr.length; i3++) {
                Object obj = objArr[i2];
                Object obj2 = objArr[i3];
                if (obj.toString().compareTo(obj2.toString()) > 0) {
                    objArr[i2] = obj2;
                    objArr[i3] = obj;
                }
            }
        }
        return objArr;
    }

    public static Integer[] a(Integer[] numArr) {
        for (int i2 = 0; i2 < numArr.length; i2++) {
            for (int i3 = i2 + 1; i3 < numArr.length; i3++) {
                Integer num = numArr[i2];
                Integer num2 = numArr[i3];
                if (num.intValue() > num2.intValue()) {
                    numArr[i2] = num2;
                    numArr[i3] = num;
                }
            }
        }
        return numArr;
    }

    public static Object[] a(Q[] qArr) {
        for (int i2 = 0; i2 < qArr.length; i2++) {
            for (int i3 = i2 + 1; i3 < qArr.length; i3++) {
                Q q2 = qArr[i2];
                Q q3 = qArr[i3];
                if (q2.c().compareTo(q3.c()) > 0) {
                    qArr[i2] = q3;
                    qArr[i3] = q2;
                }
            }
        }
        return qArr;
    }

    public static String[] a(String[] strArr) {
        for (int i2 = 0; i2 < strArr.length; i2++) {
            for (int i3 = i2 + 1; i3 < strArr.length; i3++) {
                String str = strArr[i2];
                String str2 = strArr[i3];
                if (str.compareToIgnoreCase(str2) > 0) {
                    strArr[i2] = str2;
                    strArr[i3] = str;
                }
            }
        }
        return strArr;
    }

    public static List a(List list) {
        for (int i2 = 0; i2 < list.size(); i2++) {
            for (int i3 = i2 + 1; i3 < list.size(); i3++) {
                Q q2 = (Q) list.get(i2);
                Q q3 = (Q) list.get(i3);
                if (q2.c().compareTo(q3.c()) > 0) {
                    list.set(i2, q3);
                    list.set(i3, q2);
                }
            }
        }
        return list;
    }

    public static List b(List list) {
        for (int i2 = 0; i2 < list.size(); i2++) {
            for (int i3 = i2 + 1; i3 < list.size(); i3++) {
                String str = (String) list.get(i2);
                String str2 = (String) list.get(i3);
                if (str.toLowerCase().compareTo(str2.toLowerCase()) > 0) {
                    list.set(i2, str2);
                    list.set(i3, str);
                }
            }
        }
        return list;
    }
}
