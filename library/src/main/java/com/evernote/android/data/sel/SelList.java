package com.evernote.android.data.sel;

import java.util.ArrayList;
import java.util.Collection;

final class SelList implements Selection {

    private final String glue;
    private final Selection[] selections;

    public SelList(String glue, Selection[] selections) {
        if (selections == null || selections.length == 0) {
            throw new IllegalArgumentException("Cannot build an empty list");
        }
        this.glue = glue;
        this.selections = selections;
    }

    @Override
    public String sql() {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Selection selection : selections) {
            if (first) {
                first = false;
            } else {
                result.append(glue);
            }
            result.append('(').append(selection.sql()).append(')');
        }
        return result.toString();
    }

    @Override
    public Collection<String> params() {
        java.util.List<String> params = new ArrayList<>();
        for (Selection selection : selections) {
            params.addAll(selection.params());
        }
        return params;
    }

}
