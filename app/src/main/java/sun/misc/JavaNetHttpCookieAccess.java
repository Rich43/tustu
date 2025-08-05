package sun.misc;

import java.net.HttpCookie;
import java.util.List;

/* loaded from: rt.jar:sun/misc/JavaNetHttpCookieAccess.class */
public interface JavaNetHttpCookieAccess {
    List<HttpCookie> parse(String str);

    String header(HttpCookie httpCookie);
}
