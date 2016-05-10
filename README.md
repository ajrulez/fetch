# Fetch

A library to eliminate boilerplate when querying `ContentResolver` and
`SQLiteDatabase`.


## Usage

Add the following dependency to your Gradle config:

```groovy
dependencies {
    compile 'com.evernote:android-fetch:0.3.0'
}
```

In its simplest form, the library enables this:

```java
Cursor cursor;
List<String> result = new ArrayList<>();
try {
  cursor = contentResolver.query(sourceUri, null, null, null, null);
  while (cursor != null && cursor.moveToNext()) {
    result.add(cursor.getString(0));
  }
} finally {
  if (cursor != null) {
    cursor.close();
  }
}

```

To become this:

```java
List<String> result = QueryBuilder.cr(sourceUri)
  .fetch(contentResolver).toList(Converter.STRING);
```

Every parameter of `query` can be specified by calling additional optional
methods, and you can use a custom `Converter` to build your own objects:

```java
List<Thing> things = QueryBuilder.db("my_table")
  .projection("id", "name", "description")
  .selection("size = ?")
  .selectionArgs("42")
  .sortOrder("name")
  .fetch(database)
  .toList(new Converter<Thing>() {
    @Override
    public Thing convert(Cursor c) {
      int id = c.getInt(0);
      String name = c.getString(1);
      String description = c.getString(2);
      return new Thing(id, name, description);
    }
  });
```

There are also convenience methods for filters on list of values:


```java
QueryBuilder.cr(sourceUri).select("name", nameList);
```

is equivalent to:

```java
contentResolver.query(sourceUri, null,
  "name in (?,?,?)",
  nameList.toArray(new String[nameList.size()]),
  null);
```

You can also create `Observable`s to do the work in a background thread:

```java
QueryBuilder.cr(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        .projection(MediaStore.Images.Media.DATA)
        .selection(selection)
        .fetchStream(mContext, Converter.STRING)
        .map(...)
        .filter(...)
        .observeOn(...);
```

License
-------

    Copyright 2008-2016 Evernote Corporation. All rights reserved.

    Use of the source code and binary libraries included in this package
    is permitted under the following terms:

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions
    are met:

        1. Redistributions of source code must retain the above copyright
        notice, this list of conditions and the following disclaimer.
        2. Redistributions in binary form must reproduce the above copyright
        notice, this list of conditions and the following disclaimer in the
        documentation and/or other materials provided with the distribution.

    THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
    IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
    OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
    IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
    INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
    NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
    DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
    THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
    (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
    THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.