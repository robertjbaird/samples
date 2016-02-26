package samples.logutils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.channels.Channels;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A utility class to write log entries to a collection of different files.
 * This writer will keep track of all files that are written to. When a file
 * hits a size limit the writer will close that file and create a new file
 * to write to. All files are Java "temporary" files and will have 
 * {@link File#deleteOnExit()} invoked. 
 * 
 * The LogEntries will be sorted by this writer prior to writing.
 */
public class RollingSortedLogWriter {

  private int currentCount_;
  private final LogEntry[] entries_; 
  private final Set<File> files_ = new HashSet<>();

  public RollingSortedLogWriter(final int maxLineCount) {
    // Note: maxLineCount is not an entirely accurate/best measurement.
    // A more optimal constructor argument would be a "desired file size"
    // but I have not authored that implementation yet. 
    entries_ = new LogEntry[maxLineCount];
    currentCount_ = 0;
  }
  
  /**
   * Add a new log entry. May cause the writer to create a new file if
   * the current file is over the allocated size limit. 
   * @param entry the log entry to add
   * @throws IOException on disk IO errors during writing
   */
  public void addLogEntry(final LogEntry entry) throws IOException {
    
    entries_[currentCount_] = entry;
    
    currentCount_++;
    
    if (currentCount_ == entries_.length) {
      rollFile();
    }
    
  }

  /**
   * Closes this writer. If there are any pending log entries that have
   * not been sent to disk this method will store those entires. 
   * @throws IOException
   */
  public void close() throws IOException {
    
    if (currentCount_ != 0) {
      rollFile();
    }
    
  }
  
  /**
   * @return all of the files that have been created by this writer 
   */
  public Set<File> getFiles() {
    
    return files_;
    
  }
  
  /**
   * Protected method to trigger the file to roll. Made protected to aid
   * in testing. May help to make public. 
   * 
   * This method will sort the pending LogEntries before saving to disk. 
   * 
   * @throws IOException on disk IO errors during writing
   */
  protected void rollFile() throws IOException {
    
    Arrays.sort(entries_, 0, currentCount_);
    flushToFile();
    currentCount_ = 0;
    
  }
  
  /**
   * Allocate a new file and write the string versions of the log entries
   * to the file. The new file will be marked as "temporary" by the JVM and
   * will be deleted when the JVM exits. 
   */
  private void flushToFile() throws IOException {
    
    final File tempFile = File.createTempFile("temp-sort-file-", ".txt");
    tempFile.deleteOnExit();
    files_.add(tempFile);
    
    final FileOutputStream fos = new FileOutputStream(tempFile);
    final Writer writer = Channels.newWriter(fos.getChannel(), "UTF-8");
    
    try {

      for (int i = 0; i < currentCount_; i++) {
        final LogEntry entry = entries_[i];
        writer.write(entry.getLogLine());
        
        if (i < currentCount_ - 1) {
          writer.write(System.lineSeparator());
        }
        
        writer.flush();
      }
      
    } finally {
      
      fos.close();
      
    }
    
  }

}
