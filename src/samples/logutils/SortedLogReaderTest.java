package samples.logutils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class SortedLogReaderTest {

  @Test
  public void testSortedRead() throws IOException {

    final RollingSortedLogWriter rslw = new RollingSortedLogWriter(4);
    
    final LogEntry entry1 = LogEntry.fromString("[2015-11-19 10:31:55.128+0000] [HOST2] [ERROR] [CLASS2] [MESSAGE2 random]");
    final LogEntry entry2 = LogEntry.fromString("[2015-11-19 10:31:55.128+0000] [HOST3] [INFO] [CLASS6] [MESSAGE5 from another host]");
    final LogEntry entry3 = LogEntry.fromString("[2015-11-19 10:33:54.934+0000] [HOST1] [INFO] [CLASS1] [MESSAGE1 something]");
    
    final LogEntry entry4 = LogEntry.fromString("[2015-11-19 10:35:55.267+0000] [HOST4] [INFO] [CLASS4] [MESSAGE4 too]");
    final LogEntry entry5 = LogEntry.fromString("[2015-11-19 10:37:55.246+0000] [HOST2] [WARN] [CLASS9] [MESSAGE9 again]");

    final LogEntry entry6 = LogEntry.fromString("[2015-11-19 10:37:55.246+0000] [HOST2] [INFO] [CLASS9] [MESSAGE9 again]");
    
    rslw.addLogEntry(entry1);
    rslw.addLogEntry(entry2);
    rslw.addLogEntry(entry5);
    rslw.rollFile();

    rslw.addLogEntry(entry3);
    rslw.addLogEntry(entry4);
    rslw.rollFile();
    
    rslw.addLogEntry(entry6);

    rslw.close();
    
    final Set<File> files = rslw.getFiles();
    
    final SortedLogReader reader = new SortedLogReader(files);
    
    final List<LogEntry> list = new ArrayList<>();
    while (reader.hasNext()) {
      list.add(reader.getNextEntry());
    }
    
    assertThat(list, equalTo(Arrays.asList(entry1, entry2, entry3, entry4, entry5, entry6)));
    
  }

}
