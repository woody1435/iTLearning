package java.lang;

import java.io.ObjectStreamField;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Formatter;
import java.util.Locale;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * String类的底层是用字符数组形式存储在常量池中，JDK1.8中常量池在JVM的元空间（即方法区）
 * 1. 通过字面量赋值，指向常量池中该字符串的引用，常量池中不存在先创建。
 * 2. 通过new关键字，每次new出来的都是一个新的对象，在堆中开辟空间进行存储，存储的是常量池中的该字符串引用
 */
public final class String
    implements java.io.Serializable, Comparable<String>, CharSequence {
    /** String的值都存在这个char数组中 */
    private final char value[];

    /** String的hash值 */
    private int hash; // Default to 0

    /**
    * @作用 实现Serializable接口，为类可持久化
    *
    * @原理 序列化操作时会把系统当前类的serialVersionUID写入到序列化文件中，当反序列化时系统会自动检测文件中
     * 的serialVersionUID，判断它是否与当前类中的serialVersionUID一致。如果一致说明序列化文件的版本与当前类的版本是一样的，
     * 可以反序列化成功
    *
    * @备注 显示声明serialVersionUID可以避免对象不一致
    *
    * @since 0.0.1
    *
    * @author woody
    */
    private static final long serialVersionUID = -6849794470754667710L;

    /**
     * Class String is special cased within the Serialization Stream Protocol.
     *
     * A String instance is written into an ObjectOutputStream according to
     * <a href="{@docRoot}/../platform/serialization/spec/output.html">
     * Object Serialization Specification, Section 6.2, "Stream Elements"</a>
     */
    private static final ObjectStreamField[] serialPersistentFields =
        new ObjectStreamField[0];

    /**
     * 无参构造函数，初始值返回""
     * 没必要调用此构造函数，因为字符串在常量池中，直接使用String s = "";创建即可
     */
    public String() {
        this.value = "".value;
    }

    /**
     * 结论：String s = "abc"直接指向了 常量池 中。而new String("abc")是在Heap中创建了一个指向 常量池 的引用，
     * 即新创建的字符串对象是参数字符串的副本
     *
     * String s1 = new String("abc");这句话创建了几个字符串对象？
     * 将创建 1 或 2 个字符串。如果池中已存在字符串常量“abc”，则只会在堆空间创建一个字符串对象，其引用指向常量池的abc。
     * 如果池中没有字符串常量“abc”，那么它将首先在池中创建，然后在堆空间中创建，因此将创建总共 2 个字符串对象。
     *
     * @see org.ddzj.java.lang.StringTest#StringStorage()
     *
     * @备注
     * String s1 = new String(""); //第一种
     * String s2 = ""; //第二种
     * 第一种始终不会入池的.
     * 第二种要看情况而定(等号右边如果是常量则入池,非常量则不入池)
     */
    public String(String original) {
        this.value = original.value;
        this.hash = original.hash;
    }

    /**
    * @作用 通过char[]得到String
    *
    * @原理 底层应该是做了字符拼接
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public String(char value[]) {
        this.value = Arrays.copyOf(value, value.length);
    }

    /**
    * @作用 通过char[]，偏移量，需要总长度获取最终字符串
    *
    * @原理 原理描述
    *
    * @备注 偏移量+需要总长度 大于char[]总长度会报错
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public String(char value[], int offset, int count) {
        if (offset < 0) {
            throw new StringIndexOutOfBoundsException(offset);
        }
        if (count <= 0) {
            if (count < 0) {
                throw new StringIndexOutOfBoundsException(count);
            }
            if (offset <= value.length) {
                this.value = "".value;
                return;
            }
        }
        // Note: offset or count might be near -1>>>1.
        if (offset > value.length - count) {
            throw new StringIndexOutOfBoundsException(offset + count);
        }
        this.value = Arrays.copyOfRange(value, offset, offset+count);
    }

    /**
    * @作用 unicode int[]，偏移量，截取长度
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public String(int[] codePoints, int offset, int count) {
        if (offset < 0) {
            throw new StringIndexOutOfBoundsException(offset);
        }
        if (count <= 0) {
            if (count < 0) {
                throw new StringIndexOutOfBoundsException(count);
            }
            if (offset <= codePoints.length) {
                this.value = "".value;
                return;
            }
        }
        // Note: offset or count might be near -1>>>1.
        if (offset > codePoints.length - count) {
            throw new StringIndexOutOfBoundsException(offset + count);
        }

        final int end = offset + count;

        // Pass 1: Compute precise size of char[]
        int n = count;
        for (int i = offset; i < end; i++) {
            int c = codePoints[i];
            if (Character.isBmpCodePoint(c))
                continue;
            else if (Character.isValidCodePoint(c))
                n++;
            else throw new IllegalArgumentException(Integer.toString(c));
        }

        // Pass 2: Allocate and fill in char[]
        final char[] v = new char[n];

        for (int i = offset, j = 0; i < end; i++, j++) {
            int c = codePoints[i];
            if (Character.isBmpCodePoint(c))
                v[j] = (char)c;
            else
                Character.toSurrogates(c, v, j++);
        }

        this.value = v;
    }

    /**
     * Allocates a new {@code String} constructed from a subarray of an array
     * of 8-bit integer values.
     *
     * <p> The {@code offset} argument is the index of the first byte of the
     * subarray, and the {@code count} argument specifies the length of the
     * subarray.
     *
     * <p> Each {@code byte} in the subarray is converted to a {@code char} as
     * specified in the method above.
     *
     * @deprecated This method does not properly convert bytes into characters.
     * As of JDK&nbsp;1.1, the preferred way to do this is via the
     * {@code String} constructors that take a {@link
     * java.nio.charset.Charset}, charset name, or that use the platform's
     * default charset.
     *
     * @param  ascii
     *         The bytes to be converted to characters
     *
     * @param  hibyte
     *         The top 8 bits of each 16-bit Unicode code unit
     *
     * @param  offset
     *         The initial offset
     * @param  count
     *         The length
     *
     * @throws  IndexOutOfBoundsException
     *          If the {@code offset} or {@code count} argument is invalid
     *
     * @see  #String(byte[], int)
     * @see  #String(byte[], int, int, java.lang.String)
     * @see  #String(byte[], int, int, java.nio.charset.Charset)
     * @see  #String(byte[], int, int)
     * @see  #String(byte[], java.lang.String)
     * @see  #String(byte[], java.nio.charset.Charset)
     * @see  #String(byte[])
     */
    @Deprecated
    public String(byte ascii[], int hibyte, int offset, int count) {
        checkBounds(ascii, offset, count);
        char value[] = new char[count];

        if (hibyte == 0) {
            for (int i = count; i-- > 0;) {
                value[i] = (char)(ascii[i + offset] & 0xff);
            }
        } else {
            hibyte <<= 8;
            for (int i = count; i-- > 0;) {
                value[i] = (char)(hibyte | (ascii[i + offset] & 0xff));
            }
        }
        this.value = value;
    }

    /**
     * Allocates a new {@code String} containing characters constructed from
     * an array of 8-bit integer values. Each character <i>c</i>in the
     * resulting string is constructed from the corresponding component
     * <i>b</i> in the byte array such that:
     *
     * <blockquote><pre>
     *     <b><i>c</i></b> == (char)(((hibyte &amp; 0xff) &lt;&lt; 8)
     *                         | (<b><i>b</i></b> &amp; 0xff))
     * </pre></blockquote>
     *
     * @deprecated  This method does not properly convert bytes into
     * characters.  As of JDK&nbsp;1.1, the preferred way to do this is via the
     * {@code String} constructors that take a {@link
     * java.nio.charset.Charset}, charset name, or that use the platform's
     * default charset.
     *
     * @param  ascii
     *         The bytes to be converted to characters
     *
     * @param  hibyte
     *         The top 8 bits of each 16-bit Unicode code unit
     *
     * @see  #String(byte[], int, int, java.lang.String)
     * @see  #String(byte[], int, int, java.nio.charset.Charset)
     * @see  #String(byte[], int, int)
     * @see  #String(byte[], java.lang.String)
     * @see  #String(byte[], java.nio.charset.Charset)
     * @see  #String(byte[])
     */
    @Deprecated
    public String(byte ascii[], int hibyte) {
        this(ascii, hibyte, 0, ascii.length);
    }

    /**
    * @作用 检验参数
     * 偏移量是否小于0，截取的长度+偏移量是否超过总长度
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    private static void checkBounds(byte[] bytes, int offset, int length) {
        if (length < 0)
            throw new StringIndexOutOfBoundsException(length);
        if (offset < 0)
            throw new StringIndexOutOfBoundsException(offset);
        if (offset > bytes.length - length)
            throw new StringIndexOutOfBoundsException(offset + length);
    }

    /**
    * @作用 byte[]，偏移量，截取长度，编码名称 解码成字符串
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public String(byte bytes[], int offset, int length, String charsetName)
            throws UnsupportedEncodingException {
        if (charsetName == null)
            throw new NullPointerException("charsetName");
        checkBounds(bytes, offset, length);
        this.value = StringCoding.decode(charsetName, bytes, offset, length);
    }

    /**
    * @作用 byte[]，偏移量，截取长度，Charset类型编码名称 解码成字符串
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public String(byte bytes[], int offset, int length, Charset charset) {
        if (charset == null)
            throw new NullPointerException("charset");
        checkBounds(bytes, offset, length);
        this.value =  StringCoding.decode(charset, bytes, offset, length);
    }

    /**
    * @作用 byte[],编码名称 解码字符串
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public String(byte bytes[], String charsetName)
            throws UnsupportedEncodingException {
        this(bytes, 0, bytes.length, charsetName);
    }

    /**
    * @作用 byte[],Charset类型编码 解码字符串
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public String(byte bytes[], Charset charset) {
        this(bytes, 0, bytes.length, charset);
    }

    /**
    * @作用 byte[]，偏移量，截取长度 解码字符串
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public String(byte bytes[], int offset, int length) {
        checkBounds(bytes, offset, length);
        this.value = StringCoding.decode(bytes, offset, length);
    }

    /**
    * @作用 bytes[] 解码成字符串
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public String(byte bytes[]) {
        this(bytes, 0, bytes.length);
    }

    /**
    * @作用 StringBuffer 对象解码字符串，并对StringBuffer中锁，防止被修改
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public String(StringBuffer buffer) {
        synchronized(buffer) {
            this.value = Arrays.copyOf(buffer.getValue(), buffer.length());
        }
    }

    /**
     * @作用 StringBuilder 对象解码字符串，并对StringBuffer中锁，防止被修改
     *
     * @原理 原理描述
     *
     * @备注 备注信息
     *
     * @since 0.0.1
     *
     * @author woody
     */
    public String(StringBuilder builder) {
        this.value = Arrays.copyOf(builder.getValue(), builder.length());
    }

    /*
    * Package private constructor which shares value array for speed.
    * this constructor is always expected to be called with share==true.
    * a separate constructor is needed because we already have a public
    * String(char[]) constructor that makes a copy of the given char[].
    */
    String(char[] value, boolean share) {
        // assert share : "unshared not supported";
        this.value = value;
    }

    /**
    * @作用 返回字符串长度
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public int length() {
        return value.length;
    }

    /**
     * Returns {@code true} if, and only if, {@link #length()} is {@code 0}.
     *
     * @return {@code true} if {@link #length()} is {@code 0}, otherwise
     * {@code false}
     *
     * @since 1.6
     */
    public boolean isEmpty() {
        return value.length == 0;
    }

    /**
    * @作用 返回索引位置的字符
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public char charAt(int index) {
        if ((index < 0) || (index >= value.length)) {
            throw new StringIndexOutOfBoundsException(index);
        }
        return value[index];
    }

    /**
    * @作用 返回当前字符串索引位置的ascii码
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public int codePointAt(int index) {
        if ((index < 0) || (index >= value.length)) {
            throw new StringIndexOutOfBoundsException(index);
        }
        return Character.codePointAtImpl(value, index, value.length);
    }

    /**
    * @作用 返回索引前一个位置ascii码
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public int codePointBefore(int index) {
        int i = index - 1;
        if ((i < 0) || (i >= value.length)) {
            throw new StringIndexOutOfBoundsException(index);
        }
        return Character.codePointBeforeImpl(value, index, 0);
    }

    /**
    * @作用 返回从开始到结束位置的ascii码有几位
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public int codePointCount(int beginIndex, int endIndex) {
        if (beginIndex < 0 || endIndex > value.length || beginIndex > endIndex) {
            throw new IndexOutOfBoundsException();
        }
        return Character.codePointCountImpl(value, beginIndex, endIndex - beginIndex);
    }

    /**
     * Returns the index within this {@code String} that is
     * offset from the given {@code index} by
     * {@code codePointOffset} code points. Unpaired surrogates
     * within the text range given by {@code index} and
     * {@code codePointOffset} count as one code point each.
     *
     * @param index the index to be offset
     * @param codePointOffset the offset in code points
     * @return the index within this {@code String}
     * @exception IndexOutOfBoundsException if {@code index}
     *   is negative or larger then the length of this
     *   {@code String}, or if {@code codePointOffset} is positive
     *   and the substring starting with {@code index} has fewer
     *   than {@code codePointOffset} code points,
     *   or if {@code codePointOffset} is negative and the substring
     *   before {@code index} has fewer than the absolute value
     *   of {@code codePointOffset} code points.
     * @since 1.5
     */
    public int offsetByCodePoints(int index, int codePointOffset) {
        if (index < 0 || index > value.length) {
            throw new IndexOutOfBoundsException();
        }
        return Character.offsetByCodePointsImpl(value, 0, value.length,
                index, codePointOffset);
    }

    /**
    * @作用 将此字符串中的字符复制到从dstBegin开始的dst中。
    *
    * @原理 原理描述
    *
    * @备注 此方法不执行任何范围检查
    *
    * @since 0.0.1
    *
    * @author woody
    */
    void getChars(char dst[], int dstBegin) {
        System.arraycopy(value, 0, dst, dstBegin, value.length);
    }

    /**
     * Copies characters from this string into the destination character
     * array.
     * <p>
     * The first character to be copied is at index {@code srcBegin};
     * the last character to be copied is at index {@code srcEnd-1}
     * (thus the total number of characters to be copied is
     * {@code srcEnd-srcBegin}). The characters are copied into the
     * subarray of {@code dst} starting at index {@code dstBegin}
     * and ending at index:
     * <blockquote><pre>
     *     dstBegin + (srcEnd-srcBegin) - 1
     * </pre></blockquote>
     *
     * @param      srcBegin   index of the first character in the string
     *                        to copy.
     * @param      srcEnd     index after the last character in the string
     *                        to copy.
     * @param      dst        the destination array.
     * @param      dstBegin   the start offset in the destination array.
     * @exception IndexOutOfBoundsException If any of the following
     *            is true:
     *            <ul><li>{@code srcBegin} is negative.
     *            <li>{@code srcBegin} is greater than {@code srcEnd}
     *            <li>{@code srcEnd} is greater than the length of this
     *                string
     *            <li>{@code dstBegin} is negative
     *            <li>{@code dstBegin+(srcEnd-srcBegin)} is larger than
     *                {@code dst.length}</ul>
     */
    public void getChars(int srcBegin, int srcEnd, char dst[], int dstBegin) {
        if (srcBegin < 0) {
            throw new StringIndexOutOfBoundsException(srcBegin);
        }
        if (srcEnd > value.length) {
            throw new StringIndexOutOfBoundsException(srcEnd);
        }
        if (srcBegin > srcEnd) {
            throw new StringIndexOutOfBoundsException(srcEnd - srcBegin);
        }
        System.arraycopy(value, srcBegin, dst, dstBegin, srcEnd - srcBegin);
    }

    /**
     * Copies characters from this string into the destination byte array. Each
     * byte receives the 8 low-order bits of the corresponding character. The
     * eight high-order bits of each character are not copied and do not
     * participate in the transfer in any way.
     *
     * <p> The first character to be copied is at index {@code srcBegin}; the
     * last character to be copied is at index {@code srcEnd-1}.  The total
     * number of characters to be copied is {@code srcEnd-srcBegin}. The
     * characters, converted to bytes, are copied into the subarray of {@code
     * dst} starting at index {@code dstBegin} and ending at index:
     *
     * <blockquote><pre>
     *     dstBegin + (srcEnd-srcBegin) - 1
     * </pre></blockquote>
     *
     * @deprecated  This method does not properly convert characters into
     * bytes.  As of JDK&nbsp;1.1, the preferred way to do this is via the
     * {@link #getBytes()} method, which uses the platform's default charset.
     *
     * @param  srcBegin
     *         Index of the first character in the string to copy
     *
     * @param  srcEnd
     *         Index after the last character in the string to copy
     *
     * @param  dst
     *         The destination array
     *
     * @param  dstBegin
     *         The start offset in the destination array
     *
     * @throws  IndexOutOfBoundsException
     *          If any of the following is true:
     *          <ul>
     *            <li> {@code srcBegin} is negative
     *            <li> {@code srcBegin} is greater than {@code srcEnd}
     *            <li> {@code srcEnd} is greater than the length of this String
     *            <li> {@code dstBegin} is negative
     *            <li> {@code dstBegin+(srcEnd-srcBegin)} is larger than {@code
     *                 dst.length}
     *          </ul>
     */
    @Deprecated
    public void getBytes(int srcBegin, int srcEnd, byte dst[], int dstBegin) {
        if (srcBegin < 0) {
            throw new StringIndexOutOfBoundsException(srcBegin);
        }
        if (srcEnd > value.length) {
            throw new StringIndexOutOfBoundsException(srcEnd);
        }
        if (srcBegin > srcEnd) {
            throw new StringIndexOutOfBoundsException(srcEnd - srcBegin);
        }
        Objects.requireNonNull(dst);

        int j = dstBegin;
        int n = srcEnd;
        int i = srcBegin;
        char[] val = value;   /* avoid getfield opcode */

        while (i < n) {
            dst[j++] = (byte)val[i++];
        }
    }

    /**
     * Encodes this {@code String} into a sequence of bytes using the named
     * charset, storing the result into a new byte array.
     *
     * <p> The behavior of this method when this string cannot be encoded in
     * the given charset is unspecified.  The {@link
     * java.nio.charset.CharsetEncoder} class should be used when more control
     * over the encoding process is required.
     *
     * @param  charsetName
     *         The name of a supported {@linkplain java.nio.charset.Charset
     *         charset}
     *
     * @return  The resultant byte array
     *
     * @throws  UnsupportedEncodingException
     *          If the named charset is not supported
     *
     * @since  JDK1.1
     */
    public byte[] getBytes(String charsetName)
            throws UnsupportedEncodingException {
        if (charsetName == null) throw new NullPointerException();
        return StringCoding.encode(charsetName, value, 0, value.length);
    }

    /**
     * Encodes this {@code String} into a sequence of bytes using the given
     * {@linkplain java.nio.charset.Charset charset}, storing the result into a
     * new byte array.
     *
     * <p> This method always replaces malformed-input and unmappable-character
     * sequences with this charset's default replacement byte array.  The
     * {@link java.nio.charset.CharsetEncoder} class should be used when more
     * control over the encoding process is required.
     *
     * @param  charset
     *         The {@linkplain java.nio.charset.Charset} to be used to encode
     *         the {@code String}
     *
     * @return  The resultant byte array
     *
     * @since  1.6
     */
    public byte[] getBytes(Charset charset) {
        if (charset == null) throw new NullPointerException();
        return StringCoding.encode(charset, value, 0, value.length);
    }

    /**
    * @作用 返回string的值为ascii码的byte[]
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public byte[] getBytes() {
        return StringCoding.encode(value, 0, value.length);
    }

    /**
     * Compares this string to the specified object.  The result is {@code
     * true} if and only if the argument is not {@code null} and is a {@code
     * String} object that represents the same sequence of characters as this
     * object.
     *
     * @param  anObject
     *         The object to compare this {@code String} against
     *
     * @return  {@code true} if the given object represents a {@code String}
     *          equivalent to this string, {@code false} otherwise
     *
     * @see  #compareTo(String)
     * @see  #equalsIgnoreCase(String)
     */
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof String) {
            String anotherString = (String)anObject;
            int n = value.length;
            if (n == anotherString.value.length) {
                char v1[] = value;
                char v2[] = anotherString.value;
                int i = 0;
                while (n-- != 0) {
                    if (v1[i] != v2[i])
                        return false;
                    i++;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Compares this string to the specified {@code StringBuffer}.  The result
     * is {@code true} if and only if this {@code String} represents the same
     * sequence of characters as the specified {@code StringBuffer}. This method
     * synchronizes on the {@code StringBuffer}.
     *
     * @param  sb
     *         The {@code StringBuffer} to compare this {@code String} against
     *
     * @return  {@code true} if this {@code String} represents the same
     *          sequence of characters as the specified {@code StringBuffer},
     *          {@code false} otherwise
     *
     * @since  1.4
     */
    public boolean contentEquals(StringBuffer sb) {
        return contentEquals((CharSequence)sb);
    }

    private boolean nonSyncContentEquals(AbstractStringBuilder sb) {
        char v1[] = value;
        char v2[] = sb.getValue();
        int n = v1.length;
        if (n != sb.length()) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            if (v1[i] != v2[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compares this string to the specified {@code CharSequence}.  The
     * result is {@code true} if and only if this {@code String} represents the
     * same sequence of char values as the specified sequence. Note that if the
     * {@code CharSequence} is a {@code StringBuffer} then the method
     * synchronizes on it.
     *
     * @param  cs
     *         The sequence to compare this {@code String} against
     *
     * @return  {@code true} if this {@code String} represents the same
     *          sequence of char values as the specified sequence, {@code
     *          false} otherwise
     *
     * @since  1.5
     */
    public boolean contentEquals(CharSequence cs) {
        // Argument is a StringBuffer, StringBuilder
        if (cs instanceof AbstractStringBuilder) {
            if (cs instanceof StringBuffer) {
                synchronized(cs) {
                   return nonSyncContentEquals((AbstractStringBuilder)cs);
                }
            } else {
                return nonSyncContentEquals((AbstractStringBuilder)cs);
            }
        }
        // Argument is a String
        if (cs instanceof String) {
            return equals(cs);
        }
        // Argument is a generic CharSequence
        char v1[] = value;
        int n = v1.length;
        if (n != cs.length()) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            if (v1[i] != cs.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /**
    * @作用 比较字符串，是否忽略大小写
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public boolean equalsIgnoreCase(String anotherString) {
        return (this == anotherString) ? true
                : (anotherString != null)
                && (anotherString.value.length == value.length)
                && regionMatches(true, 0, anotherString, 0, value.length);
    }

    /**
    * @作用 比较两个字符串，当相同索引位置的字符ASCII码大于，返回正数
    *
    * @原理 ASCII码值比较
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public int compareTo(String anotherString) {
        int len1 = value.length;
        int len2 = anotherString.value.length;
        int lim = Math.min(len1, len2);
        char v1[] = value;
        char v2[] = anotherString.value;

        int k = 0;
        while (k < lim) {
            char c1 = v1[k];
            char c2 = v2[k];
            if (c1 != c2) {
                return c1 - c2;
            }
            k++;
        }
        return len1 - len2;
    }

    /**
     * A Comparator that orders {@code String} objects as by
     * {@code compareToIgnoreCase}. This comparator is serializable.
     * <p>
     * Note that this Comparator does <em>not</em> take locale into account,
     * and will result in an unsatisfactory ordering for certain locales.
     * The java.text package provides <em>Collators</em> to allow
     * locale-sensitive ordering.
     *
     * @see     java.text.Collator#compare(String, String)
     * @since   1.2
     */
    public static final Comparator<String> CASE_INSENSITIVE_ORDER
                                         = new CaseInsensitiveComparator();
    private static class CaseInsensitiveComparator
            implements Comparator<String>, java.io.Serializable {
        // use serialVersionUID from JDK 1.2.2 for interoperability
        private static final long serialVersionUID = 8575799808933029326L;

        public int compare(String s1, String s2) {
            int n1 = s1.length();
            int n2 = s2.length();
            int min = Math.min(n1, n2);
            for (int i = 0; i < min; i++) {
                char c1 = s1.charAt(i);
                char c2 = s2.charAt(i);
                if (c1 != c2) {
                    c1 = Character.toUpperCase(c1);
                    c2 = Character.toUpperCase(c2);
                    if (c1 != c2) {
                        c1 = Character.toLowerCase(c1);
                        c2 = Character.toLowerCase(c2);
                        if (c1 != c2) {
                            // No overflow because of numeric promotion
                            return c1 - c2;
                        }
                    }
                }
            }
            return n1 - n2;
        }

        /** Replaces the de-serialized object. */
        private Object readResolve() { return CASE_INSENSITIVE_ORDER; }
    }

    /**
     * Compares two strings lexicographically, ignoring case
     * differences. This method returns an integer whose sign is that of
     * calling {@code compareTo} with normalized versions of the strings
     * where case differences have been eliminated by calling
     * {@code Character.toLowerCase(Character.toUpperCase(character))} on
     * each character.
     * <p>
     * Note that this method does <em>not</em> take locale into account,
     * and will result in an unsatisfactory ordering for certain locales.
     * The java.text package provides <em>collators</em> to allow
     * locale-sensitive ordering.
     *
     * @param   str   the {@code String} to be compared.
     * @return  a negative integer, zero, or a positive integer as the
     *          specified String is greater than, equal to, or less
     *          than this String, ignoring case considerations.
     * @see     java.text.Collator#compare(String, String)
     * @since   1.2
     */
    public int compareToIgnoreCase(String str) {
        return CASE_INSENSITIVE_ORDER.compare(this, str);
    }

    /**
    * @作用 从toffset位置比较从ooffset开始的other字符串，比较len位
    * @see org.ddzj.java.lang.StringTest#regionMatches()
     *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public boolean regionMatches(int toffset, String other, int ooffset,
            int len) {
        char ta[] = value;
        int to = toffset;
        char pa[] = other.value;
        int po = ooffset;
        // Note: toffset, ooffset, or len might be near -1>>>1.
        if ((ooffset < 0) || (toffset < 0)
                || (toffset > (long)value.length - len)
                || (ooffset > (long)other.value.length - len)) {
            return false;
        }
        while (len-- > 0) {
            if (ta[to++] != pa[po++]) {
                return false;
            }
        }
        return true;
    }

    /**
     * @作用 从toffset位置比较从ooffset开始的other字符串，比较len位，是否忽略大小写
     * @see org.ddzj.java.lang.StringTest#regionMatches()
     *
     * @原理 原理描述
     *
     * @备注 备注信息
     *
     * @since 0.0.1
     *
     * @author woody
     */
    public boolean regionMatches(boolean ignoreCase, int toffset,
            String other, int ooffset, int len) {
        char ta[] = value;
        int to = toffset;
        char pa[] = other.value;
        int po = ooffset;
        // Note: toffset, ooffset, or len might be near -1>>>1.
        if ((ooffset < 0) || (toffset < 0)
                || (toffset > (long)value.length - len)
                || (ooffset > (long)other.value.length - len)) {
            return false;
        }
        while (len-- > 0) {
            char c1 = ta[to++];
            char c2 = pa[po++];
            if (c1 == c2) {
                continue;
            }
            if (ignoreCase) {
                // If characters don't match but case may be ignored,
                // try converting both characters to uppercase.
                // If the results match, then the comparison scan should
                // continue.
                char u1 = Character.toUpperCase(c1);
                char u2 = Character.toUpperCase(c2);
                if (u1 == u2) {
                    continue;
                }
                // Unfortunately, conversion to uppercase does not work properly
                // for the Georgian alphabet, which has strange rules about case
                // conversion.  So we need to make one last check before
                // exiting.
                if (Character.toLowerCase(u1) == Character.toLowerCase(u2)) {
                    continue;
                }
            }
            return false;
        }
        return true;
    }

    /**
    * @作用 偏移toffset位后是否以某个字符串开头
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public boolean startsWith(String prefix, int toffset) {
        char ta[] = value;
        int to = toffset;
        char pa[] = prefix.value;
        int po = 0;
        int pc = prefix.value.length;
        // Note: toffset might be near -1>>>1.
        if ((toffset < 0) || (toffset > value.length - pc)) {
            return false;
        }
        while (--pc >= 0) {
            if (ta[to++] != pa[po++]) {
                return false;
            }
        }
        return true;
    }

    /**
    * @作用 是否以某字符串开关
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public boolean startsWith(String prefix) {
        return startsWith(prefix, 0);
    }

    /**
     * Tests if this string ends with the specified suffix.
     *
     * @param   suffix   the suffix.
     * @return  {@code true} if the character sequence represented by the
     *          argument is a suffix of the character sequence represented by
     *          this object; {@code false} otherwise. Note that the
     *          result will be {@code true} if the argument is the
     *          empty string or is equal to this {@code String} object
     *          as determined by the {@link #equals(Object)} method.
     */
    public boolean endsWith(String suffix) {
        return startsWith(suffix, value.length - suffix.value.length);
    }

    /**
    * @作用 返回字符串的哈希值
    *
    * @原理 s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public int hashCode() {
        int h = hash;
        if (h == 0 && value.length > 0) {
            char val[] = value;

            for (int i = 0; i < value.length; i++) {
                h = 31 * h + val[i];
            }
            hash = h;
        }
        return h;
    }

    /**
    * @作用 返回ACSII值为ch的字符所在的索引位置
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public int indexOf(int ch) {
        return indexOf(ch, 0);
    }

    /**
    * @作用 返回从fromIndex索引开始搜索ACSII值为ch的字符所在的索引位置
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public int indexOf(int ch, int fromIndex) {
        final int max = value.length;
        if (fromIndex < 0) {
            fromIndex = 0;
        } else if (fromIndex >= max) {
            // Note: fromIndex might be near -1>>>1.
            return -1;
        }

        if (ch < Character.MIN_SUPPLEMENTARY_CODE_POINT) {
            // handle most cases here (ch is a BMP code point or a
            // negative value (invalid code point))
            final char[] value = this.value;
            for (int i = fromIndex; i < max; i++) {
                if (value[i] == ch) {
                    return i;
                }
            }
            return -1;
        } else {
            return indexOfSupplementary(ch, fromIndex);
        }
    }

    /**
     * Handles (rare) calls of indexOf with a supplementary character.
     */
    private int indexOfSupplementary(int ch, int fromIndex) {
        if (Character.isValidCodePoint(ch)) {
            final char[] value = this.value;
            final char hi = Character.highSurrogate(ch);
            final char lo = Character.lowSurrogate(ch);
            final int max = value.length - 1;
            for (int i = fromIndex; i < max; i++) {
                if (value[i] == hi && value[i + 1] == lo) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
    * @作用 返回从最大索引位开始搜索，第一个出现ASCII值为ch的索引位置
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public int lastIndexOf(int ch) {
        return lastIndexOf(ch, value.length - 1);
    }

    /**
    * @作用 返回从最大索引位至fromIndex位开始搜索，第一个出现ASCII值为ch的索引位置
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public int lastIndexOf(int ch, int fromIndex) {
        if (ch < Character.MIN_SUPPLEMENTARY_CODE_POINT) {
            // handle most cases here (ch is a BMP code point or a
            // negative value (invalid code point))
            final char[] value = this.value;
            int i = Math.min(fromIndex, value.length - 1);
            for (; i >= 0; i--) {
                if (value[i] == ch) {
                    return i;
                }
            }
            return -1;
        } else {
            return lastIndexOfSupplementary(ch, fromIndex);
        }
    }

    /**
     * Handles (rare) calls of lastIndexOf with a supplementary character.
     */
    private int lastIndexOfSupplementary(int ch, int fromIndex) {
        if (Character.isValidCodePoint(ch)) {
            final char[] value = this.value;
            char hi = Character.highSurrogate(ch);
            char lo = Character.lowSurrogate(ch);
            int i = Math.min(fromIndex, value.length - 2);
            for (; i >= 0; i--) {
                if (value[i] == hi && value[i + 1] == lo) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * @作用 返回从索引0开始搜索，包含str的首个索引位置
     *
     * @原理 原理描述
     *
     * @备注 备注信息
     *
     * @since 0.0.1
     *
     * @author woody
     */
    public int indexOf(String str) {
        return indexOf(str, 0);
    }

    /**
    * @作用 返回从fromIndex索引开始搜索，包含str的首个索引位置
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public int indexOf(String str, int fromIndex) {
        return indexOf(value, 0, value.length,
                str.value, 0, str.value.length, fromIndex);
    }

    /**
     * Code shared by String and AbstractStringBuilder to do searches. The
     * source is the character array being searched, and the target
     * is the string being searched for.
     *
     * @param   source       the characters being searched.
     * @param   sourceOffset offset of the source string.
     * @param   sourceCount  count of the source string.
     * @param   target       the characters being searched for.
     * @param   fromIndex    the index to begin searching from.
     */
    static int indexOf(char[] source, int sourceOffset, int sourceCount,
            String target, int fromIndex) {
        return indexOf(source, sourceOffset, sourceCount,
                       target.value, 0, target.value.length,
                       fromIndex);
    }

    /**
     * Code shared by String and StringBuffer to do searches. The
     * source is the character array being searched, and the target
     * is the string being searched for.
     *
     * @param   source       the characters being searched.
     * @param   sourceOffset offset of the source string.
     * @param   sourceCount  count of the source string.
     * @param   target       the characters being searched for.
     * @param   targetOffset offset of the target string.
     * @param   targetCount  count of the target string.
     * @param   fromIndex    the index to begin searching from.
     */
    static int indexOf(char[] source, int sourceOffset, int sourceCount,
            char[] target, int targetOffset, int targetCount,
            int fromIndex) {
        if (fromIndex >= sourceCount) {
            return (targetCount == 0 ? sourceCount : -1);
        }
        if (fromIndex < 0) {
            fromIndex = 0;
        }
        if (targetCount == 0) {
            return fromIndex;
        }

        char first = target[targetOffset];
        int max = sourceOffset + (sourceCount - targetCount);

        for (int i = sourceOffset + fromIndex; i <= max; i++) {
            /* Look for first character. */
            if (source[i] != first) {
                while (++i <= max && source[i] != first);
            }

            /* Found first character, now look at the rest of v2 */
            if (i <= max) {
                int j = i + 1;
                int end = j + targetCount - 1;
                for (int k = targetOffset + 1; j < end && source[j]
                        == target[k]; j++, k++);

                if (j == end) {
                    /* Found whole string. */
                    return i - sourceOffset;
                }
            }
        }
        return -1;
    }

    /**
     * @作用 从索引最大处开始从右至左搜索字符串str第一次出现的索引位置
     *
     * @原理 原理描述
     *
     * @备注 备注信息
     *
     * @since 0.0.1
     *
     * @author woody
     */
    public int lastIndexOf(String str) {
        return lastIndexOf(str, value.length);
    }

    /**
    * @作用 从索引为fromIndex的位置开始从右至左搜索字符串str第一次出现的索引位置
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public int lastIndexOf(String str, int fromIndex) {
        return lastIndexOf(value, 0, value.length,
                str.value, 0, str.value.length, fromIndex);
    }

    /**
     * Code shared by String and AbstractStringBuilder to do searches. The
     * source is the character array being searched, and the target
     * is the string being searched for.
     *
     * @param   source       the characters being searched.
     * @param   sourceOffset offset of the source string.
     * @param   sourceCount  count of the source string.
     * @param   target       the characters being searched for.
     * @param   fromIndex    the index to begin searching from.
     */
    static int lastIndexOf(char[] source, int sourceOffset, int sourceCount,
            String target, int fromIndex) {
        return lastIndexOf(source, sourceOffset, sourceCount,
                       target.value, 0, target.value.length,
                       fromIndex);
    }

    /**
     * Code shared by String and StringBuffer to do searches. The
     * source is the character array being searched, and the target
     * is the string being searched for.
     *
     * @param   source       the characters being searched.
     * @param   sourceOffset offset of the source string.
     * @param   sourceCount  count of the source string.
     * @param   target       the characters being searched for.
     * @param   targetOffset offset of the target string.
     * @param   targetCount  count of the target string.
     * @param   fromIndex    the index to begin searching from.
     */
    static int lastIndexOf(char[] source, int sourceOffset, int sourceCount,
            char[] target, int targetOffset, int targetCount,
            int fromIndex) {
        /*
         * Check arguments; return immediately where possible. For
         * consistency, don't check for null str.
         */
        int rightIndex = sourceCount - targetCount;
        if (fromIndex < 0) {
            return -1;
        }
        if (fromIndex > rightIndex) {
            fromIndex = rightIndex;
        }
        /* Empty string always matches. */
        if (targetCount == 0) {
            return fromIndex;
        }

        int strLastIndex = targetOffset + targetCount - 1;
        char strLastChar = target[strLastIndex];
        int min = sourceOffset + targetCount - 1;
        int i = min + fromIndex;

    startSearchForLastChar:
        while (true) {
            while (i >= min && source[i] != strLastChar) {
                i--;
            }
            if (i < min) {
                return -1;
            }
            int j = i - 1;
            int start = j - (targetCount - 1);
            int k = strLastIndex - 1;

            while (j > start) {
                if (source[j--] != target[k--]) {
                    i--;
                    continue startSearchForLastChar;
                }
            }
            return start - sourceOffset + 1;
        }
    }

    /**
    * @作用 从索引为beginIndex位置开始截取字符串
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public String substring(int beginIndex) {
        if (beginIndex < 0) {
            throw new StringIndexOutOfBoundsException(beginIndex);
        }
        int subLen = value.length - beginIndex;
        if (subLen < 0) {
            throw new StringIndexOutOfBoundsException(subLen);
        }
        return (beginIndex == 0) ? this : new String(value, beginIndex, subLen);
    }

    /**
    * @作用 返回索引为[beginIndex,endIndex)的截取字符串
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public String substring(int beginIndex, int endIndex) {
        if (beginIndex < 0) {
            throw new StringIndexOutOfBoundsException(beginIndex);
        }
        if (endIndex > value.length) {
            throw new StringIndexOutOfBoundsException(endIndex);
        }
        int subLen = endIndex - beginIndex;
        if (subLen < 0) {
            throw new StringIndexOutOfBoundsException(subLen);
        }
        return ((beginIndex == 0) && (endIndex == value.length)) ? this
                : new String(value, beginIndex, subLen);
    }

    /**
    * @作用 字符串截取，与{@code String#substring(int, int)}一模一样
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public CharSequence subSequence(int beginIndex, int endIndex) {
        return this.substring(beginIndex, endIndex);
    }

    /**
    * @作用 将value与str拼接
    *
    * @原理 通过char[]拼接字符串
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public String concat(String str) {
        int otherLen = str.length();
        if (otherLen == 0) {
            return this;
        }
        int len = value.length;
        char buf[] = Arrays.copyOf(value, len + otherLen);
        str.getChars(buf, len);
        return new String(buf, true);
    }

    /**
    * @作用 将value字符串中的oldChar替换成newChar
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public String replace(char oldChar, char newChar) {
        if (oldChar != newChar) {
            int len = value.length;
            int i = -1;
            char[] val = value; /* avoid getfield opcode */

            while (++i < len) {
                if (val[i] == oldChar) {
                    break;
                }
            }
            if (i < len) {
                char buf[] = new char[len];
                for (int j = 0; j < i; j++) {
                    buf[j] = val[j];
                }
                while (i < len) {
                    char c = val[i];
                    buf[i] = (c == oldChar) ? newChar : c;
                    i++;
                }
                return new String(buf, true);
            }
        }
        return this;
    }

    /**
    * @作用 value是否能匹配正则表达式regex
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public boolean matches(String regex) {
        return Pattern.matches(regex, this);
    }

    /**
    * @作用 value是否包含字符序列s
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since x.y.z
    *
    * @author woody
    */
    public boolean contains(CharSequence s) {
        return indexOf(s.toString()) > -1;
    }

    /**
    * @作用 返回value符合regex正则的第一个字符串替换成replacement
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public String replaceFirst(String regex, String replacement) {
        return Pattern.compile(regex).matcher(this).replaceFirst(replacement);
    }

    /**
    * @作用 替换value中所有符合regex的字符串为replacement
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public String replaceAll(String regex, String replacement) {
        return Pattern.compile(regex).matcher(this).replaceAll(replacement);
    }

    /**
     * @作用 替换value中所有符合regex的字符串为replacement
     *
     * @原理 原理描述
     *
     * @备注 备注信息
     *
     * @since 0.0.1
     *
     * @author woody
     */
    public String replace(CharSequence target, CharSequence replacement) {
        return Pattern.compile(target.toString(), Pattern.LITERAL).matcher(
                this).replaceAll(Matcher.quoteReplacement(replacement.toString()));
    }

    /**
    * @作用 返回由regex分割value后字符数组，长度为limit
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since x.y.z
    *
    * @author woody
    */
    public String[] split(String regex, int limit) {
        /* fastpath if the regex is a
         (1)one-char String and this character is not one of the
            RegEx's meta characters ".$|()[{^?*+\\", or
         (2)two-char String and the first char is the backslash and
            the second is not the ascii digit or ascii letter.
         */
        char ch = 0;
        if (((regex.value.length == 1 &&
             ".$|()[{^?*+\\".indexOf(ch = regex.charAt(0)) == -1) ||
             (regex.length() == 2 &&
              regex.charAt(0) == '\\' &&
              (((ch = regex.charAt(1))-'0')|('9'-ch)) < 0 &&
              ((ch-'a')|('z'-ch)) < 0 &&
              ((ch-'A')|('Z'-ch)) < 0)) &&
            (ch < Character.MIN_HIGH_SURROGATE ||
             ch > Character.MAX_LOW_SURROGATE))
        {
            int off = 0;
            int next = 0;
            boolean limited = limit > 0;
            ArrayList<String> list = new ArrayList<>();
            while ((next = indexOf(ch, off)) != -1) {
                if (!limited || list.size() < limit - 1) {
                    list.add(substring(off, next));
                    off = next + 1;
                } else {    // last one
                    //assert (list.size() == limit - 1);
                    list.add(substring(off, value.length));
                    off = value.length;
                    break;
                }
            }
            // If no match was found, return this
            if (off == 0)
                return new String[]{this};

            // Add remaining segment
            if (!limited || list.size() < limit)
                list.add(substring(off, value.length));

            // Construct result
            int resultSize = list.size();
            if (limit == 0) {
                while (resultSize > 0 && list.get(resultSize - 1).length() == 0) {
                    resultSize--;
                }
            }
            String[] result = new String[resultSize];
            return list.subList(0, resultSize).toArray(result);
        }
        return Pattern.compile(regex).split(this, limit);
    }

    /**
    * @作用 返回由regex分割value后字符数组
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public String[] split(String regex) {
        return split(regex, 0);
    }

    /**
    * @作用 使用delimiter分隔符拼接elements字符序列可变数组成字符串
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public static String join(CharSequence delimiter, CharSequence... elements) {
        Objects.requireNonNull(delimiter);
        Objects.requireNonNull(elements);
        // Number of elements not likely worth Arrays.stream overhead.
        StringJoiner joiner = new StringJoiner(delimiter);
        for (CharSequence cs: elements) {
            joiner.add(cs);
        }
        return joiner.toString();
    }

    /**
     * Returns a new {@code String} composed of copies of the
     * {@code CharSequence elements} joined together with a copy of the
     * specified {@code delimiter}.
     *
     * <blockquote>For example,
     * <pre>{@code
     *     List<String> strings = new LinkedList<>();
     *     strings.add("Java");strings.add("is");
     *     strings.add("cool");
     *     String message = String.join(" ", strings);
     *     //message returned is: "Java is cool"
     *
     *     Set<String> strings = new LinkedHashSet<>();
     *     strings.add("Java"); strings.add("is");
     *     strings.add("very"); strings.add("cool");
     *     String message = String.join("-", strings);
     *     //message returned is: "Java-is-very-cool"
     * }</pre></blockquote>
     *
     * Note that if an individual element is {@code null}, then {@code "null"} is added.
     *
     * @param  delimiter a sequence of characters that is used to separate each
     *         of the {@code elements} in the resulting {@code String}
     * @param  elements an {@code Iterable} that will have its {@code elements}
     *         joined together.
     *
     * @return a new {@code String} that is composed from the {@code elements}
     *         argument
     *
     * @throws NullPointerException If {@code delimiter} or {@code elements}
     *         is {@code null}
     *
     * @see    #join(CharSequence,CharSequence...)
     * @see    java.util.StringJoiner
     * @since 1.8
     */
    public static String join(CharSequence delimiter,
            Iterable<? extends CharSequence> elements) {
        Objects.requireNonNull(delimiter);
        Objects.requireNonNull(elements);
        StringJoiner joiner = new StringJoiner(delimiter);
        for (CharSequence cs: elements) {
            joiner.add(cs);
        }
        return joiner.toString();
    }

    /**
    * @作用 转换成本地语言的小写
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public String toLowerCase(Locale locale) {
        if (locale == null) {
            throw new NullPointerException();
        }

        int firstUpper;
        final int len = value.length;

        /* Now check if there are any characters that need to be changed. */
        scan: {
            for (firstUpper = 0 ; firstUpper < len; ) {
                char c = value[firstUpper];
                if ((c >= Character.MIN_HIGH_SURROGATE)
                        && (c <= Character.MAX_HIGH_SURROGATE)) {
                    int supplChar = codePointAt(firstUpper);
                    if (supplChar != Character.toLowerCase(supplChar)) {
                        break scan;
                    }
                    firstUpper += Character.charCount(supplChar);
                } else {
                    if (c != Character.toLowerCase(c)) {
                        break scan;
                    }
                    firstUpper++;
                }
            }
            return this;
        }

        char[] result = new char[len];
        int resultOffset = 0;  /* result may grow, so i+resultOffset
                                * is the write location in result */

        /* Just copy the first few lowerCase characters. */
        System.arraycopy(value, 0, result, 0, firstUpper);

        String lang = locale.getLanguage();
        boolean localeDependent =
                (lang == "tr" || lang == "az" || lang == "lt");
        char[] lowerCharArray;
        int lowerChar;
        int srcChar;
        int srcCount;
        for (int i = firstUpper; i < len; i += srcCount) {
            srcChar = (int)value[i];
            if ((char)srcChar >= Character.MIN_HIGH_SURROGATE
                    && (char)srcChar <= Character.MAX_HIGH_SURROGATE) {
                srcChar = codePointAt(i);
                srcCount = Character.charCount(srcChar);
            } else {
                srcCount = 1;
            }
            if (localeDependent ||
                srcChar == '\u03A3' || // GREEK CAPITAL LETTER SIGMA
                srcChar == '\u0130') { // LATIN CAPITAL LETTER I WITH DOT ABOVE
                lowerChar = ConditionalSpecialCasing.toLowerCaseEx(this, i, locale);
            } else {
                lowerChar = Character.toLowerCase(srcChar);
            }
            if ((lowerChar == Character.ERROR)
                    || (lowerChar >= Character.MIN_SUPPLEMENTARY_CODE_POINT)) {
                if (lowerChar == Character.ERROR) {
                    lowerCharArray =
                            ConditionalSpecialCasing.toLowerCaseCharArray(this, i, locale);
                } else if (srcCount == 2) {
                    resultOffset += Character.toChars(lowerChar, result, i + resultOffset) - srcCount;
                    continue;
                } else {
                    lowerCharArray = Character.toChars(lowerChar);
                }

                /* Grow result if needed */
                int mapLen = lowerCharArray.length;
                if (mapLen > srcCount) {
                    char[] result2 = new char[result.length + mapLen - srcCount];
                    System.arraycopy(result, 0, result2, 0, i + resultOffset);
                    result = result2;
                }
                for (int x = 0; x < mapLen; ++x) {
                    result[i + resultOffset + x] = lowerCharArray[x];
                }
                resultOffset += (mapLen - srcCount);
            } else {
                result[i + resultOffset] = (char)lowerChar;
            }
        }
        return new String(result, 0, len + resultOffset);
    }

    /**
    * @作用 转换小写
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public String toLowerCase() {
        return toLowerCase(Locale.getDefault());
    }

    /**
    * @作用 转换成本地语言的大写字符串
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public String toUpperCase(Locale locale) {
        if (locale == null) {
            throw new NullPointerException();
        }

        int firstLower;
        final int len = value.length;

        /* Now check if there are any characters that need to be changed. */
        scan: {
            for (firstLower = 0 ; firstLower < len; ) {
                int c = (int)value[firstLower];
                int srcCount;
                if ((c >= Character.MIN_HIGH_SURROGATE)
                        && (c <= Character.MAX_HIGH_SURROGATE)) {
                    c = codePointAt(firstLower);
                    srcCount = Character.charCount(c);
                } else {
                    srcCount = 1;
                }
                int upperCaseChar = Character.toUpperCaseEx(c);
                if ((upperCaseChar == Character.ERROR)
                        || (c != upperCaseChar)) {
                    break scan;
                }
                firstLower += srcCount;
            }
            return this;
        }

        /* result may grow, so i+resultOffset is the write location in result */
        int resultOffset = 0;
        char[] result = new char[len]; /* may grow */

        /* Just copy the first few upperCase characters. */
        System.arraycopy(value, 0, result, 0, firstLower);

        String lang = locale.getLanguage();
        boolean localeDependent =
                (lang == "tr" || lang == "az" || lang == "lt");
        char[] upperCharArray;
        int upperChar;
        int srcChar;
        int srcCount;
        for (int i = firstLower; i < len; i += srcCount) {
            srcChar = (int)value[i];
            if ((char)srcChar >= Character.MIN_HIGH_SURROGATE &&
                (char)srcChar <= Character.MAX_HIGH_SURROGATE) {
                srcChar = codePointAt(i);
                srcCount = Character.charCount(srcChar);
            } else {
                srcCount = 1;
            }
            if (localeDependent) {
                upperChar = ConditionalSpecialCasing.toUpperCaseEx(this, i, locale);
            } else {
                upperChar = Character.toUpperCaseEx(srcChar);
            }
            if ((upperChar == Character.ERROR)
                    || (upperChar >= Character.MIN_SUPPLEMENTARY_CODE_POINT)) {
                if (upperChar == Character.ERROR) {
                    if (localeDependent) {
                        upperCharArray =
                                ConditionalSpecialCasing.toUpperCaseCharArray(this, i, locale);
                    } else {
                        upperCharArray = Character.toUpperCaseCharArray(srcChar);
                    }
                } else if (srcCount == 2) {
                    resultOffset += Character.toChars(upperChar, result, i + resultOffset) - srcCount;
                    continue;
                } else {
                    upperCharArray = Character.toChars(upperChar);
                }

                /* Grow result if needed */
                int mapLen = upperCharArray.length;
                if (mapLen > srcCount) {
                    char[] result2 = new char[result.length + mapLen - srcCount];
                    System.arraycopy(result, 0, result2, 0, i + resultOffset);
                    result = result2;
                }
                for (int x = 0; x < mapLen; ++x) {
                    result[i + resultOffset + x] = upperCharArray[x];
                }
                resultOffset += (mapLen - srcCount);
            } else {
                result[i + resultOffset] = (char)upperChar;
            }
        }
        return new String(result, 0, len + resultOffset);
    }

    /**
    * @作用 转换成大写字符串
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public String toUpperCase() {
        return toUpperCase(Locale.getDefault());
    }

    /**
    * @作用 去除value前后空格
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public String trim() {
        int len = value.length;
        int st = 0;
        char[] val = value;    /* avoid getfield opcode */

        while ((st < len) && (val[st] <= ' ')) {
            st++;
        }
        while ((st < len) && (val[len - 1] <= ' ')) {
            len--;
        }
        return ((st > 0) || (len < value.length)) ? substring(st, len) : this;
    }

    /**
    * @作用 返回当前字符串
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public String toString() {
        return this;
    }

    /**
    * @作用 String转char[]
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public char[] toCharArray() {
        // Cannot use Arrays.copyOf because of class initialization order issues
        char result[] = new char[value.length];
        System.arraycopy(value, 0, result, 0, value.length);
        return result;
    }

    /**
    * @作用 通过format格式进行格式化
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public static String format(String format, Object... args) {
        return new Formatter().format(format, args).toString();
    }

    /**
     * Returns a formatted string using the specified locale, format string,
     * and arguments.
     *
     * @param  l
     *         The {@linkplain java.util.Locale locale} to apply during
     *         formatting.  If {@code l} is {@code null} then no localization
     *         is applied.
     *
     * @param  format
     *         A <a href="../util/Formatter.html#syntax">format string</a>
     *
     * @param  args
     *         Arguments referenced by the format specifiers in the format
     *         string.  If there are more arguments than format specifiers, the
     *         extra arguments are ignored.  The number of arguments is
     *         variable and may be zero.  The maximum number of arguments is
     *         limited by the maximum dimension of a Java array as defined by
     *         <cite>The Java&trade; Virtual Machine Specification</cite>.
     *         The behaviour on a
     *         {@code null} argument depends on the
     *         <a href="../util/Formatter.html#syntax">conversion</a>.
     *
     * @throws  java.util.IllegalFormatException
     *          If a format string contains an illegal syntax, a format
     *          specifier that is incompatible with the given arguments,
     *          insufficient arguments given the format string, or other
     *          illegal conditions.  For specification of all possible
     *          formatting errors, see the <a
     *          href="../util/Formatter.html#detail">Details</a> section of the
     *          formatter class specification
     *
     * @return  A formatted string
     *
     * @see  java.util.Formatter
     * @since  1.5
     */
    public static String format(Locale l, String format, Object... args) {
        return new Formatter(l).format(format, args).toString();
    }

    /**
    * @作用 返回obj的字符串表现形式 类信息@哈希值转换后的无符号形式
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public static String valueOf(Object obj) {
        return (obj == null) ? "null" : obj.toString();
    }

    /**
     * Returns the string representation of the {@code char} array
     * argument. The contents of the character array are copied; subsequent
     * modification of the character array does not affect the returned
     * string.
     *
     * @param   data     the character array.
     * @return  a {@code String} that contains the characters of the
     *          character array.
     */
    public static String valueOf(char data[]) {
        return new String(data);
    }

    /**
    * @作用 返回char[]偏移offset位，长度为count位的字符串
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public static String valueOf(char data[], int offset, int count) {
        return new String(data, offset, count);
    }

    /**
    * @作用 返回char[]偏移offset位，长度为count位的字符串
    *
    * @原理
     * @see java.lang.String
    *
    * @备注 备注信息
    *
    * @since x.y.z
    *
    * @author woody
    */
    public static String copyValueOf(char data[], int offset, int count) {
        return new String(data, offset, count);
    }

    /**
     * Equivalent to {@link #valueOf(char[])}.
     *
     * @param   data   the character array.
     * @return  a {@code String} that contains the characters of the
     *          character array.
     */
    public static String copyValueOf(char data[]) {
        return new String(data);
    }

    /**
    * @作用 返回boolean类型的字符串表示形式
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public static String valueOf(boolean b) {
        return b ? "true" : "false";
    }

    /**
    * @作用 返回一个字符的字符串形式
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public static String valueOf(char c) {
        char data[] = {c};
        return new String(data, true);
    }

    /**
    * @作用 返回int的字符串开示形式
    *
    * @原理 原理描述
    *
    * @备注 备注信息
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public static String valueOf(int i) {
        return Integer.toString(i);
    }

    /**
     * @作用 返回long的字符串开示形式
     *
     * @原理 原理描述
     *
     * @备注 备注信息
     *
     * @since 0.0.1
     *
     * @author woody
     */
    public static String valueOf(long l) {
        return Long.toString(l);
    }

    /**
     * Returns the string representation of the {@code float} argument.
     * <p>
     * The representation is exactly the one returned by the
     * {@code Float.toString} method of one argument.
     *
     * @param   f   a {@code float}.
     * @return  a string representation of the {@code float} argument.
     * @see     java.lang.Float#toString(float)
     */
    public static String valueOf(float f) {
        return Float.toString(f);
    }

    /**
     * Returns the string representation of the {@code double} argument.
     * <p>
     * The representation is exactly the one returned by the
     * {@code Double.toString} method of one argument.
     *
     * @param   d   a {@code double}.
     * @return  a  string representation of the {@code double} argument.
     * @see     java.lang.Double#toString(double)
     */
    public static String valueOf(double d) {
        return Double.toString(d);
    }

    /**
    * @作用 如果池中已经有了这个String对象，那么直接返回，
     * 另一方面如果没有那么就将这个String对象添加到池中，并返回这个字符串的引用
    * @see org.ddzj.java.lang.StringTest#intern()
     *
    * @原理 原理描述
    *
    * @备注
     * String s1 = new String(""); //第一种
     * String s2 = ""; //第二种
     * 第一种始终不会入池的.
     * 第二种要看情况而定(等号右边如果是常量则入池,非常量则不入池)
     *
    *
    * @since 0.0.1
    *
    * @author woody
    */
    public native String intern();
}
