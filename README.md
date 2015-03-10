# Scalatra MongoDB Seed

A seed for building [microservices](http://martinfowler.com/articles/microservices.html) with [Scalatra](http://www.scalatra.org/),
 [MongoDB](http://www.mongodb.org/) and [Docker](https://www.docker.com/).

## Development mode

For rapid development feedback use the [sbt-revolver](https://github.com/spray/sbt-revolver) plugin:

    $ sbt ~re-start

## Deploying with Docker

### Preparing a Docker image

Build an image using the [sbt-docker](https://github.com/marcuslonnberg/sbt-docker) plugin:

    $ sbt docker

You can verify the list of available images by running:

    $ docker images

### Running the image in a new Docker container

A minimal setup will require a `MONGO_HOST` variable to be passed to the container:

    $ docker run -e MONGO_HOST=192.168.0.2 -p 8080:8080 com.mintbeans/scalatra-mongodb-seed:v0.1-SNAPSHOT

A slightly more specific configuration can be setup as follows:

    $ docker run -e MONGO_HOST=192.168.0.2 -e MONGO_PORT=27017 -e MONGO_DB=test -p 8080:8080 com.mintbeans/scalatra-mongodb-seed:v0.1-SNAPSHOT

To verify the setup, check `DOCKER_HOST` under the published port:

    $ curl -v http://DOCKER_HOST:8080/locations

### Changing Logback configuration at runtime

The image should include a `logback.xml` file with configuration [scanning](http://logback.qos.ch/manual/configuration.html#autoScan) enabled. You can alter most logging settings by following these steps:

    $ docker ps
    $ docker exec -it <container-id> bash
    [ root@23b382f59781:/data ]$ vim /app/logback.xml
    [ root@23b382f59781:/data ]$ exit

## Links

* [Working with Docker](http://docs.docker.com/introduction/working-with-docker/).
