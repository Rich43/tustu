package bh;

import com.efiAnalytics.ui.C1705w;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Iterator;
import javax.swing.JPanel;
import javax.swing.Scrollable;

/* renamed from: bh.u, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bh/u.class */
class C1161u extends JPanel implements Scrollable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1154n f8133a;

    C1161u(C1154n c1154n) {
        this.f8133a = c1154n;
    }

    @Override // javax.swing.Scrollable
    public Dimension getPreferredScrollableViewportSize() {
        int iA = a();
        if (iA == 0) {
            return getPreferredSize();
        }
        Dimension preferredSize = getPreferredSize();
        if (iA > 0) {
            preferredSize.height += iA;
        } else if (iA < 0) {
            preferredSize.width += Math.abs(iA);
        }
        return preferredSize;
    }

    @Override // javax.swing.Scrollable
    public int getScrollableUnitIncrement(Rectangle rectangle, int i2, int i3) {
        return 10;
    }

    @Override // javax.swing.Scrollable
    public int getScrollableBlockIncrement(Rectangle rectangle, int i2, int i3) {
        return (i2 == 1 ? rectangle.height : rectangle.width) - 10;
    }

    @Override // javax.swing.Scrollable
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    private int a() {
        int iN = 0;
        Iterator it = this.f8133a.b().iterator();
        while (it.hasNext()) {
            iN += ((C1705w) it.next()).n();
        }
        return iN;
    }

    @Override // javax.swing.Scrollable
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}
