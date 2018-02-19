package me.aboullaite.minio.service;

import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import me.aboullaite.minio.vo.MinioItem;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class MinioTemplate {

    private String endpoint, accessKey, secretKey;

    public MinioTemplate() {
    }

    /**
     * Create new instance of the {@link MinioTemplate} with the access key and secret key.
     * @param endpoint minio URL, it should be a  URL, domain name, IPv4 address or IPv6 address
     * @param accessKey uniquely identifies a minio account.
     * @param secretKey the password to a minio account.
     */
    public MinioTemplate(String endpoint, String accessKey, String secretKey) {
        this.endpoint = endpoint;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    /**
     * Bucket Operations
     */

    public void createBucket(String bucketName) throws XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidPortException, InvalidEndpointException, RegionConflictException, NoResponseException, InternalException, ErrorResponseException, InsufficientDataException, InvalidBucketNameException {
        MinioClient client = getMinioClient();
        if(!client.bucketExists(bucketName)){
            client.makeBucket(bucketName);
        }
    }

    public List<Bucket> getAllBuckets() throws InvalidPortException, InvalidEndpointException, IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        return getMinioClient().listBuckets();
    }

    public Optional<Bucket> getBucket(String bucketName) throws InvalidPortException, InvalidEndpointException, IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        return getMinioClient().listBuckets().stream().filter( b -> b.name().equals(bucketName)).findFirst();
    }

    public void removeBucket(String bucketName) throws InvalidPortException, InvalidEndpointException, IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        getMinioClient().removeBucket(bucketName);
    }

    public List<MinioItem> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) throws InvalidPortException, InvalidEndpointException{
        List objectList = new ArrayList();
        Iterable<Result<Item>> objectsIterator = getMinioClient().listObjects(bucketName, prefix, recursive);
        objectsIterator.forEach(i -> {
            try {
                objectList.add(new MinioItem(i.get()));
            } catch (Exception e) {
                new Exception(e);
            }
        });
        return objectList;
    }

    /**
     * Object operations
     */

    public String getObjectURL(String bucketName, String objectName, Integer expires) throws InvalidPortException, InvalidEndpointException, IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidExpiresRangeException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        return getMinioClient().presignedGetObject(bucketName, objectName, expires);
    }

    public void saveObject(String bucketName, String objectName, InputStream stream, long size, String contentType) throws InvalidPortException, InvalidEndpointException, IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        getMinioClient().putObject(bucketName, objectName, stream, size, contentType);
    }

    public ObjectStat getObjectInfo(String bucketName, String objectName) throws InvalidPortException, InvalidEndpointException, IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        return getMinioClient().statObject(bucketName, objectName);
    }

    public void removeObject(String bucketName, String objectName ) throws InvalidPortException, InvalidEndpointException, IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, InvalidArgumentException {
        getMinioClient().removeObject(bucketName, objectName);
    }


    /**
     * Gets a Minio client
     *
     * @return an authenticated Amazon S3 client
     */
    public MinioClient getMinioClient() throws InvalidPortException, InvalidEndpointException {
        return new MinioClient(endpoint, accessKey, secretKey);
    }
}
