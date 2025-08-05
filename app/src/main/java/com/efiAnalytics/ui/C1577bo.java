package com.efiAnalytics.ui;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* renamed from: com.efiAnalytics.ui.bo, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/bo.class */
class C1577bo extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1571bi f11011a;

    C1577bo(C1571bi c1571bi) {
        this.f11011a = c1571bi;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getX() > this.f11011a.getWidth() - eJ.a(37) && mouseEvent.getY() < eJ.a(304)) {
            this.f11011a.b();
        }
        if (this.f11011a.f11003h || mouseEvent.getX() <= eJ.a(169) || mouseEvent.getX() >= eJ.a(184) || mouseEvent.getY() >= eJ.a(304)) {
            return;
        }
        if (this.f11011a.f10996a.getText().equals("")) {
            this.f11011a.dispose();
        } else if (bV.a("Are you sure you want to delete this comment?", (Component) this.f11011a.f10996a, true)) {
            this.f11011a.c();
        }
    }
}
