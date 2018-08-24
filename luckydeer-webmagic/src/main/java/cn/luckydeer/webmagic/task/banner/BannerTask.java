package cn.luckydeer.webmagic.task.banner;

import java.io.Serializable;

import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.TargetUrl;
import cn.luckydeer.webmagic.constants.WebmagicConstant;
import cn.luckydeer.webmagic.constants.XpathConstant;

/**
 * 首页海报 任务
 * 
 * @author yuanxx
 * @version $Id: IndexPosterModel.java, v 0.1 2018年6月22日 上午10:49:01 yuanxx Exp $
 */
@TargetUrl(value = WebmagicConstant.CAT_HOST)
@ExtractBy(value = XpathConstant.INDEX_POSTER_TARGET_XPATH, multi = true)
public class BannerTask implements Serializable {

    /**  */
    private static final long serialVersionUID = -7837892347071317232L;

    /**  图片Url地址 */
    @ExtractBy(value = XpathConstant.INDEX_POSTER_IMG_XPATH)
    private String            imgUrl;

    /** 图片链接的Url地址  */
    @ExtractBy(value = XpathConstant.INDEX_POSTER_TARGET_URL_XPATH)
    private String            targetUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

}
