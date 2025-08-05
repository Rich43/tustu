package com.efiAnalytics.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;

/* renamed from: com.efiAnalytics.ui.dl, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/dl.class */
public class C1628dl extends JComponent {

    /* renamed from: b, reason: collision with root package name */
    C1629dm f11428b;

    public C1628dl() {
        this.f11428b = null;
        this.f11428b = new C1629dm(this);
        setBackground(Color.BLACK);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        this.f11428b.a(graphics);
    }

    public void a(InterfaceC1614cy interfaceC1614cy) {
        this.f11428b.a(interfaceC1614cy);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return new Dimension(100, this.f11428b.b() * 20);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return new Dimension(250, 30 + (this.f11428b.b() * 30));
    }

    @Override // java.awt.Container, java.awt.Component
    public void validate() {
        this.f11428b.a();
        super.validate();
    }
}
