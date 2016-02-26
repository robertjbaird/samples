package samples.logutils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A data structure representing the log entries. Responsible for parsing
 * the date and the severity level, and also implements the comparable
 * interface to aid with sorting the entries.   
 */
public class LogEntry implements Comparable<LogEntry> {
  
  private final String logLine_;
  private final Date timestamp_;
  private final Severity severity_;
  
  private final static Pattern LOG_REGEX = Pattern.compile("\\[(.*?)\\]");
  private final static DateFormat DATE_FORMAT = 
      new SimpleDateFormat("yyyy-mm-dd HH:mm:ss.SSSZ");
  
  /**
   * Parse a string and creates a new instance of the LogEntry representation. 
   * @param logLine the log entry to parse 
   * @return a new instance of the LogEntry 
   * @throws IllegalArgumentException on parsing errors 
   */
  public static LogEntry fromString(final String logLine) throws IllegalArgumentException {
    
    // Example syntax: 
    // [2015-11-19 10:33:54.934+0000] [HOST1] [INFO] [CLASS1] [MESSAGE1 something]
    
    try {
      
      // Use a regex matcher to extract [] groups 
      final Matcher matcher = LOG_REGEX.matcher(logLine);
      
      matcher.find();
      final String timeStampString = matcher.group(1);
      matcher.find(); // skip the [host] group 
      matcher.find();
      final String severityString = matcher.group(1);
      
      final Date date = DATE_FORMAT.parse(timeStampString);
      final Severity severity = Severity.valueOf(severityString);
      
      return new LogEntry(logLine, date, severity);
      
    } catch (Exception e) {
      
      // TODO: Better exception reporting
      throw new IllegalArgumentException("Unable to parse log line: " + logLine 
          + "\n Root cause was: " + e);
      
    }
    
  }
  
  /** protected constructor to enforce fromString(..) access pattern */
  protected LogEntry(final String logLine, 
                     final Date timestamp, 
                     final Severity severity) {
    logLine_ = logLine;
    timestamp_ = timestamp;
    severity_ = severity;
  }
  
  /**
   * @return the raw log line 
   */
  public String getLogLine() {
    
    return logLine_;
    
  }
  
  /**
   * @return the extract timestamp of the log entry as a Java Date
   */
  public Date getTimestamp() {
    
    return timestamp_;
    
  }
  
  /**
   * @return the extracted severity of the log entry  
   */
  public Severity getSeverity() {
    
    return severity_;
    
  }
  
  @Override
  public String toString() {
    return logLine_;
  }
  
  @Override
  public int compareTo(LogEntry o) {
    
    final int dateCompare = timestamp_.compareTo(o.timestamp_);
    
    if (dateCompare != 0) {
      
      return dateCompare;
      
    } else {
      
      return severity_.compareTo(o.severity_);
      
    }
    
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((logLine_ == null) ? 0 : logLine_.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    LogEntry other = (LogEntry) obj;
    if (logLine_ == null) {
      if (other.logLine_ != null)
        return false;
    } else if (!logLine_.equals(other.logLine_))
      return false;
    return true;
  }


  /** Log entry level severity */
  enum Severity {
    ERROR, 
    WARN, 
    INFO,
  } // rely on natural order enum compareTo provided by Java

  
}
