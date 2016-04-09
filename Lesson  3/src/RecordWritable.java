/**
 * Created by seb on 09.04.16.
 */

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class RecordWritable implements Writable  {
    public double payment;
    public String store;

    public void set(double payment, String store) {
        this.payment = payment;
        this.store = store;
    }

    public void write(DataOutput out) throws IOException {
        out.writeDouble(payment);
        out.writeUTF(store);
    }

    public void readFields(DataInput in) throws IOException {
        payment = in.readDouble();
        store = in.readUTF();
    }
}

