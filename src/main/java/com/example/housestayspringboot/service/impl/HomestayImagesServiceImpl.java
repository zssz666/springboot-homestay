package com.example.housestayspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.housestayspringboot.entity.HomestayImages;
import com.example.housestayspringboot.mapper.HomestayImagesMapper;
import com.example.housestayspringboot.service.HomestayImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class HomestayImagesServiceImpl implements HomestayImagesService {
    @Autowired
    private HomestayImagesMapper homestayImagesMapper;

    @Override
    public List<HomestayImages> findByHomestayId(Integer homestayId) {
        LambdaQueryWrapper<HomestayImages> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HomestayImages::getHomestayId, homestayId)
               .orderByAsc(HomestayImages::getSortOrder);
        return homestayImagesMapper.selectList(wrapper);
    }

    @Override
    @Transactional
    public void saveImages(Integer homestayId, List<String> imageUrls) {
        LambdaQueryWrapper<HomestayImages> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HomestayImages::getHomestayId, homestayId);
        homestayImagesMapper.delete(wrapper);

        for (int i = 0; i < imageUrls.size(); i++) {
            HomestayImages img = new HomestayImages();
            img.setHomestayId(homestayId);
            img.setImageUrl(imageUrls.get(i));
            img.setSortOrder(i);
            homestayImagesMapper.insert(img);
        }
    }

    @Override
    @Transactional
    public void deleteByHomestayId(Integer homestayId) {
        LambdaQueryWrapper<HomestayImages> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HomestayImages::getHomestayId, homestayId);
        homestayImagesMapper.delete(wrapper);
    }
}
