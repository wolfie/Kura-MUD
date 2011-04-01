package com.github.wolfie.kuramud;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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

  /**
   * This method converts all arguments of a command into a string.
   * <p/>
   * The first element in <code>argv</code> is ignored, others are concatenated
   * with a space character in between.
   * 
   * @param argv
   *          The argv argument, usually from <code>Console</code>
   * @return A concatenated String of <code>argv</code>
   */
  public static final String argvToString(final String[] argv) {
    final StringBuilder arguments = new StringBuilder();
    if (argv != null && argv.length > 0) {
      for (int i = 1; i < argv.length; i++) {
        arguments.append(argv[i]);
        arguments.append(" ");
      }
    }
    return arguments.toString();
  }

  public static String join(final Iterable<?> set, final String joiner) {
    final StringBuilder sb = new StringBuilder();
    final Iterator<?> i = set.iterator();
    while (i.hasNext()) {
      sb.append(i.next());
      if (i.hasNext()) {
        sb.append(joiner);
      }
    }
    return sb.toString();
  }

}
