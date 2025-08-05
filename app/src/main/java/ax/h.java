package aX;

import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.eJ;
import com.efiAnalytics.ui.fI;
import com.efiAnalytics.ui.fS;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aX/h.class */
public class h extends JPanel implements fS {

    /* renamed from: a, reason: collision with root package name */
    JList f4013a = new JList();

    /* renamed from: b, reason: collision with root package name */
    DefaultListModel f4014b = new DefaultListModel();

    /* renamed from: c, reason: collision with root package name */
    JList f4015c = new JList();

    /* renamed from: d, reason: collision with root package name */
    DefaultListModel f4016d = new DefaultListModel();

    /* renamed from: e, reason: collision with root package name */
    JButton f4017e = new JButton(C1818g.b("Scan for Bluetooth Adapters"));

    /* renamed from: f, reason: collision with root package name */
    fI f4018f = new fI();

    /* renamed from: g, reason: collision with root package name */
    final Object f4019g = new Object();

    /* renamed from: h, reason: collision with root package name */
    aC.d f4020h = new l(this);

    public h() {
        setLayout(new BorderLayout());
        this.f4018f.setLayout(new BorderLayout());
        this.f4018f.a(C1818g.b("Searching") + "...");
        JScrollPane jScrollPane = new JScrollPane(this.f4013a);
        jScrollPane.setBorder(BorderFactory.createTitledBorder(C1818g.b("Already Paired Adapters")));
        this.f4013a.setModel(this.f4014b);
        this.f4013a.setBorder(BorderFactory.createLoweredBevelBorder());
        jScrollPane.setPreferredSize(eJ.a(240, 60));
        this.f4018f.add(jScrollPane, "North");
        this.f4013a.setEnabled(false);
        JScrollPane jScrollPane2 = new JScrollPane(this.f4015c);
        jScrollPane2.setBorder(BorderFactory.createTitledBorder(C1818g.b("Discovered Unpaired Adapters")));
        this.f4015c.setModel(this.f4016d);
        this.f4015c.setBorder(BorderFactory.createLoweredBevelBorder());
        jScrollPane2.setPreferredSize(eJ.a(240, 120));
        this.f4018f.add(jScrollPane2, BorderLayout.CENTER);
        add(this.f4018f, BorderLayout.CENTER);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(0));
        jPanel.add(this.f4017e);
        add(jPanel, "South");
        this.f4017e.addActionListener(new i(this));
    }

    public void a() {
        if (!aC.b.a() || !this.f4017e.isEnabled()) {
            bV.d(C1818g.b("Bluetooth is not enabled on this computer."), this);
            return;
        }
        j jVar = new j(this);
        this.f4017e.setEnabled(false);
        this.f4018f.b();
        jVar.start();
    }

    public void c() {
        if (this.f4017e.isEnabled()) {
            return;
        }
        try {
            LocalDevice.getLocalDevice().getDiscoveryAgent().cancelInquiry(this.f4020h);
        } catch (BluetoothStateException e2) {
            Logger.getLogger(h.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    public RemoteDevice d() {
        if (this.f4015c.getSelectedValue() instanceof m) {
            return ((m) this.f4015c.getSelectedValue()).a();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        this.f4014b.clear();
        this.f4016d.clear();
        aC.b.a(this.f4020h);
        SwingUtilities.invokeLater(new k(this));
    }

    @Override // com.efiAnalytics.ui.fS
    public boolean g_() {
        return false;
    }
}
