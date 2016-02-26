package samples.logutils;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;

import org.junit.Test;

public class RollingSortedLogWriterTest {

  @Test
  public void testFiles() throws IOException {
    
    final RollingSortedLogWriter rslw = new RollingSortedLogWriter(1);
    rslw.addLogEntry(LogEntry.fromString("[2015-11-19 10:31:55.128+0000] [HOST2] [ERROR] [CLASS2] [MESSAGE2 random]"));
    rslw.addLogEntry(LogEntry.fromString("[2015-11-19 10:31:55.128+0000] [HOST2] [ERROR] [CLASS2] [MESSAGE2 random]"));
    rslw.addLogEntry(LogEntry.fromString("[2015-11-19 10:31:55.128+0000] [HOST2] [ERROR] [CLASS2] [MESSAGE2 random]"));
    
    rslw.close();
    assertThat(rslw.getFiles().size(), equalTo(3));
    
  }
  
  @Test
  public void testPartial() throws IOException {
    
    final RollingSortedLogWriter rslw = new RollingSortedLogWriter(3);
    
    rslw.addLogEntry(LogEntry.fromString("[2015-11-19 10:31:55.128+0000] [HOST2] [ERROR] [CLASS2] [MESSAGE2 random]"));
    rslw.addLogEntry(LogEntry.fromString("[2015-11-19 10:31:55.128+0000] [HOST2] [ERROR] [CLASS2] [MESSAGE2 random]"));
    rslw.addLogEntry(LogEntry.fromString("[2015-11-19 10:31:55.128+0000] [HOST2] [ERROR] [CLASS2] [MESSAGE2 random]"));
    
    rslw.addLogEntry(LogEntry.fromString("[2015-11-19 10:31:55.128+0000] [HOST2] [ERROR] [CLASS2] [MESSAGE2 random]"));
    rslw.addLogEntry(LogEntry.fromString("[2015-11-19 10:31:55.128+0000] [HOST2] [ERROR] [CLASS2] [MESSAGE2 random]"));
    
    rslw.close();
    assertThat(rslw.getFiles().size(), equalTo(2));
    
  }
  
  @Test 
  public void testRoll() throws IOException {
    
    final RollingSortedLogWriter rslw = new RollingSortedLogWriter(3);
    
    rslw.addLogEntry(LogEntry.fromString("[2015-11-19 10:31:55.128+0000] [HOST2] [ERROR] [CLASS2] [MESSAGE2 random]"));
    rslw.rollFile();
    
    rslw.addLogEntry(LogEntry.fromString("[2015-11-19 10:31:55.128+0000] [HOST2] [ERROR] [CLASS2] [MESSAGE2 random]"));
    rslw.rollFile();
    
    rslw.addLogEntry(LogEntry.fromString("[2015-11-19 10:31:55.128+0000] [HOST2] [ERROR] [CLASS2] [MESSAGE2 random]"));
    rslw.rollFile();
    
    rslw.addLogEntry(LogEntry.fromString("[2015-11-19 10:31:55.128+0000] [HOST2] [ERROR] [CLASS2] [MESSAGE2 random]"));
    rslw.addLogEntry(LogEntry.fromString("[2015-11-19 10:31:55.128+0000] [HOST2] [ERROR] [CLASS2] [MESSAGE2 random]"));
    rslw.close();
    
    assertThat(rslw.getFiles().size(), equalTo(4));
    
  }

}
