import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class payments {

  public static class PaymentMapper
          extends Mapper<Object, Text, IntWritable, DoubleWritable>{
    private IntWritable id = new IntWritable();
    private DoubleWritable payment = new DoubleWritable();
    private Pattern pattern =
            Pattern.compile("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} (\\d+) ([\\d.]+)");

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
      Matcher matcher = pattern.matcher(value.toString());
      if (matcher.find()) {
        id.set(Integer.parseInt(matcher.group(1)));
        payment.set(Double.parseDouble(matcher.group(2)));
        context.write(id, payment);
      }
    }
  }

  public static class PaymentReducer
          extends Reducer<IntWritable, DoubleWritable, IntWritable, DoubleWritable> {
    private IntWritable id = new IntWritable();
    private DoubleWritable payment = new DoubleWritable();

    public void reduce(IntWritable key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
      double sum = 0;
      for (DoubleWritable payment: values) {
        sum += payment.get();
      }
      payment.set(sum);
      context.write(key, payment);
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

    Job job = Job.getInstance(conf, "Customer payments");
    job.setJarByClass(payments.class);

    job.setMapperClass(PaymentMapper.class);
    job.setCombinerClass(PaymentReducer.class);
    job.setReducerClass(PaymentReducer.class);

    job.setOutputKeyClass(IntWritable.class);
    job.setOutputValueClass(DoubleWritable.class);

    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
