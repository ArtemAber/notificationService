package notificationService.domainservice;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import notificationService.domain.users.UserModel;
import notificationService.domain.users.UserRoles;
import notificationService.infrastructure.database.UserDAO;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Service
public class SecurityService {

    @Inject
    private Jackson2ObjectMapperBuilder mapperBuilder;

    @Inject
    private UserDAO userDAO;

    public Boolean checkToken(String token) {

        Key key = new SecretKeySpec(Base64.getDecoder().decode("secret"), SignatureAlgorithm.HS256.getJcaName());
        Jws<Claims> jwt = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
        UserModel user;
        try {
            user = mapperBuilder.build().readValue(jwt.getBody().get("user").toString(), UserModel.class);
        } catch (Exception e) {
            return false;
        }
        if (userDAO.getUser(user.getLogin(), user.getPassword()) != null
                && user.getRole() == UserRoles.ADMIN
                && jwt.getBody().getExpiration().after(new Date())) {
            return true;
        } else {
            return false;
        }
    }
}
