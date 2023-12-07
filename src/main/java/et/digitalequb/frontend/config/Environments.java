package et.digitalequb.frontend.config;

import lombok.Getter;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@Getter
public class Environments {
    @Autowired
    private Environment env;

    public String getBackendUrl(){
        return env.getProperty("backend.url");
    }

}
