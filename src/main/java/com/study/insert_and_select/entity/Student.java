package com.study.insert_and_select.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Student { // 객체의 변수명, 자료형이 일치해야함
	private int studentId;
	private String name;
	private int age;
}
