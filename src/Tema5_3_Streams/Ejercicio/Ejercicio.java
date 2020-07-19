package Tema5_3_Streams.Ejercicio;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Ejercicio {
    static List<Person> people;

    public static void main(String[] args) throws FileNotFoundException {
        readDatabase("database.csv");

        //exercise1();
        //exercise2('M');
        //exercise3();
        //exercise4();
        //exercise5();
        //exercise6("Mafioso");
        //exercise6("Engineer");
        //exercise7();
        //exercise8("Arts");
        exercise9();
    }

    static void exercise1() {
        people.stream().forEach(System.out::println);
        people.forEach(System.out::println);
    }

    static void exercise2(char sex) {
        people.parallelStream()
                .filter(p -> p.getSex() == sex)
                .map(Person::getFullName)
                .forEach(System.out::println);
    }


    static void exercise3() {
        List<String> lista = people.parallelStream()
                .map(Person::getCountry)
                .map(String::toUpperCase)
                .distinct()
                .sorted(String::compareTo)
                .collect(Collectors.toList());
        lista.forEach(System.out::println);
    }

    static void exercise4() {
        people.parallelStream()
                .map(Person::getContinent)
                .distinct()
                .forEach(System.out::println);

        // Siempre Europe
        people.parallelStream()
                .filter(person -> person.getContinent().equalsIgnoreCase("europe"))
                .map(person -> {
                    String year = person.getBirthYear2();
                    try {
                        // Esto es solo para esta clase, no hacer en casa
                        person.setBirthYear2(Integer.parseInt(year));
                        return person;
                    } catch (Exception e) {
                        System.out.println("ERROR");
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .filter(person -> person.getBirthYear() < 0)
                .forEach(person -> System.out.println(person.getFullName() + "\t" + person.getBirthYear2()));
    }

    static void exercise5() {
        BigInteger value = people.parallelStream()
                .map(person -> person.getPageViews())
                .reduce(BigInteger.ZERO, (suma, valor) -> suma.add(BigInteger.valueOf(valor)), BigInteger::add);

        BigInteger value2 = people.parallelStream()
                .reduce(BigInteger.ZERO, (suma, person) -> suma.add(BigInteger.valueOf(person.getPageViews())), BigInteger::add);

        people.parallelStream()
                .map(person -> BigInteger.valueOf(person.getPageViews()))
                .reduce(BigInteger::add).ifPresent(System.out::println);

        System.out.println(value);
        System.out.println(value2);
    }


    static void exercise6(String input) {
        //people.parallelStream()
        //.map(Person::getOccupacion)
        //.distinct()
        //.forEach( System.out::println );

        people.parallelStream()
                .filter(p -> p.getOccupacion().equalsIgnoreCase(input))
                .map(Person::getPopularity)
                .reduce(Math::max)
                .ifPresent(System.out::println);
    }


    static void exercise7() {
        people.parallelStream()
                .reduce((p1, p2) -> p1.getPopularity() > p2.getPopularity() ? p1 : p2)
                .ifPresent(System.out::println);

        people.parallelStream().max((p1, p2) -> Integer.compare(p1.getPopularity(), p2.getPopularity()))
                .ifPresent(System.out::println);
    }


    static void exercise8(String domain) {
        String phrase = people.parallelStream()
                .filter(person -> person.getDomain().equalsIgnoreCase(domain))
                .map(Person::getFullName)
                .collect(Collectors.joining(",\n", "En la base de datos:\n", "\npertenecen al dominio " + domain));
        System.out.println(phrase);
    }

    static void exercise9() {
        Map<String, Set<String>> values = people.parallelStream()
                .collect(Collectors.groupingBy(Person::getIndustry, Collectors.mapping(Person::getOccupacion, Collectors.toSet())));
        values.forEach((k, v) -> System.out.println(k + ":" + v));
    }


    static void readDatabase(String path) throws FileNotFoundException {
        BufferedReader bf = new BufferedReader(new FileReader(path));
        people = bf.lines()
                .skip(1)
                .map(line -> line.split(";"))
                .filter(tokens -> tokens.length == 17)
                .map(tokens -> {
                    try {
                        String fullName = tokens[1];
                        char sex = tokens[2].charAt(0);
                        //int birthYear = Integer.parseInt(tokens[3]);
                        String birthYear = tokens[3];
                        String city = tokens[4];
                        String country = tokens[6];
                        String continent = tokens[7];
                        String occupacion = tokens[10];
                        String industry = tokens[11];
                        String domain = tokens[12];
                        int languages = Integer.parseInt(tokens[13]);
                        int pageViews = Integer.parseInt(tokens[14]);
                        int popularity = Integer.parseInt(tokens[15]);
                        return new Person(fullName, sex, birthYear, city, country, continent, occupacion, industry, domain, languages, pageViews, popularity);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}

class Person{
    String fullName;
    char sex;
    String birthYear;

    public void setBirthYear2(int birthYear2) {
        this.birthYear2 = birthYear2;
    }

    int birthYear2;
    String city;
    String country;
    String continent;
    String occupacion;
    String industry;
    String domain;
    int languages;
    int pageViews;
    int popularity;

    public Person(String fullName, char sex, String birthYear, String city, String country, String continent, String occupacion, String industry, String domain, int languages, int pageViews, int popularity) {
        this.fullName = fullName;
        this.sex = sex;
        this.birthYear = birthYear;
        this.city = city;
        this.country = country;
        this.continent = continent;
        this.occupacion = occupacion;
        this.industry = industry;
        this.domain = domain;
        this.languages = languages;
        this.pageViews = pageViews;
        this.popularity = popularity;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public int getBirthYear() {
        return Integer.parseInt(birthYear);
    }

    public String getBirthYear2() {
        return birthYear;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getOccupacion() {
        return occupacion;
    }

    public void setOccupacion(String occupacion) {
        this.occupacion = occupacion;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getLanguages() {
        return languages;
    }

    public void setLanguages(int languages) {
        this.languages = languages;
    }

    public int getPageViews() {
        return pageViews;
    }

    public void setPageViews(int pageViews) {
        this.pageViews = pageViews;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    @Override
    public String toString() {
        return "Person{" +
                "fullName='" + fullName + '\'' +
                ", sex=" + sex +
                ", birthYear=" + birthYear +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", continent='" + continent + '\'' +
                ", occupacion='" + occupacion + '\'' +
                ", industry='" + industry + '\'' +
                ", domain='" + domain + '\'' +
                ", languages=" + languages +
                ", pageViews=" + pageViews +
                ", popularity=" + popularity +
                '}';
    }
}

