package com.example.mariakovaleva.mariasnewsapp;

public class News {

    private String mAuthorName;
    private String mPublicationDateAndTime;
    private String mWebTitle;
    private String mTrailText;
    private String mSectionId;
    private String mUrl;


    public News(String authorName, String publicationDateAndTime, String webTitle, String trailText, String sectionId, String url) {
        this.mAuthorName = authorName;
        this.mPublicationDateAndTime = publicationDateAndTime;
        this.mWebTitle = webTitle;
        this.mTrailText = trailText;
        this.mSectionId = sectionId;
        this.mUrl = url;
    }

    public String getAuthorName() {
        return mAuthorName;
    }

    public String getPublicationDateAndTime() {
        return mPublicationDateAndTime;
    }

    public String getWebTitle() {
        return mWebTitle;
    }

    public String getTrailText() {
        return mTrailText;
    }

    public String getSectionId() {
        return mSectionId;
    }

    public String getUrl() {
        return mUrl;
    }
}
