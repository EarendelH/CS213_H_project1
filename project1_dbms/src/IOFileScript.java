import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class IOFileScript extends DataScript{
    
    public String videos_csv="/Users/earendelh/Documents/CS213DB/project1/B站每周.csv";
    public String videos_json="/Users/earendelh/Documents/CS213DB/project1/bilibili.json";
    @Override
    public void videos_reader() throws SQLException{
    try(
            BufferedReader reader = new BufferedReader(new FileReader(videos_csv));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
        ){
            long start = System.currentTimeMillis();
            List<Map<String, String>> dataList = new ArrayList<>();
            for (CSVRecord record : csvParser) {
                Map<String, String> rowMap = new HashMap<>();
                record.toMap().forEach(rowMap::put);
                dataList.add(rowMap);
            }
            csvParser.close();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter fileWriter = new FileWriter(videos_json);
            gson.toJson(dataList, fileWriter);
            fileWriter.flush();
            fileWriter.close();
            System.out.println("CSV文件成功转换为JSON并存储到：" + videos_json);
            long end = System.currentTimeMillis();
            System.out.println("数据存储耗时：" + (end - start) + " ms\n\n");
        }catch(Exception e){
        System.err.println("File not found");
        System.err.println(e.getMessage());
        System.exit(1);
    }
    }

}
