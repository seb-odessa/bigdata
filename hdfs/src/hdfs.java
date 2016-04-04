import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.IllegalArgumentException;

import hdfs.command;

public class hdfs {

  private static void usage() {
    System.out.println("Usage: hadoop jar hdfs.jar <cmd> [<arguments>");
    System.out.println("\thadoop jar hdfs.jar put source_file .");
    System.out.println("\thadoop jar hdfs.jar put source_file destination_file");
    System.out.println("\thadoop jar hdfs.jar put source_file /user/coudera/");
    System.out.println("\thadoop jar hdfs.jar put source_file /user/coudera/destination_file");
    System.out.println("\thadoop jar hdfs.jar put source_file hdfs://quickstart.cloudera:8020/user/cloudera/");
    System.out.println("\thadoop jar hdfs.jar put source_file hdfs://quickstart.cloudera:8020/user/cloudera/destination_file");

    System.out.println("\n");
    System.out.println("\thadoop jar hdfs.jar get hdfs://localhost/users/coudera/source_file .");
    System.out.println("\thadoop jar hdfs.jar get hdfs://localhost/users/coudera/source_file destination_file");
  }


  public static void main(String[] args) throws Exception {
    if (args.length == 3) {
      switch (args[0]) {
        case "put" :
        command.put(args[1], args[2]);
        return;

        case "get" :
        command.get(args[1], args[2]);
        return;
      }
    }
    usage();
  }
}
