package toolset;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Created by seb on 22.04.16.
 */
public class friends extends Configured implements Tool {

    public static class TextMapper
            extends Mapper<Object, Text, IntWritable, IntWritable> {
        private IntWritable id = new IntWritable();
        private IntWritable friend = new IntWritable();

        public void map(Object _, Text value, Context context)
                throws IOException, InterruptedException {
            StringTokenizer it = new StringTokenizer(value.toString());
            id.set(Integer.parseInt(it.nextToken()));
            friend.set(Integer.parseInt(it.nextToken()));
            context.write(id, friend);
        }
    }

    public static class FriendsReducer
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

    public final int run(final String[] args) throws Exception {
        Configuration conf = super.getConf();
        Job job = Job.getInstance(conf, "Facebook Friends collector");
        job.setJarByClass(friends.class);

        FileInputFormat.addInputPath(job, new Path(args[1]));
        FileOutputFormat.setOutputPath(job, new Path(args[2]));

        job.setMapperClass(TextMapper.class);
        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setReducerClass(FriendsReducer.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(Text.class);

        return job.waitForCompletion(true) ? 0 : 1;
    }
}
