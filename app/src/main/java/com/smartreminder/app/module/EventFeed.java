package com.smartreminder.app.module;

/**
 * Created by LiJunxing on 6/10/14.
 */
public class EventFeed {
    public static final int GRAY_TAG = 0;
    public static final int RED_TAG = 1;
    public static final int YELLOW_TAG = 2;
    public static final int BLUE_TAG = 3;

    static final long GRAY_ALERT_LIMITE = 0;
    static final long RED_ALERT_LIMITE = 1000*60*60*24*3;
    static final long YELLOW_ALERT_LIMITE = 1000*60*60*24*10;

    private long createTime;
    private String title;
    private String description;
    private long alertTime;
    private boolean alertOn;
    private int category;

    public EventFeed() {
//        setCreateTime(System.currentTimeMillis());
    }

    // setters
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public void setTitle(String title) {
        this.title = title;
//        setCreateTime(System.currentTimeMillis());
    }

    public void setDescription(String description) {
        this.description = description;
//        setCreateTime(System.currentTimeMillis());
    }

    public void setAlertOn(boolean alertOn) {
        this.alertOn = alertOn;
//        setCreateTime(System.currentTimeMillis());
    }

    public void setAlertTime(long alertTime) {
        this.alertTime = alertTime;
        setCategory();
//        setCreateTime(System.currentTimeMillis());
    }

    public void setCategory() {
        long timeLeft = this.getAlertTime() - System.currentTimeMillis();

        if (timeLeft < GRAY_ALERT_LIMITE)
            this.category = GRAY_TAG;
        else if (timeLeft >= GRAY_ALERT_LIMITE && timeLeft < RED_ALERT_LIMITE )
            this.category = RED_TAG;
        else if (timeLeft >= RED_ALERT_LIMITE && timeLeft < YELLOW_ALERT_LIMITE)
            this.category = YELLOW_TAG;
        else
            this.category = BLUE_TAG;

//        setCreateTime(System.currentTimeMillis());
    }

    // getters
    public int getCategory() {
        setCategory();
        return category;
    }

    public long getAlertTime() {
        return alertTime;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public long getCreateTime() {
        return createTime;
    }

    public boolean getAlertOn() {
        return alertOn;
    }
}
