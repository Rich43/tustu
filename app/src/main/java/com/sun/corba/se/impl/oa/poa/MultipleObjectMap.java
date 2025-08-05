package com.sun.corba.se.impl.oa.poa;

import com.sun.corba.se.impl.oa.poa.ActiveObjectMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.omg.PortableServer.POAPackage.WrongPolicy;

/* compiled from: ActiveObjectMap.java */
/* loaded from: rt.jar:com/sun/corba/se/impl/oa/poa/MultipleObjectMap.class */
class MultipleObjectMap extends ActiveObjectMap {
    private Map entryToKeys;

    public MultipleObjectMap(POAImpl pOAImpl) {
        super(pOAImpl);
        this.entryToKeys = new HashMap();
    }

    @Override // com.sun.corba.se.impl.oa.poa.ActiveObjectMap
    public ActiveObjectMap.Key getKey(AOMEntry aOMEntry) throws WrongPolicy {
        throw new WrongPolicy();
    }

    @Override // com.sun.corba.se.impl.oa.poa.ActiveObjectMap
    protected void putEntry(ActiveObjectMap.Key key, AOMEntry aOMEntry) {
        super.putEntry(key, aOMEntry);
        Set hashSet = (Set) this.entryToKeys.get(aOMEntry);
        if (hashSet == null) {
            hashSet = new HashSet();
            this.entryToKeys.put(aOMEntry, hashSet);
        }
        hashSet.add(key);
    }

    @Override // com.sun.corba.se.impl.oa.poa.ActiveObjectMap
    public boolean hasMultipleIDs(AOMEntry aOMEntry) {
        Set set = (Set) this.entryToKeys.get(aOMEntry);
        return set != null && set.size() > 1;
    }

    @Override // com.sun.corba.se.impl.oa.poa.ActiveObjectMap
    protected void removeEntry(AOMEntry aOMEntry, ActiveObjectMap.Key key) {
        Set set = (Set) this.entryToKeys.get(aOMEntry);
        if (set != null) {
            set.remove(key);
            if (set.isEmpty()) {
                this.entryToKeys.remove(aOMEntry);
            }
        }
    }

    @Override // com.sun.corba.se.impl.oa.poa.ActiveObjectMap
    public void clear() {
        super.clear();
        this.entryToKeys.clear();
    }
}
