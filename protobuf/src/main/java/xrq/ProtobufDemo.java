package xrq;

import xrq.PersonProtos.Person;

public class ProtobufDemo {

    public byte[] buildPerson(String name, int id, String email) {
        Person person = Person.newBuilder()
                .setName(name)
                .setId(id)
                .setEmail(email)
                .build();

        byte[] data = person.toByteArray();

        return data;
    }

    public String parsePersonName(byte[] data) throws Exception {
        Person parsedPerson = Person.parseFrom(data);

        return parsedPerson.getName();
    }

    public int parsePersonId(byte[] data) throws Exception {
        Person parsedPerson = Person.parseFrom(data);

        return parsedPerson.getId();
    }

    public String parsePersonEmail(byte[] data) throws Exception {
        Person parsedPerson = Person.parseFrom(data);

        return parsedPerson.getEmail();
    }

    public static void main(String[] args) throws Exception {
        ProtobufDemo protobufDemo = new ProtobufDemo();
        byte[] data = protobufDemo.buildPerson("xrq", 123456789, "123456789@qq.com");
        System.out.println("Name: " + protobufDemo.parsePersonName(data));
        System.out.println("Id: " + protobufDemo.parsePersonId(data));
        System.out.println("Email: " + protobufDemo.parsePersonEmail(data));
    }
}
