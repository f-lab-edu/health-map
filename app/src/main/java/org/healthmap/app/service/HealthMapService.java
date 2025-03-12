package org.healthmap.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.healthmap.app.dto.HealthMapRequestDto;
import org.healthmap.app.dto.HealthMapResponseDto;
import org.healthmap.db.mongodb.model.MedicalFacility;
import org.healthmap.db.mongodb.repository.MedicalFacilityRepository;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class HealthMapService {
    private final MedicalFacilityRepository medicalFacilityRepository;

    public List<HealthMapResponseDto> getAllMedicalFacility() {
        List<MedicalFacility> allFacility = medicalFacilityRepository.findAll();
        if (!allFacility.isEmpty()) {
            return allFacility.stream()
                    .map(x -> convertDocumentToDto(x, null))
                    .limit(5000)
                    .toList();
        }
        return new ArrayList<>();

    }

    // {requestDto.distance} km 이내의 시설 찾기
    public List<HealthMapResponseDto> getNearByMedicalFacility(HealthMapRequestDto requestDto) {
        List<GeoResult<MedicalFacility>> result = medicalFacilityRepository.getLocationNear(requestDto.getX(), requestDto.getY(), requestDto.getDistance());
        List<HealthMapResponseDto> healthMapResponseDtoList = geoResultToDtoList(result);

        log.info("healthMapResponseDtoList size : {}", healthMapResponseDtoList.size());
        return healthMapResponseDtoList;
    }

    private List<HealthMapResponseDto> geoResultToDtoList(List<GeoResult<MedicalFacility>> result) {
        List<HealthMapResponseDto> healthMapResponseDtoList = new ArrayList<>();

        for (GeoResult<MedicalFacility> geoResult : result) {
            MedicalFacility content = geoResult.getContent();
            double distance = geoResult.getDistance().getValue();
            HealthMapResponseDto healthMapResponseDto = convertDocumentToDto(content, distance);
            healthMapResponseDtoList.add(healthMapResponseDto);
        }
        return healthMapResponseDtoList;
    }

    private HealthMapResponseDto convertDocumentToDto(MedicalFacility doc, Double distance) {
        return HealthMapResponseDto.of(doc, distance);
    }
}
