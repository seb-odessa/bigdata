import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.StringTokenizer;

public class facebook {

    public static class FacebookMapper extends Mapper<Object, Text, IntWritable, IntWritable>{
        private IntWritable id = new IntWritable();
        private IntWritable friend = new IntWritable();

        public void map(Object _, Text value, Context context) throws IOException, InterruptedException {
            StringTokenizer it = new StringTokenizer(value.toString());
            id.set(Integer.parseInt(it.nextToken()));
            friend.set(Integer.parseInt(it.nextToken()));
            context.write(id, friend);
        }
    }

    public static class FacebookReducer extends Reducer<IntWritable, IntWritable, IntWritable, Text> {
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

    private static void usage() {
        System.out.println("Usage: hadoop jar facebook.jar <input> <output>");
        System.out.println("\thadoop jar facebook.jar  /user/seb/facebook_combined.txt .");
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            usage();
            return;
        }

        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf, "Customer_Payments");
        job.setJarByClass(facebook.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setMapperClass(FacebookMapper.class);
        job.setReducerClass(FacebookReducer.class);

        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(Text.class);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
