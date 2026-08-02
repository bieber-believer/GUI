package Game;
import java.io.*;

public class SaveManager {
    private static final String SAVE_FILE = "save.txt";

    public static PrintWriter createWriter() throws IOException{
        return new PrintWriter(new FileWriter(SAVE_FILE));
    }

    public static BufferedReader createReader() throws IOException{
        return new BufferedReader(new FileReader(SAVE_FILE));
    }

    public static boolean saveExists(){
        File file = new File(SAVE_FILE);
        return file.exists();
    }
}
