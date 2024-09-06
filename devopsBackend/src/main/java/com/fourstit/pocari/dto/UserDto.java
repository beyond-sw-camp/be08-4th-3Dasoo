package com.fourstit.pocari.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String id;
    private String pwd;
    private String name;
    private String email;
    private LocalDate birth;
    private String gender;
    private String phone;
    private List<String> interest;
}
