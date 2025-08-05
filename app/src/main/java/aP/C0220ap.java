package aP;

import com.efiAnalytics.tunerStudio.panels.C1452a;
import com.efiAnalytics.ui.C1651ei;
import com.efiAnalytics.ui.C1685fp;
import com.efiAnalytics.ui.InterfaceC1650eh;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* renamed from: aP.ap, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/ap.class */
public class C0220ap extends JPanel {

    /* renamed from: d, reason: collision with root package name */
    C1452a f2925d;

    /* renamed from: a, reason: collision with root package name */
    C1651ei f2922a = new C1651ei();

    /* renamed from: b, reason: collision with root package name */
    JComboBox f2923b = new JComboBox();

    /* renamed from: c, reason: collision with root package name */
    JComboBox f2924c = new JComboBox();

    /* renamed from: e, reason: collision with root package name */
    aE.a f2926e = null;

    public C0220ap() {
        setLayout(new BorderLayout(4, 4));
        JPanel jPanel = new JPanel();
        jPanel.setBorder(BorderFactory.createTitledBorder("Projects Main Controller"));
        jPanel.setLayout(new GridLayout(0, 1, 5, 5));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout(8, 8));
        JLabel jLabel = new JLabel("Main Controller CAN ID", 4);
        jPanel2.add(BorderLayout.CENTER, jLabel);
        jLabel.setToolTipText("The CAN ID assigned to the main project controller. This must match what is set in the Tune.");
        jPanel2.add("East", this.f2923b);
        this.f2923b.setEditable(false);
        for (int i2 = 0; i2 < 16; i2++) {
            this.f2923b.addItem("" + i2);
        }
        this.f2923b.addActionListener(new C0221aq(this));
        jPanel.add(jPanel2);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new BorderLayout(5, 5));
        JLabel jLabel2 = new JLabel("Serial Protocol: ", 4);
        jPanel3.add(BorderLayout.CENTER, jLabel2);
        jLabel2.setToolTipText("<htm>Used to select the protocol used to communicate with the Main Controller.<br>If connecting to a remote device through a device<br> with a different protocol this will be required.<html>");
        jPanel3.add("East", this.f2924c);
        this.f2924c.setEditable(false);
        C0223as c0223as = new C0223as(this);
        c0223as.a("Firmware Default");
        c0223as.b("");
        this.f2924c.addItem(c0223as);
        Iterator itB = G.cJ.a().b();
        while (itB.hasNext()) {
            G.cK cKVar = (G.cK) itB.next();
            C0223as c0223as2 = new C0223as(this);
            c0223as2.b(cKVar.c());
            c0223as2.a("Force - " + cKVar.d());
            this.f2924c.addItem(c0223as2);
        }
        jPanel.add(jPanel3);
        add("North", jPanel);
        JPanel jPanel4 = new JPanel();
        add(BorderLayout.CENTER, jPanel4);
        jPanel4.setBorder(BorderFactory.createTitledBorder("CAN Devices"));
        jPanel4.setLayout(new BorderLayout(15, 15));
        jPanel4.add("North", this.f2922a);
        this.f2922a.a((InterfaceC1650eh) new C0222ar(this));
        this.f2925d = new C1452a(G.T.a().c());
        jPanel4.add(BorderLayout.CENTER, this.f2925d);
        C1685fp.a((Component) this.f2925d, false);
    }

    public aE.d[] a() {
        this.f2925d.a();
        Object[] objArrA = this.f2922a.a();
        aE.d[] dVarArr = new aE.d[objArrA.length];
        for (int i2 = 0; i2 < objArrA.length; i2++) {
            dVarArr[i2] = (aE.d) objArrA[i2];
        }
        return dVarArr;
    }

    public void a(aE.d dVar) {
        this.f2922a.a(dVar);
    }

    public int b() {
        return this.f2923b.getSelectedIndex();
    }

    public String c() {
        C0223as c0223as = (C0223as) this.f2924c.getSelectedItem();
        return c0223as != null ? c0223as.b() : "";
    }

    public boolean d() {
        String str = "";
        aE.d[] dVarArrA = a();
        ArrayList arrayList = new ArrayList();
        int i2 = 0;
        while (true) {
            if (i2 >= dVarArrA.length) {
                break;
            }
            str = str + b(dVarArrA[i2]);
            if (arrayList.contains(dVarArrA[i2].a())) {
                str = str + "All deveices must have unique Device Identifiers. " + dVarArrA[i2].a() + " is assigned to more than 1 device.\n";
                break;
            }
            arrayList.add(dVarArrA[i2].a());
            i2++;
        }
        if (str.length() <= 0) {
            return true;
        }
        com.efiAnalytics.ui.bV.d("Please correct the following CAN configuration errors\nor remove them from the project if not needed:\n" + str, this);
        return false;
    }

    public String b(aE.d dVar) {
        String str = "";
        if (dVar.a().length() == 0) {
            str = str + "Device Identifier missing.\n";
        } else if (dVar.a().length() > 6) {
            str = str + "Device Identifier '" + dVar.a() + "' is too long, must be 6 or less characters.\n";
        }
        if (dVar.b().length() == 0) {
            str = str + "Device Description missing.\n";
        }
        if (dVar.c().length() == 0) {
            str = str + "Controller Configuration file must be set for each CAN device.\n";
        } else if (this.f2926e != null && !new File(dVar.a(this.f2926e)).exists()) {
            str = str + "Device Configuration File must be the full path and file name to a valid file.\n";
        }
        return str;
    }

    public void a(aE.a aVar) {
        this.f2926e = aVar;
        for (int i2 = 0; i2 < this.f2922a.a().length; i2++) {
            this.f2922a.a(i2);
        }
        this.f2925d.a(aVar);
        if (aVar.c() < 0 || aVar.c() >= 16) {
            this.f2923b.setSelectedIndex(0);
        } else {
            this.f2923b.setSelectedIndex(aVar.c());
        }
        C0223as c0223as = new C0223as(this);
        c0223as.b(aVar.d());
        this.f2924c.setSelectedItem(c0223as);
        Iterator itI = aVar.I();
        while (itI.hasNext()) {
            aE.d dVar = (aE.d) itI.next();
            try {
                a(dVar.clone());
            } catch (CloneNotSupportedException e2) {
                a(dVar);
                Logger.getLogger(C0220ap.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }

    public void e() {
        this.f2925d.d();
    }
}
