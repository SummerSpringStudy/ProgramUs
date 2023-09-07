package com.pu.programus.GCS;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class GCSController {

    private final GCSService gcsService;

    @PostMapping("gcs/upload")
    public ResponseEntity<?> uploadToStorage(@RequestBody UploadReqDto uploadReqDto) throws IOException {
        BlobInfo blobInfo = gcsService.uploadImage(uploadReqDto);
        return ResponseEntity.ok(blobInfo.toString());
    }
}