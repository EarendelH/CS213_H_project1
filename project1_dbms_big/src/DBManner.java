import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;

// import DataBaseScript.datamanipu; // Correct the spelling if necessary or remove if not needed

public class DBManner implements DataManner {
    

    @Override
    public boolean sign_user(User user,DataScript dataManipulator) {
        Connection conn=dataManipulator.getConn();
        try{
            long start = System.currentTimeMillis();
            String sql_users = "INSERT INTO users(mid, level, name, official_role, official_title, rank, sign) VALUES(?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stat_user=conn.prepareStatement(sql_users);
            stat_user.setBigDecimal(1,user.getMid());
            stat_user.setInt(2,user.getLevel());
            stat_user.setString(3,user.getName());
            stat_user.setInt(4,user.getOfficial_role());
            stat_user.setString(5,user.getOfficial_title());
            stat_user.setInt(6,user.getRank());
            stat_user.setString(7,user.getSign());
            stat_user.execute();
            System.out.println("用户"+user.getName()+"已注册");
            long end = System.currentTimeMillis();
            System.out.println("插入数据耗时：" + (end - start) + " ms\n\n");
        }catch(Exception e){
            System.err.println("插入用户失败");
            System.err.println(e.getMessage());
            
        }
            
        return true;
    }
    @Override
    public boolean follow_user(BigDecimal id, BigDecimal followerId, DataScript dataManipulator) {
        Connection conn=dataManipulator.getConn();
        String sql_follows="INSERT INTO follows(following,follower) VALUES(?,?)";
        try{
            long start = System.currentTimeMillis();
            PreparedStatement stat_follows=conn.prepareStatement(sql_follows);
            stat_follows.setBigDecimal(1,id);
            stat_follows.setBigDecimal(2,followerId);
            stat_follows.execute();
            System.out.println("用户"+followerId+"已关注用户"+id);
            long end = System.currentTimeMillis();
            System.out.println("插入数据耗时：" + (end - start) + " ms\n\n");
        }catch(Exception e){
            System.err.println("关注失败");
            System.err.println(e.getMessage());
        }
        return true;
    }
    @Override
    public boolean unfollow_user(BigDecimal followingId, BigDecimal followerId, DataScript dataManipulator) {
        Connection conn=dataManipulator.getConn();
        try{
            long start = System.currentTimeMillis();
            String sql_unfollows="delete from follows where following=? and follower=?";
            PreparedStatement stat_unfollows=conn.prepareStatement(sql_unfollows);
            stat_unfollows.setBigDecimal(1,followingId);
            stat_unfollows.setBigDecimal(2,followerId);
            stat_unfollows.execute();
            System.out.println("用户"+followerId+"已取消关注用户"+followingId);
            long end = System.currentTimeMillis();
            System.out.println("删除数据耗时：" + (end - start) + " ms\n\n");
        }catch(Exception e){
            System.err.println("取消关注失败");
            System.err.println(e.getMessage());   
        }
        return true;
    }

    @Override
    public boolean logout_user(BigDecimal id, DataScript dataManipulator) {
        Connection conn=dataManipulator.getConn();
        try{
            long start = System.currentTimeMillis();
            String sql_logout="delete from users where mid=?";
            PreparedStatement stat_logout=conn.prepareStatement(sql_logout);
            stat_logout.setBigDecimal(1,id);
            stat_logout.execute();
            System.out.println("用户"+id+"已注销");
            long end = System.currentTimeMillis();
            System.out.println("删除数据耗时：" + (end - start) + " ms\n\n");
        }catch(Exception e){
            System.err.println("注销失败");
            System.err.println(e.getMessage());   
        }
        return true;
    }

    @Override
    public boolean select_mid(String mid, DataScript dataManipulator) {
        Connection conn=dataManipulator.getConn();
        try{
            long start = System.currentTimeMillis();
            String sql_select="select * from users where mid=?";
            PreparedStatement stat_select=conn.prepareStatement(sql_select);
            stat_select.setBigDecimal(1,new BigDecimal(mid));
            stat_select.execute();
            System.out.println("mid为"+mid+"的用户信息如下：");
            while(stat_select.getResultSet().next()){
                System.out.println("mid:"+stat_select.getResultSet().getBigDecimal("mid")+"  名称："+stat_select.getResultSet().getString("name"));
            }
            long end = System.currentTimeMillis();
            System.out.println("查询数据耗时：" + (end - start) + " ms\n\n");
        }catch(Exception e){
            System.err.println("查询用户失败");
            System.err.println(e.getMessage());
            
        }
        
        return true;
    }

    @Override
    public boolean select_following(BigDecimal id, DataScript dataManipulator) {
        Connection conn=dataManipulator.getConn();
        try{
            long start = System.currentTimeMillis();
            String sql_select="select * from follows where follower=?";
            PreparedStatement stat_select=conn.prepareStatement(sql_select);
            stat_select.setBigDecimal(1,id);
            stat_select.execute();
            System.out.println("用户"+id+"关注的用户如下：");
            while(stat_select.getResultSet().next()){
                System.out.println("mid:"+stat_select.getResultSet().getBigDecimal("following"));
            }
            long end = System.currentTimeMillis();
            System.out.println("查询数据耗时：" + (end - start) + " ms\n\n");
        }catch(Exception e){
            System.err.println("查询用户失败");
            System.err.println(e.getMessage());
        }
        
        return true;
    }
    @Override
    public boolean select_follower(BigDecimal id, DataScript dataManipulator) {
        Connection conn=dataManipulator.getConn();
        try{
            long start = System.currentTimeMillis();
            String sql_select="select * from follows where following=?";
            PreparedStatement stat_select=conn.prepareStatement(sql_select);
            stat_select.setBigDecimal(1,id);
            stat_select.execute();
            System.out.println("关注用户"+id+"的用户如下：");
            while(stat_select.getResultSet().next()){
                System.out.println("mid:"+stat_select.getResultSet().getBigDecimal("follower"));
            }
            long end = System.currentTimeMillis();
            System.out.println("查询数据耗时：" + (end - start) + " ms\n\n");
        }catch(Exception e){
            System.err.println("查询用户失败");
            System.err.println(e.getMessage());
        }
        return true;
    }


    
    @Override
    public boolean update_user(BigDecimal id, String column, String content, DataScript dataManipulator) {
        Connection conn=dataManipulator.getConn();
        try{
            long start = System.currentTimeMillis();
            String sql_update="update users set "+column+"=? where mid=?";
            PreparedStatement stat_update=conn.prepareStatement(sql_update);
            stat_update.setBigDecimal(2,id);
            switch (column) {
                case "mid":
                    stat_update.setBigDecimal(1,new BigDecimal(content));
                    break;
                case "is_followed":
                    stat_update.setInt(1,Integer.parseInt(content));
                    break;
                case "level":
                    stat_update.setInt(1,Integer.parseInt(content));
                    break;
                case "name":
                    stat_update.setString(1,content);
                    break;
                case "official_role":
                    stat_update.setInt(1,Integer.parseInt(content));
                    break;
                case "official_title":
                    stat_update.setString(1,content);
                    break;
                case "rank":
                    stat_update.setInt(1,Integer.parseInt(content));
                    break;
                case "sign":
                    stat_update.setString(1,content);
                    break;
                default:
                    break;
            }
            stat_update.execute();
            System.out.println("用户"+id+" 的属性 "+column+"已更新为"+content);
            long end = System.currentTimeMillis();
            System.out.println("更新数据耗时：" + (end - start) + " ms\n\n");
        }catch(Exception e){
            System.err.println("更新用户失败");
            System.err.println(e.getMessage());
            
        }
        
        return true;
    }
    
}
