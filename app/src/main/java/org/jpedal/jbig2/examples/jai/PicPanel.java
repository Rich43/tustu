package org.jpedal.jbig2.examples.jai;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/* compiled from: JBIG2ReaderPluginTester.java */
/* loaded from: icepdf-core.jar:org/jpedal/jbig2/examples/jai/PicPanel.class */
class PicPanel extends JPanel {
    static final int WIDTH = 600;
    static final int HEIGHT = 440;

    /* renamed from: bi, reason: collision with root package name */
    private BufferedImage f13137bi;

    PicPanel(BufferedImage bi2) {
        setBufferedImage(bi2);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        if (this.f13137bi == null) {
            return new Dimension(600, 440);
        }
        return new Dimension(this.f13137bi.getWidth(), this.f13137bi.getHeight());
    }

    @Override // javax.swing.JComponent
    public void paintComponent(Graphics g2) {
        super.paintComponent(g2);
        if (this.f13137bi != null) {
            g2.drawImage(this.f13137bi, 0, 0, this);
        }
    }

    public void setBufferedImage(BufferedImage bi2) {
        this.f13137bi = bi2;
        revalidate();
        repaint();
    }
}
