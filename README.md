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