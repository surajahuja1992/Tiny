package com.tiny.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tiny.repository.TrafficLogRepository;
import com.tiny.serviceImpl.MailerServiceImpl;

@Component
public class CronJobs {
    private static Logger LOG = LoggerFactory.getLogger(CronJobs.class);
    private final TrafficLogRepository trafficLogRepository;
    @Autowired
    private MailerServiceImpl mailerServiceImpl;
    public CronJobs(TrafficLogRepository trafficLogRepository) {
        this.trafficLogRepository = trafficLogRepository;
    }
    //@Scheduled(fixedRate = 30000)
    public void sendCSVLogs() {
    	try {
    	//List<TrafficLog> trafficLogs = trafficLogRepository.findAll();
    	//TinyUtils.writerCSVLog(trafficLogs);
    	//mailerServiceImpl.sendCSVLogs();;
        LOG.debug("Mail sent");
    	}catch(Exception exception) {
    		exception.printStackTrace();
    	}
    }
}