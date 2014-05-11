/*******************************************************************************
 * Copyright (c) 2013-2014 Cherry Platform
 * 
 * The contents of this file are subject to the terms of one of the following
 * open source licenses: Apache 2.0 or LGPL 3.0 or LGPL 2.1 or CDDL 1.0 or EPL
 * 1.0 (the "Licenses"). You can select the license that you prefer but you may
 * not use this file except in compliance with one of these Licenses.
 * 
 * You can obtain a copy of the Apache 2.0 license at
 * http://www.opensource.org/licenses/apache-2.0
 * 
 * You can obtain a copy of the LGPL 3.0 license at
 * http://www.opensource.org/licenses/lgpl-3.0
 * 
 * You can obtain a copy of the LGPL 2.1 license at
 * http://www.opensource.org/licenses/lgpl-2.1
 * 
 * You can obtain a copy of the CDDL 1.0 license at
 * http://www.opensource.org/licenses/cddl1
 * 
 * You can obtain a copy of the EPL 1.0 license at
 * http://www.opensource.org/licenses/eclipse-1.0
 * 
 * See the Licenses for the specific language governing permissions and
 * limitations under the Licenses.
 * 
 * 
 * Cherry Platform is a registered trademark of Sabadios
 * 
 * Contributors:
 * Mark Reinhold  - initial design, API and implementation for java.io.StringWriter
 * Cristian Malinescu - refactoring
 *******************************************************************************/
package org.Cherry.Core;

import java.io.IOException;
import java.io.Writer;
import java.lang.ref.SoftReference;

public class FastStringWriter extends Writer {
  @Override
  public void write(final int c) {
    _stringBuilder.get().append((char) c);
  }

  /**
   * Write a portion of an array of characters.
   * 
   * @param cbuf
   *          Array of characters
   * @param off
   *          Offset from which to start writing characters
   * @param len
   *          Number of characters to write
   */
  @Override
  public void write(final char cbuf[], final int off, final int len) {
    if (off < 0 || off > cbuf.length || len < 0 ||
        off + len > cbuf.length || off + len < 0)
      throw new IndexOutOfBoundsException();
    else if (len == 0) return;

    if (null != _stringBuilder.get())
      _stringBuilder.get().append(cbuf, off, len);
    else throw new IllegalStateException(new NullPointerException());
  }

  /**
   * Write a string.
   */
  @Override
  public void write(final String str) {
    _stringBuilder.get().append(str);
  }

  /**
   * Write a portion of a string.
   * 
   * @param str
   *          String to be written
   * @param off
   *          Offset from which to start writing characters
   * @param len
   *          Number of characters to write
   */
  @Override
  public void write(final String str, final int off, final int len) {
    _stringBuilder.get().append(str.substring(off, off + len));
  }

  /**
   * Appends the specified character sequence to this writer.
   * 
   * <p>
   * An invocation of this method of the form <tt>out.append(csq)</tt> behaves in exactly the same way as the invocation
   * 
   * <pre>
   * out.write(csq.toString())
   * </pre>
   * 
   * <p>
   * Depending on the specification of <tt>toString</tt> for the character sequence <tt>csq</tt>, the entire sequence may not be appended. For instance,
   * invoking the <tt>toString</tt> method of a character buffer will return a subsequence whose content depends upon the buffer's position and limit.
   * 
   * @param csq
   *          The character sequence to append. If <tt>csq</tt> is <tt>null</tt>, then the four characters <tt>"null"</tt> are appended to this writer.
   * 
   * @return This writer
   * 
   * @since 1.5
   */
  @Override
  public FastStringWriter append(final CharSequence csq) {
    if (csq == null)
      write("null");
    else write(csq.toString());
    return this;
  }

  /**
   * Appends a subsequence of the specified character sequence to this writer.
   * 
   * <p>
   * An invocation of this method of the form <tt>out.append(csq, start,
   * end)</tt> when <tt>csq</tt> is not <tt>null</tt>, behaves in exactly the same way as the invocation
   * 
   * <pre>
   * out.write(csq.subSequence(start, end).toString())
   * </pre>
   * 
   * @param csq
   *          The character sequence from which a subsequence will be appended. If <tt>csq</tt> is <tt>null</tt>, then characters will be appended as if
   *          <tt>csq</tt> contained the four characters <tt>"null"</tt>.
   * 
   * @param start
   *          The index of the first character in the subsequence
   * 
   * @param end
   *          The index of the character following the last character in the subsequence
   * 
   * @return This writer
   * 
   * @throws IndexOutOfBoundsException
   *           If <tt>start</tt> or <tt>end</tt> are negative, <tt>start</tt> is greater than <tt>end</tt>, or <tt>end</tt> is greater than
   *           <tt>csq.length()</tt>
   * 
   * @since 1.5
   */
  @Override
  public FastStringWriter append(final CharSequence csq, final int start, final int end) {
    final CharSequence cs = csq == null ? "null" : csq;
    write(cs.subSequence(start, end).toString());
    return this;
  }

  /**
   * Appends the specified character to this writer.
   * 
   * <p>
   * An invocation of this method of the form <tt>out.append(c)</tt> behaves in exactly the same way as the invocation
   * 
   * <pre>
   * out.write(c)
   * </pre>
   * 
   * @param c
   *          The 16-bit character to append
   * 
   * @return This writer
   * 
   * @since 1.5
   */
  @Override
  public FastStringWriter append(final char c) {
    write(c);
    return this;
  }

  /**
   * Return the buffer's current value as a string.
   */
  @Override
  public String toString() {
    return _stringBuilder.get().toString();
  }

  /**
   * Return the string buffer itself.
   * 
   * @return StringBuilder holding the current buffer value.
   */
  public StringBuilder getBuffer() {
    return _stringBuilder.get();
  }

  /**
   * Flush the stream.
   */
  @Override
  public void flush() {
  }

  /**
   * Closing a <tt>StringWriter</tt> has no effect. The methods in this class can be called after the stream has been closed without generating an
   * <tt>IOException</tt>.
   */
  @Override
  public void close() throws IOException {
  }

  private final SoftReference<StringBuilder> _stringBuilder;

  /**
   * Create a new string writer using the default initial string-buffer size.
   */
  public FastStringWriter() {
    _stringBuilder = new SoftReference<StringBuilder>(new StringBuilder());
  }

  /**
   * Create a new string writer using the specified initial string-buffer size.
   * 
   * @param initialSize
   *          The number of <tt>char</tt> values that will fit into this buffer before it is automatically expanded
   * 
   * @throws IllegalArgumentException
   *           If <tt>initialSize</tt> is negative
   */
  public FastStringWriter(final int initialSize) {
    if (initialSize < 0) throw new IllegalArgumentException("Negative buffer size");
    _stringBuilder = new SoftReference<StringBuilder>(new StringBuilder(initialSize));
  }
}
