package lt.vu.usecases.mybatis.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Model {

    private Integer id;
    private String firstName;
    private String lastName;
    private Integer factoryId;
    private String registrationNo;

    // Rankomis prirašyti:
    private Factory factory;
    private List<Brand> brands;
}