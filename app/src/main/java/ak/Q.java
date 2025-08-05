package ak;

import com.sun.corba.se.impl.orbutil.ORBConstants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: TunerStudioMS.jar:ak/Q.class */
public class Q extends R {

    /* renamed from: a, reason: collision with root package name */
    float[] f4595a;

    /* renamed from: b, reason: collision with root package name */
    ArrayList f4596b;

    /* renamed from: H, reason: collision with root package name */
    boolean f4597H;

    public Q() {
        super(",", false);
        this.f4595a = null;
        this.f4596b = new ArrayList();
        this.f4597H = true;
        this.f4837t = true;
    }

    @Override // ak.C0546f, W.V
    public Iterator b() {
        String strL;
        try {
            C0543c c0543c = new C0543c();
            c0543c.a("Time");
            c0543c.b("s.");
            this.f4823g.add(c0543c);
            C0543c c0543c2 = null;
            do {
                strL = l();
                if (strL.startsWith("Channel :")) {
                    c0543c2 = new C0543c();
                    String strTrim = strL.substring(strL.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) + 1, strL.length()).trim();
                    if (strTrim.equals("MAPSource")) {
                        c0543c2.a("Manifold Pressure");
                    } else if (strTrim.equals("CoolantTemp")) {
                        c0543c2.a("Coolant Temp");
                    } else if (strTrim.equals("Load")) {
                        c0543c2.a("Fuel Load");
                    } else if (strTrim.equals("LoadIgnition")) {
                        c0543c2.a("Ignition Load");
                    } else {
                        c0543c2.a(l(strTrim));
                    }
                    this.f4823g.add(c0543c2);
                } else if (c0543c2 != null && strL.startsWith("Type :")) {
                    String strTrim2 = strL.substring(strL.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) + 1, strL.length()).trim();
                    if (strTrim2.equals("Percentage")) {
                        c0543c2.b(FXMLLoader.RESOURCE_KEY_PREFIX);
                        c0543c2.a(0.1f);
                        c0543c2.a(1);
                    } else if (strTrim2.equals("Decibel")) {
                        c0543c2.b(ORBConstants.DEFAULT_DB_NAME);
                        c0543c2.a(0.01f);
                        c0543c2.a(2);
                    } else if (strTrim2.equals("BatteryVoltage")) {
                        c0543c2.b("volts");
                        c0543c2.a(0.001f);
                        c0543c2.a(1);
                    } else if (strTrim2.equals("AFR")) {
                        c0543c2.b(":1");
                        c0543c2.a(0.001f);
                        c0543c2.a(3);
                    } else if (strTrim2.equals("Pressure")) {
                        c0543c2.b("kPa");
                        c0543c2.a(0.1f);
                        c0543c2.a(1);
                        c0543c2.b(-1013.0f);
                    } else if (strTrim2.equals("Angle")) {
                        c0543c2.b("°");
                        c0543c2.a(0.1f);
                        c0543c2.a(1);
                    } else if (strTrim2.equals("Time_us")) {
                        c0543c2.b("ms");
                        c0543c2.a(0.001f);
                        c0543c2.a(3);
                    } else if (strTrim2.equals("Temperature")) {
                        c0543c2.b("°C");
                        c0543c2.a(0.1f);
                        c0543c2.a(2);
                        c0543c2.b(-2730.0f);
                    } else if (strTrim2.equals("EngineSpeed")) {
                        c0543c2.b("RPM");
                        c0543c2.a(1.0f);
                        c0543c2.a(0);
                    } else if (strTrim2.equals("Speed")) {
                        c0543c2.b("");
                        c0543c2.a(0.1f);
                        c0543c2.a(1);
                    } else if (strTrim2.equals("Raw")) {
                        c0543c2.b("");
                        c0543c2.a(1.0f);
                        c0543c2.a(0);
                    }
                }
                if (strL == null) {
                    break;
                }
            } while (!strL.startsWith("Log :"));
            if (strL.startsWith("Log :")) {
            }
        } catch (V.f e2) {
            Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        } catch (IOException e3) {
            Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        } catch (Exception e4) {
            bH.C.a("Failed to get units from this row:\n");
            Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
        }
        ArrayList arrayList = new ArrayList();
        Iterator it = this.f4823g.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        return arrayList.iterator();
    }

    private String l(String str) {
        boolean z2;
        StringBuilder sb = new StringBuilder();
        boolean z3 = false;
        char[] charArray = str.toCharArray();
        int length = charArray.length;
        for (int i2 = 0; i2 < length; i2++) {
            char c2 = charArray[i2];
            if (z3 || c2 > 'Z' || c2 < 'A') {
                sb.append(c2);
                z2 = (c2 <= 'Z' && c2 >= 'A') || c2 == '_' || c2 == ' ';
            } else {
                sb.append(" ").append(c2);
                z2 = true;
            }
            z3 = z2;
        }
        return sb.toString().trim();
    }

    @Override // ak.C0546f
    protected int b(String str) {
        BufferedReader bufferedReader = null;
        try {
            try {
                this.f4824h = new File(str);
                BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(new FileInputStream(this.f4824h)));
                for (int i2 = 0; i2 < 100; i2++) {
                    String line = bufferedReader2.readLine();
                    if (line == null) {
                        throw new V.a("Unable to read Haltech file, is it corrupt?");
                    }
                    if (line.startsWith("Channel :")) {
                        int i3 = i2;
                        if (bufferedReader2 != null) {
                            try {
                                bufferedReader2.close();
                            } catch (IOException e2) {
                                Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                            }
                        }
                        return i3;
                    }
                }
                throw new V.a("Log Fie Header not as expected for a Haltech ESP log file.");
            } catch (FileNotFoundException e3) {
                throw new V.a("Unable to open file for reading:\n" + str);
            } catch (IOException e4) {
                throw new V.a("Unable to read from file:\n" + str);
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    bufferedReader.close();
                } catch (IOException e5) {
                    Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
                }
            }
            throw th;
        }
    }

    @Override // ak.C0546f, W.V
    public float[] c() throws V.a {
        String strTrim;
        if (this.f4595a == null) {
            this.f4595a = new float[this.f4823g.size()];
        }
        if (this.f4837t && this.f4832p >= 2000 && k()) {
            throw new V.a("This Edition is limited to loading 500 rows of data. \nPlease Register to load large log files.");
        }
        this.f4596b.clear();
        try {
            String strL = l();
            boolean z2 = false;
            do {
                if (this.f4829m == -1) {
                    this.f4829m = this.f4824h.length() / (strL.length() + 3);
                }
                if (strL.endsWith(this.f4822f)) {
                }
                aD aDVar = new aD(strL, this.f4822f);
                this.f4825i = new float[aDVar.c()];
                int i2 = 0;
                while (true) {
                    if (i2 >= this.f4825i.length) {
                        break;
                    }
                    try {
                        strTrim = aDVar.b().trim();
                    } catch (Exception e2) {
                        bH.C.c("Error Parsing record:\n" + strL);
                        strTrim = "0";
                        e2.printStackTrace();
                    }
                    if (strTrim.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) != -1) {
                        this.f4825i[i2] = g(strTrim);
                    } else if (strTrim.length() == 0 || strTrim.contains("-2.147484E+09")) {
                        this.f4825i[i2] = Float.NaN;
                    } else {
                        try {
                            this.f4825i[i2] = Float.parseFloat(bH.W.b(strTrim, ",", "."));
                        } catch (NumberFormatException e3) {
                            this.f4825i[i2] = Float.NaN;
                        }
                    }
                    if (!Float.isNaN(this.f4825i[i2])) {
                        if (i2 > 0 && this.f4596b.contains(Integer.valueOf(i2))) {
                            a(true);
                            z2 = true;
                            break;
                        }
                        this.f4595a[i2] = this.f4825i[i2];
                        this.f4596b.add(Integer.valueOf(i2));
                    }
                    i2++;
                }
                if (!z2) {
                    try {
                        strL = l();
                    } catch (V.f e4) {
                        z2 = true;
                    } catch (IOException e5) {
                        e5.printStackTrace();
                        throw new V.a("IO Error reading row from file on row " + this.f4832p + ".");
                    }
                }
                if (z2) {
                    break;
                }
            } while (strL != null);
            return this.f4595a;
        } catch (IOException e6) {
            e6.printStackTrace();
            throw new V.a("IO Error reading row from file on row " + this.f4832p + ".");
        }
    }
}
