package com.evernote.android.data.sel;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/** Factory class to build complex selections */
public class Sel {

  private Sel() {}

  public static Selection filter(String column, String value) {
    return filter(column, Collections.singleton(value));
  }

  /** Filter on a list of values. {@code column in (?, ?, ?)} */
  public static Selection filter(String column, String... values) {
    return filter(column, Arrays.asList(values));
  }

  /** Filter on a list of values. {@code column in (?, ?, ?)} */
  public static Selection filter(String column, Collection<String> values) {
    return new Filter(column, values);
  }

  public static Selection and(Selection... selections) {
    return new SelList(" AND ", selections);
  }

  public static Selection or(Selection... selections) {
    return new SelList(" OR ", selections);
  }

  public static Selection not(Selection selection) {
    return new Not(selection);
  }

}
