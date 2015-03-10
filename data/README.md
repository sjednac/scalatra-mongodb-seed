# Populating MongoDB with sample data #

You can use the `mongoimport` utility to load the sample data to a specific collection:

    $ mongoimport -d test -c locations --jsonArray sample_locations.json

If you want to use additional capabilities such as full-text search and/or spatial search, than some special indexes will be required as well:

    $ mongo test < sample_location_indexes.js
