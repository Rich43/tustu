package org.omg.PortableInterceptor;

/* loaded from: rt.jar:org/omg/PortableInterceptor/ServerRequestInterceptorOperations.class */
public interface ServerRequestInterceptorOperations extends InterceptorOperations {
    void receive_request_service_contexts(ServerRequestInfo serverRequestInfo) throws ForwardRequest;

    void receive_request(ServerRequestInfo serverRequestInfo) throws ForwardRequest;

    void send_reply(ServerRequestInfo serverRequestInfo);

    void send_exception(ServerRequestInfo serverRequestInfo) throws ForwardRequest;

    void send_other(ServerRequestInfo serverRequestInfo) throws ForwardRequest;
}
