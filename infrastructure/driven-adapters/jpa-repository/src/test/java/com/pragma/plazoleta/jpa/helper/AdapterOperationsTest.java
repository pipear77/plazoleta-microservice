package com.pragma.plazoleta.jpa.helper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdapterOperationsTest {

    @Mock
    private DummyRepository repository;
    @Mock
    private ObjectMapper mapper;

    private DummyAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new DummyAdapter(repository, mapper, data -> new DummyEntity(data.getId()));
    }

    @Test
    void save_Success() {
        DummyEntity entity = new DummyEntity(1L);
        DummyData data = new DummyData(1L);
        when(mapper.map(entity, DummyData.class)).thenReturn(data);
        when(repository.save(data)).thenReturn(data);

        DummyEntity result = adapter.save(entity);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void saveAllEntities_Success() {
        DummyEntity entity = new DummyEntity(1L);
        DummyData data = new DummyData(1L);
        when(mapper.map(entity, DummyData.class)).thenReturn(data);
        when(repository.saveAll(any())).thenReturn(List.of(data));

        List<DummyEntity> result = adapter.saveAllEntities(List.of(entity));

        assertEquals(1, result.size());
    }

    @Test
    void findById_Success() {
        DummyData data = new DummyData(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(data));

        DummyEntity result = adapter.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void findByExample_Success() {
        DummyEntity entity = new DummyEntity(1L);
        DummyData data = new DummyData(1L);
        when(mapper.map(entity, DummyData.class)).thenReturn(data);
        when(repository.findAll(any(Example.class))).thenReturn(List.of(data));

        List<DummyEntity> result = adapter.findByExample(entity);

        assertEquals(1, result.size());
    }

    @Test
    void findAll_Success() {
        DummyData data = new DummyData(1L);
        when(repository.findAll()).thenReturn(List.of(data));

        List<DummyEntity> result = adapter.findAll();

        assertEquals(1, result.size());
    }

    static class DummyEntity {
        private final Long id;
        public DummyEntity(Long id) { this.id = id; }
        public Long getId() { return id; }
    }

    static class DummyData {
        private final Long id;
        public DummyData(Long id) { this.id = id; }
        public Long getId() { return id; }
    }

    interface DummyRepository extends CrudRepository<DummyData, Long>, QueryByExampleExecutor<DummyData> {}

    static class DummyAdapter extends AdapterOperations<DummyEntity, DummyData, Long, DummyRepository> {
        public DummyAdapter(DummyRepository repository, ObjectMapper mapper, Function<DummyData, DummyEntity> toEntityFn) {
            super(repository, mapper, toEntityFn);
        }
    }
}