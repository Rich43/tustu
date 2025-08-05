package ao;

import W.C0188n;
import ax.C0918u;
import bH.C1011s;
import g.C1733k;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import k.C1753a;
import k.C1755c;
import k.C1756d;
import org.apache.commons.math3.geometry.VectorFormat;

/* renamed from: ao.r, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/r.class */
public class C0820r extends JDialog implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    boolean f6161a;

    /* renamed from: b, reason: collision with root package name */
    JButton f6162b;

    /* renamed from: c, reason: collision with root package name */
    JButton f6163c;

    /* renamed from: d, reason: collision with root package name */
    JTextField f6164d;

    /* renamed from: e, reason: collision with root package name */
    JTextField f6165e;

    /* renamed from: f, reason: collision with root package name */
    Frame f6166f;

    public C0820r(Frame frame, String str, String str2) {
        super(frame, "Custom Field", true);
        this.f6161a = false;
        this.f6166f = frame;
        setLayout(new BorderLayout(15, 15));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(0, 1));
        jPanel.add(new JLabel(" ", 0));
        jPanel.add(new JLabel("Define a custom field.", 0));
        jPanel.add(new JLabel("This field will be calculated at log load time like all calculated fields", 0));
        JLabel jLabel = new JLabel("For syntax help, read Math Parser help in Help Topics for more information.", 0);
        Color color = UIManager.getColor("Label.background");
        if (color == null || color.getRed() + color.getGreen() + color.getBlue() > 192) {
            jLabel.setForeground(Color.BLUE.darker());
        } else {
            jLabel.setForeground(Color.CYAN);
        }
        jLabel.setCursor(new Cursor(12));
        jLabel.addMouseListener(new C0821s(this));
        jPanel.add(jLabel);
        add(jPanel, "North");
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(0, 1));
        add(BorderLayout.CENTER, jPanel2);
        jPanel2.add(new JLabel("Field Name:", 2));
        this.f6164d = new JTextField(str);
        this.f6164d.setBorder(BorderFactory.createLoweredBevelBorder());
        this.f6164d.setEnabled(str.equals(""));
        jPanel2.add(this.f6164d);
        jPanel2.add(new JLabel(" "));
        jPanel2.add(new JLabel("Formula:  (Press Ctrl+Space to insert log field names)", 2));
        this.f6165e = new JTextField(str2);
        this.f6165e.setBorder(BorderFactory.createLoweredBevelBorder());
        this.f6165e.addActionListener(new C0822t(this));
        hz.a(this.f6165e, "control SPACE");
        jPanel2.add(this.f6165e);
        add(new JLabel(" "), "West");
        add(new JLabel(" "), "East");
        a(true);
        pack();
        setSize(getWidth() + 50, getHeight());
        Dimension size = frame.getSize();
        Dimension size2 = getSize();
        Point location = frame.getLocation();
        setLocation((int) (location.getX() + ((size.getWidth() - size2.getWidth()) / 2.0d)), (int) (location.getY() + ((size.getHeight() - size2.getHeight()) / 2.0d)));
        setVisible(true);
    }

    protected void a(boolean z2) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout());
        a(jPanel);
        if (z2) {
            b(jPanel);
        }
        add("South", jPanel);
    }

    protected void a(JPanel jPanel) {
        JButton jButton = new JButton("OK");
        this.f6162b = jButton;
        jPanel.add(jButton);
        this.f6162b.addActionListener(this);
    }

    protected void b(JPanel jPanel) {
        JButton jButton = new JButton("Cancel");
        this.f6163c = jButton;
        jPanel.add(jButton);
        this.f6163c.addActionListener(this);
    }

    public boolean a() {
        return this.f6161a;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.f6162b && e()) {
            this.f6161a = true;
            setVisible(false);
        } else if (actionEvent.getSource() == this.f6163c) {
            setVisible(false);
        }
    }

    private boolean e() throws ax.U {
        if (this.f6164d.getText().equals("")) {
            C1733k.a("You must supply a Field Name", getParent());
            return false;
        }
        for (int i2 = 0; i2 < "()*&%^|+-/><=_[]".length(); i2++) {
            if (this.f6164d.getText().indexOf("()*&%^|+-/><=_[]".charAt(i2)) != -1) {
                C1733k.a("The formula Name can not contain any of the following special characters:\n()*&%^|+-/><=_[]", getParent());
                return false;
            }
        }
        try {
            if (this.f6165e.getText().trim().equals("")) {
                C1733k.a("Formula can not be empty\nPlease correct.", getParent());
                return false;
            }
            C0188n c0188nR = C0804hg.a().r();
            if (c0188nR != null) {
                String text = this.f6165e.getText();
                while (text.indexOf(VectorFormat.DEFAULT_PREFIX) > 0) {
                    text = text.substring(0, text.indexOf(VectorFormat.DEFAULT_PREFIX)) + "1.0" + text.substring(text.indexOf("}", text.indexOf(VectorFormat.DEFAULT_PREFIX)) + 1);
                }
                String strC = h.g.a().c(text);
                try {
                    try {
                        C1753a c1753aA = C1756d.a().a(strC);
                        for (String str : c1753aA.b()) {
                            if (!strC.contains("[" + str)) {
                                throw new ax.U("Square brackets [] must surround field names.");
                            }
                        }
                        c1753aA.a(c0188nR, 20);
                    } catch (C0918u e2) {
                    } catch (C1755c e3) {
                        if (C1733k.a("The inc file used in this expression was not found.\n" + e3.a().getName() + "\n\nWould you like to browse to it and add it to the inc dir located at:\n" + ((Object) h.h.c()), (Component) getParent(), true)) {
                            a(e3.a());
                            return false;
                        }
                    }
                } catch (ax.U e4) {
                    return C1733k.a("Failed to validate formula:\n" + this.f6165e.getText() + "\nReason:\n" + e4.getMessage() + "\n\nSave formula anyway?", (Component) getParent(), true);
                }
            } else {
                C1733k.a("Can not validate formula while no log file is loaded.\nThe formula will be assumed to be correct, and will be available after\nnext log file load if correct.", getParent());
            }
            return true;
        } catch (Exception e5) {
            C1733k.a("Invalid Formula:" + this.f6165e.getText() + "\nPlease check your syntax.", getParent());
            return false;
        }
    }

    private void a(File file) {
        String strA = com.efiAnalytics.ui.bV.a((Component) getParent(), "Browse to inc file", new String[]{"inc"}, "*.inc", h.h.d(), true);
        if (strA == null || strA.isEmpty()) {
            return;
        }
        try {
            C1011s.a(new File(strA), new File(h.h.c(), file.getName()));
        } catch (V.a e2) {
            C1733k.a("Unable to copy inc file!\nThe following error occurred:\n" + e2.getLocalizedMessage(), this);
            Logger.getLogger(C0820r.class.getName()).log(Level.SEVERE, "Failed to copy file", (Throwable) e2);
        }
    }

    public String b() {
        return this.f6164d.getText();
    }

    public String c() {
        return this.f6165e.getText();
    }

    protected void d() {
        C0649bm c0649bmA = C0804hg.a().A();
        c0649bmA.a(C0648bl.a(C0648bl.f5418d));
        c0649bmA.a(this, h.i.f12255b + " help");
    }
}
