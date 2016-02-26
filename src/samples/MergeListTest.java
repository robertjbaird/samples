package samples;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Ignore;
import org.junit.Test;

import samples.datatypes.ListNode;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

public class MergeListTest {

  @Test
  public void matcherTest() {
    
    // sanity check that asList and hasNumbers util methods works 
    final ListNode<Integer> list = asList(1, 2, 3);
    assertThat(list, hasNumbers(1, 2, 3));
    
    assertThat(list, not(hasNumbers(1)));
    assertThat(list, not(hasNumbers(1, 10, 3)));
    assertThat(list, not(hasNumbers(1, 2, 3, 4)));
    assertThat(list, not(hasNumbers(0, 1, 2, 3)));
    
  }
  
  @Ignore
  @Test
  public void testOrder() {
    // TODO: write a test to check that order is retained 
    fail("Not yet implemented.");
  }
  
  @Test
  public void testDisjoint() {
    
    final ListNode<Integer> list1 = asList(-10, -5, 0);
    final ListNode<Integer> list2 = asList(10, 20, 30);
    
    final ListNode<Integer> res = MergeList.mergeLists(list1, list2);
    
    assertThat(res, hasNumbers(-10, -5, 0, 10, 20, 30));
    
  }
  
  @Test
  public void testInterlaced() {
    
    final ListNode<Integer> list1 = asList(-10, 0, 5);
    final ListNode<Integer> list2 = asList(-7, 3, 30);
    
    final ListNode<Integer> res = MergeList.mergeLists(list1, list2);
    
    assertThat(res, hasNumbers(-10, -7, 0, 3, 5, 30));
    
  }
  
  @Test
  public void testListModification() {
   
    // test to make sure list1 and list2 are not modified
    final ListNode<Integer> list1 = asList(1, 2, 3);
    final ListNode<Integer> list2 = asList(-1, 2, 3, 4);
    
    @SuppressWarnings("unused")
    final ListNode<Integer> res = MergeList.mergeLists(list1, list2);
    
    assertThat(list1, hasNumbers(1, 2, 3));
    assertThat(list2, hasNumbers(-1, 2, 3, 4));
    
  }

  
  /**
   * A utility method to quickly create a list from an array of numbers
   * @param integers the integers to insert into the list 
   * @return the head of the list 
   */
  private ListNode<Integer> asList(Integer ...integers) {
    
    final ListNode<Integer> head = new ListNode<>(integers[0]);
    ListNode<Integer> cursor = head;
    
    for (int i = 1; i < integers.length; i++) {
      final ListNode<Integer> newNode = new ListNode<>(integers[i]);
      cursor.next_ = newNode;
      cursor = newNode;
    }
    
    return head; 
  }
  
  
  /**
   * Utility matcher to check that a list has a given order of numbers
   * @param numbers the numbers (in order) to check the list for 
   * @return a hamcrest matcher for validating the list numbers
   */
  private Matcher<ListNode<Integer>> hasNumbers(final Integer ...numbers) {
    return new BaseMatcher<ListNode<Integer>>() {
       @Override
       public boolean matches(final Object item) {

        @SuppressWarnings("unchecked")
        ListNode<Integer> node = (ListNode<Integer>)item;
         
         for (final Integer i : numbers) {
           if (node == null) 
             return false; // list too short 
           if (node.val_ != i)
             return false; // incorrect value in list 
           
           node = node.next_;
         }
         
         if (node != null)
           return false; // premature end
         
         return true; 
       }
       @Override
       public void describeTo(final Description description) {
          description.appendText("hasNumbers should match ")
            .appendValue(Arrays.asList(numbers));
       }
    };
 }
  
}
