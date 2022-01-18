package com.f4n.blog.controller;

import com.f4n.blog.utils.QiniuUtils;
import com.f4n.blog.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("upload")
public class UploadController {
    @Autowired
    private QiniuUtils qiniuUtils;
    @PostMapping
    public Result upload(@RequestParam("image") MultipartFile file) {
        // 获得前端传过来的文件名称
        String originalFilename = file.getOriginalFilename();
        // 将前端的文件名 转换为后端存的文件名 要转换成唯一的文件
        String fileName = UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(originalFilename, ".");
        /*
         *  文件上传的位置
         *  如果的传输也发送到当前的应用服务器,那么会非常占用带宽和内存
         *  一般会把图片转发的专用的图片服务器,减轻应用服务器的负担
         *  这里使用七牛云网络服务器
         * */
        boolean upload = qiniuUtils.upload(file, fileName);
        if (upload){
            return Result.success(QiniuUtils.url+fileName);
        }


        return Result.fail(20001,"上传失败");
    }
}
