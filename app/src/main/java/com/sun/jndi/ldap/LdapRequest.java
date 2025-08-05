package com.sun.jndi.ldap;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import javax.naming.CommunicationException;
import javax.naming.NamingException;

/* loaded from: rt.jar:com/sun/jndi/ldap/LdapRequest.class */
final class LdapRequest {
    private static final BerDecoder EOF = new BerDecoder(new byte[0], -1, 0);
    private static final String CLOSE_MSG = "LDAP connection has been closed";
    private static final String TIMEOUT_MSG_FMT = "LDAP response read timed out, timeout used: %d ms.";
    LdapRequest next;
    final int msgId;
    private final BlockingQueue<BerDecoder> replies;
    private volatile boolean cancelled;
    private volatile boolean closed;
    private volatile boolean completed;
    private final boolean pauseAfterReceipt;

    LdapRequest(int i2, boolean z2, int i3) {
        this.msgId = i2;
        this.pauseAfterReceipt = z2;
        if (i3 == -1) {
            this.replies = new LinkedBlockingQueue();
        } else {
            this.replies = new LinkedBlockingQueue((8 * i3) / 10);
        }
    }

    void cancel() {
        this.cancelled = true;
        this.replies.offer(EOF);
    }

    synchronized void close() {
        this.closed = true;
        this.replies.offer(EOF);
    }

    private boolean isClosed() {
        return this.closed && (this.replies.size() == 0 || this.replies.peek() == EOF);
    }

    synchronized boolean addReplyBer(BerDecoder berDecoder) {
        if (this.cancelled || this.closed) {
            return false;
        }
        try {
            berDecoder.parseSeq(null);
            berDecoder.parseInt();
            this.completed = berDecoder.peekByte() == 101;
        } catch (IOException e2) {
        }
        berDecoder.reset();
        try {
            this.replies.put(berDecoder);
        } catch (InterruptedException e3) {
        }
        return this.pauseAfterReceipt;
    }

    BerDecoder getReplyBer(long j2) throws InterruptedException, NamingException {
        if (this.cancelled) {
            throw new CommunicationException("Request: " + this.msgId + " cancelled");
        }
        if (isClosed()) {
            throw new NamingException(CLOSE_MSG);
        }
        BerDecoder berDecoderPoll2 = j2 > 0 ? this.replies.poll2(j2, TimeUnit.MILLISECONDS) : this.replies.take2();
        if (this.cancelled) {
            throw new CommunicationException("Request: " + this.msgId + " cancelled");
        }
        if (berDecoderPoll2 == null) {
            throw new NamingException(String.format(TIMEOUT_MSG_FMT, Long.valueOf(j2)));
        }
        if (berDecoderPoll2 == EOF) {
            throw new NamingException(CLOSE_MSG);
        }
        return berDecoderPoll2;
    }

    boolean hasSearchCompleted() {
        return this.completed;
    }
}
