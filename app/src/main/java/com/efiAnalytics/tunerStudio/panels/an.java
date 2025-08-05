package com.efiAnalytics.tunerStudio.panels;

import com.efiAnalytics.ui.aY;
import java.awt.Rectangle;
import java.util.StringTokenizer;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/an.class */
class an implements aY {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ TriggerLoggerPanel f10077a;

    an(TriggerLoggerPanel triggerLoggerPanel) {
        this.f10077a = triggerLoggerPanel;
    }

    @Override // com.efiAnalytics.ui.aY
    public void a(double d2, double d3) {
        String text = this.f10077a.f9977b.getText();
        int i2 = 0;
        String strNextToken = "";
        StringTokenizer stringTokenizer = new StringTokenizer(text, "\n");
        while (stringTokenizer.hasMoreTokens()) {
            String str = strNextToken;
            strNextToken = stringTokenizer.nextToken();
            if (strNextToken.contains("\t")) {
                try {
                    if (Double.parseDouble(strNextToken.substring(strNextToken.lastIndexOf("\t")).trim()) > d2) {
                        int iIndexOf = text.indexOf(str);
                        int iIndexOf2 = text.indexOf("\n", text.indexOf(str.substring(str.lastIndexOf("\t")).trim(), iIndexOf));
                        this.f10077a.f9977b.requestFocus();
                        this.f10077a.f9977b.select(iIndexOf, iIndexOf2);
                        if (this.f10077a.f10033Z) {
                            this.f10077a.f9979d.getSelectionModel().setSelectionInterval(i2 - 1, i2 - 1);
                            Rectangle bounds = this.f10077a.f10032Y.getBounds();
                            bounds.f12373y += (this.f10077a.f9979d.getHeight() * (i2 - 10)) / this.f10077a.f9978c.getRowCount();
                            this.f10077a.f9979d.scrollRectToVisible(bounds);
                        }
                        return;
                    }
                    i2++;
                } catch (NumberFormatException e2) {
                }
            }
        }
    }
}
