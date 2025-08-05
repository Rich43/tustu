package sun.net.httpserver;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: rt.jar:sun/net/httpserver/AuthFilter.class */
public class AuthFilter extends Filter {
    private Authenticator authenticator;

    public AuthFilter(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    @Override // com.sun.net.httpserver.Filter
    public String description() {
        return "Authentication filter";
    }

    public void setAuthenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    public void consumeInput(HttpExchange httpExchange) throws IOException {
        InputStream requestBody = httpExchange.getRequestBody();
        while (requestBody.read(new byte[4096]) != -1) {
        }
        requestBody.close();
    }

    @Override // com.sun.net.httpserver.Filter
    public void doFilter(HttpExchange httpExchange, Filter.Chain chain) throws IOException {
        if (this.authenticator != null) {
            Authenticator.Result resultAuthenticate = this.authenticator.authenticate(httpExchange);
            if (resultAuthenticate instanceof Authenticator.Success) {
                ExchangeImpl.get(httpExchange).setPrincipal(((Authenticator.Success) resultAuthenticate).getPrincipal());
                chain.doFilter(httpExchange);
                return;
            } else if (resultAuthenticate instanceof Authenticator.Retry) {
                consumeInput(httpExchange);
                httpExchange.sendResponseHeaders(((Authenticator.Retry) resultAuthenticate).getResponseCode(), -1L);
                return;
            } else {
                if (resultAuthenticate instanceof Authenticator.Failure) {
                    consumeInput(httpExchange);
                    httpExchange.sendResponseHeaders(((Authenticator.Failure) resultAuthenticate).getResponseCode(), -1L);
                    return;
                }
                return;
            }
        }
        chain.doFilter(httpExchange);
    }
}
