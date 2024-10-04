import java.io.IOException;
import java.math.BigDecimal;

public interface DataManner {
    //----------insert方法--------------------------------------------------------------------------------------------------
    public boolean ref_video(Video video,DataScript dataManipulator) throws IOException;

    //----------delete方法--------------------------------------------------------------------------------------------------
    public boolean delete_video(int term,BigDecimal aid,DataScript dataManipulator) throws IOException;

    //----------select方法--------------------------------------------------------------------------------------------------
    public boolean select_author(String author,DataScript dataManipulator);
    public boolean select_aid(BigDecimal aid,DataScript dataManipulator);

    //----------update方法--------------------------------------------------------------------------------------------------
    public boolean update_video(int term,BigDecimal mid,String column,String content,DataScript dataManipulator) throws IOException;
}
