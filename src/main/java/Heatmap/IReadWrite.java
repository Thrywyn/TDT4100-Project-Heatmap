package Heatmap;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public interface IReadWrite {

    public  void write(String fileName, Heatmap heatmap) throws IOException;

    public Heatmap read(String fileName) throws IOException;

}
