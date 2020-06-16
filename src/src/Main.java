package src;

/**
 * @author Andrey Negrash P3130
 * @version 1.0
 */

import java.io.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    String comand, dataName;
    String[] data, historyData;
    LinkedList<LabWork> labwork;
    LinkedList<History> history;
    File base;
    LabWork number;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     *
     * @throws IOException to throw IOExceptions
     */
    public static void main(String[] args) throws IOException {
        new Main();
    }

    /**
     *
     * @throws IOException to throw IOExceptions
     */
    Main() throws IOException {
        labwork = new LinkedList<>();
        history = new LinkedList<>();
        dataName = System.getenv("LAB_NEGRASH");
        base = new File(dataName);
        formatter = formatter.withLocale(Locale.US);

        System.out.println("Здравствуйте!");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(dataName));
            String line;
            while ((line = reader.readLine()) != null) {
                int index = line.lastIndexOf(';');
                if (index == -1) {
                    System.err.println("Файл с данными пустой");
                } else {
                    data = line.split(";");
                    int id = Integer.parseInt(data[0]);
                    String name = data[1];
                    Long x = Long.parseLong(data[2]);
                    Long y = Long.parseLong(data[3]);
                    ZonedDateTime creationDate = ZonedDateTime.parse(data[4]);
                    float minimalPoint = Float.parseFloat(data[5]);
                    Double personalQualitiesMinimum = Double.parseDouble(data[6]);
                    Difficulty difficulty = Difficulty.valueOf(data[7]);
                    String nameAuthor = data[8];
                    LocalDate birthday = LocalDate.parse(data[9], formatter);
                    float height = Float.parseFloat(data[10]);
                    Country nationality = Country.valueOf(data[11]);

                    labwork.add(new LabWork(id, name, new Coordinates(x,y), creationDate, minimalPoint, personalQualitiesMinimum, difficulty, new Person(nameAuthor, birthday, height, nationality)));
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        labwork.sort(new SortById());

        try {
            BufferedReader reader = new BufferedReader(new FileReader("history.csv"));
            String line;
            while ((line = reader.readLine()) != null) {
                int index = line.lastIndexOf(';');
                if (index == -1) {
                    System.err.println("Файл с историей пустой");
                } else {
                    historyData = line.split(";");
                    ZonedDateTime creationDate = ZonedDateTime.parse(historyData[0]);
                    String whatAct = historyData[1];

                    history.add(new History(creationDate, whatAct));
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true){
            takeComand();
        }

    }

    /**
     * Use to read comand and send it to program
     * @throws IOException to throw IOExceptions
     */
    void takeComand() throws IOException {
        System.out.println("Введите команду (для справки по всем командам введите help)");
        Scanner sc = new Scanner(System.in);
        comand = sc.nextLine();
        checkComand();
    }

    /**
     * Use to switch type of comand and do some acts with it
     * @throws IOException to throw IOExceptions
     */
    void checkComand() throws IOException {
        String[] s = comand.split(" ");
        switch (s[0]) {
            case "help":
                System.out.println("help : вывести справку по доступным командам\n" +
                        "info : вывести в стандартный поток вывода информацию о коллекции (тип, количество элементов, вес файла и т.д.)\n" +
                        "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                        "add {element} : добавить новый элемент в коллекцию\n" +
                        "update_id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                        "remove_by_id id : удалить элемент из коллекции по его id\n" +
                        "clear : очистить коллекцию\n" +
                        "save : сохранить коллекцию в файл\n" +
                        "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                        "exit : завершить программу (без сохранения в файл)\n" +
                        "add_if_max {element} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции\n" +
                        "remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный\n" +
                        "history : вывести последние 12 команд (без их аргументов)\n" +
                        "count_less_than_minimal_point minimalPoint : вывести количество элементов, значение поля minimalPoint которых меньше заданного\n" +
                        "filter_by_minimal_point minimalPoint : вывести элементы, значение поля minimalPoint которых равно заданному\n" +
                        "filter_less_than_personal_qualities_minimum personalQualitiesMinimum : вывести элементы, значение поля personalQualitiesMinimum которых меньше заданного");

                addToHistory("help");
                break;
            case "info":
                System.out.println("Тип коллекции: LinkedList");
                System.out.println("Количество элементов коллекции: " + labwork.size());
                System.out.println("Абсолютный путь файла хранения коллекции: " + base.getAbsolutePath());
                System.out.println("Вес файла хранения коллекции: " + base.length() + " байт");

                addToHistory("info");
                break;
            case "show":
                for (LabWork p : labwork) {
                    System.out.println(p.getInfo());
                }
                System.out.println();

                addToHistory("show");
                break;
            case "add":

                int idEnter;
                if (labwork.isEmpty()){
                    idEnter=0;
                }else{
                    idEnter = labwork.getLast().getId()+1;
                }
                String nameEnter;
                long xEnter, yEnter;
                float minimalPointEnter;
                double personalQalityEnter;
                Difficulty dificultyEnter;
                String authornameEnter, authorbirthEnter;
                float heightEnter;
                Country nationEnter;
                System.out.println("Введите имя:");
                Scanner sc = new Scanner(System.in);
                if(sc.hasNext()) {
                    nameEnter = sc.nextLine();
                } else {
                    System.err.println("Ошибка: Пустая строка");
                    break;
                }
                System.out.println("Введите координату X (тип Long):");
                if(sc.hasNextLong()) {
                    xEnter = sc.nextLong();
                } else {
                    System.err.println("Ошибка: Неверный формат");
                    break;
                }
                System.out.println("Введите координату Y (тип Long):");
                if(sc.hasNextLong()) {
                    yEnter = sc.nextLong();
                } else {
                    System.err.println("Ошибка: Неверный формат");
                    break;
                }
                System.out.println("Введите значение Minimal Point (тип Float):");

                if(sc.hasNextFloat()) {
                    float entMP = sc.nextFloat();
                    if(entMP>0) minimalPointEnter = entMP; else {
                        System.err.println("Ошибка: Неверное значение");
                        break;
                    }
                } else {
                    System.err.println("Ошибка: Неверный формат");
                    break;
                }
                System.out.println("Введите значение Personal Qualities Minimum (тип Double):");
                if(sc.hasNextDouble()) {
                    double entpqm = sc.nextDouble();
                    if(entpqm>0) personalQalityEnter = entpqm; else {
                        System.err.println("Ошибка: Неверное значение");
                        break;
                    }
                } else {
                    System.err.println("Ошибка: Неверный формат");
                    break;
                }
                System.out.println("Введите значение сложности (NORMAL, HARD, IMPOSSIBLE, TERRIBLE):");
                Scanner sc3 = new Scanner(System.in);
                String wtfDifficulty=sc3.nextLine();
                boolean exists = true;
                try {
                    Difficulty.valueOf(wtfDifficulty);
                } catch (IllegalArgumentException e) {
                    exists=false;
                }
                if(exists){

                    dificultyEnter=Difficulty.valueOf(wtfDifficulty);
                }else{
                    System.err.println("Ошибка: введённого значения не существует");
                    break;
                }
                System.out.println("Введите своё имя (автора):");
                if(sc3.hasNext()) {
                    authornameEnter= sc3.nextLine();
                } else {
                    System.err.println("Ошибка: Пустая строка");
                    break;
                }
                System.out.println("Вводим свою дату рождения (автора) в формате гггг-мм-дд:");
                System.out.println("Год в формате ГГГГ");
                int yearBA;
                if(sc.hasNextInt()) {
                    yearBA = sc.nextInt();
                    String yComp = yearBA+"";
                    if(yComp.length()==4 && yearBA>1900 && yearBA<2020) {
                        authorbirthEnter = yComp;
                    } else {
                        System.err.println("Ошибка: Неверный формат");
                        break;
                    }
                } else {
                    System.err.println("Ошибка: Неверные данные");
                    break;
                }

                System.out.println("Месяц в формате ММ");
                int monthBA;
                if(sc.hasNextInt()) {
                    monthBA = sc.nextInt();
                    if(monthBA>0 && monthBA<13) {
                        if(monthBA<10){
                            authorbirthEnter = authorbirthEnter+"-0"+monthBA;
                        }else{
                            authorbirthEnter = authorbirthEnter+"-"+monthBA;
                        }
                    } else {
                        System.err.println("Ошибка: Неверный формат");
                        break;
                    }
                } else {
                    System.err.println("Ошибка: Неверные данные");
                    break;
                }
                System.out.println("День в формате ДД");
                int dayBA;
                if(sc.hasNextInt()) {
                    dayBA = sc.nextInt();
                    if(dayBA>0 && dayBA<31) {
                        if(dayBA<10){
                            authorbirthEnter = authorbirthEnter+"-0"+dayBA;
                        }else{
                            authorbirthEnter = authorbirthEnter+"-"+dayBA;
                        }
                    } else {
                        System.err.println("Ошибка: Неверный формат");
                        break;
                    }
                } else {
                    System.err.println("Ошибка: Неверные данные");
                    break;
                }
                System.out.println("Введите свой рост (автора, тип Float):");
                if(sc.hasNextFloat()) {
                    float enth = sc.nextFloat();
                    if(enth>0) heightEnter = enth; else {
                        System.err.println("Ошибка: Неверное значение");
                        break;
                    }
                } else {
                    System.err.println("Ошибка: Неверный формат");
                    break;
                }
                System.out.println("Введите свою национальность (FRANCE, INDIA, ITALY, NORTH_KOREA):");
                Scanner sc4 = new Scanner(System.in);
                String wtfCountry=sc4.nextLine();
                boolean exists2 = true;
                try {
                    Country.valueOf(wtfCountry);
                } catch (IllegalArgumentException e) {
                    exists2=false;
                }
                if(exists2){

                    nationEnter=Country.valueOf(wtfCountry);
                }else{
                    System.err.println("Ошибка: введённого значения не существует");
                    break;
                }

                String name = nameEnter;
                long x = xEnter;
                long y = yEnter;
                ZonedDateTime creationDate = ZonedDateTime.now();
                float minimalPoint = minimalPointEnter;
                double personalQualitiesMinimum = personalQalityEnter;
                Difficulty difficulty = dificultyEnter;
                String nameAuthor = authornameEnter;
                LocalDate birthday = LocalDate.parse(authorbirthEnter, formatter);
                float height = heightEnter;
                Country nationality = nationEnter;

                labwork.add(new LabWork(idEnter, name, new Coordinates(x, y), creationDate, minimalPoint, personalQualitiesMinimum, difficulty, new Person(nameAuthor, birthday, height, nationality)));

                addToHistory("add");


                break;
            case "update_id": {
                System.out.println("Введите id элемента коллекции, которую хотите изменить");
                Scanner sc11 = new Scanner(System.in);
                int idUpd;
                if(sc11.hasNextInt()) {
                    idUpd = sc11.nextInt();
                    updateById(idUpd);
                } else {
                    System.err.println("Ошибка: Вы ввели не целое число");
                }
                addToHistory("update_id");
                break;
            }
            case "remove_by_id": {
                System.out.println("Введите id элемента коллекции, которую хотите удалить");
                Scanner sc12 = new Scanner(System.in);
                int idRem;
                if(sc12.hasNextInt()) {
                    idRem = sc12.nextInt();
                    removeById(idRem);
                } else {
                    System.err.println("Ошибка: Вы ввели не целое число");
                }

                addToHistory("remove_by_id");
                break;
            }
            case "clear":
                labwork.clear();

                addToHistory("clear");
                break;
            case "save":
                try (PrintWriter pw = new PrintWriter(dataName)) {
                    for (LabWork p : labwork) {
                        pw.println(p.getInfo());
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                addToHistory("save");
                break;
            case "execute_script":
                System.out.println("Введите имя файла с расширением (.txt)");
                Scanner sc2 = new Scanner(System.in);
                String scriptFileName= sc2.nextLine();
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(scriptFileName));
                    String line;
                    while ((line = reader.readLine()) != null) {

                        comand=line;
                        System.out.println(comand);
                        checkComand();
                    }
                    reader.close();
                } catch (FileNotFoundException e) {
                    System.err.println("Файл не найден");
                }
                addToHistory("execute_script");
                break;
            case "exit":
                addToHistory("exit");
                System.exit(0);
            case "add_if_max": {
                int maxVal = 0;
                for (LabWork p : labwork) {
                    if (p.getInfo().length() > maxVal) maxVal = p.getInfo().length();
                }
                if (labwork.isEmpty()){
                    idEnter=0;
                }else{
                    idEnter = labwork.getLast().getId()+1;
                }
                System.out.println("Введите имя:");
                Scanner sc6 = new Scanner(System.in);
                if(sc6.hasNext()) {
                    nameEnter = sc6.nextLine();
                } else {
                    System.err.println("Ошибка: Пустая строка");
                    break;
                }
                System.out.println("Введите координату X (тип Long):");
                if(sc6.hasNextLong()) {
                    xEnter = sc6.nextLong();
                } else {
                    System.err.println("Ошибка: Неверный формат");
                    break;
                }
                System.out.println("Введите координату Y (тип Long):");
                if(sc6.hasNextLong()) {
                    yEnter = sc6.nextLong();
                } else {
                    System.err.println("Ошибка: Неверный формат");
                    break;
                }
                System.out.println("Введите значение Minimal Point (тип Float):");
                if(sc6.hasNextFloat()) {
                    float entMP = sc6.nextFloat();
                    if(entMP>0) minimalPointEnter = entMP; else {
                        System.err.println("Ошибка: Неверное значение");
                        break;
                    }
                } else {
                    System.err.println("Ошибка: Неверный формат");
                    break;
                }
                System.out.println("Введите значение Personal Qualities Minimum (тип Double):");
                if(sc6.hasNextDouble()) {
                    double entpqm = sc6.nextDouble();
                    if(entpqm>0) personalQalityEnter = entpqm; else {
                        System.err.println("Ошибка: Неверное значение");
                        break;
                    }
                } else {
                    System.err.println("Ошибка: Неверный формат");
                    break;
                }
                System.out.println("Введите значение сложности (NORMAL, HARD, IMPOSSIBLE, TERRIBLE):");
                Scanner sc7 = new Scanner(System.in);
                String wtfDifficulty2=sc7.nextLine();
                boolean exists20 = true;
                try {
                    Difficulty.valueOf(wtfDifficulty2);
                } catch (IllegalArgumentException e) {
                    exists20=false;
                }
                if(exists20){

                    dificultyEnter=Difficulty.valueOf(wtfDifficulty2);
                }else{
                    System.err.println("Ошибка: введённого значения не существует");
                    break;
                }
                System.out.println("Введите своё имя (автора):");
                if(sc7.hasNext()) {
                    authornameEnter= sc7.nextLine();
                } else {
                    System.err.println("Ошибка: Пустая строка");
                    break;
                }
                System.out.println("Вводим свою дату рождения (автора) в формате гггг-мм-дд:");
                System.out.println("Год в формате ГГГГ");
                int yearBA2;
                if(sc7.hasNextInt()) {
                    yearBA2 = sc7.nextInt();
                    String yComp2 = yearBA2+"";
                    if(yComp2.length()==4 && yearBA2>1900 && yearBA2<2020) {
                        authorbirthEnter = yComp2;
                    } else {
                        System.err.println("Ошибка: Неверный формат");
                        break;
                    }
                } else {
                    System.err.println("Ошибка: Неверные данные");
                    break;
                }

                System.out.println("Месяц в формате ММ");
                if(sc7.hasNextInt()) {
                    monthBA = sc7.nextInt();
                    if(monthBA>0 && monthBA<13) {
                        if(monthBA<10){
                            authorbirthEnter = authorbirthEnter+"-0"+monthBA;
                        }else{
                            authorbirthEnter = authorbirthEnter+"-"+monthBA;
                        }
                    } else {
                        System.err.println("Ошибка: Неверный формат");
                        break;
                    }
                } else {
                    System.err.println("Ошибка: Неверные данные");
                    break;
                }
                System.out.println("День в формате ДД");
                if(sc7.hasNextInt()) {
                    dayBA = sc7.nextInt();
                    if(dayBA>0 && dayBA<31) {
                        if(dayBA<10){
                            authorbirthEnter = authorbirthEnter+"-0"+dayBA;
                        }else{
                            authorbirthEnter = authorbirthEnter+"-"+dayBA;
                        }
                    } else {
                        System.err.println("Ошибка: Неверный формат");
                        break;
                    }
                } else {
                    System.err.println("Ошибка: Неверные данные");
                    break;
                }
                System.out.println("Введите свой рост (автора, тип Float):");
                if(sc7.hasNextFloat()) {
                    float enth = sc7.nextFloat();
                    if(enth>0) heightEnter = enth; else {
                        System.err.println("Ошибка: Неверное значение");
                        break;
                    }
                } else {
                    System.err.println("Ошибка: Неверный формат");
                    break;
                }
                System.out.println("Введите свою национальность (FRANCE, INDIA, ITALY, NORTH_KOREA):");
                Scanner sc5 = new Scanner(System.in);
                String wtfCountry2=sc5.nextLine();
                boolean exists3 = true;
                try {
                    Country.valueOf(wtfCountry2);
                } catch (IllegalArgumentException e) {
                    exists3=false;
                }
                if(exists3){

                    nationEnter=Country.valueOf(wtfCountry2);
                }else{
                    System.err.println("Ошибка: введённого значения не существует");
                    break;
                }

                name = nameEnter;
                x = xEnter;
                y = yEnter;
                creationDate = ZonedDateTime.now();
                minimalPoint = minimalPointEnter;
                personalQualitiesMinimum = personalQalityEnter;
                difficulty = dificultyEnter;
                nameAuthor = authornameEnter;
                birthday = LocalDate.parse(authorbirthEnter, formatter);
                height = heightEnter;
                nationality = nationEnter;

                labwork.add(new LabWork(idEnter, name, new Coordinates(x, y), creationDate, minimalPoint, personalQualitiesMinimum, difficulty, new Person(nameAuthor, birthday, height, nationality)));

                if (labwork.getLast().getInfo().length() <= maxVal) {
                    labwork.remove(labwork.getLast());
                }
                addToHistory("add_if_max");

                break;
            }
            case "remove_lower": {

                if (labwork.isEmpty()){
                    idEnter=0;
                }else{
                    idEnter = labwork.getLast().getId()+1;
                }
                System.out.println("Введите имя:");

                Scanner sc9 = new Scanner(System.in);
                if(sc9.hasNext()) {
                    nameEnter = sc9.nextLine();
                } else {
                    System.err.println("Ошибка: Пустая строка");
                    break;
                }
                System.out.println("Введите координату X (тип Long):");
                if(sc9.hasNextLong()) {
                    xEnter = sc9.nextLong();
                } else {
                    System.err.println("Ошибка: Неверный формат");
                    break;
                }
                System.out.println("Введите координату Y (тип Long):");
                if(sc9.hasNextLong()) {
                    yEnter = sc9.nextLong();
                } else {
                    System.err.println("Ошибка: Неверный формат");
                    break;
                }
                System.out.println("Введите значение Minimal Point (тип Float):");
                if(sc9.hasNextFloat()) {
                    float entMP = sc9.nextFloat();
                    if(entMP>0) minimalPointEnter = entMP; else {
                        System.err.println("Ошибка: Неверное значение");
                        break;
                    }
                } else {
                    System.err.println("Ошибка: Неверный формат");
                    break;
                }
                System.out.println("Введите значение Personal Qualities Minimum (тип Double):");
                if(sc9.hasNextDouble()) {
                    double entpqm = sc9.nextDouble();
                    if(entpqm>0) personalQalityEnter = entpqm; else {
                        System.err.println("Ошибка: Неверное значение");
                        break;
                    }
                } else {
                    System.err.println("Ошибка: Неверный формат");
                    break;
                }
                System.out.println("Введите значение сложности (NORMAL, HARD, IMPOSSIBLE, TERRIBLE):");
                Scanner sc6 = new Scanner(System.in);
                String wtfDifficulty6=sc6.nextLine();
                boolean exists6 = true;
                try {
                    Difficulty.valueOf(wtfDifficulty6);
                } catch (IllegalArgumentException e) {
                    exists6=false;
                }
                if(exists6){

                    dificultyEnter=Difficulty.valueOf(wtfDifficulty6);
                }else{
                    System.err.println("Ошибка: введённого значения не существует");
                    break;
                }
                System.out.println("Введите своё имя (автора):");
                if(sc6.hasNext()) {
                    authornameEnter= sc6.nextLine();
                } else {
                    System.err.println("Ошибка: Пустая строка");
                    break;
                }
                System.out.println("Вводим свою дату рождения (автора) в формате гггг-мм-дд:");
                System.out.println("Год в формате ГГГГ");
                if(sc6.hasNextInt()) {
                    yearBA = sc6.nextInt();
                    String yComp = yearBA+"";
                    if(yComp.length()==4 && yearBA>1900 && yearBA<2020) {
                        authorbirthEnter = yComp;
                    } else {
                        System.err.println("Ошибка: Неверный формат");
                        break;
                    }
                } else {
                    System.err.println("Ошибка: Неверные данные");
                    break;
                }

                System.out.println("Месяц в формате ММ");
                if(sc6.hasNextInt()) {
                    monthBA = sc6.nextInt();
                    if(monthBA>0 && monthBA<13) {
                        if(monthBA<10){
                            authorbirthEnter = authorbirthEnter+"-0"+monthBA;
                        }else{
                            authorbirthEnter = authorbirthEnter+"-"+monthBA;
                        }
                    } else {
                        System.err.println("Ошибка: Неверный формат");
                        break;
                    }
                } else {
                    System.err.println("Ошибка: Неверные данные");
                    break;
                }
                System.out.println("День в формате ДД");
                if(sc6.hasNextInt()) {
                    dayBA = sc6.nextInt();
                    if(dayBA>0 && dayBA<31) {
                        if(dayBA<10){
                            authorbirthEnter = authorbirthEnter+"-0"+dayBA;
                        }else{
                            authorbirthEnter = authorbirthEnter+"-"+dayBA;
                        }
                    } else {
                        System.err.println("Ошибка: Неверный формат");
                        break;
                    }
                } else {
                    System.err.println("Ошибка: Неверные данные");
                    break;
                }
                System.out.println("Введите свой рост (автора, тип Float):");
                if(sc6.hasNextFloat()) {
                    float enth = sc6.nextFloat();
                    if(enth>0) heightEnter = enth; else {
                        System.err.println("Ошибка: Неверное значение");
                        break;
                    }
                } else {
                    System.err.println("Ошибка: Неверный формат");
                    break;
                }
                System.out.println("Введите свою национальность (FRANCE, INDIA, ITALY, NORTH_KOREA):");
                Scanner sc0 = new Scanner(System.in);
                String wtfCountry0=sc0.nextLine();
                boolean exists0 = true;
                try {
                    Country.valueOf(wtfCountry0);
                } catch (IllegalArgumentException e) {
                    exists0=false;
                }
                if(exists0){

                    nationEnter=Country.valueOf(wtfCountry0);
                }else{
                    System.err.println("Ошибка: введённого значения не существует");
                    break;
                }

                name = nameEnter;
                x = xEnter;
                y = yEnter;
                creationDate = ZonedDateTime.now();
                minimalPoint = minimalPointEnter;
                personalQualitiesMinimum = personalQalityEnter;
                difficulty = dificultyEnter;
                nameAuthor = authornameEnter;
                birthday = LocalDate.parse(authorbirthEnter, formatter);
                height = heightEnter;
                nationality = nationEnter;

                labwork.add(new LabWork(idEnter, name, new Coordinates(x, y), creationDate, minimalPoint, personalQualitiesMinimum, difficulty, new Person(nameAuthor, birthday, height, nationality)));
                for (LabWork p : labwork) {
                    if (labwork.getLast().getInfo().length() > p.getInfo().length()) labwork.remove(p);
                }
                labwork.remove(labwork.getLast());

                addToHistory("remove_lower");

                break;
            }
            case "history":
                for (History h : history) {
                    System.out.println(h.getHistory());
                }
                System.out.println();

                addToHistory("history");

                break;
            case "count_less_than_minimal_point": {
                System.out.println("Введите значение minimal_point для сравнения");
                int counter = 0;
                Scanner sc00 = new Scanner(System.in);
                float minpoint = Float.parseFloat(sc00.nextLine());
                for (LabWork p : labwork) {
                    if (minpoint > p.getMinimalPoint())
                        counter++;

                }
                System.out.println("Количество элементов, значение поля minimalPoint которых меньше заданного: " + counter);
                addToHistory("count_less_than_minimal_point");

                break;
            }
            case "filter_by_minimal_point": {
                System.out.println("Введите значение minimal_point для фильтрации");
                Scanner sc11 = new Scanner(System.in);
                float minpoint = Float.parseFloat(sc11.nextLine());
                for (LabWork p : labwork) {
                    if (minpoint == p.getMinimalPoint()) System.out.println(p.getInfo());
                }
                addToHistory("filter_by_minimal_point");
                break;
            }
            case "filter_less_than_personal_qualities_minimum": {
                System.out.println("Введите значение personal_qualities_minimum для фильтрации");
                Scanner sc22 = new Scanner(System.in);
                double minPoint = Double.parseDouble(sc22.nextLine());
                for (LabWork p : labwork) {
                    if (minPoint > p.getPQM()) System.out.println(p.getInfo());
                }
                addToHistory("filter_less_than_personal_qualities_minimum");
                break;
            }
            default:
                System.out.println("Такой команды не существует. Проверьте написание команды или ознакомьтес с полным списком при помощи help");
                break;
        }
    }

    /**
     *
     * @param idUpdate used to take updated id and send it to LabWork
     * @see LabWork
     */
    public void updateById(int idUpdate){
        int trueid=-1;

        for(LabWork p : labwork){
            int idCheck=p.getId();
            if (idCheck==idUpdate){
                trueid=idUpdate;
                number = p;
            }
        }
        if (trueid!=-1){
            System.out.println(number.getInfo());
            System.out.println("Какое поле вы бы хотели изменить? (id, name, coordinates.x, coordinates.y, creationDate, minimalPoint, personalQualitiesMinimum, difficulty, author.name, author.birthday, author.height, author.nationality)");
            Scanner sc = new Scanner(System.in);
            String field = sc.nextLine();

            switch (field) {
                case "id":
                    System.err.println("У Вас недостаточно прав для изменения id");
                    break;
                case "name":
                    System.out.print("На что меняем: ");
                    number.setName(sc.nextLine());

                    break;
                case "coordinates.x":
                    System.out.print("На что меняем: ");
                    Long xW = Long.parseLong(sc.nextLine());
                    number.setCoordinates(xW, "x");

                    break;
                case "coordinates.y":
                    System.out.print("На что меняем: ");
                    Long yW = Long.parseLong(sc.nextLine());
                    number.setCoordinates(yW, "y");

                    break;
                case "creationDate":
                    System.err.println("У Вас недостаточно прав для изменения даты создания (creationDate)");
                    break;
                case "minimalPoint":
                    System.out.print("На что меняем: ");
                    number.setMinimalPoint(Float.parseFloat(sc.nextLine()));

                    break;
                case "personalQualitiesMinimum":
                    System.out.print("На что меняем: ");
                    number.setPQM(Double.parseDouble(sc.nextLine()));

                    break;
                case "difficulty":
                    System.out.print("На что меняем: ");
                    number.setDif(Difficulty.valueOf(sc.nextLine()));

                    break;
                case "author.name":
                    System.out.print("На что меняем: ");
                    number.setAuthorName(sc.nextLine());

                    break;
                case "author.birthday":
                    System.out.print("На что меняем: ");
                    number.setAuthorBday(LocalDate.parse(sc.nextLine(), formatter));

                    break;
                case "author.height":
                    System.out.print("На что меняем: ");
                    number.setAuthorHeight(Float.parseFloat(sc.nextLine()));

                    break;
                case "author.nationality":
                    System.out.print("На что меняем: ");
                    number.setAuthorCountry(Country.valueOf(sc.nextLine()));

                    break;
                default:
                    System.err.println("Такого поля не существует");
                    break;
            }
        }else{
            System.err.println("Этот id не принадлежит ни одному элементу коллекции");
        }
    }

    /**
     *
     * @param idRemove used to take removed id and send it to LabWork
     * @see LabWork
     */
    public void removeById(int idRemove){
        int trueid=-1;

        for(LabWork p : labwork){
            int idCheck=p.getId();
            if (idCheck==idRemove){
                trueid=idRemove;
                number = p;
            }
        }
        if (trueid!=-1){
            labwork.remove(number);
        }else{
            System.err.println("Элемент с таким id не существует");
        }
    }

    /**
     *
     * @param myComand used to add it to history and save
     */
    public void addToHistory(String myComand){
        ZonedDateTime now = ZonedDateTime.now();
        if (history.size() >= 12) {
            history.removeLast();
        }
        history.addFirst(new History(now, myComand));

        try(PrintWriter pw = new PrintWriter("history.csv"))
        {
            for(History h : history){

                pw.println(h.getToWrite());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}