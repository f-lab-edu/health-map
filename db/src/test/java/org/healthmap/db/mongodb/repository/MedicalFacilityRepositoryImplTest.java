package org.healthmap.db.mongodb.repository;

import org.assertj.core.api.Assertions;
import org.healthmap.db.MongoConfig;
import org.healthmap.db.mongodb.model.MedicalFacility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.GeoResult;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {MongoConfig.class})
@ActiveProfiles("secret")
class MedicalFacilityRepositoryImplTest {
    @Autowired
    MedicalFacilityRepositoryImpl medicalFacilityRepository;

    @Test
    @DisplayName("근처 쿼리 가져오는지 확인")
    public void getLocationNear() {
        Double x = 126.9963104;
        Double y = 37.4828517;

        List<GeoResult<MedicalFacility>> locationNear = medicalFacilityRepository.getLocationNear(x, y, 0.5);
        Assertions.assertThat(locationNear).isNotEmpty();
    }
}
