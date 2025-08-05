package com.intel.bluetooth.obex;

import com.intel.bluetooth.DebugLog;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/obex/OBEXClientOperationGet.class */
class OBEXClientOperationGet extends OBEXClientOperation {
    OBEXClientOperationGet(OBEXClientSessionImpl session, OBEXHeaderSetImpl sendHeaders) throws IOException {
        super(session, (char) 3, sendHeaders);
    }

    @Override // com.intel.bluetooth.obex.OBEXClientOperation, javax.microedition.io.InputConnection
    public InputStream openInputStream() throws IOException {
        validateOperationIsOpen();
        if (this.inputStreamOpened) {
            throw new IOException("input stream already open");
        }
        DebugLog.debug("openInputStream");
        this.inputStreamOpened = true;
        endRequestPhase();
        return this.inputStream;
    }

    @Override // com.intel.bluetooth.obex.OBEXClientOperation, javax.microedition.io.OutputConnection
    public OutputStream openOutputStream() throws IOException {
        validateOperationIsOpen();
        if (this.outputStreamOpened) {
            throw new IOException("output already open");
        }
        if (this.requestEnded) {
            throw new IOException("the request phase has already ended");
        }
        this.outputStreamOpened = true;
        this.outputStream = new OBEXOperationOutputStream(this.session.mtu, this);
        return this.outputStream;
    }
}
