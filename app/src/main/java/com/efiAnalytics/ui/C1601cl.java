package com.efiAnalytics.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;

/* renamed from: com.efiAnalytics.ui.cl, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/cl.class */
public class C1601cl extends AbstractC1600ck {

    /* renamed from: a, reason: collision with root package name */
    Image f11267a = null;

    public C1601cl() {
        Dimension dimension = new Dimension(150, 200);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        double width = 0.75d;
        graphics.clearRect(0, 0, getWidth(), getHeight());
        if (this.f11267a != null) {
            if (this.f11267a.getHeight(null) > 0) {
                width = this.f11267a.getWidth(null) / this.f11267a.getHeight(null);
            }
            int width2 = getWidth() - 6;
            graphics.drawImage(this.f11267a, 6, 3, width2, (int) (width2 * width), this);
        }
    }

    @Override // java.awt.Component, java.awt.image.ImageObserver
    public boolean imageUpdate(Image image, int i2, int i3, int i4, int i5, int i6) {
        repaint();
        return true;
    }

    @Override // com.efiAnalytics.ui.AbstractC1600ck
    public void a(File file) {
        if (file == null) {
            this.f11267a = null;
        } else {
            this.f11267a = Toolkit.getDefaultToolkit().getImage(file.getAbsolutePath());
            repaint();
        }
    }

    @Override // com.efiAnalytics.ui.AbstractC1600ck
    public void b(File file) {
        this.f11267a = null;
        repaint();
    }
}
