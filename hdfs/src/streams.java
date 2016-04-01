package tools;

//import java.io.File;
import java.io.IOException;
import java.io.InputStream;
//import java.io.OutputStream;
import java.io.FileInputStream;
//import java.io.FileOutputStream;

import java.lang.IllegalArgumentException;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
//import org.apache.hadoop.util.Tool;
//import org.apache.hadoop.util.ToolRunner;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;

public class streams {

  private static void AssertNotNull(Object arg) {
    if (arg == null) {
      throw new IllegalArgumentException("argument is null.");
    }
  }



/*
  private static InputStream istream(String filename, FileSystem fs) throws IllegalArgumentException, IOException {
        File local = new File(filename);
	if (local.exists() && ! local.isDirectory()) {
	    return new FileInputStream(filename);
	}
        return fs.open(new Path(filename));
  }

  private static OutputStream ostream(String filename, FileSystem fs) throws IllegalArgumentException, IOException {
        File local = new File(filename);
	try {
	
	}

	if (!local.exists() && local.isDirectory()) {
	    return new FileInputStream(filename);
	}

    AssertNotNull(filename);
    return new FileOutputStream(filename);
  }
*/

    public static void put(String ifile, String ofile) throws IllegalArgumentException, IOException {
	AssertNotNull(ifile);
	AssertNotNull(ofile);
	Configuration conf = new Configuration();
	FileSystem.get(conf).copyFromLocalFile(new Path(ifile), new Path(ofile));
/*	
	InputStream is = new FileInputStream(ifile);
	FSDataOutputStream os = FileSystem.get(conf).create(new Path(ofile));
	IOUtils.copyBytes(is, os, conf, true);
*/
    }
}
