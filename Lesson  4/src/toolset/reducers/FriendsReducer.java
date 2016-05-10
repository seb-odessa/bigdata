package toolset.reducers;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;

/**
 * Created by seb on 26.04.16.
 */
public class FriendsReducer
        extends Reducer<IntWritable, IntWritable, IntWritable, Text> {
    private Text  friends = new Text();
    private StringBuilder sb = new StringBuilder();

    public void reduce(IntWritable key, Iterable<IntWritable> records, Context context)
            throws IOException, InterruptedException {

        for (IntWritable friend: records) {
            if (sb.length() > 0)
                sb.append(" ");
            sb.append(friend.get());
        }
        friends.set(sb.toString());
        context.write(key, friends);
        sb.setLength(0);
    }
}
