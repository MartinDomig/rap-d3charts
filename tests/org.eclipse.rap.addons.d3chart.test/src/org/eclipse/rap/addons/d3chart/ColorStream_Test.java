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


public class ColorStream_Test {

  private Display display;

  @Rule
  public TestContext context = new TestContext();

  @Before
  public void setUp() {
    display = new Display();
  }

  @Test( expected = NullPointerException.class )
  public void creationFailsWithNull() {
    new ColorStream( null );
  }

  @Test( expected = IllegalStateException.class )
  public void creationFailsWithDisposedSequence() {
    ColorSequence sequence = new ColorSequence( display, new RGB( 1,  2,  3 ) );
    sequence.dispose();

    new ColorStream( sequence );
  }

  @Test
  public void next_loopsWithSingleColor() {
    ColorStream stream = new ColorStream( new ColorSequence( display, new RGB( 0, 0, 1 ) ) );

    assertEquals( new Color( display, 0, 0, 1 ), stream.next() );
    assertEquals( new Color( display, 0, 0, 1 ), stream.next() );
  }

  @Test
  public void next_loopsThroughColors() {
    ColorSequence sequence = new ColorSequence( display, new RGB( 0, 0, 1 ), new RGB( 0, 0, 2 ) );
    ColorStream stream = new ColorStream( sequence );

    assertEquals( new Color( display, 0, 0, 1 ), stream.next() );
    assertEquals( new Color( display, 0, 0, 2 ), stream.next() );
    assertEquals( new Color( display, 0, 0, 1 ), stream.next() );
  }

  @Test( expected = IllegalStateException.class )
  public void next_checksDisposed() {
    ColorSequence sequence = new ColorSequence( display, new RGB( 1,  2,  3 ) );
    ColorStream colorStream = new ColorStream( sequence );

    sequence.dispose();

    colorStream.next();
  }

}
