package com.efiAnalytics.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import javax.swing.JDialog;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/dF.class */
public class dF extends JDialog implements InterfaceC1565bc {

    /* renamed from: o, reason: collision with root package name */
    aO f11325o;

    /* renamed from: a, reason: collision with root package name */
    private InterfaceC1662et f11326a;

    public dF(Window window, String str) {
        this(window, str, null);
    }

    public dF(Window window, String str, aO aOVar) {
        this(window, str, aOVar, false);
    }

    public dF(Window window, String str, aO aOVar, boolean z2) {
        super(window, str, JDialog.DEFAULT_MODALITY_TYPE);
        this.f11325o = null;
        this.f11326a = null;
        setModal(z2);
        this.f11325o = aOVar;
        addWindowListener(new dG(this));
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new dH(this, this));
    }

    public void k() {
        if (this.f11325o != null) {
            this.f11325o.i();
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        l();
        dispose();
    }

    @Override // java.awt.Dialog, java.awt.Window, java.awt.Component
    public void setVisible(boolean z2) {
        if (z2) {
            m();
        } else {
            l();
        }
        super.setVisible(z2);
    }

    protected void l() {
        if (this.f11326a != null) {
            Point location = getLocation();
            this.f11326a.a(LanguageTag.PRIVATEUSE, location.f12370x + "");
            this.f11326a.a(PdfOps.y_TOKEN, location.f12371y + "");
            Dimension size = getSize();
            this.f11326a.a(PdfOps.w_TOKEN, size.width + "");
            this.f11326a.a(PdfOps.h_TOKEN, size.height + "");
        }
    }

    protected void m() throws HeadlessException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int i2 = screenSize.width > 1150 ? 1150 : screenSize.width;
        if (this.f11326a != null) {
            String strA = this.f11326a.a(LanguageTag.PRIVATEUSE);
            int i3 = (strA == null || strA.equals("")) ? -1 : Integer.parseInt(strA);
            String strA2 = this.f11326a.a(PdfOps.y_TOKEN);
            int i4 = (strA2 == null || strA2.equals("")) ? -1 : Integer.parseInt(strA2);
            String strA3 = this.f11326a.a(PdfOps.w_TOKEN);
            int i5 = (strA3 == null || strA3.equals("")) ? i2 : Integer.parseInt(strA3);
            String strA4 = this.f11326a.a(PdfOps.h_TOKEN);
            setSize(i5, (strA4 == null || strA4.equals("")) ? 800 : Integer.parseInt(strA4));
            if ((i3 < 0) && (i4 < 0)) {
                bV.a((Window) getParent(), (Component) this);
            } else {
                setLocation(i3, i4);
            }
        }
    }

    public void a(InterfaceC1662et interfaceC1662et) {
        this.f11326a = interfaceC1662et;
    }
}
