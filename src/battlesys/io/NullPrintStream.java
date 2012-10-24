package battlesys.io;

import java.io.OutputStream;


/**
 * Null print stream that discards all outputs
 * @author Peter
 */
public class NullPrintStream extends OutputStream{

    /**
     *
     */
    public NullPrintStream(){}

    public void write(int b){}

    @Override
    public void write(byte[] b){}

    @Override
    public void write(byte[] b, int off, int len){}

}
