package samples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.channels.Channels;
import java.util.Set;

import samples.logutils.LogEntry;
import samples.logutils.RollingSortedLogWriter;
import samples.logutils.SortedLogReader;

public class SortLogLines {

  public static final int TEMP_FILE_LINE_COUNT = 1000000;
  
  /**
   * Sort a potentially large file of log entries. 
   * 
   * Note: this implementation uses a variety of helper classes and uses
   * an "external merge" sort approach to handle potentially large files.
   * 
   * The constant value {@link #TEMP_FILE_LINE_COUNT} should be adjusted
   * based on the expected size of the input file, and should only be 
   * considered a default. Alternatively, rather than counting lines a 
   * more proper sizing guide would be to measure the size of the external
   * sort files, however I have not done this yet. 
   * 
   * At a very high level, the following utilities are used:
   * <ul>
   * <li>{@link LogEntry} - parses log entries for sorting</li>
   * <li>{@link RollingSortedLogWriter} - writes external/smaller/sorted log files</li>
   * <li>{@link SortedLogReader} - a reader for reading the sorted log files in order</li>
   * </ul>
   * 
   * Enough memory must be allocated to the heap to ensure an array of 
   * {@link #TEMP_FILE_LINE_COUNT} size for storing {@link LogEntry} can be 
   * allocated. Approximately each entry requires a date object (32 bytes), 
   * an enum reference (32 bits), the string (variable, approx 176 bytes based
   * off of sample input), plus the object overhead (~16 byes). This is on
   * average 256 bytes. If {@link #TEMP_FILE_LINE_COUNT} is 1 million, that
   * is approximately 256mb of heap for the objects alone. 
   * 
   * Note that there is a tradeoff to having smaller incremental files in the
   * external sort algorithm. If the files are smaller, you will end up with
   * more files. During the load phase of the external sort you will have to 
   * keep an open reference to all files simultaneously. The associated cost
   * of the buffered input streams and file readers can become excessive, so
   * selecting an appropriate balance for  {@link #TEMP_FILE_LINE_COUNT} is
   * crucial. 
   * 
   * Also note: this approach is single threaded, but designed to work with
   * large data sets. Since a large number of log entries will be read into
   * the heap and then (relatively) immediately dereferenced, an excessive 
   * amount of time will be spent performing garbage collection. It may be 
   * beneficial to execute using parallel GC and parallel compaction. See
   * the guidlines at:  
   * http://www.oracle.com/technetwork/java/javase/gc-tuning-6-140523.html#available_collectors.selecting
   * 
   * @param inputFileName the filename of the input file
   * @param outputFileName the filename of the output file to write results to 
   * @throws IOException on file not found and other disk IO problems 
   */
  public static void sort(final String inputFileName, final String outputFileName) throws IOException {
    
    final File inputFile = new File(inputFileName);
    final File outputFile = new File(outputFileName);
    
    // Part 1: read the file into smaller/sorted files
    final Set<File> tempFiles = readIntoSortedTempFiles(inputFile);
    
    // Part 2: write one large sorted output 
    final SortedLogReader logReader = new SortedLogReader(tempFiles);
    
    final FileOutputStream fos = new FileOutputStream(outputFile);
    final Writer writer = Channels.newWriter(fos.getChannel(), "UTF-8");
    
    try {

      while (logReader.hasNext()) {
        
        final LogEntry entry = logReader.getNextEntry();
        writer.write(entry.getLogLine());
        writer.write(System.lineSeparator());
        
      }
      
      writer.flush();
      
    } finally {
      
      fos.close();
      
    }
    
    logReader.closeAll();
    
  }

  /**
   * Read the large input file into a number of smaller sorted files. 
   * 
   * @param inputFile the input file to read
   * @return a set of the sorted temporary files 
   * @throws FileNotFoundException
   * @throws IOException
   */
  private static Set<File> readIntoSortedTempFiles(final File inputFile) 
      throws FileNotFoundException, IOException {
    
    final RollingSortedLogWriter rslw = 
        new RollingSortedLogWriter(TEMP_FILE_LINE_COUNT);
    
    final BufferedReader reader = new BufferedReader(new FileReader(inputFile));
    
    try {
      
      for (String line; (line = reader.readLine()) != null;) {
        
        try {
          
          final LogEntry entry = LogEntry.fromString(line);
          rslw.addLogEntry(entry);
          
        } catch (IllegalArgumentException iae) {
          
          System.err.println("Skipping line due to parse error: " + line);
          iae.printStackTrace();
          
        }
        
      }
      
    } finally {
      reader.close();
    }

    rslw.close();
    
    return rslw.getFiles();
  }
  
}
