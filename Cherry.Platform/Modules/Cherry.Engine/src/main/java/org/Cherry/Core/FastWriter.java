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
 * Mark Reinhold  - initial design, API and implementation for java.io.Writer
 * Cristian Malinescu - refactoring
 *******************************************************************************/
package org.Cherry.Core;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

public abstract class FastWriter implements Appendable, Closeable, Flushable {

  /**
   * Temporary buffer used to hold writes of strings and single characters
   */
  private char[] _writeBuffer;

  /**
   * Size of writeBuffer, must be >= 1
   */
  private final int _writeBufferSize = 4096;

  /**
   * Creates a new character-stream writer whose critical sections will synchronize on the writer itself.
   */
  protected FastWriter() {
  }

  /**
   * Creates a new character-stream writer whose critical sections will synchronize on the given object.
   * 
   * @param lock
   *          Object to synchronize on
   */
  protected FastWriter(final Object lock) {
    if (lock == null) throw new NullPointerException();
  }

  /**
   * Writes a single character. The character to be written is contained in the 16 low-order bits of the given integer value; the 16 high-order bits are
   * ignored.
   * 
   * <p>
   * Subclasses that intend to support efficient single-character output should override this method.
   * 
   * @param c
   *          int specifying a character to be written
   * 
   * @throws IOException
   *           If an I/O error occurs
   */
  public void write(final int c) throws IOException {
    if (_writeBuffer == null) _writeBuffer = new char[_writeBufferSize];
    _writeBuffer[0] = (char) c;
    write(_writeBuffer, 0, 1);
  }

  /**
   * Writes an array of characters.
   * 
   * @param cbuf
   *          Array of characters to be written
   * 
   * @throws IOException
   *           If an I/O error occurs
   */
  public void write(final char cbuf[]) throws IOException {
    write(cbuf, 0, cbuf.length);
  }

  /**
   * Writes a portion of an array of characters.
   * 
   * @param cbuf
   *          Array of characters
   * 
   * @param off
   *          Offset from which to start writing characters
   * 
   * @param len
   *          Number of characters to write
   * 
   * @throws IOException
   *           If an I/O error occurs
   */
  abstract public void write(char cbuf[], int off, int len) throws IOException;

  /**
   * Writes a string.
   * 
   * @param str
   *          String to be written
   * 
   * @throws IOException
   *           If an I/O error occurs
   */
  public void write(final String str) throws IOException {
    write(str, 0, str.length());
  }

  /**
   * Writes a portion of a string.
   * 
   * @param str
   *          A String
   * 
   * @param off
   *          Offset from which to start writing characters
   * 
   * @param len
   *          Number of characters to write
   * 
   * @throws IndexOutOfBoundsException
   *           If <tt>off</tt> is negative, or <tt>len</tt> is negative, or <tt>off+len</tt> is negative or greater than the length of the given string
   * 
   * @throws IOException
   *           If an I/O error occurs
   */
  public void write(final String str, final int off, final int len) throws IOException {
    char cbuf[];
    if (len <= _writeBufferSize) {
      if (_writeBuffer == null) _writeBuffer = new char[_writeBufferSize];
      cbuf = _writeBuffer;
    } else cbuf = new char[len];
    str.getChars(off, off + len, cbuf, 0);
    write(cbuf, 0, len);
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
   * @throws IOException
   *           If an I/O error occurs
   * 
   * @since 1.5
   */
  @Override
  public FastWriter append(final CharSequence csq) throws IOException {
    if (csq == null)
      write("null");
    else write(csq.toString());
    return this;
  }

  /**
   * Appends a subsequence of the specified character sequence to this writer. <tt>Appendable</tt>.
   * 
   * <p>
   * An invocation of this method of the form <tt>out.append(csq, start,
   * end)</tt> when <tt>csq</tt> is not <tt>null</tt> behaves in exactly the same way as the invocation
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
   * @throws IOException
   *           If an I/O error occurs
   * 
   * @since 1.5
   */
  @Override
  public FastWriter append(final CharSequence csq, final int start, final int end) throws IOException {
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
   * @throws IOException
   *           If an I/O error occurs
   * 
   * @since 1.5
   */
  @Override
  public FastWriter append(final char c) throws IOException {
    write(c);
    return this;
  }

  /**
   * Flushes the stream. If the stream has saved any characters from the various write() methods in a buffer, write them immediately to their intended
   * destination. Then, if that destination is another character or byte stream, flush it. Thus one flush() invocation will flush all the buffers in a chain of
   * Writers and OutputStreams.
   * 
   * <p>
   * If the intended destination of this stream is an abstraction provided by the underlying operating system, for example a file, then flushing the stream
   * guarantees only that bytes previously written to the stream are passed to the operating system for writing; it does not guarantee that they are actually
   * written to a physical device such as a disk drive.
   * 
   * @throws IOException
   *           If an I/O error occurs
   */
  @Override
  abstract public void flush() throws IOException;

  /**
   * Closes the stream, flushing it first. Once the stream has been closed, further write() or flush() invocations will cause an IOException to be thrown.
   * Closing a previously closed stream has no effect.
   * 
   * @throws IOException
   *           If an I/O error occurs
   */
  @Override
  abstract public void close() throws IOException;
}
