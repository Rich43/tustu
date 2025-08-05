package com.sun.jndi.dns;

import java.lang.ref.SoftReference;
import java.util.Date;

/* loaded from: rt.jar:com/sun/jndi/dns/ZoneNode.class */
class ZoneNode extends NameNode {
    private SoftReference<NameNode> contentsRef;
    private long serialNumber;
    private Date expiration;

    ZoneNode(String str) {
        super(str);
        this.contentsRef = null;
        this.serialNumber = -1L;
        this.expiration = null;
    }

    @Override // com.sun.jndi.dns.NameNode
    protected NameNode newNameNode(String str) {
        return new ZoneNode(str);
    }

    synchronized void depopulate() {
        this.contentsRef = null;
        this.serialNumber = -1L;
    }

    synchronized boolean isPopulated() {
        return getContents() != null;
    }

    synchronized NameNode getContents() {
        if (this.contentsRef != null) {
            return this.contentsRef.get();
        }
        return null;
    }

    synchronized boolean isExpired() {
        return this.expiration != null && this.expiration.before(new Date());
    }

    ZoneNode getDeepestPopulated(DnsName dnsName) {
        ZoneNode zoneNode = this;
        ZoneNode zoneNode2 = isPopulated() ? this : null;
        for (int i2 = 1; i2 < dnsName.size(); i2++) {
            zoneNode = (ZoneNode) zoneNode.get(dnsName.getKey(i2));
            if (zoneNode == null) {
                break;
            }
            if (zoneNode.isPopulated()) {
                zoneNode2 = zoneNode;
            }
        }
        return zoneNode2;
    }

    NameNode populate(DnsName dnsName, ResourceRecords resourceRecords) {
        NameNode nameNode = new NameNode(null);
        for (int i2 = 0; i2 < resourceRecords.answer.size(); i2++) {
            ResourceRecord resourceRecordElementAt = resourceRecords.answer.elementAt(i2);
            DnsName name = resourceRecordElementAt.getName();
            if (name.size() > dnsName.size() && name.startsWith(dnsName)) {
                NameNode nameNodeAdd = nameNode.add(name, dnsName.size());
                if (resourceRecordElementAt.getType() == 2) {
                    nameNodeAdd.setZoneCut(true);
                }
            }
        }
        ResourceRecord resourceRecordFirstElement = resourceRecords.answer.firstElement();
        synchronized (this) {
            this.contentsRef = new SoftReference<>(nameNode);
            this.serialNumber = getSerialNumber(resourceRecordFirstElement);
            setExpiration(getMinimumTtl(resourceRecordFirstElement));
        }
        return nameNode;
    }

    private void setExpiration(long j2) {
        this.expiration = new Date(System.currentTimeMillis() + (1000 * j2));
    }

    private static long getMinimumTtl(ResourceRecord resourceRecord) {
        String str = (String) resourceRecord.getRdata();
        return Long.parseLong(str.substring(str.lastIndexOf(32) + 1));
    }

    int compareSerialNumberTo(ResourceRecord resourceRecord) {
        return ResourceRecord.compareSerialNumbers(this.serialNumber, getSerialNumber(resourceRecord));
    }

    private static long getSerialNumber(ResourceRecord resourceRecord) {
        String str = (String) resourceRecord.getRdata();
        int length = str.length();
        int i2 = -1;
        for (int i3 = 0; i3 < 5; i3++) {
            i2 = length;
            length = str.lastIndexOf(32, i2 - 1);
        }
        return Long.parseLong(str.substring(length + 1, i2));
    }
}
