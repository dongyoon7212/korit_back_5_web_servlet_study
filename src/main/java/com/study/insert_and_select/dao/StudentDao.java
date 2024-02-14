package com.study.insert_and_select.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.study.insert_and_select.config.DBConfig;
import com.study.insert_and_select.entity.Student;

public class StudentDao {
	private static StudentDao instance;
	
	private StudentDao() {}
	
	public static StudentDao getInstance() {
		if(instance == null) {
			instance = new StudentDao();
		}
		return instance;
	}
		
	
	
	public Student findStudentByName(String name) { // select
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Student student = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");	// 데이터베이스 커넥트 드라이브 클래스 이름
			
			con = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
			String sql = "select * from student_tb where student_name = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				student = Student.builder()
						.studentId(rs.getInt(1))
						.name(rs.getString(2))
						.age(rs.getInt(3))
						.build();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(pstmt != null) { // null체크를 해야함
					pstmt.close();
				}
				if(con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return student;
	}
	
	public int saveStudent(Student student) { // insert
		Connection con = null;
		PreparedStatement pstmt = null;
		int successCount = 0;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");	// 데이터베이스 커넥트 드라이브 클래스 이름
			
			con = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
			String sql = "insert into student_tb(student_name, student_age) values (?, ?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, student.getName());
			pstmt.setInt(2, student.getAge());
			successCount = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) { // null체크를 해야함
					pstmt.close();
				}
				if(con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return successCount;
	}
	
	public List<Student> getStudentListAll() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Student> students = new ArrayList<>();
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");	// 데이터베이스 커넥트 드라이브 클래스 이름
			
			con = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
			String sql = "select * from student_tb";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Student student = Student.builder()
						.studentId(rs.getInt(1))
						.name(rs.getString(2))
						.age(rs.getInt(3))
						.build();
				
				students.add(student);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(pstmt != null) { // null체크를 해야함
					pstmt.close();
				}
				if(con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return students;
	}
}
