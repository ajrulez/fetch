/**
 * Copyright (c) 2015 Evernote Corporation. All rights reserved.
 */
package com.evernote.android.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.evernote.android.data.sel.Sel;
import com.evernote.android.data.sel.Selection;

import java.util.Collection;

import rx.Observable;
import rx.Subscriber;

/**
 * @author xlepaul
 * @since 2015-06-29
 */
public abstract class QueryBuilder<Source, Queryable, Self extends QueryBuilder<Source, Queryable, Self>> {

    /**
     * @return a QueryBuilder for {@link ContentResolver} queries
     */
    public static CR cr() {
        return new CR();
    }

    public static CR cr(Uri uri) {
        return new CR().source(uri);
    }

    /**
     * @return a QueryBuilder for {@link SQLiteDatabase} queries
     */
    public static DB db() {
        return new DB();
    }

    public static DB db(String table) {
        return new DB().source(table);
    }

    protected Source source;
    protected String[] projection;
    protected String selection;
    protected String[] selectionArgs;
    protected String sortOrder;

    protected QueryBuilder() {
    }

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

    public Self select(String columnName, String value) {
        return select(Sel.filter(columnName, value));
    }

    /**
     * @throws IllegalArgumentException if there are no values.
     */
    public Self select(String columnName, String... values) {
        return select(Sel.filter(columnName, values));
    }

    /**
     * @throws IllegalArgumentException if the list is empty.
     */
    public Self select(String columnName, Collection<String> values) {
        return select(Sel.filter(columnName, values));
    }

    public Self select(Selection selection) {
        return selection(selection.sql()).selectionArgs(selection.params());
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

    public final Observable<Cursor> queryStream(final Queryable queryable) {
        return Observable.create(new Observable.OnSubscribe<Cursor>() {
            @Override
            public void call(Subscriber<? super Cursor> subscriber) {
                subscriber.onNext(query(queryable));
                subscriber.onCompleted();
            }
        });
    }

    public final Fetcher fetch(Queryable queryable) {
        return Fetcher.of(query(queryable));
    }

    public final <ResultT> Observable<ResultT> fetchStream(final Queryable queryable, final Converter<ResultT> converter) {
        return Observable.create(new Observable.OnSubscribe<ResultT>() {
            @Override
            public void call(Subscriber<? super ResultT> subscriber) {
                fetch(queryable).subscribe(converter, subscriber);
            }
        });
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

        public Observable<Cursor> queryStream(Context context) {
            return queryStream(context.getContentResolver());
        }

        public Fetcher fetch(Context context) {
            return fetch(context.getContentResolver());
        }

        public <ResultT> Observable<ResultT> fetchStream(Context context, Converter<ResultT> converter) {
            return fetchStream(context.getContentResolver(), converter);
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
