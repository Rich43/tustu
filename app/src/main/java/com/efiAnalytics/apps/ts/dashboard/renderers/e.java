package com.efiAnalytics.apps.ts.dashboard.renderers;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/renderers/e.class */
public class e {

    /* renamed from: a, reason: collision with root package name */
    public static final String f9535a = AnalogBarPainter.NAME;

    /* renamed from: b, reason: collision with root package name */
    public static final String f9536b = AnalogMovingBarGaugePainter.NAME;

    /* renamed from: c, reason: collision with root package name */
    public static final String f9537c = HorizontalDashedBar.NAME;

    /* renamed from: d, reason: collision with root package name */
    public static final String f9538d = RectangleIndicatorPainter.NAME;

    public static GaugePainter a(String str) {
        return (str.equals("Basic Readout") || str.equals("com.efiAnalytics.tunerStudio.renderers.BasicReadoutGaugePainter")) ? new BasicReadoutGaugePainter() : (str.equals("Analog Gauge") || str.equals("com.efiAnalytics.tunerStudio.renderers.AnalogGaugePainter")) ? new AnalogGaugePainter() : (str.equals("Circle Analog Gauge") || str.equals("com.efiAnalytics.tunerStudio.renderers.RoundAnalogGaugePainter")) ? new RoundAnalogGaugePainter() : (str.equals("Vertical Bar Gauge") || str.equals("com.efiAnalytics.tunerStudio.renderers.VerticalBarPainter")) ? new VerticalBarPainter() : (str.equals("Horizontal Bar Gauge") || str.equals("com.efiAnalytics.tunerStudio.renderers.HorizontalBarPainter")) ? new HorizontalBarPainter() : (str.equals("Histogram") || str.equals("com.efiAnalytics.tunerStudio.renderers.HistogramPainter") || str.equals("Line Graph")) ? new HistogramPainter() : (str.equals(f9535a) || str.equals("com.efiAnalytics.tunerStudio.renderers.AnalogBarPainter")) ? new AnalogBarPainter() : (str.equals(f9536b) || str.equals("com.efiAnalytics.tunerStudio.renderers.AnalogMovingBarGaugePainter")) ? new AnalogMovingBarGaugePainter() : (str.equals("Vertical Dashed Bar Gauge") || str.equals("com.efiAnalytics.tunerStudio.renderers.VerticalDashedBarPainter")) ? new VerticalDashedBarPainter() : (str.equals(f9537c) || str.equals("com.efiAnalytics.tunerStudio.renderers.HorizontalDashedBar")) ? new HorizontalDashedBar() : (str.equals("Horizontal Line Gauge") || str.equals("com.efiAnalytics.tunerStudio.renderers.HorizontalLinePainter")) ? new HorizontalLinePainter() : str.equals("Asymetric Sweep Gauge") ? new AsymetricSweepRenderer() : new BasicReadoutGaugePainter();
    }

    public static String a(Object obj) {
        return obj instanceof BasicReadoutGaugePainter ? "Basic Readout" : obj instanceof VerticalBarPainter ? "Vertical Bar Gauge" : obj instanceof HorizontalLinePainter ? "Horizontal Line Gauge" : obj instanceof HorizontalBarPainter ? "Horizontal Bar Gauge" : obj instanceof HistogramPainter ? "Line Graph" : obj instanceof AnalogMovingBarGaugePainter ? f9536b : obj instanceof AnalogBarPainter ? f9535a : obj instanceof VerticalDashedBarPainter ? "Vertical Dashed Bar Gauge" : obj instanceof RoundAnalogGaugePainter ? "Circle Analog Gauge" : obj instanceof AnalogGaugePainter ? "Analog Gauge" : obj instanceof HorizontalDashedBar ? f9537c : obj instanceof AsymetricSweepRenderer ? "Asymetric Sweep Gauge" : obj instanceof RectangleIndicatorPainter ? f9538d : obj instanceof BulbIndicatorPainter ? "Bulb Indicator" : obj instanceof IndicatorPainter ? f9538d : "Analog Gauge";
    }

    public static IndicatorPainter b(String str) {
        return (str.equals(f9538d) || str.equals("com.efiAnalytics.tunerStudio.renderers.RectangleIndicatorPainter")) ? new RectangleIndicatorPainter() : (str.equals("Bulb Indicator") || str.equals("com.efiAnalytics.tunerStudio.renderers.BulbIndicatorPainter")) ? new BulbIndicatorPainter() : new RectangleIndicatorPainter();
    }

    public static GaugePainter[] a() {
        return new GaugePainter[]{new AnalogBarPainter(), new AnalogGaugePainter(), new AnalogMovingBarGaugePainter(), new AsymetricSweepRenderer(), new BasicReadoutGaugePainter(), new RoundAnalogGaugePainter(), new HorizontalDashedBar(), new HorizontalLinePainter(), new HorizontalBarPainter(), new HistogramPainter(), new VerticalBarPainter(), new VerticalDashedBarPainter()};
    }

    public static IndicatorPainter[] b() {
        return new IndicatorPainter[]{new RectangleIndicatorPainter(), new BulbIndicatorPainter()};
    }
}
