package com.sun.corba.se.impl.util;

import java.util.EmptyStackException;
import java.util.Stack;

/* compiled from: RepositoryIdCache.java */
/* loaded from: rt.jar:com/sun/corba/se/impl/util/RepositoryIdPool.class */
class RepositoryIdPool extends Stack {
    private static int MAX_CACHE_SIZE = 4;
    private RepositoryIdCache cache;

    RepositoryIdPool() {
    }

    public final synchronized RepositoryId popId() {
        try {
            return (RepositoryId) super.pop();
        } catch (EmptyStackException e2) {
            increasePool(5);
            return (RepositoryId) super.pop();
        }
    }

    final void increasePool(int i2) {
        for (int i3 = i2; i3 > 0; i3--) {
            push(new RepositoryId());
        }
    }

    final void setCaches(RepositoryIdCache repositoryIdCache) {
        this.cache = repositoryIdCache;
    }
}
