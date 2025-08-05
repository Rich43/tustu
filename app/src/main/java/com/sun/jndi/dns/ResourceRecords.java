package com.sun.jndi.dns;

import java.util.Vector;
import javax.naming.CommunicationException;
import javax.naming.NamingException;

/* loaded from: rt.jar:com/sun/jndi/dns/ResourceRecords.class */
class ResourceRecords {
    Vector<ResourceRecord> question = new Vector<>();
    Vector<ResourceRecord> answer = new Vector<>();
    Vector<ResourceRecord> authority = new Vector<>();
    Vector<ResourceRecord> additional = new Vector<>();
    boolean zoneXfer;

    ResourceRecords(byte[] bArr, int i2, Header header, boolean z2) throws NamingException {
        if (z2) {
            this.answer.ensureCapacity(8192);
        }
        this.zoneXfer = z2;
        add(bArr, i2, header);
    }

    int getFirstAnsType() {
        if (this.answer.size() == 0) {
            return -1;
        }
        return this.answer.firstElement().getType();
    }

    int getLastAnsType() {
        if (this.answer.size() == 0) {
            return -1;
        }
        return this.answer.lastElement().getType();
    }

    void add(byte[] bArr, int i2, Header header) throws NamingException {
        int size = 12;
        for (int i3 = 0; i3 < header.numQuestions; i3++) {
            try {
                ResourceRecord resourceRecord = new ResourceRecord(bArr, i2, size, true, false);
                if (!this.zoneXfer) {
                    this.question.addElement(resourceRecord);
                }
                size += resourceRecord.size();
            } catch (IndexOutOfBoundsException e2) {
                throw new CommunicationException("DNS error: corrupted message");
            }
        }
        for (int i4 = 0; i4 < header.numAnswers; i4++) {
            ResourceRecord resourceRecord2 = new ResourceRecord(bArr, i2, size, false, !this.zoneXfer);
            this.answer.addElement(resourceRecord2);
            size += resourceRecord2.size();
        }
        if (this.zoneXfer) {
            return;
        }
        for (int i5 = 0; i5 < header.numAuthorities; i5++) {
            ResourceRecord resourceRecord3 = new ResourceRecord(bArr, i2, size, false, true);
            this.authority.addElement(resourceRecord3);
            size += resourceRecord3.size();
        }
    }
}
