package t;

import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* renamed from: t.ar, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/ar.class */
class C1845ar extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    File f13799a;

    /* renamed from: b, reason: collision with root package name */
    int f13800b;

    /* renamed from: c, reason: collision with root package name */
    JLabel f13801c;

    /* renamed from: d, reason: collision with root package name */
    JLabel f13802d;

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ C1837aj f13803e;

    public C1845ar(C1837aj c1837aj, File file) {
        this.f13803e = c1837aj;
        this.f13800b = eJ.a(120);
        this.f13801c = new JLabel("", 0);
        this.f13802d = new JLabel("", 0);
        this.f13802d.setAlignmentX(0.5f);
        this.f13802d.setAlignmentY(0.5f);
        this.f13802d.setPreferredSize(new Dimension(this.f13800b, this.f13800b));
        this.f13802d.setMinimumSize(new Dimension(this.f13800b, this.f13800b));
        this.f13801c.setPreferredSize(new Dimension(this.f13800b, getFont().getSize()));
        this.f13801c.setMinimumSize(new Dimension(this.f13800b, getFont().getSize()));
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, this.f13802d);
        add("South", this.f13801c);
        a(file);
    }

    public C1845ar(C1837aj c1837aj, int i2) {
        this.f13803e = c1837aj;
        this.f13800b = eJ.a(120);
        this.f13801c = new JLabel("", 0);
        this.f13802d = new JLabel("", 0);
        this.f13800b = i2;
        this.f13802d.setAlignmentX(0.5f);
        this.f13802d.setAlignmentY(0.5f);
        this.f13802d.setPreferredSize(new Dimension(i2, i2));
        this.f13802d.setMinimumSize(new Dimension(i2, i2));
        this.f13801c.setPreferredSize(new Dimension(i2, getFont().getSize()));
        this.f13801c.setMinimumSize(new Dimension(i2, getFont().getSize()));
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, this.f13802d);
        add("South", this.f13801c);
    }

    public void a(File file) {
        this.f13799a = file;
        if (file == null || !file.exists()) {
            this.f13802d.setIcon(null);
            this.f13801c.setText("");
            repaint();
            return;
        }
        Image image = Toolkit.getDefaultToolkit().getImage(file.getAbsolutePath());
        MediaTracker mediaTracker = new MediaTracker(this);
        mediaTracker.addImage(image, 1);
        try {
            mediaTracker.waitForAll(250L);
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
        mediaTracker.removeImage(image, 1);
        float width = image.getWidth(null) > image.getHeight(null) ? this.f13800b / image.getWidth(null) : this.f13800b / image.getHeight(null);
        this.f13802d.setIcon(new ImageIcon(image.getScaledInstance(Math.round(image.getWidth(null) * width), Math.round(image.getHeight(null) * width), 1)));
        this.f13801c.setText(file.getName());
        repaint();
    }
}
