package aX;

import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.cO;
import com.efiAnalytics.ui.fI;
import com.efiAnalytics.ui.fS;
import com.intel.bluetooth.RemoteDeviceHelper;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.RemoteDevice;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aX/c.class */
public class c extends JPanel implements fS {

    /* renamed from: a, reason: collision with root package name */
    fI f4001a = new fI();

    /* renamed from: b, reason: collision with root package name */
    JButton f4002b = new JButton(C1818g.b("Pair"));

    /* renamed from: c, reason: collision with root package name */
    JTextField f4003c = new JTextField("1234");

    /* renamed from: e, reason: collision with root package name */
    private boolean f4004e = false;

    /* renamed from: f, reason: collision with root package name */
    private RemoteDevice f4005f = null;

    /* renamed from: d, reason: collision with root package name */
    String f4006d = C1818g.b("Enter the PIN for this Device, usually 1234 or 0000");

    public c() {
        setLayout(new BorderLayout());
        add(this.f4001a, BorderLayout.CENTER);
        this.f4001a.setLayout(new BorderLayout());
        try {
            this.f4001a.add(new JLabel(new ImageIcon(cO.a().a(cO.f11112B, this, 200))), BorderLayout.CENTER);
        } catch (V.a e2) {
            Logger.getLogger(c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(0, 1));
        jPanel.add(new JLabel(this.f4006d));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new FlowLayout(1));
        jPanel2.add(this.f4003c);
        jPanel.add(jPanel2);
        jPanel.add(this.f4002b);
        this.f4001a.add(jPanel, "South");
        this.f4003c.addKeyListener(new d(this));
        this.f4002b.addActionListener(new e(this));
    }

    public void a() {
        try {
            String text = this.f4003c.getText();
            this.f4004e = RemoteDeviceHelper.authenticate(this.f4005f, text);
            if (c()) {
                System.out.println("--> Pairing successful with device " + this.f4005f.getBluetoothAddress() + ", using PIN:" + text);
                bV.d(C1818g.b("Successfully Paired with " + this.f4005f.getFriendlyName(false)), this);
                this.f4002b.setEnabled(false);
                this.f4003c.setEnabled(false);
            } else {
                System.out.println("--> Pairing unsuccessful with device " + this.f4005f.getBluetoothAddress() + ", using PIN:" + text);
                bV.d(C1818g.b("Failed to Pair with " + this.f4005f.getFriendlyName(false) + ". Please check PIN and try again."), this);
            }
        } catch (IOException e2) {
            System.out.println("--> Pairing fail with device " + this.f4005f.getBluetoothAddress());
            e2.printStackTrace();
        }
    }

    @Override // com.efiAnalytics.ui.fS
    public boolean g_() {
        return false;
    }

    public void a(RemoteDevice remoteDevice) {
        this.f4005f = remoteDevice;
    }

    protected boolean c() {
        return this.f4004e;
    }

    public void a(Window window) {
        JDialog jDialog = new JDialog(window);
        jDialog.setLayout(new BorderLayout());
        jDialog.add(this, BorderLayout.CENTER);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        JButton jButton = new JButton(C1818g.b("Ok"));
        jPanel.add(jButton);
        jButton.addActionListener(new f(this, jDialog));
        JButton jButton2 = new JButton(C1818g.b("Cancel"));
        jPanel.add(jButton2);
        jButton2.addActionListener(new g(this, jDialog));
        jDialog.pack();
        bV.a(window, (Component) jDialog);
        jDialog.setVisible(true);
    }
}
