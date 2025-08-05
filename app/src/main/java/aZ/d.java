package aZ;

import W.C0184j;
import W.C0188n;
import bH.W;
import java.io.File;
import java.io.FileWriter;
import javafx.fxml.FXMLLoader;

/* loaded from: TunerStudioMS.jar:aZ/d.class */
public class d {

    /* renamed from: c, reason: collision with root package name */
    private File f4098c;

    /* renamed from: a, reason: collision with root package name */
    FileWriter f4099a;

    /* renamed from: b, reason: collision with root package name */
    int f4100b = 0;

    /* renamed from: d, reason: collision with root package name */
    private char f4101d = '\t';

    private d(String str) {
        this.f4098c = null;
        this.f4099a = null;
        this.f4098c = new File(str);
        this.f4099a = new FileWriter(this.f4098c);
    }

    public static d a(C0188n c0188n, String str, char c2) {
        d dVar = new d(str);
        dVar.a(c2);
        dVar.b(c0188n);
        return dVar;
    }

    private void b(C0188n c0188n) {
        this.f4099a.write(FXMLLoader.CONTROLLER_METHOD_PREFIX + c0188n.g() + "\n");
        for (int i2 = 0; i2 < c0188n.size(); i2++) {
            this.f4099a.write(((C0184j) c0188n.get(i2)).a());
            if (i2 < c0188n.size() - 1) {
                this.f4099a.write(this.f4101d);
            }
        }
        this.f4099a.write("\n");
        if (c0188n.e()) {
            for (int i3 = 0; i3 < c0188n.size(); i3++) {
                this.f4099a.write(((C0184j) c0188n.get(i3)).n());
                if (i3 < c0188n.size() - 1) {
                    this.f4099a.write(this.f4101d);
                }
            }
            this.f4099a.write("\n");
        }
    }

    public void a(C0188n c0188n) {
        a(c0188n, 0, Integer.MAX_VALUE);
    }

    public void a(C0188n c0188n, int i2, int i3) {
        if (c0188n.d() > 0) {
            for (int i4 = 0; i4 < c0188n.d(); i4++) {
                for (int i5 = 0; i5 < c0188n.size(); i5++) {
                    this.f4099a.write(((C0184j) c0188n.get(i5)).c(i4) + "");
                    if (i5 < c0188n.size() - 1) {
                        this.f4099a.write(this.f4101d);
                    }
                }
                this.f4099a.write("\n");
            }
            FileWriter fileWriter = this.f4099a;
            StringBuilder sbAppend = new StringBuilder().append("MARK ");
            StringBuilder sbAppend2 = new StringBuilder().append("");
            int i6 = this.f4100b;
            this.f4100b = i6 + 1;
            fileWriter.write(sbAppend.append(W.a(sbAppend2.append(i6).toString(), '0', 3)).append("\n").toString());
            this.f4099a.flush();
        }
    }

    public void a() {
        this.f4099a.flush();
        this.f4099a.close();
    }

    public void a(char c2) {
        this.f4101d = c2;
    }
}
