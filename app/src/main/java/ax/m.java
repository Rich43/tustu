package aX;

import java.io.IOException;
import javax.bluetooth.RemoteDevice;

/* loaded from: TunerStudioMS.jar:aX/m.class */
class m {

    /* renamed from: b, reason: collision with root package name */
    private RemoteDevice f4025b;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ h f4026a;

    public m(h hVar, RemoteDevice remoteDevice) {
        this.f4026a = hVar;
        this.f4025b = remoteDevice;
    }

    public String toString() {
        try {
            return this.f4025b.getFriendlyName(false) + " (" + this.f4025b.getBluetoothAddress() + ")";
        } catch (IOException e2) {
            return "(" + this.f4025b.getBluetoothAddress() + ")";
        }
    }

    public RemoteDevice a() {
        return this.f4025b;
    }
}
