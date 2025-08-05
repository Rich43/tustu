package sun.java2d.marlin;

import java.security.AccessController;
import java.util.concurrent.ConcurrentLinkedQueue;
import sun.java2d.marlin.RendererContext;
import sun.java2d.marlin.stats.Histogram;
import sun.java2d.marlin.stats.Monitor;
import sun.java2d.marlin.stats.StatLong;
import sun.misc.ThreadGroupUtils;

/* loaded from: rt.jar:sun/java2d/marlin/RendererStats.class */
public final class RendererStats implements MarlinConst {
    private static volatile RendererStats singleton = null;
    final ConcurrentLinkedQueue<RendererContext> allContexts = new ConcurrentLinkedQueue<>();
    final StatLong stat_cache_rowAA = new StatLong("cache.rowAA");
    final StatLong stat_cache_rowAAChunk = new StatLong("cache.rowAAChunk");
    final StatLong stat_cache_tiles = new StatLong("cache.tiles");
    final StatLong stat_rdr_poly_stack_curves = new StatLong("renderer.poly.stack.curves");
    final StatLong stat_rdr_poly_stack_types = new StatLong("renderer.poly.stack.types");
    final StatLong stat_rdr_addLine = new StatLong("renderer.addLine");
    final StatLong stat_rdr_addLine_skip = new StatLong("renderer.addLine.skip");
    final StatLong stat_rdr_curveBreak = new StatLong("renderer.curveBreakIntoLinesAndAdd");
    final StatLong stat_rdr_curveBreak_dec = new StatLong("renderer.curveBreakIntoLinesAndAdd.dec");
    final StatLong stat_rdr_curveBreak_inc = new StatLong("renderer.curveBreakIntoLinesAndAdd.inc");
    final StatLong stat_rdr_quadBreak = new StatLong("renderer.quadBreakIntoLinesAndAdd");
    final StatLong stat_rdr_quadBreak_dec = new StatLong("renderer.quadBreakIntoLinesAndAdd.dec");
    final StatLong stat_rdr_edges = new StatLong("renderer.edges");
    final StatLong stat_rdr_edges_count = new StatLong("renderer.edges.count");
    final StatLong stat_rdr_edges_resizes = new StatLong("renderer.edges.resize");
    final StatLong stat_rdr_activeEdges = new StatLong("renderer.activeEdges");
    final StatLong stat_rdr_activeEdges_updates = new StatLong("renderer.activeEdges.updates");
    final StatLong stat_rdr_activeEdges_adds = new StatLong("renderer.activeEdges.adds");
    final StatLong stat_rdr_activeEdges_adds_high = new StatLong("renderer.activeEdges.adds_high");
    final StatLong stat_rdr_crossings_updates = new StatLong("renderer.crossings.updates");
    final StatLong stat_rdr_crossings_sorts = new StatLong("renderer.crossings.sorts");
    final StatLong stat_rdr_crossings_bsearch = new StatLong("renderer.crossings.bsearch");
    final StatLong stat_rdr_crossings_msorts = new StatLong("renderer.crossings.msorts");
    final StatLong stat_array_dasher_dasher = new StatLong("array.dasher.dasher.d_float");
    final StatLong stat_array_dasher_firstSegmentsBuffer = new StatLong("array.dasher.firstSegmentsBuffer.d_float");
    final StatLong stat_array_stroker_polystack_curves = new StatLong("array.stroker.polystack.curves.d_float");
    final StatLong stat_array_stroker_polystack_curveTypes = new StatLong("array.stroker.polystack.curveTypes.d_byte");
    final StatLong stat_array_marlincache_rowAAChunk = new StatLong("array.marlincache.rowAAChunk.d_byte");
    final StatLong stat_array_marlincache_touchedTile = new StatLong("array.marlincache.touchedTile.int");
    final StatLong stat_array_renderer_alphaline = new StatLong("array.renderer.alphaline.int");
    final StatLong stat_array_renderer_crossings = new StatLong("array.renderer.crossings.int");
    final StatLong stat_array_renderer_aux_crossings = new StatLong("array.renderer.aux_crossings.int");
    final StatLong stat_array_renderer_edgeBuckets = new StatLong("array.renderer.edgeBuckets.int");
    final StatLong stat_array_renderer_edgeBucketCounts = new StatLong("array.renderer.edgeBucketCounts.int");
    final StatLong stat_array_renderer_edgePtrs = new StatLong("array.renderer.edgePtrs.int");
    final StatLong stat_array_renderer_aux_edgePtrs = new StatLong("array.renderer.aux_edgePtrs.int");
    final Histogram hist_rdr_crossings = new Histogram("renderer.crossings");
    final Histogram hist_rdr_crossings_ratio = new Histogram("renderer.crossings.ratio");
    final Histogram hist_rdr_crossings_adds = new Histogram("renderer.crossings.adds");
    final Histogram hist_rdr_crossings_msorts = new Histogram("renderer.crossings.msorts");
    final Histogram hist_rdr_crossings_msorts_adds = new Histogram("renderer.crossings.msorts.adds");
    final Histogram hist_tile_generator_alpha = new Histogram("tile_generator.alpha");
    final Histogram hist_tile_generator_encoding = new Histogram("tile_generator.encoding");
    final Histogram hist_tile_generator_encoding_dist = new Histogram("tile_generator.encoding.dist");
    final Histogram hist_tile_generator_encoding_ratio = new Histogram("tile_generator.encoding.ratio");
    final Histogram hist_tile_generator_encoding_runLen = new Histogram("tile_generator.encoding.runLen");
    final StatLong[] statistics = {this.stat_cache_rowAA, this.stat_cache_rowAAChunk, this.stat_cache_tiles, this.stat_rdr_poly_stack_types, this.stat_rdr_poly_stack_curves, this.stat_rdr_addLine, this.stat_rdr_addLine_skip, this.stat_rdr_curveBreak, this.stat_rdr_curveBreak_dec, this.stat_rdr_curveBreak_inc, this.stat_rdr_quadBreak, this.stat_rdr_quadBreak_dec, this.stat_rdr_edges, this.stat_rdr_edges_count, this.stat_rdr_edges_resizes, this.stat_rdr_activeEdges, this.stat_rdr_activeEdges_updates, this.stat_rdr_activeEdges_adds, this.stat_rdr_activeEdges_adds_high, this.stat_rdr_crossings_updates, this.stat_rdr_crossings_sorts, this.stat_rdr_crossings_bsearch, this.stat_rdr_crossings_msorts, this.hist_rdr_crossings, this.hist_rdr_crossings_ratio, this.hist_rdr_crossings_adds, this.hist_rdr_crossings_msorts, this.hist_rdr_crossings_msorts_adds, this.hist_tile_generator_alpha, this.hist_tile_generator_encoding, this.hist_tile_generator_encoding_dist, this.hist_tile_generator_encoding_ratio, this.hist_tile_generator_encoding_runLen, this.stat_array_dasher_dasher, this.stat_array_dasher_firstSegmentsBuffer, this.stat_array_stroker_polystack_curves, this.stat_array_stroker_polystack_curveTypes, this.stat_array_marlincache_rowAAChunk, this.stat_array_marlincache_touchedTile, this.stat_array_renderer_alphaline, this.stat_array_renderer_crossings, this.stat_array_renderer_aux_crossings, this.stat_array_renderer_edgeBuckets, this.stat_array_renderer_edgeBucketCounts, this.stat_array_renderer_edgePtrs, this.stat_array_renderer_aux_edgePtrs};
    final Monitor mon_pre_getAATileGenerator = new Monitor("MarlinRenderingEngine.getAATileGenerator()");
    final Monitor mon_npi_currentSegment = new Monitor("NormalizingPathIterator.currentSegment()");
    final Monitor mon_rdr_addLine = new Monitor("Renderer.addLine()");
    final Monitor mon_rdr_endRendering = new Monitor("Renderer.endRendering()");
    final Monitor mon_rdr_endRendering_Y = new Monitor("Renderer._endRendering(Y)");
    final Monitor mon_rdr_copyAARow = new Monitor("Renderer.copyAARow()");
    final Monitor mon_pipe_renderTiles = new Monitor("AAShapePipe.renderTiles()");
    final Monitor mon_ptg_getAlpha = new Monitor("MarlinTileGenerator.getAlpha()");
    final Monitor mon_debug = new Monitor("DEBUG()");
    final Monitor[] monitors = {this.mon_pre_getAATileGenerator, this.mon_npi_currentSegment, this.mon_rdr_addLine, this.mon_rdr_endRendering, this.mon_rdr_endRendering_Y, this.mon_rdr_copyAARow, this.mon_pipe_renderTiles, this.mon_ptg_getAlpha, this.mon_debug};

    static RendererStats getInstance() {
        if (singleton == null) {
            singleton = new RendererStats();
        }
        return singleton;
    }

    public static void dumpStats() {
        if (singleton != null) {
            singleton.dump();
        }
    }

    private RendererStats() {
        AccessController.doPrivileged(() -> {
            Thread thread = new Thread(ThreadGroupUtils.getRootThreadGroup(), new Runnable() { // from class: sun.java2d.marlin.RendererStats.1
                @Override // java.lang.Runnable
                public void run() {
                    RendererStats.this.dump();
                }
            }, "MarlinStatsHook");
            thread.setContextClassLoader(null);
            Runtime.getRuntime().addShutdownHook(thread);
            return null;
        });
    }

    void dump() {
        if (doStats) {
            ArrayCache.dumpStats();
        }
        for (RendererContext rendererContext : (RendererContext[]) this.allContexts.toArray(new RendererContext[this.allContexts.size()])) {
            MarlinUtils.logInfo("RendererContext: " + rendererContext.name);
            if (doStats) {
                for (StatLong statLong : this.statistics) {
                    if (statLong.count != 0) {
                        MarlinUtils.logInfo(statLong.toString());
                        statLong.reset();
                    }
                }
                RendererContext.ArrayCachesHolder arrayCachesHolder = rendererContext.getArrayCachesHolder();
                MarlinUtils.logInfo("Array caches for thread: " + rendererContext.name);
                for (IntArrayCache intArrayCache : arrayCachesHolder.intArrayCaches) {
                    intArrayCache.dumpStats();
                }
                MarlinUtils.logInfo("Dirty Array caches for thread: " + rendererContext.name);
                for (IntArrayCache intArrayCache2 : arrayCachesHolder.dirtyIntArrayCaches) {
                    intArrayCache2.dumpStats();
                }
                for (FloatArrayCache floatArrayCache : arrayCachesHolder.dirtyFloatArrayCaches) {
                    floatArrayCache.dumpStats();
                }
                for (ByteArrayCache byteArrayCache : arrayCachesHolder.dirtyByteArrayCaches) {
                    byteArrayCache.dumpStats();
                }
            }
        }
    }
}
