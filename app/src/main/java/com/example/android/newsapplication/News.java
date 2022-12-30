package com.example.android.newsapplication;

public class News {
    private String mNewsTitle;
    private String mNewsCategory;
    private String mNewsDate;
    private String mNewsUrl;
    private String fNameAuthor;
    private String lNameAuthor;
    private String mAuthor;

    public News(String newsTitle, String newsCategory, String newsDate, String newsUrl, String firstName, String lastName) {
         mNewsTitle = newsTitle;
         mNewsCategory = newsCategory;
         mNewsDate = newsDate;
         mNewsUrl = newsUrl;
         String author = "";
         author = author.concat(firstName + " " + lastName);
         mAuthor = author;
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

    public String getmAuthor() {
        return mAuthor;
    }

    public boolean hasAuthor() {
        if(mAuthor.isEmpty() || mAuthor == null) {
            return false;
        }
        else {
            return true;
        }
    }
}
