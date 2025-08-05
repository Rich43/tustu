package com.efiAnalytics.ui;

import java.awt.Font;
import javax.swing.JPanel;

/* renamed from: com.efiAnalytics.ui.ah, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/ah.class */
class C1543ah extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    S f10831a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1705w f10832b;

    C1543ah(C1705w c1705w) {
        this.f10832b = c1705w;
    }

    public S a() {
        return this.f10831a;
    }

    public void a(S s2) {
        this.f10831a = s2;
        add(s2);
    }

    public void a(int i2) {
        if (this.f10831a == null) {
            return;
        }
        Font font = this.f10832b.f11762a.getFont();
        this.f10831a.setFont(new Font(font.getFamily(), font.getStyle(), i2));
    }

    public String[] b() {
        return this.f10831a.a();
    }

    public void b(int i2) {
        for (int i3 = 0; i3 < getComponentCount(); i3++) {
            if (super.getComponent(i3) instanceof Cdo) {
                ((Cdo) getComponent(i3)).b(i2);
            }
        }
    }
}
