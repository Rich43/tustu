package W;

import java.io.File;

/* loaded from: TunerStudioMS.jar:W/U.class */
public class U {
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0058, code lost:
    
        if (r0.indexOf("ToothTime") != (-1)) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0062, code lost:
    
        if (r0.indexOf("TriggerTime") == (-1)) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0065, code lost:
    
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0067, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:?, code lost:
    
        return true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean a(java.io.File r6) {
        /*
            r0 = r6
            if (r0 == 0) goto Lb
            r0 = r6
            boolean r0 = r0.exists()
            if (r0 != 0) goto Ld
        Lb:
            r0 = 0
            return r0
        Ld:
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch: java.io.FileNotFoundException -> L19
            r1 = r0
            r2 = r6
            r1.<init>(r2)     // Catch: java.io.FileNotFoundException -> L19
            r7 = r0
            goto L1c
        L19:
            r8 = move-exception
            r0 = 0
            return r0
        L1c:
            java.io.BufferedReader r0 = new java.io.BufferedReader
            r1 = r0
            java.io.InputStreamReader r2 = new java.io.InputStreamReader
            r3 = r2
            r4 = r7
            r3.<init>(r4)
            r1.<init>(r2)
            r8 = r0
        L2c:
            r0 = r8
            java.lang.String r0 = r0.readLine()     // Catch: java.io.IOException -> L6c
            r9 = r0
            r0 = r9
            if (r0 != 0) goto L37
            r0 = 0
            return r0
        L37:
            r0 = r9
            java.lang.String r0 = r0.trim()     // Catch: java.io.IOException -> L6c
            r9 = r0
            r0 = r9
            java.lang.String r1 = "#"
            boolean r0 = r0.startsWith(r1)     // Catch: java.io.IOException -> L6c
            if (r0 != 0) goto L69
            r0 = r9
            java.lang.String r1 = ""
            boolean r0 = r0.equals(r1)     // Catch: java.io.IOException -> L6c
            if (r0 == 0) goto L51
            goto L69
        L51:
            r0 = r9
            java.lang.String r1 = "ToothTime"
            int r0 = r0.indexOf(r1)     // Catch: java.io.IOException -> L6c
            r1 = -1
            if (r0 != r1) goto L65
            r0 = r9
            java.lang.String r1 = "TriggerTime"
            int r0 = r0.indexOf(r1)     // Catch: java.io.IOException -> L6c
            r1 = -1
            if (r0 == r1) goto L67
        L65:
            r0 = 1
            return r0
        L67:
            r0 = 0
            return r0
        L69:
            goto L2c
        L6c:
            r9 = move-exception
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: W.U.a(java.io.File):boolean");
    }

    public static boolean b(File file) {
        String name = file.getName();
        return name.toLowerCase().endsWith(".msq") || name.toLowerCase().endsWith(".tune") || name.toLowerCase().endsWith(".bigtune") || name.toLowerCase().endsWith(".big");
    }

    public static boolean c(File file) {
        String name = file.getName();
        return name.toLowerCase().endsWith(".msl") || name.toLowerCase().endsWith(".csv") || name.toLowerCase().endsWith(".mlg");
    }
}
