# Populating MongoDB with sample data #

## Standalone server

You can use the `mongoimport` utility to load the sample data to a specific collection:

    $ mongoimport -d test -c locations --jsonArray sample_locations.json

If you want to use additional capabilities such as full-text search and/or spatial search, than some special indexes will be required as well:

    $ mongo test < sample_location_indexes.js

## Docker container

If you launched MongoDB in a **Docker** container, add appropriate `--host` and `--port` options to all commands from
the previous section:

    $ mongoimport --host <DOCKER_HOST> --port <PUBLISHED_PORT> -d test -c locations --jsonArray sample_locations.json
    $ mongo --host <DOCKER_HOST> --port <PUBLISHED_PORT> test < sample_location_indexes.js

Where `DOCKER_HOST` is the host where MongoDB container is running and `PUBLISHED_PORT` is the service port provided by `-p` option.