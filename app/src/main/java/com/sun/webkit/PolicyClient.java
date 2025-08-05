package com.sun.webkit;

import java.net.URL;

/* loaded from: jfxrt.jar:com/sun/webkit/PolicyClient.class */
public interface PolicyClient {
    boolean permitNavigateAction(long j2, URL url);

    boolean permitRedirectAction(long j2, URL url);

    boolean permitAcceptResourceAction(long j2, URL url);

    boolean permitSubmitDataAction(long j2, URL url, String str);

    boolean permitResubmitDataAction(long j2, URL url, String str);

    boolean permitEnableScriptsAction(long j2, URL url);

    boolean permitNewPageAction(long j2, URL url);

    boolean permitClosePageAction(long j2);
}
