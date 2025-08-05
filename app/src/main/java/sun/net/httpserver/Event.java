package sun.net.httpserver;

/* loaded from: rt.jar:sun/net/httpserver/Event.class */
class Event {
    ExchangeImpl exchange;

    protected Event(ExchangeImpl exchangeImpl) {
        this.exchange = exchangeImpl;
    }
}
