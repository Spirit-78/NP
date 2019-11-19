import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

class Date{
    private int day, month, year;
    public Date(String date){
        year = Integer.parseInt(date.substring(0,4));
        month = Integer.parseInt(date.substring(5,7));
        day = Integer.parseInt(date.substring(8));
    }

    int getDay(){return day;}
    int getMonth(){return month;}
    int getYear(){return year;}


    public boolean compareTo(Date o) {
        if(this.year > o.year) return true;
        else if(this.year == o.year && this.month > o.month)
            return true;
        else if(this.year == o.year && this.month == o.month && this.day > o.day)
            return true;
        return false;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

abstract class Contact{

    private Date date;
    public Contact(String date){
    this.date = new Date(date);
    }

    boolean isNewerThan(Contact c){
        return this.date.compareTo(c.date);
    }

    public abstract String getType();

    public abstract String getPhone();

    public abstract String getEmail();

    //public abstract String getPhone();
}

class EmailContact extends Contact{

    String email;
    public EmailContact(String date,String email){
        super(date);
        this.email = email;
    }
    public String getEmail(){return email;}

    @Override
    public String getType() {
        return "Email";
    }

    @Override
    public String getPhone() {
        return null;
    }
}

class PhoneContact extends Contact{
    String phone;
    enum Operator{VIP, ONE, TMOBILE};

    public PhoneContact(String date, String phone){
        super(date);
        this.phone = phone;
    }
    public String getPhone(){return phone;}

    @Override
    public String getEmail() {
        return null;
    }

    Operator getOperator(){
        int op = Integer.parseInt(phone.substring(2,3));
        if(op == 0 || op == 1 || op == 2)
            return Operator.TMOBILE;
        else if(op == 5 || op == 6)
            return Operator.ONE;
        else
            return Operator.VIP;
    }

    @Override
    public String getType() {
        return "Phone";
    }
}

class Student{
    ArrayList<Contact> contacts;
    String firstName, lastName, city;
    int age;
    long index;
    public Student(String firstName, String lastName, String city, int age, long index)
    {
        this.firstName = firstName; this.lastName = lastName; this.city = city;
        this.age = age; this.index = index;
        contacts = new ArrayList<>();
    }
    void addEmailContact(String date, String email){
        contacts.add(new EmailContact(date,email));
    }
    void addPhoneContact(String date, String phone){
        contacts.add(new PhoneContact(date,phone));
    }

    Contact [] getEmailContacts(){
        return contacts.stream().filter(x -> x.getType() == "Email").toArray(Contact[]::new);
    }
    Contact [] getPhoneContacts(){
        return contacts.stream().filter(x -> x.getType() == "Phone").toArray(Contact[]::new);
    }

    String getCity(){return city;}
    String getFullName(){return firstName + " " + lastName;}
    long getIndex(){return index;}
    Contact getLatestContact(){
        AtomicReference<Contact> max = new AtomicReference<>(contacts.get(0));
        int index = 0;
        contacts.stream().forEach(con -> {
            if(con.isNewerThan(max.get()))
                max.set(con);
        });
        return max.get();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("{\"ime\":\"%s\", \"prezime\":\"%s\", \"vozrast\":%d," +
                        " \"grad\":\"%s\", \"indeks\":%d, \"telefonskiKontakti\":[",
                firstName,lastName,age,city,index));
        int len = sb.length();
        contacts.stream().filter(x -> x.getType() == "Phone").forEach(x ->
                sb.append(String.format("\"%s\", ", x.getPhone())));
        if(sb.length() == len)
            sb.append("]");
        else
            sb.replace(sb.length()-2,sb.length(),"]");
        sb.append(", \"emailKontakti\":[");
        len = sb.length();
        contacts.stream().filter(x -> x.getType() == "Email").forEach(x ->
                sb.append(String.format("\"%s\", ", x.getEmail())));
        if(sb.length() == len)
            sb.append("]}");
        else
            sb.replace(sb.length()-2,sb.length(),"]}");

        return sb.toString();
    }
}

class Faculty{
    String name;
    Student[] students;
    public Faculty(String name, Student [] students){
        this.name = name;
        this.students = students.clone();
    }
    int countStudentsFromCity(String cityName){
        return (int) Arrays.stream(students).filter(x -> x.getCity().equals(cityName)).count();
    }
    Student getStudent(long index){
        return Arrays.stream(students).filter(x->x.getIndex() == index).findFirst().get();
    }
    double getAverageNumberOfContacts(){
        DoubleSummaryStatistics db = new DoubleSummaryStatistics();
        Arrays.stream(students).forEach(x -> db.accept(x.contacts.size()));
        return db.getAverage();
    }
    Student getStudentWithMostContacts(){
        int max = Arrays.stream(students).mapToInt(x -> x.contacts.size())
                .max().orElse(-1);
        long index  = Arrays.stream(students).filter(x -> x.contacts.size() == max)
                .flatMapToLong(x->
                LongStream.of(x.getIndex()))
                .max().orElse(-1);
        return Arrays.stream(students).filter(x->x.getIndex() == index).findFirst().get();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("{\"fakultet\":\"%s\", \"studenti\":[",name));
        Arrays.stream(students).forEach(student -> sb.append(student.toString()).append(", "));
        sb.replace(sb.length()-2,sb.length(),"]}");
        return sb.toString();
    }
}

public class ContactsTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0
                            && faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}
