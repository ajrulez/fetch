/**
 * Copyright (c) 2015 Evernote Corporation. All rights reserved.
 */
package com.evernote.android.data;

import android.database.Cursor;

/**
 * @author xlepaul
 * @since 2015-06-26
 */
public interface Transformation<Output> {
    Output apply(Cursor input);
}
