package sun.security.provider;

import java.util.Arrays;
import java.util.Objects;

/* loaded from: rt.jar:sun/security/provider/SHA5.class */
abstract class SHA5 extends DigestBase {
    private static final int ITERATION = 80;
    private static final long[] ROUND_CONSTS = {4794697086780616226L, 8158064640168781261L, -5349999486874862801L, -1606136188198331460L, 4131703408338449720L, 6480981068601479193L, -7908458776815382629L, -6116909921290321640L, -2880145864133508542L, 1334009975649890238L, 2608012711638119052L, 6128411473006802146L, 8268148722764581231L, -9160688886553864527L, -7215885187991268811L, -4495734319001033068L, -1973867731355612462L, -1171420211273849373L, 1135362057144423861L, 2597628984639134821L, 3308224258029322869L, 5365058923640841347L, 6679025012923562964L, 8573033837759648693L, -7476448914759557205L, -6327057829258317296L, -5763719355590565569L, -4658551843659510044L, -4116276920077217854L, -3051310485924567259L, 489312712824947311L, 1452737877330783856L, 2861767655752347644L, 3322285676063803686L, 5560940570517711597L, 5996557281743188959L, 7280758554555802590L, 8532644243296465576L, -9096487096722542874L, -7894198246740708037L, -6719396339535248540L, -6333637450476146687L, -4446306890439682159L, -4076793802049405392L, -3345356375505022440L, -2983346525034927856L, -860691631967231958L, 1182934255886127544L, 1847814050463011016L, 2177327727835720531L, 2830643537854262169L, 3796741975233480872L, 4115178125766777443L, 5681478168544905931L, 6601373596472566643L, 7507060721942968483L, 8399075790359081724L, 8693463985226723168L, -8878714635349349518L, -8302665154208450068L, -8016688836872298968L, -6606660893046293015L, -4685533653050689259L, -4147400797238176981L, -3880063495543823972L, -3348786107499101689L, -1523767162380948706L, -757361751448694408L, 500013540394364858L, 748580250866718886L, 1242879168328830382L, 1977374033974150939L, 2944078676154940804L, 3659926193048069267L, 4368137639120453308L, 4836135668995329356L, 5532061633213252278L, 6448918945643986474L, 6902733635092675308L, 7801388544844847127L};

    /* renamed from: W, reason: collision with root package name */
    private long[] f13642W;
    private long[] state;
    private final long[] initialHashes;

    SHA5(String str, int i2, long[] jArr) {
        super(str, i2, 128);
        this.initialHashes = jArr;
        this.state = new long[8];
        this.f13642W = new long[80];
        resetHashes();
    }

    @Override // sun.security.provider.DigestBase
    final void implReset() {
        resetHashes();
        Arrays.fill(this.f13642W, 0L);
    }

    private void resetHashes() {
        System.arraycopy(this.initialHashes, 0, this.state, 0, this.state.length);
    }

    @Override // sun.security.provider.DigestBase
    final void implDigest(byte[] bArr, int i2) {
        long j2 = this.bytesProcessed << 3;
        int i3 = ((int) this.bytesProcessed) & 127;
        engineUpdate(padding, 0, (i3 < 112 ? 112 - i3 : 240 - i3) + 8);
        ByteArrayAccess.i2bBig4((int) (j2 >>> 32), this.buffer, 120);
        ByteArrayAccess.i2bBig4((int) j2, this.buffer, 124);
        implCompress(this.buffer, 0);
        int iEngineGetDigestLength = engineGetDigestLength();
        if (iEngineGetDigestLength == 28) {
            ByteArrayAccess.l2bBig(this.state, 0, bArr, i2, 24);
            ByteArrayAccess.i2bBig4((int) (this.state[3] >> 32), bArr, i2 + 24);
        } else {
            ByteArrayAccess.l2bBig(this.state, 0, bArr, i2, iEngineGetDigestLength);
        }
    }

    private static long lf_ch(long j2, long j3, long j4) {
        return (j2 & j3) ^ ((j2 ^ (-1)) & j4);
    }

    private static long lf_maj(long j2, long j3, long j4) {
        return ((j2 & j3) ^ (j2 & j4)) ^ (j3 & j4);
    }

    private static long lf_R(long j2, int i2) {
        return j2 >>> i2;
    }

    private static long lf_S(long j2, int i2) {
        return (j2 >>> i2) | (j2 << (64 - i2));
    }

    private static long lf_sigma0(long j2) {
        return (lf_S(j2, 28) ^ lf_S(j2, 34)) ^ lf_S(j2, 39);
    }

    private static long lf_sigma1(long j2) {
        return (lf_S(j2, 14) ^ lf_S(j2, 18)) ^ lf_S(j2, 41);
    }

    private static long lf_delta0(long j2) {
        return (lf_S(j2, 1) ^ lf_S(j2, 8)) ^ lf_R(j2, 7);
    }

    private static long lf_delta1(long j2) {
        return (lf_S(j2, 19) ^ lf_S(j2, 61)) ^ lf_R(j2, 6);
    }

    @Override // sun.security.provider.DigestBase
    final void implCompress(byte[] bArr, int i2) {
        implCompressCheck(bArr, i2);
        implCompress0(bArr, i2);
    }

    private void implCompressCheck(byte[] bArr, int i2) {
        Objects.requireNonNull(bArr);
        ByteArrayAccess.b2lBig128(bArr, i2, this.f13642W);
    }

    private final void implCompress0(byte[] bArr, int i2) {
        for (int i3 = 16; i3 < 80; i3++) {
            this.f13642W[i3] = lf_delta1(this.f13642W[i3 - 2]) + this.f13642W[i3 - 7] + lf_delta0(this.f13642W[i3 - 15]) + this.f13642W[i3 - 16];
        }
        long j2 = this.state[0];
        long j3 = this.state[1];
        long j4 = this.state[2];
        long j5 = this.state[3];
        long j6 = this.state[4];
        long j7 = this.state[5];
        long j8 = this.state[6];
        long j9 = this.state[7];
        for (int i4 = 0; i4 < 80; i4++) {
            long jLf_sigma1 = j9 + lf_sigma1(j6) + lf_ch(j6, j7, j8) + ROUND_CONSTS[i4] + this.f13642W[i4];
            long jLf_sigma0 = lf_sigma0(j2) + lf_maj(j2, j3, j4);
            j9 = j8;
            j8 = j7;
            j7 = j6;
            j6 = j5 + jLf_sigma1;
            j5 = j4;
            j4 = j3;
            j3 = j2;
            j2 = jLf_sigma1 + jLf_sigma0;
        }
        long[] jArr = this.state;
        jArr[0] = jArr[0] + j2;
        long[] jArr2 = this.state;
        jArr2[1] = jArr2[1] + j3;
        long[] jArr3 = this.state;
        jArr3[2] = jArr3[2] + j4;
        long[] jArr4 = this.state;
        jArr4[3] = jArr4[3] + j5;
        long[] jArr5 = this.state;
        jArr5[4] = jArr5[4] + j6;
        long[] jArr6 = this.state;
        jArr6[5] = jArr6[5] + j7;
        long[] jArr7 = this.state;
        jArr7[6] = jArr7[6] + j8;
        long[] jArr8 = this.state;
        jArr8[7] = jArr8[7] + j9;
    }

    @Override // sun.security.provider.DigestBase, java.security.MessageDigestSpi
    public Object clone() throws CloneNotSupportedException {
        SHA5 sha5 = (SHA5) super.clone();
        sha5.state = (long[]) sha5.state.clone();
        sha5.f13642W = new long[80];
        return sha5;
    }

    /* loaded from: rt.jar:sun/security/provider/SHA5$SHA512.class */
    public static final class SHA512 extends SHA5 {
        private static final long[] INITIAL_HASHES = {7640891576956012808L, -4942790177534073029L, 4354685564936845355L, -6534734903238641935L, 5840696475078001361L, -7276294671716946913L, 2270897969802886507L, 6620516959819538809L};

        @Override // sun.security.provider.SHA5, sun.security.provider.DigestBase, java.security.MessageDigestSpi
        public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public SHA512() {
            super("SHA-512", 64, INITIAL_HASHES);
        }
    }

    /* loaded from: rt.jar:sun/security/provider/SHA5$SHA384.class */
    public static final class SHA384 extends SHA5 {
        private static final long[] INITIAL_HASHES = {-3766243637369397544L, 7105036623409894663L, -7973340178411365097L, 1526699215303891257L, 7436329637833083697L, -8163818279084223215L, -2662702644619276377L, 5167115440072839076L};

        @Override // sun.security.provider.SHA5, sun.security.provider.DigestBase, java.security.MessageDigestSpi
        public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public SHA384() {
            super("SHA-384", 48, INITIAL_HASHES);
        }
    }

    /* loaded from: rt.jar:sun/security/provider/SHA5$SHA512_224.class */
    public static final class SHA512_224 extends SHA5 {
        private static final long[] INITIAL_HASHES = {-8341449602262348382L, 8350123849800275158L, 2160240930085379202L, 7466358040605728719L, 1111592415079452072L, 8638871050018654530L, 4583966954114332360L, 1230299281376055969L};

        @Override // sun.security.provider.SHA5, sun.security.provider.DigestBase, java.security.MessageDigestSpi
        public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public SHA512_224() {
            super("SHA-512/224", 28, INITIAL_HASHES);
        }
    }

    /* loaded from: rt.jar:sun/security/provider/SHA5$SHA512_256.class */
    public static final class SHA512_256 extends SHA5 {
        private static final long[] INITIAL_HASHES = {2463787394917988140L, -6965556091613846334L, 2563595384472711505L, -7622211418569250115L, -7626776825740460061L, -4729309413028513390L, 3098927326965381290L, 1060366662362279074L};

        @Override // sun.security.provider.SHA5, sun.security.provider.DigestBase, java.security.MessageDigestSpi
        public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public SHA512_256() {
            super("SHA-512/256", 32, INITIAL_HASHES);
        }
    }
}
