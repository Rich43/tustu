package m;

import am.C0577e;
import bH.W;
import com.efiAnalytics.ui.C1642e;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

/* loaded from: TunerStudioMS.jar:m/d.class */
public class d extends JPanel {

    /* renamed from: b, reason: collision with root package name */
    List f12913b;

    /* renamed from: d, reason: collision with root package name */
    private List f12911d = new ArrayList();

    /* renamed from: a, reason: collision with root package name */
    List f12912a = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    String f12914c = "<html>Click 'Select and Open' to open all Data Groups or select a subset. <br>Note: By selecting all Data Groups, the Data Rate may be lowered for <br>      higher speed Data Groups.";

    public d(List list) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLoweredSoftBevelBorder());
        this.f12913b = list;
        add("North", new JLabel(this.f12914c, 0));
        Font font = new Font(getFont().getFontName(), 1, getFont().getSize() + 1);
        JPanel jPanel = new JPanel();
        jPanel.setBorder(BorderFactory.createEmptyBorder(0, eJ.a(10), 0, eJ.a(10)));
        jPanel.setLayout(new GridLayout(0, 1));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        Component jLabel = new JLabel("Group");
        jLabel.setPreferredSize(new Dimension(eJ.a(80), 10));
        jLabel.setFont(font);
        jPanel2.add("West", jLabel);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new GridLayout(1, 0));
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new GridLayout(1, 0));
        JLabel jLabel2 = new JLabel("Channel Count");
        jLabel2.setFont(font);
        jPanel4.add(jLabel2);
        JLabel jLabel3 = new JLabel("Field Count", 0);
        jLabel3.setFont(font);
        jPanel4.add(jLabel3);
        JLabel jLabel4 = new JLabel("Data Rate", 0);
        jLabel4.setFont(font);
        jPanel4.add(jLabel4);
        jPanel3.add(jPanel4);
        JLabel jLabel5 = new JLabel("Fields", 0);
        jLabel5.setFont(font);
        jPanel3.add(jLabel5);
        jPanel2.add(BorderLayout.CENTER, jPanel3);
        JButton jButton = new JButton("Select");
        jButton.addActionListener(new e(this, jButton));
        jPanel2.add("East", jButton);
        jLabel5.setFont(font);
        jPanel.add(jPanel2);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            jPanel.add(a((C0577e) it.next()));
        }
        add(BorderLayout.CENTER, new JScrollPane(jPanel));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(Component component, int i2, int i3) {
        JPopupMenu jPopupMenu = new JPopupMenu();
        jPopupMenu.add("Select All").addActionListener(new f(this));
        jPopupMenu.add("Select None").addActionListener(new g(this));
        add(jPopupMenu);
        jPopupMenu.show(component, i2, i3);
    }

    private JPanel a(C0577e c0577e) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        Component jLabel = new JLabel("Data Group " + c0577e.h());
        jLabel.setPreferredSize(new Dimension(eJ.a(80), 10));
        jPanel.add("West", jLabel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(1, 0));
        j jVar = new j(this, c0577e);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new GridLayout(1, 0));
        jPanel3.add(new JLabel(Integer.toString(jVar.c()), 0));
        jPanel3.add(new JLabel(Long.toString(jVar.b()), 0));
        jPanel3.add(new JLabel(W.c(jVar.a(), 2), 0));
        jPanel2.add(jPanel3);
        C1642e c1642e = new C1642e();
        Iterator it = jVar.f12926d.iterator();
        while (it.hasNext()) {
            c1642e.a((String) it.next());
        }
        jPanel2.add(c1642e);
        jPanel.add(BorderLayout.CENTER, jPanel2);
        JCheckBox jCheckBox = new JCheckBox("load ", true);
        jCheckBox.setActionCommand(Integer.toString(c0577e.h()));
        this.f12911d.add(Integer.valueOf(c0577e.h()));
        this.f12912a.add(jCheckBox);
        jCheckBox.addActionListener(new h(this));
        jPanel.add("East", jCheckBox);
        return jPanel;
    }

    public static List a(Window window, List list, c cVar) {
        d dVar = new d(list);
        JDialog jDialogA = bV.a(dVar, window, "Data Group Selector", new i(dVar, list, cVar), "Select and Open");
        jDialogA.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        jDialogA.pack();
        if (jDialogA.getHeight() > window.getHeight() * 0.7d) {
            jDialogA.setSize(jDialogA.getWidth() + eJ.a(25), (int) (window.getHeight() * 0.7d));
        } else {
            jDialogA.setSize(jDialogA.getWidth() + eJ.a(25), jDialogA.getHeight());
        }
        bV.a(window, (Component) jDialogA);
        jDialogA.setVisible(true);
        return list;
    }

    public List a() {
        return this.f12911d;
    }
}
