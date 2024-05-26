# Monitoring Sensors

Sample monitoring app with spring integration and AWS localstack

# Running the app

> Before start, be sure you have Java 21 installed and correctly configured in you environment

To run the app, start the docker environment with:

```shell
docker compose -p monitoring up
```

After that you need to build the application using gradle:

> If you are using local gradle installation, the minimum version required is 8.6, otherwise, use the wrapper

- In the root folder for the warehouse-service, run: `gradlew clean bootJar`
- In the root folder for the central-service, run: `gradlew clean bootJar`

After, you can use the generated jar file from the `build\libs` folder in each project. You can run them using:

- `java -jar central-service-0.0.1.jar` for central-service
- `java -jar warehouse-service-0.0.1.jar` for warehouse-service

> WARNING! be sure that the port 3344 and 3355 are available before starting the application

If you did everything correctly, the application is up and ready to receive UDP packets in the endpoints described bellow.

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
