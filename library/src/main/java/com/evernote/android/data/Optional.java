/**
 * Copyright (c) 2015 Evernote Corporation. All rights reserved.
 */
package com.evernote.android.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * (partial) backport of Java 8 {@code java.util.Optional}
 *
 * @author xlepaul
 * @since 2015-04-28
 */
public abstract class Optional<T> {

  // INTERFACE //

  public abstract boolean isPresent();

  @NonNull
  public abstract T get();

  @NonNull
  public abstract T orElse(@NonNull T defaultValue);

  @Nullable
  public abstract T orNull();

  // FACTORY METHODS //

  @SuppressWarnings("unchecked")
  public static <T> Optional<T> empty() {
    return (Optional<T>) Absent.INSTANCE;
  }

  public static <T> Optional<T> of(T value) {
    return new Present<>(value);
  }

  public static <T> Optional<T> ofNullable(T value) {
    return value == null ? Optional.<T>empty() : Optional.of(value);
  }

  // IMPLEMENTATIONS //

  private static class Absent<T> extends Optional<T> {

    private static final Absent<?> INSTANCE = new Absent<>();

    @Override
    public boolean isPresent() {
      return false;
    }

    @NonNull
    @Override
    public T get() {
      throw new IllegalStateException("get() cannot be call on an absent value");
    }

    @NonNull
    @Override
    public T orElse(@NonNull T defaultValue) {
      return checkNotNull(defaultValue, "default value");
    }

    @Nullable
    @Override
    public T orNull() {
      return null;
    }
  }

  private static class Present<T> extends Optional<T> {

    private final T value;

    private Present(T value) {
      this.value = checkNotNull(value, "value");
    }


    @Override
    public boolean isPresent() {
      return true;
    }

    @NonNull
    @Override
    public T get() {
      return value;
    }

    @NonNull
    @Override
    public T orElse(@NonNull T defaultValue) {
      checkNotNull(defaultValue, "default value"); // for consistency
      return value;
    }

    @Nullable
    @Override
    public T orNull() {
      return value;
    }
  }

  private static <T> T checkNotNull(T value, String description) {
    if (value == null) {
      throw new NullPointerException(description + " should not be null");
    }
    return value;
  }

}
