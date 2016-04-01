package hdfs;

import java.io.IOException;
import java.lang.IllegalArgumentException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class command {
  private static void AssertNotNull(Object arg) {
    if (arg == null) {
      throw new IllegalArgumentException("argument is null.");
    }
  }

  public static void put(String ifile, String ofile)
  throws IllegalArgumentException,  IOException {
    Configuration conf = new Configuration();
    Path ipath = new Path(ifile);
    Path opath = new Path(ofile);
    opath.getFileSystem(conf).copyFromLocalFile(ipath, opath);
  }

  public static void get(String ifile, String ofile)
  throws IllegalArgumentException,  IOException {
    Configuration conf = new Configuration();
    Path ipath = new Path(ifile);
    Path opath = new Path(ofile);
    ipath.getFileSystem(conf).copyToLocalFile(ipath, opath);
  }

}
