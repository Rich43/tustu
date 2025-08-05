package bw;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/* loaded from: TunerStudioMS.jar:bw/h.class */
class h implements ListSelectionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ f f9178a;

    h(f fVar) {
        this.f9178a = fVar;
    }

    @Override // javax.swing.event.ListSelectionListener
    public void valueChanged(ListSelectionEvent listSelectionEvent) throws IllegalArgumentException {
        if (!listSelectionEvent.getValueIsAdjusting()) {
            this.f9178a.f9174h = this.f9178a.a(this.f9178a.f9168b.getSelectedValue());
        }
        if (this.f9178a.f9174h != null) {
            this.f9178a.f9171e.setText(this.f9178a.f9174h.f9180b);
            this.f9178a.f9172f.setText(this.f9178a.f9174h.f9181c);
            this.f9178a.f9170d.b(this.f9178a.f9174h.f9183e);
            this.f9178a.f9169c.b(this.f9178a.f9174h.f9183e);
            this.f9178a.b();
        }
    }
}
