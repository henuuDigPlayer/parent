package com.data4truth.pi.model;

/**
 * @author: lindj
 * @date: 2019-06-24 19:16:46
 * @description:  
 */
public class GrayOrgId {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private String orgId;

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
     * @return org_id 
     */
    public String getOrgId() {
        return orgId;
    }

    /**
     * 
     *
     * @param orgId 
     */
    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }
}