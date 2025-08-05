package org.omg.PortableInterceptor;

/* loaded from: rt.jar:org/omg/PortableInterceptor/ClientRequestInterceptorOperations.class */
public interface ClientRequestInterceptorOperations extends InterceptorOperations {
    void send_request(ClientRequestInfo clientRequestInfo) throws ForwardRequest;

    void send_poll(ClientRequestInfo clientRequestInfo);

    void receive_reply(ClientRequestInfo clientRequestInfo);

    void receive_exception(ClientRequestInfo clientRequestInfo) throws ForwardRequest;

    void receive_other(ClientRequestInfo clientRequestInfo) throws ForwardRequest;
}
