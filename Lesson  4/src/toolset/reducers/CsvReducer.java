package toolset.reducers;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;

/**
 * Created by seb on 11.05.16.
 */
public class CsvReducer
        extends Reducer<IntWritable, IntWritable, NullWritable, Text> {

    private Text output = new Text();
    private StringBuilder sb = new StringBuilder();

    public void reduce(IntWritable key, Iterable<IntWritable> records, Context context)
            throws IOException, InterruptedException {
        String aKey = key.toString();
        for (IntWritable value: records) {
            sb.append(aKey);
            sb.append(",");
            sb.append(value.toString());

            output.set(sb.toString());
            context.write(NullWritable.get(), output);
            sb.setLength(0);
        }
    }
}
