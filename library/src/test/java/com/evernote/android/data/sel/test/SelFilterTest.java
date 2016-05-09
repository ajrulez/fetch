package com.evernote.android.data.sel.test;

import com.evernote.android.data.sel.Sel;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class SelFilterTest extends AbstractSelTest {

  @Test(expected = IllegalArgumentException.class)
  public void filterWithNoValue() {
    Sel.filter("column");
  }

  @Test(expected = IllegalArgumentException.class)
  public void filterWithNullList() {
    Sel.filter("column", (Collection<String>) null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void filterWithEmptyList() {
    Sel.filter("column", Collections.<String>emptyList());
  }

  @Test
  public void filterSingleValue() {
    assertSelection(Sel.filter("column", "value"), "column = ?", "value");
    assertSelection(Sel.filter("column", new String[]{"value"}), "column = ?", "value");
    assertSelection(Sel.filter("column", Collections.singleton("value")), "column = ?", "value");
  }

  @Test
  public void filterSeveralValues() {
    assertSelection(Sel.filter("col", "a", "b", "c"),
        "col in (?,?,?)", "a", "b", "c");
    assertSelection(Sel.filter("col", Arrays.asList("a", "b", "c")),
        "col in (?,?,?)", "a", "b", "c");

    String binding = Sel.filter("col", Collections.nCopies(8000, "plop")).sql();
    assertThat(binding).containsPattern("\\?(,\\?){7999}");
  }


}
