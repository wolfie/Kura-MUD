package com.github.wolfie.kuramud;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

public class Util {
  public static String outputWordWrap(final String message,
      final int columnWidth) {
    final List<String> stringRows = new ArrayList<String>();
    for (final String originalLine : Arrays.asList(message.split("\n"))) {
      StringBuilder lineBuilder = new StringBuilder();
      final Iterable<String> words = Splitter.on(Pattern.compile("\\s")).split(
          originalLine);
      for (final String word : words) {
        if (lineBuilder.length() > columnWidth) {
          stringRows.add(lineBuilder.toString());
          lineBuilder = new StringBuilder();
        }
        lineBuilder.append(word + " ");
      }

      if (lineBuilder.length() > 0) {
        stringRows.add(lineBuilder.toString());
      }
    }

    return Joiner.on('\n').join(stringRows);
  }

  public static boolean is(final String command, final String... matches) {
    if (matches == null || matches.length == 0) {
      return false;
    } else {
      for (final String match : matches) {
        if (match.equalsIgnoreCase(command)) {
          return true;
        }
      }
      return false;
    }
  }
}
