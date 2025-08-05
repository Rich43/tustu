package aN;

import G.R;
import G.aL;
import G.aM;
import bH.W;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/* loaded from: TunerStudioMS.jar:aN/e.class */
public class e extends JDialog {

    /* renamed from: a, reason: collision with root package name */
    JTextPane f2638a;

    public e(Frame frame) {
        super(frame, "Memory addressing");
        this.f2638a = new JTextPane();
        super.setDefaultCloseOperation(2);
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        this.f2638a.setEditable(false);
        jPanel.setLayout(new BorderLayout());
        jPanel.setBorder(BorderFactory.createTitledBorder("Memory Addressing"));
        jPanel.add(BorderLayout.CENTER, new JScrollPane(this.f2638a));
        add(BorderLayout.CENTER, jPanel);
        this.f2638a.setFont(new Font("Monospaced", 0, 12));
    }

    public void a(R r2) {
        int iE = r2.p().e();
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < iE; i2++) {
            int iY = r2.O().y(i2);
            sb.append("page = ").append(i2 + 1).append(", size = ").append(r2.p().b(i2).length).append(", Page Offset = 0x").append(Integer.toHexString(iY).toUpperCase()).append(" (").append(iY).append(")").append("\n");
            int iY2 = -1;
            ArrayList arrayList = new ArrayList();
            Iterator itA = r2.a(i2);
            while (itA.hasNext()) {
                arrayList.add((aM) itA.next());
            }
            Iterator it = aL.b(arrayList).iterator();
            while (it.hasNext()) {
                aM aMVar = (aM) it.next();
                if (aMVar.M()) {
                    sb.append("&");
                }
                sb.append("\t").append(W.b(aMVar.aJ(), ' ', 25));
                sb.append(" \toffset = ");
                sb.append(aMVar.g()).append(", \tbyte length = ").append(aMVar.y());
                int iY3 = r2.O().y(i2) + aMVar.g();
                boolean z2 = iY3 <= iY2;
                sb.append(", \tStart Address = ").append("0x").append(Integer.toHexString(iY3).toUpperCase());
                iY2 = ((r2.O().y(i2) + aMVar.g()) + aMVar.y()) - 1;
                sb.append(", \tEnd Address = ").append("0x").append(Integer.toHexString(iY2).toUpperCase());
                if (z2) {
                    sb.append(" *\n");
                } else {
                    sb.append("\n");
                }
            }
        }
        sb.append("\n");
        sb.append("* Denotes Address Overlap\n");
        sb.append("& Denotes ControllerPriority\n");
        this.f2638a.setText(sb.toString());
        this.f2638a.setCaretPosition(0);
    }
}
