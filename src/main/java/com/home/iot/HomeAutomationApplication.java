package com.home.iot;

import com.home.iot.domains.Device;
import com.home.iot.domains.Slot;
import com.home.iot.services.DeviceService;
import com.home.iot.services.SlotService;
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

@SpringBootApplication
public class HomeAutomationApplication implements CommandLineRunner {

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
			slotService.save(new Slot(i, "Slot-"+i, "large"));
		}

		// Register a device
		Device device1 = new Device (1, "Living Room Light 1", "On/Off", "ON", "Living Room Light is Switched ON");
		deviceService.save(device1);

		Device device10 = new Device (10, "Garage Door", "On/Off", "ON", "Garage Door is Open");
		deviceService.save (device10);

	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@PostConstruct
	private void postConstruct() {
		Device device8 = new Device (8, "WashingMachine", "On/Off", "OFF", "Washing Machine is Off");
		deviceService.save (device8);
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
			e.printStackTrace();
		}
	}
}
