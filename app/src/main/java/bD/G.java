package bD;

import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/* loaded from: TunerStudioMS.jar:bD/G.class */
public class G extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    C0957c f6622a;

    /* renamed from: b, reason: collision with root package name */
    F f6623b;

    /* renamed from: c, reason: collision with root package name */
    H f6624c = null;

    /* renamed from: d, reason: collision with root package name */
    Image f6625d = null;

    /* renamed from: e, reason: collision with root package name */
    Image f6626e = null;

    /* renamed from: f, reason: collision with root package name */
    Image f6627f = null;

    /* renamed from: g, reason: collision with root package name */
    Font f6628g = new Font("Ariel", 1, eJ.a(13));

    /* renamed from: h, reason: collision with root package name */
    private String f6629h = null;

    public G() {
        this.f6622a = null;
        this.f6623b = null;
        this.f6623b = new F();
        setLayout(new BorderLayout());
        this.f6622a = new C0957c(this.f6623b);
        JScrollPane jScrollPane = new JScrollPane(this.f6622a);
        add(BorderLayout.CENTER, jScrollPane);
        jScrollPane.getViewport().setBackground(Color.white);
        setPreferredSize(eJ.a(600, 300));
        setMinimumSize(eJ.a(450, 200));
    }

    public void a(boolean z2) {
        this.f6622a.a(z2);
    }

    public List a() {
        ArrayList arrayList = new ArrayList();
        int[] selectedRows = this.f6622a.getSelectedRows();
        if (selectedRows != null && selectedRows.length > 0) {
            for (int i2 = 0; i2 < selectedRows.length; i2++) {
                selectedRows[i2] = this.f6622a.convertRowIndexToModel(selectedRows[i2]);
            }
            for (int i3 : selectedRows) {
                arrayList.add(this.f6623b.a(i3));
            }
        }
        return arrayList;
    }

    protected C0957c b() {
        return this.f6622a;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        try {
            super.paint(graphics);
            if (this.f6624c != null) {
                Image imageG = g();
                Image imageF = f();
                int width = (getWidth() - imageF.getWidth(null)) / 2;
                int height = (getHeight() - imageF.getHeight(null)) / 2;
                Graphics graphics2 = imageF.getGraphics();
                graphics2.setColor(Color.white);
                graphics2.fill3DRect(0, 0, imageF.getWidth(null) - 1, imageF.getHeight(null) - 1, true);
                graphics2.drawImage(imageG, eJ.a(10), eJ.a(10), null);
                graphics.drawImage(imageF, width, height, null);
                if (e() != null && e().length() > 0) {
                    Image imageB = b(e());
                    graphics.drawImage(imageB, (getWidth() - imageB.getWidth(null)) / 2, height + imageF.getHeight(null) + eJ.a(5), null);
                }
            }
        } catch (Exception e2) {
        }
    }

    private Image f() {
        Image imageG = g();
        if (this.f6626e == null || this.f6626e.getHeight(null) < imageG.getHeight(null)) {
            this.f6626e = super.createImage(imageG.getWidth(null) + eJ.a(20), imageG.getHeight(null) + eJ.a(20));
        }
        return this.f6626e;
    }

    private Image b(String str) {
        String[] strArrSplit = str.split("\n");
        FontMetrics fontMetrics = getFontMetrics(this.f6628g);
        int iA = eJ.a(16);
        int length = (strArrSplit.length * fontMetrics.getHeight()) + iA;
        int iA2 = a(strArrSplit, fontMetrics) + (2 * iA);
        if (this.f6627f == null || this.f6627f.getHeight(null) != length || this.f6627f.getWidth(null) != iA2) {
            this.f6627f = super.createImage(iA2, length);
            Graphics graphics = this.f6627f.getGraphics();
            graphics.setColor(Color.white);
            graphics.fill3DRect(0, 0, this.f6627f.getWidth(null) - 1, this.f6627f.getHeight(null) - 1, true);
            graphics.setColor(Color.BLACK);
            graphics.setFont(this.f6628g);
            for (int i2 = 0; i2 < strArrSplit.length; i2++) {
                graphics.drawString(strArrSplit[i2], iA, (fontMetrics.getHeight() * (i2 + 1)) + (iA / 2));
            }
        }
        return this.f6627f;
    }

    private int a(String[] strArr, FontMetrics fontMetrics) {
        int i2 = 0;
        for (String str : strArr) {
            int iStringWidth = fontMetrics.stringWidth(str);
            if (iStringWidth > i2) {
                i2 = iStringWidth;
            }
        }
        return i2;
    }

    private Image g() {
        if (this.f6625d == null) {
            this.f6625d = Toolkit.getDefaultToolkit().getImage(getClass().getResource("wait-large.gif"));
        }
        return this.f6625d;
    }

    public void c() {
        d();
        this.f6624c = new H(this);
        this.f6624c.start();
    }

    public void d() {
        if (this.f6624c != null) {
            this.f6624c.f6630a = false;
            this.f6624c = null;
        }
        a((String) null);
    }

    public void a(List list) {
        this.f6623b.a();
        this.f6623b.a(list);
    }

    public String e() {
        return this.f6629h;
    }

    public void a(String str) {
        this.f6629h = str;
    }
}
