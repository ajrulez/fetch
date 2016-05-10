/**
 * Copyright 2008-2016 Evernote Corporation. All rights reserved.
 */
package com.evernote.android.data.sel;

import java.util.Collection;

final class Filter implements Selection {

    private final String column;
    private final Collection<String> values;

    public Filter(String column, Collection<String> values) {
        if (values == null || values.size() == 0) {
            throw new IllegalArgumentException("Cannot filter without values");
        }
        this.column = column;
        this.values = values;
    }

    @Override
    public String sql() {
        return column + binding();
    }

    private String binding() {
        final int size = values.size();
        if (size == 1) {
            return " = ?";
        }
        StringBuilder binding = new StringBuilder(5 + size * 2);
        binding.append(" IN (?");
        for (int i = 1; i < size; ++i) {
            binding.append(",?");
        }
        return binding.append(")").toString();
    }

    @Override
    public Collection<String> params() {
        return values;
    }
}
