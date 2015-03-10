db.locations.ensureIndex(
    {
        name: "text"
    },
    {
        weights: {
            name: 1
        },
        name: "text_index"
    }
)

db.locations.ensureIndex( { point: "2dsphere" } )
