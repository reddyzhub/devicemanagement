This application is a REST API for managing the devices

##

- You can start the application with `./mvnw spring-boot:run` command.
- Please make sure to install the Java 17 and maven
- The embedded H2 database will be started along with the application.
- To access the API, you can use either postman or swagger UI (http://localhost:8080/swagger-ui/index.html)


## Endpoints

There are four endpoints

| Method | Path          | Descriptions                                                       |
|--------|---------------|--------------------------------------------------------------------|
| GET    | /devices      | List all existing Devices                                          |   
| GET    | /devices/{id} | Get device by identifier                                           |   
| POST   | /devices      | Adds a new device. Id and created date are automatically generated |
| PUT    | /devices/{id} | Full update of the Device                                          | 
| PATCH |  /devices/{id} | Partial update of the Device                                       |
| DELETE | /devices/{id} | Delete the device with the given Id                                |
| GET | /devices/search/brand/{brand}| Search the device by brand |
