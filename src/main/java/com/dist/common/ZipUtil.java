package com.dist.common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.filenet.api.collection.ContentElementList;
import com.filenet.api.core.ContentElement;
import com.filenet.api.core.ContentTransfer;

public class ZipUtil {

    private static ZipOutputStream zipOut;
    static byte[] buf = new byte[1024];
    static int readedBytes = 0;
    static private ZipFile zipFile;

    public static void downloadZip(String zipDirectory, HttpServletResponse response, HttpServletRequest request) {
        File zipDir = new File(zipDirectory);
        String zipName = zipDir.getName(); 
        try {
            
            response.setContentType("contentType");
            response.setHeader("Content-disposition",
                    "attachment;filename=" + DownLoader.encodeDownloadFilename(zipName, request.getHeader("user-agent")));
            
            zipOut = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
            // 压缩文件  
            handleDir(zipDir, zipDir.getParent().length() + 1, zipOut);
            zipOut.finish();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            try {
                zipOut.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    /** 
     * 压缩文件(.zip)的函数  
     * @param zipDirectory:(需要)压缩的文件夹路径 
     * @param zipPath:文件压缩后放置的路径,该路径可以为null,null表示压缩到原文件的同级目录 
     * @return :返回一个压缩好的文件(File),否则返回null 
     */
    public static File doZip(String zipDirectory, String zipPath) {
        File zipDir = new File(zipDirectory);

        if (zipPath == null) {
            zipPath = zipDir.getParent();
        }

        // 压缩后生成的zip文件名  
        String zipFileName = zipPath + "/" + zipDir.getName() + ".zip";

        try {
            zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFileName)));

            // 压缩文件  
            handleDir(zipDir, zipDir.getParent().length() + 1, zipOut);

            zipOut.close();
            return new File(zipFileName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /** 
     * 由doZip调用,递归完成目录文件读取 
     * @param dir:(需要)压缩的文件夹(File 类型) 
     * @param len:一个参数(记录压缩文件夹的parent路径的长度) 
     * @param zipOut:需要压缩进的压缩文件 
     * @throws IOException:如果出错,会抛出IOE异常 
     */
    private static void handleDir(File dir, int len, ZipOutputStream zipOut) throws IOException {
        FileInputStream fileIn = null;
        File[] files = dir.listFiles();

        if (files != null) {
            if (files.length > 0) { // 如果目录不为空,则分别处理目录和文件.  
                for (File fileName : files) {

                    if (fileName.isDirectory()) {
                        handleDir(fileName, len, zipOut);

                    } else {
                        fileIn = new FileInputStream(fileName);

                        zipOut.putNextEntry(new ZipEntry(fileName.getPath().substring(len).replaceAll("\\\\", "/")));
                        while ((readedBytes = fileIn.read(buf)) > 0) {
                            zipOut.write(buf, 0, readedBytes);
                        }
                        zipOut.closeEntry();
                    }
                }
            } else { // 如果目录为空,则单独创建之.  
                zipOut.putNextEntry(new ZipEntry(dir.getPath().substring(len) + "/"));
                zipOut.closeEntry();
            }
        } else {// 如果是一个单独的文件  
            fileIn = new FileInputStream(dir);

            zipOut.putNextEntry(new ZipEntry(dir.getPath().substring(len)));

            while ((readedBytes = fileIn.read(buf)) > 0) {
                zipOut.write(buf, 0, readedBytes);
            }
            zipOut.closeEntry();
        }
    }

    /** 
     * 解压指定zip文件 
     * @param unZipfileName:需要解压的zip文件路径 
     * @param unZipPath:文件解压的路径,该路径可以为null,null表示解压到原文件的同级目录 
     */
    public static boolean unZip(String unZipfileName, String unZipPath) {// unZipfileName需要解压的zip文件名  
        FileOutputStream fileOut = null;
        InputStream inputStream = null;
        File file = null;

        if (unZipPath == null) {
            unZipPath = new File(unZipfileName).getParent();
            // System.out.println("1 -> " + unZipPath);  

            if (!(unZipPath.substring(unZipPath.length()).endsWith("/") || unZipPath.substring(unZipPath.length())
                    .endsWith("\\"))) {
                unZipPath += "/";
            }

        } else {
            unZipPath = new File(unZipPath).getPath() + "/";
        }

        try {
            zipFile = new ZipFile(unZipfileName);

            for (Enumeration<?> entries = zipFile.entries(); entries.hasMoreElements();) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                int iTEMP = entry.getName().indexOf("/") + 1;

                file = new File(unZipPath + entry.getName().substring(iTEMP, entry.getName().length()));

                if (entry.isDirectory()) {
                    file.mkdirs();
                } else {
                    // 如果指定文件的目录不存在,则创建之.  
                    File parent = file.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }

                    inputStream = zipFile.getInputStream(entry);

                    fileOut = new FileOutputStream(file);
                    while ((readedBytes = inputStream.read(buf)) > 0) {
                        fileOut.write(buf, 0, readedBytes);
                    }
                    fileOut.close();

                    inputStream.close();
                }
            }
            zipFile.close();
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }

    public static void downloadZip(ContentElementList elementList, HttpServletRequest request,
            HttpServletResponse response, String docTitle) {
        
        try {
            
            response.setContentType("contentType");
            response.setHeader("Content-disposition",
                    "attachment;filename=" + DownLoader.encodeDownloadFilename(docTitle, request.getHeader("user-agent")));
            
            zipOut = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
            // 压缩文件  
            handleElementList(elementList, zipOut);
            zipOut.finish();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            try {
                zipOut.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private static void handleElementList(ContentElementList elementList, ZipOutputStream zipOut) throws IOException {
        Iterator itElement = elementList.iterator();
        while(itElement.hasNext()){
            ContentTransfer ct = (ContentTransfer) itElement.next();
            InputStream contentStream = ct.accessContentStream();
//            ZipEntry zipEntry = new ZipEntry(ct.get_RetrievalName());
            while ((readedBytes = contentStream.read(buf)) > 0) {
                zipOut.write(buf, 0, readedBytes);
            }
            contentStream.close();
        }
        
        
    }
}
