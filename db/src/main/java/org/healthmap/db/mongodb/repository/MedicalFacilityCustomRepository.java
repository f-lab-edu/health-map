package org.healthmap.db.mongodb.repository;

import org.healthmap.db.mongodb.model.MedicalFacility;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.Point;

import java.util.List;

public interface MedicalFacilityCustomRepository {
    List<GeoResult<MedicalFacility>> getLocationNear(Double x, Double y, Double distance);
}
