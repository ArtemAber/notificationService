package notificationService.domain.appService;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class ViabilityServiceModel {

    private UUID id;

    @NotEmpty
    private String serviceName;

    @NotNull
    private LocalDateTime lastUpdate;


    public ViabilityServiceModel() {
        this.id = UUID.randomUUID();
        this.serviceName = "notificationService" + LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        this.lastUpdate = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
