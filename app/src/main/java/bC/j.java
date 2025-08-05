package bC;

import javax.swing.AbstractListModel;

/* loaded from: TunerStudioMS.jar:bC/j.class */
class j extends AbstractListModel {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ e f6589a;

    j(e eVar) {
        this.f6589a = eVar;
    }

    @Override // javax.swing.ListModel
    public int getSize() {
        return this.f6589a.f6580f.size();
    }

    @Override // javax.swing.ListModel
    public Object getElementAt(int i2) {
        return this.f6589a.f6580f.get(i2);
    }
}
