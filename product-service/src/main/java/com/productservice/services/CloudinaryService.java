package com.productservice.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.productservice.exception.AppException;
import com.productservice.exception.ErrorCode;
import com.productservice.models.Variant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class CloudinaryService  {
    private final Cloudinary cloudinary;

    public List<String> uploadFiles(MultipartFile[] files, String name){
        List<String> strListImages = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            String url = upload(files[i], name, i);
            strListImages.add(url);
        }
        return strListImages;
    }

    public void deleteImages(List<String> urlsToDelete, Variant variant) {
        try {

            for (String url : urlsToDelete) {
                String publicId = extractPublicId(url);
                boolean deleted = deleteImage(publicId);

                if (deleted) {
                    variant.getImageList().remove(url);
                } else {
                    throw new AppException(ErrorCode.PRODUCT_IMAGE_DELETE_FAILED);
                }
            }

        } catch (AppException ae) {
            throw ae;
        } catch (Exception e) {
            throw new AppException(ErrorCode.PRODUCT_IMAGE_PROCESSING_ERROR);
        }
    }

    private String upload(MultipartFile file, String productName, int index) {
        try {
            String cleanName = productName.replaceAll("\\s+", "_").toLowerCase();
            String publicId = "products/" + cleanName + "_" + index;

            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), Map.of(
                    "public_id", publicId,
                    "resource_type", "auto",
                    "overwrite", true // nếu trùng sẽ ghi đè
            ));

            return (String) result.get("secure_url");
        } catch (IOException e) {
            throw new AppException(ErrorCode.PRODUCT_IMAGE_UPLOAD_FAILED);
        }
    }

    private boolean deleteImage(String publicId) {
        try {
            Map<?, ?> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            String status = (String) result.get("result");
            return "ok".equals(status);
        } catch (IOException e) {
            System.out.println("Xoá ảnh thất bại: " + publicId + e.getMessage());
            throw new AppException(ErrorCode.PRODUCT_IMAGE_DELETE_FAILED);
        }
    }

    private String extractPublicId(String url) {
        try {
            URI uri = new URI(url);
            String path = uri.getPath();  // /image/upload/v1752139171/products/nokia_0.png
            String[] parts = path.split("/");

            // Tìm vị trí "upload"
            int uploadIdx = Arrays.asList(parts).indexOf("upload");

            // Phần sau "upload/"
            List<String> subParts = new ArrayList<>(Arrays.asList(parts).subList(uploadIdx + 1, parts.length));

            // Nếu phần đầu sau upload là version kiểu "v123456789", thì loại bỏ
            if (!subParts.isEmpty() && subParts.get(0).matches("^v\\d+$")) {
                subParts.remove(0);
            }

            // Ghép lại các phần còn lại: ví dụ ["products", "nokia_0.png"]
            String publicIdWithExt = String.join("/", subParts);

            // Bỏ phần mở rộng (.png, .jpg...)
            return publicIdWithExt.replaceFirst("\\.[^.]+$", "");

        } catch (Exception e) {
            System.out.println("Extract publicId error from url: " + url);
            throw new AppException(ErrorCode.PRODUCT_IMAGE_DELETE_FAILED);
        }
    }
}
