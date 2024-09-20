package in.ApniDukaan.Utils;

import in.ApniDukaan.Entities.ProductOrder;
import in.ApniDukaan.Entities.UserEntity;
import in.ApniDukaan.Services.Interfaces.UserEntityService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

@Component
@RequiredArgsConstructor
public class CommonUtils {

    private final JavaMailSender mailSender;
    private final UserEntityService userService;

    public Boolean sendMail(String url, String reciepentEmail) throws
            UnsupportedEncodingException, MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        // TODO: will change the mail
        helper.setFrom("avj.patra@gmail.com", "Shopping Cart");
        helper.setTo(reciepentEmail);

        String content = "<p>Hello,</p>" + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>" + "<p><a href=\"" + url
                + "\">Change my password</a></p>";
        helper.setSubject("Password Reset");
        helper.setText(content, true);
        mailSender.send(message);
        return true;
    }

    public static String generateUrl(HttpServletRequest request) {

        // http://localhost:8080/forgot-password
        String siteUrl = request.getRequestURL().toString();

        return siteUrl.replace(request.getServletPath(), "");
    }

    String msg = null;
    ;

    public Boolean sendMailForProductOrder(ProductOrder order, String status) throws Exception {

        msg = "<p>Hello [[name]],</p>"
                + "<p>Thank you order <b>[[orderStatus]]</b>.</p>"
                + "<p><b>Product Details:</b></p>"
                + "<p>Name : [[productName]]</p>"
                + "<p>Category : [[category]]</p>"
                + "<p>Quantity : [[quantity]]</p>"
                + "<p>Price : [[price]]</p>"
                + "<p>Payment Type : [[paymentType]]</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("daspabitra55@gmail.com", "Shooping Cart");
        helper.setTo(order.getOrderAddress().getEmail());

        msg = msg.replace("[[name]]", order.getOrderAddress().getFirstName());
        msg = msg.replace("[[orderStatus]]", status);
        msg = msg.replace("[[productName]]", order.getProduct().getTitle());
        msg = msg.replace("[[category]]", order.getProduct().getCategory());
        msg = msg.replace("[[quantity]]", order.getQuantity().toString());
        msg = msg.replace("[[price]]", order.getPrice().toString());
        msg = msg.replace("[[paymentType]]", order.getPaymentType());

        helper.setSubject("Product Order Status");
        helper.setText(msg, true);
        mailSender.send(message);
        return true;
    }

    public UserEntity getLoggedInUserDetails(Principal p) {
        String email = p.getName();
        return userService.getUserByEmail(email);
    }
}
