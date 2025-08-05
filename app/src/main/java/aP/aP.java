package aP;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/* loaded from: TunerStudioMS.jar:aP/aP.class */
public class aP extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    ButtonGroup f2837a = new ButtonGroup();

    /* renamed from: b, reason: collision with root package name */
    List f2838b = new ArrayList();

    public aP() {
        String[] strArrD = G.T.a().d();
        String strC = G.T.a().c().c();
        setLayout(new GridLayout(0, 1));
        aQ aQVar = new aQ(this);
        if (strArrD != null) {
            for (int i2 = 0; i2 < strArrD.length; i2++) {
                JRadioButton jRadioButton = new JRadioButton(strArrD[i2]);
                jRadioButton.setActionCommand(strArrD[i2]);
                jRadioButton.addActionListener(aQVar);
                this.f2837a.add(jRadioButton);
                add(jRadioButton);
                if (strC != null && strArrD[i2].equals(strC)) {
                    this.f2837a.setSelected(jRadioButton.getModel(), true);
                    b();
                }
            }
        }
    }

    public void a(aR aRVar) {
        this.f2838b.add(aRVar);
    }

    public String a() {
        return this.f2837a.getSelection().getActionCommand();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        String strA = a();
        Iterator it = this.f2838b.iterator();
        while (it.hasNext()) {
            ((aR) it.next()).a(strA);
        }
    }
}
