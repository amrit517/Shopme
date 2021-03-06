package com.shopme.admin.user.export;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

public class AbstractExporter {
	public void setResponseHeader(HttpServletResponse response,String contentType,String extension) throws IOException {
		DateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String timeStamp  = dateformatter.format(new Date());
		String fileName = "users_" + timeStamp + extension;
		response.setContentType(contentType);
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachement;filename=" + fileName;
		response.setHeader(headerKey, headerValue);
	}

}
