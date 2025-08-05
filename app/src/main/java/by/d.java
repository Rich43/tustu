package by;

import bH.W;
import bH.aa;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.Iterator;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.slf4j.Marker;
import sun.util.locale.LanguageTag;

/* loaded from: TunerStudioMS.jar:by/d.class */
public class d extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    m f9236a;

    /* renamed from: b, reason: collision with root package name */
    List f9237b;

    /* renamed from: c, reason: collision with root package name */
    aa f9238c;

    /* renamed from: d, reason: collision with root package name */
    JTextArea f9239d = new JTextArea("", 4, 40);

    public d(List list, m mVar, aa aaVar) {
        this.f9237b = list;
        this.f9236a = mVar;
        this.f9238c = aaVar;
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        this.f9239d.setBorder(BorderFactory.createBevelBorder(1));
        this.f9239d.setLineWrap(true);
        this.f9239d.setWrapStyleWord(true);
        jPanel.add(this.f9239d, BorderLayout.CENTER);
        this.f9239d.addMouseListener(new e(this));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new FlowLayout(0, eJ.a(1), eJ.a(1)));
        f fVar = new f(this);
        Dimension dimension = new Dimension(eJ.a(28), eJ.a(26));
        Insets insets = new Insets(0, 0, 0, 0);
        JButton jButton = new JButton(Marker.ANY_NON_NULL_MARKER);
        jButton.setActionCommand(Marker.ANY_NON_NULL_MARKER);
        jButton.setPreferredSize(dimension);
        jButton.setMargin(insets);
        jButton.addActionListener(fVar);
        jPanel2.add(jButton);
        JButton jButton2 = new JButton(LanguageTag.SEP);
        jButton2.setPreferredSize(dimension);
        jButton2.setMargin(insets);
        jButton2.setActionCommand(LanguageTag.SEP);
        jButton2.addActionListener(fVar);
        jPanel2.add(jButton2);
        JButton jButton3 = new JButton("/");
        jButton3.setPreferredSize(dimension);
        jButton3.setMargin(insets);
        jButton3.setActionCommand("/");
        jButton3.addActionListener(fVar);
        jPanel2.add(jButton3);
        JButton jButton4 = new JButton("*");
        jButton4.setPreferredSize(dimension);
        jButton4.setMargin(insets);
        jButton4.setActionCommand("*");
        jButton4.addActionListener(fVar);
        jPanel2.add(jButton4);
        JButton jButton5 = new JButton(FXMLLoader.RESOURCE_KEY_PREFIX);
        jButton5.setPreferredSize(dimension);
        jButton5.setMargin(insets);
        jButton5.setActionCommand(FXMLLoader.RESOURCE_KEY_PREFIX);
        jButton5.addActionListener(fVar);
        jPanel2.add(jButton5);
        JButton jButton6 = new JButton("&");
        jButton6.setPreferredSize(dimension);
        jButton6.setMargin(insets);
        jButton6.setActionCommand("&");
        jButton6.addActionListener(fVar);
        jPanel2.add(jButton6);
        JButton jButton7 = new JButton(CallSiteDescriptor.OPERATOR_DELIMITER);
        jButton7.setPreferredSize(dimension);
        jButton7.setMargin(insets);
        jButton7.setActionCommand(CallSiteDescriptor.OPERATOR_DELIMITER);
        jButton7.addActionListener(fVar);
        jPanel2.add(jButton7);
        JButton jButton8 = new JButton(">");
        jButton8.setPreferredSize(dimension);
        jButton8.setMargin(insets);
        jButton8.setActionCommand(">");
        jButton8.addActionListener(fVar);
        jPanel2.add(jButton8);
        JButton jButton9 = new JButton("<");
        jButton9.setPreferredSize(dimension);
        jButton9.setMargin(insets);
        jButton9.setActionCommand("<");
        jButton9.addActionListener(fVar);
        jPanel2.add(jButton9);
        JButton jButton10 = new JButton("!=");
        jButton10.setPreferredSize(dimension);
        jButton10.setMargin(insets);
        jButton10.setActionCommand("!=");
        jButton10.addActionListener(fVar);
        jPanel2.add(jButton10);
        JButton jButton11 = new JButton("Not");
        jButton11.setPreferredSize(dimension);
        jButton11.setMargin(insets);
        jButton11.setActionCommand("!");
        jButton11.addActionListener(fVar);
        jPanel2.add(jButton11);
        JButton jButton12 = new JButton("And");
        jButton12.setPreferredSize(dimension);
        jButton12.setMargin(insets);
        jButton12.setActionCommand("&&");
        jButton12.addActionListener(fVar);
        jPanel2.add(jButton12);
        JButton jButton13 = new JButton("Or");
        jButton13.setPreferredSize(dimension);
        jButton13.setMargin(insets);
        jButton13.setActionCommand("||");
        jButton13.addActionListener(fVar);
        jPanel2.add(jButton13);
        JButton jButton14 = new JButton("(");
        jButton14.setPreferredSize(dimension);
        jButton14.setMargin(insets);
        jButton14.setActionCommand("(");
        jButton14.addActionListener(fVar);
        jPanel2.add(jButton14);
        JButton jButton15 = new JButton(")");
        jButton15.setPreferredSize(dimension);
        jButton15.setMargin(insets);
        jButton15.setActionCommand(")");
        jButton15.addActionListener(fVar);
        jPanel2.add(jButton15);
        jPanel.add(jPanel2, "South");
        add(jPanel, "North");
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new GridLayout(1, 0, eJ.a(4), eJ.a(4)));
        Iterator it = list.iterator();
        while (it.hasNext()) {
            i iVar = (i) it.next();
            JPanel jPanel4 = new JPanel();
            jPanel4.setLayout(new GridLayout(1, 1));
            jPanel4.setBorder(BorderFactory.createTitledBorder(e(iVar.a())));
            List listB = iVar.b();
            JList jList = new JList(listB.toArray(new String[listB.size()]));
            JScrollPane jScrollPane = new JScrollPane(jList);
            jList.addMouseListener(new g(this));
            jScrollPane.setPreferredSize(new Dimension(eJ.a(180), eJ.a(150)));
            jPanel4.add(jScrollPane);
            jPanel3.add(jPanel4);
        }
        JPanel jPanel5 = new JPanel();
        jPanel5.setLayout(new GridLayout(1, 1));
        jPanel5.setBorder(BorderFactory.createTitledBorder(e("Functions")));
        List listA = mVar.a();
        JList jList2 = new JList(listA.toArray(new k[listA.size()]));
        JScrollPane jScrollPane2 = new JScrollPane(jList2);
        jList2.addMouseListener(new h(this));
        jScrollPane2.setPreferredSize(new Dimension(eJ.a(180), eJ.a(150)));
        jPanel5.add(jScrollPane2);
        jPanel3.add(jPanel5);
        add(jPanel3, BorderLayout.CENTER);
    }

    private String e(String str) {
        return this.f9238c != null ? this.f9238c.a(str) : str;
    }

    public void a(String str) {
        this.f9239d.append(" " + str + " ");
    }

    public void a() {
        String text = this.f9239d.getText();
        int caretPosition = this.f9239d.getCaretPosition();
        if (caretPosition <= 0 || this.f9239d.getSelectedText() != null || text.lastIndexOf("[", caretPosition) == -1 || text.indexOf("]", caretPosition) == -1) {
            return;
        }
        int iLastIndexOf = text.lastIndexOf("[", caretPosition);
        int iIndexOf = text.indexOf("[", iLastIndexOf + 1);
        int iIndexOf2 = text.indexOf("]", caretPosition) + 1;
        if (iLastIndexOf == -1 || iIndexOf2 == -1) {
            return;
        }
        if (iIndexOf == -1 || iIndexOf2 < iIndexOf) {
            this.f9239d.setSelectionStart(iLastIndexOf);
            this.f9239d.setSelectionEnd(iIndexOf2);
        }
    }

    public void b(String str) {
        if (this.f9239d.getSelectedText() == null || this.f9239d.getSelectedText().isEmpty()) {
            this.f9239d.insert(" " + str + " ", this.f9239d.getCaretPosition());
            return;
        }
        int selectionStart = this.f9239d.getSelectionStart();
        String text = this.f9239d.getText();
        this.f9239d.setText(text.substring(0, selectionStart) + " " + str + " " + text.substring(this.f9239d.getSelectionEnd(), text.length()));
    }

    public void c(String str) {
        b(str);
    }

    public void a(k kVar) {
        b(kVar.a());
    }

    public String b() {
        return W.b(this.f9239d.getText(), "\n", " ");
    }

    public void d(String str) {
        this.f9239d.setText(str);
    }
}
