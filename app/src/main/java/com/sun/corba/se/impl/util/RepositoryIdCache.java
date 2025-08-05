package com.sun.corba.se.impl.util;

import java.util.Hashtable;

/* loaded from: rt.jar:com/sun/corba/se/impl/util/RepositoryIdCache.class */
public class RepositoryIdCache extends Hashtable {
    private RepositoryIdPool pool = new RepositoryIdPool();

    public RepositoryIdCache() {
        this.pool.setCaches(this);
    }

    public final synchronized RepositoryId getId(String str) {
        RepositoryId repositoryId = (RepositoryId) super.get(str);
        if (repositoryId != null) {
            return repositoryId;
        }
        RepositoryId repositoryId2 = new RepositoryId(str);
        put(str, repositoryId2);
        return repositoryId2;
    }
}
