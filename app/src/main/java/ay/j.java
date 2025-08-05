package aY;

import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import r.C1806i;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aY/j.class */
public class j extends JPanel {
    public j(String str) {
        a(str, null);
    }

    private void a(String str, ActionListener actionListener) {
        if (actionListener == null) {
            actionListener = new k(this);
        }
        setBorder(BorderFactory.createTitledBorder(C1818g.b("Data Rate")));
        setLayout(new GridLayout(0, 1));
        add(new JLabel(C1818g.b("Speed may be limited by device or cable.")));
        add(new JLabel(C1818g.b("i.e. Bluetooth cable may slow actual data rate") + "."));
        add(new JLabel(" "));
        ButtonGroup buttonGroup = new ButtonGroup();
        String strB = C1818g.b("Reads Per Second");
        JRadioButton jRadioButton = new JRadioButton("0 " + strB);
        jRadioButton.setActionCommand("0");
        jRadioButton.setSelected(str.equals("0"));
        jRadioButton.addActionListener(actionListener);
        add(jRadioButton);
        buttonGroup.add(jRadioButton);
        JRadioButton jRadioButton2 = new JRadioButton("1 " + strB);
        jRadioButton2.setActionCommand("1");
        jRadioButton2.setSelected(str.equals("1"));
        jRadioButton2.addActionListener(actionListener);
        add(jRadioButton2);
        buttonGroup.add(jRadioButton2);
        JRadioButton jRadioButton3 = new JRadioButton("5 " + strB);
        jRadioButton3.setActionCommand("5");
        jRadioButton3.setSelected(str.equals("5"));
        jRadioButton3.addActionListener(actionListener);
        add(jRadioButton3);
        buttonGroup.add(jRadioButton3);
        JRadioButton jRadioButton4 = new JRadioButton("10 " + strB);
        jRadioButton4.setActionCommand("10");
        jRadioButton4.setSelected(str.equals("10"));
        jRadioButton4.addActionListener(actionListener);
        add(jRadioButton4);
        buttonGroup.add(jRadioButton4);
        JRadioButton jRadioButton5 = new JRadioButton("15 " + strB);
        jRadioButton5.setActionCommand("15");
        jRadioButton5.setSelected(str.equals("15"));
        jRadioButton5.addActionListener(actionListener);
        add(jRadioButton5);
        buttonGroup.add(jRadioButton5);
        boolean zA = C1806i.a().a(" 98 98  0gep9gds09kfg09");
        JRadioButton jRadioButton6 = new JRadioButton("20 " + strB);
        jRadioButton6.setActionCommand("20");
        jRadioButton6.setSelected(str.equals("20"));
        jRadioButton6.addActionListener(actionListener);
        jRadioButton6.setEnabled(zA);
        add(jRadioButton6);
        buttonGroup.add(jRadioButton6);
        JRadioButton jRadioButton7 = new JRadioButton("25 " + strB);
        jRadioButton7.setActionCommand("25");
        jRadioButton7.setSelected(str.equals("25"));
        jRadioButton7.addActionListener(actionListener);
        jRadioButton7.setEnabled(zA);
        add(jRadioButton7);
        buttonGroup.add(jRadioButton7);
        JRadioButton jRadioButton8 = new JRadioButton("50 " + strB);
        jRadioButton8.setActionCommand("50");
        jRadioButton8.setSelected(str.equals("50"));
        jRadioButton8.addActionListener(actionListener);
        jRadioButton8.setEnabled(zA);
        add(jRadioButton8);
        buttonGroup.add(jRadioButton8);
        JRadioButton jRadioButton9 = new JRadioButton("100 " + strB);
        jRadioButton9.setActionCommand("100");
        jRadioButton9.setSelected(str.equals("100"));
        jRadioButton9.addActionListener(actionListener);
        jRadioButton9.setEnabled(zA);
        add(jRadioButton9);
        buttonGroup.add(jRadioButton9);
        JRadioButton jRadioButton10 = new JRadioButton("250 " + strB);
        jRadioButton10.setActionCommand("250");
        jRadioButton10.setSelected(str.equals("250"));
        jRadioButton10.addActionListener(actionListener);
        jRadioButton10.setEnabled(zA);
        add(jRadioButton10);
        buttonGroup.add(jRadioButton10);
        JRadioButton jRadioButton11 = new JRadioButton("500 " + strB);
        jRadioButton11.setActionCommand("500");
        jRadioButton11.setSelected(str.equals("500"));
        jRadioButton11.addActionListener(actionListener);
        jRadioButton11.setEnabled(zA);
        add(jRadioButton11);
        buttonGroup.add(jRadioButton11);
        JRadioButton jRadioButton12 = new JRadioButton("Max " + strB);
        jRadioButton12.setActionCommand("1000");
        jRadioButton12.setSelected(str.equals("1000"));
        jRadioButton12.addActionListener(actionListener);
        jRadioButton12.setEnabled(zA);
        add(jRadioButton12);
        buttonGroup.add(jRadioButton12);
    }

    public void a(Component component) {
        bV.a(this, component, C1818g.b("Set Data Rate"), (InterfaceC1565bc) null);
    }
}
