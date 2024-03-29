package Exceptions.Attestation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.DataFormatException;

public class Main {
    public static void main(String[] args) throws NameFormatException, PhoneFormatException,
            DataFormatException, GenderFormatException, IOException {

        String contact = create_contact();
        String[] array = contact.split(" ");

        if (array.length > 6)
            throw new IllegalArgumentException("Введены лишние данные! \n Повторите, пожалуйста ввод!");
        if (array.length < 6)
            throw new IllegalArgumentException("Не все данные внесены! \n Повторите, пожалуйста ввод!");

        WriterToFile writer = new WriterToFile();
        Map<String, String> mapContact = contactTypes(array);
        String path = new String(writer.getPath(mapContact));
        writer.writeToFile(writer.createStringBuilder(mapContact), path);
    }

    public static String create_contact(){
        System.out.println("Введите данные контакта через пробел: \n " +
                "ФИО; \n дата рождения в формате dd.mm.yyyy; \n номер телефона; \n пол - латиницей f или m: \n ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static Map<String, String> contactTypes(String[] array){
        Map<String, String> mapContact = new HashMap<>();
        DateFormat df = new SimpleDateFormat("dd.mm.yyyy");
        Date date;
        double phone;

        for (String element: array) {
            if (element.length() == 1) {
                if (element.equals("m") || element.equals("f")) {
                    mapContact.put("gender",element);
//                    String gender = element;
                } else throw new GenderFormatException();
            }
            else {
                if (element.matches("\\d{1,2}\\.\\d{1,2}\\.\\d{4}")){
                    try {
                        date = df.parse(element);
                    } catch (DateFormatException e){
                        System.out.println(e.getMessage());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    String[] arrayDate = element.split("\\.");
//                        boolean flag = false;
                    if (Integer.parseInt(arrayDate[0]) < 0 ||
                            Integer.parseInt(arrayDate[0]) > 31 ||
                            Integer.parseInt(arrayDate[1]) < 0 ||
                            Integer.parseInt(arrayDate[1]) > 12 ||
                            Integer.parseInt(arrayDate[2]) < 1800 ||
                            Integer.parseInt(arrayDate[2]) > 2024 ) {
                        throw new DateFormatException();
                    }
                    else mapContact.put("birthDate",element);
                }
                else {
                    if (element.matches(".*\\d.*")) {
                        try {
                            phone = Double.parseDouble(element);
                        } catch (NumberFormatException e) {
                            throw new PhoneFormatException();
                        }
                        mapContact.put("phoneNumber", element);
                    }
                    else {
                        if (!mapContact.containsKey("surname")){
                            mapContact.put("surname",element);
                        }
                        else {
                            if (!mapContact.containsKey("name")){
                                mapContact.put("name",element);
                            }
                            else {
                                if (!mapContact.containsKey("patronymic")){
                                    mapContact.put("patronymic",element);
                                }
                                else throw new NameFormatException();
                            }
                        }
                    }
                }
            }
        }
        return mapContact;
    }
//    FileWriter fileWriter = null;
//        try {
//        fileWriter = new FileWriter(contactTypes() + ".txt", true);
//        fileWriter.write(thename + " " + name + " " + secondname + " " + birthdate + " " + phonenumber + " " + gender + "\n");
//        fileWriter.close();
//    } catch (IOException e) {
//        throw new RuntimeException(e);
//    }
}

class DateFormatException extends RuntimeException{
        public DateFormatException(){
            super("Неверный формат даты рождения.");
        }
}
class PhoneFormatException extends RuntimeException{
    public PhoneFormatException(){
        super("Неверный формат номера телефона. Следует использовать только цифры.");
    }
}

class GenderFormatException extends RuntimeException{
    public GenderFormatException(){
        super("Неверный формат пола. Следует использовать только f или m.");
    }
}

class NameFormatException extends RuntimeException{
    public NameFormatException(){
        super("Неверный формат имени. Имя должно содержать только буквы");
    }
}

class WriterToFile {

    public WriterToFile() {}

    public StringBuilder createStringBuilder(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("<").append(map.get("surname")).append(">");
            sb.append("<").append(map.get("name")).append(">");
            sb.append("<").append(map.get("patronymic")).append(">");
            sb.append("<").append(map.get("birthDate")).append(">");
            sb.append("<").append(map.get("phoneNumber")).append(">");
            sb.append("<").append(map.get("gender")).append(">").append("\n");
        } catch (IllegalArgumentException e) {
            e.getMessage();
        }
        return sb;
    }

    public String getPath(Map<String, String> map) {
        String path = new String(map.get("surname") + ".txt");
        return path;
    }

    public void writeToFile(StringBuilder sb, String path) throws IOException {
        FileWriter writer = new FileWriter(new File(path), true);
        try {
            writer.write(String.valueOf(sb));
            writer.write("\n");
            writer.flush();
            writer.close();
            System.out.println("Новая запись успешно добавлена в файл " + path);
        } catch (IOException e) {
            System.out.println("Не удалось сделать запись в файл.");
        }
    }
}



