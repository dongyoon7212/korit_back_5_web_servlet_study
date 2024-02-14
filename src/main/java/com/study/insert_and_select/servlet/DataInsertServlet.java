package com.study.insert_and_select.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.study.insert_and_select.dao.StudentDao;
import com.study.insert_and_select.entity.Student;

@WebServlet("/data/addition")
public class DataInsertServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public DataInsertServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder builder = new StringBuilder(); // 메모리를 할당시켜놓고 한번에 메모리에 추가를 시킨다.
		
		String readData = null;
		
		BufferedReader reader = request.getReader();
		
		/** 
		 * json형식
		 * "{
		 * 		"name": "이동윤",
		 * 		"age": 26,
		 * }"
		 */
		
		while((readData = reader.readLine()) != null) { // 한 줄씩 가져옴
			builder.append(readData);
		}
		
		Gson gson = new Gson();
		
		// Json -> Map, 키값을 다 외우고 있어야 함
		Map<String, Object> map = gson.fromJson(builder.toString(), Map.class); // Map 객
		
		System.out.println(map);
		System.out.println(map.get("name"));
		System.out.println(map.get("age"));
		
		// Json -> 객체(entity 객체), 객체방법을 쓰는게 좋다. 키값을 외우지 않아도 됨 => DTO
		Student student = gson.fromJson(builder.toString(), Student.class);
		
		System.out.println(student);
		System.out.println(student.getName());
		System.out.println(student.getAge());
		
		StudentDao studentDao = StudentDao.getInstance();
		
		Student findStudent = studentDao.findStudentByName(student.getName());
		
		if(findStudent != null) { // 이미 데이터베이스에 이름이 존재하고 있다
			Map<String, Object> errorMap = new HashMap<>();
			errorMap.put("errorMessage", "이미 등록된 이름입니다");
			
			response.setStatus(400);
			response.setContentType("application/json");
			response.getWriter().println(gson.toJson(errorMap));
			return;
		}
		
		int successCount = studentDao.saveStudent(student);
		
		Map<String, Object> responseMap = new HashMap<>();
		
		responseMap.put("status", 201);
		responseMap.put("data", "응답데이터");
		responseMap.put("successCount", successCount);
		
		response.setStatus(201);
		response.setContentType("application/json");
		
		PrintWriter writer = response.getWriter(); // PrintWriter 아웃풋
		writer.println(gson.toJson(responseMap));
		
	}

}
