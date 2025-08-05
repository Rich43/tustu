package org.icepdf.ri.common.views.listeners;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.events.PageImageEvent;
import org.icepdf.core.events.PageInitializingEvent;
import org.icepdf.core.events.PageLoadingEvent;
import org.icepdf.core.events.PageLoadingListener;
import org.icepdf.core.events.PagePaintingEvent;
import org.icepdf.core.pobjects.Page;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/listeners/MetricsPageLoadingListener.class */
public class MetricsPageLoadingListener implements PageLoadingListener {
    private static final Logger logger = Logger.getLogger(MetricsPageLoadingListener.class.toString());
    public static final DecimalFormat formatter = new DecimalFormat("#.###");
    public static final DecimalFormat percentFormatter = new DecimalFormat(FXMLLoader.CONTROLLER_METHOD_PREFIX);
    private int pageIndex;
    private int pageCount;
    private long startLoading;
    private long endLoading;
    private long startInit;
    private long endInit;
    private long imageCount;
    private long imageLoadDuration;
    private long startPaint;
    private long endPaint;
    private long paintCount;

    public MetricsPageLoadingListener(int pageCount) {
        this.pageCount = pageCount;
    }

    @Override // org.icepdf.core.events.PageLoadingListener
    public void pageLoadingStarted(PageLoadingEvent event) {
        this.startLoading = System.nanoTime();
        this.pageIndex = ((Page) event.getSource()).getPageIndex();
        this.imageCount = event.getImageResourceCount();
    }

    @Override // org.icepdf.core.events.PageLoadingListener
    public void pageInitializationStarted(PageInitializingEvent event) {
        this.startInit = System.nanoTime();
    }

    @Override // org.icepdf.core.events.PageLoadingListener
    public void pageInitializationEnded(PageInitializingEvent event) {
        this.endInit = System.nanoTime();
    }

    @Override // org.icepdf.core.events.PageLoadingListener
    public void pageImageLoaded(PageImageEvent event) {
        this.imageLoadDuration += event.getDuration();
    }

    @Override // org.icepdf.core.events.PageLoadingListener
    public void pagePaintingStarted(PagePaintingEvent event) {
        this.startPaint = System.nanoTime();
        this.paintCount = event.getShapesCount();
    }

    @Override // org.icepdf.core.events.PageLoadingListener
    public void pagePaintingEnded(PagePaintingEvent event) {
        this.endPaint = System.nanoTime();
    }

    @Override // org.icepdf.core.events.PageLoadingListener
    public void pageLoadingEnded(PageLoadingEvent event) {
        this.endLoading = System.nanoTime();
        displayConsoleMetrics();
    }

    private void displayConsoleMetrics() {
        System.out.println("Loading page: " + (this.pageIndex + 1) + "/" + this.pageCount);
        double totalTime = convert(this.endLoading - this.startLoading);
        double initTime = convert(this.endInit - this.startInit);
        double paintTime = convert(this.endPaint - this.startPaint);
        double imageTime = convert(this.imageLoadDuration);
        System.out.println("        init time: " + formatter.format(initTime) + "ms (" + percentFormatter.format((initTime / totalTime) * 100.0d) + "%)");
        System.out.println("       paint time: " + formatter.format(paintTime) + "ms (" + percentFormatter.format((paintTime / totalTime) * 100.0d) + "%) " + this.paintCount + " shapes");
        System.out.println("       image time: " + formatter.format(imageTime) + "ms");
        System.out.println("  avg. image time: " + formatter.format(imageTime / this.imageCount) + "ms for " + NumberFormat.getNumberInstance(Locale.US).format(this.imageCount) + " image(s)");
        System.out.println("       total time: " + formatter.format(totalTime));
    }

    private double convert(long duration) {
        return duration / 1.0E9d;
    }
}
