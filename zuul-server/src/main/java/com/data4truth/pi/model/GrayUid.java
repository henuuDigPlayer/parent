package com.data4truth.pi.model;

/**
 * @author: lindj
 * @date: 2019-06-24 19:16:46
 * @description:  
 */
public class GrayUid {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private String uid;

    /**
     * 
     *
     * @return id 
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     *
     * @param id 
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     *
     * @return uid 
     */
    public String getUid() {
        return uid;
    }

    /**
     * 
     *
     * @param uid 
     */
    public void setUid(String uid) {
        this.uid = uid == null ? null : uid.trim();
    }
}