import java.math.BigDecimal;
import java.sql.Timestamp;


public class Client {
    public static void main(String[] args)
    {
        String manipulator="DataBaseScript";
        // String manipulator="IOFileScript";
        String manner="";
        System.out.println("选择操作系统"+manipulator);
        if (manipulator.equals("DataBaseScript")){
            manner="DBManner";
        }
        else{
            manner="FileManner";
        }
        DataManner dbms = null;
        DataScript dataManipulator = null;
        try {
            dbms = (DataManner) Class.forName(manner).getDeclaredConstructor().newInstance();
            dataManipulator = (DataScript)Class.forName(manipulator).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        try{
            dataManipulator.videos_reader();
            Video video = new Video(
            238, // term
            "00:45:30", // duration
            "DBMS test", // title
            "SQL", // part
            "My fist project for Database H", // description
            "https://earendelh.github.io/", // url
            new BigDecimal("12.34"), // last_time
            "EarendelH", // author
            new BigDecimal("123456"), // mid
            new BigDecimal("123456"), // aid
            new BigDecimal("123456"), // coins
            new BigDecimal("123456"), // danmus
            new BigDecimal("123456"), // favorites
            new BigDecimal("123456"), // likes
            new BigDecimal("123456"), // comments
            new BigDecimal("123456"), // shares
            new BigDecimal("123456"), // plays
            new Timestamp(System.currentTimeMillis()) // date
            );
            dbms.select_author("河野华", dataManipulator);
            dbms.select_aid(new BigDecimal("356737965"), dataManipulator);
            dbms.ref_video(video,dataManipulator);
            dbms.update_video(238,new BigDecimal("123456"),"coins","987654", dataManipulator);
            dbms.delete_video(238,new BigDecimal("123456"),dataManipulator);
            
            dataManipulator.closeConnection();
        }catch(Exception e){
            System.err.println("操作失败");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
