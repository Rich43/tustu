package aP;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBoxMenuItem;

/* renamed from: aP.cn, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/cn.class */
class C0272cn implements ItemListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bZ f3166a;

    C0272cn(bZ bZVar) {
        this.f3166a = bZVar;
    }

    @Override // java.awt.event.ItemListener
    public void itemStateChanged(ItemEvent itemEvent) {
        h.i.c("showGraphHalfMark", ((JCheckBoxMenuItem) itemEvent.getSource()).getState() + "");
        this.f3166a.f3027b.o();
    }
}
