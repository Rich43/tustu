package aP;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.TransferHandler;

/* renamed from: aP.dt, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/dt.class */
final class C0304dt extends TransferHandler {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0293dh f3248a;

    C0304dt(C0293dh c0293dh) {
        this.f3248a = c0293dh;
    }

    @Override // javax.swing.TransferHandler
    public boolean canImport(TransferHandler.TransferSupport transferSupport) {
        for (DataFlavor dataFlavor : transferSupport.getDataFlavors()) {
            if (dataFlavor.isFlavorJavaFileListType()) {
                if (aE.a.A() != null) {
                    return true;
                }
                try {
                    List list = (List) transferSupport.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    if (list.size() > 0) {
                        return !W.U.c((File) list.get(0));
                    }
                    return false;
                } catch (Exception e2) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // javax.swing.TransferHandler
    public boolean importData(TransferHandler.TransferSupport transferSupport) {
        if (!canImport(transferSupport)) {
            return false;
        }
        try {
            List list = (List) transferSupport.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
            if (list != null && list.size() > 0) {
                C0338f.a().a(cZ.a().c(), (File) list.get(0));
            }
            return true;
        } catch (UnsupportedFlavorException | IOException e2) {
            return false;
        }
    }
}
