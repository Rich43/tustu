package aI;

import G.C0130m;
import G.C0132o;
import G.R;
import bH.C;
import bH.C0995c;
import bH.Z;
import com.efiAnalytics.remotefileaccess.RemoteAccessException;
import com.efiAnalytics.remotefileaccess.RemoteFileDescriptor;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/* loaded from: TunerStudioMS.jar:aI/e.class */
public class e {

    /* renamed from: a, reason: collision with root package name */
    R f2441a;

    /* renamed from: b, reason: collision with root package name */
    r f2442b;

    /* renamed from: c, reason: collision with root package name */
    int f2443c = 0;

    /* renamed from: d, reason: collision with root package name */
    int f2444d = 0;

    /* renamed from: e, reason: collision with root package name */
    boolean f2445e = false;

    /* renamed from: f, reason: collision with root package name */
    private boolean f2446f = true;

    public e(R r2, r rVar) {
        this.f2441a = null;
        this.f2442b = null;
        this.f2441a = r2;
        this.f2442b = rVar;
    }

    public File a(File file, RemoteFileDescriptor remoteFileDescriptor) throws RemoteAccessException {
        this.f2445e = false;
        long size = remoteFileDescriptor.getSize();
        C0130m c0130mA = d.a(this.f2441a.O(), (int) remoteFileDescriptor.getDirectory().getNumericId(), (int) (size / 512));
        o oVarD = o.d(this.f2441a);
        C0132o c0132oA = oVarD.a(c0130mA, false, 2000);
        if (c0132oA == null) {
            throw new RemoteAccessException("Communication failure. File " + remoteFileDescriptor.getName() + " not read.");
        }
        if (c0132oA.a() == 3) {
            throw new RemoteAccessException(c0132oA.c());
        }
        File file2 = new File(file, remoteFileDescriptor.getName());
        h fVar = this.f2446f ? new f(this, file2) : new i(this, file2);
        if (this.f2442b != null) {
            this.f2442b.a(remoteFileDescriptor);
        }
        this.f2443c = 0;
        this.f2444d = 0;
        Z z2 = new Z();
        C.c("Starting SD file read: " + remoteFileDescriptor.getName());
        z2.a();
        int i2 = 0;
        do {
            try {
                oVarD.a(false);
                int i3 = i2;
                i2++;
                C0130m c0130mD = d.d(this.f2441a.O(), i3);
                z2.b();
                if (0 != 0) {
                    C.c("Reading file block. Time: " + z2.c());
                }
                oVarD.a(3000);
                C0132o c0132oA2 = oVarD.a(c0130mD, false, 15000);
                z2.b();
                if (0 != 0) {
                    C.c("Complete Read file block. Time: " + z2.c());
                }
                oVarD.a(3000);
                if (c0132oA2 == null || c0132oA2.a() == 3) {
                    throw new RemoteAccessException(c0132oA2 == null ? "ECU returned no data" : c0132oA2.c());
                }
                if (c0132oA2.g() != null && c0132oA2.g().length > 0) {
                    if (0 != 0) {
                        C.c("Begin handing data to writer. Time: " + z2.c());
                    }
                    byte[] bArrG = c0132oA2.g();
                    int iA = C0995c.a(bArrG, 0, 2, true, false);
                    byte[] bArr = new byte[bArrG.length - 2];
                    System.arraycopy(bArrG, 2, bArr, 0, bArr.length);
                    z2.b();
                    if (0 != 0) {
                        C.c("Complete data prep, passing data to writer. Time: " + z2.c());
                    }
                    fVar.a(bArr);
                    z2.b();
                    if (0 != 0) {
                        C.c("Writer has the data. Time: " + z2.c());
                    }
                    new g(this, fVar, iA, bArr.length, size).start();
                    z2.b();
                    if (0 != 0) {
                        C.c("Notified Listeners. Time: " + z2.c());
                    }
                }
                if (c0132oA2.g() == null || c0132oA2.g().length != 2050) {
                    break;
                }
            } finally {
                fVar.a();
                oVarD.a();
                oVarD.b();
                C.c("Runtime Reads enabled.");
            }
        } while (!this.f2445e);
        if (!this.f2445e) {
            return file2;
        }
        RemoteAccessException remoteAccessException = new RemoteAccessException("Download File " + remoteFileDescriptor.getName() + " Cancelled.");
        remoteAccessException.setTerminalToBatch(true);
        throw remoteAccessException;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public OutputStream a(File file) throws IOException {
        if (file.exists()) {
            file.delete();
        }
        file.getParentFile().mkdirs();
        file.createNewFile();
        return new BufferedOutputStream(new FileOutputStream(file));
    }

    void a() {
        this.f2445e = true;
    }

    public void a(boolean z2) {
        this.f2446f = z2;
    }
}
