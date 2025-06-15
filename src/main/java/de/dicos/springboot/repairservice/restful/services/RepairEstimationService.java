package de.dicos.springboot.repairservice.restful.services;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.opencsv.CSVReader;

import de.dicos.springboot.repairservice.restful.dto.ErrorResponseDto;
import de.dicos.springboot.repairservice.restful.dto.RepairEstimationRequestDto;
import de.dicos.springboot.repairservice.restful.dto.RepairEstimationResponseDto;
import de.dicos.springboot.repairservice.restful.exception.CsvLoadingException;
import de.dicos.springboot.repairservice.restful.exception.RepairServiceException;
import de.dicos.springboot.repairservice.restful.model.RepairAction;

@Component
public class RepairEstimationService {

    private static final Logger log = LoggerFactory.getLogger(RepairEstimationService.class);

    private final Map<String, Map<String, Double>> priceMap = new HashMap<>();

    @PostConstruct
    protected void loadPriceDataCsv() {
	try (CSVReader reader = new CSVReader(new InputStreamReader(
		new ClassPathResource("price_estimations.csv").getInputStream(), Charset.forName("windows-1252")))) {

	    List<String[]> rows = reader.readAll();
	    rows.remove(0);

	    for (String[] row : rows) {
		String carModel = row[0].trim();
		String repairAction = row[1].trim();
		double price = Double.parseDouble(row[2].trim());

		priceMap.computeIfAbsent(carModel, k -> new HashMap<>()).put(repairAction, price);
	    }
	    log.info("CSV loaded successfully");
	} catch (IOException e) {
	    throw new CsvLoadingException("Unable to read CSV file", e);
	} catch (ArrayIndexOutOfBoundsException e) {
	    throw new CsvLoadingException("CSV format error: one or more rows are incomplete.", e);
	} catch (Exception e) {
	    log.error("Unexpected error loading CSV: " + e.getMessage());
	    throw new CsvLoadingException("Unexpected error loading CSV data from 'price_estimations.csv'", e);
	}
    }

    public RepairEstimationResponseDto estimateCosts(RepairEstimationRequestDto request) {
	if (request.getCarModel() == null) {
	    throw new RepairServiceException(ResponseEntity.status(HttpStatus.BAD_REQUEST)
		    .body(new ErrorResponseDto(400, "Car model is required")));
	}
	if (request.getRepairActions() == null || request.getRepairActions().isEmpty()) {
	    throw new RepairServiceException(ResponseEntity.status(HttpStatus.BAD_REQUEST)
		    .body(new ErrorResponseDto(400, "At least one repair action is required")));
	}

	String carModelLabel = request.getCarModel().getCsvLabel();
	Map<String, Double> repairPricesForModel = priceMap.get(carModelLabel);
	if (repairPricesForModel == null) {
	    throw new RepairServiceException(
		    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDto(500,
			    "No repair actions with price information found for car model: " + carModelLabel)));
	}

	double estimatedPrice = 0.0;

	for (RepairAction action : request.getRepairActions()) {
	    String repairActionLabel = action.getCsvLabel();
	    Double price = repairPricesForModel.get(repairActionLabel);

	    if (price == null) {
		throw new RepairServiceException(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ErrorResponseDto(404, "No price information found for repair action: "
				+ repairActionLabel + " and car model: " + carModelLabel)));
	    }

	    estimatedPrice += price;
	}

	return new RepairEstimationResponseDto(estimatedPrice);
    }

    protected void loadPriceDataFromResourceForTest(String fileName) {
	try (CSVReader reader = new CSVReader(new InputStreamReader(new ClassPathResource(fileName).getInputStream(),
		Charset.forName("windows-1252")))) {

	    priceMap.clear();

	    List<String[]> rows = reader.readAll();
	    rows.remove(0);

	    for (String[] row : rows) {
		String carModel = row[0].trim();
		String repairAction = row[1].trim();
		double price = Double.parseDouble(row[2].trim());

		priceMap.computeIfAbsent(carModel, k -> new HashMap<>()).put(repairAction, price);
	    }
	    log.info("Test CSV loaded successfully");
	} catch (IOException e) {
	    throw new CsvLoadingException("Unable to read CSV file", e);
	} catch (ArrayIndexOutOfBoundsException e) {
	    throw new CsvLoadingException("CSV format error: one or more rows are incomplete.", e);
	} catch (Exception e) {
	    log.error("Unexpected error loading CSV: " + e.getMessage());
	    throw new CsvLoadingException("Unexpected error loading CSV data from 'price_estimations.csv'", e);
	}
    }
}
