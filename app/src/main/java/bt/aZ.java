package bt;

import java.awt.Graphics;
import javax.swing.JLabel;
import org.icepdf.core.util.PdfOps;

/* loaded from: TunerStudioMS.jar:bt/aZ.class */
public class aZ extends JLabel {

    /* renamed from: a, reason: collision with root package name */
    String f8803a = null;

    public aZ() {
        addMouseListener(new C1319ba(this));
    }

    public aZ(String str) {
        super.setOpaque(true);
        addMouseListener(new C1319ba(this));
        setText(str);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        super.paint(graphics);
    }

    @Override // javax.swing.JLabel
    public void setText(String str) {
        super.setText(a(str));
    }

    private String a(String str) {
        if (str == null) {
            return null;
        }
        if (str.contains("<a") && str.contains("href=")) {
            bH.W.b(str, PdfOps.DOUBLE_QUOTE__TOKEN, "");
            String strSubstring = str.substring(str.indexOf("http", str.indexOf("href=")));
            if (strSubstring.indexOf(">") > 0) {
                strSubstring = strSubstring.substring(0, strSubstring.indexOf(">"));
            }
            if (strSubstring.indexOf(" ") > 0) {
                strSubstring = strSubstring.substring(0, strSubstring.indexOf(" "));
            }
            this.f8803a = strSubstring;
            setToolTipText(strSubstring);
            return str;
        }
        if (!str.contains("http:") && !str.contains("https:")) {
            return str;
        }
        String strSubstring2 = str.substring(0, str.indexOf("http"));
        String strSubstring3 = "";
        String strSubstring4 = str.substring(str.indexOf("http"));
        if (strSubstring4.contains(" ")) {
            strSubstring3 = strSubstring4.substring(strSubstring4.indexOf(" "));
            strSubstring4 = strSubstring4.substring(0, strSubstring4.indexOf(" "));
        }
        this.f8803a = strSubstring4;
        setToolTipText(strSubstring4);
        StringBuilder sb = new StringBuilder();
        sb.append("<html>").append(strSubstring2).append("<a href=").append(strSubstring4).append(" >").append(strSubstring4).append("</a>").append(strSubstring3).append("</html>");
        return sb.toString();
    }
}
