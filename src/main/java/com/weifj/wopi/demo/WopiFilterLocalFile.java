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
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/wopi2/*")
public class WopiFilterLocalFile implements Filter {

	private void getFile(String path, HttpServletResponse response) {
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

		String uri = httpRequest.getRequestURI(); /// wopihost/wopi/files/excel.xlsx
        
		String fileUri = URLDecoder.decode(uri.substring(uri.indexOf("/wopi/") + 1, uri.length()), "UTF-8"); // /wopi/files/test.docx
		String filePath = request.getServletContext().getRealPath("/") + fileUri;

		if (fileUri.endsWith("/contents")) { 
			filePath = filePath.substring(0, filePath.indexOf("/contents"));
			filePath += "/2016demo.docx";
			getFile(filePath, httpResponse);

		} else { // CheckFileInfo json
			filePath += "2016demo.docx";
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=UTF-8");
			PrintWriter out = null;
			try {
				out = response.getWriter();
				out.write(FileUtils.checkFileInfo(filePath));
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
