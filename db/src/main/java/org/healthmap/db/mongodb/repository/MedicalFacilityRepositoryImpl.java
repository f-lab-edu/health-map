package org.healthmap.db.mongodb.repository;

import lombok.RequiredArgsConstructor;
import org.healthmap.db.mongodb.model.MedicalFacility;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MedicalFacilityRepositoryImpl implements MedicalFacilityCustomRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public List<GeoResult<MedicalFacility>> getLocationNear(Double x, Double y, Double distance) {
        NearQuery nearQuery = NearQuery.near(x, y)
                .maxDistance(new Distance(distance, Metrics.KILOMETERS))
                .spherical(true)
                .limit(50);
        return mongoTemplate.geoNear(nearQuery, MedicalFacility.class).getContent();
    }
}
