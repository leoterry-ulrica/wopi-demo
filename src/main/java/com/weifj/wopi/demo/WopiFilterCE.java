package com.weifj.wopi.demo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.filenet.api.core.Document;
import com.filenet.apiimpl.property.ClientInputStream;

@WebFilter("/wopi/*")
public class WopiFilterCE implements Filter {


	private void getFile(String fileId, HttpServletResponse response) {
		try {
			String contentType = "application/octet-stream";
			Document doc = FileUtils.getDocument(fileId);
			// 以流的形式下载文件。
			InputStream fis =  FileUtils.getDocumentStream(doc);
			 ClientInputStream clientStream = (ClientInputStream)fis;
			System.out.println("文件流大小："+doc.get_ContentSize().intValue());//clientStream.getTotalSize()
			byte[] buffer = new byte[doc.get_ContentSize().intValue()];//不能使用fis.available()，这个方法获取的大小并不是源文件大小
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition",
					"attachment;filename=" + new String(doc.get_Name().getBytes("utf-8"), "ISO-8859-1"));
			response.addHeader("Content-Length", "" + doc.get_ContentSize());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());

			response.setContentType(contentType);
			toClient.write(buffer);
			toClient.flush();
			toClient.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		//return response;
	}
	
	private void getFileByPath(String path, HttpServletResponse response) {
		try {
	
			File file = new File(path);
			
			String filename = file.getName();
			String contentType = "application/octet-stream";
	
			InputStream fis = new BufferedInputStream(new FileInputStream(path));
			System.out.println("文件流大小："+fis.available());
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			
			response.reset();
		
			response.addHeader("Content-Disposition",
					"attachment;filename=" + new String(filename.getBytes("utf-8"), "ISO-8859-1"));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());

			response.setContentType(contentType);
			toClient.write(buffer);
			toClient.flush();
			toClient.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		//return response;
	}
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}


	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String uri = httpRequest.getRequestURI(); 
		// 解决中文乱码问题
		String fileUri = URLDecoder.decode(uri.substring(uri.indexOf("/wopi/") + 1, uri.length()), "UTF-8"); // /wopi/files/6F532108-35A3-47E3-A8AF-36D56D88D00
		// id不能带有花括号{}
		String fileId =  "";//"{6F532108-35A3-47E3-A8AF-36D56D88D00B}";//new StringBuilder().append("{").append(fileUri.substring(fileUri.indexOf("{"),fileUri.lastIndexOf("}")+1));

		if (fileUri.endsWith("/contents")) { // GetFile ：返回文件流
			fileUri = fileUri.substring(0,fileUri.indexOf("/contents"));
			fileId = "{"+fileUri.substring(fileUri.lastIndexOf("files")+6)+"}";
			System.out.println("<====预览文档id："+fileId);
			//String filePath = request.getServletContext().getRealPath("/")+"wopi/files/2016demo.docx";
			//getFileByPath(filePath,httpResponse);
			getFile(fileId, httpResponse);

		} else { // CheckFileInfo ：返回json
			fileId = "{"+fileUri.substring(fileUri.lastIndexOf("files")+6,fileUri.lastIndexOf("/"))+"}";
			System.out.println("<====预览文档id："+fileId);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=UTF-8");
			PrintWriter out = null;
			try {
				out = response.getWriter();
				out.write(FileUtils.checkFileInfoById(fileId));
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (out != null) {
					out.close();
				}
			}
		}
		
	}

}
