package com.fourstit.pocari.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tb_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_no")
    private Long userNo; // PK

    @Column(name = "id", nullable = false)
    private String id; // 아이디

    @Column(name = "pwd", nullable = false)
    private String pwd; // 비밀번호

    @Column(name = "name")
    private String name; // 이름

    @Column(name = "email")
    private String email; // 이메일

    @Column(name = "birth")
    private LocalDate birth; // 생일

    @Column(name = "gender", length = 1)
    private String gender; // 성별

    @Column(name = "phone")
    private String phone; //핸드폰

    @Column(name = "interest")
    private String interest; // 관심사

    @Column(name = "ins_date")
    private LocalDate insDate; // 가입일

    @Column(name = "upt_date")
    private LocalDate uptDate; // 수정일

}
