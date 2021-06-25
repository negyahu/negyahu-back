package ga.negyahu.music.agency.repository;

import static ga.negyahu.music.agency.entity.QAgency.*;
import static org.junit.jupiter.api.Assertions.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import ga.negyahu.music.agency.entity.QAgency;
import ga.negyahu.music.utils.DataJpaTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({DataJpaTestConfig.class})
public class AgencyDaoImplTest {

    @Autowired
    JPAQueryFactory query;

    @Test
    public void queryTest(){


    }

}