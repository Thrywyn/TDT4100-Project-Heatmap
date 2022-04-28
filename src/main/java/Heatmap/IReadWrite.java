package Heatmap;

import java.io.IOException;

public interface IReadWrite {

    public  void write(String fileName, Heatmap heatmap) throws IOException;

    public Heatmap read(String fileName) throws IOException;

}
