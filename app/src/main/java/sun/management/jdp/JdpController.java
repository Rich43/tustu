package sun.management.jdp;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;
import sun.management.VMManagement;

/* loaded from: rt.jar:sun/management/jdp/JdpController.class */
public final class JdpController {
    private static JDPControllerRunner controller = null;

    /* loaded from: rt.jar:sun/management/jdp/JdpController$JDPControllerRunner.class */
    private static class JDPControllerRunner implements Runnable {
        private final JdpJmxPacket packet;
        private final JdpBroadcaster bcast;
        private final int pause;
        private volatile boolean shutdown;

        private JDPControllerRunner(JdpBroadcaster jdpBroadcaster, JdpJmxPacket jdpJmxPacket, int i2) {
            this.shutdown = false;
            this.bcast = jdpBroadcaster;
            this.packet = jdpJmxPacket;
            this.pause = i2;
        }

        @Override // java.lang.Runnable
        public void run() {
            while (!this.shutdown) {
                try {
                    this.bcast.sendPacket(this.packet);
                    try {
                        Thread.sleep(this.pause);
                    } catch (InterruptedException e2) {
                    }
                } catch (IOException e3) {
                }
            }
            try {
                stop();
                this.bcast.shutdown();
            } catch (IOException e4) {
            }
        }

        public void stop() {
            this.shutdown = true;
        }
    }

    private JdpController() {
    }

    private static int getInteger(String str, int i2, String str2) throws JdpException {
        if (str == null) {
            return i2;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e2) {
            throw new JdpException(str2);
        }
    }

    private static InetAddress getInetAddress(String str, InetAddress inetAddress, String str2) throws JdpException {
        if (str == null) {
            return inetAddress;
        }
        try {
            return InetAddress.getByName(str);
        } catch (UnknownHostException e2) {
            throw new JdpException(str2);
        }
    }

    private static Integer getProcessId() {
        try {
            RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
            Field declaredField = runtimeMXBean.getClass().getDeclaredField("jvm");
            declaredField.setAccessible(true);
            VMManagement vMManagement = (VMManagement) declaredField.get(runtimeMXBean);
            Method declaredMethod = vMManagement.getClass().getDeclaredMethod("getProcessId", new Class[0]);
            declaredMethod.setAccessible(true);
            return (Integer) declaredMethod.invoke(vMManagement, new Object[0]);
        } catch (Exception e2) {
            return null;
        }
    }

    public static synchronized void startDiscoveryService(InetAddress inetAddress, int i2, String str, String str2) throws JdpException, IOException {
        int integer = getInteger(System.getProperty("com.sun.management.jdp.ttl"), 1, "Invalid jdp packet ttl");
        int integer2 = getInteger(System.getProperty("com.sun.management.jdp.pause"), 5, "Invalid jdp pause") * 1000;
        InetAddress inetAddress2 = getInetAddress(System.getProperty("com.sun.management.jdp.source_addr"), null, "Invalid source address provided");
        JdpJmxPacket jdpJmxPacket = new JdpJmxPacket(UUID.randomUUID(), str2);
        String property = System.getProperty("sun.java.command");
        if (property != null) {
            jdpJmxPacket.setMainClass(property.split(" ", 2)[0]);
        }
        jdpJmxPacket.setInstanceName(str);
        jdpJmxPacket.setRmiHostname(System.getProperty("java.rmi.server.hostname"));
        jdpJmxPacket.setBroadcastInterval(new Integer(integer2).toString());
        Integer processId = getProcessId();
        if (processId != null) {
            jdpJmxPacket.setProcessId(processId.toString());
        }
        JdpBroadcaster jdpBroadcaster = new JdpBroadcaster(inetAddress, inetAddress2, i2, integer);
        stopDiscoveryService();
        controller = new JDPControllerRunner(jdpBroadcaster, jdpJmxPacket, integer2);
        Thread thread = new Thread(controller, "JDP broadcaster");
        thread.setDaemon(true);
        thread.start();
    }

    public static synchronized void stopDiscoveryService() {
        if (controller != null) {
            controller.stop();
            controller = null;
        }
    }
}
