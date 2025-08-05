package com.efiAnalytics.apps.ts.tuningViews;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JComponent;
import javax.swing.UIManager;

/* renamed from: com.efiAnalytics.apps.ts.tuningViews.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/e.class */
class C1432e extends JComponent {

    /* renamed from: b, reason: collision with root package name */
    private Image f9776b = null;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1429b f9777a;

    C1432e(C1429b c1429b) {
        this.f9777a = c1429b;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        if (this.f9776b != null) {
            graphics.drawImage(this.f9776b, 0, 0, getWidth(), getHeight(), null);
        } else {
            graphics.setColor(UIManager.getColor("Label.foreground"));
            graphics.drawString("No Image", 10, 20);
        }
    }

    public void a(Image image) {
        this.f9776b = image;
    }
}
