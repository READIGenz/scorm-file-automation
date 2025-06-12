
import com.fasterxml.jackson.databind.ObjectMapper;


public class Configuration {



    // Define a class that matches the structure of your JSON
   // public class Configuration {
        public String baseUrl;
        public String username;
        public static String organization;
        public String password;


    //public class JSONReader {
        public static void main(String[] args) {
            try {
                // Initialize the ObjectMapper
                ObjectMapper objectMapper = new ObjectMapper();

                // Read JSON data from the file into the Configuration object
                Configuration configuration = objectMapper.readValue(
                        Configuration.class.getClassLoader().getResourceAsStream("PNR.json"),
                        Configuration.class
                );

                // Access the data from the Configuration object
               // String baseUrl = configuration.credential;
                String username = configuration.username;
                String organization = Configuration.organization;
                String password = configuration.password;

               // System.out.println("Base URL: " + baseUrl);
                System.out.println("Username: " + username);
                System.out.println("Password: " + password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


