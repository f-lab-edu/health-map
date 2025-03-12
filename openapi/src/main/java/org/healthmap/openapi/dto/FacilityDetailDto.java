package org.healthmap.openapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacilityDetailDto {
    private String code;
    private String parkXpnsYn;      // 주차장 여부
    private String parkEtc;         // 주차장 관련 세부 정보
    private String trmtMonStart;    // 진료시간_월_시작
    private String trmtMonEnd;      // 진료시간_월_종료
    private String trmtTueStart;    // 진료시간_화_시작
    private String trmtTueEnd;      // 진료시간_화_종료
    private String trmtWedStart;    // 진료시간_수_시작
    private String trmtWedEnd;      // 진료시간_수_종료
    private String trmtThuStart;    // 진료시간_목_시작
    private String trmtThuEnd;      // 진료시간_목_종료
    private String trmtFriStart;    // 진료시간_금_시작
    private String trmtFriEnd;      // 진료시간_금_종료
    private String trmtSatStart;    // 진료시간_토_시작
    private String trmtSatEnd;      // 진료시간_토_종료
    private String trmtSunStart;    // 진료시간_일_시작
    private String trmtSunEnd;      // 진료시간_일_종료
    private String rcvWeek;         // 접수시간_평일
    private String rcvSat;          // 접수시간_토요일
    private String lunchWeek;       // 점심시간_평일
    private String lunchSat;        // 점심시간_토
    private String noTrmtSun;       // 일요일 휴진
    private String noTrmtHoli;      // 공휴일 휴진
    private String emyDayYn;        // 주간 응급실 운영 여부
    private String emyNgtYn;        // 야간 응급실 운영 여부

    public void saveCodeIntoDto(String code) {
        this.code = code;
    }
}
