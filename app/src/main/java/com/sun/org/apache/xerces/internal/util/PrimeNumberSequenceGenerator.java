package com.sun.org.apache.xerces.internal.util;

import com.sun.glass.events.DndEvent;
import com.sun.jndi.ldap.LdapCtx;
import com.sun.media.sound.DLSModulator;
import java.awt.Event;
import java.awt.event.KeyEvent;
import java.util.Random;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.nntp.NNTPReply;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/PrimeNumberSequenceGenerator.class */
final class PrimeNumberSequenceGenerator {
    private static final int[] PRIMES = {3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, KeyEvent.VK_INPUT_METHOD_ON_OFF, 269, 271, DLSModulator.CONN_DST_VIB_STARTDELAY, NNTPReply.AUTHENTICATION_ACCEPTED, 283, 293, 307, 311, 313, 317, FTPReply.NEED_PASSWORD, 337, 347, 349, 353, 359, 367, 373, 379, 383, LdapCtx.DEFAULT_PORT, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 509, 521, 523, 541, 547, 557, 563, 569, 571, 577, 587, 593, 599, 601, Event.SCROLL_END, DndEvent.PERFORM, 617, 619, 631, 641, 643, 647, 653, 659, 661, 673, 677, 683, 691, 701, 709, 719, 727};

    PrimeNumberSequenceGenerator() {
    }

    static void generateSequence(int[] arrayToFill) {
        Random r2 = new Random();
        for (int i2 = 0; i2 < arrayToFill.length; i2++) {
            arrayToFill[i2] = PRIMES[r2.nextInt(PRIMES.length)];
        }
    }
}
