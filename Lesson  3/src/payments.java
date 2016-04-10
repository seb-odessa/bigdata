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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class payments {

    public static class PaymentMapper extends Mapper<Object, Text, IntWritable, RecordWritable>{
        private IntWritable id = new IntWritable();
        private RecordWritable record = new RecordWritable();
        private Pattern pattern =
                Pattern.compile("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} (\\d+) ([\\d.]+) ([\\w./]+)");

        public void map(Object _, Text value, Context context) throws IOException, InterruptedException {
            Matcher matcher = pattern.matcher(value.toString());
            if (matcher.find()) {
                id.set(Integer.parseInt(matcher.group(1)));
                record.set(Double.parseDouble(matcher.group(2)), matcher.group(3));
                context.write(id, record);
            }
        }
    }

    public static class PaymentReducer extends Reducer<IntWritable, RecordWritable, IntWritable, ReducedWritable> {
        private ReducedWritable record = new ReducedWritable();

        public void reduce(IntWritable key, Iterable<RecordWritable> records, Context context)
                throws IOException, InterruptedException {
            record.clear();
            for (RecordWritable value: records) {
                record.add(value.payment, value.store);
            }
            context.write(key, record);
        }
    }

    private static void usage() {
        System.out.println("Usage: hadoop jar payments.jar <input> <output>");
        System.out.println("\thadoop jar payments.jar  /user/seb/payments.log .");
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            usage();
            return;
        }

        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf, "Customer_Payments");
        job.setJarByClass(payments.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setMapperClass(PaymentMapper.class);
        job.setReducerClass(PaymentReducer.class);

        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(RecordWritable.class);

        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(ReducedWritable.class);

        job.setOutputFormatClass(JsonOutput.class);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
