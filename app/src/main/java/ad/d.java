package aD;

import bH.C0995c;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/* loaded from: TunerStudioMS.jar:aD/d.class */
public class d extends InputStream implements SerialPortEventListener {

    /* renamed from: a, reason: collision with root package name */
    SerialPort f2327a;

    /* renamed from: b, reason: collision with root package name */
    int f2328b = 0;

    /* renamed from: c, reason: collision with root package name */
    byte[] f2329c = new byte[1];

    /* renamed from: d, reason: collision with root package name */
    byte[] f2330d = null;

    /* renamed from: e, reason: collision with root package name */
    int f2331e = 200;

    /* renamed from: f, reason: collision with root package name */
    List f2332f = new ArrayList();

    public d(SerialPort serialPort) throws SerialPortException {
        this.f2327a = null;
        this.f2327a = serialPort;
        serialPort.addEventListener(this);
    }

    public void a(SerialPortEventListener serialPortEventListener) {
        if (this.f2332f.contains(this)) {
            return;
        }
        this.f2332f.add(serialPortEventListener);
    }

    @Override // java.io.InputStream
    public synchronized int read() {
        a();
        try {
            if (this.f2327a.getInputBufferBytesCount() <= 0) {
                try {
                    wait(300L);
                } catch (InterruptedException e2) {
                    Logger.getLogger(d.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            }
            if (this.f2327a.getInputBufferBytesCount() <= 0) {
                this.f2328b = 0;
                return -1;
            }
            int iA = C0995c.a(this.f2327a.readBytes(1)[0]);
            this.f2328b--;
            return iA;
        } catch (IndexOutOfBoundsException e3) {
            throw new IOException(this.f2327a.getPortName() + ": No bytes available.");
        } catch (SerialPortException e4) {
            throw new IOException(e4.getMessage() + ", Port:" + this.f2327a.getPortName());
        }
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr) {
        return read(bArr, 0, bArr.length);
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        if (!this.f2327a.isOpened()) {
            throw new IOException("serialPort " + this.f2327a.getPortName() + " not open");
        }
        int length = 0;
        if (this.f2330d != null) {
            if (this.f2330d.length > i3) {
                System.arraycopy(this.f2330d, 0, bArr, i2, i3);
                int i4 = 0 + i3;
                byte[] bArr2 = new byte[this.f2330d.length - bArr.length];
                System.arraycopy(this.f2330d, i4, bArr2, 0, bArr2.length);
                this.f2330d = bArr2;
                return i4;
            }
            System.arraycopy(this.f2330d, 0, bArr, i2, this.f2330d.length);
            length = 0 + this.f2330d.length;
            this.f2330d = null;
        }
        while (this.f2327a.getInputBufferBytesCount() > this.f2331e && this.f2327a.getInputBufferBytesCount() <= i3) {
            try {
                byte[] bytes = this.f2327a.readBytes(this.f2331e);
                System.arraycopy(bytes, 0, bArr, length + i2, bytes.length);
                length += bytes.length;
            } catch (IndexOutOfBoundsException e2) {
                e2.printStackTrace();
                throw new IOException(this.f2327a.getPortName() + ": No bytes available.");
            } catch (SerialPortException e3) {
                throw new IOException(e3.getMessage() + ", Port:" + this.f2327a.getPortName());
            }
        }
        int i5 = i3 - length;
        if (length + i2 < bArr.length) {
            byte[] bytes2 = this.f2327a.getInputBufferBytesCount() >= i5 ? this.f2327a.readBytes(i5) : this.f2327a.readBytes();
            if (bytes2 == null || bytes2.length <= 0) {
                if (length > 0) {
                    return length;
                }
                return 0;
            }
            if (bytes2.length > bArr.length - (length + i2)) {
                this.f2330d = new byte[bytes2.length - (bArr.length - (length + i2))];
                System.arraycopy(bytes2, bytes2.length - this.f2330d.length, this.f2330d, 0, this.f2330d.length);
                byte[] bArr3 = new byte[bytes2.length - this.f2330d.length];
                System.arraycopy(bytes2, 0, bArr3, 0, bArr3.length);
                bytes2 = bArr3;
            }
            System.arraycopy(bytes2, 0, bArr, length + i2, bytes2.length);
            length += bytes2.length;
        }
        return length;
    }

    private void a() throws IOException {
        if (this.f2327a == null) {
            throw new IOException("serialPort not set!");
        }
        if (!this.f2327a.isOpened()) {
            throw new IOException("serialPort " + this.f2327a.getPortName() + " not open");
        }
    }

    @Override // jssc.SerialPortEventListener
    public synchronized void serialEvent(SerialPortEvent serialPortEvent) {
        if (serialPortEvent.isRXCHAR()) {
            this.f2328b += serialPortEvent.getEventValue();
            notify();
        }
        Iterator it = this.f2332f.iterator();
        while (it.hasNext()) {
            ((SerialPortEventListener) it.next()).serialEvent(serialPortEvent);
        }
    }

    @Override // java.io.InputStream
    public int available() {
        try {
            return this.f2327a.getInputBufferBytesCount();
        } catch (SerialPortException e2) {
            Logger.getLogger(d.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return 0;
        }
    }
}
