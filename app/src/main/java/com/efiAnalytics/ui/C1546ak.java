package com.efiAnalytics.ui;

import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JToolBar;
import javax.swing.UIManager;

/* renamed from: com.efiAnalytics.ui.ak, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/ak.class */
class C1546ak extends JToolBar {

    /* renamed from: a, reason: collision with root package name */
    String f10839a;

    /* renamed from: b, reason: collision with root package name */
    Font f10840b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C1705w f10841c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C1546ak(C1705w c1705w, String str) {
        super(str);
        this.f10841c = c1705w;
        this.f10839a = "";
        this.f10840b = new Font("Arial Unicode MS", 1, eJ.a(12));
    }

    public void a(String str) {
        this.f10839a = str;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        super.paint(graphics);
        graphics.setColor(UIManager.getColor("Label.foreground"));
        graphics.setFont(this.f10840b);
        graphics.drawString(this.f10839a, eJ.a(6), eJ.a(12));
    }
}
