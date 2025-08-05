package sun.management.jdp;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/* loaded from: rt.jar:sun/management/jdp/JdpJmxPacket.class */
public final class JdpJmxPacket extends JdpGenericPacket implements JdpPacket {
    public static final String UUID_KEY = "DISCOVERABLE_SESSION_UUID";
    public static final String MAIN_CLASS_KEY = "MAIN_CLASS";
    public static final String JMX_SERVICE_URL_KEY = "JMX_SERVICE_URL";
    public static final String INSTANCE_NAME_KEY = "INSTANCE_NAME";
    public static final String PROCESS_ID_KEY = "PROCESS_ID";
    public static final String RMI_HOSTNAME_KEY = "RMI_HOSTNAME";
    public static final String BROADCAST_INTERVAL_KEY = "BROADCAST_INTERVAL";
    private UUID id;
    private String mainClass;
    private String jmxServiceUrl;
    private String instanceName;
    private String processId;
    private String rmiHostname;
    private String broadcastInterval;

    public JdpJmxPacket(UUID uuid, String str) {
        this.id = uuid;
        this.jmxServiceUrl = str;
    }

    public JdpJmxPacket(byte[] bArr) throws JdpException {
        Map<String, String> discoveryDataAsMap = new JdpPacketReader(bArr).getDiscoveryDataAsMap();
        String str = discoveryDataAsMap.get(UUID_KEY);
        this.id = str == null ? null : UUID.fromString(str);
        this.jmxServiceUrl = discoveryDataAsMap.get(JMX_SERVICE_URL_KEY);
        this.mainClass = discoveryDataAsMap.get(MAIN_CLASS_KEY);
        this.instanceName = discoveryDataAsMap.get(INSTANCE_NAME_KEY);
        this.processId = discoveryDataAsMap.get(PROCESS_ID_KEY);
        this.rmiHostname = discoveryDataAsMap.get(RMI_HOSTNAME_KEY);
        this.broadcastInterval = discoveryDataAsMap.get(BROADCAST_INTERVAL_KEY);
    }

    public void setMainClass(String str) {
        this.mainClass = str;
    }

    public void setInstanceName(String str) {
        this.instanceName = str;
    }

    public UUID getId() {
        return this.id;
    }

    public String getMainClass() {
        return this.mainClass;
    }

    public String getJmxServiceUrl() {
        return this.jmxServiceUrl;
    }

    public String getInstanceName() {
        return this.instanceName;
    }

    public String getProcessId() {
        return this.processId;
    }

    public void setProcessId(String str) {
        this.processId = str;
    }

    public String getRmiHostname() {
        return this.rmiHostname;
    }

    public void setRmiHostname(String str) {
        this.rmiHostname = str;
    }

    public String getBroadcastInterval() {
        return this.broadcastInterval;
    }

    public void setBroadcastInterval(String str) {
        this.broadcastInterval = str;
    }

    @Override // sun.management.jdp.JdpPacket
    public byte[] getPacketData() throws IOException {
        JdpPacketWriter jdpPacketWriter = new JdpPacketWriter();
        jdpPacketWriter.addEntry(UUID_KEY, this.id == null ? null : this.id.toString());
        jdpPacketWriter.addEntry(MAIN_CLASS_KEY, this.mainClass);
        jdpPacketWriter.addEntry(JMX_SERVICE_URL_KEY, this.jmxServiceUrl);
        jdpPacketWriter.addEntry(INSTANCE_NAME_KEY, this.instanceName);
        jdpPacketWriter.addEntry(PROCESS_ID_KEY, this.processId);
        jdpPacketWriter.addEntry(RMI_HOSTNAME_KEY, this.rmiHostname);
        jdpPacketWriter.addEntry(BROADCAST_INTERVAL_KEY, this.broadcastInterval);
        return jdpPacketWriter.getPacketBytes();
    }

    public int hashCode() {
        return (((1 * 31) + this.id.hashCode()) * 31) + this.jmxServiceUrl.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof JdpJmxPacket)) {
            return false;
        }
        JdpJmxPacket jdpJmxPacket = (JdpJmxPacket) obj;
        return Objects.equals(this.id, jdpJmxPacket.getId()) && Objects.equals(this.jmxServiceUrl, jdpJmxPacket.getJmxServiceUrl());
    }
}
