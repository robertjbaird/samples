package samples.datatypes;

public class ListNode<T> {

  public ListNode<T> next_;
  public T val_;
  
  public ListNode(final T val) {
    val_ = val; 
  }
  
  @Override
  public String toString() {

    final StringBuilder sb = new StringBuilder();
    ListNode<T> curr = this;
    
    while (curr != null) {
      if (curr.val_ != null) {
        sb.append(curr.val_);
      } else {
        sb.append("null");
      }
      
      if (curr.next_ != null) {
        sb.append(" -> ");
      }
      
      curr = curr.next_;
    }

    
    return sb.toString();
  }
  
}
