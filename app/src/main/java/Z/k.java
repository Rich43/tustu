package z;

import G.C0132o;
import G.InterfaceC0131n;
import aP.C0406hn;
import bH.C;
import bH.C0995c;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/* loaded from: TunerStudioMS.jar:z/k.class */
public class k implements InterfaceC0131n, SerialPortEventListener {

    /* renamed from: b, reason: collision with root package name */
    private C0406hn f14121b;

    /* renamed from: c, reason: collision with root package name */
    private JTextArea f14122c;

    /* renamed from: d, reason: collision with root package name */
    private JTextArea f14123d;

    /* renamed from: e, reason: collision with root package name */
    private n f14124e;

    /* renamed from: g, reason: collision with root package name */
    private aD.d f14126g;

    /* renamed from: a, reason: collision with root package name */
    SerialPort f14128a = null;

    /* renamed from: j, reason: collision with root package name */
    private InterfaceC0131n f14130j = this;

    /* renamed from: i, reason: collision with root package name */
    private boolean f14129i = false;

    /* renamed from: f, reason: collision with root package name */
    private OutputStream f14125f;

    /* renamed from: h, reason: collision with root package name */
    private l f14127h = new l(this, this.f14125f);

    public k(C0406hn c0406hn, n nVar, JTextArea jTextArea, JTextArea jTextArea2) {
        this.f14121b = c0406hn;
        this.f14124e = nVar;
        this.f14122c = jTextArea;
        this.f14123d = jTextArea2;
    }

    public void a() throws m {
        try {
            this.f14128a = new SerialPort(this.f14124e.a());
            this.f14128a.openPort();
            try {
                b();
                try {
                    this.f14125f = new aD.e(this.f14128a);
                    this.f14126g = new aD.d(this.f14128a);
                    this.f14127h.a(this.f14125f);
                    this.f14122c.addKeyListener(this.f14127h);
                    this.f14126g.a(this);
                    this.f14129i = true;
                } catch (SerialPortException e2) {
                    Logger.getLogger(k.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    try {
                        this.f14128a.closePort();
                        throw new m("Error opening i/o streams");
                    } catch (SerialPortException e3) {
                        Logger.getLogger(k.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                    }
                }
            } catch (m e4) {
                throw e4;
            }
        } catch (Exception e5) {
            throw new m(e5.getMessage());
        }
    }

    public void b() throws m {
        if (this.f14128a == null) {
            C.b("currentPort is null, can not setConnectionParameters");
            return;
        }
        try {
            this.f14128a.setRTS(false);
            this.f14128a.setDTR(false);
        } catch (SerialPortException e2) {
            C.d("Failed to set RTS or DTR");
        }
        try {
            this.f14128a.setParams(this.f14124e.b(), this.f14124e.f(), this.f14124e.g(), this.f14124e.h());
            try {
                this.f14128a.setFlowControlMode(this.f14124e.d() | this.f14124e.e());
            } catch (SerialPortException e3) {
                throw new m("Unsupported flow control");
            }
        } catch (SerialPortException e4) {
            throw new m("Unsupported parameter");
        }
    }

    public void c() {
        if (this.f14129i) {
            this.f14122c.removeKeyListener(this.f14127h);
            if (this.f14128a != null) {
                try {
                    this.f14125f.close();
                    this.f14126g.close();
                } catch (IOException e2) {
                    System.err.println(e2);
                }
                try {
                    this.f14128a.closePort();
                } catch (SerialPortException e3) {
                    Logger.getLogger(k.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                }
            }
            this.f14129i = false;
        }
    }

    public boolean d() {
        return this.f14129i;
    }

    public void a(int i2) {
        int iB = this.f14121b.b();
        if (iB == 1) {
            this.f14123d.append(i2 + " ");
            return;
        }
        if (iB == 3) {
            if (i2 == 13) {
                i2 = 10;
            }
            this.f14123d.append(((char) i2) + "");
        } else if (iB == 2) {
            this.f14123d.append(C0995c.b((byte) i2) + " ");
        } else {
            this.f14123d.append(((char) i2) + "");
        }
    }

    public void a(int[] iArr) throws IOException {
        if (this.f14125f == null) {
            throw new IOException("Port is not open.");
        }
        if (0 != 0) {
            byte[] bArrA = C0995c.a(iArr);
            this.f14125f.write(bArrA, 0, bArrA.length);
            return;
        }
        for (int i2 : iArr) {
            this.f14125f.write(i2);
            try {
                Thread.sleep(3L);
            } catch (InterruptedException e2) {
                Logger.getLogger(k.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }

    @Override // G.InterfaceC0131n
    public void a(double d2) {
    }

    @Override // G.InterfaceC0131n
    public void a(C0132o c0132o) {
        if (c0132o.a() == 1) {
            int[] iArrE = c0132o.e();
            if (iArrE == null) {
                C.c("result == null");
                return;
            }
            for (int i2 : iArrE) {
                a(i2);
            }
            this.f14123d.append("\n");
        }
    }

    @Override // G.InterfaceC0131n
    public void e() {
    }

    @Override // jssc.SerialPortEventListener
    public void serialEvent(SerialPortEvent serialPortEvent) {
        StringBuffer stringBuffer = new StringBuffer();
        int i2 = 0;
        this.f14121b.b();
        switch (serialPortEvent.getEventType()) {
            case 1:
                try {
                    Thread.sleep(150L);
                } catch (InterruptedException e2) {
                    Logger.getLogger(k.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
                while (i2 != -1) {
                    try {
                        i2 = this.f14126g.read();
                    } catch (IOException e3) {
                        System.err.println(e3);
                    }
                    if (i2 == -1) {
                        this.f14123d.append(new String(stringBuffer) + "\n");
                        this.f14123d.setCaretPosition(this.f14123d.getText().length());
                        break;
                    } else {
                        a(i2);
                    }
                }
                this.f14123d.append(new String(stringBuffer) + "\n");
                this.f14123d.setCaretPosition(this.f14123d.getText().length());
        }
    }
}
