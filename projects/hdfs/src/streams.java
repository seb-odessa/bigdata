package tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.IllegalArgumentException;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class streams {

  private static void AssertNotNull(Object arg) {
    if (arg == null) {
      throw new IllegalArgumentException("argument is null.");
    }
  }

  public static InputStream istream(String filename)
    throws IllegalArgumentException, IOException {
    AssertNotNull(filename);
    return new FileInputStream(filename);
  }

  public static OutputStream ostream(String filename)
    throws IllegalArgumentException, IOException {
    AssertNotNull(filename);
    return new FileOutputStream(filename);
  }

  public static void cp(InputStream istream, OutputStream ostream)
    throws IllegalArgumentException, IOException {
    AssertNotNull(istream);
    AssertNotNull(ostream);

    Configuration conf = new Configuration();
    IOUtils.copyBytes(istream, ostream, conf, true);
    //
    // int c;
    // while ((c = istream.read()) != -1) {
    //   ostream.write(c);
    // }

    istream.close();
    ostream.close();
  }
}
