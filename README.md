# Degree Days API

Assists with gathering degree day data and using it in practical applications such as normalizing 
home energy costs over time. 

## Getting Started

Build with maven. Deploy the REST API and access the standard endpoints.

## Location

A valid weather station must be defined in the station_id parameter. Locations can be obtained 
by country and city, or 
postal code at [NCDC](https://www.ncdc.noaa.gov).

## Units

Values passed in the units parameter must be valid if present. Use F for Fahrenheit or C for celsius 
or pass complete 
names. celsius is used if the parameter is absent.

## Degree Base

The "base_temp" parameter may be used to specify the temperature desired for cooling/heating purposes. 
This is essential since [degree days](https://en.wikipedia.org/wiki/Heating_degree_day) 
are defined as the number of degrees above or below a base temp, the base temp is 18.0C or 65.0F 
if unspecified. Standard defaults (18C heating and 20C cooling or 65F heating and 70F cooling) are provided 
if no base_temp parameter is passed. 

## Degree Day Type

It's possible to indicate whether the total degree days needed for heating or cooling are to be measured
by setting the "type" parameter to "c" ("cooling") or "h" ("heating"). The default is Heating. 
This setting affects the default base temperature (18c for heating and 20c for cooling).

## Degree Days Example

```bash
> curl "localhost:8080/degrees?type=heating&units=celsius&start_date=2015-01-01&end_date=2015-12-31&station_id=USW00014739&base_temp=18"
{"numDays":365,"degreeDays":3106.3,"numReadings":365}
```
## Readings Example

```bash
> curl "localhost:8080/dailies?units=celsius&start_date=2017-01-21&end_date=2017-01-23&station_id=USW00014739&base_temp=18"
[
  {"units":"celsius","degrees":6.0,"date":"2017-02-21"},
  {"units":"celsius","degrees":6.6,"date":"2017-02-22"},
  {"units":"celsius","degrees":3.3,"date":"2017-02-23"}
]
```

## Third Party Weather API Authentication

A weather service is required by the application. A token for the 3rd party web service can be 
set in an environment variable.

```bash
> unset NCDC_API_TOKEN
> mvn spring-boot:run
...
> curl "localhost:8080/degrees?units=celsius&start_date=2015-01-01&end_date=2015-12-31&station_id=USW00014739&base_temp=18"
401 {"reason":"no NCDC application token was found in the environment variable NCDC_API_TOKEN","code":"401"}
...
> export NCDC_API_TOKEN=<my_token>
> mvn spring-boot:run
...
> curl "localhost:8080/degrees?units=celsius&start_date=2015-01-01&end_date=2015-12-31&station_id=USW00014739&base_temp=18"
200
{"num_days":365,"degree_days":3089.3,"num_readings":365,"base_temp":18.0,"units":"celsius","degree_day_type":"heating","average":8.46}
```

## Docker

Build a Docker image and run a container using:
```bash
docker build --rm -t lsamaha/degreeday-api:0.1 .
docker run --env-file .local_env -p 8080:8080 lsamaha/degreeday-api:0.1
```
Environment variables should be set in an env file (.local_env above):
```bash
NCDC_API_TOKEN=<my_token>
```
