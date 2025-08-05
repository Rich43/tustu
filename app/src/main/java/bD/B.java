package bD;

import com.efiAnalytics.ui.eJ;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JToolBar;

/* loaded from: TunerStudioMS.jar:bD/B.class */
class B extends JToolBar {

    /* renamed from: a, reason: collision with root package name */
    String f6608a;

    /* renamed from: b, reason: collision with root package name */
    Font f6609b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ r f6610c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public B(r rVar, String str) {
        super(str);
        this.f6610c = rVar;
        this.f6608a = "";
        this.f6609b = new Font("Arial Unicode MS", 1, eJ.a(12));
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        super.paint(graphics);
        graphics.setFont(this.f6609b);
        graphics.drawString(this.f6608a, 6, 12);
    }
}
