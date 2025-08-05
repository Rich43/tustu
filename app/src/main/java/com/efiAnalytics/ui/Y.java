package com.efiAnalytics.ui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.TransferHandler;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/Y.class */
class Y extends C1538ac {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1705w f10713a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ boolean f10714b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ S f10715c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    Y(S s2, C1705w c1705w, boolean z2) {
        super(s2);
        this.f10715c = s2;
        this.f10713a = c1705w;
        this.f10714b = z2;
    }

    @Override // javax.swing.TransferHandler
    public boolean canImport(TransferHandler.TransferSupport transferSupport) {
        return transferSupport.isDataFlavorSupported(DataFlavor.stringFlavor);
    }

    @Override // javax.swing.TransferHandler
    public boolean importData(TransferHandler.TransferSupport transferSupport) {
        if (!canImport(transferSupport)) {
            return false;
        }
        try {
            String[] strArrSplit = ((String) transferSupport.getTransferable().getTransferData(DataFlavor.stringFlavor)).replace("\n", " ").replace("\t", " ").replace(Constants.INDENT, " ").split(" ");
            if (strArrSplit == null || strArrSplit.length == 0 || strArrSplit[0] == null || strArrSplit[0].trim().isEmpty()) {
                return false;
            }
            int selectedRow = this.f10714b ? this.f10715c.getSelectedRow() : this.f10715c.getSelectedColumn();
            int length = strArrSplit.length + selectedRow;
            int rowCount = this.f10714b ? this.f10715c.getRowCount() : this.f10715c.getColumnCount();
            if (length > rowCount) {
                length = rowCount;
            }
            int i2 = 0;
            for (int i3 = selectedRow; i3 < length; i3++) {
                this.f10715c.getModel().setValueAt(Double.valueOf(Double.parseDouble(strArrSplit[i2].trim())), this.f10714b ? i3 : 0, this.f10714b ? 0 : i3);
                i2++;
            }
            return true;
        } catch (UnsupportedFlavorException e2) {
            return false;
        } catch (IOException e3) {
            return false;
        }
    }
}
