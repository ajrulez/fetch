/**
 * Copyright 2008-2016 Evernote Corporation. All rights reserved.
 */
package com.evernote.android.data.sel.test;

import com.evernote.android.data.sel.Sel;
import com.evernote.android.data.sel.Selection;

import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

public class SelLogicTest extends AbstractSelTest {

  @Test
  public void testNegation() {
    assertSelection(Sel.not(constant(0)), "not (0)");
  }

  @Test
  public void testConjunction() {
    assertSelection(Sel.and(constant(0), constant(1)),
        "(0) and (1)");
  }

  @Test
  public void testDisjunction() {
    assertSelection(Sel.or(constant(0), constant(1)),
        "(0) or (1)");
  }

  @Test
  public void testParameterForwarding() {
    assertSelection(Sel.not(Sel.filter("col", "a", "b")),
        "not (col in (?,?))", "a", "b");

    assertSelection(Sel.and(constant(42), Sel.filter("col", "1", "2")),
        "(42) and (col in (?,?))", "1", "2");

    assertSelection(Sel.or(Sel.filter("colA", "a"), Sel.filter("colB", "b")),
        "(colA = ?) or (colB = ?)", "a", "b");
  }


  private Selection constant(final long value) {
    return new Selection() {
      @Override
      public String sql() {
        return String.valueOf(value);
      }

      @Override
      public Collection<String> params() {
        return Collections.emptyList();
      }
    };
  }


}
