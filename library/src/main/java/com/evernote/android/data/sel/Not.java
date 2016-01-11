package com.evernote.android.data.sel;

import java.util.Collection;

final class Not implements Selection {

  private final Selection base;

  public Not(Selection base) {
    this.base = base;
  }

  @Override
  public String sql() {
    return "NOT (" + base.sql() + ")";
  }

  @Override
  public Collection<String> params() {
    return base.params();
  }

}
