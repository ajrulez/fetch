/**
 * Copyright 2008-2016 Evernote Corporation. All rights reserved.
 */
package com.evernote.android.data;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author xlepaul
 * @since 2015-06-26
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public interface Converter<OutputT> {
  @Nullable
  OutputT convert(@NonNull Cursor input);

  Converter<String> STRING = new Converter<String>() {
    @Override
    public String convert(@NonNull Cursor cursor) {
      return cursor.getString(0);
    }
  };

  Converter<Long> LONG = new Converter<Long>() {
    @Override
    public Long convert(@NonNull Cursor cursor) {
      return cursor.getLong(0);
    }
  };

  Converter<Integer> INT = new Converter<Integer>() {
    @Override
    public Integer convert(@NonNull Cursor cursor) {
      return cursor.getInt(0);
    }
  };

  Converter<Boolean> BOOLEAN = new Converter<Boolean>() {
    @Override
    public Boolean convert(@NonNull Cursor cursor) {
      return cursor.getInt(0) > 0;
    }
  };

}
