package com.tiny.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.tiny.entity.TrafficLog;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

@Service
public class TinyUtils {

	// Variables to read the CSV
	static String seperator = ",";
	public String loc = "D:\\Suraj_WorkSpace\\RBL_TinyTest\\Updated CSV\\Path.csv";
	// Variables for converting csv values into random numbers
	public static String charsetStr = "abcdefghijklmnopqrstuvwxyzABCEDFGHIJKLMNOPQRSTUVWXYZ0123456789";
	public final int base = charsetStr.length();
	private AtomicInteger counter = new AtomicInteger(10);

	public ArrayList<String> readCSV(String location) throws Exception {
		ArrayList<String> list = new ArrayList<>();
		try {
			FileReader fr = new FileReader(location);
			BufferedReader br = new BufferedReader(fr);
			String line = "";
			//br.readLine();
			while ((line = br.readLine()) != null) {
				String[] lineValue = line.split(seperator);
				list.add(lineValue[0]);
			}
			br.close();
		} catch (Exception e) {
			throw e;
		}
		return list;

	}

	public List<UrlDTO> gererateURL(List<String> values) throws Exception {
		List<UrlDTO> list = new ArrayList<UrlDTO>();
		for (int i = 0; i < values.size(); i++) {
			UrlDTO dto = new UrlDTO();
			final long nextNumber = getNextNumber();
			dto.setLongURL(values.get(i));
			dto.setTinyURL(convertAndGetBase62Code(nextNumber));
			list.add(dto);
		}
		try {
			writerCSV(list, loc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
		return list;

	}
	public void writerCSV(List<UrlDTO> shortUrls, String loc) throws Exception {
		try {
			FileWriter fw = new FileWriter(loc, false);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			shortUrls.forEach(data -> {
				pw.println(data.getLongURL() + "," + data.getTinyURL());
			});
			pw.flush();
			pw.close();
		} catch (Exception e) {
			throw e;
		}
	}

	private long getNextNumber() {
		int counterValue = counter.incrementAndGet();
		long systemTime = Long.valueOf("" + counterValue + System.currentTimeMillis());
		return systemTime;

	}

	private String convertAndGetBase62Code(long num) {
		StringBuffer sb = new StringBuffer("");
		while (num > 0) {
			int remainder = (int) (num % base);
			sb.append(charsetStr.charAt(remainder));
			num = num / base;
		}
		return sb.toString();

	}

	public static void writerCSVLog(List<TrafficLog> shortUrls, String loc) throws Exception {
		try {
			FileWriter fw = new FileWriter(loc, false);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			shortUrls.forEach(data -> {
				pw.println(data.getDiviceInfo() + "," + data.getUrl() + "," + data.getDataTime());
			});
			pw.flush();
			pw.close();
		} catch (Exception e) {
			throw e;
		}
	}

	public static String populateRequestedDeviceDetails(String userAgentString) {
		String deviceDetails = "";
		String browserAndVersion = "";
		String osAndDevice = "";
		try {
			if (userAgentString != null) {
				UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);

				OperatingSystem operatingSystem = userAgent.getOperatingSystem();
				osAndDevice = operatingSystem.getName() + " - " + operatingSystem.getDeviceType();

				Browser browser = userAgent.getBrowser();
				browserAndVersion = browser.getName() + " - " + userAgent.getBrowserVersion();

			}
			deviceDetails = browserAndVersion + "," + osAndDevice;
		} catch (Exception exception) {
			throw exception;
		}
		return deviceDetails;
	}

	public static boolean isEmpty(Object argString) {
		if ((argString == null)) {
			return true;
		} else {
			return false;
		}
	}

}
