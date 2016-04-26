package toolset.mappers;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Created by seb on 26.04.16.
 */
public class TextMapper
        extends Mapper<Object, Text, IntWritable, IntWritable> {
    private IntWritable id = new IntWritable();
    private IntWritable friend = new IntWritable();

    @SuppressWarnings("unused")
    public void map(Object key, Text value, Context context)
            throws IOException, InterruptedException {
        StringTokenizer it = new StringTokenizer(value.toString());
        id.set(Integer.parseInt(it.nextToken()));
        friend.set(Integer.parseInt(it.nextToken()));
        context.write(id, friend);
    }
}
