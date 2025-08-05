package sun.util.locale.provider;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import sun.util.locale.provider.LocaleProviderAdapter;

/* loaded from: rt.jar:sun/util/locale/provider/FallbackLocaleProviderAdapter.class */
public class FallbackLocaleProviderAdapter extends JRELocaleProviderAdapter {
    private static final Set<String> rootTagSet = Collections.singleton(Locale.ROOT.toLanguageTag());
    private final LocaleResources rootLocaleResources = new LocaleResources(this, Locale.ROOT);

    @Override // sun.util.locale.provider.JRELocaleProviderAdapter, sun.util.locale.provider.LocaleProviderAdapter
    public LocaleProviderAdapter.Type getAdapterType() {
        return LocaleProviderAdapter.Type.FALLBACK;
    }

    @Override // sun.util.locale.provider.JRELocaleProviderAdapter, sun.util.locale.provider.LocaleProviderAdapter
    public LocaleResources getLocaleResources(Locale locale) {
        return this.rootLocaleResources;
    }

    @Override // sun.util.locale.provider.JRELocaleProviderAdapter
    protected Set<String> createLanguageTagSet(String str) {
        return rootTagSet;
    }
}
