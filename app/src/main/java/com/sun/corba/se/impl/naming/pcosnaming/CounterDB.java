package com.sun.corba.se.impl.naming.pcosnaming;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/* compiled from: ServantManagerImpl.java */
/* loaded from: rt.jar:com/sun/corba/se/impl/naming/pcosnaming/CounterDB.class */
class CounterDB implements Serializable {
    private Integer counter;
    private static String counterFileName = Constants.ELEMNAME_COUNTER_STRING;
    private transient File counterFile;
    public static final int rootCounter = 0;

    CounterDB(File file) {
        counterFileName = Constants.ELEMNAME_COUNTER_STRING;
        this.counterFile = new File(file, counterFileName);
        if (!this.counterFile.exists()) {
            this.counter = new Integer(0);
            writeCounter();
        } else {
            readCounter();
        }
    }

    private void readCounter() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(this.counterFile));
            this.counter = (Integer) objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e2) {
        }
    }

    private void writeCounter() {
        try {
            this.counterFile.delete();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(this.counterFile));
            objectOutputStream.writeObject(this.counter);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (Exception e2) {
        }
    }

    public synchronized int getNextCounter() {
        int iIntValue = this.counter.intValue() + 1;
        this.counter = new Integer(iIntValue);
        writeCounter();
        return iIntValue;
    }
}
