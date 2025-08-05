package sun.security.krb5.internal.ccache;

import java.io.IOException;
import java.io.OutputStream;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.PrincipalName;
import sun.security.krb5.internal.Ticket;
import sun.security.krb5.internal.TicketFlags;
import sun.security.krb5.internal.ccache.CredentialsCache;
import sun.security.krb5.internal.util.KrbDataOutputStream;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:sun/security/krb5/internal/ccache/CCacheOutputStream.class */
public class CCacheOutputStream extends KrbDataOutputStream implements FileCCacheConstants {
    public CCacheOutputStream(OutputStream outputStream) {
        super(outputStream);
    }

    public void writeHeader(PrincipalName principalName, int i2) throws IOException {
        write((i2 & NormalizerImpl.CC_MASK) >> 8);
        write(i2 & 255);
        principalName.writePrincipal(this);
    }

    public void addCreds(Credentials credentials) throws Asn1Exception, IOException {
        credentials.cname.writePrincipal(this);
        credentials.sname.writePrincipal(this);
        credentials.key.writeKey(this);
        write32((int) (credentials.authtime.getTime() / 1000));
        if (credentials.starttime != null) {
            write32((int) (credentials.starttime.getTime() / 1000));
        } else {
            write32(0);
        }
        write32((int) (credentials.endtime.getTime() / 1000));
        if (credentials.renewTill != null) {
            write32((int) (credentials.renewTill.getTime() / 1000));
        } else {
            write32(0);
        }
        if (credentials.isEncInSKey) {
            write8(1);
        } else {
            write8(0);
        }
        writeFlags(credentials.flags);
        if (credentials.caddr == null) {
            write32(0);
        } else {
            credentials.caddr.writeAddrs(this);
        }
        if (credentials.authorizationData == null) {
            write32(0);
        } else {
            credentials.authorizationData.writeAuth(this);
        }
        writeTicket(credentials.ticket);
        writeTicket(credentials.secondTicket);
    }

    public void addConfigEntry(PrincipalName principalName, CredentialsCache.ConfigEntry configEntry) throws IOException {
        principalName.writePrincipal(this);
        configEntry.getSName().writePrincipal(this);
        write16(0);
        write16(0);
        write32(0);
        write32(0);
        write32(0);
        write32(0);
        write32(0);
        write8(0);
        write32(0);
        write32(0);
        write32(0);
        write32(configEntry.getData().length);
        write(configEntry.getData());
        write32(0);
    }

    void writeTicket(Ticket ticket) throws Asn1Exception, IOException {
        if (ticket == null) {
            write32(0);
            return;
        }
        byte[] bArrAsn1Encode = ticket.asn1Encode();
        write32(bArrAsn1Encode.length);
        write(bArrAsn1Encode, 0, bArrAsn1Encode.length);
    }

    void writeFlags(TicketFlags ticketFlags) throws IOException {
        int i2 = 0;
        boolean[] booleanArray = ticketFlags.toBooleanArray();
        if (booleanArray[1]) {
            i2 = 0 | 1073741824;
        }
        if (booleanArray[2]) {
            i2 |= 536870912;
        }
        if (booleanArray[3]) {
            i2 |= 268435456;
        }
        if (booleanArray[4]) {
            i2 |= 134217728;
        }
        if (booleanArray[5]) {
            i2 |= 67108864;
        }
        if (booleanArray[6]) {
            i2 |= 33554432;
        }
        if (booleanArray[7]) {
            i2 |= 16777216;
        }
        if (booleanArray[8]) {
            i2 |= 8388608;
        }
        if (booleanArray[9]) {
            i2 |= 4194304;
        }
        if (booleanArray[10]) {
            i2 |= 2097152;
        }
        if (booleanArray[11]) {
            i2 |= 1048576;
        }
        write32(i2);
    }
}
