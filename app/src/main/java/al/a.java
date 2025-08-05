package aL;

import bH.C;
import bH.I;
import bH.aa;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: TunerStudioMS.jar:aL/a.class */
public class a extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    aa f2599a;

    /* renamed from: b, reason: collision with root package name */
    f f2600b;

    /* renamed from: d, reason: collision with root package name */
    JButton f2602d;

    /* renamed from: e, reason: collision with root package name */
    JButton f2603e;

    /* renamed from: h, reason: collision with root package name */
    JCheckBox f2606h;

    /* renamed from: c, reason: collision with root package name */
    JComboBox f2601c = new JComboBox();

    /* renamed from: f, reason: collision with root package name */
    JPanel f2604f = new JPanel();

    /* renamed from: g, reason: collision with root package name */
    JDialog f2605g = null;

    public a(f fVar, aa aaVar) {
        this.f2599a = null;
        this.f2600b = fVar;
        this.f2599a = aaVar;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(eJ.a(6), eJ.a(6), eJ.a(6), eJ.a(6)));
        this.f2606h = new JCheckBox(a("Include Audio in Data Logs"));
        add("North", this.f2606h);
        this.f2606h.setSelected(fVar.a());
        this.f2606h.addActionListener(new b(this));
        JPanel jPanel = new JPanel();
        jPanel.setBorder(BorderFactory.createEmptyBorder(eJ.a(10), eJ.a(10), eJ.a(10), eJ.a(10)));
        jPanel.setLayout(new FlowLayout(2));
        this.f2602d = new JButton(a("Ok"));
        this.f2602d.addActionListener(new c(this));
        this.f2603e = new JButton(a("Cancel"));
        this.f2603e.addActionListener(new d(this));
        if (I.a()) {
            jPanel.add(this.f2602d);
            jPanel.add(this.f2603e);
        } else {
            jPanel.add(this.f2603e);
            jPanel.add(this.f2602d);
        }
        add("South", jPanel);
        List listA = a();
        this.f2604f.setLayout(new GridLayout(0, 1, eJ.a(5), eJ.a(5)));
        Iterator it = listA.iterator();
        while (it.hasNext()) {
            this.f2601c.addItem((e) it.next());
        }
        b(fVar.b());
        this.f2604f.add(a("Audio Input", this.f2601c));
        add(BorderLayout.CENTER, this.f2604f);
    }

    private JPanel a(String str, Component component) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(eJ.a(5), eJ.a(5)));
        JLabel jLabel = new JLabel(a(str) + CallSiteDescriptor.TOKEN_DELIMITER, 4);
        Dimension dimension = new Dimension(eJ.a(100), UIManager.getFont("Label.font").getSize() + eJ.a(4));
        jLabel.setMaximumSize(dimension);
        jLabel.setPreferredSize(dimension);
        jPanel.add("West", jLabel);
        jPanel.add(BorderLayout.CENTER, component);
        return jPanel;
    }

    private List a() {
        ArrayList arrayList = new ArrayList();
        for (Mixer.Info info : AudioSystem.getMixerInfo()) {
            Mixer mixer = AudioSystem.getMixer(info);
            Line.Info[] targetLineInfo = mixer.getTargetLineInfo();
            if (targetLineInfo.length > 0 && targetLineInfo[0].getLineClass().equals(TargetDataLine.class)) {
                C.d("Found Audio Input: " + info.getName());
                C.d("Line Description: " + info.getDescription());
                for (Line.Info info2 : targetLineInfo) {
                    C.d("\t--- " + ((Object) info2));
                    arrayList.add(new e(this, info, mixer, info2));
                }
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        this.f2600b.a(((e) this.f2601c.getSelectedItem()).toString());
        if (this.f2605g != null) {
            this.f2605g.dispose();
            this.f2605g = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        if (this.f2605g != null) {
            this.f2605g.dispose();
            this.f2605g = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        this.f2600b.a(this.f2606h.isSelected());
    }

    public void a(Window window) {
        this.f2605g = new JDialog(window, a("Data Log Audio Configuration"));
        this.f2605g.add(BorderLayout.CENTER, this);
        this.f2605g.pack();
        bV.a(window, (Component) this.f2605g);
        this.f2605g.setVisible(true);
    }

    private String a(String str) {
        if (this.f2599a != null) {
            str = this.f2599a.a(str);
        }
        return str;
    }

    private void b(String str) {
        for (int i2 = 0; i2 < this.f2601c.getItemCount(); i2++) {
            if (((e) this.f2601c.getItemAt(i2)).equals(str)) {
                this.f2601c.setSelectedIndex(i2);
                return;
            }
        }
    }
}
