import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.IllegalArgumentException;

import tools.streams;

public class hdfs {
  public static void main(String[] args) throws IOException {
	  if (args.length == 0) {
       System.out.println("Usage: java -jar hdfs.jar <cmd> [<arg0>, ..., <argN> ]");
       return;
	  }
    switch (args[0]) {
      case "cp" :
        streams.cp(streams.istream(args[1]), streams.ostream(args[2]));
    }

  }
}
