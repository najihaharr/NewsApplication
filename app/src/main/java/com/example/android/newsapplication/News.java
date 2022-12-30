package com.example.android.newsapplication;

public class News {
    private String mNewsTitle;
    private String mNewsCategory;
    private String mNewsDate;
    private String mNewsUrl;
    private int mImgResourceId = NO_IMAGE_PROVIDED;

    // -1 because its out of the range of all the possible valid resource ID
    private static final int NO_IMAGE_PROVIDED = -1;

    public News(String newsTitle, String newsCategory, String newsDate, String newsUrl) {
         mNewsTitle = newsTitle;
         mNewsCategory = newsCategory;
         mNewsDate= newsDate;
         mNewsUrl= newsUrl;
    }

    public String getNewsTitle() {
        return mNewsTitle;
    }

    public String getNewsCategory() {
        return mNewsCategory;
    }

    public String getNewsDate() {
        return mNewsDate;
    }

    public String getNewsUrl() {
        return mNewsUrl;
    }

    public int getImgResourceId() {
        return mImgResourceId;
    }

    public boolean hasImage() {
        return mImgResourceId != NO_IMAGE_PROVIDED;
    }
}
