package cn.xxyangyoulin.android_rxjava_demo;

import java.util.List;

public class School {

  private String name;
  private List<Student> studentList;

  public List<Student> getStudentList() {
      return studentList;
  }
  public void setStudentList(List<Student> studentList) {
      this.studentList = studentList;
  }
  public String getName() {
      return name;
  }
  public void setName(String name) {
      this.name = name;
  }
  public static class Student{
      private String name;
      public String getName() {
          return name;
      }
      public Student setName(String name) {
          this.name = name;
          return this;
      }
  }
}