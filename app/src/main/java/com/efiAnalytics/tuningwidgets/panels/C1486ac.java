package com.efiAnalytics.tuningwidgets.panels;

import G.bS;
import W.C0171aa;
import W.C0200z;
import com.efiAnalytics.ui.AbstractC1600ck;
import com.efiAnalytics.ui.C1685fp;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.io.File;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import r.C1798a;
import s.C1818g;

/* renamed from: com.efiAnalytics.tuningwidgets.panels.ac, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/ac.class */
public class C1486ac extends AbstractC1600ck {

    /* renamed from: c, reason: collision with root package name */
    JPanel f10385c;

    /* renamed from: a, reason: collision with root package name */
    ButtonGroup f10382a = new ButtonGroup();

    /* renamed from: b, reason: collision with root package name */
    JTextPane f10383b = new JTextPane();

    /* renamed from: d, reason: collision with root package name */
    private String f10384d = "";

    /* renamed from: e, reason: collision with root package name */
    private boolean f10386e = false;

    public C1486ac(String[] strArr, String str) {
        this.f10385c = null;
        setBorder(BorderFactory.createTitledBorder(FXMLLoader.CONTROLLER_SUFFIX));
        this.f10385c = new JPanel();
        this.f10385c.setLayout(new GridLayout(0, 1));
        if (strArr != null) {
            for (int i2 = 0; i2 < strArr.length; i2++) {
                JRadioButton jRadioButton = new JRadioButton(strArr[i2]);
                jRadioButton.setActionCommand(strArr[i2]);
                this.f10382a.add(jRadioButton);
                this.f10385c.add(jRadioButton);
                if (str != null && strArr[i2].equals(str)) {
                    this.f10382a.setSelected(jRadioButton.getModel(), true);
                }
            }
        }
        setLayout(new BorderLayout());
        add("North", this.f10385c);
        add(BorderLayout.CENTER, this.f10383b);
        this.f10383b.setBackground(getBackground());
        this.f10383b.setForeground(UIManager.getColor("Label.foreground"));
        this.f10383b.setBorder(BorderFactory.createEtchedBorder());
    }

    public String a() {
        return this.f10382a.getSelection().getActionCommand();
    }

    public void a(String str) {
        Enumeration<AbstractButton> elements = this.f10382a.getElements();
        while (elements.hasMoreElements()) {
            AbstractButton abstractButtonNextElement2 = elements.nextElement2();
            this.f10382a.setSelected(abstractButtonNextElement2.getModel(), abstractButtonNextElement2.getText().equals(str));
        }
        C1685fp.a((Component) this.f10385c, false);
    }

    public void b(String str) {
        Enumeration<AbstractButton> elements = this.f10382a.getElements();
        while (elements.hasMoreElements()) {
            AbstractButton abstractButtonNextElement2 = elements.nextElement2();
            if (abstractButtonNextElement2.getActionCommand().equals(str)) {
                this.f10382a.setSelected(abstractButtonNextElement2.getModel(), true);
            }
        }
    }

    @Override // com.efiAnalytics.ui.AbstractC1600ck
    public void a(File file) {
        if (file == null) {
            this.f10383b.setText(b());
            return;
        }
        if (this.f10384d.length() > 0) {
            this.f10383b.setText(b() + "\n\n" + c(file));
        } else {
            this.f10383b.setText(c(file));
        }
        String strA = C0200z.a(file);
        String[] strArrD = G.T.a().d();
        if (strArrD != null) {
            for (int i2 = 0; i2 < strArrD.length; i2++) {
                if (G.T.a().c(strArrD[i2]).i().equals(strA)) {
                    b(strArrD[i2]);
                }
            }
        }
    }

    protected String c(File file) {
        try {
            String strB = C1818g.b("Unrecognized file type");
            if (file.getName().toLowerCase().endsWith("." + C1798a.cw.toLowerCase()) || file.getName().toLowerCase().endsWith("." + C1798a.cz.toLowerCase()) || file.getName().toLowerCase().indexOf(".ini") != -1 || file.getName().toLowerCase().indexOf(".ecu") != -1) {
                strB = "Selected File Info:\n\n";
                try {
                    bS bSVarA = new C0171aa().a(file);
                    if (bSVarA.c() != null && bSVarA.c().length() > 0) {
                        strB = strB + "Firmware:\n" + URLDecoder.decode(bSVarA.c(), "UTF-8") + "\n\n";
                    }
                    strB = strB + "Signature:\n" + bSVarA.b() + "\n\nLast Modified : " + DateFormat.getDateInstance(2).format(Long.valueOf(file.lastModified()));
                } catch (Exception e2) {
                }
            } else if (file.getName().toLowerCase().endsWith("." + C1798a.f13295C)) {
                strB = "Selected File Info:\n\nOld style BigComm tune file.\nLast Modified : " + DateFormat.getDateInstance(2).format(Long.valueOf(file.lastModified()));
            }
            return strB;
        } catch (Exception e3) {
            Logger.getLogger(C1486ac.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            return C1818g.b("Unrecognized file type");
        }
    }

    @Override // com.efiAnalytics.ui.AbstractC1600ck
    public void b(File file) {
        bH.C.c("Directory Selected: " + file.getAbsolutePath());
    }

    public String b() {
        return this.f10384d;
    }

    public void c(String str) {
        this.f10384d = str;
        this.f10383b.setText(str);
    }

    public void a(boolean z2) {
        this.f10386e = z2;
    }
}
