package org.micah.system.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @program: eladmin-cloud
 * @description:
 * @author: Micah
 * @create: 2020-08-09 21:31
 **/
class DictServiceImplTest {

    private final Set<Long> set = new HashSet<>();

    @Test
    void queryAll() {
        Set<Long> set = new HashSet<>();
        set.add(1L);
        List<Long> ids = new ArrayList<>();
        ids.add(2L);
        ids.add(3L);
        set.addAll(ids);
        System.out.println(set);
    }

    @Test
    void download() {
        set.add(1L);
        set.add(2L);
        System.out.println(new ArrayList<Long>(set));
    }

    @Test
    void create() {
    }

    @Test
    void updateDict() {
    }

    @Test
    void delete() {
    }
}