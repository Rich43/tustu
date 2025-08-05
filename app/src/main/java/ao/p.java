package aO;

import com.efiAnalytics.ui.aY;
import java.awt.Rectangle;
import java.util.StringTokenizer;

/* loaded from: TunerStudioMS.jar:aO/p.class */
class p implements aY {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ k f2709a;

    p(k kVar) {
        this.f2709a = kVar;
    }

    @Override // com.efiAnalytics.ui.aY
    public void a(double d2, double d3) {
        String text = this.f2709a.f2666b.getText();
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
                        this.f2709a.f2666b.requestFocus();
                        this.f2709a.f2666b.select(iIndexOf, iIndexOf2);
                        if (this.f2709a.f2703F) {
                            this.f2709a.f2668d.getSelectionModel().setSelectionInterval(i2 - 1, i2 - 1);
                            Rectangle bounds = this.f2709a.f2702E.getBounds();
                            bounds.f12373y += (this.f2709a.f2668d.getHeight() * (i2 - 10)) / this.f2709a.f2667c.getRowCount();
                            this.f2709a.f2668d.scrollRectToVisible(bounds);
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
