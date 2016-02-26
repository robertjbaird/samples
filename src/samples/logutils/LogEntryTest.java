package samples.logutils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

public class LogEntryTest {

  @Test
  public void testBasicLine() throws ParseException {
    
    final String line = new String("[2015-11-19 10:33:54.934+0000] [HOST1] [INFO] [CLASS1] [MESSAGE1 something]");
    final LogEntry entry = LogEntry.fromString(line);
    
    assertThat(entry.getSeverity(), equalTo(LogEntry.Severity.INFO));
    assertThat(entry.getTimestamp(), equalTo(new Date(1421663634934L)));
    assertThat(entry.getLogLine(), equalTo(line));
    
  }
  
  @Test
  public void testSorting() throws ParseException {
    
    final LogEntry entry1 = LogEntry.fromString("[2015-11-19 10:31:55.128+0000] [HOST2] [ERROR] [CLASS2] [MESSAGE2 random]");
    final LogEntry entry2 = LogEntry.fromString("[2015-11-19 10:31:55.128+0000] [HOST3] [INFO] [CLASS6] [MESSAGE5 from another host]");
    final LogEntry entry3 = LogEntry.fromString("[2015-11-19 10:33:54.934+0000] [HOST1] [INFO] [CLASS1] [MESSAGE1 something]");
    final LogEntry entry4 = LogEntry.fromString("[2015-11-19 10:35:55.267+0000] [HOST4] [INFO] [CLASS4] [MESSAGE4 too]");
    final LogEntry entry5 = LogEntry.fromString("[2015-11-19 10:37:55.246+0000] [HOST2] [WARN] [CLASS9] [MESSAGE9 again]");
    final LogEntry entry6 = LogEntry.fromString("[2015-11-19 10:37:55.246+0000] [HOST2] [INFO] [CLASS9] [MESSAGE9 again]");
    
    final LogEntry[] array1 = new LogEntry[]{entry1, entry2, entry3, entry4, entry5, entry6};
    Arrays.sort(array1);
    assertThat(array1, equalTo(new LogEntry[]{entry1, entry2, entry3, entry4, entry5, entry6}));
    
    final LogEntry[] array2 = new LogEntry[]{entry6, entry5, entry4, entry3, entry2, entry1};
    Arrays.sort(array2);
    assertThat(array2, equalTo(new LogEntry[]{entry1, entry2, entry3, entry4, entry5, entry6}));
    
    final LogEntry[] array3 = new LogEntry[]{entry2, entry1, entry4, entry6, entry3, entry5};
    Arrays.sort(array3);
    assertThat(array3, equalTo(new LogEntry[]{entry1, entry2, entry3, entry4, entry5, entry6}));
    
  }

}
