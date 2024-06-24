package com.crud.rental.service;

import com.crud.rental.domain.FuelPriceDto;
import com.crud.rental.domain.FuelUsage;
import com.crud.rental.domain.Reservation;
import com.crud.rental.repository.FuelUsageRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service

@RequiredArgsConstructor
public class FuelUsageService {
    private static final String URL = "https://www.autocentrum.pl/paliwa/ceny-paliw/";
    private final FuelUsageRepository fuelUsageRepository;

   public FuelUsage saveFuelUsage(final FuelUsage fuelUsage){
       return fuelUsageRepository.save(fuelUsage);
   }

    public BigDecimal calculateTotalCost(int fuelConsumption, BigDecimal fuelPrice) {
        return fuelPrice.multiply(BigDecimal.valueOf(fuelConsumption));
    }

    private BigDecimal calculateFuelConsumption(int startKm, int endKm, int fuelUsed) {
        int distanceTraveled = endKm - startKm;
        return BigDecimal.valueOf((double) fuelUsed / distanceTraveled * 100);
    }

    public List<FuelUsage> getAllFuelUsages() {
        return fuelUsageRepository.findAll();
    }

    public FuelUsage getFuelUsageById(Long id) {
        return fuelUsageRepository.findById(id).orElseThrow(() -> new RuntimeException("Fuel usage not found"));
    }

    public void deleteFuelUsageById(Long id) {
        fuelUsageRepository.deleteById(id);
    }

    public List<FuelPriceDto> getFuelPrices() throws IOException {
        List<FuelPriceDto> fuelPrices = new ArrayList<>();
        Document document = Jsoup.connect(URL).get();

        // Select the table rows
        Elements rows = document.select("table.petrols-table.table-striped tr");

        // Extract the fuel types from the first row
        Elements fuelTypeElements = rows.get(0).select("th");
        List<String> fuelTypes = new ArrayList<>();
        for (int i = 1; i < fuelTypeElements.size(); i++) {
            String fuelType = fuelTypeElements.get(i).text();
            fuelTypes.add(fuelType);
        }

        // Extract the data from the following rows
        for (int i = 1; i < rows.size() - 1; i++) {
            Element row = rows.get(i);
            Elements columns = row.select("td");

            if (columns.size() > 1) {
                String region = columns.get(0).text();

                for (int j = 1; j < columns.size(); j++) {
                   try {
                       String priceText = columns.get(j).text().replace(" zł", "").replace(",", ".");
                       BigDecimal price = new BigDecimal(priceText);
                       FuelPriceDto fuelPrice = new FuelPriceDto(region, fuelTypes.get(j - 1), price);
                       fuelPrices.add(fuelPrice);
                   }
                   catch(Exception e) {}
                }
            }
        }

        return fuelPrices;
    }

}
