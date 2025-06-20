import java.io.*;
import java.util.*;

public class FocusHistoryManager {
    private static final String FILE_NAME = "focus_history.txt";

    public static void save(int minutes) {
        try (FileWriter fw = new FileWriter(FILE_NAME, true)) {
            fw.write(minutes + "\n"); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Integer> load() {
        List<Integer> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();  // 공백 제거
                if (line.isEmpty()) continue; //  빈 줄은 무시
                try {
                    data.add(Integer.parseInt(line));
                } catch (NumberFormatException e) {
                    System.out.println("무시된 잘못된 기록: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }    
}
