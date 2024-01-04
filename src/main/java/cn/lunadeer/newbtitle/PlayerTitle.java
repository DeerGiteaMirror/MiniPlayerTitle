package cn.lunadeer.newbtitle;

public class PlayerTitle extends Title {
    private Long _expire_at = -1L;

    public PlayerTitle(Integer id, Long expire_at) {
        super(id);
        this._expire_at = expire_at;
    }

    public String getExpireAt() {
        if (this._expire_at == -1L) {
            return "永久";
        } else if (this._expire_at < System.currentTimeMillis()) {
            return "已过期";
        } else {
            return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(this._expire_at));
        }
    }

    public Boolean isExpired() {
        if (this._expire_at == -1L) {
            return false;
        } else {
            return this._expire_at < System.currentTimeMillis();
        }
    }
}
