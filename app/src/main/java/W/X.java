package W;

import bH.C1011s;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:W/X.class */
public class X {

    /* renamed from: a, reason: collision with root package name */
    public static String f1959a = "delimitedAscii";

    /* renamed from: b, reason: collision with root package name */
    public static String f1960b = "VTune Data+";

    /* renamed from: c, reason: collision with root package name */
    public static String f1961c = "VTune Data1";

    /* renamed from: d, reason: collision with root package name */
    public static String f1962d = "CAM Tune Data%";

    /* renamed from: e, reason: collision with root package name */
    public static String f1963e = "Generic O2 Data7";

    /* renamed from: f, reason: collision with root package name */
    public static String f1964f = "Generic Data1";

    /* renamed from: g, reason: collision with root package name */
    public static String f1965g = "DBW DataC";

    /* renamed from: h, reason: collision with root package name */
    public static String f1966h = "Dyno Data";

    /* renamed from: i, reason: collision with root package name */
    public static String f1967i = "Oxygen Sensor Data=";

    /* renamed from: j, reason: collision with root package name */
    public static String f1968j = "Spark Data";

    /* renamed from: k, reason: collision with root package name */
    public static String f1969k = "Engine Data 17";

    /* renamed from: l, reason: collision with root package name */
    public static String f1970l = "Engine Data 2I";

    /* renamed from: m, reason: collision with root package name */
    public static String f1971m = "TunerPro v5 Log";

    /* renamed from: n, reason: collision with root package name */
    public static String f1972n = "Power Vision Log";

    /* renamed from: o, reason: collision with root package name */
    public static String f1973o = "MX2_ATHENA";

    /* renamed from: p, reason: collision with root package name */
    public static String f1974p = "Torque Log";

    /* renamed from: q, reason: collision with root package name */
    public static String f1975q = "ECUDataScan";

    /* renamed from: r, reason: collision with root package name */
    public static String f1976r = "Dynojet Export";

    /* renamed from: s, reason: collision with root package name */
    public static String f1977s = "Dynomite";

    /* renamed from: t, reason: collision with root package name */
    public static String f1978t = "Evo Scan";

    /* renamed from: u, reason: collision with root package name */
    public static String f1979u = "OBDTester";

    /* renamed from: v, reason: collision with root package name */
    public static String f1980v = "ProEFI";

    /* renamed from: w, reason: collision with root package name */
    public static String f1981w = "BigStuffDelimited";

    /* renamed from: x, reason: collision with root package name */
    public static String f1982x = "HPTunerExport";

    /* renamed from: y, reason: collision with root package name */
    public static String f1983y = "HPTunerCvsLog";

    /* renamed from: z, reason: collision with root package name */
    public static String f1984z = "HaltechESP";

    /* renamed from: A, reason: collision with root package name */
    public static String f1985A = "TCFI Delimited";

    /* renamed from: B, reason: collision with root package name */
    public static String f1986B = "ThunderMaxAFR";

    /* renamed from: C, reason: collision with root package name */
    public static String f1987C = "HolleyExport";

    /* renamed from: D, reason: collision with root package name */
    public static String f1988D = "unknownFormat";

    /* renamed from: E, reason: collision with root package name */
    public static String f1989E = "VCDSLog";

    /* renamed from: F, reason: collision with root package name */
    public static String f1990F = "DTALog";

    /* renamed from: G, reason: collision with root package name */
    public static String f1991G = "FAPLog";

    /* renamed from: H, reason: collision with root package name */
    public static String f1992H = "Electromotive/WINTEC";

    /* renamed from: I, reason: collision with root package name */
    public static String f1993I = "MegaLogViewer Log";

    /* renamed from: J, reason: collision with root package name */
    public static String f1994J = "COBB Tuning";

    /* renamed from: K, reason: collision with root package name */
    public static String f1995K = "EFI Technology/ECT";

    /* renamed from: L, reason: collision with root package name */
    public static String f1996L = "ASAM MDF Log";

    /* renamed from: M, reason: collision with root package name */
    public static String f1997M = "ASAM MDF4 Log";

    /* renamed from: N, reason: collision with root package name */
    public static String f1998N = "AEM Export";

    /* renamed from: O, reason: collision with root package name */
    public static String f1999O = "Race Technology";

    /* renamed from: P, reason: collision with root package name */
    public static String f2000P = "Fuel Tech";

    /* renamed from: Q, reason: collision with root package name */
    public static String f2001Q = "ECU Master";

    /* renamed from: R, reason: collision with root package name */
    public static String f2002R = "EMtron";

    /* renamed from: S, reason: collision with root package name */
    public static String f2003S = "MicroTech";

    /* renamed from: T, reason: collision with root package name */
    public static String f2004T = "Race Capture Pro";

    /* renamed from: U, reason: collision with root package name */
    public static String f2005U = "AIM CSV";

    /* renamed from: V, reason: collision with root package name */
    public static String f2006V = "LifRacer CSV";

    /* renamed from: W, reason: collision with root package name */
    public static String f2007W = "MAXX_ECU";

    /* renamed from: X, reason: collision with root package name */
    public static String f2008X = "Motronic_CVS";

    /* renamed from: Y, reason: collision with root package name */
    public static String f2009Y = "Scanmatik_CVS";

    /* renamed from: Z, reason: collision with root package name */
    public static String f2010Z = "NSFW_PCM_Logger_CVS";

    /* renamed from: aa, reason: collision with root package name */
    public static String f2011aa = "Serdia_Log";

    /* renamed from: ab, reason: collision with root package name */
    public static String f2012ab = "VehiCAL_Log";

    /* renamed from: ac, reason: collision with root package name */
    public static String f2013ac = "G4x_Log";

    /* renamed from: ad, reason: collision with root package name */
    public static String f2014ad = "edgeAutonomy";

    /* renamed from: ae, reason: collision with root package name */
    public static String f2015ae = "HWiNFO";

    /* renamed from: af, reason: collision with root package name */
    public static String f2016af = "Durametric";

    public static String a(File file) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException("Log File not found:\n" + file.getAbsolutePath());
        }
        String lowerCase = file.getName().toLowerCase();
        if (lowerCase.endsWith(".xls") || lowerCase.endsWith(".msl")) {
            return c(file) ? f1993I : f1959a;
        }
        if (lowerCase.endsWith(".mlg")) {
            return f1993I;
        }
        if (lowerCase.endsWith(".mtl")) {
            return f2003S;
        }
        if (lowerCase.endsWith(".emulog") || lowerCase.endsWith(".emublog")) {
            return f2001Q;
        }
        if (lowerCase.endsWith(".etl")) {
            return f1995K;
        }
        if (lowerCase.endsWith(".mdf") || lowerCase.endsWith(".mf4") || (lowerCase.endsWith(".dat") && C1011s.a(file, ak.Z.f4640f))) {
            return C1011s.a(file, ak.U.f4600a) ? f1997M : f1996L;
        }
        if (lowerCase.endsWith(".dm3")) {
            String strB = b(file);
            bH.C.c(PdfOps.SINGLE_QUOTE_TOKEN + strB + PdfOps.SINGLE_QUOTE_TOKEN);
            return strB;
        }
        if (lowerCase.toLowerCase().endsWith(".maxxecu-zip-log") || lowerCase.toLowerCase().endsWith(".maxxecu-log")) {
            return f2007W;
        }
        if (!lowerCase.endsWith(".csv") && !lowerCase.endsWith(".txt") && !lowerCase.endsWith(".dat") && !lowerCase.endsWith(".rec") && !lowerCase.endsWith(".log")) {
            return f1959a;
        }
        String strD = d(file);
        bH.C.c(PdfOps.SINGLE_QUOTE_TOKEN + strD + PdfOps.SINGLE_QUOTE_TOKEN);
        return strD;
    }

    private static String b(File file) {
        FileInputStream fileInputStream = new FileInputStream(file);
        String str = "";
        try {
            try {
                fileInputStream.skip(1638L);
                int i2 = fileInputStream.read();
                while (i2 != 0 && i2 != 19) {
                    str = str + ((char) i2);
                    i2 = fileInputStream.read();
                }
                if (str.length() == 0) {
                    while (true) {
                        if (i2 >= 65 && i2 <= 90) {
                            break;
                        }
                        i2 = fileInputStream.read();
                    }
                    while (i2 != 0 && i2 != 19) {
                        str = str + ((char) i2);
                        i2 = fileInputStream.read();
                    }
                }
                return str;
            } catch (IOException e2) {
                e2.printStackTrace();
                throw new V.a("Unable to read DM3 file type identifier.");
            }
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0046  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static boolean c(java.io.File r5) {
        /*
            java.io.FileInputStream r0 = new java.io.FileInputStream
            r1 = r0
            r2 = r5
            r1.<init>(r2)
            r6 = r0
            r0 = 6
            byte[] r0 = new byte[r0]     // Catch: java.io.IOException -> L65 java.lang.Throwable -> L96
            r7 = r0
            r0 = r6
            r1 = r7
            int r0 = r0.read(r1)     // Catch: java.io.IOException -> L65 java.lang.Throwable -> L96
            r0 = r7
            r1 = 0
            r0 = r0[r1]     // Catch: java.io.IOException -> L65 java.lang.Throwable -> L96
            r1 = 77
            if (r0 != r1) goto L46
            r0 = r7
            r1 = 1
            r0 = r0[r1]     // Catch: java.io.IOException -> L65 java.lang.Throwable -> L96
            r1 = 76
            if (r0 != r1) goto L46
            r0 = r7
            r1 = 2
            r0 = r0[r1]     // Catch: java.io.IOException -> L65 java.lang.Throwable -> L96
            r1 = 86
            if (r0 != r1) goto L46
            r0 = r7
            r1 = 3
            r0 = r0[r1]     // Catch: java.io.IOException -> L65 java.lang.Throwable -> L96
            r1 = 76
            if (r0 != r1) goto L46
            r0 = r7
            r1 = 4
            r0 = r0[r1]     // Catch: java.io.IOException -> L65 java.lang.Throwable -> L96
            r1 = 71
            if (r0 != r1) goto L46
            r0 = r7
            r1 = 5
            r0 = r0[r1]     // Catch: java.io.IOException -> L65 java.lang.Throwable -> L96
            if (r0 != 0) goto L46
            r0 = 1
            goto L47
        L46:
            r0 = 0
        L47:
            r8 = r0
            r0 = r6
            r0.close()     // Catch: java.lang.Exception -> L4f
            goto L63
        L4f:
            r9 = move-exception
            java.lang.Class<W.X> r0 = W.X.class
            java.lang.String r0 = r0.getName()
            java.util.logging.Logger r0 = java.util.logging.Logger.getLogger(r0)
            java.util.logging.Level r1 = java.util.logging.Level.INFO
            java.lang.String r2 = "Odd, nut no biggie"
            r3 = r9
            r0.log(r1, r2, r3)
        L63:
            r0 = r8
            return r0
        L65:
            r7 = move-exception
            java.lang.Class<W.X> r0 = W.X.class
            java.lang.String r0 = r0.getName()     // Catch: java.lang.Throwable -> L96
            java.util.logging.Logger r0 = java.util.logging.Logger.getLogger(r0)     // Catch: java.lang.Throwable -> L96
            java.util.logging.Level r1 = java.util.logging.Level.WARNING     // Catch: java.lang.Throwable -> L96
            java.lang.String r2 = "File not readable."
            r3 = r7
            r0.log(r1, r2, r3)     // Catch: java.lang.Throwable -> L96
            r0 = 0
            r8 = r0
            r0 = r6
            r0.close()     // Catch: java.lang.Exception -> L80
            goto L94
        L80:
            r9 = move-exception
            java.lang.Class<W.X> r0 = W.X.class
            java.lang.String r0 = r0.getName()
            java.util.logging.Logger r0 = java.util.logging.Logger.getLogger(r0)
            java.util.logging.Level r1 = java.util.logging.Level.INFO
            java.lang.String r2 = "Odd, nut no biggie"
            r3 = r9
            r0.log(r1, r2, r3)
        L94:
            r0 = r8
            return r0
        L96:
            r10 = move-exception
            r0 = r6
            r0.close()     // Catch: java.lang.Exception -> L9f
            goto Lb3
        L9f:
            r11 = move-exception
            java.lang.Class<W.X> r0 = W.X.class
            java.lang.String r0 = r0.getName()
            java.util.logging.Logger r0 = java.util.logging.Logger.getLogger(r0)
            java.util.logging.Level r1 = java.util.logging.Level.INFO
            java.lang.String r2 = "Odd, nut no biggie"
            r3 = r11
            r0.log(r1, r2, r3)
        Lb3:
            r0 = r10
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: W.X.c(java.io.File):boolean");
    }

    private static String d(File file) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        try {
            try {
                String line = bufferedReader.readLine();
                boolean z2 = false;
                for (int i2 = 0; line != null && i2 < 100; i2++) {
                    if (line.startsWith("ScannerPro Engine") || line.startsWith("TunerPro")) {
                        return f1971m;
                    }
                    if (line.startsWith("\"Time Line\";\"")) {
                        String str = f2000P;
                        try {
                            bufferedReader.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                        return str;
                    }
                    if (line.contains("Dynojet Power Vision Log File")) {
                        String str2 = f1972n;
                        try {
                            bufferedReader.close();
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                        return str2;
                    }
                    if (line.indexOf("\"Time, sec\";") >= 0) {
                        String str3 = f2009Y;
                        try {
                            bufferedReader.close();
                        } catch (IOException e4) {
                            e4.printStackTrace();
                        }
                        return str3;
                    }
                    if (line.startsWith("Time_s;")) {
                        String str4 = f1973o;
                        try {
                            bufferedReader.close();
                        } catch (IOException e5) {
                            e5.printStackTrace();
                        }
                        return str4;
                    }
                    if (line.startsWith("Device Time") || line.startsWith("GPS Time")) {
                        String str5 = f1974p;
                        try {
                            bufferedReader.close();
                        } catch (IOException e6) {
                            e6.printStackTrace();
                        }
                        return str5;
                    }
                    if (line.startsWith("#ECUDatascan")) {
                        String str6 = f1975q;
                        try {
                            bufferedReader.close();
                        } catch (IOException e7) {
                            e7.printStackTrace();
                        }
                        return str6;
                    }
                    if (i2 == 0 && line.contains(".djl")) {
                        String str7 = f1976r;
                        try {
                            bufferedReader.close();
                        } catch (IOException e8) {
                            e8.printStackTrace();
                        }
                        return str7;
                    }
                    if (i2 == 0 && line.contains("LogEntryDate,LogEntryTime")) {
                        String str8 = f1978t;
                        try {
                            bufferedReader.close();
                        } catch (IOException e9) {
                            e9.printStackTrace();
                        }
                        return str8;
                    }
                    if (line.contains("***_Parameter_End***")) {
                        String str9 = f1977s;
                        try {
                            bufferedReader.close();
                        } catch (IOException e10) {
                            e10.printStackTrace();
                        }
                        return str9;
                    }
                    if (i2 == 0 && line.startsWith(",\"")) {
                        String str10 = f1979u;
                        try {
                            bufferedReader.close();
                        } catch (IOException e11) {
                            e11.printStackTrace();
                        }
                        return str10;
                    }
                    if (i2 == 0 && line.startsWith("Interval (ms)")) {
                        String str11 = f1980v;
                        try {
                            bufferedReader.close();
                        } catch (IOException e12) {
                            e12.printStackTrace();
                        }
                        return str11;
                    }
                    if (i2 == 0 && line.startsWith("Vehicle Type:")) {
                        String str12 = f1982x;
                        try {
                            bufferedReader.close();
                        } catch (IOException e13) {
                            e13.printStackTrace();
                        }
                        return str12;
                    }
                    if (i2 == 0 && line.startsWith("HP Tuners CSV")) {
                        String str13 = f1983y;
                        try {
                            bufferedReader.close();
                        } catch (IOException e14) {
                            e14.printStackTrace();
                        }
                        return str13;
                    }
                    if (line.startsWith("Software : Haltech ESP") || line.startsWith("Software : Haltech NSP") || line.startsWith("Software : ECU Manager")) {
                        String str14 = f1984z;
                        try {
                            bufferedReader.close();
                        } catch (IOException e15) {
                            e15.printStackTrace();
                        }
                        return str14;
                    }
                    if (line.startsWith("\"DataFlash Configuration Flag")) {
                        String str15 = f1985A;
                        try {
                            bufferedReader.close();
                        } catch (IOException e16) {
                            e16.printStackTrace();
                        }
                        return str15;
                    }
                    if (line.startsWith("Point Number")) {
                        String str16 = f1987C;
                        try {
                            bufferedReader.close();
                        } catch (IOException e17) {
                            e17.printStackTrace();
                        }
                        return str16;
                    }
                    if (line.contains("Afr Adjustment Required")) {
                        String str17 = f1986B;
                        try {
                            bufferedReader.close();
                        } catch (IOException e18) {
                            e18.printStackTrace();
                        }
                        return str17;
                    }
                    if (line.contains("Replay Data Uploaded") || line.startsWith("Log Window - Logged On")) {
                        String str18 = f1981w;
                        try {
                            bufferedReader.close();
                        } catch (IOException e19) {
                            e19.printStackTrace();
                        }
                        return str18;
                    }
                    if (line.contains("VCDS") || line.startsWith("Marker,STAMP,")) {
                        String str19 = f1989E;
                        try {
                            bufferedReader.close();
                        } catch (IOException e20) {
                            e20.printStackTrace();
                        }
                        return str19;
                    }
                    if (line.startsWith("WINTEC")) {
                        String str20 = f1992H;
                        try {
                            bufferedReader.close();
                        } catch (IOException e21) {
                            e21.printStackTrace();
                        }
                        return str20;
                    }
                    if (line.contains(",AP Info:[") || line.contains(",\"AP Info:[")) {
                        String str21 = f1994J;
                        try {
                            bufferedReader.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                        return str21;
                    }
                    if (line.startsWith("SESSION")) {
                        String str22 = f1990F;
                        try {
                            bufferedReader.close();
                        } catch (IOException e23) {
                            e23.printStackTrace();
                        }
                        return str22;
                    }
                    if (line.startsWith("Date;Time;")) {
                        String str23 = f1991G;
                        try {
                            bufferedReader.close();
                        } catch (IOException e24) {
                            e24.printStackTrace();
                        }
                        return str23;
                    }
                    if (line.startsWith("       Time/s\t")) {
                        String str24 = f1998N;
                        try {
                            bufferedReader.close();
                        } catch (IOException e25) {
                            e25.printStackTrace();
                        }
                        return str24;
                    }
                    if (line.startsWith("Data Output From Race Technology Data Logging System")) {
                        String str25 = f1999O;
                        try {
                            bufferedReader.close();
                        } catch (IOException e26) {
                            e26.printStackTrace();
                        }
                        return str25;
                    }
                    if (line.matches("TimeStamp(,\\d+)+") || line.indexOf(".ecf") != -1) {
                        String str26 = f2002R;
                        try {
                            bufferedReader.close();
                        } catch (IOException e27) {
                            e27.printStackTrace();
                        }
                        return str26;
                    }
                    if (line.startsWith("\"Interval\"|")) {
                        String str27 = f2004T;
                        try {
                            bufferedReader.close();
                        } catch (IOException e28) {
                            e28.printStackTrace();
                        }
                        return str27;
                    }
                    if (line.toUpperCase().indexOf("AIM CSV") != -1) {
                        String str28 = f2005U;
                        try {
                            bufferedReader.close();
                        } catch (IOException e29) {
                            e29.printStackTrace();
                        }
                        return str28;
                    }
                    if (line.indexOf("####, ##") > 0) {
                        String str29 = f2006V;
                        try {
                            bufferedReader.close();
                        } catch (IOException e30) {
                            e30.printStackTrace();
                        }
                        return str29;
                    }
                    if (line.indexOf("VAGHWNumber") >= 0) {
                        String str30 = f2008X;
                        try {
                            bufferedReader.close();
                        } catch (IOException e31) {
                            e31.printStackTrace();
                        }
                        return str30;
                    }
                    if (line.indexOf("SiE MDT Graph Log") >= 0) {
                        String str31 = f2011aa;
                        try {
                            bufferedReader.close();
                        } catch (IOException e32) {
                            e32.printStackTrace();
                        }
                        return str31;
                    }
                    if (z2 && i2 == 1 && line.indexOf("Elapsed time") >= 0) {
                        String str32 = f2012ab;
                        try {
                            bufferedReader.close();
                        } catch (IOException e33) {
                            e33.printStackTrace();
                        }
                        return str32;
                    }
                    if (line.startsWith("Clock Time, Elapsed Time")) {
                        String str33 = f2010Z;
                        try {
                            bufferedReader.close();
                        } catch (IOException e34) {
                            e34.printStackTrace();
                        }
                        return str33;
                    }
                    if (line.startsWith("Name,PC Datalog")) {
                        String str34 = f2013ac;
                        try {
                            bufferedReader.close();
                        } catch (IOException e35) {
                            e35.printStackTrace();
                        }
                        return str34;
                    }
                    if (line.startsWith("<Clock>[ms]")) {
                        String str35 = f2014ad;
                        try {
                            bufferedReader.close();
                        } catch (IOException e36) {
                            e36.printStackTrace();
                        }
                        return str35;
                    }
                    if (i2 == 0 && line.startsWith("Time;Engine")) {
                        String str36 = f2016af;
                        try {
                            bufferedReader.close();
                        } catch (IOException e37) {
                            e37.printStackTrace();
                        }
                        return str36;
                    }
                    if (line.startsWith("\"Time,")) {
                        String str37 = f1959a;
                        try {
                            bufferedReader.close();
                        } catch (IOException e38) {
                            e38.printStackTrace();
                        }
                        return str37;
                    }
                    if (i2 == 0 && line.startsWith("Date,Time,") && line.indexOf(" Memory ") > 10) {
                        String str38 = f2015ae;
                        try {
                            bufferedReader.close();
                        } catch (IOException e39) {
                            e39.printStackTrace();
                        }
                        return str38;
                    }
                    if (i2 == 0 && line.startsWith("Time", 1)) {
                        z2 = true;
                    }
                    line = bufferedReader.readLine();
                }
                String str39 = f1959a;
                try {
                    bufferedReader.close();
                } catch (IOException e40) {
                    e40.printStackTrace();
                }
                return str39;
            } catch (IOException e41) {
                e41.printStackTrace();
                throw new V.a("Unable to read CSV file type.");
            }
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e42) {
                e42.printStackTrace();
            }
        }
    }
}
