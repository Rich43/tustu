package bc;

import com.efiAnalytics.ui.eJ;
import java.awt.Dimension;
import javax.swing.JLabel;

/* renamed from: bc.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bc/f.class */
class C1059f extends JLabel {

    /* renamed from: a, reason: collision with root package name */
    int f7871a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1054a f7872b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    C1059f(C1054a c1054a, String str) {
        super(str, 4);
        this.f7872b = c1054a;
        this.f7871a = eJ.a(90);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        Dimension preferredSize = super.getPreferredSize();
        if (preferredSize.width < this.f7871a) {
            preferredSize.width = this.f7871a;
        }
        return preferredSize;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        Dimension minimumSize = super.getMinimumSize();
        if (minimumSize.width < this.f7871a) {
            minimumSize.width = this.f7871a;
        }
        return minimumSize;
    }
}
