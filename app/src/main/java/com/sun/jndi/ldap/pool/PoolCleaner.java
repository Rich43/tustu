package com.sun.jndi.ldap.pool;

/* loaded from: rt.jar:com/sun/jndi/ldap/pool/PoolCleaner.class */
public final class PoolCleaner implements Runnable {
    private final Pool[] pools;
    private final long period;

    public PoolCleaner(long j2, Pool[] poolArr) {
        this.period = j2;
        this.pools = (Pool[]) poolArr.clone();
    }

    @Override // java.lang.Runnable
    public void run() {
        while (true) {
            synchronized (this) {
                try {
                    wait(this.period);
                } catch (InterruptedException e2) {
                }
                long jCurrentTimeMillis = System.currentTimeMillis() - this.period;
                for (int i2 = 0; i2 < this.pools.length; i2++) {
                    if (this.pools[i2] != null) {
                        this.pools[i2].expire(jCurrentTimeMillis);
                    }
                }
            }
        }
    }
}
