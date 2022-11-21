package notificationService.infrastructure.database;

import notificationService.domain.appService.ViabilityServiceModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.SessionFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class ViabilityServiceDAO {

    @Inject
    private SessionFactory sessionFactory;

    @Inject
    private Jackson2ObjectMapperBuilder mapperBuilder;

    @Transactional
    public void saveService(ViabilityServiceModel serviceModel) {
        ObjectMapper objectMapper = mapperBuilder.build();
        sessionFactory.getCurrentSession().saveOrUpdate(objectMapper.convertValue(serviceModel, ViabilityServiceEntity.class));
    }

    @Transactional
    public void updateLastUpdateService(ViabilityServiceModel serviceModel) {
        sessionFactory.getCurrentSession().createQuery("update ViabilityServiceEntity set lastUpdate=:lastUpdate where id=:id").setParameter("lastUpdate", LocalDateTime.now())
                .setParameter("id", serviceModel.getId()).executeUpdate();
    }

    @Transactional
    public List<ViabilityServiceModel> getAllViabilityService() {
        LocalDateTime res = LocalDateTime.now().minusSeconds(300);
        List<ViabilityServiceEntity> serviceEntityList = sessionFactory.getCurrentSession().createQuery("select v from ViabilityServiceEntity v " +
                "where v.lastUpdate < :res").setParameter("res", res).getResultList();
        ObjectMapper objectMapper = mapperBuilder.build();
        List<ViabilityServiceModel> serviceModelList = new ArrayList<>();
        for(ViabilityServiceEntity serviceEntity: serviceEntityList) {
            serviceModelList.add(objectMapper.convertValue(serviceEntity, ViabilityServiceModel.class));
        }
        return serviceModelList;
    }

    @Transactional
    public void deleteViabilityService(UUID id) {
        sessionFactory.getCurrentSession().createQuery("delete from ViabilityServiceEntity where id=:id").setParameter("id", id).executeUpdate();
    }
}
