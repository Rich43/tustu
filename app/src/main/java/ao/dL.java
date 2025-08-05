package ao;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.TransferHandler;

/* loaded from: TunerStudioMS.jar:ao/dL.class */
final class dL extends TransferHandler {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5526a;

    dL(bP bPVar) {
        this.f5526a = bPVar;
    }

    @Override // javax.swing.TransferHandler
    public boolean canImport(TransferHandler.TransferSupport transferSupport) {
        for (DataFlavor dataFlavor : transferSupport.getDataFlavors()) {
            if (dataFlavor.isFlavorJavaFileListType()) {
                return true;
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
            String[] strArr = new String[list.size()];
            for (int i2 = 0; i2 < list.size(); i2++) {
                File file = (File) list.get(i2);
                boolean z2 = false;
                String[] strArrW = this.f5526a.W();
                int length = strArrW.length;
                int i3 = 0;
                while (true) {
                    if (i3 >= length) {
                        break;
                    }
                    if (file.getName().toLowerCase().endsWith(strArrW[i3])) {
                        z2 = true;
                        break;
                    }
                    i3++;
                }
                if (!z2) {
                    return false;
                }
                strArr[i2] = file.getAbsolutePath();
            }
            this.f5526a.a(strArr);
            return true;
        } catch (UnsupportedFlavorException | IOException e2) {
            return false;
        }
    }
}
