package com.bsjhx.cashflow.domain.bucket.exception;

public class BucketExceptionReasons {
    
    public static final String BUCKET_ALREADY_OPENED =  "Bucket can't ne opened once it was opened";
    public static final String BUCKET_NOT_OPENED =  "Bucket is not opened";
    public static final String BUCKET_ID_NOT_MATCHED = "Bucket id is not matched to provided in event";
}
