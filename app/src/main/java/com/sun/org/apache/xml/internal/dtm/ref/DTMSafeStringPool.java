package com.sun.org.apache.xml.internal.dtm.ref;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/DTMSafeStringPool.class */
public class DTMSafeStringPool extends DTMStringPool {
    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMStringPool
    public synchronized void removeAllElements() {
        super.removeAllElements();
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMStringPool
    public synchronized String indexToString(int i2) throws ArrayIndexOutOfBoundsException {
        return super.indexToString(i2);
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMStringPool
    public synchronized int stringToIndex(String s2) {
        return super.stringToIndex(s2);
    }

    public static void _main(String[] args) throws ArrayIndexOutOfBoundsException {
        String[] word = {"Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen", "Twenty", "Twenty-One", "Twenty-Two", "Twenty-Three", "Twenty-Four", "Twenty-Five", "Twenty-Six", "Twenty-Seven", "Twenty-Eight", "Twenty-Nine", "Thirty", "Thirty-One", "Thirty-Two", "Thirty-Three", "Thirty-Four", "Thirty-Five", "Thirty-Six", "Thirty-Seven", "Thirty-Eight", "Thirty-Nine"};
        DTMStringPool pool = new DTMSafeStringPool();
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
