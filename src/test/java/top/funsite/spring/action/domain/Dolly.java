package top.funsite.spring.action.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
public class Dolly {

    private String name;

    private Integer size;

    private Boolean gender;

    private Date date;

    private LocalTime localTime;

    private LocalDate localDate;

    private LocalDateTime dateTime;

}
