package com.dist.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件相关工具类
 * @author  heshun
 * @version 1.0 2013-12-20 heshun	发布
 */
public class FileUtil {

    /**
     * 还原空格，在使用file流的时候，如果路径中有空格，会被转化成“%20”
     */
    public static String reductionBlank(String str) {
        return str.replace("%20", " ");
    }

    /**
     *  上传文件，另外起线程
     * @throws IOException 
     */
    public static void upload(String path, String name, InputStream in) throws IOException {
        Thread uploadThread = new Thread(new UploadThread(path, name, in));
        uploadThread.start();
    }

}

class UploadThread implements Runnable {
    String path;
    String name;
    InputStream in;

    public UploadThread(String path, String name, InputStream in) {
        this.path = path;
        this.name = name;
        this.in = in;
    }

    public void run() {
        File folder = new File(path);
        FileOutputStream fos = null;
        //若没有文件夹，就建立。
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File file = new File(new StringBuffer().append(path).append("/").append(name).toString());
        //若没有文件，就建立。
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            fos = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = in.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(null != fos){
                    in.close();
                    fos.flush();
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
