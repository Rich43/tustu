package ao;

import com.efiAnalytics.ui.C1562b;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* loaded from: TunerStudioMS.jar:ao/gH.class */
class gH extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    JLabel f5911a = new JLabel("");

    /* renamed from: b, reason: collision with root package name */
    JLabel f5912b = new JLabel("");

    /* renamed from: c, reason: collision with root package name */
    JLabel f5913c = new JLabel("");

    /* renamed from: d, reason: collision with root package name */
    JLabel f5914d = new JLabel("");

    /* renamed from: e, reason: collision with root package name */
    JLabel f5915e = new JLabel("");

    /* renamed from: f, reason: collision with root package name */
    JLabel f5916f = new JLabel("");

    /* renamed from: g, reason: collision with root package name */
    JLabel f5917g = new JLabel("");

    /* renamed from: h, reason: collision with root package name */
    JLabel f5918h = new JLabel("");

    /* renamed from: i, reason: collision with root package name */
    JLabel f5919i = new JLabel("");

    /* renamed from: j, reason: collision with root package name */
    Dimension f5920j = new Dimension(70, 20);

    /* renamed from: k, reason: collision with root package name */
    final /* synthetic */ fX f5921k;

    gH(fX fXVar) {
        this.f5921k = fXVar;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Data Details"));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, 1));
        add(jPanel, "North");
        jPanel.add(a("Total Records:", this.f5912b));
        jPanel.add(a("Filtered Records:", this.f5911a));
        jPanel.add(new JLabel(" "));
        jPanel.add(a("Cell Hit Count:", this.f5914d));
        jPanel.add(a("Cell Weight:", this.f5913c));
        jPanel.add(a("Variance:", this.f5915e));
        jPanel.add(a("Standard Dev:", this.f5916f));
        jPanel.add(a("Minimum:", this.f5918h));
        jPanel.add(a("Mean:", this.f5917g));
        jPanel.add(a("Max:", this.f5919i));
    }

    public void a() {
        int[] selectedColumns = this.f5921k.f5736l.h().getSelectedColumns();
        int[] selectedRows = this.f5921k.f5736l.h().getSelectedRows();
        if (selectedColumns.length <= 0 || selectedRows.length <= 0) {
            this.f5913c.setText("");
            this.f5914d.setText("");
            this.f5915e.setText("");
            this.f5916f.setText("");
            this.f5917g.setText("");
            this.f5918h.setText("");
            this.f5919i.setText("");
            return;
        }
        int iJ = 0;
        double dK = 0.0d;
        double dE = 0.0d;
        double dC = 0.0d;
        double d2 = 0.0d;
        double dDoubleValue = Double.NaN;
        double dDoubleValue2 = Double.NaN;
        for (int i2 : selectedColumns) {
            for (int i3 : selectedRows) {
                C1562b[][] c1562bArrD = this.f5921k.b().D();
                C1562b c1562b = c1562bArrD[(c1562bArrD.length - i3) - 1][i2];
                iJ += c1562b.j();
                dK += c1562b.k();
                dE += c1562b.e() * c1562b.k();
                dC = c1562b.c() * c1562b.k();
                d2 = c1562b.d() * c1562b.k();
                if (c1562b.f() != null && (Double.isNaN(dDoubleValue) || c1562b.f().doubleValue() > dDoubleValue)) {
                    dDoubleValue = c1562b.f().doubleValue();
                }
                if (c1562b.g() != null && (Double.isNaN(dDoubleValue2) || c1562b.g().doubleValue() < dDoubleValue2)) {
                    dDoubleValue2 = c1562b.g().doubleValue();
                }
            }
        }
        this.f5913c.setText(bH.W.b(dK, 2));
        this.f5914d.setText("" + iJ);
        this.f5915e.setText(bH.W.b(d2 / dK, this.f5921k.f5736l.h().b()));
        this.f5916f.setText(bH.W.b(dC / dK, this.f5921k.f5736l.h().b()));
        this.f5917g.setText(bH.W.b(dE / dK, this.f5921k.f5736l.h().b()));
        this.f5918h.setText(bH.W.b(dDoubleValue2, this.f5921k.f5736l.h().b()));
        this.f5919i.setText(bH.W.b(dDoubleValue, this.f5921k.f5736l.h().b()));
    }

    private JPanel a(String str, Component component) {
        JLabel jLabel = new JLabel(str, 4);
        jLabel.setMinimumSize(this.f5920j);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(3, 3));
        jPanel.add(component, BorderLayout.CENTER);
        jPanel.add(jLabel, "West");
        return jPanel;
    }

    public void a(String str) throws IllegalArgumentException {
        this.f5912b.setText(str);
    }

    public void b(String str) throws IllegalArgumentException {
        this.f5911a.setText(str);
    }
}
