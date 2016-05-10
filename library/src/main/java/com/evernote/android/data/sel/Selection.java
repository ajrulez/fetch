/**
 * Copyright 2008-2016 Evernote Corporation. All rights reserved.
 */
package com.evernote.android.data.sel;

import java.util.Collection;

/**
 * Interface for building where clauses.
 */
public interface Selection {

    /**
     * Returns the where clause with placeholders.
     */
    String sql();

    /**
     * Returns the list of params to bind.
     * Its size should match the number of placeholders in the string returned by {@link #sql()}.
     */
    Collection<String> params();

}
