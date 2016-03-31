package Streams;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.IllegalArgumentException;

public class CopyStreams {
    public static void copy(FileInputStream in, FileOutputStream out) throws IllegalArgumentException, IOException {
	if (in == null) {
    	    throw new IllegalArgumentException("in stream is null.");
	}
	if (out == null) {
    	    throw new IllegalArgumentException("out stream is null.");
	}
        int c;
        while ((c = in.read()) != -1) {
            out.write(c);
        }
    }
}
