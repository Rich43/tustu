package aG;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;

/* loaded from: TunerStudioMS.jar:aG/f.class */
class f implements FTPFileFilter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ e f2402a;

    f(e eVar) {
        this.f2402a = eVar;
    }

    @Override // org.apache.commons.net.ftp.FTPFileFilter
    public boolean accept(FTPFile fTPFile) {
        return fTPFile.getName().toLowerCase().endsWith(".mlg") || fTPFile.getName().toLowerCase().endsWith(".csv") || fTPFile.getName().toLowerCase().endsWith(".msl") || fTPFile.getName().toLowerCase().endsWith(".bsf") || fTPFile.getName().toLowerCase().endsWith(".fof");
    }
}
