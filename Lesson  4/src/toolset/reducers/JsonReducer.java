package toolset.reducers;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.json.simple.JSONObject;

import java.io.IOException;

/**
 * Created by seb on 26.04.16.
 */
public class JsonReducer
        extends Reducer<IntWritable, IntWritable, NullWritable, Text> {

    private JSONObject js = new JSONObject();
    public void reduce(IntWritable key, Iterable<IntWritable> records, Context context)
            throws IOException, InterruptedException {

        js.put("id", key.get());
        for (IntWritable friend: records) {
            js.put("friend", friend.get());
            context.write(NullWritable.get(), new Text(js.toJSONString()));
        }
    }
}
