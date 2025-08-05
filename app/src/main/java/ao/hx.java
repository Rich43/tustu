package ao;

import com.efiAnalytics.ui.C1705w;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Iterator;
import javax.swing.JPanel;
import javax.swing.Scrollable;

/* loaded from: TunerStudioMS.jar:ao/hx.class */
class hx extends JPanel implements Scrollable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ hn f6145a;

    hx(hn hnVar) {
        this.f6145a = hnVar;
    }

    @Override // javax.swing.Scrollable
    public Dimension getPreferredScrollableViewportSize() {
        Dimension preferredSize = getPreferredSize();
        int iA = a();
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
        Iterator it = this.f6145a.p().iterator();
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
