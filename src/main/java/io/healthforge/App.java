/*
 * Copyright (c) 2016. All rights reserved, HealthForge.
 */

package io.healthforge;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.collect.Lists;
import io.healthforge.exception.InvalidModelException;
import io.healthforge.models.Clinician;
import io.healthforge.models.Patient;
import io.healthforge.models.orders.Order;
import io.healthforge.models.orders.OrderStatus;
import io.healthforge.models.orders.SimpleOrder;
import io.healthforge.exception.NotFoundException;
import io.healthforge.services.ClinicianService;
import io.healthforge.services.OrderService;
import io.healthforge.services.PatientService;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

@SpringBootApplication
@EnableScheduling
public class App implements ApplicationListener<ApplicationReadyEvent> {

	private final static Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Autowired
	private PatientService patientService;

	@Autowired
	private ClinicianService clinicianService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private ResourceLoader resourceLoader;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {

		DateTimeFormatter formatter = DateTimeFormat.forPattern("dd MMM yyyy");

		logger.info("Loading patients from file...");

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.registerModule(new JodaModule());
		objectMapper.setDateFormat(new ISO8601DateFormat());
		MappingIterator<Patient> patients = null;
		try {
			final InputStream stream = this.resourceLoader.getClassLoader().getResourceAsStream("patients.json");
			patients = objectMapper.readerFor(Patient.class).readValues(stream);
			for (Patient patient : patients.readAll()) {
				logger.info("{} {} {}", patient.getId(), patient.getFirstName(), patient.getLastName());
				patientService.add(patient);
			}
		} catch (IOException e) {
			logger.error("Unable to read patients file", e);
		}

		clinicianService.add(new Clinician(Lists.newArrayList("G100"), "Doctor", "Apple"));
		clinicianService.add(new Clinician(Lists.newArrayList("G101"), "Doctor", "Orange"));
		clinicianService.add(new Clinician(Lists.newArrayList("G102"), "Doctor", "Pear"));
	}

	//---------------------------------------------------------------------------------------
	// Simulate order processing by third party systems
	//

	/**
	 * Every 5 seconds this method will create a new random order request
	 */
	@Scheduled(initialDelay = 5000, fixedDelay=5000)
	public void doCreateOrderRequest() {
		SimpleOrder order = new SimpleOrder(OrderStatus.NEW, randomPatient().getId(), randomClinician().getId(),
				(new DateTime()).toDateTimeISO() + "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer facilisis turpis mi, ac dignissim ante luctus sit amet. Maecenas elementum efficitur lacus, id ullamcorper magna mollis quis. Vestibulum id sapien eros. Ut vel sodales dolor, a tempus sapien. Pellentesque laoreet id felis eget maximus. Integer vitae neque fermentum, pellentesque diam at, finibus nulla. Curabitur nec tempus leo, in elementum ante. Ut nec massa diam. Nam nec leo posuere, consectetur risus sed, lacinia massa. Sed elementum, turpis at elementum sodales, orci ligula congue metus, eget finibus ex tortor sit amet tellus. Etiam diam est, lacinia eu metus ac, euismod consequat odio.");
		orderService.add(order);
	}

	/**
	 * Every few seconds this method will process a random order request and change its state
	 */
	@Scheduled(initialDelay = 5000, fixedDelay=1250)
	public void doProcessOrderRequest() throws NotFoundException, InvalidModelException {
		Order order = randomOrder();
		if(order != null) {
			if(order.getOrderStatus().equals(OrderStatus.NEW)) {
				order.setOrderStatus(OrderStatus.PENDING);
			} else if(order.getOrderStatus().equals(OrderStatus.PENDING)) {
				order.setOrderStatus(OrderStatus.COMPLETED);
			} else if(order.getOrderStatus().equals(OrderStatus.ABORT)) {
				order.setOrderStatus(OrderStatus.ABORTED);
			}
			orderService.update(order);
		}
	}

	/**
	 * Every 6 seconds this method will abort a random order
	 */
	@Scheduled(initialDelay = 5000, fixedDelay=6000)
	public void doAbortOrderRequest() throws NotFoundException, InvalidModelException {
		Order order = randomOrder();
		if(order != null) {
			if(order.getOrderStatus().equals(OrderStatus.PENDING)) {
				order.setOrderStatus(OrderStatus.ABORT);
			}
			orderService.update(order);
		}
	}

	//
	//---------------------------------------------------------------------------------------

	private Random rng = new Random();

	private Patient randomPatient() {
		return patientService.get(rng.nextInt(patientService.size()), 1, null).getItems().get(0);
	}

	private Clinician randomClinician() {
		return clinicianService.get(rng.nextInt(clinicianService.size()), 1, null).getItems().get(0);
	}

	private Order randomOrder() {
		if(orderService.size() > 1) {
			return orderService.get(rng.nextInt(orderService.size()), 1, null).getItems().get(0);
		}
		return null;
	}
}
