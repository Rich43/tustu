package aR;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:aR/t.class */
class t implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    String f3878a;

    /* renamed from: b, reason: collision with root package name */
    ActionListener f3879b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ s f3880c;

    public t(s sVar, String str, ActionListener actionListener) {
        this.f3880c = sVar;
        this.f3878a = str;
        this.f3879b = actionListener;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        String actionCommand = actionEvent.getActionCommand();
        this.f3880c.f3876a.clear();
        if (actionCommand == null || actionCommand.isEmpty()) {
            return;
        }
        this.f3880c.f3876a.add(new d.i("ecuConfig", this.f3878a));
        this.f3880c.f3876a.add(new d.i("settingsPanelName", actionCommand));
        this.f3879b.actionPerformed(actionEvent);
    }
}
