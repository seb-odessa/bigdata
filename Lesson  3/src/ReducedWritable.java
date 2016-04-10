/**
 * Created by seb on 09.04.16.
 */

import org.apache.hadoop.io.Writable;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class ReducedWritable implements Writable  {
    public Double total;
    public Set<String> stores = new TreeSet();

    public void add(double payment, String store) {
        total += payment;
        stores.add(store);
    }

    public void clear() {
        total = 0.0;
        stores.clear();
    }

    public void write(DataOutput out) throws IOException {
        out.writeDouble(total);
        out.writeInt(stores.size());;
        Iterator i = stores.iterator();
        while(i.hasNext()) out.writeUTF(i.next().toString());
    }

    public void readFields(DataInput in) throws IOException {
        total = in.readDouble();
        int records = in.readInt();
        while (records --> 0) stores.add(in.readUTF());
    }
}

