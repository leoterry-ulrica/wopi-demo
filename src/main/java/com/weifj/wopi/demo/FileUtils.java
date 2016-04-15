package com.weifj.wopi.demo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Iterator;

import com.dist.common.Util;
import com.filenet.api.collection.ContentElementList;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.core.ContentElement;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.util.Id;
import com.filenet.apiimpl.core.ContentTransferImpl;
import com.filenet.apiimpl.property.ClientInputStream;

public class FileUtils {

	/**
     * 获取文件基本信息
     * @param filePath      文件路径
     * @return
     */
    public static String checkFileInfo(String filePath) {
            File file = new File(filePath);
            String baseFileName = null;     //文件名
            String ownerId = null;  //文件所有者的唯一编号
            long size = 0;  //文件大小，以bytes为单位
            //String sha256 = null; //文件的256位bit的SHA-2编码散列内容
            long version = 0;       //文件版本号，文件如果被编辑，版本号也要跟着改变
            if(file.exists()) {
                    // 取得文件名。
                    baseFileName = file.getName();
                    size = file.length();
                    // 取得文件的后缀名。
                    //String ext = baseFileName.substring(baseFileName.lastIndexOf(".") + 1);
                    ownerId = "admin";
                    //sha256 = new SHAUtils().SHA(FileUtils.readByByte(file), "SHA-256");
                    version = file.lastModified();
            }

            return "{\"BaseFileName\":\"" + baseFileName + "\",\"OwnerId\":\"" + ownerId + "\",\"Size\":\"" + size
                            + "\",\"AllowExternalMarketplace\":\"" + true + "\",\"Version\":\"" + version + "\"}";
    }
    
    public static String checkFileInfoById(String fileId) {
    	
    	Document doc = getDocument(fileId);

        String baseFileName = doc.get_Name();     //文件名
        String ownerId =  "admin"; //文件所有者的唯一编号
        double size = doc.get_ContentSize();  //文件大小，以bytes为单位
        long version = doc.get_DateLastModified().getTime();       //文件版本号，文件如果被编辑，版本号也要跟着改变

        return "{\"BaseFileName\":\"" + baseFileName + "\",\"OwnerId\":\"" + ownerId + "\",\"Size\":\"" + size
                        + "\",\"AllowExternalMarketplace\":\"" + true + "\",\"Version\":\"" + version + "\"}";
}

	public static Document getDocument(String fileId) {
		ObjectStore os = Util.getObjectStore("NewOS");
		Document doc = Factory.Document.fetchInstance(os, new Id(fileId), null);

		return doc;
	}
	
	public static InputStream getDocumentStream(String fileId) {
		ObjectStore os = Util.getObjectStore("NewOS");
		Document doc = Factory.Document.fetchInstance(os, new Id(fileId), null);
		ContentElementList contents = doc.get_ContentElements();
		
		Iterator it = contents.iterator();
		while(it.hasNext()){
			ContentTransfer ct = (ContentTransfer) it.next();
			InputStream is = ct.accessContentStream();
			return is;
		
		}
		return null;
	}
	
	public static InputStream getDocumentStream(Document doc) {
	
		ContentElementList contents = doc.get_ContentElements();
		
		Iterator it = contents.iterator();
		while(it.hasNext()){
	
			ContentTransfer ct = (ContentTransfer) it.next();

			InputStream is = ct.accessContentStream();
			
			return is;
		
		}
		return null;
	}
	  public static void view1(ObjectStore os, String ID) throws IOException {
	        // Get document and populate property cache.
	        PropertyFilter pf = new PropertyFilter();
	        pf.addIncludeProperty(new FilterElement(null, null, null, PropertyNames.CONTENT_SIZE, null));
	        pf.addIncludeProperty(new FilterElement(null, null, null, PropertyNames.CONTENT_ELEMENTS, null));
	        Document doc = Factory.Document.fetchInstance(os, ID, pf);

	        // Print information about content elements.
	        System.out.println("No. of document content elements: " + doc.get_ContentElements().size() + "\n"
	                + "Total size of content: " + doc.get_ContentSize() + "\n");

	        // Get content elements and iterate list.
	        ContentElementList docContentList = doc.get_ContentElements();
	        Iterator iter = docContentList.iterator();
	        while (iter.hasNext()) {
	            ContentTransfer ct = (ContentTransfer) iter.next();

	            // Print element sequence number and content type of the element.
	            System.out.println("\nElement Sequence number: " + ct.get_ElementSequenceNumber().intValue() + "\n"
	                    + "Content type: " + ct.get_ContentType() + "\n");

	            // Get and print the content of the element.
	            InputStream stream = ct.accessContentStream();
	            ClientInputStream clientStream = (ClientInputStream)stream;
	      
	            System.out.println("InputStream文件流大小："+ stream.available());
	            System.out.println("ClientInputStream文件流大小："+ clientStream.getTotalSize());
	            System.out.println("clientStream.getContentAsStream()文件流大小："+ clientStream.getContentAsStream().available());
	         
	            String readStr = "";
	            try {
	                int docLen = 1024;
	                byte[] buf = new byte[docLen];
	                int n = 1;
	                while (n > 0) {
	                    n = stream.read(buf, 0, docLen);
	                    readStr = readStr + new String(buf,"utf-8");
	                    buf = new byte[docLen];
	                }
	                System.out.println("Content:\n " + readStr);
	                stream.close();
	            } catch (IOException ioe) {
	                ioe.printStackTrace();
	            }
	        }
	    }
	public static byte[] getDocumentContentByte(Document doc) {
		
		ContentElementList contents = doc.get_ContentElements();
		
		Iterator it = contents.iterator();
		while(it.hasNext()){
			ContentTransferImpl ct = (ContentTransferImpl) it.next();
			return ct.getContent();
		
		}
		return null;
	}
	
	public static void main(String[]args) throws IOException{
		
		//FileUtils.view1(Util.getObjectStore("NewOS"), "{6F532108-35A3-47E3-A8AF-36D56D88D00B}");
		Document doc = FileUtils.getDocument("{6F532108-35A3-47E3-A8AF-36D56D88D00B}");
		
		System.out.println("int :"+doc.get_ContentSize().intValue());
	}
	
    
    public static byte[] readByByte(File file) {
    	try{
    	InputStream fis = new BufferedInputStream(new FileInputStream(file.getAbsolutePath()));
		byte[] buffer = new byte[fis.available()];
		fis.read(buffer);
		fis.close();
		
		return buffer;
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	return null;
    }
    
    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }
    
}
