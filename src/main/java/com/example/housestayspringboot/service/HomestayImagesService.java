package com.example.housestayspringboot.service;

import com.example.housestayspringboot.entity.HomestayImages;
import java.util.List;

public interface HomestayImagesService {
    List<HomestayImages> findByHomestayId(Integer homestayId);
    void saveImages(Integer homestayId, List<String> imageUrls);
    void deleteByHomestayId(Integer homestayId);
}
