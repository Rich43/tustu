package aP;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:aP/iE.class */
public class iE extends JWindow {

    /* renamed from: a, reason: collision with root package name */
    JLabel f3642a;

    /* renamed from: b, reason: collision with root package name */
    String[] f3643b;

    public iE(Frame frame, String str, boolean z2) throws HeadlessException {
        super(frame);
        this.f3642a = new JLabel("Starting " + C1798a.f13268b + " " + C1798a.f13269c + " " + C1798a.f13267a);
        this.f3643b = new String[]{"Get a better tune and Faster with VE Analyze Live!", "Load Crank Trigger logs and page through data", "Advanced 3D table features and customizations", "Support the effort to bring you better tuning tools!", "Full Screen Dashboards", "Get a Wide Selection of Gauge styles and dashboards", "Toggle from 2D to 3D view on any table", "Have multiple Tabbed dashboards open at all times", "Difference Reports - Compare your tune to a saved state or ECU", "Save comments with any setting"};
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        add(BorderLayout.CENTER, jPanel);
        jPanel.setBorder(BorderFactory.createRaisedBevelBorder());
        this.f3642a.setBorder(BorderFactory.createEtchedBorder());
        if (z2) {
            JPanel jPanel2 = new JPanel();
            jPanel2.setLayout(new BorderLayout());
            jPanel2.add(BorderLayout.CENTER, new JLabel(this.f3643b[(int) Math.round(Math.random() * (this.f3643b.length - 1))], 2));
            JButton jButton = new JButton("Learn More!");
            jButton.addActionListener(new iF(this));
            jPanel2.add("East", jButton);
            jPanel.add("North", jPanel2);
        }
        jPanel.add("South", this.f3642a);
        Image image = Toolkit.getDefaultToolkit().getImage(str);
        MediaTracker mediaTracker = new MediaTracker(this);
        mediaTracker.addImage(image, 0);
        try {
            mediaTracker.waitForAll(2000L);
        } catch (InterruptedException e2) {
            Logger.getLogger(iE.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        if (image.getHeight(null) > com.efiAnalytics.ui.eJ.a(318)) {
            image = com.efiAnalytics.ui.eJ.a(image, this, 318);
        } else if (image.getHeight(null) < com.efiAnalytics.ui.eJ.a(280)) {
            image = com.efiAnalytics.ui.eJ.a(image, this, 280);
        }
        ImageIcon imageIcon = new ImageIcon(image);
        JLabel jLabel = new JLabel();
        jLabel.setIcon(imageIcon);
        jPanel.add(BorderLayout.CENTER, jLabel);
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - getSize().width) / 2, (screenSize.height - getSize().height) / 2);
        setVisible(true);
    }

    @Override // java.awt.Component, java.awt.image.ImageObserver
    public boolean imageUpdate(Image image, int i2, int i3, int i4, int i5, int i6) {
        bH.C.c("info: " + i2 + ", width:" + i5 + ", height:" + i6);
        return super.imageUpdate(image, i2, i3, i4, i5, i6);
    }

    public void a(String str) throws IllegalArgumentException {
        this.f3642a.setText(str);
        this.f3642a.repaint();
    }

    public String a() {
        return this.f3642a.getText();
    }
}
