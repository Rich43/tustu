package bB;

/* loaded from: TunerStudioMS.jar:bB/c.class */
public class c implements b {
    @Override // bB.b
    public r a(String str) {
        String strA = h.i.a("FIELD_MIN_MAX_" + str, (String) null);
        a aVar = new a();
        aVar.a(str);
        if (strA == null || strA.indexOf(";") == -1) {
            aVar.a(Double.NaN);
            aVar.b(Double.NaN);
            aVar.a(-1);
        } else {
            try {
                String[] strArrSplit = strA.split(";");
                String str2 = strArrSplit[0];
                String str3 = strArrSplit.length > 1 ? strArrSplit[1] : "Auto";
                String str4 = strArrSplit.length > 2 ? strArrSplit[2] : "Auto";
                if (str2.contains("Auto")) {
                    aVar.a(Double.NaN);
                } else {
                    aVar.a(Double.parseDouble(str2));
                }
                if (str3.contains("Auto")) {
                    aVar.b(Double.NaN);
                } else {
                    aVar.b(Double.parseDouble(str3));
                }
                if (str4.contains("Auto")) {
                    aVar.a(-1);
                } else {
                    aVar.a(Integer.parseInt(str4));
                }
            } catch (Exception e2) {
            }
        }
        return aVar;
    }

    @Override // bB.b
    public r a(r rVar) {
        a aVar = null;
        String strC = h.i.c("FIELD_MIN_MAX_" + rVar.e());
        if (strC != null) {
            try {
                aVar = new a();
                aVar.a(rVar.e());
                String[] strArrSplit = strC.split(";");
                String str = strArrSplit[0];
                String str2 = strArrSplit.length > 1 ? strArrSplit[1] : "Auto";
                String str3 = strArrSplit.length > 2 ? strArrSplit[2] : "Auto";
                if (str.contains("Auto")) {
                    aVar.a(Double.NaN);
                } else {
                    aVar.a(Double.parseDouble(str));
                }
                if (str2.contains("Auto")) {
                    aVar.b(Double.NaN);
                } else {
                    aVar.b(Double.parseDouble(str2));
                }
                if (str3.contains("Auto")) {
                    aVar.a(-1);
                } else {
                    aVar.a(Integer.parseInt(str3));
                }
            } catch (Exception e2) {
                return null;
            }
        }
        return aVar;
    }
}
