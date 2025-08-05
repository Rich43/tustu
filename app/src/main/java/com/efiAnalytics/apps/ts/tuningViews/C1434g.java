package com.efiAnalytics.apps.ts.tuningViews;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;

/* renamed from: com.efiAnalytics.apps.ts.tuningViews.g, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/g.class */
public class C1434g extends JComponent {

    /* renamed from: a, reason: collision with root package name */
    TuneViewComponent f9779a;

    public C1434g(TuneViewComponent tuneViewComponent) {
        this.f9779a = tuneViewComponent;
    }

    @Override // javax.swing.JComponent, java.awt.Component
    protected void processMouseEvent(MouseEvent mouseEvent) {
        if (mouseEvent.getID() != 500 && mouseEvent.getID() != 501 && mouseEvent.getID() != 502) {
            super.processMouseEvent(mouseEvent);
            return;
        }
        Component componentA = a(mouseEvent.getX(), mouseEvent.getY());
        if (!this.f9779a.isEditComponent(componentA) && this.f9779a.isShieldedDuringEdit()) {
            super.processMouseEvent(mouseEvent);
            return;
        }
        mouseEvent.setSource(componentA);
        Point locationOnScreen = mouseEvent.getLocationOnScreen();
        Point locationOnScreen2 = componentA.getLocationOnScreen();
        componentA.dispatchEvent(new MouseEvent(componentA, mouseEvent.getID(), mouseEvent.getWhen(), mouseEvent.getModifiers(), locationOnScreen.f12370x - locationOnScreen2.f12370x, locationOnScreen.f12371y - locationOnScreen2.f12371y, mouseEvent.getClickCount(), mouseEvent.isPopupTrigger(), mouseEvent.getButton()));
        mouseEvent.consume();
    }

    private Component a(int i2, int i3) {
        Component componentFindComponentAt = null;
        synchronized (getTreeLock()) {
            for (Component component : this.f9779a.getComponents()) {
                if (!(component instanceof C1434g) && component.contains(i2 - component.getX(), (i3 + this.f9779a.getInsets().top) - component.getY()) && (component instanceof Container)) {
                    componentFindComponentAt = ((Container) component).findComponentAt(i2, i3);
                }
            }
        }
        return componentFindComponentAt != null ? componentFindComponentAt : this;
    }
}
