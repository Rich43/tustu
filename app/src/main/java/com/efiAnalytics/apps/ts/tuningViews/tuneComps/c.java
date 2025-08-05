package com.efiAnalytics.apps.ts.tuningViews.tuneComps;

import com.efiAnalytics.ui.cO;
import java.awt.Font;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import n.C1762b;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/tuneComps/c.class */
class c implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ BurnButtonTv f9851a;

    c(BurnButtonTv burnButtonTv) {
        this.f9851a = burnButtonTv;
    }

    @Override // java.lang.Runnable
    public void run() {
        int width = this.f9851a.f9812c.getWidth();
        int height = this.f9851a.f9812c.getHeight();
        int i2 = height / 2 < width / 4 ? height / 2 : width / 4;
        Font font = this.f9851a.f9812c.getFont();
        int i3 = 0;
        try {
            do {
                i3++;
                font = new Font(font.getFamily(), font.getStyle(), i3);
                if ((width / 2) - i2 > this.f9851a.f9812c.getFontMetrics(font).stringWidth(this.f9851a.f9812c.getText())) {
                }
                break;
            } while (i3 < height / 2);
            break;
            this.f9851a.f9815f = new ImageIcon(cO.a().a(cO.f11130T, this.f9851a.f9812c, i2));
            this.f9851a.f9816g = new ImageIcon(cO.a().a(cO.f11133W, this.f9851a.f9812c, i2));
        } catch (V.a e2) {
            Logger.getLogger(C1762b.class.getName()).log(Level.INFO, "Unable to load burn button image.", (Throwable) e2);
        }
        this.f9851a.enableBurn(this.f9851a.f9817h);
        this.f9851a.f9812c.setFont(font);
    }
}
