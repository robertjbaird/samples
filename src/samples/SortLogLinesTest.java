package samples;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.junit.Test;

public class SortLogLinesTest {

  @Test
  public void testFullAlgorithm() throws IOException {
    
    final File inputFile = File.createTempFile("test-input-", ".txt");
    final File outputFile = File.createTempFile("test-output-", ".txt");
    inputFile.deleteOnExit();
    outputFile.deleteOnExit();
    
    writeStringToFile(SAMPLE_INPUT, inputFile);
    
    SortLogLines.sort(inputFile.getAbsolutePath(), outputFile.getAbsolutePath());
    
    final String actualOutput = readStringFromFile(outputFile);
    
    assertThat(actualOutput, equalTo(EXPECTED_OUTPUT));
  }

  private void writeStringToFile(final String string, final File file) throws IOException {
    final BufferedWriter writer = new BufferedWriter(new FileWriter(file));
    try {
      writer.write(string);
    } finally {
      writer.close();
    }
  }
  
  private String readStringFromFile(final File file) throws IOException {
    final StringBuilder builder = new StringBuilder();
    final Scanner scanner = new Scanner(file);
    try {
      while (scanner.hasNextLine()) {
        builder.append(scanner.nextLine());
        if (scanner.hasNextLine()) 
          builder.append(System.lineSeparator());
      }
    } finally {
      scanner.close();
    }
    return builder.toString();
  }
  
  private static final String SAMPLE_INPUT = 
        "[2015-11-19 10:33:54.934+0000] [HOST1] [INFO] [CLASS1] [MESSAGE1 something]" + System.lineSeparator()
      + "[2015-11-19 10:31:55.128+0000] [HOST2] [ERROR] [CLASS2] [MESSAGE2 random]" + System.lineSeparator()
      + "[2015-11-19 10:31:55.128+0000] [HOST3] [INFO] [CLASS6] [MESSAGE5 from another host]" + System.lineSeparator()
      + "[2015-11-19 10:37:55.246+0000] [HOST2] [WARN] [CLASS9] [MESSAGE9 again]" + System.lineSeparator()
      + "[2015-11-19 10:35:55.267+0000] [HOST4] [INFO] [CLASS4] [MESSAGE4 too]" + System.lineSeparator()
      + "[2015-11-19 10:22:55.307+0000] [HOST1] [ERROR] [CLASS5] [some error happened]" + System.lineSeparator()
      + "[2015-11-19 10:18:55.377+0000] [HOST5] [INFO] [CLASS1] [MESSAGE1 status message]" + System.lineSeparator()
      + "[2015-11-19 10:31:55.128+0000] [HOST7] [WARN] [CLASS3] [MESSAGE7 something fishy]" + System.lineSeparator()
      + "[2015-11-19 10:43:55.667+0000] [HOST9] [INFO] [CLASS7] [MESSAGE8 normal message]" + System.lineSeparator()
      + "[2015-11-19 10:32:55.782+0000] [HOST1] [ERROR] [CLASS1] [MESSAGE3 another error]";
  
  private static final String EXPECTED_OUTPUT = 
        "[2015-11-19 10:18:55.377+0000] [HOST5] [INFO] [CLASS1] [MESSAGE1 status message]" + System.lineSeparator()
      + "[2015-11-19 10:22:55.307+0000] [HOST1] [ERROR] [CLASS5] [some error happened]" + System.lineSeparator()
      + "[2015-11-19 10:31:55.128+0000] [HOST2] [ERROR] [CLASS2] [MESSAGE2 random]" + System.lineSeparator()
      + "[2015-11-19 10:31:55.128+0000] [HOST7] [WARN] [CLASS3] [MESSAGE7 something fishy]" + System.lineSeparator()
      + "[2015-11-19 10:31:55.128+0000] [HOST3] [INFO] [CLASS6] [MESSAGE5 from another host]" + System.lineSeparator()
      + "[2015-11-19 10:32:55.782+0000] [HOST1] [ERROR] [CLASS1] [MESSAGE3 another error]" + System.lineSeparator()
      + "[2015-11-19 10:33:54.934+0000] [HOST1] [INFO] [CLASS1] [MESSAGE1 something]" + System.lineSeparator()
      + "[2015-11-19 10:35:55.267+0000] [HOST4] [INFO] [CLASS4] [MESSAGE4 too]" + System.lineSeparator()
      + "[2015-11-19 10:37:55.246+0000] [HOST2] [WARN] [CLASS9] [MESSAGE9 again]" + System.lineSeparator()
      + "[2015-11-19 10:43:55.667+0000] [HOST9] [INFO] [CLASS7] [MESSAGE8 normal message]"
      ;
  
  
}
