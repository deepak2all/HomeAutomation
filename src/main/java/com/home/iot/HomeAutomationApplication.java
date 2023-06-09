package com.home.iot;

import com.home.iot.domains.DeviceDTO;
import com.home.iot.domains.SlotDTO;
import com.home.iot.services.DeviceService;
import com.home.iot.services.SlotService;
import com.home.iot.util.DeviceStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Log4j2
@SpringBootApplication
public class HomeAutomationApplication implements CommandLineRunner {

	//@Autowired
	//private BuildProperties buildProperties;

	@Autowired
	DeviceService deviceService;

	@Autowired
	SlotService slotService;

	public static void main(String[] args) {
		SpringApplication.run(HomeAutomationApplication.class, args);
		openHomePage();
	}

	@Override
	public void run(String... args) throws Exception {
		for (int i=1; i<=10; i++) {
			slotService.save(new SlotDTO(i, "Slot-"+i, "large"));
		}

		// Register a device
		DeviceDTO deviceDTO1 = new DeviceDTO(1, "Living Room Light 1", "On/Off", DeviceStatus.ON, "Living Room Light is Switched ON");
		deviceService.save(deviceDTO1);

		DeviceDTO deviceDTO10 = new DeviceDTO(10, "Garage Door", "On/Off", DeviceStatus.ON, "Garage Door is Open");
		deviceService.save (deviceDTO10);

	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@PostConstruct
	private void postConstruct() {
		appInfo();
		DeviceDTO deviceDTO8 = new DeviceDTO(8, "WashingMachine", "On/Off", DeviceStatus.OFF, "Washing Machine is Off");
		deviceService.save (deviceDTO8);
	}

	private void appInfo() {
		log.info(":::::  APP  INFO   ::::::");
		//log.info("App Name   : " + buildProperties.getName());
		//log.info("App Version   : " + buildProperties.getVersion());
	}

	/**
	 * Auto-launching of home page as soon as server comes up
	 */
	private static void openHomePage() {
		try {
			URI homepage = new URI("http://localhost:8500/");
			System.setProperty("java.awt.headless", "false");
			Desktop.getDesktop().browse(homepage);
		} catch (URISyntaxException | IOException e) {
			log.error("Issue with launching home page {}", e.getMessage());
		}
	}
}
