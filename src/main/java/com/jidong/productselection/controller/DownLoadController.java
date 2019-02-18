package com.jidong.productselection.controller;

import com.jidong.productselection.dao.JdOrderMapper;
import com.jidong.productselection.entity.JdOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * @Author: LiuChong
 * @Date: 2019-02-18 16:52
 * @Description:
 */
@Controller
@Slf4j
public class DownLoadController {

    @Autowired
    private JdOrderMapper orderMapper;

    @Value("${PREVIEW_LOCATION}")
    private String PREVIEW_LOCATION;

    private static String PREVIEW_SUFFIX = ".EASM";

    @GetMapping("/order/downloadPreview/{orderId}")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response, @PathVariable("orderId") Integer orderId) throws UnsupportedEncodingException {


        JdOrder order = orderMapper.selectByPrimaryKey(orderId);
        //设置文件路径
        String path = PREVIEW_LOCATION + "/" + order.getOrderNumber() + "/" + order.getProductModel() + PREVIEW_SUFFIX;
        File file = new File(path);

        // 如果文件名存在，则进行下载
        if (file.exists()) {

            // 配置文件下载
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(order.getProductModel() + PREVIEW_SUFFIX, "UTF-8"));

            // 实现文件下载
            byte[] buffer = new byte[1024 * 1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                log.info("Download the preview successfully!");
            } catch (Exception e) {
                log.info("Download the preview failed!");
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
