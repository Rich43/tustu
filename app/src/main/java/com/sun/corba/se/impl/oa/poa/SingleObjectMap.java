package com.sun.corba.se.impl.oa.poa;

import com.sun.corba.se.impl.oa.poa.ActiveObjectMap;
import java.util.HashMap;
import java.util.Map;
import org.omg.PortableServer.POAPackage.WrongPolicy;

/* compiled from: ActiveObjectMap.java */
/* loaded from: rt.jar:com/sun/corba/se/impl/oa/poa/SingleObjectMap.class */
class SingleObjectMap extends ActiveObjectMap {
    private Map entryToKey;

    public SingleObjectMap(POAImpl pOAImpl) {
        super(pOAImpl);
        this.entryToKey = new HashMap();
    }

    @Override // com.sun.corba.se.impl.oa.poa.ActiveObjectMap
    public ActiveObjectMap.Key getKey(AOMEntry aOMEntry) throws WrongPolicy {
        return (ActiveObjectMap.Key) this.entryToKey.get(aOMEntry);
    }

    @Override // com.sun.corba.se.impl.oa.poa.ActiveObjectMap
    protected void putEntry(ActiveObjectMap.Key key, AOMEntry aOMEntry) {
        super.putEntry(key, aOMEntry);
        this.entryToKey.put(aOMEntry, key);
    }

    @Override // com.sun.corba.se.impl.oa.poa.ActiveObjectMap
    public boolean hasMultipleIDs(AOMEntry aOMEntry) {
        return false;
    }

    @Override // com.sun.corba.se.impl.oa.poa.ActiveObjectMap
    protected void removeEntry(AOMEntry aOMEntry, ActiveObjectMap.Key key) {
        this.entryToKey.remove(aOMEntry);
    }

    @Override // com.sun.corba.se.impl.oa.poa.ActiveObjectMap
    public void clear() {
        super.clear();
        this.entryToKey.clear();
    }
}
