import java.io.IOException;
import java.math.BigDecimal;

public interface DataManner {
//     插入数据
//    1. `public boolean sign_user`
//    2. `public boolean follow_user`
// 4. 删除数据
//    1. `public boolean logout_user`
//    2. `public boolean unfollow_user`
// 5. 查询数据
//    1. `public boolean select_mid` 根据作者查询
//    2. `public boolean select_follower` 查询关注当前用户的用户
//    3. `public boolean select_following` 查询当前用户关注的用户
// 6. 更新数据`public boolean update_user`
    //----------insert方法--------------------------------------------------------------------------------------------------
    public boolean sign_user(User user,DataScript dataManipulator) throws IOException;
    public boolean follow_user(BigDecimal mid,BigDecimal follower,DataScript dataManipulator) throws IOException;

    //----------delete方法--------------------------------------------------------------------------------------------------
    public boolean logout_user(BigDecimal mid,DataScript dataManipulator) throws IOException;
    public boolean unfollow_user(BigDecimal mid,BigDecimal follower,DataScript dataManipulator) throws IOException;

    //----------select方法--------------------------------------------------------------------------------------------------
    public boolean select_mid(String name,DataScript dataManipulator) throws IOException;
    public boolean select_follower(BigDecimal mid,DataScript dataManipulator) throws IOException;
    public boolean select_following(BigDecimal mid,DataScript dataManipulator) throws IOException;

    //----------update方法--------------------------------------------------------------------------------------------------
    public boolean update_user(BigDecimal mid,String column,String value,DataScript dataManipulator) throws IOException;
}
