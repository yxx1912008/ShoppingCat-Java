package cn.luckydeer.model.banner;

import java.io.Serializable;

public class BannerModel implements Serializable {

    /**  */
    private static final long serialVersionUID = -5139607738820224013L;

    //链接地址
    private String            bannerUrl;

    //图片地址
    private String            bannerImg;

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getBannerImg() {
        return bannerImg;
    }

    public void setBannerImg(String bannerImg) {
        this.bannerImg = bannerImg;
    }

}
