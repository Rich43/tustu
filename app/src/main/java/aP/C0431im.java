package aP;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* renamed from: aP.im, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/im.class */
public class C0431im extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    public C0435iq[] f3736a = {new C0435iq(this, "Awesome Dashboards!", "[localfile]/help/learnMore.html"), new C0435iq(this, "VE Table Generator", "[localfile]/help/learnMore.html"), new C0435iq(this, "AFR Table Generator", "[localfile]/help/learnMore.html"), new C0435iq(this, "VE Analyze Live!", "[localfile]/help/learnMore.html"), new C0435iq(this, "Tabbed Dashboards", "[localfile]/help/learnMore.html"), new C0435iq(this, "Trigger Log Viewer", "[localfile]/help/learnMore.html"), new C0435iq(this, "Difference Reports", "[localfile]/help/learnMore.html"), new C0435iq(this, "High Speed GPS", "https://www.efianalytics.com/products/BT-Q818XT.html"), new C0435iq(this, "Android Dashboards", "http://www.tunerstudio.com/index.php/shadowdashmsmenu"), new C0435iq(this, "Tested USB Cables", "http://www.efianalytics.com/products/usbSerialFtdi.html"), new C0435iq(this, "Fullscreen Dashboard", "[localfile]/help/learnMore.html"), new C0435iq(this, "Tune Restore Points", "[localfile]/help/learnMore.html"), new C0435iq(this, "Bluetooth Adapters", "http://www.efianalytics.com/products/class1Bluetooth.html"), new C0435iq(this, "Enhanced 3D Tables", "[localfile]/help/learnMore.html")};

    /* renamed from: b, reason: collision with root package name */
    JLabel f3737b = new JLabel();

    /* renamed from: c, reason: collision with root package name */
    int f3738c = 0;

    public C0431im() {
        setLayout(new BorderLayout());
        C0432in c0432in = new C0432in(this, " Learn More! ");
        c0432in.setFont(new Font("Arial Unicode MS", 0, com.efiAnalytics.ui.eJ.a(12)));
        c0432in.setForeground(Color.blue);
        c0432in.setBackground(Color.black);
        c0432in.addMouseListener(new C0433io(this, c0432in));
        add("East", c0432in);
        this.f3737b.setFont(new Font("Arial Unicode MS", 0, com.efiAnalytics.ui.eJ.a(12)));
        this.f3737b.setHorizontalAlignment(0);
        this.f3737b.setMinimumSize(new Dimension(130, 15));
        this.f3737b.setPreferredSize(new Dimension(130, 15));
        add(BorderLayout.CENTER, this.f3737b);
        new C0434ip(this).start();
    }
}
