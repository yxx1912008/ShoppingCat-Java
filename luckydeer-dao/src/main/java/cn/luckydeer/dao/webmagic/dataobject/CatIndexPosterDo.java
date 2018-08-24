package cn.luckydeer.dao.webmagic.dataobject;

import java.util.Date;

/**
 * 
 * 购物猫海报
 * @author yuanxx
 * @version $Id: CatIndexPosterDo.java, v 0.1 2018年6月22日 下午3:09:17 yuanxx Exp $
 */
public class CatIndexPosterDo {

    /**  海报内部ID */
    private Integer id;
    /**  海报图片地址 */
    private String  imgUrl;
    /**  海报链接地址 */
    private String  targetUrl;
    /**  创建时间 */
    private Date    gmtCreate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}