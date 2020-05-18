package com.talkingdata.framework.gateway.entity;

import java.util.Date;

/**
 * token实体类
 * @author tao.yang
 * @date 2019-11-15
 */
public class TokenEntity {

    /**
     * token
     */
    private String token;

    /**
     * 过期时间
     */
    private Date expire;


    public TokenEntity(String token, Date expire){
        this.token = token;
        this.expire = expire;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }
}
