package com.efiAnalytics.tunerStudio.panels;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JComponent;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/ah.class */
class ah implements MouseWheelListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ TriggerLoggerPanel f10063a;

    ah(TriggerLoggerPanel triggerLoggerPanel) {
        this.f10063a = triggerLoggerPanel;
    }

    @Override // java.awt.event.MouseWheelListener
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
        if (mouseWheelEvent.getWheelRotation() > 0) {
            if (mouseWheelEvent.getSource() instanceof JComponent) {
                this.f10063a.b(mouseWheelEvent.getX() / ((JComponent) mouseWheelEvent.getSource()).getWidth());
                return;
            }
            return;
        }
        if (mouseWheelEvent.getSource() instanceof JComponent) {
            this.f10063a.a(mouseWheelEvent.getX() / ((JComponent) mouseWheelEvent.getSource()).getWidth());
        }
    }
}
