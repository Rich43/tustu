package bi;

import G.R;
import G.T;
import G.de;
import aP.C0338f;
import bH.C;
import bH.W;
import bt.C1345d;
import com.efiAnalytics.remotefileaccess.RemoteAccessException;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.dO;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Window;
import java.io.File;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* renamed from: bi.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bi/a.class */
public class C1166a extends C1345d implements InterfaceC1565bc, Serializable {

    /* renamed from: a, reason: collision with root package name */
    String f8144a;

    /* renamed from: b, reason: collision with root package name */
    dO f8145b = new dO();

    /* renamed from: c, reason: collision with root package name */
    JButton f8146c = new JButton("Open Replay Log");

    /* renamed from: d, reason: collision with root package name */
    JButton f8147d = new JButton("Cancel");

    /* renamed from: e, reason: collision with root package name */
    JLabel f8148e = new JLabel("", 4);

    /* renamed from: i, reason: collision with root package name */
    private final String f8149i = "Standard Speed";

    /* renamed from: j, reason: collision with root package name */
    private final String f8150j = "High Speed";

    /* renamed from: f, reason: collision with root package name */
    File f8151f = null;

    /* renamed from: g, reason: collision with root package name */
    aG.b f8152g = null;

    /* renamed from: h, reason: collision with root package name */
    de f8153h = new C1169d(this);

    public C1166a(R r2) throws IllegalArgumentException {
        this.f8144a = r2.c();
        r2.O().a(this.f8153h);
        if (r2.O().W()) {
            this.f8148e.setText("High Speed");
        } else {
            this.f8148e.setText("Standard Speed");
        }
        this.f8148e.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setLayout(new BorderLayout());
        add("North", this.f8148e);
        add(BorderLayout.CENTER, this.f8145b);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        this.f8146c.addActionListener(new C1167b(this));
        this.f8146c.setEnabled(false);
        jPanel.add(this.f8146c);
        this.f8147d.addActionListener(new C1168c(this));
        jPanel.add(this.f8147d);
        add("South", jPanel);
        new C1171f(this).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        if (this.f8151f == null || !this.f8151f.exists()) {
            bV.d("Replay file not found.", this);
        } else {
            C0338f.a().a(this.f8151f);
            b();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        if (this.f8152g != null) {
            this.f8152g.a();
        }
        T.a().c(this.f8144a).O().b(this.f8153h);
        bV.b(this).dispose();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        this.f8151f = new File(aE.a.A().L().getAbsolutePath(), "BigLog_Replay_" + W.a() + ".csv");
        this.f8152g = new aG.b(T.a().c(this.f8144a));
        try {
            this.f8152g.a(this.f8151f, new C1170e(this));
        } catch (RemoteAccessException e2) {
            Logger.getLogger(C1166a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            C.a("Failed to upload replay data", e2, this);
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        R rC = T.a().c(this.f8144a);
        if (rC != null) {
            rC.O().b(this.f8153h);
        }
    }

    public JDialog a(Frame frame, String str) {
        JDialog jDialog = new JDialog(frame, str, true);
        jDialog.add(BorderLayout.CENTER, this);
        jDialog.pack();
        bV.a((Window) frame, (Component) jDialog);
        jDialog.setVisible(true);
        return jDialog;
    }
}
