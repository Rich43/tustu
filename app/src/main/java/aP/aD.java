package aP;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import r.C1807j;

/* loaded from: TunerStudioMS.jar:aP/aD.class */
class aD extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0224at f2802a;

    aD(C0224at c0224at) {
        this.f2802a = c0224at;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        File file = new File(this.f2802a.b() + "12" + keyEvent.getKeyChar() + "12");
        try {
            file.createNewFile();
            file.delete();
        } catch (Exception e2) {
            this.f2802a.f2932a.setText(bH.W.b(this.f2802a.f2932a.getText(), "" + keyEvent.getKeyChar(), ""));
        }
        String strU = C1807j.u();
        this.f2802a.f(strU.substring(0, strU.lastIndexOf(File.separator)));
    }
}
