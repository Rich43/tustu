package aR;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:aR/B.class */
class B implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    ActionListener f3850a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ A f3851b;

    public B(A a2, ActionListener actionListener) {
        this.f3851b = a2;
        this.f3850a = actionListener;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        String actionCommand = actionEvent.getActionCommand();
        this.f3851b.f3848c.clear();
        if (actionCommand == null || actionCommand.isEmpty()) {
            return;
        }
        this.f3851b.f3848c.add(new d.i(A.f3847b, actionCommand));
        this.f3850a.actionPerformed(actionEvent);
    }
}
