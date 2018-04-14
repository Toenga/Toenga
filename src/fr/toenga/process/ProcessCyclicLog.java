package fr.toenga.process;

import java.io.IOException;
import java.io.OutputStream;

import lombok.Data;

@Data
public class ProcessCyclicLog
{
	
    public static final int MAX_LOG = 100000;
    private byte[] array;
    private boolean cycle;
    private int end;
    
    public ProcessCyclicLog()
    {
        this.cycle = false;
        this.end = 0;
        this.array = new byte[100000];
    }
    
    public void add(final int value)
    {
        this.array[this.end] = (byte)value;
        final int nend = (this.end + 1) % 100000;
        if (this.end > nend && !this.cycle)
        {
            this.cycle = true;
        }
        this.end = nend;
    }
    
    public void write(final OutputStream os) throws IOException
    {
        if (this.cycle)
        {
            os.write(this.array, this.end, 100000);
        }
        os.write(this.array, 0, this.end);
        os.flush();
    }
    
}
