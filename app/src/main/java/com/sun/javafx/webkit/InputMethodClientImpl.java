package com.sun.javafx.webkit;

import com.sun.javafx.scene.input.ExtendedInputMethodRequests;
import com.sun.webkit.InputMethodClient;
import com.sun.webkit.Invoker;
import com.sun.webkit.WebPage;
import com.sun.webkit.event.WCInputMethodEvent;
import com.sun.webkit.graphics.WCPoint;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Point2D;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.InputMethodHighlight;
import javafx.scene.input.InputMethodTextRun;
import javafx.scene.web.WebView;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/InputMethodClientImpl.class */
public final class InputMethodClientImpl implements InputMethodClient, ExtendedInputMethodRequests {
    private static final Logger log = Logger.getLogger(InputMethodClientImpl.class.getName());
    private final WeakReference<WebView> wvRef;
    private final WebPage webPage;
    private boolean state;

    public InputMethodClientImpl(WebView wv, WebPage webPage) {
        this.wvRef = new WeakReference<>(wv);
        this.webPage = webPage;
        if (webPage != null) {
            webPage.setInputMethodClient(this);
        }
    }

    @Override // com.sun.webkit.InputMethodClient
    public void activateInputMethods(boolean doActivate) {
        WebView wv = this.wvRef.get();
        if (wv != null && wv.getScene() != null) {
            wv.getScene().impl_enableInputMethodEvents(doActivate);
        }
        this.state = doActivate;
    }

    public boolean getInputMethodState() {
        return this.state;
    }

    public static WCInputMethodEvent convertToWCInputMethodEvent(InputMethodEvent ie) {
        List<Integer> underlines = new ArrayList<>();
        StringBuilder composed = new StringBuilder();
        int pos = 0;
        for (InputMethodTextRun run : ie.getComposed()) {
            String rawText = run.getText();
            InputMethodHighlight imh = run.getHighlight();
            underlines.add(Integer.valueOf(pos));
            underlines.add(Integer.valueOf(pos + rawText.length()));
            underlines.add(Integer.valueOf((imh == InputMethodHighlight.SELECTED_CONVERTED || imh == InputMethodHighlight.SELECTED_RAW) ? 1 : 0));
            pos += rawText.length();
            composed.append(rawText);
        }
        int size = underlines.size();
        if (size == 0) {
            underlines.add(0);
            underlines.add(Integer.valueOf(pos));
            underlines.add(0);
            size = underlines.size();
        }
        int[] attributes = new int[size];
        for (int i2 = 0; i2 < size; i2++) {
            attributes[i2] = underlines.get(i2).intValue();
        }
        return new WCInputMethodEvent(ie.getCommitted(), composed.toString(), attributes, ie.getCaretPosition());
    }

    @Override // javafx.scene.input.InputMethodRequests
    public Point2D getTextLocation(int offset) {
        FutureTask<Point2D> f2 = new FutureTask<>(() -> {
            int[] loc = this.webPage.getClientTextLocation(offset);
            WCPoint point = this.webPage.getPageClient().windowToScreen(new WCPoint(loc[0], loc[1] + loc[3]));
            return new Point2D(point.getIntX(), point.getIntY());
        });
        Invoker.getInvoker().invokeOnEventThread(f2);
        Point2D result = null;
        try {
            result = f2.get();
        } catch (InterruptedException ex) {
            log.log(Level.SEVERE, "InputMethodClientImpl.getTextLocation InterruptedException" + ((Object) ex));
        } catch (ExecutionException ex2) {
            log.log(Level.SEVERE, "InputMethodClientImpl.getTextLocation " + ((Object) ex2));
        }
        return result;
    }

    @Override // javafx.scene.input.InputMethodRequests
    public int getLocationOffset(int x2, int y2) {
        FutureTask<Integer> f2 = new FutureTask<>(() -> {
            WCPoint point = this.webPage.getPageClient().windowToScreen(new WCPoint(0.0f, 0.0f));
            return Integer.valueOf(this.webPage.getClientLocationOffset(x2 - point.getIntX(), y2 - point.getIntY()));
        });
        Invoker.getInvoker().invokeOnEventThread(f2);
        int location = 0;
        try {
            location = f2.get().intValue();
        } catch (InterruptedException ex) {
            log.log(Level.SEVERE, "InputMethodClientImpl.getTextLocation InterruptedException" + ((Object) ex));
        } catch (ExecutionException ex2) {
            log.log(Level.SEVERE, "InputMethodClientImpl.getLocationOffset " + ((Object) ex2));
        }
        return location;
    }

    @Override // javafx.scene.input.InputMethodRequests
    public void cancelLatestCommittedText() {
    }

    @Override // javafx.scene.input.InputMethodRequests
    public String getSelectedText() {
        return this.webPage.getClientSelectedText();
    }

    @Override // com.sun.javafx.scene.input.ExtendedInputMethodRequests
    public int getInsertPositionOffset() {
        return this.webPage.getClientInsertPositionOffset();
    }

    @Override // com.sun.javafx.scene.input.ExtendedInputMethodRequests
    public String getCommittedText(int begin, int end) {
        try {
            return this.webPage.getClientCommittedText().substring(begin, end);
        } catch (StringIndexOutOfBoundsException e2) {
            throw new IllegalArgumentException(e2);
        }
    }

    @Override // com.sun.javafx.scene.input.ExtendedInputMethodRequests
    public int getCommittedTextLength() {
        return this.webPage.getClientCommittedTextLength();
    }
}
