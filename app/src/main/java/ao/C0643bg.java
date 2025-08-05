package ao;

import com.efiAnalytics.ui.C1580br;
import h.C1737b;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.UIManager;

/* renamed from: ao.bg, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/bg.class */
public class C0643bg extends JPanel implements InterfaceC0813k {

    /* renamed from: d, reason: collision with root package name */
    private String f5398d;

    /* renamed from: a, reason: collision with root package name */
    Font f5399a;

    /* renamed from: b, reason: collision with root package name */
    int f5400b;

    /* renamed from: c, reason: collision with root package name */
    int f5401c;

    public C0643bg() {
        this.f5398d = null;
        this.f5399a = new Font("SansSerif", 1, com.efiAnalytics.ui.eJ.a(20));
        this.f5400b = com.efiAnalytics.ui.eJ.a(30);
        this.f5401c = com.efiAnalytics.ui.eJ.a(15);
        setLayout(new GridLayout(0, 1, 1, 1));
        if (C1737b.a().a("fieldSmoothing")) {
            addMouseListener(new C0644bh(this));
        }
    }

    public C0643bg(String str) {
        this();
        a(str);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        super.paint(graphics);
        if (C1737b.a().a("fieldSmoothing")) {
            graphics.setColor(UIManager.getColor("Label.foreground"));
            graphics.setFont(this.f5399a);
            graphics.drawString("...", getWidth() - com.efiAnalytics.ui.eJ.a(25), com.efiAnalytics.ui.eJ.a(13));
        }
    }

    public void a(String str) {
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), str));
        this.f5398d = str;
    }

    @Override // ao.InterfaceC0813k
    public void a(Color color) {
    }

    @Override // ao.InterfaceC0813k
    public void a(Color color, int i2) {
    }

    @Override // java.awt.Component
    public void repaint() {
        super.repaint();
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            getComponent(i2).repaint();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i2, int i3) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (int i4 = 0; i4 < getComponentCount(); i4++) {
            JComboBox jComboBoxA = a(getComponent(i4));
            if (jComboBoxA != null) {
                arrayList2.add("" + jComboBoxA.getSelectedItem());
                arrayList.add(jComboBoxA);
            }
        }
        C1580br c1580br = new C1580br();
        C0597O.a(c1580br, this.f5398d, arrayList, arrayList2, this);
        add(c1580br);
        c1580br.show(this, i2, i3);
    }

    private JComboBox a(Component component) {
        if (component instanceof JComboBox) {
            return (JComboBox) component;
        }
        if (!(component instanceof Container)) {
            return null;
        }
        Container container = (Container) component;
        for (int i2 = 0; i2 < container.getComponentCount(); i2++) {
            if (container.getComponent(i2) instanceof JComboBox) {
                return (JComboBox) container.getComponent(i2);
            }
        }
        return null;
    }
}
