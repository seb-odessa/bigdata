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

    public static void put(String ifile, String ofile) throws IllegalArgumentException, IOException {
	AssertNotNull(ifile);
	AssertNotNull(ofile);
	Configuration conf = new Configuration();
	FileSystem.get(conf).copyFromLocalFile(new Path(ifile), new Path(ofile));
    }

}
