package bg;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.UIManager;

/* renamed from: bg.k, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bg/k.class */
class C1131k extends JComponent {

    /* renamed from: b, reason: collision with root package name */
    private Image f8080b = null;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1121a f8081a;

    C1131k(C1121a c1121a) {
        this.f8081a = c1121a;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        super.paint(graphics);
        if (this.f8080b != null) {
            graphics.drawImage(this.f8080b, 0, 0, getWidth(), getHeight(), null);
        } else {
            graphics.setColor(UIManager.getColor("Label.foreground"));
            graphics.drawString("Tune View Preview", 10, 20);
        }
    }

    public void a(File file) {
        try {
            this.f8080b = ImageIO.read(file);
        } catch (IOException e2) {
            Logger.getLogger(C1121a.class.getName()).log(Level.SEVERE, "Failed to load preview image", (Throwable) e2);
        }
        repaint();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        this.f8080b = null;
        repaint();
    }
}
