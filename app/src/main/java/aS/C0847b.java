package as;

import ay.C0926c;
import bD.r;
import com.efiAnalytics.remotefileaccess.http.FileAccessPreferences;
import com.efiAnalytics.remotefileaccess.http.HttpFileAccess;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.awt.GridLayout;
import java.io.File;
import javax.swing.JPanel;

/* renamed from: as.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:as/b.class */
public class C0847b extends JPanel implements InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    HttpFileAccess f6241a;

    /* renamed from: b, reason: collision with root package name */
    r f6242b;

    /* renamed from: c, reason: collision with root package name */
    private C0926c f6243c;

    /* renamed from: d, reason: collision with root package name */
    private InterfaceC0846a f6244d = null;

    /* renamed from: e, reason: collision with root package name */
    private static FileAccessPreferences f6245e = null;

    public static FileAccessPreferences a() {
        return f6245e;
    }

    public static void a(FileAccessPreferences fileAccessPreferences) {
        f6245e = fileAccessPreferences;
    }

    public C0847b(C0926c c0926c) {
        this.f6243c = c0926c;
        this.f6241a = new HttpFileAccess(c0926c.c(), a(c0926c), new File(h.i.e("lastFileDir", h.h.d())));
        this.f6242b = new r(this.f6241a, new C0850e(this));
        setLayout(new GridLayout(1, 1));
        add(this.f6242b);
        this.f6241a.addFileDownloadProgressListener(new C0848c(this));
        if (f6245e == null) {
            this.f6241a.setFileAccessPreferences(new C0851f(this));
        } else {
            this.f6241a.setFileAccessPreferences(f6245e);
        }
    }

    private int a(C0926c c0926c) {
        for (String str : c0926c.b()) {
            if (str.equalsIgnoreCase(DeploymentDescriptorParser.ATTR_PORT)) {
                return Integer.parseInt(c0926c.a(str));
            }
        }
        return 80;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        this.f6242b.close();
    }

    public C0926c b() {
        return this.f6243c;
    }

    public void a(InterfaceC0846a interfaceC0846a) {
        this.f6244d = interfaceC0846a;
    }
}
