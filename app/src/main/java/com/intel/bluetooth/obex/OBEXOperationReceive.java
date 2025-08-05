package com.intel.bluetooth.obex;

import java.io.IOException;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/obex/OBEXOperationReceive.class */
interface OBEXOperationReceive extends OBEXOperation {
    void receiveData(OBEXOperationInputStream oBEXOperationInputStream) throws IOException;
}
