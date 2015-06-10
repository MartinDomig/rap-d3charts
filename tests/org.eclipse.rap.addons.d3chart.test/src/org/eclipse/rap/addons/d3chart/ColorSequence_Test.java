/*******************************************************************************
 * Copyright (c) 2013, 2015 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ralf Sternberg - initial API and implementation
 ******************************************************************************/
package org.eclipse.rap.addons.d3chart;

import static org.junit.Assert.*;

import org.eclipse.rap.addons.d3chart.ColorSequence;
import org.eclipse.rap.addons.d3chart.ColorStream;
import org.eclipse.rap.rwt.testfixture.TestContext;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


public class ColorSequence_Test {

  private Display display;
  private final RGB rgb1 = new RGB( 0, 0, 1 );
  private final RGB rgb2 = new RGB( 0, 0, 2 );
  private final RGB rgb3 = new RGB( 0, 0, 3 );

  @Rule
  public TestContext context = new TestContext();

  @Before
  public void setUp() {
    display = new Display();
  }

  @Test( expected = NullPointerException.class )
  public void creationFailsWithNullDisplay() {
    new ColorSequence( null );
  }

  @Test( expected = NullPointerException.class )
  public void creationFailsWithNullColors() {
    new ColorSequence( display, (RGB[])null );
  }

  @Test( expected = IllegalArgumentException.class )
  public void creationFailsWithoutColor() {
    new ColorSequence( display );
  }

  @Test
  public void loop_createsStreamWithSingleColor() {
    ColorSequence sequence = new ColorSequence( display, rgb1 );

    ColorStream stream = sequence.loop();

    assertEquals( new Color( display, rgb1 ), stream.next() );
  }

  @Test
  public void loop_createsStreamWithMultipleColors() {
    ColorSequence sequence = new ColorSequence( display, rgb1, rgb2, rgb3 );

    ColorStream stream = sequence.loop();

    assertEquals( new Color( display, rgb1 ), stream.next() );
    assertEquals( new Color( display, rgb2 ), stream.next() );
    assertEquals( new Color( display, rgb3 ), stream.next() );
  }

  @Test
  public void loop_createsLoopingStream() {
    ColorSequence sequence = new ColorSequence( display, rgb1, rgb2 );

    ColorStream stream = sequence.loop();

    assertEquals( new Color( display, rgb1 ), stream.next() );
    assertEquals( new Color( display, rgb2 ), stream.next() );
    assertEquals( new Color( display, rgb1 ), stream.next() );
  }

  @Test( expected = IllegalStateException.class )
  public void loop_checksDisposed() {
    ColorSequence sequence = new ColorSequence( display, rgb1, rgb2 );
    sequence.dispose();

    sequence.loop();
  }

  @Test
  public void keepsSafeCopyOfColorsArray() {
    RGB[] colors = { rgb1, rgb2, rgb3 };
    ColorSequence sequence = new ColorSequence( display, colors );

    colors[ 0 ] = new RGB( 47, 47, 47 );
    ColorStream stream = sequence.loop();

    assertEquals( new Color( display, rgb1 ), stream.next() );
  }

  @Test
  public void size() {
    ColorSequence sequence = new ColorSequence( display, rgb1, rgb2, rgb3 );

    int size = sequence.size();

    assertEquals( 3, size );
  }

  @Test( expected = IllegalStateException.class )
  public void size_checksDisposed() {
    ColorSequence sequence = new ColorSequence( display, rgb1, rgb2, rgb3 );
    sequence.dispose();

    sequence.size();
  }

  @Test( expected = IndexOutOfBoundsException.class )
  public void get_failsWithNegativeIndex() {
    ColorSequence sequence = new ColorSequence( display, rgb1 );

    sequence.get( -1 );
  }

  @Test( expected = IllegalStateException.class )
  public void get_checksDispose() {
    ColorSequence sequence = new ColorSequence( display, rgb1 );
    sequence.dispose();

    sequence.get( 0 );
  }

  @Test
  public void isDispose_falseByDefault() {
    ColorSequence sequence = new ColorSequence( display, rgb1 );

    assertFalse( sequence.isDisposed() );
  }

  @Test
  public void isDispose_trueAfterDispose() {
    ColorSequence sequence = new ColorSequence( display, rgb1 );
    sequence.dispose();

    assertTrue( sequence.isDisposed() );
  }

  @Test
  public void dispose_disposesColors() {
    ColorSequence sequence = new ColorSequence( display, rgb1 );
    Color color = sequence.get( 0 );

    sequence.dispose();

    assertTrue( color.isDisposed() );
  }

}
