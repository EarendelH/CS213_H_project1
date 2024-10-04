import java.math.BigDecimal;
import java.sql.Timestamp;

public class Video {
    private int term;
    private String duration;
    private String title;
    private String part;
    private String description;
    private String url;
    private BigDecimal last_time;
    private String author;
    private BigDecimal mid;
    private BigDecimal aid;
    private BigDecimal coins;
    private BigDecimal danmus;
    private BigDecimal favorites;
    private BigDecimal likes;
    private BigDecimal comments;
    private BigDecimal shares;
    private BigDecimal plays;
    private Timestamp date;

    public Video(int term, String duration, String title, String part, String description,String url, BigDecimal last_time, String author, BigDecimal mid, BigDecimal aid, BigDecimal coins, BigDecimal danmus, BigDecimal favorites, BigDecimal likes, BigDecimal comments, BigDecimal shares, BigDecimal plays, Timestamp date) {
        this.term = term;
        this.duration = duration;
        this.title = title;
        this.description = description;
        this.part = part;
        this.url = url;
        this.last_time = last_time;
        this.author = author;
        this.mid = mid;
        this.aid = aid;
        this.coins = coins;
        this.danmus = danmus;
        this.favorites = favorites;
        this.likes = likes;
        this.comments = comments;
        this.shares = shares;
        this.plays = plays;
        this.date = date;
    }
    public int getTerm() {
        return term;
    }
    public void setTerm(int term) {
        this.term = term;
    }
    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getPart() {
        return part;
    }
    public void setPart(String part) {
        this.part = part;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public BigDecimal getLast_time() {
        return last_time;
    }
    public void setLast_time(BigDecimal last_time) {
        this.last_time = last_time;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public BigDecimal getMid() {
        return mid;
    }
    public void setMid(BigDecimal mid) {
        this.mid = mid;
    }
    public BigDecimal getAid() {
        return aid;
    }
    public void setAid(BigDecimal aid) {
        this.aid = aid;
    }
    public BigDecimal getCoins() {
        return coins;
    }
    public void setCoins(BigDecimal coins) {
        this.coins = coins;
    }
    public BigDecimal getDanmus() {
        return danmus;
    }
    public void setDanmus(BigDecimal danmus) {
        this.danmus = danmus;
    }
    public BigDecimal getFavorites() {
        return favorites;
    }
    public void setFavorites(BigDecimal favorites) {
        this.favorites = favorites;
    }
    public BigDecimal getLikes() {
        return likes;
    }
    public void setLikes(BigDecimal likes) {
        this.likes = likes;
    }
    public BigDecimal getComments() {
        return comments;
    }
    public void setComments(BigDecimal comments) {
        this.comments = comments;
    }
    public BigDecimal getShares() {
        return shares;
    }
    public void setShares(BigDecimal shares) {
        this.shares = shares;
    }
    public BigDecimal getPlays() {
        return plays;
    }
    public void setPlays(BigDecimal plays) {
        this.plays = plays;
    }
    public Timestamp getDate() {
        return date;
    }
    public void setDate(Timestamp date) {
        this.date = date;
    }
    @Override
    public String toString() {
        return "Video [aid=" + aid + ", author=" + author + ", coins=" + coins + ", comments=" + comments + ", danmus="
                + danmus + ", date=" + date + ", description=" + description + ", duration=" + duration + ", favorites="
                + favorites + ", last_time=" + last_time + ", likes=" + likes + ", mid=" + mid + ", part=" + part
                + ", plays=" + plays + ", shares=" + shares + ", term=" + term + ", title=" + title + ", url=" + url
                + "]";
    }
}
