package com.tiny.serviceImpl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tiny.entity.TinyUrlInfo;
import com.tiny.entity.TrafficLog;
import com.tiny.repository.TinyUrlRepository;
import com.tiny.repository.TrafficLogRepository;
import com.tiny.service.MainService;
import com.tiny.utils.LogDTO;
import com.tiny.utils.TinyUtils;
import com.tiny.utils.UrlDTO;


@Service
public class MainServiceImpl implements MainService {
	@Autowired
	TinyUtils tinyUtils;
	@Autowired
	TinyUrlRepository tinyUrlRepository;
	@Autowired
	TrafficLogRepository trafficLogRepository; 
	@Override
	public void saveCSV(String filePath) throws Exception {
		try {
			tinyUrlRepository.saveAll(tinyUtils.gererateURL(tinyUtils.readCSV(filePath)).stream().map(data -> new TinyUrlInfo(data.getLongURL(), data.getTinyURL())).collect(Collectors.toList()));
			//tinyUtils.gererateURL(tinyUtils.readCSV(filePath));
		} catch (Exception e) {
			throw e;
		}

	}
	
	@Override
	public UrlDTO loadDataBasedShortURL(String shortUrls) throws Exception {
		UrlDTO dto = new UrlDTO();
		try {
			List<TinyUrlInfo> dbList = tinyUrlRepository.findByShortUrls(shortUrls);
			if(!dbList.isEmpty()) {
				TinyUrlInfo info = dbList.get(dbList.size()-1);
				dto.setLongURL(info.getLongUrls());
				dto.setTinyURL(info.getShortUrls());
			}
		}catch(Exception exception) {
			throw exception;
		}
		return dto;
	}
	@Override
	public void saveTraficLog(LogDTO dto) throws Exception {
		try {
			TrafficLog trafficLog = new TrafficLog();
			trafficLog.setDataTime(new Date());
			trafficLog.setDiviceInfo(dto.getIpAddress() + " - "+dto.getDeviceInfo());
			trafficLog.setUrl(dto.getLongUrl());
			trafficLogRepository.save(trafficLog);
		}catch (Exception e) {
			throw e;
		}
	}
	public void writeTrafficLog(List<LogDTO> logs, String loc) throws Exception {
		try {
			FileWriter fw = new FileWriter(loc, true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			logs.forEach(data -> {
				pw.println(new Date() + ","  + data.getIpAddress() + "," + data.getDeviceInfo() + "," + data.getLongUrl());
			});
			pw.flush();
			pw.close();
		} catch (Exception e) {
			throw e;
		}
	}
	
}
