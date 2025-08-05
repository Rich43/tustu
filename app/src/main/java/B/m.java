package B;

import G.R;
import bH.C;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: TunerStudioMS.jar:B/m.class */
public class m extends l implements bT.r {

    /* renamed from: m, reason: collision with root package name */
    c f181m;

    /* renamed from: n, reason: collision with root package name */
    R f182n;

    /* renamed from: l, reason: collision with root package name */
    ServerSocket f180l = null;

    /* renamed from: o, reason: collision with root package name */
    private int f183o = -1;

    public m(R r2) {
        this.f182n = r2;
        this.f181m = new c(r2);
        this.f172d = 21845;
    }

    @Override // bT.r
    public bT.r v() {
        m mVar = null;
        int iU = u();
        this.f181m.b(iU);
        if (this.f183o > 0) {
            this.f181m.a(this.f183o);
        } else {
            this.f181m.a(iU);
        }
        this.f181m.b();
        try {
            try {
                if (this.f180l == null || !this.f180l.isBound()) {
                    this.f180l = new ServerSocket(iU);
                    try {
                        byte[] address = this.f180l.getInetAddress().getAddress();
                        this.f171c = ((int) address[0]) + CallSiteDescriptor.TOKEN_DELIMITER + ((int) address[1]) + CallSiteDescriptor.TOKEN_DELIMITER + ((int) address[2]) + CallSiteDescriptor.TOKEN_DELIMITER + ((int) address[3]);
                    } catch (Exception e2) {
                        C.a("Server failed to get bound IP address");
                    }
                }
                b(2);
                C.d("TCP/IP Server Connection " + this.f182n.c() + " listening on port: " + iU);
                Socket socketAccept = this.f180l.accept();
                C.d("TCP/IP Server Connection opened on: " + iU + ", socket info: " + socketAccept.toString());
                mVar = new m(this.f182n);
                for (A.r rVar : l()) {
                    try {
                        mVar.a(rVar.c(), a(rVar.c()));
                    } catch (A.s e3) {
                        Logger.getLogger(m.class.getName()).log(Level.WARNING, "Failed to set Setting", (Throwable) e3);
                    }
                }
                mVar.a(socketAccept);
                mVar.a(new BufferedInputStream(socketAccept.getInputStream()));
                mVar.a(socketAccept.getOutputStream());
                mVar.b(3);
                mVar.a();
                this.f181m.a();
            } catch (IOException e4) {
                g();
                C.a(e4);
                this.f181m.a();
            }
            return mVar;
        } catch (Throwable th) {
            this.f181m.a();
            throw th;
        }
    }

    @Override // bT.r
    public void w() {
        this.f181m.a();
        if (this.f180l != null) {
            try {
                this.f180l.close();
            } catch (IOException e2) {
                Logger.getLogger(m.class.getName()).log(Level.SEVERE, "Error trying to stop TCP Slave Server", (Throwable) e2);
            }
            this.f180l = null;
        }
    }

    @Override // B.l, A.f
    public int s() {
        return 2;
    }

    @Override // bT.r
    public int x() {
        return 10;
    }
}
