package me.aboullaite.minio.http;

import io.minio.errors.*;
import io.minio.messages.Bucket;
import me.aboullaite.minio.service.MinioTemplate;
import me.aboullaite.minio.vo.MinioItem;
import me.aboullaite.minio.vo.MinioObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConditionalOnProperty(name = "minio.endpoint.enable", havingValue = "true")
@RestController
@RequestMapping("${minio.endpoint.name:/minio}")
public class MinioEndpoint {

    @Autowired
    private MinioTemplate template;
    private static final Integer ONE_DAY = 60 * 60 * 24;

    /**
     *
     * Bucket Endpoints
     */
    @PostMapping("/bucket/{bucketName}")
    public Bucket createBucker(@PathVariable String bucketName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidPortException, ErrorResponseException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, RegionConflictException {

        template.createBucket(bucketName);
        return template.getBucket(bucketName).get();

    }

    @GetMapping("/bucket")
    public List<Bucket> getBuckets() throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidPortException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, InternalException {
            return template.getAllBuckets();
    }

    @GetMapping("/bucket/{bucketName}")
    public Bucket getBucket(@PathVariable String bucketName) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidPortException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, InternalException {
        return template.getBucket(bucketName).orElseThrow(() -> new IllegalArgumentException("Bucket Name not found!"));
    }

    @DeleteMapping("/bucket/{bucketName}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteBucket(@PathVariable String bucketName) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidPortException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, InternalException {

            template.removeBucket(bucketName);
    }

    /**
     *
     * Object Endpoints
     */

    @PostMapping("/object/{bucketName}")
    public MinioObject createObject(@RequestBody MultipartFile object, @PathVariable String bucketName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidPortException, ErrorResponseException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, RegionConflictException, InvalidArgumentException {
        String name = object.getOriginalFilename();
        template.saveObject(bucketName, name, object.getInputStream(), object.getSize(), object.getContentType());
        return new MinioObject(template.getObjectInfo(bucketName, name));

    }

    @PostMapping("/object/{bucketName}/{objectName}")
    public MinioObject createObject(@RequestBody MultipartFile object, @PathVariable String bucketName, @PathVariable String objectName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidPortException, ErrorResponseException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, RegionConflictException, InvalidArgumentException {
        template.saveObject(bucketName, objectName, object.getInputStream(), object.getSize(), object.getContentType());
        return new MinioObject(template.getObjectInfo(bucketName, objectName));

    }


    @GetMapping("/object/{bucketName}/{objectName}")
    public  List<MinioItem>  filterObject(@PathVariable String bucketName, @PathVariable String objectName) throws InvalidPortException, InvalidEndpointException {

        return template.getAllObjectsByPrefix(bucketName, objectName, true);

    }

    @GetMapping("/object/{bucketName}/{objectName}/{expires}")
    public Map<String, Object> getObject( @PathVariable String bucketName, @PathVariable String objectName, @PathVariable Integer expires) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidPortException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, InternalException, InvalidExpiresRangeException {
        Map<String,Object> responseBody = new HashMap<>();
        // Put Object info
        responseBody.put("bucket" , bucketName);
        responseBody.put("object" , objectName);
        responseBody.put("url" , template.getObjectURL(bucketName, objectName, expires));
        responseBody.put("expires" ,  expires);
        return  responseBody;
    }

    @DeleteMapping("/object/{bucketName}/{objectName}/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteObject(@PathVariable String bucketName, @PathVariable String objectName) throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidPortException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InvalidEndpointException, InternalException, InvalidArgumentException {

        template.removeObject(bucketName, objectName);
    }


}
