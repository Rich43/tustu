package sun.net.httpserver;

/* loaded from: rt.jar:sun/net/httpserver/WriteFinishedEvent.class */
class WriteFinishedEvent extends Event {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !WriteFinishedEvent.class.desiredAssertionStatus();
    }

    WriteFinishedEvent(ExchangeImpl exchangeImpl) {
        super(exchangeImpl);
        if (!$assertionsDisabled && exchangeImpl.writefinished) {
            throw new AssertionError();
        }
        exchangeImpl.writefinished = true;
    }
}
