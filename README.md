# Degree Days API

Assists with gathering degree day data and using it in practical applications such as normalizing 
home energy costs over time. 

## Getting Started

Build with maven. Deploy the REST API servers and access the standard endpoints.

## Location

A valid weather station must be defined in the stationid parameter. Locations can be obtained 
by country and city, or 
postal code at [NCDC](https://www.ncdc.noaa.gov).

## Units

Values passed in the units parameter must be valid if present. Use F for Fahrenheit or C for Celsius 
or pass complete 
names. Celsius is used if the parameter is absent.

## Degree Base

The "baseTemp" parameter may be used to specify the temperature desired for cooling/heating purposes. 
This is essential since [degree days](https://en.wikipedia.org/wiki/Heating_degree_day) 
are defined as the number of degrees above or below a base temp, the base temp is 18.0C or 65.0F 
if unspecified. Standard defaults (18C heating and 20C cooling or 65F heating and 70F cooling) are provided 
if no baseTemp parameter is passed. 

## Degree Day Type

It's possible to indicate whether the total degree days needed for heating or cooling are to be measured
by setting the "type" parameter to C (Cooling) or H (Heating). The default is Heating. 
This setting affects the default base temperature (18C for heating and 20C for cooling).

## Degree Days Example

```bash
> curl "localhost:8080/degrees?type=Heating&units=CELSIUS&startDate=2015-01-01&endDate=2015-12-31&stationId=USW00014739&baseTemp=18"
{"numDays":365,"degreeDays":3106.3,"numReadings":365}
```
## Readings Example

```bash
> curl "localhost:8080/dailies?units=CELSIUS&startDate=2017-01-21&endDate=2017-01-23&stationId=USW00014739&baseTemp=18"
[
  {"units":"CELSIUS","degrees":6.0,"date":"2017-02-21"},
  {"units":"CELSIUS","degrees":6.6,"date":"2017-02-22"},
  {"units":"CELSIUS","degrees":3.3,"date":"2017-02-23"}
]
```

## Third Party Weather API Authentication

A weather service is required by the application. A token for the 3rd party web service can be 
set in an environment variable.

```bash
> unset NCDC_API_TOKEN
> mvn spring-boot:run
...
> curl "localhost:8080/degrees?units=CELSIUS&startDate=2015-01-01&endDate=2015-12-31&stationId=USW00014739&baseTemp=18"
401 {"reason":"no NCDC application token was found in the environment variable NCDC_API_TOKEN","code":"401"}
...
> export NCDC_API_TOKEN=ef9VaV25IlTaaf53kUdnv93kdfweEQ55y
> mvn spring-boot:run
...
> curl "localhost:8080/degrees?units=CELSIUS&startDate=2015-01-01&endDate=2015-12-31&stationId=USW00014739&baseTemp=18"
200

```
