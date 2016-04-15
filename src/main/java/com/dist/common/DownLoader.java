package com.dist.common;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.filenet.api.collection.ContentElementList;
import com.filenet.api.core.CmThumbnail;
import com.filenet.api.core.ContentTransfer;

public class DownLoader {

    /**
     *  浏览器下载
     * @param request
     * @param response
     * @param downLoadPath  下载的地址
     * @param fileName  文件名
     */
    public static void downLoadByOutputStrem(HttpServletRequest request, HttpServletResponse response, InputStream is,
            String fileName) {

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {

            response.setContentType("contentType");
            response.setHeader("Content-disposition",
                    "attachment;filename=" + encodeDownloadFilename(fileName, request.getHeader("user-agent")));
            //            response.setHeader("Content-Length", String.valueOf(fileLength));
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(response.getOutputStream());

            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(bis, bos);
        }
    }

    public static void writeImage(HttpServletRequest request, HttpServletResponse response, CmThumbnail cb) {
        BufferedOutputStream bos = null;
        if(null != cb && null != cb.get_Image()){
            try {
                
                response.setContentType(cb.get_MimeType());
                response.setHeader("Content-disposition", "attachment");
                bos = new BufferedOutputStream(response.getOutputStream());
                bos.write(cb.get_Image());
                
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeStream(null, bos);
            }
        }
    }

    /**
     * 下载文件时，针对不同浏览器，进行附件名的编码
     * 
     * @param filename
     *            下载文件名
     * @param agent
     *            客户端浏览器
     * @return 编码后的下载附件名
     * @throws IOException
     */
    public static String encodeDownloadFilename(String filename, String agent) throws IOException {

        if (!StringUtil.isNullOrEmpty(filename)) {

            if (agent.contains("Firefox")) { // 火狐浏览器
                //                filename = "=?UTF-8?B?" + new BASE64Encoder().encode(filename.getBytes("utf-8")) + "?=";
                filename = new String(filename.getBytes("utf-8"), "ISO8859-1");
            } else { // IE及其他浏览器
                filename = URLEncoder.encode(filename, "utf-8");
            }
        } else {
            throw new RuntimeException("没有附件！");
        }
        return filename;
    }

    /**
     * 关闭缓冲流
     * @param bis
     * @param bos
     */
    public static void closeStream(BufferedInputStream bis, BufferedOutputStream bos) {
        if (bis != null) {
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void downLoadByOutputStrem(HttpServletRequest request, HttpServletResponse response,String fileName,
            ContentTransfer ct) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {

            response.setContentType("contentType");
            response.setHeader(
                    "Content-disposition",
                    "attachment;filename="
                            + encodeDownloadFilename(fileName, request.getHeader("user-agent")));
            response.setHeader("Content-Length", String.valueOf(ct.get_ContentSize()));
            bis = new BufferedInputStream(ct.accessContentStream());
            bos = new BufferedOutputStream(response.getOutputStream());

            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(bis, bos);
        }
    }

}
