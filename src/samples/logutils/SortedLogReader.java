package samples.logutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 * A utility class to read {@link LogEntry} values from multiple files
 * simultaneously. This reader will read in order across all files. 
 */
public class SortedLogReader {

  private final TreeMap<LogEntry, BufferedReader> map_ = new TreeMap<>();
  
  public SortedLogReader(final Set<File> files) throws IOException {
    
    for (final File file : files) {
      
      final BufferedReader reader = new BufferedReader(new FileReader(file));
      final String line = reader.readLine();
      final LogEntry entry = LogEntry.fromString(line);
      map_.put(entry, reader);
      
    }
    
  }
  
  /**
   * @return true if there are any more log entries to read
   */
  public boolean hasNext() {
    return !map_.isEmpty(); 
  }
  
  /** 
   * Fetch the next entry from the log reader.
   */
  public LogEntry getNextEntry() throws IOException {
    
    final Entry<LogEntry, BufferedReader> mapEntry = map_.firstEntry();
    
    final LogEntry logEntry = mapEntry.getKey();
    final BufferedReader reader = mapEntry.getValue();
    
    map_.remove(logEntry);
    
    final String line = reader.readLine();
    
    if (line != null) {
      
      final LogEntry newEntry = LogEntry.fromString(line);
      map_.put(newEntry, reader);
      
    } else {
      reader.close();
    }
    
    return logEntry;
    
  }
  
  /**
   * Close any opened files and clears the local storage map. Invoke this
   * method on early termination. 
   * @throws IOException on failure during reader close 
   */
  public void closeAll() throws IOException {
    
    for (Entry<LogEntry, BufferedReader> entry : map_.entrySet()) {
      entry.getValue().close();
    }
    
    map_.clear();
    
  }
  
}
