package com.sun.scenario.effect.impl;

import com.sun.scenario.effect.Filterable;
import java.lang.ref.SoftReference;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/ImagePool.class */
public class ImagePool {
    public static long numEffects;
    static long numCreated;
    static long pixelsCreated;
    static long numAccessed;
    static long pixelsAccessed;
    static final int QUANT = 32;
    private final List<SoftReference<PoolFilterable>> unlocked = new ArrayList();
    private final List<SoftReference<PoolFilterable>> locked = new ArrayList();
    private final boolean usePurgatory = Boolean.getBoolean("decora.purgatory");
    private final List<Filterable> hardPurgatory = new ArrayList();
    private final List<SoftReference<PoolFilterable>> softPurgatory = new ArrayList();

    static {
        AccessController.doPrivileged(() -> {
            if (System.getProperty("decora.showstats") != null) {
                Runtime.getRuntime().addShutdownHook(new Thread() { // from class: com.sun.scenario.effect.impl.ImagePool.1
                    @Override // java.lang.Thread, java.lang.Runnable
                    public void run() {
                        ImagePool.printStats();
                    }
                });
                return null;
            }
            return null;
        });
    }

    static void printStats() {
        System.out.println("effects executed:  " + numEffects);
        System.out.println("images created:    " + numCreated);
        System.out.println("pixels created:    " + pixelsCreated);
        System.out.println("images accessed:   " + numAccessed);
        System.out.println("pixels accessed:   " + pixelsAccessed);
        if (numEffects != 0) {
            double avgImgs = numAccessed / numEffects;
            double avgPxls = pixelsAccessed / numEffects;
            System.out.println("images per effect: " + avgImgs);
            System.out.println("pixels per effect: " + avgPxls);
        }
    }

    ImagePool() {
    }

    public synchronized PoolFilterable checkOut(Renderer renderer, int w2, int h2) {
        if (w2 <= 0 || h2 <= 0) {
            h2 = 1;
            w2 = 1;
        }
        int w3 = renderer.getCompatibleWidth((((w2 + 32) - 1) / 32) * 32);
        int h3 = renderer.getCompatibleHeight((((h2 + 32) - 1) / 32) * 32);
        numAccessed++;
        pixelsAccessed += w3 * h3;
        SoftReference<PoolFilterable> chosenEntry = null;
        PoolFilterable chosenImage = null;
        int mindiff = Integer.MAX_VALUE;
        Iterator<SoftReference<PoolFilterable>> entries = this.unlocked.iterator();
        while (entries.hasNext()) {
            SoftReference<PoolFilterable> entry = entries.next();
            PoolFilterable eimg = entry.get();
            if (eimg == null) {
                entries.remove();
            } else {
                int ew = eimg.getMaxContentWidth();
                int eh = eimg.getMaxContentHeight();
                if (ew >= w3 && eh >= h3 && (ew * eh) / 2 <= w3 * h3) {
                    int diff = (ew - w3) * (eh - h3);
                    if (chosenEntry == null || diff < mindiff) {
                        eimg.lock();
                        if (eimg.isLost()) {
                            entries.remove();
                        } else {
                            if (chosenImage != null) {
                                chosenImage.unlock();
                            }
                            chosenEntry = entry;
                            chosenImage = eimg;
                            mindiff = diff;
                        }
                    }
                }
            }
        }
        if (chosenEntry != null) {
            this.unlocked.remove(chosenEntry);
            this.locked.add(chosenEntry);
            renderer.clearImage(chosenImage);
            return chosenImage;
        }
        Iterator<SoftReference<PoolFilterable>> entries2 = this.locked.iterator();
        while (entries2.hasNext()) {
            if (entries2.next().get() == null) {
                entries2.remove();
            }
        }
        PoolFilterable img = null;
        try {
            img = renderer.createCompatibleImage(w3, h3);
        } catch (OutOfMemoryError e2) {
        }
        if (img == null) {
            pruneCache();
            try {
                img = renderer.createCompatibleImage(w3, h3);
            } catch (OutOfMemoryError e3) {
            }
        }
        if (img != null) {
            img.setImagePool(this);
            this.locked.add(new SoftReference<>(img));
            numCreated++;
            pixelsCreated += w3 * h3;
        }
        return img;
    }

    public synchronized void checkIn(PoolFilterable img) {
        SoftReference<PoolFilterable> chosenEntry = null;
        Filterable chosenImage = null;
        Iterator<SoftReference<PoolFilterable>> entries = this.locked.iterator();
        while (true) {
            if (!entries.hasNext()) {
                break;
            }
            SoftReference<PoolFilterable> entry = entries.next();
            Filterable eimg = entry.get();
            if (eimg == null) {
                entries.remove();
            } else if (eimg == img) {
                chosenEntry = entry;
                chosenImage = eimg;
                img.unlock();
                break;
            }
        }
        if (chosenEntry != null) {
            this.locked.remove(chosenEntry);
            if (this.usePurgatory) {
                this.hardPurgatory.add(chosenImage);
                this.softPurgatory.add(chosenEntry);
            } else {
                this.unlocked.add(chosenEntry);
            }
        }
    }

    public synchronized void releasePurgatory() {
        if (this.usePurgatory && !this.softPurgatory.isEmpty()) {
            this.unlocked.addAll(this.softPurgatory);
            this.softPurgatory.clear();
            this.hardPurgatory.clear();
        }
    }

    private void pruneCache() {
        for (SoftReference<PoolFilterable> r2 : this.unlocked) {
            Filterable image = r2.get();
            if (image != null) {
                image.flush();
            }
        }
        this.unlocked.clear();
        System.gc();
        System.runFinalization();
        System.gc();
        System.runFinalization();
    }

    public synchronized void dispose() {
        for (SoftReference<PoolFilterable> r2 : this.unlocked) {
            Filterable image = r2.get();
            if (image != null) {
                image.flush();
            }
        }
        this.unlocked.clear();
        this.locked.clear();
    }
}
