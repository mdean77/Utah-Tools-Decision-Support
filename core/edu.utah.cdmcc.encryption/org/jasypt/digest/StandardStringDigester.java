/*
 * =============================================================================
 * 
 *   Copyright (c) 2007-2010, The JASYPT team (http://www.jasypt.org)
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * 
 * =============================================================================
 */
package org.jasypt.digest;

import java.security.Provider;

import org.jasypt.commons.CommonUtils;
import org.jasypt.contrib.org.apache.commons.codec_1_3.binary.Base64;
import org.jasypt.digest.config.DigesterConfig;
import org.jasypt.digest.config.StringDigesterConfig;
import org.jasypt.exceptions.AlreadyInitializedException;
import org.jasypt.exceptions.EncryptionInitializationException;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.normalization.Normalizer;
import org.jasypt.salt.SaltGenerator;


/**
 * <p>
 * Standard implementation of the {@link StringDigester} interface.
 * This class lets the user specify the algorithm (and provider) to be used for 
 * creating digests, the size of the salt to be applied,
 * the number of times the hash function will be applied (iterations) and
 * the salt generator to be used.
 * </p>
 * <p>
 * This class avoids byte-conversion problems related to the fact of 
 * different platforms having different default charsets, and returns 
 * digests in the form of BASE64-encoded or HEXADECIMAL ASCII Strings.
 * </p>
 * <p>
 * This class is <i>thread-safe</i>.
 * </p>
 * <p>
 * <br/><b><u>Configuration</u></b>
 * </p>
 * <p>
 * The algorithm, provider, salt size, iterations and salt generator can take values 
 * in any of these ways:
 * <ul>
 *   <li>Using its default values.</li>
 *   <li>Setting a <tt>{@link org.jasypt.digest.config.DigesterConfig}</tt> 
 *       object which provides new 
 *       configuration values.</li>
 *   <li>Calling the corresponding <tt>setX(...)</tt> methods.</li>
 * </ul>
 * And the actual values to be used for initialization will be established
 * by applying the following priorities:
 * <ol>
 *   <li>First, the default values are considered.</li>
 *   <li>Then, if a <tt>{@link org.jasypt.digest.config.DigesterConfig}</tt> 
 *       object has been set with
 *       <tt>setConfig</tt>, the non-null values returned by its
 *       <tt>getX</tt> methods override the default values.</li>
 *   <li>Finally, if the corresponding <tt>setX</tt> method has been called
 *       on the digester itself for any of the configuration parameters, the 
 *       values set by these calls override all of the above.</li>
 * </ol>
 * </p>
 * 
 * <p>
 * <br/><b><u>Initialization</u></b>
 * </p>
 * <p>
 * Before it is ready to create digests, an object of this class has to be
 * <i>initialized</i>. Initialization happens:
 * <ul>
 *   <li>When <tt>initialize</tt> is called.</li>
 *   <li>When <tt>digest</tt> or <tt>matches</tt> are called for the
 *       first time, if <tt>initialize</tt> has not been called before.</li>
 * </ul>
 * Once a digester has been initialized, trying to
 * change its configuration
 * will result in an <tt>AlreadyInitializedException</tt> being thrown.
 * </p>
 * 
 * <p>
 * <br/><b><u>Usage</u></b>
 * </p>
 * <p>
 * A digester may be used in two different ways:
 * <ul>
 *   <li>For <i>creating digests</i>, by calling the <tt>digest</tt> method.</li>
 *   <li>For <i>matching digests</i>, this is, checking whether a digest
 *       corresponds adequately to a digest (as in password checking) or not, by
 *       calling the <tt>matches</tt> method.</li> 
 * </ul>
 * The steps taken for creating digests are:
 * <ol>
 *   <li>The String message is converted to a byte array.</li>
 *   <li>A salt of the specified size is generated (see 
 *       {@link org.jasypt.salt.SaltGenerator}).</li>
 *   <li>The salt bytes are added to the message.</li>
 *   <li>The hash function is applied to the salt and message altogether, 
 *       and then to the
 *       results of the function itself, as many times as specified
 *       (iterations).</li>
 *   <li>If specified by the salt generator (see 
 *       {@link org.jasypt.salt.SaltGenerator#includePlainSaltInEncryptionResults()}), 
 *       the <i>undigested</i> salt and the final result of the hash
 *       function are concatenated and returned as a result.</li>
 *   <li>The result of the concatenation is encoded in BASE64 or HEXADECIMAL
 *       and returned as an ASCII String.</li>
 * </ol>
 * Put schematically in bytes:
 * <ul>
 *   <li>
 *     DIGEST = <tt>|<b>S</b>|..(ssb)..|<b>S</b>|<b>X</b>|<b>X</b>|<b>X</b>|...|<b>X</b>|</tt>
 *       <ul>
 *         <li><tt><b>S</b></tt>: salt bytes (plain, not digested). <i>(OPTIONAL)</i>.</li>
 *         <li><tt>ssb</tt>: salt size in bytes.</li>
 *         <li><tt><b>X</b></tt>: bytes resulting from hashing (see below).</li>
 *       </ul>
 *   </li>
 *   <li>
 *     <tt>|<b>X</b>|<b>X</b>|<b>X</b>|...|<b>X</b>|</tt> = 
 *     <tt><i>H</i>(<i>H</i>(<i>H</i>(..(it)..<i>H</i>(<b>Z</b>|<b>Z</b>|<b>Z</b>|...|<b>Z</b>|))))</tt>
 *     <ul>
 *       <li><tt><i>H</i></tt>: Hash function (algorithm).</li>
 *       <li><tt>it</tt>: Number of iterations.</li>
 *       <li><tt><b>Z</b></tt>: Input for hashing (see below).</li> 
 *     </ul>
 *   </li>
 *   <li>
 *     <tt>|<b>Z</b>|<b>Z</b>|<b>Z</b>|...|<b>Z</b>|</tt> =
 *     <tt>|<b>S</b>|..(ssb)..|<b>S</b>|<b>M</b>|<b>M</b>|<b>M</b>...|<b>M</b>|</tt>
 *     <ul>
 *         <li><tt><b>S</b></tt>: salt bytes (plain, not digested).</li>
 *         <li><tt>ssb</tt>: salt size in bytes.</li>
 *         <li><tt><b>M</b></tt>: message bytes.</li>
 *     </ul>
 *   </li>
 * </ul>
 * <b>If a random salt generator is used, two digests created for the same 
 * message will always be different
 * (except in the case of random salt coincidence).</b>
 * Because of this, in this case the result of the <tt>digest</tt> method 
 * will contain both the <i>undigested</i> salt and the digest of the 
 * (salt + message), so that another digest operation can be performed 
 * with the same salt on a different message to check if both messages 
 * match (all of which will be managed automatically by the 
 * <tt>matches</tt> method).
 * </p>
 * <p>     
 * To learn more about the mechanisms involved in digest creation, read
 * <a href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127" 
 * target="_blank">PKCS &#035;5: Password-Based Cryptography Standard</a>.
 * </p>
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 */
public final class StandardStringDigester implements StringDigester {


    /**
     * <p>
     * Charset to be used to obtain "digestable" byte arrays from input Strings.
     * Set to <b>UTF-8</b>.
     * </p>
     * <p> 
     * This charset has to be fixed to some value so that we avoid problems 
     * with different platforms having different "default" charsets. 
     * </p>
     * <p>
     * It is set to <b>UTF-8</b> because it covers the whole spectrum of characters
     * representable in Java (which internally uses UTF-16), and avoids the
     * size penalty of UTF-16 (which will always use two bytes for representing
     * each character, even if it is an ASCII one).
     * </p>
     * <p>
     * Setting this value to UTF-8 does not mean that Strings that originally
     * come for, for example, an ISO-8859-1 input, will not be correcly 
     * digested. It simply provides a way of "fixing" the way a String will
     * be converted into bytes for digesting.
     * </p>
     */
    public static final String MESSAGE_CHARSET = "UTF-8";
    
    /**
     * <p>
     * Charset to be used for encoding the resulting digests. 
     * Set to <b>US-ASCII</b>.
     * </p>
     * <p>
     * The result of digesting some bytes can be any other bytes, and so
     * the result of digesting, for example, some LATIN-1 valid String bytes, 
     * can be bytes that may not conform a "valid" LATIN-1 String.
     * </p>
     * <p>
     * Because of this, digests are always encoded in <i>BASE64</i> or
     * HEXADECIMAL after 
     * being created, and this ensures that the 
     * digests will make perfectly representable, safe ASCII Strings. Because
     * of this, the charset used to convert the digest bytes to the returned 
     * String is set to <b>US-ASCII</b>.
     * </p>
     */
    public static final String DIGEST_CHARSET = "US-ASCII";
    
    /**
     * <p>
     * Whether the Unicode normalization step should be ignored because of
     * legacy-compatibility issues. Defaults to <b>FALSE</b> (the normalization
     * step WILL be performed).
     * </p>
     */
    public static final boolean DEFAULT_UNICODE_NORMALIZATION_IGNORED = false;

    /**
     * <p>
     * Default type of String output. Set to <b>BASE64</b>. 
     * </p> 
     */
    public static final String DEFAULT_STRING_OUTPUT_TYPE = 
        CommonUtils.STRING_OUTPUT_TYPE_BASE64;

    
    // The StandardByteDigester that will be internally used.
    private final StandardByteDigester byteDigester;

    // If the config object set is a StringDigesterConfig, it must be referenced
    private StringDigesterConfig stringDigesterConfig = null;
    
    // This variable holds whether the unicode normalization step should
    // be ignored or not (default = DO NOT ignore).
    private boolean unicodeNormalizationIgnored = 
        DEFAULT_UNICODE_NORMALIZATION_IGNORED;
    
    // This variable holds the type of String output which will be done,
    // and also a boolean variable for faster comparison
    private String stringOutputType = DEFAULT_STRING_OUTPUT_TYPE;
    private boolean stringOutputTypeBase64 = true;
    

    // Prefix and suffix to be added to encryption results (if any)
    private String prefix = null;
    private String suffix = null;
    

    /*
     * Set of booleans which indicate whether the config or default values
     * have to be overriden because of the setX methods having been
     * called.
     */
    private boolean unicodeNormalizationIgnoredSet = false;
    private boolean stringOutputTypeSet = false;
    private boolean prefixSet = false;
    private boolean suffixSet = false;
    
    
    // BASE64 encoder which will make sure the returned digests are
    // valid US-ASCII strings (if the user chooses BASE64 output).
    // The Bsae64 encoder is THREAD-SAFE
    private final Base64 base64;


    
    /**
     * Creates a new instance of <tt>StandardStringDigester</tt>.
     */
    public StandardStringDigester() {
        super();
        this.byteDigester = new StandardByteDigester();
        this.base64 = new Base64();
    }


    
    /*
     * Creates a new instance of <tt>StandardStringDigester</tt> using
     * the specified byte digester (constructor used for cloning)
     */
    private StandardStringDigester(final StandardByteDigester standardByteDigester) {
        super();
        this.byteDigester = standardByteDigester;
        this.base64 = new Base64();
    }

    
    /**
     * <p>
     * Sets a <tt>{@link org.jasypt.digest.config.DigesterConfig}</tt> 
     * or {@link StringDigesterConfig} object 
     * for the digester. If this config
     * object is set, it will be asked values for:
     * </p>
     * 
     * <ul>
     *   <li>Algorithm</li>
     *   <li>Security Provider (or provider name)</li>
     *   <li>Salt size</li>
     *   <li>Hashing iterations</li>
     *   <li>Salt generator</li>
     *   <li>Use of Unicode normalization mechanisms 
     *       (only <tt>StringDigesterConfig</tt>)</li>
     *   <li>Output type (base64, hexadecimal) 
     *       (only <tt>StringDigesterConfig</tt>)</li>
     * </ul>
     * 
     * <p>
     * The non-null values it returns will override the default ones, 
     * <i>and will be overriden by any values specified with a <tt>setX</tt>
     * method</i>.
     * </p>
     * 
     * @param config the <tt>DigesterConfig</tt> object to be used as the 
     *               source for configuration parameters.
     */
    public synchronized void setConfig(final DigesterConfig config) {
        this.byteDigester.setConfig(config);
        if ((config != null) && (config instanceof StringDigesterConfig)) {
            this.stringDigesterConfig = (StringDigesterConfig) config;
        }
    }

    
    /**
     * <p>
     * Sets the algorithm to be used for digesting, like <tt>MD5</tt> 
     * or <tt>SHA-1</tt>.
     * </p>
     * <p>
     * This algorithm has to be supported by your security infrastructure, and
     * it should be allowed as an algorithm for creating
     * java.security.MessageDigest instances.
     * </p>
     * <p>
     * If you are specifying a security provider with {@link #setProvider(Provider)} or
     * {@link #setProviderName(String)}, this algorithm should be
     * supported by your specified provider.
     * </p>
     * <p>
     * If you are not specifying a provider, you will be able to use those
     * algorithms provided by the default security provider of your JVM vendor.
     * For valid names in the Sun JVM, see <a target="_blank" 
     *         href="http://java.sun.com/j2se/1.5.0/docs/guide/security/CryptoSpec.html#AppA">Java 
     *         Cryptography Architecture API Specification & 
     *         Reference</a>.
     * </p>
     * 
     * @param algorithm the name of the algorithm to be used.
     */
    public void setAlgorithm(final String algorithm) {
        this.byteDigester.setAlgorithm(algorithm);
    }

    
    /**
     * <p>
     * Sets the size of the salt to be used to compute the digest.
     * This mechanism is explained in 
     * <a href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127" 
     * target="_blank">PKCS &#035;5: Password-Based Cryptography Standard</a>.
     * </p>
     * 
     * <p>
     * If salt size is set to zero, then no salt will be used.
     * </p>
     * 
     * @param saltSizeBytes the size of the salt to be used, in bytes.
     */
    public void setSaltSizeBytes(final int saltSizeBytes) {
        this.byteDigester.setSaltSizeBytes(saltSizeBytes);
    }

    
    /**
     * <p>
     * Set the number of times the hash function will be applied recursively.
     * <br/>
     * The hash function will be applied to its own results as many times as 
     * specified: <i>h(h(...h(x)...))</i>
     * </p>
     * <p>
     * This mechanism is explained in 
     * <a href="http://www.rsasecurity.com/rsalabs/node.asp?id=2127" 
     * target="_blank">PKCS &#035;5: Password-Based Cryptography Standard</a>.
     * </p>
     * 
     * @param iterations the number of iterations.
     */
    public void setIterations(final int iterations) {
        this.byteDigester.setIterations(iterations);
    }

    
    /**
     * <p>
     * Sets the salt generator to be used. If no salt generator is specified,
     * an instance of {@link org.jasypt.salt.RandomSaltGenerator} will be used. 
     * </p>
     * 
     * @param saltGenerator the salt generator to be used.
     */
    public void setSaltGenerator(final SaltGenerator saltGenerator) {
        this.byteDigester.setSaltGenerator(saltGenerator);
    }

    
    /**
     * <p>
     * Sets the name of the security provider to be asked for the
     * digest algorithm. This security provider has to be registered beforehand
     * at the JVM security framework. 
     * </p>
     * <p>
     * The provider can also be set with the {@link #setProvider(Provider)}
     * method, in which case it will not be necessary neither registering
     * the provider beforehand,
     * nor calling this {@link #setProviderName(String)} method to specify
     * a provider name.
     * </p>
     * <p>
     * Note that a call to {@link #setProvider(Provider)} overrides any value 
     * set by this method.
     * </p>
     * <p>
     * If no provider name / provider is explicitly set, the default JVM
     * provider will be used.
     * </p>
     * 
     * @since 1.3
     * 
     * @param providerName the name of the security provider to be asked
     *                     for the digest algorithm.
     */
    public void setProviderName(final String providerName) {
        this.byteDigester.setProviderName(providerName);
    }
    
    
    /**
     * <p>
     * Sets the security provider to be asked for the digest algorithm.
     * The provider does not have to be registered at the security 
     * infrastructure beforehand, and its being used here will not result in
     * its being registered.
     * </p>
     * <p>
     * If this method is called, calling {@link #setProviderName(String)}
     * becomes unnecessary.
     * </p>
     * <p>
     * If no provider name / provider is explicitly set, the default JVM
     * provider will be used.
     * </p>
     * 
     * @since 1.3
     * 
     * @param provider the provider to be asked for the chosen algorithm
     */
    public void setProvider(final Provider provider) {
        this.byteDigester.setProvider(provider);
    }
    
    
    /**
     * <p>
     * Whether the salt bytes are to be appended after the 
     * message ones before performing the digest operation on the whole. The 
     * default behaviour is to insert those bytes before the message bytes, but 
     * setting this configuration item to <tt>true</tt> allows compatibility 
     * with some external systems and specifications (e.g. LDAP {SSHA}).
     * </p>
     * <p>
     * If this parameter is not explicitly set, the default behaviour 
     * (insertion of salt before message) will be applied.
     * </p>
     * 
     * @since 1.7
     * 
     * @param invertPositionOfSaltInMessageBeforeDigesting
     *        whether salt will be appended after the message before applying 
     *        the digest operation on the whole, instead of inserted before it
     *        (which is the default).
     */
    public synchronized void setInvertPositionOfSaltInMessageBeforeDigesting(
            final boolean invertPositionOfSaltInMessageBeforeDigesting) {
        this.byteDigester.setInvertPositionOfSaltInMessageBeforeDigesting(invertPositionOfSaltInMessageBeforeDigesting);
    }
    
    
    /**
     * <p>
     * Whether the plain (not hashed) salt bytes are to 
     * be appended after the digest operation result bytes. The default behaviour is 
     * to insert them before the digest result, but setting this configuration 
     * item to <tt>true</tt> allows compatibility with some external systems
     * and specifications (e.g. LDAP {SSHA}).
     * </p>
     * <p>
     * If this parameter is not explicitly set, the default behaviour 
     * (insertion of plain salt before digest result) will be applied.
     * </p>
     * 
     * @since 1.7
     * 
     * @param invertPositionOfPlainSaltInEncryptionResults
     *        whether plain salt will be appended after the digest operation 
     *        result instead of inserted before it (which is the 
     *        default).
     */
    public synchronized void setInvertPositionOfPlainSaltInEncryptionResults(
            final boolean invertPositionOfPlainSaltInEncryptionResults) {
        this.byteDigester.setInvertPositionOfPlainSaltInEncryptionResults(invertPositionOfPlainSaltInEncryptionResults);
    }
    
    
    /**
     * <p>
     * Whether digest matching operations will allow matching
     * digests with a salt size different to the one configured in the "saltSizeBytes"
     * property. This is possible because digest algorithms will produce a fixed-size 
     * result, so the remaining bytes from the hashed input will be considered salt.
     * </p>
     * <p>
     * This will allow the digester to match digests produced in environments which do not
     * establish a fixed salt size as standard (for example, SSHA password encryption
     * in LDAP systems).  
     * </p>
     * <p>
     * The value of this property will <b>not</b> affect the creation of digests, 
     * which will always have a salt of the size established by the "saltSizeBytes" 
     * property. It will only affect digest matching.  
     * </p>
     * <p>
     * Setting this property to <tt>true</tt> is not compatible with {@link SaltGenerator}
     * implementations which return false for their 
     * {@link SaltGenerator#includePlainSaltInEncryptionResults()} property. 
     * </p>
     * <p>
     * Also, be aware that some algorithms or algorithm providers might not support
     * knowing the size of the digests beforehand, which is also incompatible with
     * a lenient behaviour.
     * </p>
     * <p>
     * If this parameter is not explicitly set, the default behaviour 
     * (NOT lenient) will be applied.
     * </p>
     * 
     * @since 1.7
     * 
     * @param useLenientSaltSizeCheck whether the digester will allow matching of 
     *        digests with different salt sizes than established or not (default 
     *        is false).
     */
    public synchronized void setUseLenientSaltSizeCheck(final boolean useLenientSaltSizeCheck) {
        this.byteDigester.setUseLenientSaltSizeCheck(useLenientSaltSizeCheck);
    }
    
    
    /**
     * <p>
     * Sets whether the unicode text normalization step should be ignored.
     * </p>
     * <p>
     * The Java Virtual Machine internally handles all Strings as UNICODE. When
     * digesting or matching digests in jasypt, these Strings are first 
     * <b>normalized to 
     * its NFC form</b> so that digest matching is not affected by the specific
     * form in which the messages where input.
     * </p>
     * <p>
     * <b>It is normally safe (and recommended) to leave this parameter set to 
     * its default FALSE value (and thus DO perform normalization 
     * operations)</b>. But in some specific cases in which issues with legacy
     * software could arise, it might be useful to set this to TRUE.
     * </p>
     * <p>
     * For more information on unicode text normalization, see this issue of 
     * <a href="http://java.sun.com/mailers/techtips/corejava/2007/tt0207.html">Core Java Technologies Tech Tips</a>.
     * </p>
     * 
     * @since 1.3
     * 
     * @param unicodeNormalizationIgnored whether the unicode text 
     *        normalization step should be ignored or not.
     */
    public synchronized void setUnicodeNormalizationIgnored(final boolean unicodeNormalizationIgnored) {
        if (isInitialized()) {
            throw new AlreadyInitializedException();
        }
        this.unicodeNormalizationIgnored = unicodeNormalizationIgnored;
        this.unicodeNormalizationIgnoredSet = true;
    }
    
    
    /**
     * <p>
     * Sets the the form in which String output
     * will be encoded. Available encoding types are:
     * </p>
     * <ul>
     *   <li><tt><b>base64</b></tt> (default)</li>
     *   <li><tt><b>hexadecimal</b></tt></li>
     * </ul>
     * <p>
     * If not set, null will be returned.
     * </p>
     *
     * @since 1.3
     * 
     * @param stringOutputType the string output type.
     */
    public synchronized void setStringOutputType(final String stringOutputType) {
        CommonUtils.validateNotEmpty(stringOutputType, "String output type cannot be set empty");
        if (isInitialized()) {
            throw new AlreadyInitializedException();
        }
        this.stringOutputType = 
            CommonUtils.
                getStandardStringOutputType(stringOutputType);
        this.stringOutputTypeSet = true;
    }
    
    
    /**
     * <p>
     * Sets the prefix to be added at the beginning of encryption results, and also to
     * be expected at the beginning of plain messages provided for matching operations
     * (raising an {@link EncryptionOperationNotPossibleException} if not).
     * </p>
     * <p>
     * By default, no prefix will be added to encryption results.
     * </p>
     * 
     * @since 1.7
     * 
     * @param prefix the prefix to be set
     */
    public synchronized void setPrefix(final String prefix) {
        if (isInitialized()) {
            throw new AlreadyInitializedException();
        }
        this.prefix = prefix;
        this.prefixSet = true;
    }
    
    
    /**
     * <p>
     * Sets the suffix to be added at the end of encryption results, and also to
     * be expected at the end of plain messages provided for matching operations
     * (raising an {@link EncryptionOperationNotPossibleException} if not).
     * </p>
     * <p>
     * By default, no suffix will be added to encryption results.
     * </p>
     * 
     * @since 1.7
     * 
     * @param suffix the suffix to be set
     */
    public synchronized void setSuffix(final String suffix) {
        if (isInitialized()) {
            throw new AlreadyInitializedException();
        }
        this.suffix = suffix;
        this.suffixSet = true;
    }

    


    
    
    
    
    /*
     * Clone this digester.
     */
    StandardStringDigester cloneDigester() {
        
        // Check initialization
        if (!isInitialized()) {
            initialize();
        }
        
        final StandardStringDigester cloned = 
            new StandardStringDigester(this.byteDigester.cloneDigester());
        cloned.setPrefix(this.prefix);
        cloned.setSuffix(this.suffix);
        if (CommonUtils.isNotEmpty(this.stringOutputType)) {
            cloned.setStringOutputType(this.stringOutputType);
        }
        cloned.setUnicodeNormalizationIgnored(this.unicodeNormalizationIgnored);
        
        return cloned;
        
    }
    
    
    
    
    /**
     * <p>
     *   Returns true if the digester has already been initialized, false if
     *   not.<br/> 
     *   Initialization happens:
     * </p>
     * <ul>
     *   <li>When <tt>initialize</tt> is called.</li>
     *   <li>When <tt>digest</tt> or <tt>matches</tt> are called for the
     *       first time, if <tt>initialize</tt> has not been called before.</li>
     * </ul>
     * <p>
     *   Once a digester has been initialized, trying to
     *   change its configuration will result in an 
     *   <tt>AlreadyInitializedException</tt>
     *   being thrown.
     * </p>
     * 
     * @return true if the digester has already been initialized, false if
     *   not.
     */
    public boolean isInitialized() {
        return this.byteDigester.isInitialized();
    }

    
    /**
     * <p>
     * Initialize the digester.
     * </p>
     * <p>
     * This operation will consist in determining the actual configuration 
     * values to be used, and then initializing the digester with them.
     * <br/>
     * These values are decided by applying the following priorities:
     * </p>
     * <ol>
     *   <li>First, the default values are considered.</li>
     *   <li>Then, if a 
     *       <tt>{@link org.jasypt.digest.config.DigesterConfig}</tt> object 
     *       has been set with
     *       <tt>setConfig</tt>, the non-null values returned by its
     *       <tt>getX</tt> methods override the default values.</li>
     *   <li>Finally, if the corresponding <tt>setX</tt> method has been called
     *       on the digester itself for any of the configuration parameters, the 
     *       values set by these calls override all of the above.</li>
     * </ol>
     * <p>
     *   Once a digester has been initialized, trying to
     *   change its configuration will result in an 
     *   <tt>AlreadyInitializedException</tt> 
     *   being thrown.
     * </p>
     * 
     * @throws EncryptionInitializationException if initialization could not
     *         be correctly done (for example, if the digest algorithm chosen
     *         cannot be used).
     *
     */
    public synchronized void initialize() {
        
        // Double-check to avoid synchronization issues
        if (!this.isInitialized()) {

            /*
             * If a StringDigesterConfig object has been set, we need to 
             * consider the values it returns (if, for each value, the
             * corresponding "setX" method has not been called).
             */
            if (this.stringDigesterConfig != null) {
                
                final Boolean configUnicodeNormalizationIgnored = 
                    this.stringDigesterConfig.isUnicodeNormalizationIgnored();
                final String configStringOutputType = 
                    this.stringDigesterConfig.getStringOutputType();
                final String configPrefix = 
                    this.stringDigesterConfig.getPrefix();
                final String configSuffix =
                    this.stringDigesterConfig.getSuffix();

                this.unicodeNormalizationIgnored = 
                    ((this.unicodeNormalizationIgnoredSet) || (configUnicodeNormalizationIgnored == null))?
                            this.unicodeNormalizationIgnored : configUnicodeNormalizationIgnored.booleanValue();
                this.stringOutputType = 
                    ((this.stringOutputTypeSet) || (configStringOutputType == null))?
                            this.stringOutputType : configStringOutputType;
                this.prefix =
                    ((this.prefixSet) || (configPrefix == null))?
                            this.prefix : configPrefix;
                this.suffix =
                    ((this.suffixSet) || (configSuffix == null))?
                            this.suffix : configSuffix;
                
            }
            
            this.stringOutputTypeBase64 =
                (CommonUtils.STRING_OUTPUT_TYPE_BASE64.
                    equalsIgnoreCase(this.stringOutputType));
            
            this.byteDigester.initialize();
        
        }

    }
    

    /**
     * <p>
     * Performs a digest operation on a String message.
     * </p>
     * <p>
     * The steps taken for creating the digest are:
     * <ol>
     *   <li>The String message is converted to a byte array.</li>
     *   <li>A salt of the specified size is generated (see 
     *       {@link org.jasypt.salt.SaltGenerator}).</li>
     *   <li>The salt bytes are added to the message.</li>
     *   <li>The hash function is applied to the salt and message altogether, 
     *       and then to the
     *       results of the function itself, as many times as specified
     *       (iterations).</li>
     *   <li>If specified by the salt generator (see 
     *       {@link org.jasypt.salt.SaltGenerator#includePlainSaltInEncryptionResults()}), 
     *       the <i>undigested</i> salt and the final result of the hash
     *       function are concatenated and returned as a result.</li>
     *   <li>The result of the concatenation is encoded in BASE64 (default) 
     *       or HEXADECIMAL
     *       and returned as an ASCII String.</li>
     * </ol>
     * Put schematically in bytes:
     * <ul>
     *   <li>
     *     DIGEST = <tt>|<b>S</b>|..(ssb)..|<b>S</b>|<b>X</b>|<b>X</b>|<b>X</b>|...|<b>X</b>|</tt>
     *       <ul>
     *         <li><tt><b>S</b></tt>: salt bytes (plain, not digested). <i>(OPTIONAL)</i>.</li>
     *         <li><tt>ssb</tt>: salt size in bytes.</li>
     *         <li><tt><b>X</b></tt>: bytes resulting from hashing (see below).</li>
     *       </ul>
     *   </li>
     *   <li>
     *     <tt>|<b>X</b>|<b>X</b>|<b>X</b>|...|<b>X</b>|</tt> = 
     *     <tt><i>H</i>(<i>H</i>(<i>H</i>(..(it)..<i>H</i>(<b>Z</b>|<b>Z</b>|<b>Z</b>|...|<b>Z</b>|))))</tt>
     *     <ul>
     *       <li><tt><i>H</i></tt>: Hash function (algorithm).</li>
     *       <li><tt>it</tt>: Number of iterations.</li>
     *       <li><tt><b>Z</b></tt>: Input for hashing (see below).</li> 
     *     </ul>
     *   </li>
     *   <li>
     *     <tt>|<b>Z</b>|<b>Z</b>|<b>Z</b>|...|<b>Z</b>|</tt> =
     *     <tt>|<b>S</b>|..(ssb)..|<b>S</b>|<b>M</b>|<b>M</b>|<b>M</b>...|<b>M</b>|</tt>
     *     <ul>
     *         <li><tt><b>S</b></tt>: salt bytes (plain, not digested).</li>
     *         <li><tt>ssb</tt>: salt size in bytes.</li>
     *         <li><tt><b>M</b></tt>: message bytes.</li>
     *     </ul>
     *   </li>
     * </ul>
     * </p>
     * <p>
     * <b>If a random salt generator is used, two digests created for the same 
     * message will always be different
     * (except in the case of random salt coincidence).</b>
     * Because of this, in this case the result of the <tt>digest</tt> method 
     * will contain both the <i>undigested</i> salt and the digest of the 
     * (salt + message), so that another digest operation can be performed 
     * with the same salt on a different message to check if both messages 
     * match (all of which will be managed automatically by the 
     * <tt>matches</tt> method).
     * </p>
     * 
     * @param message the String to be digested 
     * @return the digest result
     * @throws EncryptionOperationNotPossibleException if the digest operation
     *         fails, ommitting any further information about the cause for
     *         security reasons.
     * @throws EncryptionInitializationException if initialization could not
     *         be correctly done (for example, if the digest algorithm chosen
     *         cannot be used).
     */
    public String digest(final String message) {

        if (message == null) {
            return null;
        }

        // Check initialization
        if (!isInitialized()) {
            initialize();
        }
        
        try {

            // Normalize Unicode message to NFC form
            String normalizedMessage = null;
            if (! this.unicodeNormalizationIgnored) {
                normalizedMessage = Normalizer.normalizeToNfc(message);
            } else {
                normalizedMessage = message;
            }
            
            // The input String is converted into bytes using MESSAGE_CHARSET
            // as a fixed charset to avoid problems with different platforms
            // having different default charsets (see MESSAGE_CHARSET doc).
            final byte[] messageBytes = normalizedMessage.getBytes(MESSAGE_CHARSET);

            // The StandardByteDigester does its job.
            byte[] digest = this.byteDigester.digest(messageBytes);

            // We build the result variable
            final StringBuffer result = new StringBuffer();
            
            if (this.prefix != null) {
                // Prefix is added
                result.append(this.prefix);
            }
            
            // We encode the result in BASE64 or HEXADECIMAL so that we obtain
            // the safest result String possible.
            if (this.stringOutputTypeBase64) {
                digest = this.base64.encode(digest);
                result.append(new String(digest, DIGEST_CHARSET)); 
            } else {
                result.append(CommonUtils.toHexadecimal(digest));
            }
            
            if (this.suffix != null) {
                // Suffix is added
                result.append(this.suffix);
            }
            
            return result.toString(); 

        } catch (EncryptionInitializationException e) {
            throw e;
        } catch (EncryptionOperationNotPossibleException e) {
            throw e;
        } catch (Exception e) {
            // If digest fails, it is more secure not to return any information
            // about the cause in nested exceptions. Simply fail.
            throw new EncryptionOperationNotPossibleException();
        }
        
    }

    
    
    /**
     * <p>
     * Checks a message against a given digest.
     * </p>
     * <p>
     * This method tells whether a message corresponds to a specific digest
     * or not by getting the salt with which the digest was created and
     * applying it to a digest operation performed on the message. If 
     * new and existing digest match, the message is said to match the digest.
     * </p>
     * <p>
     * This method will be used, for instance, for password checking in
     * authentication processes.
     * </p>
     * <p>
     * A null message will only match a null digest.
     * </p>
     * 
     * @param message the message to be compared to the digest.
     * @param digest the digest. 
     * @return true if the specified message matches the digest, false
     *         if not.
     * @throws EncryptionOperationNotPossibleException if the digest matching
     *         operation fails, ommitting any further information about the 
     *         cause for security reasons.
     * @throws EncryptionInitializationException if initialization could not
     *         be correctly done (for example, if the digest algorithm chosen
     *         cannot be used).
     */
    public boolean matches(final String message, final String digest) {

        String processedDigest = digest;
        
        if (processedDigest != null) {
            if (this.prefix != null) {
                if (!processedDigest.startsWith(this.prefix)) {
                    throw new EncryptionOperationNotPossibleException(
                            "Digest does not start with required prefix \"" + this.prefix + "\"");
                }
                processedDigest = processedDigest.substring(this.prefix.length()); 
            }
            if (this.suffix != null) {
                if (!processedDigest.endsWith(this.suffix)) {
                    throw new EncryptionOperationNotPossibleException(
                            "Digest does not end with required suffix \"" + this.suffix + "\"");
                }
                processedDigest = processedDigest.substring(0, processedDigest.length() - this.suffix.length());
            }
        }
        

        if (message == null) {
            return (processedDigest == null);
        } else if (processedDigest == null) {
            return false;
        }

        
        // Check initialization
        if (!isInitialized()) {
            initialize();
        }
        
        try {

            // Normalize Unicode message to NFC form
            String normalizedMessage = null;
            if (! this.unicodeNormalizationIgnored) {
                normalizedMessage = Normalizer.normalizeToNfc(message);
            } else {
                normalizedMessage = message;
            }
            
            // We get a valid byte array from the message, in the 
            // fixed MESSAGE_CHARSET that the digest operations use.
            final byte[] messageBytes = normalizedMessage.getBytes(MESSAGE_CHARSET);
            

            // The BASE64 or HEXADECIMAL encoding is reversed and the digest
            // is converted into a byte array.
            byte[] digestBytes = null;
            if (this.stringOutputTypeBase64) {
                // The digest must be a US-ASCII String BASE64-encoded
                digestBytes = processedDigest.getBytes(DIGEST_CHARSET);
                digestBytes = this.base64.decode(digestBytes);
            } else {
                digestBytes = CommonUtils.fromHexadecimal(processedDigest);
            }
            
            // The StandardByteDigester is asked to match message to digest.
            return this.byteDigester.matches(messageBytes, digestBytes); 
        
        } catch (EncryptionInitializationException e) {
            throw e;
        } catch (EncryptionOperationNotPossibleException e) {
            throw e;
        } catch (Exception e) {
            // If digest fails, it is more secure not to return any information
            // about the cause in nested exceptions. Simply fail.
            throw new EncryptionOperationNotPossibleException();
        }

    }
    
    
}
