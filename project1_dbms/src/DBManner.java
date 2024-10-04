import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

// import DataBaseScript.datamanipu;

public class DBManner implements DataManner{
    @Override
    public boolean ref_video(Video video,DataScript dataManipulator) {
        Connection conn=dataManipulator.getConn();
        try{
            long start = System.currentTimeMillis();
            String sql_ref="insert into videos values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement stat_ref=conn.prepareStatement(sql_ref);
            stat_ref.setInt(1,video.getTerm());
            stat_ref.setString(2,video.getDuration());
            stat_ref.setString(3,video.getTitle());
            stat_ref.setString(4,video.getPart());
            stat_ref.setString(5,video.getDescription());
            stat_ref.setString(6,video.getUrl());
            stat_ref.setBigDecimal(7,video.getLast_time());
            stat_ref.setString(8,video.getAuthor());
            stat_ref.setBigDecimal(9,video.getMid());
            stat_ref.setBigDecimal(10,video.getAid());
            stat_ref.setBigDecimal(11,video.getCoins());
            stat_ref.setBigDecimal(12,video.getDanmus());
            stat_ref.setBigDecimal(13,video.getFavorites());
            stat_ref.setBigDecimal(14,video.getLikes());
            stat_ref.setBigDecimal(15,video.getComments());
            stat_ref.setBigDecimal(16,video.getShares());
            stat_ref.setBigDecimal(17,video.getPlays());
            stat_ref.setTimestamp(18,video.getDate());
            stat_ref.execute();
            System.out.println("视频aid:"+video.getAid()+"成功推荐至第"+video.getTerm()+"期");
            long end = System.currentTimeMillis();
            System.out.println("导入数据耗时：" + (end - start) + " ms\n\n");

        }catch(Exception e){
            System.err.println("插入视频失败\n\n");
            System.err.println(e.getMessage());
            System.exit(1);
        }
        
        return false;
    }
    @Override
    public boolean delete_video(int term,BigDecimal aid,DataScript dataManipulator) {
        Connection conn=dataManipulator.getConn();
        try{
            long start = System.currentTimeMillis();
            String sql_delete="delete from videos where term=? and aid=?";
            PreparedStatement stat_delete=conn.prepareStatement(sql_delete);
            stat_delete.setInt(1,term);
            stat_delete.setBigDecimal(2,aid);
            stat_delete.execute();
            System.out.println("视频aid:"+aid+"已从第"+term+"期删除");
            long end = System.currentTimeMillis();
            System.out.println("删除数据耗时：" + (end - start) + " ms\n\n");
        }catch(Exception e){
            System.err.println("删除视频失败\n\n");
            System.err.println(e.getMessage());
            System.exit(1);
        }
        
        return false;
    }
    @Override
    public boolean select_author(String author,DataScript dataManipulator) {
        Connection conn=dataManipulator.getConn();
        try{
            long start = System.currentTimeMillis();
            String sql_select="select * from videos where author=?";
            PreparedStatement stat_select=conn.prepareStatement(sql_select);
            stat_select.setString(1,author);
            stat_select.execute();
            System.out.println("作者"+author+"入选每日推荐的视频如下：");
            while(stat_select.getResultSet().next()){
                System.out.println("aid:"+stat_select.getResultSet().getBigDecimal("aid")+"  标题："+stat_select.getResultSet().getString("title"));
            }
            long end = System.currentTimeMillis();
            System.out.println("查询数据耗时：" + (end - start) + " ms\n\n");
        }catch(Exception e){
            System.err.println("查询视频失败");
            System.err.println(e.getMessage());
            System.exit(1);
        }
        
        return false;
    }
    @Override
    public boolean select_aid(BigDecimal aid,DataScript dataManipulator) {
        Connection conn=dataManipulator.getConn();
        try{
            long start = System.currentTimeMillis();
            String sql_select="select * from videos where aid=?";
            PreparedStatement stat_select=conn.prepareStatement(sql_select);
            stat_select.setBigDecimal(1,aid);
            stat_select.execute();
            System.out.println("aid:"+aid+"的视频入选每周推荐的时间如下：");
            while(stat_select.getResultSet().next()){
                System.out.println("期数："+stat_select.getResultSet().getString("term")+"; 标题："+stat_select.getResultSet().getString("title")+"; 作者："+stat_select.getResultSet().getString("author")+"; 时间："+stat_select.getResultSet().getTimestamp("date"));
            }
            long end = System.currentTimeMillis();
            System.out.println("查询数据耗时：" + (end - start) + " ms\n\n");
        }catch(Exception e){
            System.err.println("查询视频失败\n\n");
            System.err.println(e.getMessage());
            System.exit(1);
        }
        
        return false;
    }
    @Override
    public boolean update_video(int term,BigDecimal aid,String column,String content,DataScript dataManipulator) {
        Connection conn=dataManipulator.getConn();
        try{
            long start = System.currentTimeMillis();
            String sql_update="update videos set "+column+"=? where term=? and aid=?";
            PreparedStatement stat_update=conn.prepareStatement(sql_update);
            
            stat_update.setInt(2,term);
            stat_update.setBigDecimal(3,aid);
            switch (column) {
                case "term":
                    stat_update.setInt(1,Integer.parseInt(content));
                    break;
                case "duration":
                    stat_update.setString(1,content);
                    break;
                case "title":
                    stat_update.setString(1,content);
                    break;
                case "part":
                    stat_update.setString(1,content);
                    break;
                case "description":
                    stat_update.setString(1,content);
                    break;
                case "url":
                    stat_update.setString(1,content);
                    break;
                case "last_time":
                    stat_update.setBigDecimal(1,new BigDecimal(content));
                    break;
                case "author":
                    stat_update.setString(1,content);
                    break;
                case "mid":
                    stat_update.setBigDecimal(1,new BigDecimal(content));
                    break;
                case "aid":
                    stat_update.setBigDecimal(1,new BigDecimal(content));
                    break;
                case "coins":
                    stat_update.setBigDecimal(1,new BigDecimal(content));
                    break;
                case "danmus":
                    stat_update.setBigDecimal(1,new BigDecimal(content));
                    break;
                case "favorites":
                    stat_update.setBigDecimal(1,new BigDecimal(content));
                    break;
                case "likes":
                    stat_update.setBigDecimal(1,new BigDecimal(content));
                    break;
                case "comments":
                    stat_update.setBigDecimal(1,new BigDecimal(content));
                    break;
                case "shares":
                    stat_update.setBigDecimal(1,new BigDecimal(content));
                    break;
                case "plays":
                    stat_update.setBigDecimal(1,new BigDecimal(content));
                    break;
                case "date":
                    stat_update.setTimestamp(1,Timestamp.valueOf(content));
                    break;
                default:
                    break;
            }
            stat_update.execute();
            System.out.println("第"+term+"期推荐的视频"+aid+" 的属性 "+column+"已更新为"+content);
            long end = System.currentTimeMillis();
            System.out.println("更新数据耗时：" + (end - start) + " ms\n\n");
        }catch(Exception e){
            System.err.println("更新视频失败");
            System.err.println(e.getMessage());
            System.exit(1);
        }
        
        return false;
    }
}
