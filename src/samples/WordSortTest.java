package samples;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;


public class WordSortTest {

    @Test
    public void questionPrompt() {
      assertThat(WordSort.sort("premature optimization is the root of all evil"), 
          equalTo("all evil is of optimization premature root the"));
    }
    
    @Test 
    public void repeatWords() {
      assertThat(WordSort.sort("word is is is"),  equalTo("is is is word"));
      assertThat(WordSort.sort("abc is abc is"),  equalTo("abc abc is is"));
    }
    
    @Test(expected = IllegalArgumentException.class) 
    public void testNull() {
      WordSort.sort(null);
    }
    
    @Test
    public void testEmptyString() {
      assertThat(WordSort.sort(""),  equalTo(""));
    }
    
    @Test
    public void testSingleWord() {
      assertThat(WordSort.sort("word"),  equalTo("word"));
    }

}
