import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class CSVToSQLScript {

    public static void main(String[] args) {
        String csvFilePath = "F:\\Lectures\\Database(H)\\Project1_Java\\B站每周.csv"; // CSV文件路径
        String sqlScriptPath = "F:\\Lectures\\Database(H)\\Project1_Java\\sqlscript.sql"; // SQL脚本文件路径

        try (CSVParser parser = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .parse(new FileReader(csvFilePath));
             Writer writer = new FileWriter(sqlScriptPath)) {

            String command="INSERT INTO videos (term, duration, title, part,description, url, last_time, author, mid, aid, coins, danmus, favorites, likes, comments, shares, plays, date) VALUES ";
//            writer.write("(");

            for (CSVRecord record : parser) {
                int term = Integer.parseInt(record.get("期数"));
                String duration = record.get("期数描述");
                String title = record.get("标题");
                title=escapeSQL(title);
                String part = record.get("视频类型");
                String description = record.get("视频标签");
                description=escapeSQL(description);
                String url = record.get("视频链接");
                int lastTime = Integer.parseInt(record.get("视频时长"));
                String author = record.get("up主");
                BigDecimal mid = new BigDecimal(record.get("up主_id"));
                BigDecimal aid = new BigDecimal(record.get("aid"));
                BigDecimal coins = new BigDecimal(record.get("投币数"));
                BigDecimal danmus = new BigDecimal(record.get("弹幕数"));
                BigDecimal favorites = new BigDecimal(record.get("收藏数"));
                BigDecimal likes = new BigDecimal(record.get("点赞数"));
                BigDecimal comments = new BigDecimal(record.get("评论数"));
                BigDecimal shares = new BigDecimal(record.get("分享数"));
                BigDecimal plays = new BigDecimal(record.get("播放数"));
                String dateStr = record.get("发布时间");
                if(dateStr.lastIndexOf('.')>0) {
                    dateStr = dateStr.substring(0, dateStr.lastIndexOf('.'));

                }
//                System.out.println(dateStr);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date date = new Timestamp(dateFormat.parse(dateStr).getTime());

                String insert=command+"(" +
                        term + ", '" +
                        duration + "', '" +
                        title + "', '" +
                        part + "', '" +
                        description + "', '" +
                        url + "', " +
                        lastTime + ", '" +
                        author + "', " +
                        mid + ", " +
                        aid + ", " +
                        coins + ", " +
                        danmus + ", " +
                        favorites + ", " +
                        likes + ", " +
                        comments + ", " +
                        shares + ", " +
                        plays + ", '" +
                        date + "');\n";
//                if (parser.getRecordNumber() < parser.getRecords().size()) {
////                    writer.write(",\n");
//                }
                System.out.println(insert);
                writer.write(insert);
            }
            System.out.println("Total "+parser.getRecordNumber());


        } catch (IOException | NumberFormatException | java.text.ParseException e) {
            e.printStackTrace();
        }
    }
    public static String escapeSQL(String value) {
        if (value == null) {
            return null;
        }
        return value.replace("'", "''").replace("\0", "\\0");
    }
}