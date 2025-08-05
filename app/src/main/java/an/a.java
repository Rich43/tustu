package aN;

import W.ap;
import bH.C0995c;
import bH.W;
import com.efiAnalytics.ui.bV;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: TunerStudioMS.jar:aN/a.class */
public class a extends JDialog {

    /* renamed from: a, reason: collision with root package name */
    JTextPane f2627a;

    /* renamed from: b, reason: collision with root package name */
    JTextPane f2628b;

    /* renamed from: c, reason: collision with root package name */
    JTextField f2629c;

    /* renamed from: d, reason: collision with root package name */
    JTextField f2630d;

    /* renamed from: e, reason: collision with root package name */
    JLabel f2631e;

    /* renamed from: f, reason: collision with root package name */
    JButton f2632f;

    /* renamed from: g, reason: collision with root package name */
    ap f2633g;

    public a(Frame frame, ap apVar) {
        super(frame);
        this.f2629c = new JTextField();
        this.f2630d = new JTextField();
        this.f2631e = new JLabel("<html> <br> </html>");
        this.f2633g = null;
        setTitle("Binary File Difference");
        super.setDefaultCloseOperation(2);
        this.f2633g = apVar;
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(5, 5));
        jPanel.setBorder(BorderFactory.createTitledBorder("Binary Difference Report"));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout(5, 5));
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new GridLayout(0, 1, 4, 4));
        jPanel3.add(a(this.f2629c, "File 1:"));
        jPanel3.add(a(this.f2630d, "File 2:"));
        jPanel2.add("North", jPanel3);
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new BorderLayout());
        jPanel4.add("North", new JLabel("Different Addresses:"));
        this.f2628b = new JTextPane();
        this.f2628b.setEditable(false);
        this.f2628b.setPreferredSize(new Dimension(300, 100));
        jPanel4.add(BorderLayout.CENTER, new JScrollPane(this.f2628b));
        JPanel jPanel5 = new JPanel();
        jPanel5.setLayout(new BorderLayout(5, 5));
        this.f2632f = new JButton("Perform Difference");
        this.f2632f.addActionListener(new b(this));
        jPanel5.add("East", this.f2632f);
        jPanel5.add(BorderLayout.CENTER, this.f2631e);
        this.f2631e.setFont(new Font(Font.DIALOG_INPUT, 0, 13));
        jPanel2.add("South", jPanel5);
        jPanel.add("North", jPanel2);
        JPanel jPanel6 = new JPanel();
        jPanel6.setLayout(new GridLayout(1, 1, 5, 5));
        this.f2627a = new JTextPane();
        this.f2627a.setEditable(false);
        JScrollPane jScrollPane = new JScrollPane(this.f2627a);
        this.f2627a.setFont(new Font(Font.DIALOG_INPUT, 1, 13));
        jScrollPane.setMinimumSize(new Dimension(400, 500));
        jScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        jPanel6.add(jScrollPane);
        jPanel.add(BorderLayout.CENTER, jPanel6);
        add(BorderLayout.CENTER, jPanel);
        this.f2627a.addCaretListener(new c(this));
        b();
    }

    private JPanel a(JTextField jTextField, String str) {
        JPanel jPanel = new JPanel();
        jTextField.setBorder(BorderFactory.createLoweredBevelBorder());
        jPanel.setLayout(new BorderLayout(5, 5));
        jPanel.add("West", new JLabel(str));
        jPanel.add(BorderLayout.CENTER, jTextField);
        JButton jButton = new JButton("Select File");
        jPanel.add("East", jButton);
        jButton.addActionListener(new d(this, jTextField));
        return jPanel;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() throws IllegalArgumentException {
        String strB = b(this.f2627a.getSelectedText());
        String strB2 = b(c(this.f2627a.getSelectedText()));
        if (strB.length() <= 0) {
            this.f2631e.setText("");
            return;
        }
        BigInteger bigInteger = new BigInteger(strB, 16);
        BigInteger bigInteger2 = new BigInteger(strB2, 16);
        this.f2631e.setText("<html>Big endian Decimal: " + bigInteger.toString(10) + ", Bin: " + bigInteger.toString(2) + "<br>Lit endian Decimal: " + bigInteger2.toString(10) + ", Bin: " + bigInteger2.toString(2) + "</html>");
    }

    private String b(String str) {
        return W.b(W.b(W.b(W.b(W.b(W.b(str, "x0", ""), LanguageTag.PRIVATEUSE, ""), PdfOps.DOUBLE_QUOTE__TOKEN, ""), "\n", ""), "\t", ""), " ", "");
    }

    private String c(String str) {
        String[] strArrSplit = str.split(LanguageTag.PRIVATEUSE);
        StringBuilder sb = new StringBuilder();
        for (int length = strArrSplit.length - 1; length >= 0; length--) {
            sb.append(strArrSplit[length]);
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str, String str2) {
        if (this.f2633g != null) {
            this.f2633g.a(str, str2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String b(String str, String str2) {
        return this.f2633g != null ? this.f2633g.b(str, str2) : str2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        if (new File(this.f2629c.getText()).exists() && new File(this.f2630d.getText()).exists()) {
            this.f2632f.setEnabled(true);
        } else {
            this.f2632f.setEnabled(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        try {
            try {
                this.f2627a.setText(C0995c.a(a(this.f2629c.getText()), a(this.f2630d.getText())));
                this.f2627a.setCaretPosition(0);
            } catch (FileNotFoundException e2) {
                bV.d("File not found:\n" + this.f2629c.getText(), this);
            } catch (IOException e3) {
                bV.d("Error loading file '" + e3.getLocalizedMessage() + "':\n" + this.f2629c.getText(), this);
            }
        } catch (FileNotFoundException e4) {
            bV.d("File not found:\n" + this.f2629c.getText(), this);
        } catch (IOException e5) {
            bV.d("Error loading file '" + e5.getLocalizedMessage() + "':\n" + this.f2629c.getText(), this);
        }
    }

    protected int[] a(String str) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(new File(str)));
        ArrayList arrayList = new ArrayList();
        int i2 = bufferedInputStream.read();
        while (true) {
            int i3 = i2;
            if (i3 == -1) {
                break;
            }
            arrayList.add(Integer.valueOf(i3));
            i2 = bufferedInputStream.read();
        }
        int[] iArr = new int[arrayList.size()];
        for (int i4 = 0; i4 < iArr.length; i4++) {
            iArr[i4] = ((Integer) arrayList.get(i4)).intValue();
        }
        return iArr;
    }
}
