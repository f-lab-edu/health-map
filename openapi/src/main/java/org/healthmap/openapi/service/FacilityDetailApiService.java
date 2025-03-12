package org.healthmap.openapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.healthmap.openapi.api.FacilityDetailInfoApi;
import org.healthmap.openapi.dto.FacilityDetailDto;
import org.healthmap.openapi.dto.FacilityDetailUpdateDto;
import org.healthmap.openapi.error.OpenApiErrorCode;
import org.healthmap.openapi.exception.OpenApiProblemException;
import org.healthmap.openapi.pattern.PatternMatcherManager;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class FacilityDetailApiService {
    private final FacilityDetailInfoApi facilityDetailInfoApi;
    private final PatternMatcherManager patternMatcherManager;

    private final static String TREATMENT_N = "휴진";
    private final static String TREATMENT_Y = "휴진없음";
    private final static String NOTHING = "없음";
    private final static Set<String> NO_LUNCH = new HashSet<>(List.of("공란", "휴진", "없음", "휴무", "전체휴진", "오전진료", "무", "점심시간없음"));
    private final static Set<String> TREATMENT_YES = new HashSet<>(List.of("정상근무", "정규진료", "진료", "휴진없음"));
    private final static Set<String> TREATMENT_NO = new HashSet<>(List.of("전부휴진", "모두휴진", "휴진", "휴무", "전부휴일",
            "전부휴무", "전체휴진", "매주휴진", "종일휴진", "휴뮤", "휴진입니다.")
    );


    // 1. API로부터 JsonDTO 가져오기
    // 2. jsonDTO를 updateDTO로 변환
    // 3. repository에 update 진행
    public Optional<FacilityDetailUpdateDto> getFacilityDetailInfo(String id) {
        FacilityDetailDto facilityDetailDto = facilityDetailInfoApi.getFacilityDetailDtoFromApi(id);

        if (facilityDetailDto != null) {
            try {
                return convertToUpdateDto(facilityDetailDto);
            } catch (Exception e) {
                log.error("진행중에 오류가 발생했습니다. : {}", e.getMessage());
                throw new OpenApiProblemException(OpenApiErrorCode.CONVERT_ERROR, e.getMessage());
            }
        }
        return Optional.empty();
    }


    // DTO 변환 로직
    private Optional<FacilityDetailUpdateDto> convertToUpdateDto(FacilityDetailDto dto) {
        String noTreatmentSun = checkNoTreatment(dto.getNoTrmtSun());
        String noTreatmentHoliday = checkNoTreatment(dto.getNoTrmtSun());
        String treatmentMon = getTreatmentTime(dto.getTrmtMonStart(), dto.getTrmtMonEnd());
        String treatmentTue = getTreatmentTime(dto.getTrmtTueStart(), dto.getTrmtTueEnd());
        String treatmentWed = getTreatmentTime(dto.getTrmtWedStart(), dto.getTrmtWedEnd());
        String treatmentThu = getTreatmentTime(dto.getTrmtThuStart(), dto.getTrmtThuEnd());
        String treatmentFri = getTreatmentTime(dto.getTrmtFriStart(), dto.getTrmtFriEnd());
        String treatmentSat = getTreatmentTime(dto.getTrmtSatStart(), dto.getTrmtSatEnd());
        String treatmentSun = getSundayTreatment(noTreatmentSun, dto.getTrmtSunStart(), dto.getTrmtSunEnd());
        String receiveWeek = changeTimeFormat(dto.getRcvWeek());
        String receiveSat = changeTimeFormat(dto.getRcvSat());
        String lunchWeek = changeLunchTime(dto.getLunchWeek());
        String lunchSat = changeLunchTime(dto.getLunchSat());

        FacilityDetailUpdateDto updateDto = FacilityDetailUpdateDto.of(dto.getCode(), dto.getParkXpnsYn(),
                dto.getParkEtc(), treatmentMon, treatmentTue, treatmentWed, treatmentThu, treatmentFri, treatmentSat,
                treatmentSun, receiveWeek, receiveSat, lunchWeek, lunchSat, noTreatmentSun, noTreatmentHoliday,
                dto.getEmyDayYn(), dto.getEmyNgtYn());
        return Optional.ofNullable(updateDto);
    }

    private String getSundayTreatment(String noTreatmentSun, String treatmentStart, String treatmentEnd) {
        if (noTreatmentSun != null && noTreatmentSun.equals(TREATMENT_N)) {
            return TREATMENT_N;
        }
        return getTreatmentTime(treatmentStart, treatmentEnd);
    }

    private String changeLunchTime(String lunchTime) {
        if (lunchTime == null) {
            return null;
        }

        String spaceRemoved = lunchTime.replaceAll(" ", "");
        if (NO_LUNCH.contains(spaceRemoved)) {
            return NOTHING;
        }
        return changeTimeFormat(lunchTime);

    }

    private String checkNoTreatment(String time) {

        if (time == null) {
            return null;
        }
        String spaceRemoved = time.replaceAll(" ", "");
        if (TREATMENT_YES.contains(spaceRemoved)) {
            return TREATMENT_Y;
        } else if (TREATMENT_NO.contains(spaceRemoved)) {
            return TREATMENT_N;
        }
        return time;
    }

    private String changeTimeFormat(String time) {
        return patternMatcherManager.matchAndFormat(time);
    }

    private String getTreatmentTime(String start, String end) {
        String result = null;
        if ((start != null && start.length() == 4)
                && (end != null && end.length() == 4)) {
            String startTime = String.format("%s:%s", start.substring(0, 2), start.substring(2, 4));
            String endTime = String.format("%s:%s", end.substring(0, 2), end.substring(2, 4));
            result = String.format("%s ~ %s", startTime, endTime);
        }
        return result;
    }
}
