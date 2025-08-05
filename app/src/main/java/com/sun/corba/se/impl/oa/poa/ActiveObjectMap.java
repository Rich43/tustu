package com.sun.corba.se.impl.oa.poa;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.omg.PortableServer.POAPackage.WrongPolicy;
import org.omg.PortableServer.Servant;

/* loaded from: rt.jar:com/sun/corba/se/impl/oa/poa/ActiveObjectMap.class */
public abstract class ActiveObjectMap {
    protected POAImpl poa;
    private Map keyToEntry = new HashMap();
    private Map entryToServant = new HashMap();
    private Map servantToEntry = new HashMap();

    public abstract Key getKey(AOMEntry aOMEntry) throws WrongPolicy;

    protected abstract void removeEntry(AOMEntry aOMEntry, Key key);

    public abstract boolean hasMultipleIDs(AOMEntry aOMEntry);

    /* loaded from: rt.jar:com/sun/corba/se/impl/oa/poa/ActiveObjectMap$Key.class */
    public static class Key {
        public byte[] id;

        Key(byte[] bArr) {
            this.id = bArr;
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i2 = 0; i2 < this.id.length; i2++) {
                stringBuffer.append(Integer.toString(this.id[i2], 16));
                if (i2 != this.id.length - 1) {
                    stringBuffer.append(CallSiteDescriptor.TOKEN_DELIMITER);
                }
            }
            return stringBuffer.toString();
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Key)) {
                return false;
            }
            Key key = (Key) obj;
            if (key.id.length != this.id.length) {
                return false;
            }
            for (int i2 = 0; i2 < this.id.length; i2++) {
                if (this.id[i2] != key.id[i2]) {
                    return false;
                }
            }
            return true;
        }

        public int hashCode() {
            int i2 = 0;
            for (int i3 = 0; i3 < this.id.length; i3++) {
                i2 = (31 * i2) + this.id[i3];
            }
            return i2;
        }
    }

    protected ActiveObjectMap(POAImpl pOAImpl) {
        this.poa = pOAImpl;
    }

    public static ActiveObjectMap create(POAImpl pOAImpl, boolean z2) {
        if (z2) {
            return new MultipleObjectMap(pOAImpl);
        }
        return new SingleObjectMap(pOAImpl);
    }

    public final boolean contains(Servant servant) {
        return this.servantToEntry.containsKey(servant);
    }

    public final boolean containsKey(Key key) {
        return this.keyToEntry.containsKey(key);
    }

    public final AOMEntry get(Key key) {
        AOMEntry aOMEntry = (AOMEntry) this.keyToEntry.get(key);
        if (aOMEntry == null) {
            aOMEntry = new AOMEntry(this.poa);
            putEntry(key, aOMEntry);
        }
        return aOMEntry;
    }

    public final Servant getServant(AOMEntry aOMEntry) {
        return (Servant) this.entryToServant.get(aOMEntry);
    }

    public Key getKey(Servant servant) throws WrongPolicy {
        return getKey((AOMEntry) this.servantToEntry.get(servant));
    }

    protected void putEntry(Key key, AOMEntry aOMEntry) {
        this.keyToEntry.put(key, aOMEntry);
    }

    public final void putServant(Servant servant, AOMEntry aOMEntry) {
        this.entryToServant.put(aOMEntry, servant);
        this.servantToEntry.put(servant, aOMEntry);
    }

    public final void remove(Key key) {
        AOMEntry aOMEntry = (AOMEntry) this.keyToEntry.remove(key);
        Servant servant = (Servant) this.entryToServant.remove(aOMEntry);
        if (servant != null) {
            this.servantToEntry.remove(servant);
        }
        removeEntry(aOMEntry, key);
    }

    protected void clear() {
        this.keyToEntry.clear();
    }

    public final Set keySet() {
        return this.keyToEntry.keySet();
    }
}
