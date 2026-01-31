package utilities;

import net.datafaker.Faker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

public class DataGenerator {
    private final Faker faker = new Faker();

    public final ContactUs contactUs;
    public final SignupCredential signup;
    public final ProductReview review;

    public DataGenerator() {
        this.contactUs = new ContactUs();
        this.signup = new SignupCredential();
        this.review = new ProductReview();
    }

    public class ContactUs {
        public String name;
        public String email;
        public String subject;
        public String message;

        public ContactUs(){
            name = getName();
            email = getInbox(name);
            subject = faker.internet().emailSubject();
            message = faker.lorem().characters(25,50);
        }
    }

    public class SignupCredential {
        public String fullName;
        public String email;
        public String password;
        public String dob;
        public String fullAddress;
        public String firstName;
        public String lastName;
        public String company;
        public String address;
        public String city;
        public String state;
        public String zipcode;
        public String mobile;

        public SignupCredential() {
            fullName = getName();
            email = getInbox(fullName);
            password = faker.credentials().password(8, 12, true, false, true);;
            dob = getDOB();

            fullAddress = faker.address().fullAddress();
            firstName = separateString(fullName, "\\s+")[0];
            lastName = separateString(fullName, "\\s+")[1];
            company = faker.company().name();
            mobile = faker.phoneNumber().phoneNumber();

            String[] separateAddress = separateString(fullAddress, ",");
            String[] stateZip = separateString(separateAddress[2].trim(), " ");
            address = separateAddress[0];
            city = separateAddress[1];
            state = stateZip[0];
            zipcode = stateZip[1];
        }

        private String[] separateString(String s, String regex) {
            return s.trim().split(regex);
        }

        private String getDOB() {
            /* site restriction: 1900 - 2021 */
            Instant from = LocalDate.of(1900, 1, 1).atStartOfDay(ZoneOffset.UTC).toInstant();
            Instant to = LocalDate.of(2021, 12, 31).atStartOfDay(ZoneOffset.UTC).toInstant();
            return faker.timeAndDate().between(from, to, "yyyy-MM-dd");
        }

    }

    public class ProductReview {
        public String name;
        public String email;
        public String review;

        public ProductReview() {
            name = getName();
            email = getInbox(name);
            review = faker.lorem().paragraph();
        }
    }

    private String getName() {
        return faker.name().firstName() + " " + faker.name().lastName();
    }

    private String getInbox(String name) {
        return faker.internet().safeEmailAddress(name);
    }

    public String generateSingleEmail() {
        return faker.internet().safeEmailAddress();
    }
}
