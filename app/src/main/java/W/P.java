package W;

import aa.C0479b;
import aa.C0481d;
import aa.C0482e;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: TunerStudioMS.jar:W/P.class */
public class P {

    /* renamed from: a, reason: collision with root package name */
    public static int f1944a = 28;

    /* renamed from: b, reason: collision with root package name */
    public static int f1945b = 10;

    /* renamed from: c, reason: collision with root package name */
    private OutputStreamWriter f1942c = null;

    /* renamed from: d, reason: collision with root package name */
    private BufferedWriter f1943d = null;

    /* renamed from: e, reason: collision with root package name */
    private boolean f1946e = true;

    /* renamed from: f, reason: collision with root package name */
    private final Map f1947f = new HashMap();

    /* renamed from: g, reason: collision with root package name */
    private final String[] f1948g = {"FILE_HEADER", "MegaTune", "TunerStudio", "SettingGroups", "Constants", "ConstantsExtensions", "ReferenceTables", "PcVariables", "SettingContextHelp", "Menu", "KeyActions", "VerbiageOverride", "ControllerCommands", "UserDefined", "UiDialogs", "PortEditor", "FTPBrowser", "CurveEditor", "TableEditor", "GaugeConfigurations", "FrontPage", "RunTime", "Tuning", "LoggerDefinition", "AccelerationWizard", "TurboBaud", "BurstMode", "OutputChannels", "Replay", "ExtendedReplay", "Datalog", "VeAnalyze", "DatalogViews", "TuningViews", "EncodedData"};

    public void a(File file, G.R r2, InterfaceC0192r interfaceC0192r) {
        J j2 = null;
        if (file.exists()) {
            j2 = new J();
            j2.a(file, true);
            j2.b();
        }
        try {
            try {
                try {
                    this.f1942c = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
                    this.f1943d = new BufferedWriter(this.f1942c);
                    this.f1943d.append((CharSequence) "encoding=UTF-8\n");
                    ArrayList arrayList = new ArrayList();
                    for (String str : this.f1948g) {
                        N nB = (j2 == null || !j2.a(str)) ? null : j2.b(str);
                        if (this.f1947f.containsKey(str)) {
                            O o2 = (O) this.f1947f.get(str);
                            arrayList.add(str);
                            this.f1943d.write("\n");
                            if (!str.equals("FILE_HEADER")) {
                                this.f1943d.append((CharSequence) "[").append((CharSequence) str).append((CharSequence) "]").append((CharSequence) "\n");
                            }
                            if (this.f1946e) {
                                this.f1943d.append((CharSequence) o2.b());
                                this.f1943d.append((CharSequence) "\n");
                            }
                            o2.a(r2, this.f1943d, nB, interfaceC0192r);
                            this.f1943d.write("\n");
                            this.f1943d.flush();
                        } else if (nB != null) {
                            if (nB.a().equals("UserDefined")) {
                                nB.a("UiDialogs");
                            }
                            a(nB);
                            arrayList.add(str);
                        }
                    }
                    if (j2 != null) {
                        Iterator itB = j2.b();
                        while (itB.hasNext()) {
                            String str2 = (String) itB.next();
                            if (!arrayList.contains(str2)) {
                                a(j2.b(str2));
                                arrayList.add(str2);
                            }
                        }
                    }
                } catch (IOException e2) {
                    throw e2;
                }
            } catch (Exception e3) {
                e3.printStackTrace();
                throw new IOException("Error writing ini file!\nProblem: " + (e3.getMessage() != null ? e3.getMessage() : "Check Log") + "\nfailure during: Open Writer");
            }
        } finally {
            try {
                if (this.f1943d != null) {
                    this.f1943d.close();
                }
                if (this.f1942c != null) {
                    this.f1942c.close();
                }
            } catch (Exception e4) {
            }
        }
    }

    public void a(O o2) {
        this.f1947f.put(o2.a(), o2);
    }

    public void a(boolean z2) {
        this.f1946e = z2;
    }

    public static P a() {
        P p2 = new P();
        p2.a(new C0482e());
        p2.a(new C0481d());
        p2.a(new C0479b());
        return p2;
    }

    private void a(N n2) throws IOException {
        boolean z2 = false;
        if (n2.a().equals("FILE_HEADER")) {
            z2 = true;
        } else {
            this.f1943d.append((CharSequence) "[").append((CharSequence) n2.a()).append((CharSequence) "]").append((CharSequence) "\n");
        }
        int i2 = 0;
        Iterator it = n2.iterator();
        while (it.hasNext()) {
            String strA = ((M) it.next()).a();
            i2 = strA.trim().isEmpty() ? i2 + 1 : 0;
            if (i2 < 5 && (!z2 || !strA.contains("encoding=UTF-8"))) {
                this.f1943d.append((CharSequence) strA).append((CharSequence) "\n");
            }
        }
        this.f1943d.flush();
    }
}
