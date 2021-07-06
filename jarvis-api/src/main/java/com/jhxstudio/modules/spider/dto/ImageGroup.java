package com.jhxstudio.modules.spider.dto;

/**
 * 功能描述: todo
 *
 * 作者: westinyang
 * 日期: 2017-05-04 14:54:09
 */
public class ImageGroup {

    private String title;
    private String imgUrl;
    private String linkUrl;

    public ImageGroup() {
    }

    public ImageGroup(String title, String imgUrl, String linkUrl) {
        this.title = title;
        this.imgUrl = imgUrl;
        this.linkUrl = linkUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    @Override
    public String toString() {
        return "ImgGroup{" +
                "title='" + title + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                '}';
    }
}
