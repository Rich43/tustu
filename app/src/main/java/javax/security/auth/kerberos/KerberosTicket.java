package javax.security.auth.kerberos;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import javax.crypto.SecretKey;
import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;
import javax.security.auth.RefreshFailedException;
import javax.security.auth.Refreshable;
import sun.misc.HexDumpEncoder;
import sun.security.krb5.Credentials;
import sun.security.krb5.KrbException;

/* loaded from: rt.jar:javax/security/auth/kerberos/KerberosTicket.class */
public class KerberosTicket implements Destroyable, Refreshable, Serializable {
    private static final long serialVersionUID = 7395334370157380539L;
    private static final int FORWARDABLE_TICKET_FLAG = 1;
    private static final int FORWARDED_TICKET_FLAG = 2;
    private static final int PROXIABLE_TICKET_FLAG = 3;
    private static final int PROXY_TICKET_FLAG = 4;
    private static final int POSTDATED_TICKET_FLAG = 6;
    private static final int RENEWABLE_TICKET_FLAG = 8;
    private static final int INITIAL_TICKET_FLAG = 9;
    private static final int NUM_FLAGS = 32;
    private byte[] asn1Encoding;
    private KeyImpl sessionKey;
    private boolean[] flags;
    private Date authTime;
    private Date startTime;
    private Date endTime;
    private Date renewTill;
    private KerberosPrincipal client;
    private KerberosPrincipal server;
    private InetAddress[] clientAddresses;
    transient KerberosPrincipal clientAlias = null;
    transient KerberosPrincipal serverAlias = null;
    KerberosTicket proxy = null;
    private transient boolean destroyed = false;

    public KerberosTicket(byte[] bArr, KerberosPrincipal kerberosPrincipal, KerberosPrincipal kerberosPrincipal2, byte[] bArr2, int i2, boolean[] zArr, Date date, Date date2, Date date3, Date date4, InetAddress[] inetAddressArr) {
        init(bArr, kerberosPrincipal, kerberosPrincipal2, bArr2, i2, zArr, date, date2, date3, date4, inetAddressArr);
    }

    private void init(byte[] bArr, KerberosPrincipal kerberosPrincipal, KerberosPrincipal kerberosPrincipal2, byte[] bArr2, int i2, boolean[] zArr, Date date, Date date2, Date date3, Date date4, InetAddress[] inetAddressArr) {
        if (bArr2 == null) {
            throw new IllegalArgumentException("Session key for ticket cannot be null");
        }
        init(bArr, kerberosPrincipal, kerberosPrincipal2, new KeyImpl(bArr2, i2), zArr, date, date2, date3, date4, inetAddressArr);
    }

    private void init(byte[] bArr, KerberosPrincipal kerberosPrincipal, KerberosPrincipal kerberosPrincipal2, KeyImpl keyImpl, boolean[] zArr, Date date, Date date2, Date date3, Date date4, InetAddress[] inetAddressArr) {
        if (bArr == null) {
            throw new IllegalArgumentException("ASN.1 encoding of ticket cannot be null");
        }
        this.asn1Encoding = (byte[]) bArr.clone();
        if (kerberosPrincipal == null) {
            throw new IllegalArgumentException("Client name in ticket cannot be null");
        }
        this.client = kerberosPrincipal;
        if (kerberosPrincipal2 == null) {
            throw new IllegalArgumentException("Server name in ticket cannot be null");
        }
        this.server = kerberosPrincipal2;
        this.sessionKey = keyImpl;
        if (zArr != null) {
            if (zArr.length >= 32) {
                this.flags = (boolean[]) zArr.clone();
            } else {
                this.flags = new boolean[32];
                for (int i2 = 0; i2 < zArr.length; i2++) {
                    this.flags[i2] = zArr[i2];
                }
            }
        } else {
            this.flags = new boolean[32];
        }
        if (this.flags[8] && date4 != null) {
            this.renewTill = new Date(date4.getTime());
        }
        if (date != null) {
            this.authTime = new Date(date.getTime());
        }
        if (date2 != null) {
            this.startTime = new Date(date2.getTime());
        } else {
            this.startTime = this.authTime;
        }
        if (date3 == null) {
            throw new IllegalArgumentException("End time for ticket validity cannot be null");
        }
        this.endTime = new Date(date3.getTime());
        if (inetAddressArr != null) {
            this.clientAddresses = (InetAddress[]) inetAddressArr.clone();
        }
    }

    public final KerberosPrincipal getClient() {
        return this.client;
    }

    public final KerberosPrincipal getServer() {
        return this.server;
    }

    public final SecretKey getSessionKey() {
        if (this.destroyed) {
            throw new IllegalStateException("This ticket is no longer valid");
        }
        return this.sessionKey;
    }

    public final int getSessionKeyType() {
        if (this.destroyed) {
            throw new IllegalStateException("This ticket is no longer valid");
        }
        return this.sessionKey.getKeyType();
    }

    public final boolean isForwardable() {
        if (this.flags == null) {
            return false;
        }
        return this.flags[1];
    }

    public final boolean isForwarded() {
        if (this.flags == null) {
            return false;
        }
        return this.flags[2];
    }

    public final boolean isProxiable() {
        if (this.flags == null) {
            return false;
        }
        return this.flags[3];
    }

    public final boolean isProxy() {
        if (this.flags == null) {
            return false;
        }
        return this.flags[4];
    }

    public final boolean isPostdated() {
        if (this.flags == null) {
            return false;
        }
        return this.flags[6];
    }

    public final boolean isRenewable() {
        if (this.flags == null) {
            return false;
        }
        return this.flags[8];
    }

    public final boolean isInitial() {
        if (this.flags == null) {
            return false;
        }
        return this.flags[9];
    }

    public final boolean[] getFlags() {
        if (this.flags == null) {
            return null;
        }
        return (boolean[]) this.flags.clone();
    }

    public final Date getAuthTime() {
        if (this.authTime == null) {
            return null;
        }
        return (Date) this.authTime.clone();
    }

    public final Date getStartTime() {
        if (this.startTime == null) {
            return null;
        }
        return (Date) this.startTime.clone();
    }

    public final Date getEndTime() {
        if (this.endTime == null) {
            return null;
        }
        return (Date) this.endTime.clone();
    }

    public final Date getRenewTill() {
        if (this.renewTill == null) {
            return null;
        }
        return (Date) this.renewTill.clone();
    }

    public final InetAddress[] getClientAddresses() {
        if (this.clientAddresses == null) {
            return null;
        }
        return (InetAddress[]) this.clientAddresses.clone();
    }

    public final byte[] getEncoded() {
        if (this.destroyed) {
            throw new IllegalStateException("This ticket is no longer valid");
        }
        return (byte[]) this.asn1Encoding.clone();
    }

    @Override // javax.security.auth.Refreshable
    public boolean isCurrent() {
        return this.endTime != null && System.currentTimeMillis() <= this.endTime.getTime();
    }

    @Override // javax.security.auth.Refreshable
    public void refresh() throws RefreshFailedException, ArrayIndexOutOfBoundsException {
        if (this.destroyed) {
            throw new RefreshFailedException("A destroyed ticket cannot be renewd.");
        }
        if (!isRenewable()) {
            throw new RefreshFailedException("This ticket is not renewable");
        }
        if (getRenewTill() == null) {
            return;
        }
        if (System.currentTimeMillis() > getRenewTill().getTime()) {
            throw new RefreshFailedException("This ticket is past its last renewal time.");
        }
        Throwable th = null;
        Credentials credentialsRenew = null;
        try {
            credentialsRenew = new Credentials(this.asn1Encoding, this.client.toString(), this.clientAlias != null ? this.clientAlias.getName() : null, this.server.toString(), this.serverAlias != null ? this.serverAlias.getName() : null, this.sessionKey.getEncoded(), this.sessionKey.getKeyType(), this.flags, this.authTime, this.startTime, this.endTime, this.renewTill, this.clientAddresses).renew();
        } catch (IOException e2) {
            th = e2;
        } catch (KrbException e3) {
            th = e3;
        }
        if (th != null) {
            RefreshFailedException refreshFailedException = new RefreshFailedException("Failed to renew Kerberos Ticket for client " + ((Object) this.client) + " and server " + ((Object) this.server) + " - " + th.getMessage());
            refreshFailedException.initCause(th);
            throw refreshFailedException;
        }
        synchronized (this) {
            try {
                destroy();
            } catch (DestroyFailedException e4) {
            }
            init(credentialsRenew.getEncoded(), new KerberosPrincipal(credentialsRenew.getClient().getName()), new KerberosPrincipal(credentialsRenew.getServer().getName(), 2), credentialsRenew.getSessionKey().getBytes(), credentialsRenew.getSessionKey().getEType(), credentialsRenew.getFlags(), credentialsRenew.getAuthTime(), credentialsRenew.getStartTime(), credentialsRenew.getEndTime(), credentialsRenew.getRenewTill(), credentialsRenew.getClientAddresses());
            this.destroyed = false;
        }
    }

    @Override // javax.security.auth.Destroyable
    public void destroy() throws DestroyFailedException {
        if (!this.destroyed) {
            Arrays.fill(this.asn1Encoding, (byte) 0);
            this.client = null;
            this.server = null;
            this.sessionKey.destroy();
            this.flags = null;
            this.authTime = null;
            this.startTime = null;
            this.endTime = null;
            this.renewTill = null;
            this.clientAddresses = null;
            this.destroyed = true;
        }
    }

    @Override // javax.security.auth.Destroyable
    public boolean isDestroyed() {
        return this.destroyed;
    }

    public String toString() {
        if (this.destroyed) {
            return "Destroyed KerberosTicket";
        }
        StringBuffer stringBuffer = new StringBuffer();
        if (this.clientAddresses != null) {
            for (int i2 = 0; i2 < this.clientAddresses.length; i2++) {
                stringBuffer.append("clientAddresses[" + i2 + "] = " + this.clientAddresses[i2].toString());
            }
        }
        return new StringBuilder().append("Ticket (hex) = \n").append(new HexDumpEncoder().encodeBuffer(this.asn1Encoding)).append("\nClient Principal = ").append(this.client.toString()).append("\nServer Principal = ").append(this.server.toString()).append("\nSession Key = ").append(this.sessionKey.toString()).append("\nForwardable Ticket ").append(this.flags[1]).append("\nForwarded Ticket ").append(this.flags[2]).append("\nProxiable Ticket ").append(this.flags[3]).append("\nProxy Ticket ").append(this.flags[4]).append("\nPostdated Ticket ").append(this.flags[6]).append("\nRenewable Ticket ").append(this.flags[8]).append("\nInitial Ticket ").append(this.flags[8]).append("\nAuth Time = ").append(String.valueOf(this.authTime)).append("\nStart Time = ").append(String.valueOf(this.startTime)).append("\nEnd Time = ").append(this.endTime.toString()).append("\nRenew Till = ").append(String.valueOf(this.renewTill)).append("\nClient Addresses ").append(this.clientAddresses == null ? " Null " : stringBuffer.toString() + (this.proxy == null ? "" : "\nwith a proxy ticket") + "\n").toString();
    }

    public int hashCode() {
        if (isDestroyed()) {
            return 17;
        }
        int iHashCode = (((((((((17 * 37) + Arrays.hashCode(getEncoded())) * 37) + this.endTime.hashCode()) * 37) + this.client.hashCode()) * 37) + this.server.hashCode()) * 37) + this.sessionKey.hashCode();
        if (this.authTime != null) {
            iHashCode = (iHashCode * 37) + this.authTime.hashCode();
        }
        if (this.startTime != null) {
            iHashCode = (iHashCode * 37) + this.startTime.hashCode();
        }
        if (this.renewTill != null) {
            iHashCode = (iHashCode * 37) + this.renewTill.hashCode();
        }
        int iHashCode2 = (iHashCode * 37) + Arrays.hashCode(this.clientAddresses);
        if (this.proxy != null) {
            iHashCode2 = (iHashCode2 * 37) + this.proxy.hashCode();
        }
        return (iHashCode2 * 37) + Arrays.hashCode(this.flags);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof KerberosTicket)) {
            return false;
        }
        KerberosTicket kerberosTicket = (KerberosTicket) obj;
        if (isDestroyed() || kerberosTicket.isDestroyed() || !Arrays.equals(getEncoded(), kerberosTicket.getEncoded()) || !this.endTime.equals(kerberosTicket.getEndTime()) || !this.server.equals(kerberosTicket.getServer()) || !this.client.equals(kerberosTicket.getClient()) || !this.sessionKey.equals(kerberosTicket.getSessionKey()) || !Arrays.equals(this.clientAddresses, kerberosTicket.getClientAddresses()) || !Arrays.equals(this.flags, kerberosTicket.getFlags())) {
            return false;
        }
        if (this.authTime == null) {
            if (kerberosTicket.getAuthTime() != null) {
                return false;
            }
        } else if (!this.authTime.equals(kerberosTicket.getAuthTime())) {
            return false;
        }
        if (this.startTime == null) {
            if (kerberosTicket.getStartTime() != null) {
                return false;
            }
        } else if (!this.startTime.equals(kerberosTicket.getStartTime())) {
            return false;
        }
        if (this.renewTill == null) {
            if (kerberosTicket.getRenewTill() != null) {
                return false;
            }
        } else if (!this.renewTill.equals(kerberosTicket.getRenewTill())) {
            return false;
        }
        if (!Objects.equals(this.proxy, kerberosTicket.proxy)) {
            return false;
        }
        return true;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.sessionKey == null) {
            throw new InvalidObjectException("Session key cannot be null");
        }
        try {
            init(this.asn1Encoding, this.client, this.server, this.sessionKey, this.flags, this.authTime, this.startTime, this.endTime, this.renewTill, this.clientAddresses);
        } catch (IllegalArgumentException e2) {
            throw ((InvalidObjectException) new InvalidObjectException(e2.getMessage()).initCause(e2));
        }
    }
}
