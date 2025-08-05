package sun.print;

import java.awt.Graphics;
import java.awt.PrintGraphics;
import java.awt.PrintJob;

/* loaded from: rt.jar:sun/print/ProxyPrintGraphics.class */
public class ProxyPrintGraphics extends ProxyGraphics implements PrintGraphics {
    private PrintJob printJob;

    public ProxyPrintGraphics(Graphics graphics, PrintJob printJob) {
        super(graphics);
        this.printJob = printJob;
    }

    @Override // java.awt.PrintGraphics
    public PrintJob getPrintJob() {
        return this.printJob;
    }

    @Override // sun.print.ProxyGraphics, java.awt.Graphics
    public Graphics create() {
        return new ProxyPrintGraphics(getGraphics().create(), this.printJob);
    }

    @Override // sun.print.ProxyGraphics, java.awt.Graphics
    public Graphics create(int i2, int i3, int i4, int i5) {
        return new ProxyPrintGraphics(getGraphics().create(i2, i3, i4, i5), this.printJob);
    }

    @Override // sun.print.ProxyGraphics
    public Graphics getGraphics() {
        return super.getGraphics();
    }

    @Override // sun.print.ProxyGraphics, java.awt.Graphics
    public void dispose() {
        super.dispose();
    }
}
