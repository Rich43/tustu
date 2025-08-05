package sun.security.pkcs11.wrapper;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import net.lingala.zip4j.crypto.PBKDF2.BinTools;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/wrapper/Functions.class */
public class Functions {
    private static final Map<Integer, String> mechNames = new HashMap();
    private static final Map<String, Integer> mechIds = new HashMap();
    private static final Map<String, Long> hashMechIds = new HashMap();
    private static final Map<Integer, String> keyNames = new HashMap();
    private static final Map<String, Integer> keyIds = new HashMap();
    private static final Map<Integer, String> attributeNames = new HashMap();
    private static final Map<String, Integer> attributeIds = new HashMap();
    private static final Map<Integer, String> objectClassNames = new HashMap();
    private static final Map<String, Integer> objectClassIds = new HashMap();
    private static final Map<Integer, String> mgfNames = new HashMap();
    private static final Map<String, Integer> mgfIds = new HashMap();
    private static final char[] HEX_DIGITS = BinTools.hex.toCharArray();
    private static final Flags slotInfoFlags = new Flags(new long[]{1, 2, 4}, new String[]{"CKF_TOKEN_PRESENT", "CKF_REMOVABLE_DEVICE", "CKF_HW_SLOT"});
    private static final Flags tokenInfoFlags = new Flags(new long[]{1, 2, 4, 8, 32, 64, 256, 512, 1024, 2048, 65536, 131072, 262144, 524288, 1048576, 2097152, 4194304, PKCS11Constants.CKF_SO_PIN_TO_BE_CHANGED}, new String[]{"CKF_RNG", "CKF_WRITE_PROTECTED", "CKF_LOGIN_REQUIRED", "CKF_USER_PIN_INITIALIZED", "CKF_RESTORE_KEY_NOT_NEEDED", "CKF_CLOCK_ON_TOKEN", "CKF_PROTECTED_AUTHENTICATION_PATH", "CKF_DUAL_CRYPTO_OPERATIONS", "CKF_TOKEN_INITIALIZED", "CKF_SECONDARY_AUTHENTICATION", "CKF_USER_PIN_COUNT_LOW", "CKF_USER_PIN_FINAL_TRY", "CKF_USER_PIN_LOCKED", "CKF_USER_PIN_TO_BE_CHANGED", "CKF_SO_PIN_COUNT_LOW", "CKF_SO_PIN_FINAL_TRY", "CKF_SO_PIN_LOCKED", "CKF_SO_PIN_TO_BE_CHANGED"});
    private static final Flags sessionInfoFlags = new Flags(new long[]{2, 4}, new String[]{"CKF_RW_SESSION", "CKF_SERIAL_SESSION"});
    private static final Flags mechanismInfoFlags = new Flags(new long[]{1, 2, 4, 8, 16, 32, 64, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 1048576, 2097152, 4194304, 4194304, PKCS11Constants.CKF_EC_UNCOMPRESS, PKCS11Constants.CKF_EC_COMPRESS, PKCS11Constants.CKF_EC_CURVENAME, 2147483648L}, new String[]{"CKF_HW", "CKF_MESSAGE_ENCRYPT", "CKF_MESSAGE_DECRYPT", "CKF_MESSAGE_SIGN", "CKF_MESSAGE_VERIFY", "CKF_MULTI_MESSAGE", "CKF_FIND_OBJECTS", "CKF_ENCRYPT", "CKF_DECRYPT", "CKF_DIGEST", "CKF_SIGN", "CKF_SIGN_RECOVER", "CKF_VERIFY", "CKF_VERIFY_RECOVER", "CKF_GENERATE", "CKF_GENERATE_KEY_PAIR", "CKF_WRAP", "CKF_UNWRAP", "CKF_DERIVE", "CKF_EC_F_P", "CKF_EC_F_2M", "CKF_EC_ECPARAMETERS", "CKF_EC_OID", "CKF_EC_UNCOMPRESS", "CKF_EC_COMPRESS", "CKF_EC_CURVENAME", "CKF_EXTENSION"});

    static {
        addMech(0L, "CKM_RSA_PKCS_KEY_PAIR_GEN");
        addMech(1L, "CKM_RSA_PKCS");
        addMech(2L, "CKM_RSA_9796");
        addMech(3L, "CKM_RSA_X_509");
        addMech(4L, "CKM_MD2_RSA_PKCS");
        addMech(5L, "CKM_MD5_RSA_PKCS");
        addMech(6L, "CKM_SHA1_RSA_PKCS");
        addMech(7L, "CKM_RIPEMD128_RSA_PKCS");
        addMech(8L, "CKM_RIPEMD160_RSA_PKCS");
        addMech(9L, "CKM_RSA_PKCS_OAEP");
        addMech(10L, "CKM_RSA_X9_31_KEY_PAIR_GEN");
        addMech(11L, "CKM_RSA_X9_31");
        addMech(12L, "CKM_SHA1_RSA_X9_31");
        addMech(13L, "CKM_RSA_PKCS_PSS");
        addMech(14L, "CKM_SHA1_RSA_PKCS_PSS");
        addMech(16L, "CKM_DSA_KEY_PAIR_GEN");
        addMech(17L, "CKM_DSA");
        addMech(18L, "CKM_DSA_SHA1");
        addMech(19L, "CKM_DSA_SHA224");
        addMech(20L, "CKM_DSA_SHA256");
        addMech(21L, "CKM_DSA_SHA384");
        addMech(22L, "CKM_DSA_SHA512");
        addMech(24L, "CKM_DSA_SHA3_224");
        addMech(25L, "CKM_DSA_SHA3_256");
        addMech(26L, "CKM_DSA_SHA3_384");
        addMech(27L, "CKM_DSA_SHA3_512");
        addMech(32L, "CKM_DH_PKCS_KEY_PAIR_GEN");
        addMech(33L, "CKM_DH_PKCS_DERIVE");
        addMech(48L, "CKM_X9_42_DH_KEY_PAIR_GEN");
        addMech(49L, "CKM_X9_42_DH_DERIVE");
        addMech(50L, "CKM_X9_42_DH_HYBRID_DERIVE");
        addMech(51L, "CKM_X9_42_MQV_DERIVE");
        addMech(64L, "CKM_SHA256_RSA_PKCS");
        addMech(65L, "CKM_SHA384_RSA_PKCS");
        addMech(66L, "CKM_SHA512_RSA_PKCS");
        addMech(67L, "CKM_SHA256_RSA_PKCS_PSS");
        addMech(68L, "CKM_SHA384_RSA_PKCS_PSS");
        addMech(69L, "CKM_SHA512_RSA_PKCS_PSS");
        addMech(70L, "CKM_SHA224_RSA_PKCS");
        addMech(71L, "CKM_SHA224_RSA_PKCS_PSS");
        addMech(72L, "CKM_SHA512_224");
        addMech(73L, "CKM_SHA512_224_HMAC");
        addMech(74L, "CKM_SHA512_224_HMAC_GENERAL");
        addMech(75L, "CKM_SHA512_224_KEY_DERIVATION");
        addMech(76L, "CKM_SHA512_256");
        addMech(77L, "CKM_SHA512_256_HMAC");
        addMech(78L, "CKM_SHA512_256_HMAC_GENERAL");
        addMech(79L, "CKM_SHA512_256_KEY_DERIVATION");
        addMech(80L, "CKM_SHA512_T");
        addMech(81L, "CKM_SHA512_T_HMAC");
        addMech(82L, "CKM_SHA512_T_HMAC_GENERAL");
        addMech(83L, "CKM_SHA512_T_KEY_DERIVATION");
        addMech(96L, "CKM_SHA3_256_RSA_PKCS");
        addMech(97L, "CKM_SHA3_384_RSA_PKCS");
        addMech(98L, "CKM_SHA3_512_RSA_PKCS");
        addMech(99L, "CKM_SHA3_256_RSA_PKCS_PSS");
        addMech(100L, "CKM_SHA3_384_RSA_PKCS_PSS");
        addMech(101L, "CKM_SHA3_512_RSA_PKCS_PSS");
        addMech(102L, "CKM_SHA3_224_RSA_PKCS");
        addMech(103L, "CKM_SHA3_224_RSA_PKCS_PSS");
        addMech(256L, "CKM_RC2_KEY_GEN");
        addMech(257L, "CKM_RC2_ECB");
        addMech(258L, "CKM_RC2_CBC");
        addMech(259L, "CKM_RC2_MAC");
        addMech(260L, "CKM_RC2_MAC_GENERAL");
        addMech(261L, "CKM_RC2_CBC_PAD");
        addMech(272L, "CKM_RC4_KEY_GEN");
        addMech(273L, "CKM_RC4");
        addMech(288L, "CKM_DES_KEY_GEN");
        addMech(289L, "CKM_DES_ECB");
        addMech(290L, "CKM_DES_CBC");
        addMech(291L, "CKM_DES_MAC");
        addMech(292L, "CKM_DES_MAC_GENERAL");
        addMech(293L, "CKM_DES_CBC_PAD");
        addMech(304L, "CKM_DES2_KEY_GEN");
        addMech(305L, "CKM_DES3_KEY_GEN");
        addMech(306L, "CKM_DES3_ECB");
        addMech(307L, "CKM_DES3_CBC");
        addMech(308L, "CKM_DES3_MAC");
        addMech(309L, "CKM_DES3_MAC_GENERAL");
        addMech(310L, "CKM_DES3_CBC_PAD");
        addMech(311L, "CKM_DES3_CMAC_GENERAL");
        addMech(312L, "CKM_DES3_CMAC");
        addMech(320L, "CKM_CDMF_KEY_GEN");
        addMech(321L, "CKM_CDMF_ECB");
        addMech(322L, "CKM_CDMF_CBC");
        addMech(323L, "CKM_CDMF_MAC");
        addMech(324L, "CKM_CDMF_MAC_GENERAL");
        addMech(325L, "CKM_CDMF_CBC_PAD");
        addMech(336L, "CKM_DES_OFB64");
        addMech(337L, "CKM_DES_OFB8");
        addMech(338L, "CKM_DES_CFB64");
        addMech(339L, "CKM_DES_CFB8");
        addMech(512L, "CKM_MD2");
        addMech(513L, "CKM_MD2_HMAC");
        addMech(514L, "CKM_MD2_HMAC_GENERAL");
        addMech(528L, "CKM_MD5");
        addMech(529L, "CKM_MD5_HMAC");
        addMech(530L, "CKM_MD5_HMAC_GENERAL");
        addMech(544L, "CKM_SHA_1");
        addMech(545L, "CKM_SHA_1_HMAC");
        addMech(546L, "CKM_SHA_1_HMAC_GENERAL");
        addMech(560L, "CKM_RIPEMD128");
        addMech(561L, "CKM_RIPEMD128_HMAC");
        addMech(562L, "CKM_RIPEMD128_HMAC_GENERAL");
        addMech(576L, "CKM_RIPEMD160");
        addMech(577L, "CKM_RIPEMD160_HMAC");
        addMech(578L, "CKM_RIPEMD160_HMAC_GENERAL");
        addMech(592L, "CKM_SHA256");
        addMech(593L, "CKM_SHA256_HMAC");
        addMech(594L, "CKM_SHA256_HMAC_GENERAL");
        addMech(597L, "CKM_SHA224");
        addMech(598L, "CKM_SHA224_HMAC");
        addMech(599L, "CKM_SHA224_HMAC_GENERAL");
        addMech(608L, "CKM_SHA384");
        addMech(609L, "CKM_SHA384_HMAC");
        addMech(610L, "CKM_SHA384_HMAC_GENERAL");
        addMech(624L, "CKM_SHA512");
        addMech(625L, "CKM_SHA512_HMAC");
        addMech(626L, "CKM_SHA512_HMAC_GENERAL");
        addMech(640L, "CKM_SECURID_KEY_GEN");
        addMech(642L, "CKM_SECURID");
        addMech(656L, "CKM_HOTP_KEY_GEN");
        addMech(657L, "CKM_HOTP");
        addMech(672L, "CKM_ACTI");
        addMech(673L, "CKM_ACTI_KEY_GEN");
        addMech(688L, "CKM_SHA3_256");
        addMech(689L, "CKM_SHA3_256_HMAC");
        addMech(690L, "CKM_SHA3_256_HMAC_GENERAL");
        addMech(691L, "CKM_SHA3_256_KEY_GEN");
        addMech(693L, "CKM_SHA3_224");
        addMech(694L, "CKM_SHA3_224_HMAC");
        addMech(695L, "CKM_SHA3_224_HMAC_GENERAL");
        addMech(696L, "CKM_SHA3_224_KEY_GEN");
        addMech(704L, "CKM_SHA3_384");
        addMech(705L, "CKM_SHA3_384_HMAC");
        addMech(706L, "CKM_SHA3_384_HMAC_GENERAL");
        addMech(707L, "CKM_SHA3_384_KEY_GEN");
        addMech(720L, "CKM_SHA3_512");
        addMech(721L, "CKM_SHA3_512_HMAC");
        addMech(722L, "CKM_SHA3_512_HMAC_GENERAL");
        addMech(723L, "CKM_SHA3_512_KEY_GEN");
        addMech(768L, "CKM_CAST_KEY_GEN");
        addMech(769L, "CKM_CAST_ECB");
        addMech(770L, "CKM_CAST_CBC");
        addMech(771L, "CKM_CAST_MAC");
        addMech(772L, "CKM_CAST_MAC_GENERAL");
        addMech(773L, "CKM_CAST_CBC_PAD");
        addMech(784L, "CKM_CAST3_KEY_GEN");
        addMech(785L, "CKM_CAST3_ECB");
        addMech(786L, "CKM_CAST3_CBC");
        addMech(787L, "CKM_CAST3_MAC");
        addMech(788L, "CKM_CAST3_MAC_GENERAL");
        addMech(789L, "CKM_CAST3_CBC_PAD");
        addMech(800L, "CKM_CAST128_KEY_GEN");
        addMech(801L, "CKM_CAST128_ECB");
        addMech(802L, "CKM_CAST128_CBC");
        addMech(803L, "CKM_CAST128_MAC");
        addMech(804L, "CKM_CAST128_MAC_GENERAL");
        addMech(805L, "CKM_CAST128_CBC_PAD");
        addMech(816L, "CKM_RC5_KEY_GEN");
        addMech(817L, "CKM_RC5_ECB");
        addMech(818L, "CKM_RC5_CBC");
        addMech(819L, "CKM_RC5_MAC");
        addMech(820L, "CKM_RC5_MAC_GENERAL");
        addMech(821L, "CKM_RC5_CBC_PAD");
        addMech(832L, "CKM_IDEA_KEY_GEN");
        addMech(833L, "CKM_IDEA_ECB");
        addMech(834L, "CKM_IDEA_CBC");
        addMech(835L, "CKM_IDEA_MAC");
        addMech(836L, "CKM_IDEA_MAC_GENERAL");
        addMech(837L, "CKM_IDEA_CBC_PAD");
        addMech(848L, "CKM_GENERIC_SECRET_KEY_GEN");
        addMech(864L, "CKM_CONCATENATE_BASE_AND_KEY");
        addMech(866L, "CKM_CONCATENATE_BASE_AND_DATA");
        addMech(867L, "CKM_CONCATENATE_DATA_AND_BASE");
        addMech(868L, "CKM_XOR_BASE_AND_DATA");
        addMech(869L, "CKM_EXTRACT_KEY_FROM_KEY");
        addMech(880L, "CKM_SSL3_PRE_MASTER_KEY_GEN");
        addMech(881L, "CKM_SSL3_MASTER_KEY_DERIVE");
        addMech(882L, "CKM_SSL3_KEY_AND_MAC_DERIVE");
        addMech(883L, "CKM_SSL3_MASTER_KEY_DERIVE_DH");
        addMech(884L, "CKM_TLS_PRE_MASTER_KEY_GEN");
        addMech(885L, "CKM_TLS_MASTER_KEY_DERIVE");
        addMech(886L, "CKM_TLS_KEY_AND_MAC_DERIVE");
        addMech(887L, "CKM_TLS_MASTER_KEY_DERIVE_DH");
        addMech(888L, "CKM_TLS_PRF");
        addMech(896L, "CKM_SSL3_MD5_MAC");
        addMech(897L, "CKM_SSL3_SHA1_MAC");
        addMech(912L, "CKM_MD5_KEY_DERIVATION");
        addMech(913L, "CKM_MD2_KEY_DERIVATION");
        addMech(914L, "CKM_SHA1_KEY_DERIVATION");
        addMech(915L, "CKM_SHA256_KEY_DERIVATION");
        addMech(916L, "CKM_SHA384_KEY_DERIVATION");
        addMech(917L, "CKM_SHA512_KEY_DERIVATION");
        addMech(918L, "CKM_SHA224_KEY_DERIVATION");
        addMech(919L, "CKM_SHA3_256_KEY_DERIVATION");
        addMech(920L, "CKM_SHA3_224_KEY_DERIVATION");
        addMech(921L, "CKM_SHA3_384_KEY_DERIVATION");
        addMech(922L, "CKM_SHA3_512_KEY_DERIVATION");
        addMech(923L, "CKM_SHAKE_128_KEY_DERIVATION");
        addMech(924L, "CKM_SHAKE_256_KEY_DERIVATION");
        addMech(928L, "CKM_PBE_MD2_DES_CBC");
        addMech(929L, "CKM_PBE_MD5_DES_CBC");
        addMech(930L, "CKM_PBE_MD5_CAST_CBC");
        addMech(931L, "CKM_PBE_MD5_CAST3_CBC");
        addMech(932L, "CKM_PBE_MD5_CAST128_CBC");
        addMech(933L, "CKM_PBE_SHA1_CAST128_CBC");
        addMech(934L, "CKM_PBE_SHA1_RC4_128");
        addMech(935L, "CKM_PBE_SHA1_RC4_40");
        addMech(936L, "CKM_PBE_SHA1_DES3_EDE_CBC");
        addMech(937L, "CKM_PBE_SHA1_DES2_EDE_CBC");
        addMech(938L, "CKM_PBE_SHA1_RC2_128_CBC");
        addMech(939L, "CKM_PBE_SHA1_RC2_40_CBC");
        addMech(944L, "CKM_PKCS5_PBKD2");
        addMech(960L, "CKM_PBA_SHA1_WITH_SHA1_HMAC");
        addMech(976L, "CKM_WTLS_PRE_MASTER_KEY_GEN");
        addMech(977L, "CKM_WTLS_MASTER_KEY_DERIVE");
        addMech(978L, "CKM_WTLS_MASTER_KEY_DERIVE_DH_ECC");
        addMech(979L, "CKM_WTLS_PRF");
        addMech(980L, "CKM_WTLS_SERVER_KEY_AND_MAC_DERIVE");
        addMech(981L, "CKM_WTLS_CLIENT_KEY_AND_MAC_DERIVE");
        addMech(982L, "CKM_TLS10_MAC_SERVER");
        addMech(983L, "CKM_TLS10_MAC_CLIENT");
        addMech(984L, "CKM_TLS12_MAC");
        addMech(985L, "CKM_TLS12_KDF");
        addMech(992L, "CKM_TLS12_MASTER_KEY_DERIVE");
        addMech(993L, "CKM_TLS12_KEY_AND_MAC_DERIVE");
        addMech(994L, "CKM_TLS12_MASTER_KEY_DERIVE_DH");
        addMech(995L, "CKM_TLS12_KEY_SAFE_DERIVE");
        addMech(996L, "CKM_TLS_MAC");
        addMech(997L, "CKM_TLS_KDF");
        addMech(1024L, "CKM_KEY_WRAP_LYNKS");
        addMech(1025L, "CKM_KEY_WRAP_SET_OAEP");
        addMech(1280L, "CKM_CMS_SIG");
        addMech(PKCS11Constants.CKM_KIP_DERIVE, "CKM_KIP_DERIVE");
        addMech(PKCS11Constants.CKM_KIP_WRAP, "CKM_KIP_WRAP");
        addMech(PKCS11Constants.CKM_KIP_MAC, "CKM_KIP_MAC");
        addMech(PKCS11Constants.CKM_CAMELLIA_KEY_GEN, "CKM_CAMELLIA_KEY_GEN");
        addMech(PKCS11Constants.CKM_CAMELLIA_ECB, "CKM_CAMELLIA_ECB");
        addMech(PKCS11Constants.CKM_CAMELLIA_CBC, "CKM_CAMELLIA_CBC");
        addMech(PKCS11Constants.CKM_CAMELLIA_MAC, "CKM_CAMELLIA_MAC");
        addMech(PKCS11Constants.CKM_CAMELLIA_MAC_GENERAL, "CKM_CAMELLIA_MAC_GENERAL");
        addMech(PKCS11Constants.CKM_CAMELLIA_CBC_PAD, "CKM_CAMELLIA_CBC_PAD");
        addMech(PKCS11Constants.CKM_CAMELLIA_ECB_ENCRYPT_DATA, "CKM_CAMELLIA_ECB_ENCRYPT_DATA");
        addMech(PKCS11Constants.CKM_CAMELLIA_CBC_ENCRYPT_DATA, "CKM_CAMELLIA_CBC_ENCRYPT_DATA");
        addMech(PKCS11Constants.CKM_CAMELLIA_CTR, "CKM_CAMELLIA_CTR");
        addMech(PKCS11Constants.CKM_ARIA_KEY_GEN, "CKM_ARIA_KEY_GEN");
        addMech(PKCS11Constants.CKM_ARIA_ECB, "CKM_ARIA_ECB");
        addMech(PKCS11Constants.CKM_ARIA_CBC, "CKM_ARIA_CBC");
        addMech(PKCS11Constants.CKM_ARIA_MAC, "CKM_ARIA_MAC");
        addMech(PKCS11Constants.CKM_ARIA_MAC_GENERAL, "CKM_ARIA_MAC_GENERAL");
        addMech(PKCS11Constants.CKM_ARIA_CBC_PAD, "CKM_ARIA_CBC_PAD");
        addMech(PKCS11Constants.CKM_ARIA_ECB_ENCRYPT_DATA, "CKM_ARIA_ECB_ENCRYPT_DATA");
        addMech(PKCS11Constants.CKM_ARIA_CBC_ENCRYPT_DATA, "CKM_ARIA_CBC_ENCRYPT_DATA");
        addMech(PKCS11Constants.CKM_SEED_KEY_GEN, "CKM_SEED_KEY_GEN");
        addMech(PKCS11Constants.CKM_SEED_ECB, "CKM_SEED_ECB");
        addMech(PKCS11Constants.CKM_SEED_CBC, "CKM_SEED_CBC");
        addMech(PKCS11Constants.CKM_SEED_MAC, "CKM_SEED_MAC");
        addMech(PKCS11Constants.CKM_SEED_MAC_GENERAL, "CKM_SEED_MAC_GENERAL");
        addMech(PKCS11Constants.CKM_SEED_CBC_PAD, "CKM_SEED_CBC_PAD");
        addMech(PKCS11Constants.CKM_SEED_ECB_ENCRYPT_DATA, "CKM_SEED_ECB_ENCRYPT_DATA");
        addMech(PKCS11Constants.CKM_SEED_CBC_ENCRYPT_DATA, "CKM_SEED_CBC_ENCRYPT_DATA");
        addMech(4096L, "CKM_SKIPJACK_KEY_GEN");
        addMech(PKCS11Constants.CKM_SKIPJACK_ECB64, "CKM_SKIPJACK_ECB64");
        addMech(PKCS11Constants.CKM_SKIPJACK_CBC64, "CKM_SKIPJACK_CBC64");
        addMech(PKCS11Constants.CKM_SKIPJACK_OFB64, "CKM_SKIPJACK_OFB64");
        addMech(PKCS11Constants.CKM_SKIPJACK_CFB64, "CKM_SKIPJACK_CFB64");
        addMech(PKCS11Constants.CKM_SKIPJACK_CFB32, "CKM_SKIPJACK_CFB32");
        addMech(PKCS11Constants.CKM_SKIPJACK_CFB16, "CKM_SKIPJACK_CFB16");
        addMech(PKCS11Constants.CKM_SKIPJACK_CFB8, "CKM_SKIPJACK_CFB8");
        addMech(PKCS11Constants.CKM_SKIPJACK_WRAP, "CKM_SKIPJACK_WRAP");
        addMech(PKCS11Constants.CKM_SKIPJACK_PRIVATE_WRAP, "CKM_SKIPJACK_PRIVATE_WRAP");
        addMech(PKCS11Constants.CKM_SKIPJACK_RELAYX, "CKM_SKIPJACK_RELAYX");
        addMech(PKCS11Constants.CKM_KEA_KEY_PAIR_GEN, "CKM_KEA_KEY_PAIR_GEN");
        addMech(PKCS11Constants.CKM_KEA_KEY_DERIVE, "CKM_KEA_KEY_DERIVE");
        addMech(PKCS11Constants.CKM_FORTEZZA_TIMESTAMP, "CKM_FORTEZZA_TIMESTAMP");
        addMech(PKCS11Constants.CKM_BATON_KEY_GEN, "CKM_BATON_KEY_GEN");
        addMech(PKCS11Constants.CKM_BATON_ECB128, "CKM_BATON_ECB128");
        addMech(PKCS11Constants.CKM_BATON_ECB96, "CKM_BATON_ECB96");
        addMech(PKCS11Constants.CKM_BATON_CBC128, "CKM_BATON_CBC128");
        addMech(PKCS11Constants.CKM_BATON_COUNTER, "CKM_BATON_COUNTER");
        addMech(PKCS11Constants.CKM_BATON_SHUFFLE, "CKM_BATON_SHUFFLE");
        addMech(PKCS11Constants.CKM_BATON_WRAP, "CKM_BATON_WRAP");
        addMech(4160L, "CKM_EC_KEY_PAIR_GEN");
        addMech(PKCS11Constants.CKM_EC_KEY_PAIR_GEN_W_EXTRA_BITS, "CKM_EC_KEY_PAIR_GEN_W_EXTRA_BITS");
        addMech(PKCS11Constants.CKM_ECDSA, "CKM_ECDSA");
        addMech(PKCS11Constants.CKM_ECDSA_SHA1, "CKM_ECDSA_SHA1");
        addMech(PKCS11Constants.CKM_ECDSA_SHA224, "CKM_ECDSA_SHA224");
        addMech(PKCS11Constants.CKM_ECDSA_SHA256, "CKM_ECDSA_SHA256");
        addMech(PKCS11Constants.CKM_ECDSA_SHA384, "CKM_ECDSA_SHA384");
        addMech(PKCS11Constants.CKM_ECDSA_SHA512, "CKM_ECDSA_SHA512");
        addMech(PKCS11Constants.CKM_ECDSA_SHA3_224, "CKM_ECDSA_SHA3_224");
        addMech(PKCS11Constants.CKM_ECDSA_SHA3_256, "CKM_ECDSA_SHA3_256");
        addMech(PKCS11Constants.CKM_ECDSA_SHA3_384, "CKM_ECDSA_SHA3_384");
        addMech(PKCS11Constants.CKM_ECDSA_SHA3_512, "CKM_ECDSA_SHA3_512");
        addMech(PKCS11Constants.CKM_ECDH1_DERIVE, "CKM_ECDH1_DERIVE");
        addMech(PKCS11Constants.CKM_ECDH1_COFACTOR_DERIVE, "CKM_ECDH1_COFACTOR_DERIVE");
        addMech(PKCS11Constants.CKM_ECMQV_DERIVE, "CKM_ECMQV_DERIVE");
        addMech(PKCS11Constants.CKM_ECDH_AES_KEY_WRAP, "CKM_ECDH_AES_KEY_WRAP");
        addMech(PKCS11Constants.CKM_RSA_AES_KEY_WRAP, "CKM_RSA_AES_KEY_WRAP");
        addMech(PKCS11Constants.CKM_EC_EDWARDS_KEY_PAIR_GEN, "CKM_EC_EDWARDS_KEY_PAIR_GEN");
        addMech(PKCS11Constants.CKM_EC_MONTGOMERY_KEY_PAIR_GEN, "CKM_EC_MONTGOMERY_KEY_PAIR_GEN");
        addMech(PKCS11Constants.CKM_EDDSA, "CKM_EDDSA");
        addMech(PKCS11Constants.CKM_JUNIPER_KEY_GEN, "CKM_JUNIPER_KEY_GEN");
        addMech(PKCS11Constants.CKM_JUNIPER_ECB128, "CKM_JUNIPER_ECB128");
        addMech(PKCS11Constants.CKM_JUNIPER_CBC128, "CKM_JUNIPER_CBC128");
        addMech(PKCS11Constants.CKM_JUNIPER_COUNTER, "CKM_JUNIPER_COUNTER");
        addMech(PKCS11Constants.CKM_JUNIPER_SHUFFLE, "CKM_JUNIPER_SHUFFLE");
        addMech(PKCS11Constants.CKM_JUNIPER_WRAP, "CKM_JUNIPER_WRAP");
        addMech(PKCS11Constants.CKM_FASTHASH, "CKM_FASTHASH");
        addMech(PKCS11Constants.CKM_AES_XTS, "CKM_AES_XTS");
        addMech(PKCS11Constants.CKM_AES_XTS_KEY_GEN, "CKM_AES_XTS_KEY_GEN");
        addMech(PKCS11Constants.CKM_AES_KEY_GEN, "CKM_AES_KEY_GEN");
        addMech(PKCS11Constants.CKM_AES_ECB, "CKM_AES_ECB");
        addMech(PKCS11Constants.CKM_AES_CBC, "CKM_AES_CBC");
        addMech(PKCS11Constants.CKM_AES_MAC, "CKM_AES_MAC");
        addMech(PKCS11Constants.CKM_AES_MAC_GENERAL, "CKM_AES_MAC_GENERAL");
        addMech(PKCS11Constants.CKM_AES_CBC_PAD, "CKM_AES_CBC_PAD");
        addMech(PKCS11Constants.CKM_AES_CTR, "CKM_AES_CTR");
        addMech(PKCS11Constants.CKM_AES_GCM, "CKM_AES_GCM");
        addMech(PKCS11Constants.CKM_AES_CCM, "CKM_AES_CCM");
        addMech(PKCS11Constants.CKM_AES_CTS, "CKM_AES_CTS");
        addMech(PKCS11Constants.CKM_AES_CMAC, "CKM_AES_CMAC");
        addMech(PKCS11Constants.CKM_AES_CMAC_GENERAL, "CKM_AES_CMAC_GENERAL");
        addMech(PKCS11Constants.CKM_AES_XCBC_MAC, "CKM_AES_XCBC_MAC");
        addMech(PKCS11Constants.CKM_AES_XCBC_MAC_96, "CKM_AES_XCBC_MAC_96");
        addMech(PKCS11Constants.CKM_AES_GMAC, "CKM_AES_GMAC");
        addMech(PKCS11Constants.CKM_BLOWFISH_KEY_GEN, "CKM_BLOWFISH_KEY_GEN");
        addMech(PKCS11Constants.CKM_BLOWFISH_CBC, "CKM_BLOWFISH_CBC");
        addMech(PKCS11Constants.CKM_TWOFISH_KEY_GEN, "CKM_TWOFISH_KEY_GEN");
        addMech(PKCS11Constants.CKM_TWOFISH_CBC, "CKM_TWOFISH_CBC");
        addMech(PKCS11Constants.CKM_BLOWFISH_CBC_PAD, "CKM_BLOWFISH_CBC_PAD");
        addMech(PKCS11Constants.CKM_TWOFISH_CBC_PAD, "CKM_TWOFISH_CBC_PAD");
        addMech(PKCS11Constants.CKM_DES_ECB_ENCRYPT_DATA, "CKM_DES_ECB_ENCRYPT_DATA");
        addMech(PKCS11Constants.CKM_DES_CBC_ENCRYPT_DATA, "CKM_DES_CBC_ENCRYPT_DATA");
        addMech(PKCS11Constants.CKM_DES3_ECB_ENCRYPT_DATA, "CKM_DES3_ECB_ENCRYPT_DATA");
        addMech(PKCS11Constants.CKM_DES3_CBC_ENCRYPT_DATA, "CKM_DES3_CBC_ENCRYPT_DATA");
        addMech(PKCS11Constants.CKM_AES_ECB_ENCRYPT_DATA, "CKM_AES_ECB_ENCRYPT_DATA");
        addMech(PKCS11Constants.CKM_AES_CBC_ENCRYPT_DATA, "CKM_AES_CBC_ENCRYPT_DATA");
        addMech(PKCS11Constants.CKM_GOSTR3410_KEY_PAIR_GEN, "CKM_GOSTR3410_KEY_PAIR_GEN");
        addMech(PKCS11Constants.CKM_GOSTR3410, "CKM_GOSTR3410");
        addMech(PKCS11Constants.CKM_GOSTR3410_WITH_GOSTR3411, "CKM_GOSTR3410_WITH_GOSTR3411");
        addMech(PKCS11Constants.CKM_GOSTR3410_KEY_WRAP, "CKM_GOSTR3410_KEY_WRAP");
        addMech(PKCS11Constants.CKM_GOSTR3410_DERIVE, "CKM_GOSTR3410_DERIVE");
        addMech(PKCS11Constants.CKM_GOSTR3411, "CKM_GOSTR3411");
        addMech(PKCS11Constants.CKM_GOSTR3411_HMAC, "CKM_GOSTR3411_HMAC");
        addMech(PKCS11Constants.CKM_GOST28147_KEY_GEN, "CKM_GOST28147_KEY_GEN");
        addMech(PKCS11Constants.CKM_GOST28147_ECB, "CKM_GOST28147_ECB");
        addMech(PKCS11Constants.CKM_GOST28147, "CKM_GOST28147");
        addMech(PKCS11Constants.CKM_GOST28147_MAC, "CKM_GOST28147_MAC");
        addMech(PKCS11Constants.CKM_GOST28147_KEY_WRAP, "CKM_GOST28147_KEY_WRAP");
        addMech(PKCS11Constants.CKM_CHACHA20_KEY_GEN, "CKM_CHACHA20_KEY_GEN");
        addMech(PKCS11Constants.CKM_CHACHA20, "CKM_CHACHA20");
        addMech(PKCS11Constants.CKM_POLY1305_KEY_GEN, "CKM_POLY1305_KEY_GEN");
        addMech(PKCS11Constants.CKM_POLY1305, "CKM_POLY1305");
        addMech(8192L, "CKM_DSA_PARAMETER_GEN");
        addMech(PKCS11Constants.CKM_DH_PKCS_PARAMETER_GEN, "CKM_DH_PKCS_PARAMETER_GEN");
        addMech(PKCS11Constants.CKM_X9_42_DH_PARAMETER_GEN, "CKM_X9_42_DH_PARAMETER_GEN");
        addMech(PKCS11Constants.CKM_DSA_PROBABLISTIC_PARAMETER_GEN, "CKM_DSA_PROBABLISTIC_PARAMETER_GEN");
        addMech(PKCS11Constants.CKM_DSA_SHAWE_TAYLOR_PARAMETER_GEN, "CKM_DSA_SHAWE_TAYLOR_PARAMETER_GEN");
        addMech(PKCS11Constants.CKM_DSA_FIPS_G_GEN, "CKM_DSA_FIPS_G_GEN");
        addMech(PKCS11Constants.CKM_AES_OFB, "CKM_AES_OFB");
        addMech(PKCS11Constants.CKM_AES_CFB64, "CKM_AES_CFB64");
        addMech(PKCS11Constants.CKM_AES_CFB8, "CKM_AES_CFB8");
        addMech(PKCS11Constants.CKM_AES_CFB128, "CKM_AES_CFB128");
        addMech(PKCS11Constants.CKM_AES_CFB1, "CKM_AES_CFB1");
        addMech(PKCS11Constants.CKM_AES_KEY_WRAP, "CKM_AES_KEY_WRAP");
        addMech(PKCS11Constants.CKM_AES_KEY_WRAP_PAD, "CKM_AES_KEY_WRAP_PAD");
        addMech(PKCS11Constants.CKM_AES_KEY_WRAP_KWP, "CKM_AES_KEY_WRAP_KWP");
        addMech(PKCS11Constants.CKM_RSA_PKCS_TPM_1_1, "CKM_RSA_PKCS_TPM_1_1");
        addMech(PKCS11Constants.CKM_RSA_PKCS_OAEP_TPM_1_1, "CKM_RSA_PKCS_OAEP_TPM_1_1");
        addMech(PKCS11Constants.CKM_SHA_1_KEY_GEN, "CKM_SHA_1_KEY_GEN");
        addMech(PKCS11Constants.CKM_SHA224_KEY_GEN, "CKM_SHA224_KEY_GEN");
        addMech(PKCS11Constants.CKM_SHA256_KEY_GEN, "CKM_SHA256_KEY_GEN");
        addMech(PKCS11Constants.CKM_SHA384_KEY_GEN, "CKM_SHA384_KEY_GEN");
        addMech(PKCS11Constants.CKM_SHA512_KEY_GEN, "CKM_SHA512_KEY_GEN");
        addMech(PKCS11Constants.CKM_SHA512_224_KEY_GEN, "CKM_SHA512_224_KEY_GEN");
        addMech(PKCS11Constants.CKM_SHA512_256_KEY_GEN, "CKM_SHA512_256_KEY_GEN");
        addMech(PKCS11Constants.CKM_SHA512_T_KEY_GEN, "CKM_SHA512_T_KEY_GEN");
        addMech(PKCS11Constants.CKM_NULL, "CKM_NULL");
        addMech(PKCS11Constants.CKM_BLAKE2B_160, "CKM_BLAKE2B_160");
        addMech(PKCS11Constants.CKM_BLAKE2B_160_HMAC, "CKM_BLAKE2B_160_HMAC");
        addMech(PKCS11Constants.CKM_BLAKE2B_160_HMAC_GENERAL, "CKM_BLAKE2B_160_HMAC_GENERAL");
        addMech(PKCS11Constants.CKM_BLAKE2B_160_KEY_DERIVE, "CKM_BLAKE2B_160_KEY_DERIVE");
        addMech(PKCS11Constants.CKM_BLAKE2B_160_KEY_GEN, "CKM_BLAKE2B_160_KEY_GEN");
        addMech(PKCS11Constants.CKM_BLAKE2B_256, "CKM_BLAKE2B_256");
        addMech(PKCS11Constants.CKM_BLAKE2B_256_HMAC, "CKM_BLAKE2B_256_HMAC");
        addMech(PKCS11Constants.CKM_BLAKE2B_256_HMAC_GENERAL, "CKM_BLAKE2B_256_HMAC_GENERAL");
        addMech(PKCS11Constants.CKM_BLAKE2B_256_KEY_DERIVE, "CKM_BLAKE2B_256_KEY_DERIVE");
        addMech(PKCS11Constants.CKM_BLAKE2B_256_KEY_GEN, "CKM_BLAKE2B_256_KEY_GEN");
        addMech(PKCS11Constants.CKM_BLAKE2B_384, "CKM_BLAKE2B_384");
        addMech(PKCS11Constants.CKM_BLAKE2B_384_HMAC, "CKM_BLAKE2B_384_HMAC");
        addMech(PKCS11Constants.CKM_BLAKE2B_384_HMAC_GENERAL, "CKM_BLAKE2B_384_HMAC_GENERAL");
        addMech(PKCS11Constants.CKM_BLAKE2B_384_KEY_DERIVE, "CKM_BLAKE2B_384_KEY_DERIVE");
        addMech(PKCS11Constants.CKM_BLAKE2B_384_KEY_GEN, "CKM_BLAKE2B_384_KEY_GEN");
        addMech(PKCS11Constants.CKM_BLAKE2B_512, "CKM_BLAKE2B_512");
        addMech(PKCS11Constants.CKM_BLAKE2B_512_HMAC, "CKM_BLAKE2B_512_HMAC");
        addMech(PKCS11Constants.CKM_BLAKE2B_512_HMAC_GENERAL, "CKM_BLAKE2B_512_HMAC_GENERAL");
        addMech(PKCS11Constants.CKM_BLAKE2B_512_KEY_DERIVE, "CKM_BLAKE2B_512_KEY_DERIVE");
        addMech(PKCS11Constants.CKM_BLAKE2B_512_KEY_GEN, "CKM_BLAKE2B_512_KEY_GEN");
        addMech(PKCS11Constants.CKM_SALSA20, "CKM_SALSA20");
        addMech(PKCS11Constants.CKM_CHACHA20_POLY1305, "CKM_CHACHA20_POLY1305");
        addMech(PKCS11Constants.CKM_SALSA20_POLY1305, "CKM_SALSA20_POLY1305");
        addMech(PKCS11Constants.CKM_X3DH_INITIALIZE, "CKM_X3DH_INITIALIZE");
        addMech(PKCS11Constants.CKM_X3DH_RESPOND, "CKM_X3DH_RESPOND");
        addMech(PKCS11Constants.CKM_X2RATCHET_INITIALIZE, "CKM_X2RATCHET_INITIALIZE");
        addMech(PKCS11Constants.CKM_X2RATCHET_RESPOND, "CKM_X2RATCHET_RESPOND");
        addMech(PKCS11Constants.CKM_X2RATCHET_ENCRYPT, "CKM_X2RATCHET_ENCRYPT");
        addMech(PKCS11Constants.CKM_X2RATCHET_DECRYPT, "CKM_X2RATCHET_DECRYPT");
        addMech(PKCS11Constants.CKM_XEDDSA, "CKM_XEDDSA");
        addMech(PKCS11Constants.CKM_HKDF_DERIVE, "CKM_HKDF_DERIVE");
        addMech(PKCS11Constants.CKM_HKDF_DATA, "CKM_HKDF_DATA");
        addMech(PKCS11Constants.CKM_HKDF_KEY_GEN, "CKM_HKDF_KEY_GEN");
        addMech(PKCS11Constants.CKM_SALSA20_KEY_GEN, "CKM_SALSA20_KEY_GEN");
        addMech(940L, "CKM_SP800_108_COUNTER_KDF");
        addMech(941L, "CKM_SP800_108_FEEDBACK_KDF");
        addMech(942L, "CKM_SP800_108_DOUBLE_PIPELINE_KDF");
        addMech(2147483648L, "CKM_VENDOR_DEFINED");
        addMech(PKCS11Constants.CKM_NSS_TLS_PRF_GENERAL, "CKM_NSS_TLS_PRF_GENERAL");
        addMech(PKCS11Constants.PCKM_SECURERANDOM, "SecureRandom");
        addMech(PKCS11Constants.PCKM_KEYSTORE, "KeyStore");
        addHashMech(544L, "SHA-1", "SHA", "SHA1");
        addHashMech(597L, "SHA-224", "SHA224");
        addHashMech(592L, "SHA-256", "SHA256");
        addHashMech(608L, "SHA-384", "SHA384");
        addHashMech(624L, "SHA-512", "SHA512");
        addHashMech(72L, "SHA-512/224", "SHA512/224");
        addHashMech(76L, "SHA-512/256", "SHA512/256");
        addHashMech(693L, "SHA3-224");
        addHashMech(688L, "SHA3-256");
        addHashMech(704L, "SHA3-384");
        addHashMech(720L, "SHA3-512");
        addKeyType(0L, "CKK_RSA");
        addKeyType(1L, "CKK_DSA");
        addKeyType(2L, "CKK_DH");
        addKeyType(3L, "CKK_EC");
        addKeyType(4L, "CKK_X9_42_DH");
        addKeyType(5L, "CKK_KEA");
        addKeyType(16L, "CKK_GENERIC_SECRET");
        addKeyType(17L, "CKK_RC2");
        addKeyType(18L, "CKK_RC4");
        addKeyType(19L, "CKK_DES");
        addKeyType(20L, "CKK_DES2");
        addKeyType(21L, "CKK_DES3");
        addKeyType(22L, "CKK_CAST");
        addKeyType(23L, "CKK_CAST3");
        addKeyType(24L, "CKK_CAST128");
        addKeyType(25L, "CKK_RC5");
        addKeyType(26L, "CKK_IDEA");
        addKeyType(27L, "CKK_SKIPJACK");
        addKeyType(28L, "CKK_BATON");
        addKeyType(29L, "CKK_JUNIPER");
        addKeyType(30L, "CKK_CDMF");
        addKeyType(31L, "CKK_AES");
        addKeyType(32L, "CKK_BLOWFISH");
        addKeyType(33L, "CKK_TWOFISH");
        addKeyType(34L, "CKK_SECURID");
        addKeyType(35L, "CKK_HOTP");
        addKeyType(36L, "CKK_ACTI");
        addKeyType(37L, "CKK_CAMELLIA");
        addKeyType(38L, "CKK_ARIA");
        addKeyType(39L, "CKK_MD5_HMAC");
        addKeyType(40L, "CKK_SHA_1_HMAC");
        addKeyType(41L, "CKK_RIPEMD128_HMAC");
        addKeyType(42L, "CKK_RIPEMD160_HMAC");
        addKeyType(43L, "CKK_SHA256_HMAC");
        addKeyType(44L, "CKK_SHA384_HMAC");
        addKeyType(45L, "CKK_SHA512_HMAC");
        addKeyType(46L, "CKK_SHA224_HMAC");
        addKeyType(47L, "CKK_SEED");
        addKeyType(48L, "CKK_GOSTR3410");
        addKeyType(49L, "CKK_GOSTR3411");
        addKeyType(50L, "CKK_GOST28147");
        addKeyType(51L, "CKK_CHACHA20");
        addKeyType(52L, "CKK_POLY1305");
        addKeyType(53L, "CKK_AES_XTS");
        addKeyType(54L, "CKK_SHA3_224_HMAC");
        addKeyType(55L, "CKK_SHA3_256_HMAC");
        addKeyType(56L, "CKK_SHA3_384_HMAC");
        addKeyType(57L, "CKK_SHA3_512_HMAC");
        addKeyType(58L, "CKK_BLAKE2B_160_HMAC");
        addKeyType(59L, "CKK_BLAKE2B_256_HMAC");
        addKeyType(60L, "CKK_BLAKE2B_384_HMAC");
        addKeyType(61L, "CKK_BLAKE2B_512_HMAC");
        addKeyType(62L, "CKK_SALSA20");
        addKeyType(63L, "CKK_X2RATCHET");
        addKeyType(64L, "CKK_EC_EDWARDS");
        addKeyType(65L, "CKK_EC_MONTGOMERY");
        addKeyType(66L, "CKK_HKDF");
        addKeyType(67L, "CKK_SHA512_224_HMAC");
        addKeyType(68L, "CKK_SHA512_256_HMAC");
        addKeyType(69L, "CKK_SHA512_T_HMAC");
        addKeyType(2147483648L, "CKK_VENDOR_DEFINED");
        addKeyType(PKCS11Constants.PCKK_ANY, "*");
        addAttribute(0L, "CKA_CLASS");
        addAttribute(1L, "CKA_TOKEN");
        addAttribute(2L, "CKA_PRIVATE");
        addAttribute(3L, "CKA_LABEL");
        addAttribute(4L, "CKA_UNIQUE_ID");
        addAttribute(16L, "CKA_APPLICATION");
        addAttribute(17L, "CKA_VALUE");
        addAttribute(18L, "CKA_OBJECT_ID");
        addAttribute(128L, "CKA_CERTIFICATE_TYPE");
        addAttribute(129L, "CKA_ISSUER");
        addAttribute(130L, "CKA_SERIAL_NUMBER");
        addAttribute(131L, "CKA_AC_ISSUER");
        addAttribute(132L, "CKA_OWNER");
        addAttribute(133L, "CKA_ATTR_TYPES");
        addAttribute(134L, "CKA_TRUSTED");
        addAttribute(135L, "CKA_CERTIFICATE_CATEGORY");
        addAttribute(136L, "CKA_JAVA_MIDP_SECURITY_DOMAIN");
        addAttribute(137L, "CKA_URL");
        addAttribute(138L, "CKA_HASH_OF_SUBJECT_PUBLIC_KEY");
        addAttribute(139L, "CKA_HASH_OF_ISSUER_PUBLIC_KEY");
        addAttribute(140L, "CKA_NAME_HASH_ALGORITHM");
        addAttribute(144L, "CKA_CHECK_VALUE");
        addAttribute(256L, "CKA_KEY_TYPE");
        addAttribute(257L, "CKA_SUBJECT");
        addAttribute(258L, "CKA_ID");
        addAttribute(259L, "CKA_SENSITIVE");
        addAttribute(260L, "CKA_ENCRYPT");
        addAttribute(261L, "CKA_DECRYPT");
        addAttribute(262L, "CKA_WRAP");
        addAttribute(263L, "CKA_UNWRAP");
        addAttribute(264L, "CKA_SIGN");
        addAttribute(265L, "CKA_SIGN_RECOVER");
        addAttribute(266L, "CKA_VERIFY");
        addAttribute(267L, "CKA_VERIFY_RECOVER");
        addAttribute(268L, "CKA_DERIVE");
        addAttribute(272L, "CKA_START_DATE");
        addAttribute(273L, "CKA_END_DATE");
        addAttribute(288L, "CKA_MODULUS");
        addAttribute(289L, "CKA_MODULUS_BITS");
        addAttribute(290L, "CKA_PUBLIC_EXPONENT");
        addAttribute(291L, "CKA_PRIVATE_EXPONENT");
        addAttribute(292L, "CKA_PRIME_1");
        addAttribute(293L, "CKA_PRIME_2");
        addAttribute(294L, "CKA_EXPONENT_1");
        addAttribute(295L, "CKA_EXPONENT_2");
        addAttribute(296L, "CKA_COEFFICIENT");
        addAttribute(297L, "CKA_PUBLIC_KEY_INFO");
        addAttribute(304L, "CKA_PRIME");
        addAttribute(305L, "CKA_SUBPRIME");
        addAttribute(306L, "CKA_BASE");
        addAttribute(307L, "CKA_PRIME_BITS");
        addAttribute(308L, "CKA_SUB_PRIME_BITS");
        addAttribute(352L, "CKA_VALUE_BITS");
        addAttribute(353L, "CKA_VALUE_LEN");
        addAttribute(354L, "CKA_EXTRACTABLE");
        addAttribute(355L, "CKA_LOCAL");
        addAttribute(356L, "CKA_NEVER_EXTRACTABLE");
        addAttribute(357L, "CKA_ALWAYS_SENSITIVE");
        addAttribute(358L, "CKA_KEY_GEN_MECHANISM");
        addAttribute(368L, "CKA_MODIFIABLE");
        addAttribute(369L, "CKA_COPYABLE");
        addAttribute(370L, "CKA_DESTROYABLE");
        addAttribute(384L, "CKA_EC_PARAMS");
        addAttribute(385L, "CKA_EC_POINT");
        addAttribute(512L, "CKA_SECONDARY_AUTH");
        addAttribute(513L, "CKA_AUTH_PIN_FLAGS");
        addAttribute(514L, "CKA_ALWAYS_AUTHENTICATE");
        addAttribute(528L, "CKA_WRAP_WITH_TRUSTED");
        addAttribute(PKCS11Constants.CKA_WRAP_TEMPLATE, "CKA_WRAP_TEMPLATE");
        addAttribute(PKCS11Constants.CKA_UNWRAP_TEMPLATE, "CKA_UNWRAP_TEMPLATE");
        addAttribute(PKCS11Constants.CKA_DERIVE_TEMPLATE, "CKA_DERIVE_TEMPLATE");
        addAttribute(544L, "CKA_OTP_FORMAT");
        addAttribute(545L, "CKA_OTP_LENGTH");
        addAttribute(546L, "CKA_OTP_TIME_INTERVAL");
        addAttribute(547L, "CKA_OTP_USER_FRIENDLY_MODE");
        addAttribute(548L, "CKA_OTP_CHALLENGE_REQUIREMENT");
        addAttribute(549L, "CKA_OTP_TIME_REQUIREMENT");
        addAttribute(550L, "CKA_OTP_COUNTER_REQUIREMENT");
        addAttribute(551L, "CKA_OTP_PIN_REQUIREMENT");
        addAttribute(558L, "CKA_OTP_COUNTER");
        addAttribute(559L, "CKA_OTP_TIME");
        addAttribute(554L, "CKA_OTP_USER_IDENTIFIER");
        addAttribute(555L, "CKA_OTP_SERVICE_IDENTIFIER");
        addAttribute(556L, "CKA_OTP_SERVICE_LOGO");
        addAttribute(557L, "CKA_OTP_SERVICE_LOGO_TYPE");
        addAttribute(592L, "CKA_GOSTR3410_PARAMS");
        addAttribute(593L, "CKA_GOSTR3411_PARAMS");
        addAttribute(594L, "CKA_GOST28147_PARAMS");
        addAttribute(768L, "CKA_HW_FEATURE_TYPE");
        addAttribute(769L, "CKA_RESET_ON_INIT");
        addAttribute(770L, "CKA_HAS_RESET");
        addAttribute(1024L, "CKA_PIXEL_X");
        addAttribute(1025L, "CKA_PIXEL_Y");
        addAttribute(PKCS11Constants.CKA_RESOLUTION, "CKA_RESOLUTION");
        addAttribute(PKCS11Constants.CKA_CHAR_ROWS, "CKA_CHAR_ROWS");
        addAttribute(PKCS11Constants.CKA_CHAR_COLUMNS, "CKA_CHAR_COLUMNS");
        addAttribute(PKCS11Constants.CKA_COLOR, "CKA_COLOR");
        addAttribute(PKCS11Constants.CKA_BITS_PER_PIXEL, "CKA_BITS_PER_PIXEL");
        addAttribute(PKCS11Constants.CKA_CHAR_SETS, "CKA_CHAR_SETS");
        addAttribute(PKCS11Constants.CKA_ENCODING_METHODS, "CKA_ENCODING_METHODS");
        addAttribute(PKCS11Constants.CKA_MIME_TYPES, "CKA_MIME_TYPES");
        addAttribute(1280L, "CKA_MECHANISM_TYPE");
        addAttribute(PKCS11Constants.CKA_REQUIRED_CMS_ATTRIBUTES, "CKA_REQUIRED_CMS_ATTRIBUTES");
        addAttribute(PKCS11Constants.CKA_DEFAULT_CMS_ATTRIBUTES, "CKA_DEFAULT_CMS_ATTRIBUTES");
        addAttribute(PKCS11Constants.CKA_SUPPORTED_CMS_ATTRIBUTES, "CKA_SUPPORTED_CMS_ATTRIBUTES");
        addAttribute(PKCS11Constants.CKA_ALLOWED_MECHANISMS, "CKA_ALLOWED_MECHANISMS");
        addAttribute(PKCS11Constants.CKA_PROFILE_ID, "CKA_PROFILE_ID");
        addAttribute(PKCS11Constants.CKA_X2RATCHET_BAG, "CKA_X2RATCHET_BAG");
        addAttribute(PKCS11Constants.CKA_X2RATCHET_BAGSIZE, "CKA_X2RATCHET_BAGSIZE");
        addAttribute(PKCS11Constants.CKA_X2RATCHET_BOBS1STMSG, "CKA_X2RATCHET_BOBS1STMSG");
        addAttribute(PKCS11Constants.CKA_X2RATCHET_CKR, "CKA_X2RATCHET_CKR");
        addAttribute(PKCS11Constants.CKA_X2RATCHET_CKS, "CKA_X2RATCHET_CKS");
        addAttribute(PKCS11Constants.CKA_X2RATCHET_DHP, "CKA_X2RATCHET_DHP");
        addAttribute(PKCS11Constants.CKA_X2RATCHET_DHR, "CKA_X2RATCHET_DHR");
        addAttribute(PKCS11Constants.CKA_X2RATCHET_DHS, "CKA_X2RATCHET_DHS");
        addAttribute(PKCS11Constants.CKA_X2RATCHET_HKR, "CKA_X2RATCHET_HKR");
        addAttribute(PKCS11Constants.CKA_X2RATCHET_HKS, "CKA_X2RATCHET_HKS");
        addAttribute(PKCS11Constants.CKA_X2RATCHET_ISALICE, "CKA_X2RATCHET_ISALICE");
        addAttribute(PKCS11Constants.CKA_X2RATCHET_NHKR, "CKA_X2RATCHET_NHKR");
        addAttribute(PKCS11Constants.CKA_X2RATCHET_NHKS, "CKA_X2RATCHET_NHKS");
        addAttribute(PKCS11Constants.CKA_X2RATCHET_NR, "CKA_X2RATCHET_NR");
        addAttribute(PKCS11Constants.CKA_X2RATCHET_NS, "CKA_X2RATCHET_NS");
        addAttribute(PKCS11Constants.CKA_X2RATCHET_PNS, "CKA_X2RATCHET_PNS");
        addAttribute(PKCS11Constants.CKA_X2RATCHET_RK, "CKA_X2RATCHET_RK");
        addAttribute(2147483648L, "CKA_VENDOR_DEFINED");
        addAttribute(PKCS11Constants.CKA_NETSCAPE_DB, "CKA_NETSCAPE_DB");
        addAttribute(PKCS11Constants.CKA_NETSCAPE_TRUST_SERVER_AUTH, "CKA_NETSCAPE_TRUST_SERVER_AUTH");
        addAttribute(PKCS11Constants.CKA_NETSCAPE_TRUST_CLIENT_AUTH, "CKA_NETSCAPE_TRUST_CLIENT_AUTH");
        addAttribute(PKCS11Constants.CKA_NETSCAPE_TRUST_CODE_SIGNING, "CKA_NETSCAPE_TRUST_CODE_SIGNING");
        addAttribute(PKCS11Constants.CKA_NETSCAPE_TRUST_EMAIL_PROTECTION, "CKA_NETSCAPE_TRUST_EMAIL_PROTECTION");
        addAttribute(PKCS11Constants.CKA_NETSCAPE_CERT_SHA1_HASH, "CKA_NETSCAPE_CERT_SHA1_HASH");
        addAttribute(PKCS11Constants.CKA_NETSCAPE_CERT_MD5_HASH, "CKA_NETSCAPE_CERT_MD5_HASH");
        addObjectClass(0L, "CKO_DATA");
        addObjectClass(1L, "CKO_CERTIFICATE");
        addObjectClass(2L, "CKO_PUBLIC_KEY");
        addObjectClass(3L, "CKO_PRIVATE_KEY");
        addObjectClass(4L, "CKO_SECRET_KEY");
        addObjectClass(5L, "CKO_HW_FEATURE");
        addObjectClass(6L, "CKO_DOMAIN_PARAMETERS");
        addObjectClass(7L, "CKO_MECHANISM");
        addObjectClass(8L, "CKO_OTP_KEY");
        addObjectClass(9L, "CKO_PROFILE");
        addObjectClass(2147483648L, "CKO_VENDOR_DEFINED");
        addObjectClass(2147483427L, "*");
        addMGF(1L, "CKG_MGF1_SHA1");
        addMGF(2L, "CKG_MGF1_SHA256");
        addMGF(3L, "CKG_MGF1_SHA384");
        addMGF(4L, "CKG_MGF1_SHA512");
        addMGF(5L, "CKG_MGF1_SHA224");
        addMGF(6L, "CKG_MGF1_SHA3_224");
        addMGF(7L, "CKG_MGF1_SHA3_256");
        addMGF(8L, "CKG_MGF1_SHA3_384");
        addMGF(9L, "CKG_MGF1_SHA3_512");
    }

    public static String toFullHexString(long j2) {
        long j3 = j2;
        StringBuffer stringBuffer = new StringBuffer(16);
        for (int i2 = 0; i2 < 16; i2++) {
            stringBuffer.append(HEX_DIGITS[((int) j3) & 15]);
            j3 >>>= 4;
        }
        return stringBuffer.reverse().toString();
    }

    public static String toFullHexString(int i2) {
        int i3 = i2;
        StringBuffer stringBuffer = new StringBuffer(8);
        for (int i4 = 0; i4 < 8; i4++) {
            stringBuffer.append(HEX_DIGITS[i3 & 15]);
            i3 >>>= 4;
        }
        return stringBuffer.reverse().toString();
    }

    public static String toHexString(long j2) {
        return Long.toHexString(j2);
    }

    public static String toHexString(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer(2 * bArr.length);
        for (byte b2 : bArr) {
            int i2 = b2 & 255;
            if (i2 < 16) {
                stringBuffer.append('0');
            }
            stringBuffer.append(Integer.toString(i2, 16));
        }
        return stringBuffer.toString();
    }

    public static String toBinaryString(long j2) {
        return Long.toString(j2, 2);
    }

    public static String toBinaryString(byte[] bArr) {
        return new BigInteger(1, bArr).toString(2);
    }

    /* loaded from: sunpkcs11.jar:sun/security/pkcs11/wrapper/Functions$Flags.class */
    private static class Flags {
        private final long[] flagIds;
        private final String[] flagNames;

        Flags(long[] jArr, String[] strArr) {
            if (jArr.length != strArr.length) {
                throw new AssertionError((Object) "Array lengths do not match");
            }
            this.flagIds = jArr;
            this.flagNames = strArr;
        }

        String toString(long j2) {
            StringBuilder sb = new StringBuilder();
            boolean z2 = true;
            for (int i2 = 0; i2 < this.flagIds.length; i2++) {
                if ((j2 & this.flagIds[i2]) != 0) {
                    if (!z2) {
                        sb.append(" | ");
                    }
                    sb.append(this.flagNames[i2]);
                    z2 = false;
                }
            }
            return sb.toString();
        }
    }

    public static String slotInfoFlagsToString(long j2) {
        return slotInfoFlags.toString(j2);
    }

    public static String tokenInfoFlagsToString(long j2) {
        return tokenInfoFlags.toString(j2);
    }

    public static String sessionInfoFlagsToString(long j2) {
        return sessionInfoFlags.toString(j2);
    }

    public static String sessionStateToString(long j2) {
        String str;
        if (j2 == 0) {
            str = "CKS_RO_PUBLIC_SESSION";
        } else if (j2 == 1) {
            str = "CKS_RO_USER_FUNCTIONS";
        } else if (j2 == 2) {
            str = "CKS_RW_PUBLIC_SESSION";
        } else if (j2 == 3) {
            str = "CKS_RW_USER_FUNCTIONS";
        } else if (j2 == 4) {
            str = "CKS_RW_SO_FUNCTIONS";
        } else {
            str = "ERROR: unknown session state 0x" + toFullHexString(j2);
        }
        return str;
    }

    public static String mechanismInfoFlagsToString(long j2) {
        return mechanismInfoFlags.toString(j2);
    }

    private static String getName(Map<Integer, String> map, long j2) {
        String str = null;
        if ((j2 >>> 32) == 0) {
            str = map.get(Integer.valueOf((int) j2));
        }
        if (str == null) {
            if ((j2 & 2147483648L) != 0) {
                str = "(Vendor-Specific) 0x" + toFullHexString(j2);
            } else {
                str = "(Unknown) 0x" + toFullHexString(j2);
            }
        }
        return str;
    }

    public static long getId(Map<String, Integer> map, String str) {
        if (map.get(str) == null) {
            throw new IllegalArgumentException("Unknown name " + str);
        }
        return r0.intValue() & 4294967295L;
    }

    public static String getMechanismName(long j2) {
        return getName(mechNames, j2);
    }

    public static long getMechanismId(String str) {
        return getId(mechIds, str);
    }

    public static String getKeyName(long j2) {
        return getName(keyNames, j2);
    }

    public static long getKeyId(String str) {
        return getId(keyIds, str);
    }

    public static String getAttributeName(long j2) {
        return getName(attributeNames, j2);
    }

    public static long getAttributeId(String str) {
        return getId(attributeIds, str);
    }

    public static String getObjectClassName(long j2) {
        return getName(objectClassNames, j2);
    }

    public static long getObjectClassId(String str) {
        return getId(objectClassIds, str);
    }

    public static long getHashMechId(String str) {
        return hashMechIds.get(str).longValue();
    }

    public static String getMGFName(long j2) {
        return getName(mgfNames, j2);
    }

    public static long getMGFId(String str) {
        return getId(mgfIds, str);
    }

    private static boolean equals(char[] cArr, char[] cArr2) {
        return Arrays.equals(cArr, cArr2);
    }

    public static boolean equals(CK_DATE ck_date, CK_DATE ck_date2) {
        boolean z2;
        if (ck_date == ck_date2) {
            z2 = true;
        } else if (ck_date != null && ck_date2 != null) {
            z2 = equals(ck_date.year, ck_date2.year) && equals(ck_date.month, ck_date2.month) && equals(ck_date.day, ck_date2.day);
        } else {
            z2 = false;
        }
        return z2;
    }

    public static int hashCode(byte[] bArr) {
        int i2 = 0;
        if (bArr != null) {
            for (int i3 = 0; i3 < 4 && i3 < bArr.length; i3++) {
                i2 ^= (255 & bArr[i3]) << ((i3 % 4) << 3);
            }
        }
        return i2;
    }

    public static int hashCode(char[] cArr) {
        int i2 = 0;
        if (cArr != null) {
            for (int i3 = 0; i3 < 4 && i3 < cArr.length; i3++) {
                i2 ^= (65535 & cArr[i3]) << ((i3 % 2) << 4);
            }
        }
        return i2;
    }

    public static int hashCode(CK_DATE ck_date) {
        int i2 = 0;
        if (ck_date != null) {
            if (ck_date.year.length == 4) {
                i2 = (((0 ^ ((65535 & ck_date.year[0]) << 16)) ^ (65535 & ck_date.year[1])) ^ ((65535 & ck_date.year[2]) << 16)) ^ (65535 & ck_date.year[3]);
            }
            if (ck_date.month.length == 2) {
                i2 = (i2 ^ ((65535 & ck_date.month[0]) << 16)) ^ (65535 & ck_date.month[1]);
            }
            if (ck_date.day.length == 2) {
                i2 = (i2 ^ ((65535 & ck_date.day[0]) << 16)) ^ (65535 & ck_date.day[1]);
            }
        }
        return i2;
    }

    private static void addMapping(Map<Integer, String> map, Map<String, Integer> map2, long j2, String str) {
        if ((j2 >>> 32) != 0) {
            throw new AssertionError((Object) ("Id has high bits set: " + j2 + ", " + str));
        }
        Integer numValueOf = Integer.valueOf((int) j2);
        if (map.put(numValueOf, str) != null) {
            throw new AssertionError((Object) ("Duplicate id: " + j2 + ", " + str));
        }
        if (map2.put(str, numValueOf) != null) {
            throw new AssertionError((Object) ("Duplicate name: " + j2 + ", " + str));
        }
    }

    private static void addMech(long j2, String str) {
        addMapping(mechNames, mechIds, j2, str);
    }

    private static void addKeyType(long j2, String str) {
        addMapping(keyNames, keyIds, j2, str);
    }

    private static void addAttribute(long j2, String str) {
        addMapping(attributeNames, attributeIds, j2, str);
    }

    private static void addObjectClass(long j2, String str) {
        addMapping(objectClassNames, objectClassIds, j2, str);
    }

    private static void addHashMech(long j2, String... strArr) {
        for (String str : strArr) {
            hashMechIds.put(str, Long.valueOf(j2));
        }
    }

    private static void addMGF(long j2, String str) {
        addMapping(mgfNames, mgfIds, j2, str);
    }
}
