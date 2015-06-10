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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;

import java.util.Arrays;

import org.eclipse.rap.json.JsonArray;
import org.eclipse.rap.json.JsonValue;
import org.eclipse.rap.rwt.remote.Connection;
import org.eclipse.rap.rwt.remote.JsonMapping;
import org.eclipse.rap.rwt.remote.RemoteObject;
import org.eclipse.rap.rwt.testfixture.TestContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;


public class ChartItem_Test {

  private Display display;
  private Shell shell;
  private Chart chart;

  @Rule
  public TestContext context = new TestContext();

  @Before
  public void setUp() {
    display = new Display();
    shell = new Shell( display );
    chart = new Chart( shell, SWT.NONE, "foo" ) {};
  }

  @Test
  public void create_registersWithParent() {
    ChartItem chartItem = new ChartItem( chart );

    assertTrue( Arrays.asList( chart.getItems() ).contains( chartItem ) );
  }

  @Test
  public void create_createsRemoteObject() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    Connection connection = fakeConnection( remoteObject );

    new ChartItem( chart );

    verify( connection ).createRemoteObject( eq( "d3chart.ChartItem" ) );
  }

  @Test
  public void create_setsRemoteParent() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    fakeConnection( remoteObject );

    new ChartItem( chart );

    verify( remoteObject ).set( eq( "parent" ), eq( chart.getRemoteId() ) );
  }

  @Test
  public void dispose_deregistersFromParent() {
    ChartItem chartItem = new ChartItem( chart );

    chartItem.dispose();

    assertFalse( Arrays.asList( chart.getItems() ).contains( chartItem ) );
  }

  @Test
  public void dispose_destroysRemoteObject() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    fakeConnection( remoteObject );
    ChartItem chartItem = new ChartItem( chart );

    chartItem.dispose();

    verify( remoteObject ).destroy();
  }

  @Test
  public void getValue_defaultsToZero() {
    ChartItem chartItem = new ChartItem( chart );

    double result = chartItem.getValue();

    assertEquals( 0, result, 0 );
  }

  @Test( expected = SWTException.class )
  public void getValue_checksWidget() {
    ChartItem chartItem = new ChartItem( chart );
    chartItem.dispose();

    chartItem.getValue();
  }

  @Test
  public void setValue_changesValue() {
    ChartItem chartItem = new ChartItem( chart );

    chartItem.setValue( 3.14f );

    assertEquals( 3.14f, chartItem.getValue(), 0 );
  }

  @Test( expected = SWTException.class )
  public void setValue_checksWidget() {
    ChartItem chartItem = new ChartItem( chart );
    chartItem.dispose();

    chartItem.setValue( 3.14f );
  }

  @Test
  public void setValue_isRendered() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    fakeConnection( remoteObject );

    new ChartItem( chart ).setValue( 3.14f );

    verify( remoteObject ).set( eq( "value" ), eq( (double)3.14f ) );
  }

  @Test
  public void setValue_isNotRenderedIfUnchanged() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    fakeConnection( remoteObject );
    ChartItem chartItem = new ChartItem( chart );
    chartItem.setValue( 3.14f );
    reset( remoteObject );

    chartItem.setValue( 3.14f );

    verifyZeroInteractions( remoteObject );
  }

  @Test
  public void getValues_defaultsToNull() {
    ChartItem chartItem = new ChartItem( chart );

    float[] values = chartItem.getValues();

    assertNull( values );
  }

  @Test( expected = SWTException.class )
  public void getValues_checksWidget() {
    ChartItem chartItem = new ChartItem( chart );
    chartItem.dispose();

    chartItem.getValues();
  }

  @Test
  public void setValues_changesValues() {
    ChartItem chartItem = new ChartItem( chart );

    chartItem.setValues( 3.14f, 1.41f );

    assertArrayEquals( new float[] { 3.14f, 1.41f }, chartItem.getValues(), 0 );
  }

  @Test
  public void setValues_doesNotChangeValue() {
    ChartItem chartItem = new ChartItem( chart );
    chartItem.setValue( 3.14f );

    chartItem.setValues( 1.41f );

    assertEquals( 3.14f, chartItem.getValue(), 0 );
  }

  @Test( expected = SWTException.class )
  public void setValues_checksWidget() {
    ChartItem chartItem = new ChartItem( chart );
    chartItem.dispose();

    chartItem.setValues( 3.14f, 1.41f );
  }

  @Test
  public void setValues_isRendered() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    fakeConnection( remoteObject );

    new ChartItem( chart ).setValues( 3.14f, 1.41f );

    verify( remoteObject ).set( eq( "values" ), eq( new JsonArray().add( 3.14f ).add( 1.41f ) ) );
  }

  @Test
  public void setValues_isNotRenderedIfUnchanged() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    fakeConnection( remoteObject );
    ChartItem chartItem = new ChartItem( chart );
    chartItem.setValues( 3.14f, 1.41f );
    reset( remoteObject );

    chartItem.setValues( 3.14f, 1.41f );

    verifyZeroInteractions( remoteObject );
  }

  @Test
  public void getColor_defaultsToBlack() {
    ChartItem chartItem = new ChartItem( chart );

    Color result = chartItem.getColor();

    assertEquals( new Color( display, 0, 0, 0 ), result );
  }

  @Test( expected = SWTException.class )
  public void getColor_checksWidget() {
    ChartItem chartItem = new ChartItem( chart );
    chartItem.dispose();

    chartItem.getColor();
  }

  @Test
  public void setColor_changesColor() {
    ChartItem chartItem = new ChartItem( chart );

    chartItem.setColor( new Color( display, 255, 128, 0 ) );

    assertEquals( new Color( display, 255, 128, 0 ), chartItem.getColor() );
  }

  @Test( expected = SWTException.class )
  public void setColor_checksWidget() {
    ChartItem chartItem = new ChartItem( chart );
    chartItem.dispose();

    chartItem.setColor( null );
  }

  @Test
  public void setColor_resetsColorWithNull() {
    ChartItem chartItem = new ChartItem( chart );
    chartItem.setColor( new Color( display, 255, 128, 0 ) );

    chartItem.setColor( null );

    assertEquals( new Color( display, 0, 0, 0 ), chartItem.getColor() );
  }

  @Test
  public void setColor_isRendered() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    fakeConnection( remoteObject );

    new ChartItem( chart ).setColor( new Color( display, 255, 128, 0 ) );

    JsonValue expected = JsonMapping.toJson( new Color( display, 255, 128, 0 ) );
    verify( remoteObject ).set( eq( "color" ), eq( expected ) );
  }

  @Test
  public void setColor_isNotRenderedIfUnchanged() {
    RemoteObject remoteObject = mock( RemoteObject.class );
    fakeConnection( remoteObject );
    ChartItem chartItem = new ChartItem( chart );
    chartItem.setColor( new Color( display, 255, 128, 0 ) );
    reset( remoteObject );

    chartItem.setColor( new Color( display, 255, 128, 0 ) );

    verifyZeroInteractions( remoteObject );
  }

  private Connection fakeConnection( RemoteObject remoteObject ) {
    Connection connection = mock( Connection.class );
    when( connection.createRemoteObject( anyString() ) ).thenReturn( remoteObject );
    context.replaceConnection( connection );
    return connection;
  }

}
