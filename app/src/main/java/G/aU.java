package G;

/* loaded from: TunerStudioMS.jar:G/aU.class */
public class aU {

    /* renamed from: a, reason: collision with root package name */
    private static aU f659a = null;

    private aU() {
    }

    public static aU a() {
        if (f659a == null) {
            f659a = new aU();
        }
        return f659a;
    }

    public byte[] a(bS bSVar) {
        if (bSVar.b().equals("20")) {
            return null;
        }
        if (bSVar.b().startsWith("MSnS-extra") || bSVar.b().startsWith("MS1/Extra") || bSVar.b().startsWith("MS/Extra")) {
            return new byte[]{84};
        }
        if (bSVar.b().startsWith("MSII") || bSVar.b().startsWith("MS4") || bSVar.b().startsWith("MShift") || bSVar.b().startsWith("Trans ") || bSVar.b().startsWith("IOExt") || bSVar.b().startsWith("MS Ext") || bSVar.b().toLowerCase().startsWith("monsterfirm")) {
            return new byte[]{83};
        }
        if (bSVar.b().startsWith("MS3") || bSVar.b().startsWith("MS2Extra")) {
            return new byte[]{83};
        }
        if (bSVar.b().startsWith("Ditron")) {
            return new byte[]{113};
        }
        return null;
    }

    public byte[] b(bS bSVar) {
        if (bSVar.b().startsWith("MS3") || bSVar.b().startsWith("Trans ") || bSVar.b().startsWith("IOExt") || bSVar.b().startsWith("MS2Extra")) {
            return new byte[]{77};
        }
        return null;
    }
}
