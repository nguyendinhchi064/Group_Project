//package Login_Register;
//
//import java.util.Arrays;
//import java.util.HashSet;
//
//import io.smallrye.jwt.build.Jwt;
//
//public class TokenGenerator {
//    public static String generate(String username) {
//        String token =
//                Jwt.upn(username)
//                        .groups(new HashSet<>(Arrays.asList("User", "Admin")))
//                        .sign();
//        System.out.println("DEBUG########");
//        System.out.println(token);
//        return token;
//    }
//}
