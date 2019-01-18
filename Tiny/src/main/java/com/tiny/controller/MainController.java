package com.tiny.controller;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tiny.service.LogOfDevice;
import com.tiny.serviceImpl.MailerServiceImpl;
import com.tiny.serviceImpl.MainServiceImpl;
import com.tiny.utils.LogDTO;
import com.tiny.utils.TinyUtils;
import com.tiny.utils.UrlDTO;

@RestController
public class MainController {

	@Autowired
	LogOfDevice logOfDevice;
	@Autowired
	MainServiceImpl mainServiceImpl;
	@Autowired
	MailerServiceImpl mailService;
	
	String location ="D:\\Suraj_WorkSpace\\RBL_TinyTest\\Audit Trail\\log.csv";
	

	@RequestMapping(value = { "/{tinyUrl:.+}" }, method = RequestMethod.GET)
	public ResponseEntity<byte[]> method(@PathVariable String tinyUrl, HttpServletRequest request) {
		ResponseEntity<byte[]> response = null;
		try {
			List<LogDTO> list = new ArrayList<LogDTO>();
			UrlDTO urlDTO = mainServiceImpl.loadDataBasedShortURL(tinyUrl);
			LogDTO dto = new LogDTO();
			dto.setTinyURL(urlDTO.getTinyURL());
			dto.setIpAddress(request.getRemoteAddr());
			dto.setDeviceInfo(TinyUtils.populateRequestedDeviceDetails(request.getHeader("User-Agent")));
			dto.setLongUrl(urlDTO.getLongURL());
			mainServiceImpl.saveTraficLog(dto);
			list.add(dto);
			mainServiceImpl.writeTrafficLog(list, location);
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType("application/pdf"));
			String filename = urlDTO.getLongURL();
			headers.add("content-disposition", "inline;filename=" + filename);
			headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
			response = new ResponseEntity<byte[]>(Files.readAllBytes(Paths.get(filename)), headers, HttpStatus.OK);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return response;
	}

	@RequestMapping(value = "/loadCSV", method = RequestMethod.POST)
	public void method1(@RequestBody JSONObject jsonObject) throws Exception {
		try {
			mainServiceImpl.saveCSV(String.valueOf(jsonObject.get("csvFilePath")));
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value ="/sendMail")
	public void sendMail() throws Exception
	{
		try {
			mailService.sendCSVLogs(location);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			throw e;
		}
	}
}
