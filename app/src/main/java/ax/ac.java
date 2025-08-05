package ax;

/* loaded from: TunerStudioMS.jar:ax/ac.class */
public abstract class ac implements ab {
    public final int a(ab abVar, S s2) throws Z {
        double dB = abVar.b(s2);
        if (dB == ((int) dB) || s2.a() != 1) {
            return (int) Math.round(abVar.b(s2));
        }
        throw new Z(dB);
    }
}
