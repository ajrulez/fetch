/**
 * Copyright (c) 2015 Evernote Corporation. All rights reserved.
 */
package com.evernote.android.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Collection;
import java.util.Collections;

/**
 * @author xlepaul
 * @since 2015-06-29
 */
public abstract class QueryBuilder<Source, Queryable,
        Self extends QueryBuilder<Source, Queryable, Self>> {

    /** @return a QueryBuilder for {@link ContentResolver} queries */
    public static CR cr() { return new CR(); }

    public static CR cr(Uri uri) { return new CR().source(uri); }

    /** @return a QueryBuilder for {@link SQLiteDatabase} queries */
    public static DB db() { return new DB(); }

    public static DB db(String table) { return new DB().source(table); }

    protected Source source;
    protected String[] projection;
    protected String selection;
    protected String[] selectionArgs;
    protected String sortOrder;

    protected QueryBuilder() {}

    protected abstract Self self();

    public Self source(Source source) {
        checkNull(this.source, "source");
        this.source = source;
        return self();
    }

    public Self projection(String... projection) {
        checkNull(this.projection, "projection");
        this.projection = projection;
        return self();
    }

    public Self selection(String selection) {
        checkNull(this.selection, "selection");
        this.selection = selection;
        return self();
    }

    public Self selectionArgs(String... selectionArgs) {
        checkNull(this.selectionArgs, "selectionArgs");
        this.selectionArgs = selectionArgs;
        return self();
    }

    public Self selectionArgs(Collection<String> selectionArgs) {
        return selectionArgs(selectionArgs.toArray(new String[selectionArgs.size()]));
    }

    /** @throws IllegalArgumentException if there are no values. */
    public Self select(String columnName, String... values) {
        return selection(columnName + paramStringForList(values.length))
                .selectionArgs(values);
    }

    /** @throws IllegalArgumentException if the list is empty. */
    public Self select(String columnName, Collection<String> values) {
        return selection(columnName + paramStringForList(values.size()))
                .selectionArgs(values);
    }

    /**
     * Build the part of the where clause for querying from a list of elements, depending on the size
     * of the parameters.
     *
     * @throws IllegalArgumentException if {@code listSize == 0}
     */
    private String paramStringForList(int listSize) {
        if (listSize == 0) { throw new IllegalArgumentException("Cannot query for 0 elements"); }
        if (listSize == 1) { return " = ?"; }
        return " IN (" + TextUtils.join(",", Collections.nCopies(listSize, "?")) + ")";
    }

    public Self sortOrder(String sortOrder) {
        checkNull(this.sortOrder, "sortOrder");
        this.sortOrder = sortOrder;
        return self();
    }

    protected final void checkNull(Object field, String name) {
        if (field != null) {
            throw new IllegalStateException(name + " has already been set");
        }
    }

    public abstract Cursor query(Queryable queryable);

    public final Fetcher fetch(Queryable queryable) {
        return new Fetcher(query(queryable));
    }

    public static class CR extends QueryBuilder<Uri, ContentResolver, CR> {
        @Override
        protected CR self() {
            return this;
        }

        @Override
        public Cursor query(ContentResolver cr) {
            return cr.query(source, projection, selection, selectionArgs, sortOrder);
        }

        public Cursor query(Context context) {
            return query(context.getContentResolver());
        }

        public Fetcher fetch(Context context) {
            return fetch(context.getContentResolver());
        }

    }

    public static class DB extends QueryBuilder<String, SQLiteDatabase, DB> {

        private boolean distinct;
        private String groupBy;
        private String having;
        private String limit;

        @Override
        protected DB self() {
            return this;
        }

        public DB distinctValues() {
            this.distinct = true;
            return this;
        }

        public DB groupBy(String groupBy) {
            checkNull(this.groupBy, "groupBy");
            this.groupBy = groupBy;
            return this;
        }

        public DB having(String having) {
            checkNull(this.having, "having");
            this.having = having;
            return this;
        }

        public DB limit(String limit) {
            checkNull(this.limit, "limit");
            this.limit = limit;
            return this;
        }

        @Override
        public Cursor query(SQLiteDatabase db) {
            return db.query(distinct, source, projection, selection, selectionArgs,
                    groupBy, having, sortOrder, limit);
        }
    }

}
