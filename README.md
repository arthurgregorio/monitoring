# monitoring

Sample monitoring app with spring integration and AWS localstack

# Running the app

To run the app, start the docker environment with:

```shell
docker compose -p monitoring up
```

# Sending data

> If you are on windows and don't know how to send UDP packets, try [this shell](https://gist.github.com/PeteGoo/21a5ab7636786670e47c) script

Just send UDP packets with the following content:

For the temperature sensor:
- Data: `sensor_id=t1; value=30`
- Endpoint: `localhost:3344`
  
For the Humidity sensor:
- Data: `sensor_id=h1; value=40`
- Endpoint: `localhost:3355`

For Windows users with the script mentioned above:
- `Send-UdpDatagram -EndPoint "127.0.0.1" -Port 3344 -Message "sensor_id=t1; value=30"`
- `Send-UdpDatagram -EndPoint "127.0.0.1" -Port 3355 -Message "sensor_id=h1; value=30"`