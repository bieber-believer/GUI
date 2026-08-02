package Game;
import java.io.*;

public class SaveManager {
    private static final String FILE_NAME = "save.txt";

    public static PrintWriter getWriter() throws IOException{
        return new PrintWriter(new FileWriter(FILE_NAME));
    }

    public static BufferedReader getReader() throws IOException{
        return new BufferedReader(new FileReader(FILE_NAME));
    }

    public static boolean saveExists(){
        File file = new File(FILE_NAME);
        return file.exists();
    }
}
