import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class FileManner implements DataManner {
    public String videos_csv = "/Users/earendelh/Documents/CS213DB/project1/B站每周.csv";
    public String videos_json = "/Users/earendelh/Documents/CS213DB/project1/bilibili.json";

    private List<Map<String, String>> readJsonFile() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Map<String, String>> dataList = new ArrayList<>();
        Type listType = new TypeToken<List<Map<String, String>>>() {}.getType();
        try (FileReader reader = new FileReader(videos_json)) {
            dataList = gson.fromJson(reader, listType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    private void writeJsonFile(List<Map<String, String>> dataList) {
        try (FileWriter writer = new FileWriter(videos_json)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(dataList, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean ref_video(Video video, DataScript dataManipulator) {
        long start = System.currentTimeMillis();
        List<Map<String, String>> dataList = readJsonFile();
        Map<String, String> newObject = Map.ofEntries(
            Map.entry("期数", String.valueOf(video.getTerm())),
            Map.entry("期数描述", video.getDuration()),
            Map.entry("标题", video.getTitle()),
            Map.entry("视频类型", video.getPart()),
            Map.entry("视频标签", video.getDescription()),
            Map.entry("视频链接", video.getUrl()),
            Map.entry("视频时长", video.getLast_time().toString()),
            Map.entry("up主", video.getAuthor()),
            Map.entry("up主_id", video.getMid().toString()),
            Map.entry("aid", video.getAid().toString()),
            Map.entry("投币数", video.getCoins().toString()),
            Map.entry("弹幕数", video.getDanmus().toString()),
            Map.entry("收藏数", video.getFavorites().toString()),
            Map.entry("点赞数", video.getLikes().toString()),
            Map.entry("评论数", video.getComments().toString()),
            Map.entry("分享数", video.getShares().toString()),
            Map.entry("播放数", video.getPlays().toString()),
            Map.entry("发布时间", video.getDate().toString())
        );

        dataList.add(newObject);
        writeJsonFile(dataList);
        System.out.println("视频aid:" + video.getAid() + "成功推荐至第" + video.getTerm() + "期");

        long end = System.currentTimeMillis();
        System.out.println("插入数据耗时：" + (end - start) + " ms\n\n");
        return true;
    }

    public boolean delete_video(int term, BigDecimal aid, DataScript dataManipulator) {
        long start = System.currentTimeMillis();
        List<Map<String, String>> dataList = readJsonFile();
        boolean flag = false;

        for (Map<String, String> video : dataList) {
            if (video.get("期数").equals(String.valueOf(term)) && video.get("aid").equals(aid.toString())) {
                dataList.remove(video);
                flag = true;
                System.out.println("视频aid:" + aid + "已从第" + term + "期删除");
                break;
            }
        }

        writeJsonFile(dataList);

        if (!flag) {
            System.out.println("视频aid:" + aid + "不存在");
            return false;
        }

        long end = System.currentTimeMillis();
        System.out.println("删除数据耗时：" + (end - start) + " ms\n\n");
        return true;
    }

    public boolean select_author(String author, DataScript dataManipulator) {
        long start = System.currentTimeMillis();
        List<Map<String, String>> dataList = readJsonFile();
        System.out.println("查询up主" + author + "入选每周推荐的视频");
        boolean flag = false;

        for (Map<String, String> video : dataList) {
            if (author.equals(video.get("up主"))) {
                System.out.println("up主:" + video.get("up主") + ", 标题: " + video.get("标题") +", aid: "+video.get("aid") +", 入选第"+video.get("期数")+"期每周推荐");
                flag = true;
            }
        }

        if (!flag) {
            System.out.println("作者" + author + "不存在");
            return false;
        }

        long end = System.currentTimeMillis();
        System.out.println("查询数据耗时：" + (end - start) + " ms\n\n");
        return true;
    }

    public boolean select_aid(BigDecimal aid, DataScript dataManipulator) {
        long start = System.currentTimeMillis();
        List<Map<String, String>> dataList = readJsonFile();
        boolean flag = false;
        System.out.println("查询aid为" + aid + "入选每周推荐的视频");
        for (Map<String, String> video : dataList) {
            if (video.get("aid").equals(aid.toString())) {
                System.out.println("up主:" + video.get("up主") + ", 标题:" + video.get("标题") +", aid:"+video.get("aid") +", 视频入选第"+video.get("期数")+"期每周推荐");
                flag = true;
                break;
            }
        }

        if (!flag) {
            System.out.println("视频aid:" + aid + "不存在");
            return false;
        }

        long end = System.currentTimeMillis();
        System.out.println("查询数据耗时：" + (end - start) + " ms\n\n");
        return true;
    }

    public boolean update_video(int term, BigDecimal aid, String column, String content, DataScript dataManipulator) {
        long start = System.currentTimeMillis();
        List<Map<String, String>> dataList = readJsonFile();
        boolean flag = false;

        for (Map<String, String> video : dataList) {
            if (video.get("期数").equals(String.valueOf(term)) && video.get("aid").equals(aid.toString())) {
                video.put(column, content);
                flag = true;
                System.out.println("视频aid:" + aid + "的第" + term + "期" + column + "已更新为" + content);
                break;
            }
        }

        writeJsonFile(dataList);

        if (!flag) {
            System.out.println("期数为"+term+", aid:" + aid + "的视频不存在\n\n");
            return false;
        }

        long end = System.currentTimeMillis();
        System.out.println("更新数据耗时：" + (end - start) + " ms\n\n");
        return true;
    }
}
