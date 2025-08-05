package g;

import G.C0126i;
import W.C0184j;
import W.C0188n;
import ax.U;
import bH.R;
import com.efiAnalytics.ui.C1621de;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import k.C1756d;
import org.apache.commons.math3.geometry.VectorFormat;
import org.apache.commons.net.ftp.FTPReply;

/* renamed from: g.g, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:g/g.class */
public class C1729g extends JDialog implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    public boolean f12195a;

    /* renamed from: b, reason: collision with root package name */
    JButton f12196b;

    /* renamed from: c, reason: collision with root package name */
    JButton f12197c;

    /* renamed from: d, reason: collision with root package name */
    String f12198d;

    /* renamed from: e, reason: collision with root package name */
    ArrayList f12199e;

    /* renamed from: f, reason: collision with root package name */
    ArrayList f12200f;

    /* renamed from: g, reason: collision with root package name */
    Frame f12201g;

    /* renamed from: h, reason: collision with root package name */
    boolean f12202h;

    /* renamed from: n, reason: collision with root package name */
    private InterfaceC1735m f12203n;

    /* renamed from: i, reason: collision with root package name */
    KeyListener f12204i;

    /* renamed from: j, reason: collision with root package name */
    Set f12205j;

    /* renamed from: k, reason: collision with root package name */
    String f12207k;

    /* renamed from: l, reason: collision with root package name */
    C0188n f12208l;

    /* renamed from: o, reason: collision with root package name */
    private static String f12206o = " ";

    /* renamed from: m, reason: collision with root package name */
    public static String f12209m = "+-/=&<>*^!,% [{()}]|\n\t?:";

    public C1729g(Frame frame, String str, boolean z2, String str2, boolean z3) {
        this(frame, null, str, z2, str2, z3, null, null);
    }

    public C1729g(Frame frame, String str, String str2, boolean z2, String str3, boolean z3, InterfaceC1735m interfaceC1735m, C0188n c0188n) {
        super(frame, "User Parameter", true);
        this.f12195a = false;
        this.f12198d = null;
        this.f12199e = new ArrayList();
        this.f12200f = new ArrayList();
        this.f12201g = null;
        this.f12202h = true;
        this.f12203n = null;
        this.f12204i = null;
        this.f12205j = null;
        this.f12207k = null;
        this.f12202h = z2;
        this.f12203n = interfaceC1735m;
        this.f12207k = str;
        this.f12208l = c0188n;
        setLayout(new BorderLayout());
        this.f12204i = new C1730h(this);
        addKeyListener(this.f12204i);
        this.f12198d = str2;
        this.f12201g = frame;
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(0, 1, eJ.a(2), eJ.a(2)));
        add(BorderLayout.CENTER, jPanel);
        str3 = str3 == null ? "                                   User defined values required for this formula." : str3;
        boolean z4 = false;
        if (str2.contains(VectorFormat.DEFAULT_PREFIX)) {
            StringTokenizer stringTokenizer = new StringTokenizer(str3, "\n");
            while (stringTokenizer.hasMoreElements()) {
                Component jLabel = new JLabel(stringTokenizer.nextToken());
                jPanel.add(jLabel);
                Font font = jLabel.getFont();
                jLabel.setFont(new Font(font.getFamily(), 1, font.getSize()));
            }
            jPanel.add(new JLabel(""));
            int i2 = 0;
            while (true) {
                int iIndexOf = str2.indexOf(VectorFormat.DEFAULT_PREFIX, i2);
                if (iIndexOf == -1) {
                    break;
                }
                JPanel jPanel2 = new JPanel();
                int iA = eJ.a(5);
                jPanel2.setLayout(new GridLayout(1, 2, iA, iA));
                int iIndexOf2 = str2.indexOf("}", iIndexOf);
                i2 = iIndexOf2;
                String strSubstring = str2.substring(iIndexOf + 1, iIndexOf2);
                if (!strSubstring.equals("") && !this.f12199e.contains(strSubstring)) {
                    JTextField jTextField = new JTextField();
                    jTextField.setBorder(BorderFactory.createLoweredBevelBorder());
                    jTextField.addKeyListener(this.f12204i);
                    if (strSubstring.contains("Field Name")) {
                        jTextField.setName("Field");
                    }
                    this.f12199e.add(strSubstring);
                    this.f12200f.add(jTextField);
                    jTextField.setText(h.i.e("userParameter_" + strSubstring, ""));
                    if (jTextField.getText().length() > 80) {
                        jTextField.setPreferredSize(eJ.a(FTPReply.FILE_ACTION_PENDING, 20));
                        jTextField.setCaretPosition(0);
                    }
                    jPanel2.add(new JLabel(strSubstring, 4));
                    jPanel2.add(jTextField);
                    jPanel.add(jPanel2);
                    z4 = true;
                }
            }
        }
        this.f12205j = a(str2);
        if (!z4 && this.f12205j.isEmpty()) {
            this.f12195a = true;
            dispose();
            return;
        }
        if (c0188n != null && this.f12205j != null && !this.f12205j.isEmpty()) {
            jPanel.add(new JLabel(" "));
            JPanel jPanel3 = new JPanel();
            jPanel3.setLayout(new GridLayout(1, 0));
            JLabel jLabel2 = new JLabel("Required Field Mappings", 0);
            jPanel3.add(jLabel2);
            Font font2 = jLabel2.getFont();
            jLabel2.setFont(new Font(font2.getFamily(), 1, font2.getSize()));
            jPanel.add(jPanel3);
            JPanel jPanel4 = new JPanel();
            jPanel4.setLayout(new GridLayout(1, 0));
            jPanel4.add(new JLabel("Logical Name", 0));
            jPanel4.add(new JLabel("Default Field", 0));
            jPanel4.add(new JLabel("Selected Input Field", 0));
            jPanel.add(jPanel4);
            String[] strArrA = new String[c0188n.size()];
            for (int i3 = 0; i3 < c0188n.size(); i3++) {
                strArrA[i3] = ((C0184j) c0188n.get(i3)).a();
            }
            strArrA = h.i.a(h.i.f12284E, h.i.f12285F) ? R.a(strArrA) : strArrA;
            for (C1732j c1732j : this.f12205j) {
                JPanel jPanel5 = new JPanel();
                jPanel5.setLayout(new GridLayout(1, 0, eJ.a(4), eJ.a(4)));
                JTextField jTextField2 = new JTextField();
                jTextField2.setEditable(false);
                jTextField2.setText(c1732j.a());
                jPanel5.add(jTextField2);
                JTextField jTextField3 = new JTextField();
                jTextField3.setEditable(false);
                jTextField3.setText(c1732j.b());
                jPanel5.add(jTextField3);
                C1621de c1621de = new C1621de();
                c1621de.setName(c1732j.a());
                c1621de.a(false);
                c1621de.a(f12206o);
                for (String str4 : strArrA) {
                    c1621de.a(str4);
                }
                c1621de.addActionListener(new C1731i(this, c1621de, c1732j));
                if (c1621de.c(c1732j.b())) {
                    c1621de.setSelectedItem(c1732j.b());
                } else {
                    c1621de.setSelectedItem(f12206o);
                }
                jPanel5.add(c1621de);
                jPanel.add(jPanel5);
            }
        }
        add(new JLabel(" "), "West");
        add(new JLabel(" "), "East");
        add(new JLabel(" "), "North");
        a(z3);
        pack();
        Dimension size = frame.getSize();
        Dimension size2 = getSize();
        Point location = frame.getLocation();
        setLocation((int) (location.getX() + ((size.getWidth() - size2.getWidth()) / 2.0d)), (int) (location.getY() + ((size.getHeight() - size2.getHeight()) / 2.0d)));
        setVisible(true);
    }

    private Set a(String str) {
        while (str.contains(VectorFormat.DEFAULT_PREFIX)) {
            str = str.substring(0, str.indexOf(VectorFormat.DEFAULT_PREFIX)) + "1" + str.substring(str.indexOf("}") + 1);
        }
        HashSet hashSet = new HashSet();
        try {
            for (String str2 : C1756d.a().a(str).b()) {
                if ((this.f12207k == null || !this.f12207k.equals(str2)) && !a(hashSet, str2)) {
                    C1732j c1732j = new C1732j(this);
                    c1732j.a(str2);
                    if (str2.startsWith("Field.")) {
                        c1732j.b(h.g.a().a(str2));
                        c1732j.c(h.g.a().a(str2));
                    } else {
                        c1732j.b(str2);
                        c1732j.c(str2);
                    }
                    hashSet.add(c1732j);
                }
            }
        } catch (U e2) {
            e2.printStackTrace();
        }
        return hashSet;
    }

    private boolean a(Set set, String str) {
        Iterator it = set.iterator();
        while (it.hasNext()) {
            if (((C1732j) it.next()).a().equals(str)) {
                return true;
            }
        }
        return false;
    }

    public String a() {
        if (!this.f12195a) {
            return "";
        }
        String strA = this.f12198d;
        for (int i2 = 0; i2 < this.f12199e.size(); i2++) {
            String str = (String) this.f12199e.get(i2);
            JTextField jTextField = (JTextField) this.f12200f.get(i2);
            strA = C1733k.a(strA, VectorFormat.DEFAULT_PREFIX + str + "}", jTextField.getText());
            h.i.c("userParameter_" + str, jTextField.getText());
        }
        if (this.f12205j != null) {
            for (C1732j c1732j : this.f12205j) {
                if (!c1732j.b().equals(c1732j.c())) {
                    strA = a(strA, c1732j.a(), c1732j.c());
                }
            }
        }
        return strA;
    }

    private String a(String str, String str2, String str3) {
        byte[] bytes = str.getBytes();
        int length = 0;
        while (bytes.length > length && f12209m.indexOf((char) bytes[length]) != -1) {
            length++;
        }
        int i2 = length + 1;
        while (i2 <= bytes.length) {
            if ((i2 == bytes.length && length < i2 - 1) || (i2 != bytes.length && f12209m.indexOf((char) bytes[i2]) != -1)) {
                String strTrim = str.substring(length, i2).trim();
                if (strTrim.length() <= 1 || C0126i.b(strTrim) || C0126i.c(strTrim)) {
                    length = i2 + 1;
                } else if (strTrim.equals(str2)) {
                    str = str.substring(0, length) + str3 + str.substring(i2);
                    length = length + str3.length() + 1;
                    i2 = length;
                    bytes = str.getBytes();
                } else {
                    length = i2 + 1;
                }
            }
            i2++;
        }
        return str;
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
        this.f12196b = jButton;
        jPanel.add(jButton);
        this.f12196b.addActionListener(this);
    }

    protected void b(JPanel jPanel) {
        JButton jButton = new JButton("Cancel");
        this.f12197c = jButton;
        jPanel.add(jButton);
        this.f12197c.addActionListener(this);
    }

    protected void b() {
        this.f12195a = false;
        dispose();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        if (c()) {
            this.f12195a = true;
            dispose();
        }
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == this.f12196b) {
            d();
        }
        if (actionEvent.getSource() == this.f12197c) {
            b();
        }
    }

    protected boolean c() {
        Iterator it = this.f12200f.iterator();
        while (it.hasNext()) {
            JTextField jTextField = (JTextField) it.next();
            if (this.f12203n != null) {
                if (!this.f12203n.a(jTextField.getText())) {
                    return false;
                }
            } else if (this.f12202h && (jTextField.getName() == null || !jTextField.getName().equals("Field"))) {
                try {
                    Double.parseDouble(jTextField.getText());
                } catch (Exception e2) {
                    C1733k.a("Values Must Be Numeric", this.f12201g);
                    return false;
                }
            } else if (jTextField.equals("")) {
                C1733k.a("You must enter a value.", this.f12201g);
            }
        }
        return true;
    }
}
