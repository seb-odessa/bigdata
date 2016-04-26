package toolset;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;

import org.apache.avro.Schema;
import org.apache.avro.mapreduce.AvroJob;
import org.apache.avro.mapreduce.AvroKeyValueOutputFormat;

import toolset.mappers.TextMapper;
import toolset.reducers.AvroReducer;

/**
 * Created by seb on 22.04.16.
 */
public class avro extends Configured implements Tool {
    public final int run(final String[] args) throws Exception {
        Configuration conf = super.getConf();
        Job job = Job.getInstance(conf);
        job.setJarByClass(avro.class);
        job.setJobName("Facebook Network to Avro Transformation");

        Path inPath = new Path(args[1]);
        Path outPath = new Path(args[2]);

        FileInputFormat.setInputPaths(job, inPath);
        FileOutputFormat.setOutputPath(job, outPath);

        job.setMapperClass(TextMapper.class);
        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setReducerClass(AvroReducer.class);
        job.setOutputFormatClass(AvroKeyValueOutputFormat.class);
        AvroJob.setOutputKeySchema(job, Schema.create(Schema.Type.INT));
        AvroJob.setOutputValueSchema(job, Schema.create(Schema.Type.INT));
        return job.waitForCompletion(true) ? 0 : 1;
    }
}
