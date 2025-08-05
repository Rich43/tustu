package javafx.scene.media;

import java.util.Arrays;
import java.util.Collection;
import javafx.util.Builder;

@Deprecated
/* loaded from: jfxrt.jar:javafx/scene/media/MediaBuilder.class */
public final class MediaBuilder implements Builder<Media> {
    private int __set;
    private Runnable onError;
    private String source;
    private Collection<? extends Track> tracks;

    protected MediaBuilder() {
    }

    public static MediaBuilder create() {
        return new MediaBuilder();
    }

    public void applyTo(Media x2) {
        int set = this.__set;
        if ((set & 1) != 0) {
            x2.setOnError(this.onError);
        }
        if ((set & 2) != 0) {
            x2.getTracks().addAll(this.tracks);
        }
    }

    public MediaBuilder onError(Runnable x2) {
        this.onError = x2;
        this.__set |= 1;
        return this;
    }

    public MediaBuilder source(String x2) {
        this.source = x2;
        return this;
    }

    public MediaBuilder tracks(Collection<? extends Track> x2) {
        this.tracks = x2;
        this.__set |= 2;
        return this;
    }

    public MediaBuilder tracks(Track... x2) {
        return tracks(Arrays.asList(x2));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public Media build2() {
        Media x2 = new Media(this.source);
        applyTo(x2);
        return x2;
    }
}
