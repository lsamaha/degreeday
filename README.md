# degreeday

Assists with gathering degree day data and using it in practical applications such as normalizing home energy costs over time. 

## Getting Started

Build with maven. Deploy the REST API servers and access the standard endpoints.

## Location

A valid weather station must be defined in the stationid parameter. Locations can be queried by country and city, or postal code.

## Units

Valid units are expected in the units parameter if present. Use F for Fahrenheit or C for Celsius. Celsius is the default value.

## Base

Since degree days are defined as the number of degrees above or below a base temp, the base temp is 18.0C or 65.0F if unspecified.

## Example

```
curl -H "Authorization:Token ef9VaV25IlTaaf53kUdnv93kdfweEQ55" https://<host>/degreeday/degrees?stationid=GHCND:UKM00003772&units=F&base=65&startdate=2017-01-20&enddate=2017-01-21
```

## Result

{
  "metadata": {
    "count": 1,
    "offset": 1,
    "limit": 1000
  },
  "results": 
  [
    {
      "day": "2017-01-20",
      "degrees": 64
    }
  ]
}
