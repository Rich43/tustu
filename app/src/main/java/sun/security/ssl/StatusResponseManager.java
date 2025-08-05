package sun.security.ssl;

import java.io.IOException;
import java.net.URI;
import java.security.cert.Extension;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import sun.security.provider.certpath.CertId;
import sun.security.provider.certpath.OCSP;
import sun.security.provider.certpath.OCSPResponse;
import sun.security.provider.certpath.ResponderId;
import sun.security.ssl.CertStatusExtension;
import sun.security.ssl.SSLExtension;
import sun.security.ssl.X509Authentication;
import sun.security.util.Cache;
import sun.security.x509.PKIXExtensions;
import sun.security.x509.SerialNumber;

/* loaded from: jsse.jar:sun/security/ssl/StatusResponseManager.class */
final class StatusResponseManager {
    private static final int DEFAULT_CORE_THREADS = 8;
    private static final int DEFAULT_CACHE_SIZE = 256;
    private static final int DEFAULT_CACHE_LIFETIME = 3600;
    private final ScheduledThreadPoolExecutor threadMgr;
    private final Cache<CertId, ResponseCacheEntry> responseCache;
    private final URI defaultResponder;
    private final boolean respOverride;
    private final int cacheCapacity;
    private final int cacheLifetime;
    private final boolean ignoreExtensions;

    /* JADX WARN: Removed duplicated region for block: B:16:0x0066  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    StatusResponseManager() {
        /*
            Method dump skipped, instructions count: 244
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.ssl.StatusResponseManager.<init>():void");
    }

    int getCacheLifetime() {
        return this.cacheLifetime;
    }

    int getCacheCapacity() {
        return this.cacheCapacity;
    }

    URI getDefaultResponder() {
        return this.defaultResponder;
    }

    boolean getURIOverride() {
        return this.respOverride;
    }

    boolean getIgnoreExtensions() {
        return this.ignoreExtensions;
    }

    void clear() {
        if (SSLLogger.isOn && SSLLogger.isOn("respmgr")) {
            SSLLogger.fine("Clearing response cache", new Object[0]);
        }
        this.responseCache.clear();
    }

    int size() {
        return this.responseCache.size();
    }

    URI getURI(X509Certificate x509Certificate) {
        Objects.requireNonNull(x509Certificate);
        if (x509Certificate.getExtensionValue(PKIXExtensions.OCSPNoCheck_Id.toString()) != null) {
            if (SSLLogger.isOn && SSLLogger.isOn("respmgr")) {
                SSLLogger.fine("OCSP NoCheck extension found.  OCSP will be skipped", new Object[0]);
                return null;
            }
            return null;
        }
        if (this.defaultResponder != null && this.respOverride) {
            if (SSLLogger.isOn && SSLLogger.isOn("respmgr")) {
                SSLLogger.fine("Responder override: URI is " + ((Object) this.defaultResponder), new Object[0]);
            }
            return this.defaultResponder;
        }
        URI responderURI = OCSP.getResponderURI(x509Certificate);
        return responderURI != null ? responderURI : this.defaultResponder;
    }

    void shutdown() {
        if (SSLLogger.isOn && SSLLogger.isOn("respmgr")) {
            SSLLogger.fine("Shutting down " + this.threadMgr.getActiveCount() + " active threads", new Object[0]);
        }
        this.threadMgr.shutdown();
    }

    Map<X509Certificate, byte[]> get(CertStatusExtension.CertStatusRequestType certStatusRequestType, CertStatusExtension.CertStatusRequest certStatusRequest, X509Certificate[] x509CertificateArr, long j2, TimeUnit timeUnit) {
        HashMap map = new HashMap();
        ArrayList arrayList = new ArrayList();
        if (SSLLogger.isOn && SSLLogger.isOn("respmgr")) {
            SSLLogger.fine("Beginning check: Type = " + ((Object) certStatusRequestType) + ", Chain length = " + x509CertificateArr.length, new Object[0]);
        }
        if (x509CertificateArr.length < 2) {
            return Collections.emptyMap();
        }
        if (certStatusRequestType == CertStatusExtension.CertStatusRequestType.OCSP) {
            try {
                CertStatusExtension.OCSPStatusRequest oCSPStatusRequest = (CertStatusExtension.OCSPStatusRequest) certStatusRequest;
                CertId certId = new CertId(x509CertificateArr[1], new SerialNumber(x509CertificateArr[0].getSerialNumber()));
                ResponseCacheEntry fromCache = getFromCache(certId, oCSPStatusRequest);
                if (fromCache != null) {
                    map.put(x509CertificateArr[0], fromCache.ocspBytes);
                } else {
                    arrayList.add(new OCSPFetchCall(new StatusInfo(x509CertificateArr[0], certId), oCSPStatusRequest));
                }
            } catch (IOException e2) {
                if (SSLLogger.isOn && SSLLogger.isOn("respmgr")) {
                    SSLLogger.fine("Exception during CertId creation: ", e2);
                }
            }
        } else if (certStatusRequestType == CertStatusExtension.CertStatusRequestType.OCSP_MULTI) {
            CertStatusExtension.OCSPStatusRequest oCSPStatusRequest2 = (CertStatusExtension.OCSPStatusRequest) certStatusRequest;
            for (int i2 = 0; i2 < x509CertificateArr.length - 1; i2++) {
                try {
                    CertId certId2 = new CertId(x509CertificateArr[i2 + 1], new SerialNumber(x509CertificateArr[i2].getSerialNumber()));
                    ResponseCacheEntry fromCache2 = getFromCache(certId2, oCSPStatusRequest2);
                    if (fromCache2 != null) {
                        map.put(x509CertificateArr[i2], fromCache2.ocspBytes);
                    } else {
                        arrayList.add(new OCSPFetchCall(new StatusInfo(x509CertificateArr[i2], certId2), oCSPStatusRequest2));
                    }
                } catch (IOException e3) {
                    if (SSLLogger.isOn && SSLLogger.isOn("respmgr")) {
                        SSLLogger.fine("Exception during CertId creation: ", e3);
                    }
                }
            }
        } else if (SSLLogger.isOn && SSLLogger.isOn("respmgr")) {
            SSLLogger.fine("Unsupported status request type: " + ((Object) certStatusRequestType), new Object[0]);
        }
        if (!arrayList.isEmpty()) {
            try {
                for (Future future : this.threadMgr.invokeAll(arrayList, j2, timeUnit)) {
                    if (future.isDone()) {
                        if (!future.isCancelled()) {
                            StatusInfo statusInfo = (StatusInfo) future.get();
                            if (statusInfo != null && statusInfo.responseData != null) {
                                map.put(statusInfo.cert, statusInfo.responseData.ocspBytes);
                            } else if (SSLLogger.isOn && SSLLogger.isOn("respmgr")) {
                                SSLLogger.fine("Completed task had no response data", new Object[0]);
                            }
                        } else if (SSLLogger.isOn && SSLLogger.isOn("respmgr")) {
                            SSLLogger.fine("Found cancelled task", new Object[0]);
                        }
                    }
                }
            } catch (InterruptedException | ExecutionException e4) {
                if (SSLLogger.isOn && SSLLogger.isOn("respmgr")) {
                    SSLLogger.fine("Exception when getting data: ", e4);
                }
            }
        }
        return Collections.unmodifiableMap(map);
    }

    private ResponseCacheEntry getFromCache(CertId certId, CertStatusExtension.OCSPStatusRequest oCSPStatusRequest) {
        Iterator<Extension> it = oCSPStatusRequest.extensions.iterator();
        while (it.hasNext()) {
            if (it.next().getId().equals(PKIXExtensions.OCSPNonce_Id.toString())) {
                if (SSLLogger.isOn && SSLLogger.isOn("respmgr")) {
                    SSLLogger.fine("Nonce extension found, skipping cache check", new Object[0]);
                    return null;
                }
                return null;
            }
        }
        ResponseCacheEntry responseCacheEntry = this.responseCache.get(certId);
        if (responseCacheEntry != null && responseCacheEntry.nextUpdate != null && responseCacheEntry.nextUpdate.before(new Date())) {
            if (SSLLogger.isOn && SSLLogger.isOn("respmgr")) {
                SSLLogger.fine("nextUpdate threshold exceeded, purging from cache", new Object[0]);
            }
            responseCacheEntry = null;
        }
        if (SSLLogger.isOn && SSLLogger.isOn("respmgr")) {
            SSLLogger.fine("Check cache for SN" + ((Object) certId.getSerialNumber()) + ": " + (responseCacheEntry != null ? "HIT" : "MISS"), new Object[0]);
        }
        return responseCacheEntry;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("StatusResponseManager: ");
        sb.append("Core threads: ").append(this.threadMgr.getCorePoolSize());
        sb.append(", Cache timeout: ");
        if (this.cacheLifetime > 0) {
            sb.append(this.cacheLifetime).append(" seconds");
        } else {
            sb.append(" indefinite");
        }
        sb.append(", Cache MaxSize: ");
        if (this.cacheCapacity > 0) {
            sb.append(this.cacheCapacity).append(" items");
        } else {
            sb.append(" unbounded");
        }
        sb.append(", Default URI: ");
        if (this.defaultResponder != null) {
            sb.append((Object) this.defaultResponder);
        } else {
            sb.append("NONE");
        }
        return sb.toString();
    }

    /* loaded from: jsse.jar:sun/security/ssl/StatusResponseManager$StatusInfo.class */
    class StatusInfo {
        final X509Certificate cert;
        final CertId cid;
        final URI responder;
        ResponseCacheEntry responseData;

        StatusInfo(StatusResponseManager statusResponseManager, X509Certificate x509Certificate, X509Certificate x509Certificate2) throws IOException {
            this(x509Certificate, new CertId(x509Certificate2, new SerialNumber(x509Certificate.getSerialNumber())));
        }

        StatusInfo(X509Certificate x509Certificate, CertId certId) {
            this.cert = x509Certificate;
            this.cid = certId;
            this.responder = StatusResponseManager.this.getURI(this.cert);
            this.responseData = null;
        }

        StatusInfo(StatusInfo statusInfo) {
            this.cert = statusInfo.cert;
            this.cid = statusInfo.cid;
            this.responder = statusInfo.responder;
            this.responseData = null;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("StatusInfo:");
            sb.append("\n\tCert: ").append((Object) this.cert.getSubjectX500Principal());
            sb.append("\n\tSerial: ").append((Object) this.cert.getSerialNumber());
            sb.append("\n\tResponder: ").append((Object) this.responder);
            sb.append("\n\tResponse data: ").append(this.responseData != null ? this.responseData.ocspBytes.length + " bytes" : "<NULL>");
            return sb.toString();
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/StatusResponseManager$ResponseCacheEntry.class */
    class ResponseCacheEntry {
        final OCSPResponse.ResponseStatus status;
        final byte[] ocspBytes;
        final Date nextUpdate;
        final OCSPResponse.SingleResponse singleResp;
        final ResponderId respId;

        ResponseCacheEntry(byte[] bArr, CertId certId) throws IOException {
            Objects.requireNonNull(bArr, "Non-null responseBytes required");
            Objects.requireNonNull(certId, "Non-null Cert ID required");
            this.ocspBytes = (byte[]) bArr.clone();
            OCSPResponse oCSPResponse = new OCSPResponse(this.ocspBytes);
            this.status = oCSPResponse.getResponseStatus();
            this.respId = oCSPResponse.getResponderId();
            this.singleResp = oCSPResponse.getSingleResponse(certId);
            if (this.status == OCSPResponse.ResponseStatus.SUCCESSFUL) {
                if (this.singleResp != null) {
                    this.nextUpdate = this.singleResp.getNextUpdate();
                    return;
                }
                throw new IOException("Unable to find SingleResponse for SN " + ((Object) certId.getSerialNumber()));
            }
            this.nextUpdate = null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/StatusResponseManager$OCSPFetchCall.class */
    class OCSPFetchCall implements Callable<StatusInfo> {
        StatusInfo statInfo;
        CertStatusExtension.OCSPStatusRequest ocspRequest;
        List<Extension> extensions;
        List<ResponderId> responderIds;

        public OCSPFetchCall(StatusInfo statusInfo, CertStatusExtension.OCSPStatusRequest oCSPStatusRequest) {
            this.statInfo = (StatusInfo) Objects.requireNonNull(statusInfo, "Null StatusInfo not allowed");
            this.ocspRequest = (CertStatusExtension.OCSPStatusRequest) Objects.requireNonNull(oCSPStatusRequest, "Null OCSPStatusRequest not allowed");
            this.extensions = this.ocspRequest.extensions;
            this.responderIds = this.ocspRequest.responderIds;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.concurrent.Callable
        public StatusInfo call() {
            if (SSLLogger.isOn && SSLLogger.isOn("respmgr")) {
                SSLLogger.fine("Starting fetch for SN " + ((Object) this.statInfo.cid.getSerialNumber()), new Object[0]);
            }
            try {
            } catch (IOException e2) {
                if (SSLLogger.isOn && SSLLogger.isOn("respmgr")) {
                    SSLLogger.fine("Caught exception: ", e2);
                }
            }
            if (this.statInfo.responder == null) {
                if (SSLLogger.isOn && SSLLogger.isOn("respmgr")) {
                    SSLLogger.fine("Null URI detected, OCSP fetch aborted", new Object[0]);
                }
                return this.statInfo;
            }
            if (SSLLogger.isOn && SSLLogger.isOn("respmgr")) {
                SSLLogger.fine("Attempting fetch from " + ((Object) this.statInfo.responder), new Object[0]);
            }
            byte[] oCSPBytes = OCSP.getOCSPBytes(Collections.singletonList(this.statInfo.cid), this.statInfo.responder, (StatusResponseManager.this.ignoreExtensions || !this.responderIds.isEmpty()) ? Collections.emptyList() : this.extensions);
            if (oCSPBytes != null) {
                ResponseCacheEntry responseCacheEntry = StatusResponseManager.this.new ResponseCacheEntry(oCSPBytes, this.statInfo.cid);
                if (SSLLogger.isOn && SSLLogger.isOn("respmgr")) {
                    SSLLogger.fine("OCSP Status: " + ((Object) responseCacheEntry.status) + " (" + oCSPBytes.length + " bytes)", new Object[0]);
                }
                if (responseCacheEntry.status == OCSPResponse.ResponseStatus.SUCCESSFUL) {
                    this.statInfo.responseData = responseCacheEntry;
                    addToCache(this.statInfo.cid, responseCacheEntry);
                }
            } else if (SSLLogger.isOn && SSLLogger.isOn("respmgr")) {
                SSLLogger.fine("No data returned from OCSP Responder", new Object[0]);
            }
            return this.statInfo;
        }

        private void addToCache(CertId certId, ResponseCacheEntry responseCacheEntry) {
            if (responseCacheEntry.nextUpdate != null || StatusResponseManager.this.cacheLifetime != 0) {
                StatusResponseManager.this.responseCache.put(certId, responseCacheEntry);
                if (SSLLogger.isOn && SSLLogger.isOn("respmgr")) {
                    SSLLogger.fine("Added response for SN " + ((Object) certId.getSerialNumber()) + " to cache", new Object[0]);
                    return;
                }
                return;
            }
            if (SSLLogger.isOn && SSLLogger.isOn("respmgr")) {
                SSLLogger.fine("Not caching this OCSP response", new Object[0]);
            }
        }

        private long getNextTaskDelay(Date date) {
            long jMin;
            int cacheLifetime = StatusResponseManager.this.getCacheLifetime();
            if (date != null) {
                long time = (date.getTime() - System.currentTimeMillis()) / 1000;
                jMin = cacheLifetime > 0 ? Long.min(time, cacheLifetime) : time;
            } else {
                jMin = cacheLifetime > 0 ? cacheLifetime : -1L;
            }
            return jMin;
        }
    }

    static final StaplingParameters processStapling(ServerHandshakeContext serverHandshakeContext) {
        byte[] bArr;
        StaplingParameters staplingParameters = null;
        SSLExtension sSLExtension = null;
        CertStatusExtension.CertStatusRequestType certStatusRequestTypeValueOf = null;
        CertStatusExtension.CertStatusRequest certStatusRequest = null;
        if (!serverHandshakeContext.sslContext.isStaplingEnabled(false) || serverHandshakeContext.isResumption) {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("Staping disabled or is a resumed session", new Object[0]);
                return null;
            }
            return null;
        }
        Map<SSLExtension, SSLExtension.SSLExtensionSpec> map = serverHandshakeContext.handshakeExtensions;
        CertStatusExtension.CertStatusRequestSpec certStatusRequestSpec = (CertStatusExtension.CertStatusRequestSpec) map.get(SSLExtension.CH_STATUS_REQUEST);
        CertStatusExtension.CertStatusRequestV2Spec certStatusRequestV2Spec = (CertStatusExtension.CertStatusRequestV2Spec) map.get(SSLExtension.CH_STATUS_REQUEST_V2);
        if (certStatusRequestV2Spec != null && !serverHandshakeContext.negotiatedProtocol.useTLS13PlusSpec()) {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                SSLLogger.fine("SH Processing status_request_v2 extension", new Object[0]);
            }
            sSLExtension = SSLExtension.CH_STATUS_REQUEST_V2;
            int i2 = -1;
            int i3 = -1;
            CertStatusExtension.CertStatusRequest[] certStatusRequestArr = certStatusRequestV2Spec.certStatusRequests;
            for (int i4 = 0; i4 < certStatusRequestArr.length && (i2 == -1 || i3 == -1); i4++) {
                CertStatusExtension.CertStatusRequest certStatusRequest2 = certStatusRequestArr[i4];
                CertStatusExtension.CertStatusRequestType certStatusRequestTypeValueOf2 = CertStatusExtension.CertStatusRequestType.valueOf(certStatusRequest2.statusType);
                if (i2 < 0 && certStatusRequestTypeValueOf2 == CertStatusExtension.CertStatusRequestType.OCSP) {
                    if (((CertStatusExtension.OCSPStatusRequest) certStatusRequest2).responderIds.isEmpty()) {
                        i2 = i4;
                    }
                } else if (i3 < 0 && certStatusRequestTypeValueOf2 == CertStatusExtension.CertStatusRequestType.OCSP_MULTI && ((CertStatusExtension.OCSPStatusRequest) certStatusRequest2).responderIds.isEmpty()) {
                    i3 = i4;
                }
            }
            if (i3 >= 0) {
                certStatusRequest = certStatusRequestArr[i3];
                certStatusRequestTypeValueOf = CertStatusExtension.CertStatusRequestType.valueOf(certStatusRequest.statusType);
            } else if (i2 >= 0) {
                certStatusRequest = certStatusRequestArr[i2];
                certStatusRequestTypeValueOf = CertStatusExtension.CertStatusRequestType.valueOf(certStatusRequest.statusType);
            } else if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.finest("Warning: No suitable request found in the status_request_v2 extension.", new Object[0]);
            }
        }
        if (certStatusRequestSpec != null && (sSLExtension == null || certStatusRequestTypeValueOf == null || certStatusRequest == null)) {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake,verbose")) {
                SSLLogger.fine("SH Processing status_request extension", new Object[0]);
            }
            sSLExtension = SSLExtension.CH_STATUS_REQUEST;
            certStatusRequestTypeValueOf = CertStatusExtension.CertStatusRequestType.valueOf(certStatusRequestSpec.statusRequest.statusType);
            if (certStatusRequestTypeValueOf == CertStatusExtension.CertStatusRequestType.OCSP) {
                CertStatusExtension.OCSPStatusRequest oCSPStatusRequest = (CertStatusExtension.OCSPStatusRequest) certStatusRequestSpec.statusRequest;
                if (oCSPStatusRequest.responderIds.isEmpty()) {
                    certStatusRequest = oCSPStatusRequest;
                } else if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.finest("Warning: No suitable request found in the status_request extension.", new Object[0]);
                }
            }
        }
        if (certStatusRequestTypeValueOf == null || certStatusRequest == null || sSLExtension == null) {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.fine("No suitable status_request or status_request_v2, stapling is disabled", new Object[0]);
                return null;
            }
            return null;
        }
        X509Authentication.X509Possession x509Possession = null;
        Iterator<SSLPossession> it = serverHandshakeContext.handshakePossessions.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            SSLPossession next = it.next();
            if (next instanceof X509Authentication.X509Possession) {
                x509Possession = (X509Authentication.X509Possession) next;
                break;
            }
        }
        if (x509Possession == null) {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.finest("Warning: no X.509 certificates found.  Stapling is disabled.", new Object[0]);
                return null;
            }
            return null;
        }
        X509Certificate[] x509CertificateArr = x509Possession.popCerts;
        StatusResponseManager statusResponseManager = serverHandshakeContext.sslContext.getStatusResponseManager();
        if (statusResponseManager != null) {
            Map<X509Certificate, byte[]> map2 = statusResponseManager.get(serverHandshakeContext.negotiatedProtocol.useTLS13PlusSpec() ? CertStatusExtension.CertStatusRequestType.OCSP_MULTI : certStatusRequestTypeValueOf, certStatusRequest, x509CertificateArr, serverHandshakeContext.statusRespTimeout, TimeUnit.MILLISECONDS);
            if (!map2.isEmpty()) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                    SSLLogger.finest("Response manager returned " + map2.size() + " entries.", new Object[0]);
                }
                if (certStatusRequestTypeValueOf == CertStatusExtension.CertStatusRequestType.OCSP && ((bArr = map2.get(x509CertificateArr[0])) == null || bArr.length <= 0)) {
                    if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                        SSLLogger.finest("Warning: Null or zero-length response found for leaf certificate. Stapling is disabled.", new Object[0]);
                        return null;
                    }
                    return null;
                }
                staplingParameters = new StaplingParameters(sSLExtension, certStatusRequestTypeValueOf, certStatusRequest, map2);
            } else if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.finest("Warning: no OCSP responses obtained.  Stapling is disabled.", new Object[0]);
            }
        } else {
            if (SSLLogger.isOn && SSLLogger.isOn("ssl,handshake")) {
                SSLLogger.finest("Warning: lazy initialization of the StatusResponseManager failed.  Stapling is disabled.", new Object[0]);
            }
            staplingParameters = null;
        }
        return staplingParameters;
    }

    /* loaded from: jsse.jar:sun/security/ssl/StatusResponseManager$StaplingParameters.class */
    static final class StaplingParameters {
        final SSLExtension statusRespExt;
        final CertStatusExtension.CertStatusRequestType statReqType;
        final CertStatusExtension.CertStatusRequest statReqData;
        final Map<X509Certificate, byte[]> responseMap;

        StaplingParameters(SSLExtension sSLExtension, CertStatusExtension.CertStatusRequestType certStatusRequestType, CertStatusExtension.CertStatusRequest certStatusRequest, Map<X509Certificate, byte[]> map) {
            this.statusRespExt = sSLExtension;
            this.statReqType = certStatusRequestType;
            this.statReqData = certStatusRequest;
            this.responseMap = map;
        }
    }
}
