import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;

public class facebook {

    private static void usage(final String[] args) {
        System.out.println("Usage:\n\thadoop jar facebook.jar <friends|json|avro> <input> <output>");
        System.out.println("\thadoop jar facebook.jar ");
        for(int i = 0; i < args.length; ++i)
            System.out.print(args[i] + " ");
        System.out.println();
    }

    public static void main(final String[] args) throws Exception {
        if (args.length < 3) {
            usage(args);
            return;
        }
        int res = -1;
        Configuration conf = new Configuration();
        switch (args[0]) {
            case "friends":
                res = ToolRunner.run(conf, new toolset.friends(), args);
                break;
            case "json":
                res = ToolRunner.run(conf, new toolset.json(), args);
                break;
            case "avro":
                res = ToolRunner.run(conf, new toolset.avro(), args);
                break;
            case "csv":
                res = ToolRunner.run(conf, new toolset.csv(), args);
                break;            default:
                usage(args);
        }

        System.exit(res);
    }

}
