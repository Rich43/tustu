package com.intel.bluetooth.obex;

import java.io.IOException;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/obex/OBEXOperationDelivery.class */
interface OBEXOperationDelivery extends OBEXOperation {
    void deliverPacket(boolean z2, byte[] bArr) throws IOException;
}
