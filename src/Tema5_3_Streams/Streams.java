package Tema5_3_Streams;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Streams {
    public static void main(String[] args) {
        List<Tema5_3_Streams.Person> persons = Arrays.asList(
                new Tema5_3_Streams.Person("Max", 18),
                new Tema5_3_Streams.Person("Peter", 23),
                new Tema5_3_Streams.Person("Pamela", 23),
                new Tema5_3_Streams.Person("David", 12),
                new Tema5_3_Streams.Person("Enrique", 12));

        List<Tema5_3_Streams.Person> filtered = persons.stream()
                .map(p -> {
                    // Problema al modificar el objeto
                    // FIXME: no tocar las cosas de no tocar
                    p.age += 6;
                    return p;
                })
                .filter(p ->
                {
                    return p.name.startsWith("P");
                })
                // Método sencillo
                //.collect(Collectors.toList());
                // Método super especial
                //.collect(() -> new ArrayList<Person>(20), (list,persona)-> list.add(persona), (lista1,lista2) -> {
                //lista1.addAll(lista2);
                //});
                // Método especial reducido
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        System.out.println(filtered);

        Map<Integer, List<Tema5_3_Streams.Person>> personsByAge = persons.stream()
                .collect(Collectors.groupingBy(p -> p.age));

        //var personsByAge2 = persons.stream(); java 10 only
        Stream<Tema5_3_Streams.Person> personsByAge2 = persons.stream();
        personsByAge2.collect(Collectors.groupingBy(p -> p.age));


        Double averageAge = persons
                .stream()
                .collect(Collectors.averagingInt(p -> p.age));


        IntSummaryStatistics ageSummary = persons
                .stream()
                .collect(Collectors.summarizingInt(Tema5_3_Streams.Person::getAge));

        String phrase = persons.stream()
                .filter(p -> p.age >= 18)
                //.dropWhile(person -> person.age < 23) //java 9
                .map(p -> p.name)
                .collect(Collectors.joining
                        (" and ", "In Germany ", " are of legal age."));


        persons
                .stream()
                .reduce((p1, p2) -> p1.age > p2.age ? p1 : p2)
                .ifPresent(System.out::println);
        // Lo mismo
        persons
                .stream()
                .reduce((p1, p2) -> p1.age > p2.age ? p1 : p2)
                .ifPresent(x -> System.out.println(x));


        //var personaIdentidad = new Person("", 0); //java 10 only
        Tema5_3_Streams.Person personaIdentidad = new Tema5_3_Streams.Person("", 0);
        Tema5_3_Streams.Person result = persons
                .stream()
                .reduce(personaIdentidad, (p1, p2) -> {
                    p1.age += p2.age;
                    p1.name += p2.name;
                    return p1;
                });
        //System.out.println(personaIdentidad);
        //System.out.format("name=%s; age=%s", result.name, result.age);

        Integer ageSum = persons.parallelStream()
                .filter(p -> {
                    System.out.format("filter: %s\n", Thread.currentThread().getName());
                    return p.age >= 18;
                })
                .map(p -> {
                    // Map sin sentido
                    System.out.format("map: %s\n", Thread.currentThread().getName());
                    return p;
                })
                .sorted((s1, s2) -> {
                    System.out.format("sort: %s <> %s [%s]\n", s1, s2, Thread.currentThread().getName());
                    return s1.compareTo(s2);
                })
                .reduce(0, (sum, p) -> {
                            System.out.println("Suma " + sum + " " + p);
                            System.out.format("red1: %s\n", Thread.currentThread().getName());
                            return sum + p.age;
                        }, (sum1, sum2) ->
                        {
                            System.out.println("Suma2 " + sum1 + " " + sum2);
                            System.out.format("red2: %s\n", Thread.currentThread().getName());
                            return sum1 + sum2;
                        }
                );


        System.out.println(ageSum);
    }


}

class Person {
    String name;
    int age;

    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String toString() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int compareTo(Tema5_3_Streams.Person p2) {
        return Integer.compare(this.age, p2.age);
    }
}
