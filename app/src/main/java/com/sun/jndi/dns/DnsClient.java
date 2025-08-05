package com.sun.jndi.dns;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.naming.CommunicationException;
import javax.naming.ConfigurationException;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;
import javax.naming.ServiceUnavailableException;
import sun.security.jca.JCAUtil;

/* loaded from: rt.jar:com/sun/jndi/dns/DnsClient.class */
public class DnsClient {
    private static final int IDENT_OFFSET = 0;
    private static final int FLAGS_OFFSET = 2;
    private static final int NUMQ_OFFSET = 4;
    private static final int NUMANS_OFFSET = 6;
    private static final int NUMAUTH_OFFSET = 8;
    private static final int NUMADD_OFFSET = 10;
    private static final int DNS_HDR_SIZE = 12;
    private static final int NO_ERROR = 0;
    private static final int FORMAT_ERROR = 1;
    private static final int SERVER_FAILURE = 2;
    private static final int NAME_ERROR = 3;
    private static final int NOT_IMPL = 4;
    private static final int REFUSED = 5;
    private static final int DEFAULT_PORT = 53;
    private static final int TRANSACTION_ID_BOUND = 65536;
    private InetAddress[] servers;
    private int[] serverPorts;
    private int timeout;
    private int retries;
    private Map<Integer, ResourceRecord> reqs;
    private Map<Integer, byte[]> resps;
    private static final boolean debug = false;
    private static final String[] rcodeDescription = {"No error", "DNS format error", "DNS server failure", "DNS name not found", "DNS operation not supported", "DNS service refused"};
    private static final SecureRandom random = JCAUtil.getSecureRandom();
    private static final DNSDatagramSocketFactory factory = new DNSDatagramSocketFactory(random);
    private final Object udpSocketLock = new Object();
    private Object queuesLock = new Object();

    public DnsClient(String[] strArr, int i2, int i3) throws NamingException {
        this.timeout = i2;
        this.retries = i3;
        this.servers = new InetAddress[strArr.length];
        this.serverPorts = new int[strArr.length];
        for (int i4 = 0; i4 < strArr.length; i4++) {
            int iIndexOf = strArr[i4].indexOf(58, strArr[i4].indexOf(93) + 1);
            this.serverPorts[i4] = iIndexOf < 0 ? 53 : Integer.parseInt(strArr[i4].substring(iIndexOf + 1));
            String strSubstring = iIndexOf < 0 ? strArr[i4] : strArr[i4].substring(0, iIndexOf);
            try {
                this.servers[i4] = InetAddress.getByName(strSubstring);
            } catch (UnknownHostException e2) {
                ConfigurationException configurationException = new ConfigurationException("Unknown DNS server: " + strSubstring);
                configurationException.setRootCause(e2);
                throw configurationException;
            }
        }
        this.reqs = Collections.synchronizedMap(new HashMap());
        this.resps = Collections.synchronizedMap(new HashMap());
    }

    DatagramSocket getDatagramSocket() throws NamingException {
        try {
            return factory.open();
        } catch (SocketException e2) {
            ConfigurationException configurationException = new ConfigurationException();
            configurationException.setRootCause(e2);
            throw configurationException;
        }
    }

    protected void finalize() {
        close();
    }

    public void close() {
        synchronized (this.queuesLock) {
            this.reqs.clear();
            this.resps.clear();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x00b5 A[Catch: IOException -> 0x01b6, NameNotFoundException -> 0x01da, CommunicationException -> 0x01df, NamingException -> 0x01ed, all -> 0x021c, PHI: r23
  0x00b5: PHI (r23v6 byte[]) = (r23v5 byte[]), (r23v9 byte[]) binds: [B:15:0x0093, B:20:0x00af] A[DONT_GENERATE, DONT_INLINE], TRY_ENTER, TryCatch #5 {NamingException -> 0x01ed, blocks: (B:14:0x0074, B:16:0x0096, B:18:0x00a2, B:22:0x00b5, B:24:0x00c8, B:26:0x00d0, B:27:0x00e4, B:30:0x00ef, B:32:0x00f9, B:53:0x018d, B:35:0x0111, B:36:0x0128, B:37:0x0134, B:42:0x0144, B:44:0x015a, B:45:0x0163, B:46:0x0164, B:48:0x016f, B:51:0x0182, B:40:0x013e, B:41:0x0143, B:54:0x0193), top: B:96:0x0074, outer: #4 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    com.sun.jndi.dns.ResourceRecords query(com.sun.jndi.dns.DnsName r11, int r12, int r13, boolean r14, boolean r15) throws javax.naming.NamingException {
        /*
            Method dump skipped, instructions count: 595
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.jndi.dns.DnsClient.query(com.sun.jndi.dns.DnsName, int, int, boolean, boolean):com.sun.jndi.dns.ResourceRecords");
    }

    ResourceRecords queryZone(DnsName dnsName, int i2, boolean z2) throws NamingException {
        Packet packetMakeQueryPacket = makeQueryPacket(dnsName, random.nextInt(65536), i2, 252, z2);
        Throwable th = null;
        for (int i3 = 0; i3 < this.servers.length; i3++) {
            try {
                Tcp tcp = new Tcp(this.servers[i3], this.serverPorts[i3]);
                try {
                    byte[] bArrDoTcpQuery = doTcpQuery(tcp, packetMakeQueryPacket);
                    Header header = new Header(bArrDoTcpQuery, bArrDoTcpQuery.length);
                    checkResponseCode(header);
                    ResourceRecords resourceRecords = new ResourceRecords(bArrDoTcpQuery, bArrDoTcpQuery.length, header, true);
                    if (resourceRecords.getFirstAnsType() != 6) {
                        throw new CommunicationException("DNS error: zone xfer doesn't begin with SOA");
                    }
                    if (resourceRecords.answer.size() == 1 || resourceRecords.getLastAnsType() != 6) {
                        do {
                            byte[] bArrContinueTcpQuery = continueTcpQuery(tcp);
                            if (bArrContinueTcpQuery == null) {
                                throw new CommunicationException("DNS error: incomplete zone transfer");
                            }
                            Header header2 = new Header(bArrContinueTcpQuery, bArrContinueTcpQuery.length);
                            checkResponseCode(header2);
                            resourceRecords.add(bArrContinueTcpQuery, bArrContinueTcpQuery.length, header2);
                        } while (resourceRecords.getLastAnsType() != 6);
                    }
                    resourceRecords.answer.removeElementAt(resourceRecords.answer.size() - 1);
                    tcp.close();
                    return resourceRecords;
                } catch (Throwable th2) {
                    tcp.close();
                    throw th2;
                }
            } catch (IOException e2) {
                e = e2;
                th = e;
            } catch (NameNotFoundException e3) {
                throw e3;
            } catch (NamingException e4) {
                e = e4;
                th = e;
            }
        }
        if (th instanceof NamingException) {
            throw ((NamingException) th);
        }
        CommunicationException communicationException = new CommunicationException("DNS error during zone transfer");
        communicationException.setRootCause(th);
        throw communicationException;
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x008e, code lost:
    
        if (r0 == null) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0093, code lost:
    
        if (0 == 0) goto L17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0096, code lost:
    
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x009e, code lost:
    
        r28 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x00a0, code lost:
    
        r0.addSuppressed(r28);
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x00aa, code lost:
    
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x00c9, code lost:
    
        r0.disconnect();
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00de, code lost:
    
        if (r0 == null) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00e3, code lost:
    
        if (0 == 0) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00e6, code lost:
    
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00ee, code lost:
    
        r21 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00f0, code lost:
    
        r0.addSuppressed(r21);
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00fa, code lost:
    
        r0.close();
     */
    /* JADX WARN: Failed to calculate best type for var: r15v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r16v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.RegisterArg.getSVar()" because the return value of "jadx.core.dex.nodes.InsnNode.getResult()" is null
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.collectRelatedVars(AbstractTypeConstraint.java:31)
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.<init>(AbstractTypeConstraint.java:19)
    	at jadx.core.dex.visitors.typeinference.TypeSearch$1.<init>(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeMoveConstraint(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeConstraint(TypeSearch.java:361)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.collectConstraints(TypeSearch.java:341)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:60)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.runMultiVariableSearch(FixTypesVisitor.java:116)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:91)
     */
    /* JADX WARN: Not initialized variable reg: 15, insn: 0x0110: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r15 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) A[TRY_LEAVE], block:B:46:0x0110 */
    /* JADX WARN: Not initialized variable reg: 16, insn: 0x0115: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r16 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:48:0x0115 */
    /* JADX WARN: Type inference failed for: r15v0, types: [java.net.DatagramSocket] */
    /* JADX WARN: Type inference failed for: r16v0, types: [java.lang.Throwable] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private byte[] doUdpQuery(com.sun.jndi.dns.Packet r8, java.net.InetAddress r9, int r10, int r11, int r12) throws java.io.IOException, javax.naming.NamingException {
        /*
            Method dump skipped, instructions count: 318
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.jndi.dns.DnsClient.doUdpQuery(com.sun.jndi.dns.Packet, java.net.InetAddress, int, int, int):byte[]");
    }

    private byte[] doTcpQuery(Tcp tcp, Packet packet) throws IOException {
        int length = packet.length();
        tcp.out.write(length >> 8);
        tcp.out.write(length);
        tcp.out.write(packet.getData(), 0, length);
        tcp.out.flush();
        byte[] bArrContinueTcpQuery = continueTcpQuery(tcp);
        if (bArrContinueTcpQuery == null) {
            throw new IOException("DNS error: no response");
        }
        return bArrContinueTcpQuery;
    }

    private byte[] continueTcpQuery(Tcp tcp) throws IOException {
        int i2 = tcp.in.read();
        if (i2 == -1) {
            return null;
        }
        int i3 = tcp.in.read();
        if (i3 == -1) {
            throw new IOException("Corrupted DNS response: bad length");
        }
        int i4 = (i2 << 8) | i3;
        byte[] bArr = new byte[i4];
        int i5 = 0;
        while (true) {
            int i6 = i5;
            if (i4 > 0) {
                int i7 = tcp.in.read(bArr, i6, i4);
                if (i7 == -1) {
                    throw new IOException("Corrupted DNS response: too little data");
                }
                i4 -= i7;
                i5 = i6 + i7;
            } else {
                return bArr;
            }
        }
    }

    private Packet makeQueryPacket(DnsName dnsName, int i2, int i3, int i4, boolean z2) {
        short octets = dnsName.getOctets();
        Packet packet = new Packet(12 + octets + 4);
        int i5 = z2 ? 256 : 0;
        packet.putShort(i2, 0);
        packet.putShort(i5, 2);
        packet.putShort(1, 4);
        packet.putShort(0, 6);
        packet.putInt(0, 8);
        makeQueryName(dnsName, packet, 12);
        packet.putShort(i4, 12 + octets);
        packet.putShort(i3, 12 + octets + 2);
        return packet;
    }

    private void makeQueryName(DnsName dnsName, Packet packet, int i2) {
        for (int size = dnsName.size() - 1; size >= 0; size--) {
            String str = dnsName.get(size);
            int length = str.length();
            int i3 = i2;
            i2++;
            packet.putByte(length, i3);
            for (int i4 = 0; i4 < length; i4++) {
                int i5 = i2;
                i2++;
                packet.putByte(str.charAt(i4), i5);
            }
        }
        if (!dnsName.hasRootLabel()) {
            packet.putByte(0, i2);
        }
    }

    private byte[] lookupResponse(Integer num) throws NamingException {
        byte[] bArr = this.resps.get(num);
        if (bArr != null) {
            checkResponseCode(new Header(bArr, bArr.length));
            synchronized (this.queuesLock) {
                this.resps.remove(num);
                this.reqs.remove(num);
            }
        }
        return bArr;
    }

    private boolean isMatchResponse(byte[] bArr, int i2) throws NamingException {
        Header header = new Header(bArr, bArr.length);
        if (header.query) {
            throw new CommunicationException("DNS error: expecting response");
        }
        if (!this.reqs.containsKey(Integer.valueOf(i2))) {
            return false;
        }
        if (header.xid == i2) {
            checkResponseCode(header);
            if (!header.query && header.numQuestions == 1) {
                ResourceRecord resourceRecord = new ResourceRecord(bArr, bArr.length, 12, true, false);
                ResourceRecord resourceRecord2 = this.reqs.get(Integer.valueOf(i2));
                int type = resourceRecord2.getType();
                int rrclass = resourceRecord2.getRrclass();
                DnsName name = resourceRecord2.getName();
                if (type == 255 || type == resourceRecord.getType()) {
                    if ((rrclass == 255 || rrclass == resourceRecord.getRrclass()) && name.equals(resourceRecord.getName())) {
                        synchronized (this.queuesLock) {
                            this.resps.remove(Integer.valueOf(i2));
                            this.reqs.remove(Integer.valueOf(i2));
                        }
                        return true;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        synchronized (this.queuesLock) {
            if (this.reqs.containsKey(Integer.valueOf(header.xid))) {
                this.resps.put(Integer.valueOf(header.xid), bArr);
            }
        }
        return false;
    }

    private void checkResponseCode(Header header) throws NamingException {
        int i2 = header.rcode;
        if (i2 == 0) {
            return;
        }
        String str = (i2 < rcodeDescription.length ? rcodeDescription[i2] : "DNS error") + " [response code " + i2 + "]";
        switch (i2) {
            case 1:
            default:
                throw new NamingException(str);
            case 2:
                throw new ServiceUnavailableException(str);
            case 3:
                throw new NameNotFoundException(str);
            case 4:
            case 5:
                throw new OperationNotSupportedException(str);
        }
    }

    private static void dprint(String str) {
    }
}
