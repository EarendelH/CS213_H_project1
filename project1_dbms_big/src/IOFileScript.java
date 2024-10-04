import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class IOFileScript extends DataScript {
    
    public String users_csv = "/Users/earendelh/Documents/CS213DB/project1/random_users_data.csv";
    public String users_json = "/Users/earendelh/Documents/CS213DB/project1/users.json";

    @Override
    public void users_reader() throws SQLException {
        try (
            BufferedReader reader = new BufferedReader(new FileReader(users_csv));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
            FileWriter fileWriter = new FileWriter(users_json)
        ) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            long start = System.currentTimeMillis();
            
            // Begin JSON array
            fileWriter.write("[\n");
            
            boolean firstRecord = true;
            
            // 逐行读取并转换为 JSON
            for (CSVRecord record : csvParser) {
                if (!firstRecord) {
                    fileWriter.write(",\n"); // 分隔每个 JSON 对象
                }
                Map<String, String> rowMap = new HashMap<>();
                record.toMap().forEach(rowMap::put);
                String jsonObject = gson.toJson(rowMap);
                fileWriter.write(jsonObject);
                firstRecord = false;
            }
            
            // End JSON array
            fileWriter.write("\n]\n");
            fileWriter.flush();

            long end = System.currentTimeMillis();
            System.out.println("CSV文件成功转换为JSON并存储到：" + users_json);
            System.out.println("数据存储耗时：" + (end - start) + " ms\n\n");
            
        } catch (Exception e) {
            System.err.println("File not found");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}