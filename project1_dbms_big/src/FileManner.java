import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class FileManner implements DataManner {
    public String users_json = "/Users/earendelh/Documents/CS213DB/project1/users.json";

    // 流式读取 JSON 文件
    private Map<String, Map<String, String>> readJsonFileStream() {
        Map<String, Map<String, String>> dataMap = new HashMap<>();
        try (JsonReader reader = new JsonReader(new FileReader(users_json))) {
            reader.beginArray();
            while (reader.hasNext()) {
                reader.beginObject();
                Map<String, String> user = new HashMap<>();
                String mid = "";
                while (reader.hasNext()) {
                    String key = reader.nextName();
                    String value = reader.nextString();
                    if (key.equals("mid")) {
                        mid = value;
                    }
                    user.put(key, value);
                }
                dataMap.put(mid, user);
                reader.endObject();
            }
            reader.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataMap;
    }

    // 流式写入 JSON 文件
    private void writeJsonFileStream(Map<String, Map<String, String>> dataMap) {
        try (JsonWriter writer = new JsonWriter(new FileWriter(users_json))) {
            writer.setIndent("  ");
            writer.beginArray();
            for (Map<String, String> user : dataMap.values()) {
                writer.beginObject();
                for (Map.Entry<String, String> entry : user.entrySet()) {
                    writer.name(entry.getKey()).value(entry.getValue());
                }
                writer.endObject();
            }
            writer.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean sign_user(User user, DataScript dataManipulator) {
        long start = System.currentTimeMillis();
        Map<String, Map<String, String>> dataMap = readJsonFileStream();
        Map<String, String> newUser = new HashMap<>();
        newUser.put("mid", user.getMid().toString());
        newUser.put("level", String.valueOf(user.getLevel()));
        newUser.put("name", user.getName());
        newUser.put("official_role", String.valueOf(user.getOfficial_role()));
        newUser.put("official_title", user.getOfficial_title());
        newUser.put("rank", String.valueOf(user.getRank()));
        newUser.put("sign", user.getSign());
        newUser.put("following_list", "");
        dataMap.put(user.getMid().toString(), newUser);
        writeJsonFileStream(dataMap);

        System.out.println("用户" + user.getName() + "已注册");
        long end = System.currentTimeMillis();
        System.out.println("插入数据耗时：" + (end - start) + " ms\n\n");
        return true;
    }
    
    @Override
    public boolean follow_user(BigDecimal mid, BigDecimal follower, DataScript dataManipulator) {
        long start = System.currentTimeMillis();
        Map<String, Map<String, String>> dataMap = readJsonFileStream();
        boolean flag = false;
    
    
        for (Map<String, String> user : dataMap.values()) {
            
            if (user.get("mid").equals(follower.toString())) {
                String newFollowing = user.get("following_list") + "," + mid.toString();
                user.put("following_list", newFollowing);
                flag = true;
                System.out.println("用户" + follower + "已关注用户" + mid);
                break;

            }
            
        }
    
        writeJsonFileStream(dataMap);
    
        if (!flag) {
            System.out.println("用户" + mid + "或" + follower + "不存在");
            return false;
        }
    
        long end = System.currentTimeMillis();
        System.out.println("插入数据耗时：" + (end - start) + " ms\n\n");
        return true;
    }

    @Override
    public boolean logout_user(BigDecimal mid, DataScript dataManipulator) {
        long start = System.currentTimeMillis();
        Map<String, Map<String, String>> dataMap = readJsonFileStream();
        boolean flag = false;
    
        Map<String, String> userToRemove = null;
        for (Map<String, String> user : dataMap.values()) {
            if (user.get("mid").equals(mid.toString())) {
                userToRemove = user;
                flag = true;
                break;
            }
        }
    
        if (userToRemove != null) {
            dataMap.remove(userToRemove.get("mid"));
            System.out.println("用户" + mid + "已注销");
        }
    
        writeJsonFileStream(dataMap);
    
        if (!flag) {
            System.out.println("用户" + mid + "不存在");
            return false;
        }
    
        long end = System.currentTimeMillis();
        System.out.println("删除数据耗时：" + (end - start) + " ms\n\n");
        return true;
    }
    @Override
    public boolean unfollow_user(BigDecimal followingId, BigDecimal followerId, DataScript dataManipulator) {
        long start = System.currentTimeMillis();
        Map<String, Map<String, String>> dataMap = readJsonFileStream();
        boolean flag1 = false;
    
        for (Map<String, String> user : dataMap.values()) {
            if (user.get("mid").equals(followerId.toString())) {
                String[] followings = user.get("following_list").split(",");
                StringBuilder newFollowers = new StringBuilder();
                for (String following : followings) {
                    if (!following.equals(followingId.toString())) {
                        newFollowers.append(following).append(",");
                    }
                }
                user.put("following_list", newFollowers.toString());
                System.out.println("用户" + followerId + "已取消关注用户" + followingId);
                flag1 = true;
                break;
            }
        }
    
        writeJsonFileStream(dataMap);
    
        if (!flag1) {
            System.out.println("用户" + followingId + "或" + followerId + "不存在");
            return false;
        }
    
        long end = System.currentTimeMillis();
        System.out.println("删除数据耗时：" + (end - start) + " ms\n\n");
        return true;
    }

    @Override
    public boolean select_mid(String mid, DataScript dataManipulator) {
        long start = System.currentTimeMillis();
        Map<String, Map<String, String>> dataMap = readJsonFileStream();
        boolean flag = false;
    
        for (Map<String, String> user : dataMap.values()) {
            if (mid.equals(user.get("mid"))) {
                System.out.println("用户mid: " + user.get("mid") + ", 用户名: " + user.get("name") +
                        ", 用户等级: " + user.get("level") + ", 用户签名: " + user.get("sign"));
                flag = true;
                break;
            }
        }
    
        if (!flag) {
            System.out.println("用户" + mid + "不存在\n");
            return false;
        }
    
        long end = System.currentTimeMillis();
        System.out.println("查询耗时：" + (end - start) + " ms\n\n");
        return true;
    }

    @Override
    public boolean select_follower(BigDecimal mid, DataScript dataManipulator) {
        long start = System.currentTimeMillis();
        Map<String, Map<String, String>> dataMap = readJsonFileStream();
        boolean flag = false;
    
        for (Map<String, String> user : dataMap.values()) {
            if (user.get("following_list").contains(mid.toString())) {
                System.out.println("关注用户 " + mid + " 的关注者有: " + user.get("mid"));
                flag = true;
            }
        }
    
        if (!flag) {
            System.out.println("用户 " + mid + " 不存在\n");
            return false;
        }
    
        long end = System.currentTimeMillis();
        System.out.println("查询耗时：" + (end - start) + " ms\n\n");
        return true;
    }

    @Override
    public boolean select_following(BigDecimal mid, DataScript dataManipulator) {
        long start = System.currentTimeMillis();
        Map<String, Map<String, String>> dataMap = readJsonFileStream();
        boolean flag = false;
    
        for (Map<String, String> user : dataMap.values()) {
            if (user.get("mid").equals(mid.toString())) {
                System.out.println("用户 " + mid + " 关注的用户有: " + user.get("following_list"));
                flag = true;
                break;
            }
        }
    
        if (!flag) {
            System.out.println("用户 " + mid + " 不存在");
            return false;
        }
    
        long end = System.currentTimeMillis();
        System.out.println("查询耗时：" + (end - start) + " ms\n\n");
        return true;
    }

    @Override
    public boolean update_user(BigDecimal mid, String column, String value, DataScript dataManipulator) {
        long start = System.currentTimeMillis();
        Map<String, Map<String, String>> dataMap = readJsonFileStream();
        boolean flag = false;

        for (Map<String, String> user : dataMap.values()) {
            if (user.get("mid").equals(mid.toString())) {
                user.put(column, value);
                flag = true;
                System.out.println("用户 " + mid + " 的 " + column + " 已更新为 " + value);
                break;
            }
        }

        writeJsonFileStream(dataMap);

        if (!flag) {
            System.out.println("用户 " + mid + " 不存在");
            return false;
        }

        long end = System.currentTimeMillis();
        System.out.println("更新数据耗时：" + (end - start) + " ms\n\n");
        return true;
    }
}