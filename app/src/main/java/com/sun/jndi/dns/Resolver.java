package com.sun.jndi.dns;

import javax.naming.CommunicationException;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

/* loaded from: rt.jar:com/sun/jndi/dns/Resolver.class */
class Resolver {
    private DnsClient dnsClient;
    private int timeout;
    private int retries;

    Resolver(String[] strArr, int i2, int i3) throws NamingException {
        this.timeout = i2;
        this.retries = i3;
        this.dnsClient = new DnsClient(strArr, i2, i3);
    }

    public void close() {
        this.dnsClient.close();
        this.dnsClient = null;
    }

    ResourceRecords query(DnsName dnsName, int i2, int i3, boolean z2, boolean z3) throws NamingException {
        return this.dnsClient.query(dnsName, i2, i3, z2, z3);
    }

    ResourceRecords queryZone(DnsName dnsName, int i2, boolean z2) throws NamingException {
        DnsClient dnsClient = new DnsClient(findNameServers(dnsName, z2), this.timeout, this.retries);
        try {
            ResourceRecords resourceRecordsQueryZone = dnsClient.queryZone(dnsName, i2, z2);
            dnsClient.close();
            return resourceRecordsQueryZone;
        } catch (Throwable th) {
            dnsClient.close();
            throw th;
        }
    }

    DnsName findZoneName(DnsName dnsName, int i2, boolean z2) throws NamingException {
        DnsName dnsName2 = (DnsName) dnsName.clone();
        while (dnsName2.size() > 1) {
            ResourceRecords resourceRecordsQuery = null;
            try {
                resourceRecordsQuery = query(dnsName2, i2, 6, z2, false);
            } catch (NameNotFoundException e2) {
                throw e2;
            } catch (NamingException e3) {
            }
            if (resourceRecordsQuery != null) {
                if (resourceRecordsQuery.answer.size() > 0) {
                    return dnsName2;
                }
                for (int i3 = 0; i3 < resourceRecordsQuery.authority.size(); i3++) {
                    ResourceRecord resourceRecordElementAt = resourceRecordsQuery.authority.elementAt(i3);
                    if (resourceRecordElementAt.getType() == 6) {
                        DnsName name = resourceRecordElementAt.getName();
                        if (dnsName2.endsWith(name)) {
                            return name;
                        }
                    }
                }
            }
            dnsName2.remove(dnsName2.size() - 1);
        }
        return dnsName2;
    }

    ResourceRecord findSoa(DnsName dnsName, int i2, boolean z2) throws NamingException {
        ResourceRecords resourceRecordsQuery = query(dnsName, i2, 6, z2, false);
        for (int i3 = 0; i3 < resourceRecordsQuery.answer.size(); i3++) {
            ResourceRecord resourceRecordElementAt = resourceRecordsQuery.answer.elementAt(i3);
            if (resourceRecordElementAt.getType() == 6) {
                return resourceRecordElementAt;
            }
        }
        return null;
    }

    private String[] findNameServers(DnsName dnsName, boolean z2) throws NamingException {
        ResourceRecords resourceRecordsQuery = query(dnsName, 1, 2, z2, false);
        String[] strArr = new String[resourceRecordsQuery.answer.size()];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            ResourceRecord resourceRecordElementAt = resourceRecordsQuery.answer.elementAt(i2);
            if (resourceRecordElementAt.getType() != 2) {
                throw new CommunicationException("Corrupted DNS message");
            }
            strArr[i2] = (String) resourceRecordElementAt.getRdata();
            strArr[i2] = strArr[i2].substring(0, strArr[i2].length() - 1);
        }
        return strArr;
    }
}
