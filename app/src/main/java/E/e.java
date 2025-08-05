package E;

import bH.C;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:E/e.class */
public class e implements f {

    /* renamed from: b, reason: collision with root package name */
    private static Logger f263b = Logger.getLogger(e.class.getName());

    /* renamed from: a, reason: collision with root package name */
    static List f269a = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    private DatagramPacket f264c = null;

    /* renamed from: d, reason: collision with root package name */
    private b f265d = null;

    /* renamed from: e, reason: collision with root package name */
    private DatagramSocket f266e = null;

    /* renamed from: g, reason: collision with root package name */
    private InetAddress f268g = null;

    /* renamed from: f, reason: collision with root package name */
    private String f267f = j.a();

    public void a(b bVar) {
        this.f265d = bVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            bX.f fVarA = bX.f.a(this.f264c);
            bX.f fVarF = null;
            long j2 = Long.parseLong(fVarA.d(), 16);
            if (!a(j2) && (j2 < 123917681614848L || j2 > 123917681623039L)) {
                System.out.println("DHCP Request received from unserviced range, Ignoring:\n" + fVarA.d());
                return;
            }
            boolean z2 = this.f265d.b().e().startsWith("169.") || this.f268g.getAddress()[3] == 1;
            if (fVarA.n()) {
                Byte bW2 = fVarA.w();
                if (fVarA.o() == 1) {
                    switch (bW2.byteValue()) {
                        case 1:
                            if (!a(j2) && z2) {
                                C.c("DHCP: " + ((Object) this.f268g) + " Discover received from: " + fVarA.d());
                                fVarF = this.f265d.a(fVarA);
                                break;
                            } else {
                                C.c("DHCP: " + ((Object) this.f268g) + " Discover received by local or offer Not Allowed, ignoring: " + fVarA.d());
                                return;
                            }
                            break;
                        case 2:
                        case 5:
                        case 6:
                        default:
                            f263b.info("Unsupported message type " + ((Object) bW2));
                            break;
                        case 3:
                            if (!z2) {
                                C.c("DHCP: " + ((Object) this.f268g) + " Request received, ignoring not ready for offer: " + fVarA.d());
                                return;
                            } else if (!a(j2)) {
                                C.c("DHCP: " + ((Object) this.f268g) + " Request received from: " + fVarA.d());
                                fVarF = this.f265d.b(fVarA);
                                break;
                            } else {
                                C.c("DHCP: " + ((Object) this.f268g) + " Request received from local MAC, Declining " + fVarA.d());
                                fVarF = this.f265d.c(fVarA);
                                break;
                            }
                        case 4:
                            C.c("DHCP: " + ((Object) this.f268g) + " Decline received from: " + fVarA.d());
                            fVarF = this.f265d.e(fVarA);
                            break;
                        case 7:
                            C.c("DHCP: " + ((Object) this.f268g) + " Release received from: " + fVarA.d());
                            fVarF = this.f265d.f(fVarA);
                            break;
                        case 8:
                            C.c("DHCP: " + ((Object) this.f268g) + " Inform received from: " + fVarA.d());
                            fVarF = this.f265d.d(fVarA);
                            break;
                    }
                }
                if (fVarF != null) {
                    InetAddress inetAddressY = fVarF.y();
                    int iZ = fVarF.z();
                    byte[] bArrB = fVarF.b();
                    DatagramPacket datagramPacket = new DatagramPacket(bArrB, bArrB.length, inetAddressY, iZ);
                    C.c("DHCP: " + ((Object) this.f268g) + " Send Datagram to " + ((Object) datagramPacket.getAddress()) + " Assign IP " + ((Object) fVarF.u()) + " Server IP " + ((Object) fVarF.p()) + " Lease time " + ((Object) fVarF.f((byte) 51)));
                    this.f266e.send(datagramPacket);
                }
                if (a(j2)) {
                }
            }
        } catch (Exception e2) {
            C.a("Failed to send response from: " + ((Object) this.f268g));
            throw new RuntimeException(e2);
        }
    }

    public void a(DatagramPacket datagramPacket) {
        this.f264c = datagramPacket;
    }

    public void a(DatagramSocket datagramSocket) {
        this.f266e = datagramSocket;
    }

    protected DatagramSocket a() {
        if (this.f266e != null) {
            this.f266e.close();
        }
        try {
            Thread.sleep(20L);
            DatagramSocket datagramSocket = new DatagramSocket(0, this.f268g);
            datagramSocket.setBroadcast(true);
            return datagramSocket;
        } catch (InterruptedException e2) {
            Logger.getLogger(e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return null;
        } catch (SocketException e3) {
            e3.printStackTrace();
            return null;
        }
    }

    public void a(InetAddress inetAddress, NetworkInterface networkInterface) {
        C.d("Setting Local Address to: " + ((Object) inetAddress));
        this.f268g = inetAddress;
        f269a.add(Long.valueOf(j.b(networkInterface)));
        this.f265d.a(inetAddress);
        if (inetAddress == null) {
            C.c("null localAddress");
        }
    }

    private boolean a(long j2) {
        Iterator it = f269a.iterator();
        while (it.hasNext()) {
            if (((Long) it.next()).longValue() == j2) {
                return true;
            }
        }
        return false;
    }
}
