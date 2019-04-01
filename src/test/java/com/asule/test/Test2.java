package com.asule.test;

import junit.framework.TestCase;

import java.util.*;

public class Test2 extends TestCase{

    class Person implements Comparable<Person>{
        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }

        @Override
        public int compareTo(Person o) {
            return this.age-o.age;
        }

/*        @Override
        public int hashCode() {
            return name.hashCode()+age;
        }

        @Override
        public boolean equals(Object obj) {
            Person p= (Person) obj;
            return this.name.equals(p.name)&&this.age==p.age;
        }*/
    }



    public void test0(){
        Set collection=new TreeSet();
/*
        collection.add("1");
        collection.add("z");
        collection.add("a");
        collection.add("x");
        collection.add("ax");
*/

        collection.add(new Person("asule",21));
        collection.add(new Person("asule1",22));
        collection.add(new Person("asule2",23));
        collection.add(new Person("asule3",24));
        collection.add(new Person("asule",21));

        Iterator iterator = collection.iterator();
        while (iterator.hasNext()){
            Person p= (Person) iterator.next();

            System.out.println(p.toString());
        }
    }
}
