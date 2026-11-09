import java.util.ArrayList;
import java.util.List;

public class QuestionBank {

    public static List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();

        questions.add(new Question("Which keyword is used to inherit a class in Java?",
                new String[]{"extends", "implements", "inherits", "super"}, 0));

        questions.add(new Question("Which of these is NOT a Java primitive type?",
                new String[]{"int", "boolean", "String", "char"}, 2));

        questions.add(new Question("What is the default value of a boolean field in Java?",
                new String[]{"true", "false", "0", "null"}, 1));

        questions.add(new Question("Which collection type does not allow duplicate elements?",
                new String[]{"ArrayList", "LinkedList", "HashSet", "Vector"}, 2));

        questions.add(new Question("Which method is the entry point of a Java application?",
                new String[]{"start()", "run()", "main()", "init()"}, 2));

        questions.add(new Question("Which keyword prevents a class from being subclassed?",
                new String[]{"static", "final", "private", "abstract"}, 1));

        questions.add(new Question("What does JVM stand for?",
                new String[]{"Java Virtual Machine", "Java Variable Method",
                        "Java Verified Module", "Java Visual Machine"}, 0));

        questions.add(new Question("Which operator performs logical AND in Java?",
                new String[]{"&", "&&", "AND", "|"}, 1));

        questions.add(new Question("Which of these is a checked exception?",
                new String[]{"NullPointerException", "ArithmeticException",
                        "IOException", "ArrayIndexOutOfBoundsException"}, 2));

        questions.add(new Question("Which access modifier restricts visibility to the same class only?",
                new String[]{"public", "protected", "default", "private"}, 3));

        return questions;
    }
}