package com.sun.org.apache.xml.internal.dtm.ref;

import com.sun.org.apache.xml.internal.utils.IntVector;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMStringPool.class */
public class DTMStringPool {
    Vector m_intToString;
    static final int HASHPRIME = 101;
    int[] m_hashStart;
    IntVector m_hashChain;
    public static final int NULL = -1;

    public DTMStringPool(int chainSize) {
        this.m_hashStart = new int[101];
        this.m_intToString = new Vector();
        this.m_hashChain = new IntVector(chainSize);
        removeAllElements();
        stringToIndex("");
    }

    public DTMStringPool() {
        this(512);
    }

    public void removeAllElements() {
        this.m_intToString.removeAllElements();
        for (int i2 = 0; i2 < 101; i2++) {
            this.m_hashStart[i2] = -1;
        }
        this.m_hashChain.removeAllElements();
    }

    public String indexToString(int i2) throws ArrayIndexOutOfBoundsException {
        if (i2 == -1) {
            return null;
        }
        return (String) this.m_intToString.elementAt(i2);
    }

    public int stringToIndex(String s2) {
        if (s2 == null) {
            return -1;
        }
        int hashslot = s2.hashCode() % 101;
        if (hashslot < 0) {
            hashslot = -hashslot;
        }
        int hashlast = this.m_hashStart[hashslot];
        int iElementAt = hashlast;
        while (true) {
            int hashcandidate = iElementAt;
            if (hashcandidate != -1) {
                if (this.m_intToString.elementAt(hashcandidate).equals(s2)) {
                    return hashcandidate;
                }
                hashlast = hashcandidate;
                iElementAt = this.m_hashChain.elementAt(hashcandidate);
            } else {
                int newIndex = this.m_intToString.size();
                this.m_intToString.addElement(s2);
                this.m_hashChain.addElement(-1);
                if (hashlast == -1) {
                    this.m_hashStart[hashslot] = newIndex;
                } else {
                    this.m_hashChain.setElementAt(newIndex, hashlast);
                }
                return newIndex;
            }
        }
    }

    public static void _main(String[] args) throws ArrayIndexOutOfBoundsException {
        String[] word = {"Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen", "Twenty", "Twenty-One", "Twenty-Two", "Twenty-Three", "Twenty-Four", "Twenty-Five", "Twenty-Six", "Twenty-Seven", "Twenty-Eight", "Twenty-Nine", "Thirty", "Thirty-One", "Thirty-Two", "Thirty-Three", "Thirty-Four", "Thirty-Five", "Thirty-Six", "Thirty-Seven", "Thirty-Eight", "Thirty-Nine"};
        DTMStringPool pool = new DTMStringPool();
        System.out.println("If no complaints are printed below, we passed initial test.");
        for (int pass = 0; pass <= 1; pass++) {
            for (int i2 = 0; i2 < word.length; i2++) {
                int j2 = pool.stringToIndex(word[i2]);
                if (j2 != i2) {
                    System.out.println("\tMismatch populating pool: assigned " + j2 + " for create " + i2);
                }
            }
            for (int i3 = 0; i3 < word.length; i3++) {
                int j3 = pool.stringToIndex(word[i3]);
                if (j3 != i3) {
                    System.out.println("\tMismatch in stringToIndex: returned " + j3 + " for lookup " + i3);
                }
            }
            for (int i4 = 0; i4 < word.length; i4++) {
                String w2 = pool.indexToString(i4);
                if (!word[i4].equals(w2)) {
                    System.out.println("\tMismatch in indexToString: returned" + w2 + " for lookup " + i4);
                }
            }
            pool.removeAllElements();
            System.out.println("\nPass " + pass + " complete\n");
        }
    }
}
