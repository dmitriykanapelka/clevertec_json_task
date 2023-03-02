import example.Floor;
import example.Passport;
import example.Person;
import example.Pet;
import service.DefaultJsonParser;
import service.JsonParser;
import service.SimpleJsonParser;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
//        final List<Pet> pets = List.of(new Pet("Archer", 4), new Pet("Tuzik", 1));
//        final Passport passport = new Passport("HB2022041");
//        final List<String> phones = List.of("80291199564", "80291200038");
//        final Person person = new Person("Dima", 21,pets,passport, phones);
//
//        String personJson = """
//                {
//                    "name" : "Dima",
//                    "age" : 21,
//                    "pets" : [
//                                {"name":"Archer", "age" :4},
//                                {"name":"Tuzik", "age" :1}
//                             ],
//                    "passport" : {"number" : "HB2022041"},
//                    "phones" : ["80291199564", "80291200038"]
//                }
//                """;
//
//        JsonParser<Person> personParser = new DefaultJsonParser<>();
//        String s = personParser.parseToJson(person);
//        Person p = personParser.parseToObject(personJson);


//        String petJson = """
//                {"name":"Archer", "age" :4}
//                """;
//
//        String floorJson = """
//                {"number":"12", "flatNumbers":[1,2,3], "passports":[{"number":"HB20222"},{"number":"HB20444"}]}
//                """;
//
//        SimpleJsonParser<Floor> simpleJsonParser = new SimpleJsonParser<>();
//        Floor floor = simpleJsonParser.parseToSimpleObject(floorJson, Floor.class);
//        System.out.println(floor);

        JsonParser<Pet> petParser = new DefaultJsonParser<>();
        petParser.parseToJson(new Pet(4,"Archer"));
    }


}
