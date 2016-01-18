/**
 * Copyright (c) 2015 Evernote Corporation. All rights reserved.
 */
package com.evernote.android.data;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Utility to fetch all rows from a cursor and convert them to an object.
 * <p>
 * Only one operation is allowed, as the cursor will be closed at the end of any {@code queryXxx} operation.
 * <p>
 * This class is meant to be used in conjunction with {@link QueryBuilder}:
 * <p>
 * {@code List<String> res = QueryBuilder.db().[...].fetch(db).toList(Fetcher.STRING);}
 *
 * @author xlepaul
 * @since 2015-07-16
 */
public class Fetcher {

  private final Cursor cursor;
  private boolean skipNulls = false;

  public Fetcher(Cursor cursor) {
    this.cursor = cursor;
  }

  /**
   * Don't return rows for which the provided function returns {@code null}. Only apply to methods
   * returning a collection.
   */
  public Fetcher skipNulls() {
    this.skipNulls = true;
    return this;
  }

  /** Transform a single row of results (even if the cursor has more than 1 row). */
  public <ResultT> Optional<ResultT> toValue(Converter<ResultT> converter) {
    try {
      if (cursor != null && cursor.moveToFirst()) {
        return Optional.ofNullable(converter.convert(cursor));
      }
      return Optional.empty();
    } finally {
      close();
    }
  }

  /** Transforms all rows from the cursor and put them in a read-only list. */
  public <ResultT> List<ResultT> toList(Converter<ResultT> converter) {
    try {
      if (isEmpty(cursor)) { return Collections.emptyList(); }
      return populate(converter, new ArrayList<ResultT>(cursor.getCount()));
    } finally {
      close();
    }
  }

  /** Transforms all rows from the cursor and put them in a read-only set. */
  public <ResultT> Set<ResultT> toSet(Converter<ResultT> converter) {
    try {
      if (isEmpty(cursor)) { return Collections.emptySet(); }
      return populate(converter, new HashSet<ResultT>(cursor.getCount()));
    } finally {
      close();
    }
  }

  /** Transforms all rows from the cursor and put them in the provided collection. */
  public <ResultT, CollT extends Collection<ResultT>>
  CollT toCollection(Converter<ResultT> converter, CollT results) {
    try {
      if (isEmpty(cursor)) { return results; }
      return populate(converter, results);
    } finally {
      close();
    }
  }

  private static boolean isEmpty(Cursor cursor) {
    return cursor == null || cursor.getCount() == 0;
  }

  private <ResultT, CollT extends Collection<ResultT>>
  CollT populate(Converter<ResultT> converter, CollT results) {
    while (this.cursor.moveToNext()) {
      ResultT result = converter.convert(this.cursor);
      if (result != null || !skipNulls) {
        results.add(result);
      }
    }
    return results;
  }

  private void close() {
    if (cursor != null) { cursor.close(); }
  }

}

