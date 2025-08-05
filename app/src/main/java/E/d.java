package E;

import bH.C;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:E/d.class */
final class d extends Thread {
    d(String str) {
        super(str);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        int i2 = 0;
        while (true) {
            try {
                Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                while (networkInterfaces.hasMoreElements()) {
                    NetworkInterface networkInterfaceNextElement2 = networkInterfaces.nextElement2();
                    StringBuilder sb = new StringBuilder(networkInterfaceNextElement2.getName());
                    sb.append(" - ").append(networkInterfaceNextElement2.getDisplayName()).append(": ");
                    Enumeration<InetAddress> inetAddresses = networkInterfaceNextElement2.getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        sb.append(inetAddresses.nextElement2().getHostAddress()).append(", ");
                    }
                    c cVar = (c) c.f260f.get(networkInterfaceNextElement2.getName());
                    if (networkInterfaceNextElement2.isUp() && !networkInterfaceNextElement2.isVirtual() && !networkInterfaceNextElement2.getDisplayName().toLowerCase().contains("virtual") && !networkInterfaceNextElement2.isLoopback() && cVar == null && !c.f261g.contains(networkInterfaceNextElement2.getName())) {
                        C.c("DHCP: Found NIC, Starting DHCP Server for: " + sb.toString());
                        c cVar2 = new c(networkInterfaceNextElement2, true);
                        cVar2.start();
                        c.f260f.put(networkInterfaceNextElement2.getName(), cVar2);
                    } else if (!networkInterfaceNextElement2.isUp() && cVar != null) {
                        cVar.b();
                        C.c("DHCP: NIC went down, stopping DHCP for: " + networkInterfaceNextElement2.getDisplayName());
                    } else if (cVar != null) {
                        InetAddress inetAddressA = j.a(networkInterfaceNextElement2);
                        if (!inetAddressA.getHostAddress().equals(cVar.f257d.getHostAddress())) {
                            C.c("DHCP: IP changed, resetting server socket. Expected: " + cVar.f257d.getHostAddress() + ", found" + inetAddressA.getHostAddress());
                            cVar.b();
                        }
                    }
                }
            } catch (Exception e2) {
            }
            if (c.f260f.isEmpty()) {
                int i3 = i2;
                i2++;
                if (i3 == 0) {
                    C.c("DHCP: No unserviced NIC's found, will continue to monitor.");
                }
            }
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e3) {
                Logger.getLogger(c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            }
        }
    }
}
