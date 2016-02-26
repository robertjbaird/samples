package samples;

import samples.datatypes.ListNode;

public class MergeList {

  /**
   * Merge two lists of integers together. The lists are sorted in increasing
   * order.  
   * 
   * Note: because the items in the second list can appear before any items
   * in the first list, this method returns a new head of a new list. Also,
   * this implementation does not modify either of the old lists. 
   * 
   * @param list1 the first list 
   * @param list2 the second list 
   * @return a new head for the merged list
   */
  public static ListNode<Integer> mergeLists(ListNode<Integer> list1, 
                                             ListNode<Integer> list2) {
    // create a dummy head for referencing the start of the new list 
    final ListNode<Integer> dummyHead = new ListNode<Integer>(null);
    ListNode<Integer> node = dummyHead; 
    
    while (list1 != null && list2 != null) {
      
      final ListNode<Integer> nextNode;
      
      if (list1.val_ < list2.val_) {
        nextNode = new ListNode<>(list1.val_);
        list1 = list1.next_; // advance cursor, do not modify existing list
      } else {
        nextNode = new ListNode<>(list2.val_);
        list2 = list2.next_; // advance cursor, do not modify existing list
      }
      
      node.next_ = nextNode;
      node = node.next_;
      
    }
    
    // append any remaining items from either list 
    while (list1 != null) {
      node.next_ = new ListNode<>(list1.val_);
      node = node.next_;
      list1 = list1.next_;
    }
    while (list2 != null) {
      node.next_ = new ListNode<>(list2.val_);
      node = node.next_;
      list2 = list2.next_;
    }
    
    // return the true head of the new list 
    return dummyHead.next_; 
    
  }

}
