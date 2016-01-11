package com.evernote.android.data.sel.test;

import com.evernote.android.data.sel.Selection;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Java6Assertions.assertThat;

public abstract class AbstractSelTest {

  protected final void assertSelection(Selection selection,
                                       String expectedSql,
                                       String... expectedParams) {
    assertSelection(selection, expectedSql, Arrays.asList(expectedParams));
  }

  protected final void assertSelection(Selection selection,
                                       String expectedSql,
                                       Collection<String> expectedParams) {
    String actualSql = selection.sql();
    final Collection<String> actualParams = selection.params();

    assertThat(actualSql).isEqualToIgnoringCase(expectedSql);
    assertThat(actualParams).containsExactlyElementsOf(expectedParams);

  }

}
