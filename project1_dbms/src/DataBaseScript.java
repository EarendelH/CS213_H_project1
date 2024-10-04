import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;


public class DataBaseScript extends DataScript{
        private Connection conn=null;
        private String host = "localhost";
        private String dbname = "project1";
        private String user = "earendelh";
        private String pwd="wzhwzh041229";
        private String port="5432";
        @Override
        public void getConnection() {
            try{
                Class.forName("org.postgresql.Driver");
            }catch (Exception e){
                System.err.println(dbname+" not found");
                System.exit(1);
            }
            try{
                String url="jdbc:postgresql://"+host+":"+port+"/"+dbname;
                conn=DriverManager.getConnection(url, user, pwd);
                System.out.println("Connection successful");
            }catch (Exception e){
                System.err.println("Connection error");
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }
        @Override
        public Connection getConn() {
            return conn;
        }
        @Override
        public void closeConnection() {
            try{
                conn.close();
                System.out.println("Connection closed");
            }catch (Exception e){
                System.err.println("Connection close error");
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }
        
        public void createtable() {
            try{
                PreparedStatement stat=conn.prepareStatement("DROP TABLE IF EXISTS videos;");
                stat.execute();
                stat=conn.prepareStatement("VACUUM;");
                stat.execute();
                stat=conn.prepareStatement("\\timing;");
                long start = System.currentTimeMillis();
                String sql =
                "CREATE TABLE videos(" +
                "term INT NOT NULL, " +
                "duration VARCHAR NOT NULL, " +
                "title VARCHAR(100) NOT NULL, " +
                "part VARCHAR(100) NOT NULL, " +
                "description VARCHAR(10000) , " +
                "url VARCHAR(100) NOT NULL, " +
                "last_time NUMERIC NOT NULL, " +
                "author VARCHAR(100) NOT NULL, " +
                "mid NUMERIC NOT NULL, " +
                "aid NUMERIC NOT NULL, " +
                "coins NUMERIC NOT NULL, " +
                "danmus NUMERIC NOT NULL, " +
                "favorites NUMERIC NOT NULL, " +
                "likes NUMERIC NOT NULL, " +
                "comments NUMERIC NOT NULL, " +
                "shares NUMERIC NOT NULL, " +
                "plays NUMERIC NOT NULL, " +
                "date TIMESTAMP(0) NOT NULL, " +
                "PRIMARY KEY (term, aid)" +
                ");";
                stat=conn.prepareStatement(sql);
                stat.execute();
                System.out.println("Table videos created");
                long end = System.currentTimeMillis();
                System.out.println("创建表耗时：" + (end - start) + " ms\n\n");
            }catch (Exception e){
                System.err.println("Table create error");
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }
        @Override
        public void videos_reader() throws SQLException{
            conn.setAutoCommit(false); // 禁止自动提交，以便于回滚和提高效率
            final int BATCH_SIZE = 1000; // 每次批量插入的数据量
        
            String videos_file = "/Users/earendelh/Documents/CS213DB/project1/B站每周.csv";
            try(
                BufferedReader reader = new BufferedReader(new FileReader(videos_file));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
            ){
                String sql_video = "INSERT INTO videos(term, duration, title, part,description, url, last_time, author, mid, aid, coins, danmus, favorites, likes, comments, shares, plays, date) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                PreparedStatement stat_video = conn.prepareStatement(sql_video);
                long start = System.currentTimeMillis();
                Map <String, Integer> heads = csvParser.getHeaderMap();
                System.out.println("Headers: "  + heads);
                int count = 0;
                System.out.println("开始导入video数据, Batch size: " + BATCH_SIZE);
                for(CSVRecord record: csvParser){
                    stat_video.setInt(1, Integer.parseInt(record.get("期数")));
                    stat_video.setString(2, record.get("期数描述"));
                    stat_video.setString(3, record.get("标题"));
                    stat_video.setString(4, record.get("视频类型"));
                    stat_video.setString(5, record.get("视频标签"));
                    stat_video.setString(6, record.get("视频链接"));
                    stat_video.setBigDecimal(7, new BigDecimal(record.get("视频时长")));
                    stat_video.setString(8, record.get("up主"));
                    stat_video.setBigDecimal(9, new BigDecimal(record.get("up主_id")));
                    stat_video.setBigDecimal(10, new BigDecimal(record.get("aid")));
                    stat_video.setBigDecimal(11, new BigDecimal(record.get("投币数")));
                    stat_video.setBigDecimal(12, new BigDecimal(record.get("弹幕数")));
                    stat_video.setBigDecimal(13, new BigDecimal(record.get("收藏数")));
                    stat_video.setBigDecimal(14, new BigDecimal(record.get("点赞数")));
                    stat_video.setBigDecimal(15, new BigDecimal(record.get("评论数")));
                    stat_video.setBigDecimal(16, new BigDecimal(record.get("分享数")));
                    stat_video.setBigDecimal(17, new BigDecimal(record.get("播放数")));
                    stat_video.setTimestamp(18, java.sql.Timestamp.valueOf(record.get("发布时间")));
                    stat_video.addBatch();
                    count++;
                    if(count % BATCH_SIZE == 0){
                        stat_video.executeBatch();
                        stat_video.clearBatch();//显示清空批处理，确保下一次批处理的数据不会重复
                        conn.commit();
                        System.out.println("已导入" + count + "条数据");
                    }
                }
                stat_video.executeBatch();
                conn.commit();
                System.out.println("已导入" + count + "条数据");
                long end = System.currentTimeMillis();
                System.out.println("导入数据耗时：" + (end - start) + "ms\n\n");
            } catch (Exception e) {
                System.err.println("Error reading videos file");
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }
        public DataBaseScript() {
            getConnection();
            createtable();
        }
        // public static void main(String[] args) {
        //     datamanipu dataManipulator = new datamanipu();
        //     dataManipulator.getConnection();
        // }
    
    
}
