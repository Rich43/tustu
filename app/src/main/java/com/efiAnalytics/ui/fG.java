package com.efiAnalytics.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import org.apache.commons.net.ftp.FTPReply;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/fG.class */
public class fG extends JComponent {

    /* renamed from: a, reason: collision with root package name */
    Image f11632a = null;

    /* renamed from: b, reason: collision with root package name */
    int f11633b = FTPReply.FILE_ACTION_PENDING;

    /* renamed from: c, reason: collision with root package name */
    int f11634c = -this.f11633b;

    /* renamed from: d, reason: collision with root package name */
    fH f11635d = null;

    public fG() {
        setMinimumSize(new Dimension(100, eJ.a(4)));
        setPreferredSize(new Dimension(100, eJ.a(4)));
        super.setOpaque(true);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        if (this.f11635d == null) {
            graphics.setColor(getBackground());
            graphics.fillRect(0, 0, getWidth(), getHeight());
            return;
        }
        int i2 = this.f11634c;
        while (true) {
            int i3 = i2;
            if (i3 >= getWidth()) {
                break;
            }
            graphics.drawImage(a(graphics), i3, 0, null);
            i2 = i3 + this.f11633b;
        }
        if (this.f11634c >= 0) {
            this.f11634c = -this.f11633b;
        }
    }

    private Image a(Graphics graphics) {
        if (this.f11632a == null) {
            this.f11632a = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(this.f11633b, getHeight(), 3);
            Graphics graphics2 = this.f11632a.getGraphics();
            Graphics2D graphics2D = (Graphics2D) graphics2;
            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2.setColor(getBackground());
            graphics2.fillRect(0, 0, this.f11632a.getWidth(null), this.f11632a.getHeight(null));
            int red = getForeground().getRed();
            int green = getForeground().getGreen();
            int blue = getForeground().getBlue();
            double width = this.f11632a.getWidth(null) / 127.5d;
            for (int i2 = 0; i2 < this.f11632a.getWidth(null); i2++) {
                int i3 = (int) (i2 * width);
                if (i3 > 255) {
                    i3 = 255 - (i3 - 255);
                }
                if (i3 < 0) {
                    i3 = 0;
                }
                graphics2.setColor(new Color(red, green, blue, i3));
                graphics2.drawLine(i2, 0, i2, this.f11632a.getHeight(null));
            }
        }
        return this.f11632a;
    }

    public void a() {
        if (this.f11635d != null) {
            this.f11635d.a();
        }
        this.f11635d = new fH(this);
        this.f11635d.start();
    }

    public void b() {
        if (this.f11635d != null) {
            this.f11635d.a();
        }
        this.f11635d = null;
        repaint();
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void setForeground(Color color) {
        super.setForeground(color);
        this.f11632a = null;
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void setBackground(Color color) {
        super.setBackground(color);
        this.f11632a = null;
    }
}
