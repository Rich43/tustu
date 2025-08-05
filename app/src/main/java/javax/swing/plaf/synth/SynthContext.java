package javax.swing.plaf.synth;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.JComponent;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthContext.class */
public class SynthContext {
    private static final Queue<SynthContext> queue = new ConcurrentLinkedQueue();
    private JComponent component;
    private Region region;
    private SynthStyle style;
    private int state;

    static SynthContext getContext(JComponent jComponent, SynthStyle synthStyle, int i2) {
        return getContext(jComponent, SynthLookAndFeel.getRegion(jComponent), synthStyle, i2);
    }

    static SynthContext getContext(JComponent jComponent, Region region, SynthStyle synthStyle, int i2) {
        SynthContext synthContextPoll = queue.poll();
        if (synthContextPoll == null) {
            synthContextPoll = new SynthContext();
        }
        synthContextPoll.reset(jComponent, region, synthStyle, i2);
        return synthContextPoll;
    }

    static void releaseContext(SynthContext synthContext) {
        queue.offer(synthContext);
    }

    SynthContext() {
    }

    public SynthContext(JComponent jComponent, Region region, SynthStyle synthStyle, int i2) {
        if (jComponent == null || region == null || synthStyle == null) {
            throw new NullPointerException("You must supply a non-null component, region and style");
        }
        reset(jComponent, region, synthStyle, i2);
    }

    public JComponent getComponent() {
        return this.component;
    }

    public Region getRegion() {
        return this.region;
    }

    boolean isSubregion() {
        return getRegion().isSubregion();
    }

    void setStyle(SynthStyle synthStyle) {
        this.style = synthStyle;
    }

    public SynthStyle getStyle() {
        return this.style;
    }

    void setComponentState(int i2) {
        this.state = i2;
    }

    public int getComponentState() {
        return this.state;
    }

    void reset(JComponent jComponent, Region region, SynthStyle synthStyle, int i2) {
        this.component = jComponent;
        this.region = region;
        this.style = synthStyle;
        this.state = i2;
    }

    void dispose() {
        this.component = null;
        this.style = null;
        releaseContext(this);
    }

    SynthPainter getPainter() {
        SynthPainter painter = getStyle().getPainter(this);
        if (painter != null) {
            return painter;
        }
        return SynthPainter.NULL_PAINTER;
    }
}
