# Scalatra MongoDB Seed [![Build Status](https://travis-ci.org/sbilinski/scalatra-mongodb-seed.svg?branch=master)](https://travis-ci.org/sbilinski/scalatra-mongodb-seed) [![Codacy Badge](https://www.codacy.com/project/badge/cd005fb9e3ad402eb5b567da4687a356)](https://www.codacy.com/app/sbilinski/scalatra-mongodb-seed)

A seed for building [microservices](http://martinfowler.com/articles/microservices.html) with [Scalatra](http://www.scalatra.org/),
 [MongoDB](http://www.mongodb.org/) and [Docker](https://www.docker.com/).

Project goals and assumptions:

   * provide a project template based on technologies, that are easily accessible for most **Java** developers (e.g. Scalatra and Jetty).
   * showcase selected features of **Scala**, **sbt** and **Docker**.

## Development mode

For rapid development feedback use the [sbt-revolver](https://github.com/spray/sbt-revolver) plugin:

    sbt ~re-start

It will respawn the server process whenever a project file changes and a fresh build is required. 

## Docker deployment

This section provides a step-by-step guide for hosting the service in a virtual **Docker** environment, including a standalone **MongoDB** instance. Prerequisites:

1. Install [Docker](https://docs.docker.com/installation/). 
2. Install [Docker Machine](https://docs.docker.com/machine/), if it isn't included in the Docker distribution (useful for managing multiple servers in your local environment). 

### Create virtual Docker servers

Create a virtual machine for each component of the system:

    docker-machine create --driver virtualbox service1
    docker-machine create --driver virtualbox mongo1

### Setup MongoDB

You'll need a MongoDB instance and some [sample data](data/) to get started. Make sure, that your Docker client is connecting to the Mongo server:

    eval "$(docker-machine env mongo1)"
    
Spawn a new Mongo process:

    docker run -d --restart=always --name mongo -p 27017:27017 mongo:latest    

You can test your connection from your development box by running:

    mongo $(docker-machine ip mongo1)/test

Don't worry, if you don't have a Mongo client available locally. You can always launch it directly in the container: 

    docker exec -it mongo mongo test

Import sample data and create some custom indexes:

    docker exec -i mongo mongoimport -d test -c locations --jsonArray < data/sample_locations.json
    docker exec -i mongo mongo test < data/sample_location_indexes.js
    
### Deploy an application image

Switch your Docker client to the target environment: 

    eval "$(docker-machine env service1)"

Build an image using the [sbt-docker](https://github.com/marcuslonnberg/sbt-docker) plugin:

    sbt docker

You can verify the list of available images by running:

    docker images

Run the image in a new Docker container as such:

    docker run \
            --name=location-provider \
            -d --restart=always \
            -e MONGO_HOST=$(docker-machine ip mongo1) \
            -e MONGO_PORT=27017 \
            -e MONGO_DB=test \
            -p 8080:8080 \
            com.mintbeans/scalatra-mongodb-seed:v0.1-SNAPSHOT

The `-d` switch implies detaching the process from the current session. You can always follow the logs by executing:

    docker logs -f location-provider

Check if everything works correctly by fetching the list of locations:

    curl -v http://$(docker-machine ip service1):8080/locations

## Swagger integration

[Swagger](http://swagger.io/) should be integrated by default. Point [Swagger UI](http://petstore.swagger.io/?url=http://localhost:8080/api-docs/)
 to [http://localhost:8080/api-docs/](http://localhost:8080/api-docs/) to interact with all exposed methods and to find out their specs.

## Links

* [Working with Docker](http://docs.docker.com/introduction/working-with-docker/).
