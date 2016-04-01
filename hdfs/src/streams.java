package tools;

import java.io.File;
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
  
  private static boolean isLocal(String filename) {
    File local = new File(filename);
    return (local.exists() && ! local.isDirectory());
  }

  private static InputStream istream(String filename, FileSystem fs)
    throws IllegalArgumentException, IOException {
	if (isLocal(filename)) {
	    return new FileInputStream(filename);
	}
	Path hdfs = new Path(filename);
        return fs.open(hdfs);
  }

  private static OutputStream ostream(String filename)
    throws IllegalArgumentException, IOException {
    AssertNotNull(filename);
    return new FileOutputStream(filename);
  }

  public static void cp(String src_uri, String dst_uri)
    throws IllegalArgumentException, IOException {
        AssertNotNull(src_uri);
	AssertNotNull(dst_uri);
    	Configuration conf = new Configuration();
	FileSystem fs = FileSystem.get(conf);
	InputStream istream = istream(src_uri, fs);
	AssertNotNull(istream);
	OutputStream ostream = ostream(dst_uri);
	AssertNotNull(ostream);

	IOUtils.copyBytes(istream, ostream, conf, true);
    }
}
