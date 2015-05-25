# Scalatra MongoDB Seed

A seed for building [microservices](http://martinfowler.com/articles/microservices.html) with [Scalatra](http://www.scalatra.org/),
 [MongoDB](http://www.mongodb.org/) and [Docker](https://www.docker.com/).

Project goals and assumptions:

   * provide a usable project template based on technologies, that are easily accessible for most **Java** developers (e.g. Scalatra and Jetty).
   * showcase selected features of **Scala**, **sbt** and **Docker**.

## Prerequisites

1. Install [SBT](http://www.scala-sbt.org/release/tutorial/Setup.html).
2. Install [MongoDB](http://docs.mongodb.org/manual/installation/) and [load sample data](data/README.md) provided with the project.
3. Install [Docker](https://docs.docker.com/installation/).

## Development mode

For rapid development feedback use the [sbt-revolver](https://github.com/spray/sbt-revolver) plugin:

    $ sbt ~re-start

## Deploying with Docker

Verify your Docker installation. When using `boot2docker`, start the virtual machine and run the Docker daemon by typing:

    $ boot2docker start

then set up environment variables: `DOCKER_TLS_VERIFY`, `DOCKER_HOST` and `DOCKER_CERT_PATH`.

### Preparing a Docker image

Build an image using the [sbt-docker](https://github.com/marcuslonnberg/sbt-docker) plugin:

    $ sbt docker

You can verify the list of available images by running:

    $ docker images

### Running the image in a new Docker container

Create a new Docker container and run freshly prepared image. 
First you need to run a separate Docker container with the mongodb server. To do this simply run:

A minimal setup will require a `MONGO_HOST` variable to be passed to the container:

    $ docker run -i mongo:latest

Then you need the IP address of the mongo container. To do this, type `docker ps` and note the ID of the running container, then type (replace `$CID` with the mongo container ID):

    $ docker inspect $CID | grep IPAddress | cut -d '"' -f 4 

Now you have the mongodb Docker container IP address and is turn to run our *dockerized* application with:

    $ docker run -e MONGO_HOST=<mongo-container-ip> -p 8080:8080 com.mintbeans/scalatra-mongodb-seed:v0.1-SNAPSHOT

A slightly more specific configuration can be setup as follows:

    $ docker run -e MONGO_HOST=<mongo-container-ip> -e MONGO_PORT=27017 -e MONGO_DB=test -p 8080:8080 com.mintbeans/scalatra-mongodb-seed:v0.1-SNAPSHOT

To verify the setup, check `DOCKER_HOST` under the published port:

    $ curl -v http://DOCKER_HOST:8080/locations

*Note: Remember that Docker has its own network namespace, so MongoDB instance available at `MONGO_HOST` should be visible from Docker container. When running locally both Docker and MongoDB, you might probably want to pass `MONGO_HOST` equal to `DOCKER_HOST`.*

### Changing Logback configuration at runtime

The image should include a `logback.xml` file with configuration [scanning](http://logback.qos.ch/manual/configuration.html#autoScan) enabled. You can alter most logging settings by following these steps:

    $ docker ps
    $ docker exec -it <container-id> bash
    [ root@23b382f59781:/data ]$ vim /app/logback.xml
    [ root@23b382f59781:/data ]$ exit

## Links

* [Working with Docker](http://docs.docker.com/introduction/working-with-docker/).
