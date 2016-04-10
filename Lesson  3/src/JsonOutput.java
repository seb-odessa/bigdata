/**
 * Created by seb on 09.04.16.
 */
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;


public class JsonOutput extends TextOutputFormat<IntWritable, ReducedWritable> {

    public RecordWriter<IntWritable, ReducedWritable> getRecordWriter(TaskAttemptContext context) throws IOException, InterruptedException {
        Configuration conf = context.getConfiguration();
        Path path = getOutputPath(context);
        FileSystem fs = path.getFileSystem(conf);
        FSDataOutputStream out = fs.create(new Path(path,context.getJobName()));
        return new JsonRecordWriter(out);
    }

    private static class JsonRecordWriter extends  LineRecordWriter<IntWritable, ReducedWritable> {
        public synchronized void write(IntWritable key, ReducedWritable value) throws IOException {
            //{"id":1,"total":106.72,"stores":["www.store1.com","www.store3.com"] }

            out.writeChars("{ ");
            out.writeChars("\"id\":" + key.toString() + ", ");
            out.writeChars("\"total\":" + String.format(Locale.US, "%.2f", value.total) + ", ");
            out.writeChars("\"stores\":[");
            boolean first = true;
            Iterator i = value.stores.iterator();
            while (i.hasNext()) {
                if (first) first = false; else out.writeChars(", ");
                out.writeChars("\"" + i.next() + "\"");
            }
            out.writeChars("]");
            out.writeChars(" }\r\n");
        }

        public JsonRecordWriter(DataOutputStream out) throws IOException{
            super(out);
        }
    }
}