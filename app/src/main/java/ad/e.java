package aD;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortException;

/* loaded from: TunerStudioMS.jar:aD/e.class */
public class e extends OutputStream {

    /* renamed from: a, reason: collision with root package name */
    SerialPort f2333a;

    public e(SerialPort serialPort) {
        this.f2333a = null;
        this.f2333a = serialPort;
    }

    @Override // java.io.OutputStream
    public void write(int i2) throws IOException {
        a();
        try {
            this.f2333a.writeInt(i2);
        } catch (SerialPortException e2) {
            Logger.getLogger(e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            throw new IOException("Failed to write to port " + this.f2333a.getPortName());
        }
    }

    @Override // java.io.OutputStream
    public void write(byte[] bArr) throws IOException {
        a();
        try {
            this.f2333a.writeBytes(bArr);
        } catch (SerialPortException e2) {
            Logger.getLogger(e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            throw new IOException("Failed to write to port " + this.f2333a.getPortName());
        }
    }

    private void a() throws IOException {
        if (this.f2333a == null) {
            throw new IOException("serialPort not set!");
        }
        if (!this.f2333a.isOpened()) {
            throw new IOException("serialPort " + this.f2333a.getPortName() + " not open");
        }
    }
}
