import java.math.BigDecimal;

//cd /Users/earendelh/Documents/CS213DB/project1/project1_dbms_big ; /usr/bin/env /Library/Java/JavaVirtualMachines/jdk-22.jdk/Contents/Home/bin/java -Xms512m -Xmx4g @/var/folders/d9/xx71dx5j1p3c9chqs2lkpnjw0000gn/T/cp_ddxksa6y6jndqt39qub8lcsi5.argfile Client
public class Client {
    public static void main(String[] args)
    {
        // String manipulator="DataBaseScript";
        String manipulator="IOFileScript";
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
            dataManipulator.users_reader();
            User user = new User(
                new BigDecimal("12345678"), // mid
                1,// level
                "EarnedelH",// name
                2,// official_role
                "administator",// official_title
                1,// rank
                "I am EarnedelH"// sign
            );
            dbms.select_mid("1234567", dataManipulator);
            dbms.select_following(new BigDecimal(1234567), dataManipulator);
            dbms.select_follower(new BigDecimal(1234567), dataManipulator);
            dbms.sign_user(user, dataManipulator);
            dbms.follow_user(new BigDecimal(1234567), new BigDecimal(12345678), dataManipulator);
            dbms.unfollow_user(new BigDecimal(1234567), new BigDecimal(12345678), dataManipulator);
            dbms.update_user(new BigDecimal(12345678), "name", "EarnedelH_new", dataManipulator);
            dbms.logout_user(new BigDecimal(12345678), dataManipulator);
            
            
            dataManipulator.closeConnection();
        }catch(Exception e){
            System.err.println("操作失败");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
