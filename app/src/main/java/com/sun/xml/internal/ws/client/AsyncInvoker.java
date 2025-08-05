package com.sun.xml.internal.ws.client;

import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/AsyncInvoker.class */
public abstract class AsyncInvoker implements Runnable {
    protected AsyncResponseImpl responseImpl;
    protected boolean nonNullAsyncHandlerGiven;

    public abstract void do_run();

    public void setReceiver(AsyncResponseImpl responseImpl) {
        this.responseImpl = responseImpl;
    }

    public AsyncResponseImpl getResponseImpl() {
        return this.responseImpl;
    }

    public void setResponseImpl(AsyncResponseImpl responseImpl) {
        this.responseImpl = responseImpl;
    }

    public boolean isNonNullAsyncHandlerGiven() {
        return this.nonNullAsyncHandlerGiven;
    }

    public void setNonNullAsyncHandlerGiven(boolean nonNullAsyncHandlerGiven) {
        this.nonNullAsyncHandlerGiven = nonNullAsyncHandlerGiven;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            do_run();
        } catch (WebServiceException e2) {
            throw e2;
        } catch (Throwable t2) {
            throw new WebServiceException(t2);
        }
    }
}
