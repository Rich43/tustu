package sun.net.httpserver;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.logging.Logger;

/* loaded from: rt.jar:sun/net/httpserver/ServerConfig.class */
class ServerConfig {
    private static final int DEFAULT_IDLE_TIMER_SCHEDULE_MILLIS = 10000;
    private static final long DEFAULT_IDLE_INTERVAL_IN_SECS = 30;
    private static final int DEFAULT_MAX_CONNECTIONS = -1;
    private static final int DEFAULT_MAX_IDLE_CONNECTIONS = 200;
    private static final long DEFAULT_MAX_REQ_TIME = -1;
    private static final long DEFAULT_MAX_RSP_TIME = -1;
    private static final long DEFAULT_REQ_RSP_TIMER_TASK_SCHEDULE_MILLIS = 1000;
    private static final int DEFAULT_MAX_REQ_HEADERS = 200;
    private static final long DEFAULT_DRAIN_AMOUNT = 65536;
    private static long idleTimerScheduleMillis;
    private static long idleIntervalMillis;
    private static long drainAmount;
    private static int maxConnections;
    private static int maxIdleConnections;
    private static int maxReqHeaders;
    private static long maxReqTime;
    private static long maxRspTime;
    private static long reqRspTimerScheduleMillis;
    private static boolean debug;
    private static boolean noDelay;

    ServerConfig() {
    }

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.net.httpserver.ServerConfig.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                long unused = ServerConfig.idleIntervalMillis = Long.getLong("sun.net.httpserver.idleInterval", 30L).longValue() * 1000;
                if (ServerConfig.idleIntervalMillis <= 0) {
                    long unused2 = ServerConfig.idleIntervalMillis = 30000L;
                }
                long unused3 = ServerConfig.idleTimerScheduleMillis = Long.getLong("sun.net.httpserver.clockTick", 10000L).longValue();
                if (ServerConfig.idleTimerScheduleMillis <= 0) {
                    long unused4 = ServerConfig.idleTimerScheduleMillis = 10000L;
                }
                int unused5 = ServerConfig.maxConnections = Integer.getInteger("jdk.httpserver.maxConnections", -1).intValue();
                int unused6 = ServerConfig.maxIdleConnections = Integer.getInteger("sun.net.httpserver.maxIdleConnections", 200).intValue();
                long unused7 = ServerConfig.drainAmount = Long.getLong("sun.net.httpserver.drainAmount", 65536L).longValue();
                int unused8 = ServerConfig.maxReqHeaders = Integer.getInteger("sun.net.httpserver.maxReqHeaders", 200).intValue();
                long unused9 = ServerConfig.maxReqTime = Long.getLong("sun.net.httpserver.maxReqTime", -1L).longValue();
                long unused10 = ServerConfig.maxRspTime = Long.getLong("sun.net.httpserver.maxRspTime", -1L).longValue();
                long unused11 = ServerConfig.reqRspTimerScheduleMillis = Long.getLong("sun.net.httpserver.timerMillis", 1000L).longValue();
                if (ServerConfig.reqRspTimerScheduleMillis <= 0) {
                    long unused12 = ServerConfig.reqRspTimerScheduleMillis = 1000L;
                }
                boolean unused13 = ServerConfig.debug = Boolean.getBoolean("sun.net.httpserver.debug");
                boolean unused14 = ServerConfig.noDelay = Boolean.getBoolean("sun.net.httpserver.nodelay");
                return null;
            }
        });
    }

    static void checkLegacyProperties(final Logger logger) {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.net.httpserver.ServerConfig.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                if (System.getProperty("sun.net.httpserver.readTimeout") != null) {
                    logger.warning("sun.net.httpserver.readTimeout property is no longer used. Use sun.net.httpserver.maxReqTime instead.");
                }
                if (System.getProperty("sun.net.httpserver.writeTimeout") != null) {
                    logger.warning("sun.net.httpserver.writeTimeout property is no longer used. Use sun.net.httpserver.maxRspTime instead.");
                }
                if (System.getProperty("sun.net.httpserver.selCacheTimeout") != null) {
                    logger.warning("sun.net.httpserver.selCacheTimeout property is no longer used.");
                    return null;
                }
                return null;
            }
        });
    }

    static boolean debugEnabled() {
        return debug;
    }

    static long getIdleIntervalMillis() {
        return idleIntervalMillis;
    }

    static long getIdleTimerScheduleMillis() {
        return idleTimerScheduleMillis;
    }

    static int getMaxConnections() {
        return maxConnections;
    }

    static int getMaxIdleConnections() {
        return maxIdleConnections;
    }

    static long getDrainAmount() {
        return drainAmount;
    }

    static int getMaxReqHeaders() {
        return maxReqHeaders;
    }

    static long getMaxReqTime() {
        return maxReqTime;
    }

    static long getMaxRspTime() {
        return maxRspTime;
    }

    static long getReqRspTimerScheduleMillis() {
        return reqRspTimerScheduleMillis;
    }

    static boolean noDelay() {
        return noDelay;
    }
}
