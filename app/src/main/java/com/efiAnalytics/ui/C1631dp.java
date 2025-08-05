package com.efiAnalytics.ui;

import java.awt.Toolkit;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import sun.util.locale.LanguageTag;

/* renamed from: com.efiAnalytics.ui.dp, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/dp.class */
class C1631dp extends DocumentFilter {

    /* renamed from: b, reason: collision with root package name */
    private boolean f11442b = true;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ Cdo f11443a;

    C1631dp(Cdo cdo) {
        this.f11443a = cdo;
    }

    @Override // javax.swing.text.DocumentFilter
    public void insertString(DocumentFilter.FilterBypass filterBypass, int i2, String str, AttributeSet attributeSet) throws BadLocationException {
        if (str == null) {
            return;
        }
        String text = this.f11443a.getText();
        if (!this.f11442b || a(str)) {
            super.insertString(filterBypass, i2, str, attributeSet);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
        if (!this.f11442b || !a(this.f11443a.getText())) {
            this.f11443a.setText(text);
            Toolkit.getDefaultToolkit().beep();
        }
        this.f11443a.f11438c = Double.NaN;
    }

    @Override // javax.swing.text.DocumentFilter
    public void replace(DocumentFilter.FilterBypass filterBypass, int i2, int i3, String str, AttributeSet attributeSet) {
        String text = this.f11443a.getText();
        if (str == null || str.equals("")) {
            return;
        }
        if (!this.f11442b || a(str)) {
            try {
                super.replace(filterBypass, i2, i3, str, attributeSet);
            } catch (Exception e2) {
            }
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
        if (this.f11442b && !a(this.f11443a.getText()) && a(text)) {
            bH.C.b("Invalid numeric value: " + this.f11443a.getText() + ", setting back to: " + text);
            this.f11443a.setText(text);
        }
        this.f11443a.f11438c = Double.NaN;
    }

    private boolean a(String str) {
        if (!this.f11443a.f()) {
            try {
                Double.parseDouble(str + 1);
                return true;
            } catch (Exception e2) {
                return false;
            }
        }
        try {
            String strSubstring = str;
            if (strSubstring.startsWith("0x")) {
                strSubstring = strSubstring.substring(2);
            } else if (strSubstring.startsWith(LanguageTag.PRIVATEUSE)) {
                strSubstring = strSubstring.substring(1);
            }
            Integer.parseInt(strSubstring, 16);
            return true;
        } catch (Exception e3) {
            return false;
        }
    }

    public void a(boolean z2) {
        this.f11442b = z2;
    }
}
