import java.util.stream.IntStream;

public class d {
    public static void main(String[] args) {
        System.out.println(IntStream.range(1, 10).filter(o -> {
            System.out.println("1");
            return true;
        }).peek(System.out::println).count());
    }
}
