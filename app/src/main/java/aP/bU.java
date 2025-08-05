package aP;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* loaded from: TunerStudioMS.jar:aP/bU.class */
class bU implements ChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bT f3014a;

    bU(bT bTVar) {
        this.f3014a = bTVar;
    }

    @Override // javax.swing.event.ChangeListener
    public void stateChanged(ChangeEvent changeEvent) {
        if (this.f3014a.getSelectedIndex() != this.f3014a.getTabCount() - 1) {
            this.f3014a.f3006a = this.f3014a.getSelectedIndex();
        } else if (this.f3014a.f3006a >= this.f3014a.getTabCount() - 1) {
            this.f3014a.setSelectedIndex(0);
        } else {
            this.f3014a.setSelectedIndex(this.f3014a.f3006a);
            this.f3014a.h();
        }
    }
}
