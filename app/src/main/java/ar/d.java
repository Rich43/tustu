package aR;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:aR/d.class */
class d implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    String f3856a;

    /* renamed from: b, reason: collision with root package name */
    ActionListener f3857b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C0472b f3858c;

    public d(C0472b c0472b, String str, ActionListener actionListener) {
        this.f3858c = c0472b;
        this.f3856a = str;
        this.f3857b = actionListener;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        String actionCommand = actionEvent.getActionCommand();
        this.f3858c.f3853a.clear();
        if (actionCommand == null || actionCommand.isEmpty()) {
            return;
        }
        this.f3858c.f3853a.add(new d.i("ecuConfig", this.f3856a));
        this.f3858c.f3853a.add(new d.i("commandName", actionCommand));
        this.f3857b.actionPerformed(actionEvent);
    }
}
