package samples;

import java.util.Stack;

import samples.datatypes.ItemWithSize;

/**
 * An implementation of a bounded bag that supports concurrent access. 
 * This bag will return null if empty. An alternate implementation could
 * sleep/wait for items to be offered. 
 *
 * @param <T> the type to store in the bag
 */
public class BoundedConcurrentBag <T extends ItemWithSize> {

  // use variable size storage since items can be independently sized 
  private final Stack<T> storage_ = new Stack<>();
  private final long capacity_;
  private long currentSize_;
  
  /**
   * Construct a new bag with a given capacity 
   * @param capacity the capacity of the bag in bytes
   */
  public BoundedConcurrentBag(final long capacity) {
    
    if (capacity < 1)
      throw new IllegalArgumentException("Capacity must be positive.");
    
    capacity_ = capacity;
    currentSize_ = 0;
    
  }
  
  /**
   * Attempt to add a new item to the bag. An item will not
   * be added if adding it would exceed the current size capacity
   * of the bag. 
   * 
   * @param item the item to try to add
   * @return true if the item was added, false if it was not
   */
  public boolean offer(final T item) {
    
    if (item == null)
      throw new IllegalArgumentException("Item must be non-null");
    
    final long itemSize = item.getSizeByte();
    
    synchronized (storage_) {
      
      if ((currentSize_ + itemSize) > capacity_)
        return false;
      
      storage_.push(item);
      
      currentSize_ = currentSize_ + itemSize;
      
    }
    
    return true;
    
  }
  
  /**
   * Attempt to take an item from the bag. 
   * @return an item, or null if no items are in the bag 
   */
  public T take() {
    
    synchronized (storage_) {
    
      if (storage_.isEmpty()) {
        
        // unclear what to do here, could block/sleep
        return null; // return null for now
        
      } else {
      
        final T value = storage_.pop();
        long itemSize = value.getSizeByte();
        
        currentSize_ = currentSize_ - itemSize;
        
        return value;
      }
      
    }
    
  }
  
}
