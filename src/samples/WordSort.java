package samples;

import java.util.Arrays;

public class WordSort {

  /**
   * Sorts the words in a string in alphabetic order.
   * 
   * Note: if parsing on different delimiters is desired adding
   * the delimiter argument to the public interface is easy to do.
   *  
   * @param input the string to sort 
   * @return a new sorted string 
   */
  public static String sort(final String input) {
    return sortUsingSplit(input, " ");
  }
  
  private static String sortUsingSplit(final String input, final String delimiter) {
    
    if (input == null)
      throw new IllegalArgumentException("input cannot be null");
    
    final String[] words = input.split(delimiter);
    
    Arrays.sort(words);
    
    final StringBuilder builder = new StringBuilder();
    
    for (int i = 0; i < words.length; i++) {
      builder.append(words[i]);
      if (i != words.length - 1) {
        builder.append(" ");
      }
    }
    
    return builder.toString();
    
  }
  


}
