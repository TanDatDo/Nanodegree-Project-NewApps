package com.dan.newapps;

/**
 * Created by Dat T Do on 7/20/2017.
 */

public class News {
    /*
     * Image url of the news
     * */
    private final String mImageUrl;

    /*
     * title of the news
     * */
    private String mTitle;


    /*
     * section of the news
     * */
    private String mSection;

    /*
     *time publish of the news
     * */
    private String mPublishTime;

    /*
    * web url of the news
    * */
    private String mWebUrl;

    /**
     * Constructs a new {@link com.dan.newapps.News} News Object
     *
     * @param imageUrl    is the image url of the news
     * @param title       is the title of the news
     * @param section     is the section to which the news belong
     * @param publishTime is the time when the news is published
     */
    public News(String imageUrl, String title,
                String section, String publishTime, String webUrl) {
        mImageUrl = imageUrl;
        mTitle = title;
        mSection = section;
        mPublishTime = publishTime;
        mWebUrl = webUrl;
    }

    //get the image url of the news
    public String getImageUrl() {
        return mImageUrl;
    }

    //get the title of the news
    public String getTitle() {
        return mTitle;
    }

    //get the section of the news
    public String getSection() {
        return mSection;
    }

    //get the publish time of the news
    public String getPublishTime() {
        return mPublishTime;
    }

    //get the web url of the news
    public String getWebUrl() {
        return mWebUrl;
    }
}
