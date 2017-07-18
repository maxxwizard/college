public class ZooWooYoo {
    public static void main(String[] args) {
        Woo w1 = new Woo();
        Zoo z1 = new Woo();
        Zoo z2 = new Yoo();
        System.out.println(w1.one() + " " + w1.two() + " " + w1.extra());
        System.out.println(z1.one() + " " + z1.two() + " " + z1.extra());
        System.out.println(z2.one() + " " + z2.two() + " " + z2.extra());
    }
}