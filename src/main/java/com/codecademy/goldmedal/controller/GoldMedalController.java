package com.codecademy.goldmedal.controller;

import com.codecademy.goldmedal.model.*;
import com.codecademy.goldmedal.repository.CountryRepository;
import com.codecademy.goldmedal.repository.GoldMedalRepository;
import org.apache.commons.text.WordUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;




@RestController
@RequestMapping("/countries")
public class GoldMedalController {
    private final CountryRepository countryRepository;
    private final GoldMedalRepository goldMedalRepository;

    public GoldMedalController(CountryRepository countryRepository, GoldMedalRepository goldMedalRepository ) {
        this.countryRepository = countryRepository;
        this.goldMedalRepository = goldMedalRepository;
    }



    @GetMapping
    public CountriesResponse getCountries(@RequestParam String sort_by, @RequestParam String ascending) {
        var ascendingOrder = ascending.toLowerCase().equals("y");
        return new CountriesResponse(getCountrySummaries(sort_by.toLowerCase(), ascendingOrder));
    }

    @GetMapping("/{country}")
    public CountryDetailsResponse getCountryDetails(@PathVariable String country) {
        String countryName = WordUtils.capitalizeFully(country);
        return getCountryDetailsResponse(countryName);
    }

    @GetMapping("/{country}/medals")
    public CountryMedalsListResponse getCountryMedalsList(@PathVariable String country, @RequestParam String sort_by, @RequestParam String ascending) {
        String countryName = WordUtils.capitalizeFully(country);
        var ascendingOrder = ascending.toLowerCase().equals("y");
        return getCountryMedalsListResponse(countryName, sort_by.toLowerCase(), ascendingOrder);
    }

    private CountryMedalsListResponse getCountryMedalsListResponse(String countryName, String sortBy, boolean ascendingOrder) {
        List<GoldMedal> medalsList;
        switch (sortBy) {
            case "year":
            // TODO: list of medals sorted by year in the given order
                medalsList = goldMedalRepository.getByCountryOrderByYearAsc(countryName);      
                break;
            case "season":
            // TODO: list of medals sorted by season in the given order
                medalsList = goldMedalRepository.getByCountryOrderBySeasonAsc(countryName);  
                break;
            case "city":
            // TODO: list of medals sorted by city in the given order
                medalsList = goldMedalRepository.getByCountryOrderByCityAsc(countryName);
                break;
            case "name":
            // TODO: list of medals sorted by athlete's name in the given order
                medalsList = goldMedalRepository.getByCountryOrderByNameAsc(countryName); 
                break;
                // TODO: list of medals sorted by event in the given order
            case "event":
                medalsList = goldMedalRepository.getByCountryOrderByEventAsc(countryName); 
                break;
            default:
                medalsList = new ArrayList<>();
                break;
        }

        return new CountryMedalsListResponse(medalsList);
    }

    private CountryDetailsResponse getCountryDetailsResponse(String countryName) {
        var countryOptional =  countryRepository.getByName(countryName);
        if (countryOptional.isEmpty()) {
            return new CountryDetailsResponse(countryName);
        }

        var country = countryOptional.get();
        var goldMedalCount = goldMedalRepository.countByCountry(countryName);  
        var summerWins =  goldMedalRepository.getByCountryAndSeasonOrderByYearAsc(countryName, "Summer");
        var numberSummerWins = summerWins.size() > 0 ? summerWins.size() : null;
        var totalSummerEvents = goldMedalRepository.countBySeason("Summer"); 
        var percentageTotalSummerWins = totalSummerEvents != 0 && numberSummerWins != null ? (float) summerWins.size() / totalSummerEvents : null;
        var yearFirstSummerWin = summerWins.size() > 0 ? summerWins.get(0).getYear() : null;
        var winterWins = goldMedalRepository.getByCountryAndSeasonOrderByYearAsc(countryName, "Winter");
        var numberWinterWins = winterWins.size() > 0 ? winterWins.size() : null;
        var totalWinterEvents = goldMedalRepository.countBySeason("Winter"); 
        var percentageTotalWinterWins = totalWinterEvents != 0 && numberWinterWins != null ? (float) winterWins.size() / totalWinterEvents : null;
        var yearFirstWinterWin = winterWins.size() > 0 ? winterWins.get(0).getYear() : null;
        var numberEventsWonByFemaleAthletes = goldMedalRepository.countByCountryAndGender(countryName, "F");   
        var numberEventsWonByMaleAthletes = goldMedalRepository.countByCountryAndGender(countryName, "M"); 

        return new CountryDetailsResponse(
                countryName,
                country.getGdp(),
                country.getPopulation(),
                goldMedalCount,
                numberSummerWins,
                percentageTotalSummerWins,
                yearFirstSummerWin,
                numberWinterWins,
                percentageTotalWinterWins,
                yearFirstWinterWin,
                numberEventsWonByFemaleAthletes,
                numberEventsWonByMaleAthletes);
    }

    private List<CountrySummary> getCountrySummaries(String sortBy, boolean ascendingOrder) {
        List<Country> countries;
        switch (sortBy) {
            case "name":
                countries = ascendingOrder ? countryRepository.getAllByOrderByNameAsc() : countryRepository.getAllByOrderByNameDesc(); 
                break;
            case "gdp":
                countries = ascendingOrder ? countryRepository.getAllByOrderByGdpAsc() : countryRepository.getAllByOrderByGdpDesc(); 
                break;
            case "population":
                countries = ascendingOrder ? countryRepository.getAllByOrderByPopulationAsc() : countryRepository.getAllByOrderByPopulationDesc(); 
                break;
            case "medals":
            default:
                countries = countryRepository.getAllByOrderByNameAsc(); 
                break;
        }

        var countrySummaries = getCountrySummariesWithMedalCount(countries);

        if (sortBy.equalsIgnoreCase("medals")) {
            countrySummaries = sortByMedalCount(countrySummaries, ascendingOrder);
        }

        return countrySummaries;
    }

    private List<CountrySummary> sortByMedalCount(List<CountrySummary> countrySummaries, boolean ascendingOrder) {
        return countrySummaries.stream()
                .sorted((t1, t2) -> ascendingOrder ?
                        t1.getMedals() - t2.getMedals() :
                        t2.getMedals() - t1.getMedals())
                .collect(Collectors.toList());
    }

    private List<CountrySummary> getCountrySummariesWithMedalCount(List<Country> countries) {
        List<CountrySummary> countrySummaries = new ArrayList<>();
        for (var country : countries) {
            var goldMedalCount = goldMedalRepository.countByCountry(country.getName()); 
            countrySummaries.add(new CountrySummary(country, goldMedalCount));
        }
        return countrySummaries;
    }
}
