package F;

/* loaded from: TunerStudioMS.jar:F/a.class */
public class a extends b {
    public a(String str) throws NumberFormatException {
        f(str);
    }

    public void f(String str) throws NumberFormatException {
        System.out.println("Local IP: " + str);
        int i2 = Integer.parseInt(str.substring(str.lastIndexOf(".") + 1));
        String strSubstring = str.substring(0, str.lastIndexOf(".") + 1);
        if (i2 > 127) {
            super.g(strSubstring + "30");
            super.h(strSubstring + "120");
        } else {
            super.g(strSubstring + "128");
            super.h(strSubstring + "200");
        }
        super.k(str);
        super.j(str);
        super.i("0.0.0.0");
        super.a(3600);
    }
}
