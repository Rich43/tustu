package z;

import G.C0130m;
import G.R;
import G.T;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: TunerStudioMS.jar:z/l.class */
class l extends KeyAdapter {

    /* renamed from: b, reason: collision with root package name */
    private OutputStream f14131b;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ k f14132a;

    public l(k kVar, OutputStream outputStream) {
        this.f14132a = kVar;
        a(outputStream);
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) throws IOException {
        char keyChar = keyEvent.getKeyChar();
        R rC = T.a().c();
        if (rC != null && rC.C().q()) {
            C0130m c0130mA = C0130m.a(new int[]{keyChar});
            c0130mA.b(this.f14132a.f14130j);
            rC.C().b(c0130mA);
        } else {
            if (keyChar == '\n') {
                keyChar = '\r';
            }
            try {
                a().write(keyChar);
            } catch (IOException e2) {
                System.err.println("Could not write byte, OutputStream not open");
            }
        }
    }

    public OutputStream a() {
        return this.f14131b;
    }

    public void a(OutputStream outputStream) {
        this.f14131b = outputStream;
    }
}
