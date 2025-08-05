package bx;

import java.awt.Toolkit;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/* loaded from: TunerStudioMS.jar:bx/r.class */
class r extends DocumentFilter {

    /* renamed from: a, reason: collision with root package name */
    String f9219a = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ _";

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ q f9220b;

    r(q qVar) {
        this.f9220b = qVar;
    }

    @Override // javax.swing.text.DocumentFilter
    public void insertString(DocumentFilter.FilterBypass filterBypass, int i2, String str, AttributeSet attributeSet) throws BadLocationException {
        if (str == null) {
            return;
        }
        String text = this.f9220b.getText();
        if (a(str)) {
            super.insertString(filterBypass, i2, str, attributeSet);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
        if (a(this.f9220b.getText())) {
            return;
        }
        this.f9220b.setText(text);
        Toolkit.getDefaultToolkit().beep();
    }

    @Override // javax.swing.text.DocumentFilter
    public void replace(DocumentFilter.FilterBypass filterBypass, int i2, int i3, String str, AttributeSet attributeSet) throws BadLocationException {
        if (str == null) {
            return;
        }
        String text = this.f9220b.getText();
        int length = (text.length() + str.length()) - i3;
        if (!a(str) || length >= this.f9220b.f9218a) {
            Toolkit.getDefaultToolkit().beep();
        } else {
            super.replace(filterBypass, i2, i3, str, attributeSet);
        }
        if (a(this.f9220b.getText()) || !a(text)) {
            return;
        }
        this.f9220b.setText(text);
        Toolkit.getDefaultToolkit().beep();
    }

    private boolean a(String str) {
        for (int i2 = 0; i2 < str.length(); i2++) {
            if (this.f9219a.indexOf(str.charAt(i2)) == -1) {
                return false;
            }
        }
        return true;
    }
}
