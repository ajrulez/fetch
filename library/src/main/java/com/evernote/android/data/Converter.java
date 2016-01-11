/**
 * Copyright (c) 2015 Evernote Corporation. All rights reserved.
 */
package com.evernote.android.data;

import android.database.Cursor;

/**
 * @author xlepaul
 * @since 2015-06-26
 */
public interface Converter<OutputT> {
    OutputT convert(Cursor input);

    Converter<String> STRING = new Converter<String>() {
        @Override
        public String convert(Cursor cursor) {
            return cursor.getString(0);
        }
    };

    Converter<Long> LONG = new Converter<Long>() {
        @Override
        public Long convert(Cursor cursor) {
            return cursor.getLong(0);
        }
    };

    Converter<Integer> INT = new Converter<Integer>() {
        @Override
        public Integer convert(Cursor cursor) {
            return cursor.getInt(0);
        }
    };

    Converter<Boolean> BOOLEAN = new Converter<Boolean>() {
        @Override
        public Boolean convert(Cursor cursor) {
            return cursor.getInt(0) > 0;
        }
    };

}
