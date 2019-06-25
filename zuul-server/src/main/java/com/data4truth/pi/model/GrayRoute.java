package com.data4truth.pi.model;

/**
 * @author: lindj
 * @date: 2019-06-25 10:39:51
 * @description:  
 */
public class GrayRoute {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private String serverId;

    /**
     * 
     */
    private String currentVersion;

    /**
     * 
     */
    private String grayVersion;

    /**
     * 
     */
    private Boolean enableGray;

    /**
     * 
     */
    private Boolean enableGrayIp;

    /**
     * 
     */
    private Boolean enableGrayUid;

    /**
     * 
     */
    private Boolean enableGrayOrgId;

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
     * @return server_id 
     */
    public String getServerId() {
        return serverId;
    }

    /**
     * 
     *
     * @param serverId 
     */
    public void setServerId(String serverId) {
        this.serverId = serverId == null ? null : serverId.trim();
    }

    /**
     * 
     *
     * @return current_version 
     */
    public String getCurrentVersion() {
        return currentVersion;
    }

    /**
     * 
     *
     * @param currentVersion 
     */
    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion == null ? null : currentVersion.trim();
    }

    /**
     * 
     *
     * @return gray_version 
     */
    public String getGrayVersion() {
        return grayVersion;
    }

    /**
     * 
     *
     * @param grayVersion 
     */
    public void setGrayVersion(String grayVersion) {
        this.grayVersion = grayVersion == null ? null : grayVersion.trim();
    }

    /**
     * 
     *
     * @return enable_gray 
     */
    public Boolean getEnableGray() {
        return enableGray;
    }

    /**
     * 
     *
     * @param enableGray 
     */
    public void setEnableGray(Boolean enableGray) {
        this.enableGray = enableGray;
    }

    /**
     * 
     *
     * @return enable_gray_ip 
     */
    public Boolean getEnableGrayIp() {
        return enableGrayIp;
    }

    /**
     * 
     *
     * @param enableGrayIp 
     */
    public void setEnableGrayIp(Boolean enableGrayIp) {
        this.enableGrayIp = enableGrayIp;
    }

    /**
     * 
     *
     * @return enable_gray_uid 
     */
    public Boolean getEnableGrayUid() {
        return enableGrayUid;
    }

    /**
     * 
     *
     * @param enableGrayUid 
     */
    public void setEnableGrayUid(Boolean enableGrayUid) {
        this.enableGrayUid = enableGrayUid;
    }

    /**
     * 
     *
     * @return enable_gray_org_id 
     */
    public Boolean getEnableGrayOrgId() {
        return enableGrayOrgId;
    }

    /**
     * 
     *
     * @param enableGrayOrgId 
     */
    public void setEnableGrayOrgId(Boolean enableGrayOrgId) {
        this.enableGrayOrgId = enableGrayOrgId;
    }
}