import java.math.BigDecimal;

public class User {
    private BigDecimal mid;
    private int level;
    private String name;
    private int official_role;
    private String official_title;
    private int rank;
    private String sign;

    public User(BigDecimal mid,int level, String name, int official_role, String official_title, int rank, String sign) {
        this.mid = mid;
        this.level = level;
        this.name = name;
        this.official_role = official_role;
        this.official_title = official_title;
        this.rank = rank;
        this.sign = sign;
    }
    public BigDecimal getMid() {
        return mid;
    }
    public void setMid(BigDecimal mid) {
        this.mid = mid;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getOfficial_role() {
        return official_role;
    }
    public void setOfficial_role(int official_role) {
        this.official_role = official_role;
    }
    public String getOfficial_title() {
        return official_title;
    }
    public void setOfficial_title(String official_title) {
        this.official_title = official_title;
    }
    public int getRank() {
        return rank;
    }
    public void setRank(int rank) {
        this.rank = rank;
    }
    public String getSign() {
        return sign;
    }
    public void setSign(String sign) {
        this.sign = sign;
    }
    @Override
    public String toString() {
        return "User [is_followed=" + ", level=" + level + ", mid=" + mid + ", name=" + name
                + ", official_role=" + official_role + ", official_title=" + official_title + ", rank=" + rank + ", sign="
                + sign + "]";
    }
   
}
