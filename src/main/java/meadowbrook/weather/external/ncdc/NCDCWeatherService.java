package meadowbrook.weather.external.ncdc;

import meadowbrook.weather.degreeday.TokenConfigurationException;
import meadowbrook.weather.external.WeatherService;
import meadowbrook.weather.external.ncdc.model.NCDCReading;
import meadowbrook.weather.external.ncdc.model.NCDCWeatherServiceData;
import meadowbrook.weather.model.DailyAverage;
import meadowbrook.weather.model.Temperature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Access the NCDC weather API for daily average temperatures.
 */
@Component
public class NCDCWeatherService implements WeatherService {

    private static final String BASE_URL = "https://www.ncdc.noaa.gov/cdo-web/api/v2/data";
    private static final String ENV_TOKEN = "NCDC_API_TOKEN";
    private static final String minDatatype = "TMIN";
    private static final String maxDatatype = "TMAX";
    private static final Logger logger = LoggerFactory.getLogger(NCDCWeatherService.class);
    private static final DecimalFormat decimalFormat = new DecimalFormat(".##");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public DailyAverage getDailyAverage(LocalDate d,
                                        String stationId,
                                        Temperature.Units units) {
        return DailyAverage.create()
                .degrees(0d)
                .units(Temperature.Units.CELSIUS)
                .date(d.getYear(), d.getMonthValue(), d.getDayOfMonth());
    }

    @Override
    public Collection<DailyAverage> getDailyAverages(LocalDate startDate,
                                                     LocalDate endDate,
                                                     String stationId,
                                                     Temperature.Units units) {
        String datasetid = "GHCND";
        Integer limit = 1000;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("token", new String(getApplicationToken()));
        logger.info(headers.get("token").toString());
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("datasetid", datasetid)
                .queryParam("datatypeid", minDatatype + "," + maxDatatype)
                .queryParam("stationid", datasetid + ":" + stationId)
                .queryParam("startdate", dateFormatter.format(startDate))
                .queryParam("enddate", dateFormatter.format(endDate))
                .queryParam("limit", limit);
        URI uri = builder.build().encode().toUri();
        logger.info("GET:" + uri.toString());
        HttpEntity entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<NCDCWeatherServiceData> result = restTemplate.exchange(
                uri, HttpMethod.GET, entity, NCDCWeatherServiceData.class);
        List<NCDCReading> ncdcReadings = result.getBody().getResults();
        return calculateAverages(ncdcReadings);
    }

    protected static Collection<DailyAverage> calculateAverages(List<NCDCReading> ncdcReadings) {
        // calculate avg for each date
        Collection<DailyAverage> dailyAverages = new LinkedList();
        Map<String,Map<String,Double>> minmax = new HashMap<>();
        if(ncdcReadings != null) {
            ncdcReadings.forEach(r -> {
                String dateStr = r.getDate().substring(0, r.getDate().indexOf('T'));
                if(!minmax.containsKey(dateStr)) {
                    minmax.put(dateStr, new HashMap<>());
                }
                minmax.get(dateStr).put(r.getDatatype(), r.getDegreesCelsius());
            });
            for(String dateStr : minmax.keySet()) {
                Map<String,Double> dailyData = minmax.get(dateStr);
                dailyAverages.add(asDailyAverage(
                        dateStr, dailyData.get(minDatatype), dailyData.get(maxDatatype)));
            }
        }
        return dailyAverages;
    }

    private static DailyAverage asDailyAverage(String dateStr, Double min, Double max) {
        LocalDate readingDate = LocalDate.parse(dateStr, dateFormatter);
        double avg = (min + max) / 2d;
        double roundAvg = Double.parseDouble(decimalFormat.format(avg));
        logger.debug("min/max:" + min + "-" + max + " -> " + roundAvg);
        return DailyAverage.create()
                .degrees(roundAvg)
                .units(Temperature.Units.CELSIUS)
                .date(readingDate.getYear(), readingDate.getMonthValue(), readingDate.getDayOfMonth());
    }

    private static char[] getApplicationToken() throws TokenConfigurationException {
        Map<String, String> env = System.getenv();
        if (env.containsKey(ENV_TOKEN)) {
            return (env.get(ENV_TOKEN)).toCharArray();
        } else {
            throw new TokenConfigurationException(
                    "no NCDC application token was found in the environment variable " + ENV_TOKEN);
        }
    }

}
