import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.IllegalArgumentException;

import Tools.Streams;

public class Copy {
    public static void main(String[] args) throws IOException {
	if (args.length != 2) {
	    System.out.println("Usage: java -jar CopyFile.jar <source> <destination>");
	    return;
	}
	Streams.copy(new FileInputStream(args[0]), new FileOutputStream(args[1]));
    }
}
