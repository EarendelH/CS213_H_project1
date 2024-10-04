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
        private String dbname = "project1_big";
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
                PreparedStatement stat=conn.prepareStatement("DROP TABLE IF EXISTS follows;");
                stat.execute();
                stat=conn.prepareStatement("DROP TABLE IF EXISTS users;");
                stat.execute();
                stat=conn.prepareStatement("VACUUM;");
                stat.execute();
                stat=conn.prepareStatement("\\timing;");
                long start = System.currentTimeMillis();
                String sql =
                "CREATE TABLE users("+
                "mid numeric primary key,"+
                "level int not null,"+
                "name varchar(100),"+
                "official_role int not null,"+
                "official_title varchar(100),"+
                "rank int not null,"+
                "sign varchar(100)"+
                ")";
                stat=conn.prepareStatement(sql);
                stat.execute();
                sql="CREATE TABLE follows( "+
                "following numeric not null,"+
                "follower numeric not null,"+
                "foreign key(following) references users(mid),"+
                "foreign key(follower) references users(mid)"+
                ")";
                stat=conn.prepareStatement(sql);
                stat.execute();
                System.out.println("Table users_ created");
                long end = System.currentTimeMillis();
                System.out.println("创建表耗时：" + (end - start) + " ms\n");
            }catch (Exception e){
                System.err.println("Table create error");
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }
        @Override
        public void users_reader() throws SQLException{
            conn.setAutoCommit(false); // 禁止自动提交，以便于回滚和提高效率
            final int BATCH_SIZE = 200000; // 每次批量插入的数据量
        
            String users_file = "/Users/earendelh/Documents/CS213DB/project1/random_users_data.csv";
            try(
                BufferedReader reader = new BufferedReader(new FileReader(users_file));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
                BufferedReader reader1 = new BufferedReader(new FileReader(users_file));
                CSVParser csvParser1 = new CSVParser(reader1, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
            ){

                String sql_users = "INSERT INTO users(mid, level, name, official_role, official_title, rank, sign) VALUES(?, ?, ?, ?, ?, ?, ?)";
                String sql_follows="INSERT INTO follows(following,follower) VALUES(?,?)";
                PreparedStatement stat_video = conn.prepareStatement(sql_users);
                PreparedStatement stat_follows=conn.prepareStatement(sql_follows);
                long start = System.currentTimeMillis();
                Map <String, Integer> heads = csvParser.getHeaderMap();
                System.out.println("Headers: "  + heads);
                int count_user = 0;
                int count_follows=0;
                System.out.println("开始导入users数据, Batch size: " + BATCH_SIZE);
                for(CSVRecord record: csvParser){
                    stat_video.setBigDecimal(1, new BigDecimal(record.get("mid")));
                    stat_video.setInt(2, Integer.parseInt(record.get("level")));
                    stat_video.setString(3, record.get("name"));
                    stat_video.setInt(4, Integer.parseInt(record.get("official_role")));
                    stat_video.setString(5, record.get("official_title"));
                    stat_video.setInt(6, Integer.parseInt(record.get("rank")));
                    stat_video.setString(7, record.get("sign"));
                    stat_video.addBatch();
                    count_user++;
                    if(count_user % BATCH_SIZE == 0){
                        stat_video.executeBatch();
                        stat_video.clearBatch();//显示清空批处理，确保下一次批处理的数据不会重复
                        conn.commit();
                        System.out.println("已导入" + count_user + "条user数据");
                    }
                }
                stat_follows.executeBatch();
                conn.commit();
                System.out.println("已导入" + count_user + "条user数据");
                long end1 = System.currentTimeMillis();
                System.out.println("导入user数据耗时：" + (end1 - start) + "ms\n");
                for(CSVRecord record: csvParser1){
                    String[] followinfo=record.get("following_list").split(",");
                    for(String following:followinfo){
                        stat_follows.setBigDecimal(1,new BigDecimal(following));
                        stat_follows.setBigDecimal(2,new BigDecimal(record.get("mid")));
                        stat_follows.addBatch();
                        count_follows++;
                        if(count_follows % BATCH_SIZE == 0){
                            stat_follows.executeBatch();
                            stat_follows.clearBatch();//显示清空批处理，确保下一次批处理的数据不会重复
                            conn.commit();
                            System.out.println("已导入" + count_follows + "条数据");
                        }
                    }
                }
                
                stat_video.executeBatch();
                conn.commit();
                System.out.println("已导入"+count_follows+"条follows数据");
                long end2 = System.currentTimeMillis();
                System.out.println("导入follows数据耗时：" + (end2 - end1) + "ms");
                System.out.println("导入数据耗时：" + (end2 - start) + "ms\n");
            } catch (Exception e) {
                System.err.println("Error reading users_ file");
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
