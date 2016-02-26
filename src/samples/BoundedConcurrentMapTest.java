package samples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import samples.datatypes.ItemWithSize;

public class BoundedConcurrentMapTest {
  
  @Test(expected = IllegalArgumentException.class)
  public void testConstructor() {
    @SuppressWarnings("unused")
    final BoundedConcurrentBag<IntWithSize> bag = 
        new BoundedConcurrentBag<>(0);
  }
  
  @Test
  public void testCapacity() {
    
    final BoundedConcurrentBag<IntWithSize> bag = 
        new BoundedConcurrentBag<>(Integer.BYTES * 3);
    
    assertTrue(bag.offer(new IntWithSize(1, Integer.BYTES)));
    assertTrue(bag.offer(new IntWithSize(2, Integer.BYTES)));
    assertTrue(bag.offer(new IntWithSize(3, Integer.BYTES)));
    assertFalse(bag.offer(new IntWithSize(4, Integer.BYTES)));
    
  }
  
  @Test
  public void testTake() {
    
    final BoundedConcurrentBag<IntWithSize> bag = 
        new BoundedConcurrentBag<>(Integer.BYTES * 3);
    
    assertTrue(bag.offer(new IntWithSize(1, Integer.BYTES)));
    assertTrue(bag.offer(new IntWithSize(2, Integer.BYTES)));
    assertTrue(bag.offer(new IntWithSize(3, Integer.BYTES)));
    assertFalse(bag.offer(new IntWithSize(4, Integer.BYTES)));

    final Set<Integer> values = new HashSet<>();
    values.add(bag.take().getValue());
    values.add(bag.take().getValue());
    values.add(bag.take().getValue());

    assertEquals(values, new HashSet<>(Arrays.asList(1, 2, 3)));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testNullInsert() {
    
    final BoundedConcurrentBag<IntWithSize> bag = 
        new BoundedConcurrentBag<>(Integer.BYTES * 3);
    
    assertTrue(bag.offer(null));
    
  }

  @Ignore
  @Test
  public void testConcurrent() {
    fail("Test not implemented.");
  }
  
  
  private class IntWithSize implements ItemWithSize {

    private int value_;
    private int sizeInBytes_;

    public IntWithSize(final int value, final int sizeInBytes) {
      value_ = value;
      sizeInBytes_ = sizeInBytes;
    }
    
    @Override
    public long getSizeByte() {
      return sizeInBytes_;
    }
    
    public int getValue() {
      return value_;
    }
    
  }

}
