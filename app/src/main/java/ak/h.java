package aK;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aK/h.class */
public class h {

    /* renamed from: a, reason: collision with root package name */
    InputStream f2597a;

    /* renamed from: b, reason: collision with root package name */
    List f2598b = new ArrayList();

    public h(InputStream inputStream) {
        this.f2597a = inputStream;
    }

    public String a() throws IOException {
        if (this.f2598b.isEmpty()) {
            b();
        }
        return (String) this.f2598b.remove(0);
    }

    private void b() throws IOException {
        StringBuilder sb = new StringBuilder();
        boolean z2 = false;
        long jCurrentTimeMillis = System.currentTimeMillis() + 10000;
        do {
            int i2 = this.f2597a.read();
            if (i2 >= 0) {
                char c2 = (char) i2;
                if (c2 == '\n') {
                    z2 = true;
                } else if (c2 != '\r') {
                    sb.append(c2);
                    jCurrentTimeMillis = System.currentTimeMillis() + 10000;
                }
            } else {
                try {
                    Thread.sleep(20L);
                } catch (InterruptedException e2) {
                    Logger.getLogger(h.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            }
            if (System.currentTimeMillis() > jCurrentTimeMillis) {
                throw new IOException("Timeout reading NEMA line.");
            }
        } while (!z2);
        this.f2598b.add(sb.toString());
    }
}
