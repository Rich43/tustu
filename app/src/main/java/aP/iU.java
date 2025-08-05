package aP;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JLabel;

/* loaded from: TunerStudioMS.jar:aP/iU.class */
class iU extends JLabel {

    /* renamed from: a, reason: collision with root package name */
    String f3688a = "";

    /* renamed from: b, reason: collision with root package name */
    final ArrayList f3689b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    int f3690c = 32;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ iR f3691d;

    iU(iR iRVar) {
        this.f3691d = iRVar;
    }

    @Override // javax.swing.JLabel
    public void setText(String str) {
        super.setText(str);
        this.f3688a = str;
        if (this.f3689b != null) {
            synchronized (this.f3689b) {
                if (!str.trim().equals("")) {
                    this.f3689b.add(str);
                }
                if (this.f3689b.size() > this.f3690c + 1) {
                    this.f3689b.remove(0);
                }
                String str2 = "<html>";
                try {
                    Iterator it = this.f3689b.iterator();
                    while (it.hasNext()) {
                        if (!str2.equals("<html>")) {
                            str2 = str2 + "<br>";
                        }
                        str2 = str2 + it.next();
                    }
                } catch (Exception e2) {
                    bH.C.c("Caught an exception trying to update status history, handled.\n\t" + e2.getMessage());
                }
                setToolTipText(str2 + "</html>");
            }
        }
        repaint();
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        super.paint(graphics);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        Dimension minimumSize = super.getMinimumSize();
        minimumSize.width = cZ.a().c().getWidth() / 4;
        return minimumSize;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        preferredSize.width = cZ.a().c().getWidth() / 4;
        return preferredSize;
    }
}
