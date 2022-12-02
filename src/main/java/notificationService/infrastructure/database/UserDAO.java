package notificationService.infrastructure.database;

import notificationService.domain.users.UserModel;
import org.hibernate.SessionFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.transaction.Transactional;

@Repository
public class UserDAO {

    @Inject
    private SessionFactory sessionFactory;

    @Inject
    private Jackson2ObjectMapperBuilder mapperBuilder;

//    @Override
//    @Transactional
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        ObjectMapper objectMapper = mapperBuilder.build();
//        UserEntity userEntity = sessionFactory.getCurrentSession().createQuery("select user from UserEntity user where user.login=:login", UserEntity.class)
//                .setParameter("login", username).getSingleResult();
//        if (userEntity == null) {
//            throw new UsernameNotFoundException("Данный пользователь не зарегистрирован");
//        }
//
//        return new UsersDetails(objectMapper.convertValue(userEntity, UserModel.class));
//    }

//    public void saveUser(UserModel user) {
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        UserEntity entity = mapperBuilder.build().convertValue(user, UserEntity.class);
//        entity.setActivity(true);
//        entity.setCreatedAt(LocalDateTime.now());
//        entity.setLastUpdate(LocalDateTime.now());
//        entity.setId(UUID.randomUUID());
//        entity.setPassword(passwordEncoder.encode(user.getPassword()));
//        entity.setRole(UserRoles.USER);
//        sessionFactory.getCurrentSession().save(entity);
//    }

    @Transactional
    public UserModel getUser(String login, String password) {
        UserEntity entity;
        try {

            entity = sessionFactory.getCurrentSession().createQuery("select user from UserEntity user where user.login =:login and user.password =:password", UserEntity.class)
                    .setParameter("login", login).setParameter("password", password).stream().findFirst().get();
        } catch (Exception e) {
            return null;
        }
        return entity != null ? mapperBuilder.build().convertValue(entity, UserModel.class) : null;
    }
}
