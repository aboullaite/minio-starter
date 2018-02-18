package me.aboullaite.minio.vo;

import io.minio.ObjectStat;

import java.util.Date;

public class MinioObject {

    private String bucketName;
    private String name;
    private Date createdTime;
    private long length;
    private String etag;
    private String contentType;
    private String matDesc;

    public MinioObject(String bucketName, String name, Date createdTime, long length, String etag, String contentType, String matDesc) {
        this.bucketName = bucketName;
        this.name = name;
        this.createdTime = createdTime;
        this.length = length;
        this.etag = etag;
        this.contentType = contentType;
        this.matDesc = matDesc;
    }

    public MinioObject(ObjectStat os) {
        this.bucketName = os.bucketName();
        this.name = os.name();
        this.createdTime = os.createdTime();
        this.length = os.length();
        this.etag = os.etag();
        this.contentType = os.contentType();
        this.matDesc = os.matDesc();
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getName() {
        return name;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public long getLength() {
        return length;
    }

    public String getEtag() {
        return etag;
    }

    public String getContentType() {
        return contentType;
    }

    public String getMatDesc() {
        return matDesc;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setMatDesc(String matDesc) {
        this.matDesc = matDesc;
    }
}
