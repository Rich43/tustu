package bW;

import java.util.ArrayList;
import java.util.StringTokenizer;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: TunerStudioMS.jar:bW/c.class */
public class c {

    /* renamed from: a, reason: collision with root package name */
    private String f7648a = "";

    /* renamed from: b, reason: collision with root package name */
    private ArrayList f7649b = new ArrayList();

    public String a() {
        return this.f7648a;
    }

    public void a(String str) {
        this.f7648a = str;
    }

    public String[] b() {
        String[] strArr = new String[this.f7649b.size()];
        Object[] array = this.f7649b.toArray();
        for (int i2 = 0; i2 < strArr.length; i2++) {
            strArr[i2] = (String) array[i2];
        }
        return strArr;
    }

    public void b(String str) {
        this.f7649b.add(str);
    }

    public void c(String str) {
        c();
        StringTokenizer stringTokenizer = new StringTokenizer(str, CallSiteDescriptor.OPERATOR_DELIMITER);
        a(stringTokenizer.nextToken());
        while (stringTokenizer.hasMoreTokens()) {
            b(stringTokenizer.nextToken());
        }
    }

    public void c() {
        this.f7648a = "";
        this.f7649b.clear();
    }

    public String toString() {
        return "commandType: " + this.f7648a + "\ncommandData: " + ((Object) this.f7649b);
    }
}
