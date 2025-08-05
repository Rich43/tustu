package bt;

import java.awt.Toolkit;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/* loaded from: TunerStudioMS.jar:bt/bJ.class */
class bJ extends DocumentFilter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bI f8913a;

    bJ(bI bIVar) {
        this.f8913a = bIVar;
    }

    @Override // javax.swing.text.DocumentFilter
    public void replace(DocumentFilter.FilterBypass filterBypass, int i2, int i3, String str, AttributeSet attributeSet) throws BadLocationException {
        if (str == null) {
            return;
        }
        if ((this.f8913a.getText().length() + str.length()) - i3 <= this.f8913a.f8912b) {
            super.replace(filterBypass, i2, i3, str, attributeSet);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }
}
