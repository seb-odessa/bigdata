package toolset.reducers;

import org.apache.avro.mapred.AvroKey;
import org.apache.avro.mapred.AvroValue;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;

/**
 * Created by seb on 26.04.16.
 */
public class AvroReducer
        extends Reducer<IntWritable, IntWritable, AvroKey<Integer>, AvroValue<String>> {

    private StringBuilder sb = new StringBuilder();

    public void reduce(IntWritable key, Iterable<IntWritable> records, Context context)
            throws IOException, InterruptedException {

        Integer id = key.get();
        for (IntWritable value: records) {
            if (sb.length() > 0)
                sb.append(" ");
            sb.append(value.toString());

        }
        context.write(new AvroKey<Integer>(id), new AvroValue<String>(sb.toString()));
        sb.setLength(0);
    }
}
