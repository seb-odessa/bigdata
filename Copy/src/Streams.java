package Tools;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.IllegalArgumentException;

public class Streams {
    public static void copy(FileInputStream in, FileOutputStream out) throws IllegalArgumentException, IOException {
	if (in == null) {
    	    throw new IllegalArgumentException("in stream is null.");
	}
	if (out == null) {
    	    throw new IllegalArgumentException("out stream is null.");
	}
	try {
    	    int c;
    	    while ((c = in.read()) != -1) {
        	out.write(c);
    	    }
    	}
    	finally {
    	    if (in != null) {
    		in.close();
    	    }
    	    if (out != null) {
    		out.close();
    	    }
    	}
    }
}
