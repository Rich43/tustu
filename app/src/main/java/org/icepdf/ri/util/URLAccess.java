package org.icepdf.ri.util;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/util/URLAccess.class */
public class URLAccess {
    public String urlLocation;
    public URL url;
    public InputStream inputStream;
    public String errorMessage;

    public static URLAccess doURLAccess(String urlLocation) {
        URLAccess res = new URLAccess();
        res.urlLocation = urlLocation;
        try {
            res.url = new URL(urlLocation);
            PushbackInputStream in = new PushbackInputStream(new BufferedInputStream(res.url.openStream()), 1);
            int b2 = in.read();
            in.unread(b2);
            res.inputStream = in;
        } catch (FileNotFoundException e2) {
            res.errorMessage = "File Not Found";
        } catch (ConnectException e3) {
            res.errorMessage = "Connection Timed Out";
        } catch (MalformedURLException e4) {
            res.errorMessage = "Malformed URL";
        } catch (UnknownHostException e5) {
            res.errorMessage = "Unknown Host";
        } catch (IOException e6) {
            res.errorMessage = "IO exception";
        }
        return res;
    }

    private URLAccess() {
    }

    public void dispose() {
        this.urlLocation = null;
        this.url = null;
        this.errorMessage = null;
        closeConnection();
    }

    public void closeConnection() {
        if (this.inputStream != null) {
            try {
                this.inputStream.close();
            } catch (Exception e2) {
            }
            this.inputStream = null;
        }
    }
}
