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
	loadCsv("price_estimations.csv", "CSV for price estimation loaded successfully");
    }

    protected void loadPriceDataFromResourceForTest(String fileName) {
	loadCsv(fileName, "Test CSV loaded successfully");
    }

    /**
     * Estimates the total repair cost based on car model and repair actions.
     *
     * @param requestDto
     * @return RepairEstimationResponseDto
     * @throws RepairServiceException
     */
    public RepairEstimationResponseDto estimateCosts(RepairEstimationRequestDto requestDto) {
	if (requestDto.getCarModel() == null) {
	    log.warn("Repair cost estimation failed: car model is null");
	    throw new RepairServiceException(ResponseEntity.status(HttpStatus.BAD_REQUEST)
		    .body(new ErrorResponseDto(400, "Car model is required")));
	}
	if (requestDto.getRepairActions() == null || requestDto.getRepairActions().isEmpty()) {
	    log.warn("Repair cost estimation failed: repair actions null");
	    throw new RepairServiceException(ResponseEntity.status(HttpStatus.BAD_REQUEST)
		    .body(new ErrorResponseDto(400, "At least one repair action is required")));
	}

	String carModelLabel = requestDto.getCarModel().getCsvLabel();
	Map<String, Double> repairPricesForModel = priceMap.get(carModelLabel);
	if (repairPricesForModel == null) {
	    log.warn("No price data found for repairActions of car model '{}'", carModelLabel);
	    throw new RepairServiceException(
		    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDto(500,
			    "No repair actions with price information found for car model: " + carModelLabel)));
	}

	double estimatedPrice = 0.0;

	for (RepairAction action : requestDto.getRepairActions()) {
	    String repairActionLabel = action.getCsvLabel();
	    Double price = repairPricesForModel.get(repairActionLabel);

	    if (price == null) {
		log.warn("No price found for action '{}' and car model '{}'", repairActionLabel, carModelLabel);
		throw new RepairServiceException(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ErrorResponseDto(404, "No price information found for repair action: "
				+ repairActionLabel + " and car model: " + carModelLabel)));
	    }

	    estimatedPrice += price;
	}

	return new RepairEstimationResponseDto(estimatedPrice);
    }

    /**
     * Loads a CSV file from the classpath with price estimation data for a repair
     * request
     *
     * @param filename
     * @param successLogMessage
     * @throws CsvLoadingException
     */
    private void loadCsv(String filename, String successLogMessage) {
	try (CSVReader reader = new CSVReader(new InputStreamReader(new ClassPathResource(filename).getInputStream(),
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

	    log.debug(successLogMessage);
	} catch (IOException e) {
	    log.error("CSV loading failed", e);
	    throw new CsvLoadingException("CSV loading error", e);
	} catch (ArrayIndexOutOfBoundsException e) {
	    log.error("CSV format error: possibly incomplete row", e);
	    throw new CsvLoadingException("CSV format error: one or more rows are incomplete.", e);
	} catch (Exception e) {
	    log.error("Unexpected error loading CSV", e);
	    throw new CsvLoadingException("Unexpected error loading CSV data", e);
	}
    }
}
