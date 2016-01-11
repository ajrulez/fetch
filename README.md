# Fetch

A library to eliminate boilerplate when querying `ContentResolver` and
`SQLiteDatabase`.


## Usage

In its simplest form, this:

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

Becomes this:

```java
List<String> result = QueryBuilder.cr(sourceUri)
  .fetch(contentResolver).toList(Fetcher.STRING);
```

Every parameter of `query` can be specified by calling additional optional
methods, and you can use a custom transformation to build your own objects:

```java
List<Thing> things = QueryBuilder.db("my_table")
  .projection("id", "name", "description")
  .selection("size = ?")
  .selectionArgs("42")
  .sortOrder("name")
  .fetch(database)
  .toList(new Transformation<Thing>() {
    @Override
    public Thing apply(Cursor c) {
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